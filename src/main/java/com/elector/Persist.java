package com.elector;

import com.elector.Enums.ConfigEnum;
import com.elector.Exceptions.ServiceResponseException;
import com.elector.Objects.Entities.*;
import com.elector.Objects.General.*;
import com.elector.Services.GeneralManager;
import com.elector.Utils.ConfigUtils;
import com.elector.Utils.JsonUtils;
import com.elector.Utils.ServicesApi;
import com.elector.Utils.Utils;
import com.google.common.collect.Lists;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Utils.hasText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sigal on 5/20/2016.
 */
@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    private SessionFactory sessionFactory;

    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @Autowired
    private GeneralManager generalManager;

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public List<String> getDbClasses () {
        return new ArrayList<>(sessionFactory.getAllClassMetadata().keySet());
    }

    public void saveAll(List<? extends BaseEntity> objects) {
        for (BaseEntity object : objects) {
            save(object);
        }
    }

    public void save(Object o) {
        sessionFactory.getCurrentSession().saveOrUpdate(o);
        performPostSaveCustomOperations(o);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return (T) getQuerySession().get(clazz, oid);
    }

    public <T> List<T> getList(Class<T> clazz) {
        return (List<T>) getQuerySession().createQuery(String.format("FROM %s WHERE deleted=FALSE", clazz.getSimpleName())).list();
    }

    public Object load(Class clazz, long id) {
        return getQuerySession().get(clazz, id);
    }

    public <T> List<T> loadList(Class<T> clazz) {
        return getQuerySession().createCriteria(clazz).list();
    }

    public <T> void delete(Class<T> clazz) {
        getQuerySession().createQuery(String.format("DELETE FROM %s", clazz.getSimpleName())).executeUpdate();
    }

    public <T> void setDeleted(Class<T> clazz, int oid) {
        getQuerySession().createQuery(String.format("UPDATE %s SET deleted=TRUE WHERE oid=:oid", clazz.getSimpleName())).setInteger(PARAM_OID, oid).executeUpdate();
    }


    public <T> void delete(Class<T> clazz, int oid) {
        getQuerySession().createQuery(String.format("DELETE FROM %s WHERE oid=%s", clazz.getSimpleName(), oid)).executeUpdate();
    }

    public <T> void delete(Class<T> clazz, List<Integer> oidsList) {
        getQuerySession().createQuery(String.format("DELETE FROM %s WHERE oid IN (:oidsList)", clazz.getSimpleName())).setParameterList(PARAM_OIDS_LIST, oidsList).executeUpdate();
    }


    public <T> void deleteByAdminOid(Class<T> clazz, int adminOid) {
        Query query = getQuerySession().createQuery(String.format("DELETE FROM %s WHERE adminUserObject.oid=:adminUserOid", clazz.getSimpleName()));
        query.setInteger(PARAM_ADMIN_USER_OID, adminOid);
        query.executeUpdate();
    }

    public Map<Integer, Map<Integer, ActivistVotingRate>> getActivistsVotingRate() {
        Map<Integer, Map<Integer, Integer>> activistsDataMap = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> activistsVotingRateMap = new HashMap<>();

        List<Object[]> activistsData = getQuerySession().createSQLQuery(
                "SELECT" +
                        "  avm.admin_user_oid," +
                        "  avm.activist_oid," +
                        "  COUNT(avm.activist_oid)" +
                        " FROM activists_voters_map avm" +
                        "  INNER JOIN activists a ON avm.activist_oid = a.oid" +
                        "  WHERE avm.deleted=FALSE" +
                        "  AND a.deleted=FALSE " +
                        " GROUP BY avm.activist_oid;")
                .list();
        for (Object[] data : activistsData) {
            int adminOid = Integer.valueOf(data[0].toString());
            int activistOid = Integer.valueOf(data[1].toString());
            int supportersCount = Integer.valueOf(data[2].toString());
            Map<Integer, Integer> adminData = activistsDataMap.computeIfAbsent(adminOid, k -> new HashMap<>());
            adminData.put(activistOid, supportersCount);
        }
        List<Object[]> activistsVotingData = getQuerySession().createSQLQuery(
                "SELECT " +
                        "  avm.admin_user_oid," +
                        "  avm.activist_oid," +
                        "  COUNT(avm.activist_oid) " +
                        " FROM activists_voters_map avm " +
                        "  INNER JOIN voters v ON avm.voter_oid = v.oid " +
                        " WHERE avm.deleted=FALSE " +
                        "  AND v.voted=TRUE " +
                        " GROUP BY avm.activist_oid")
                .list();
        for (Object[] data : activistsVotingData) {
            int adminOid = Integer.valueOf(data[0].toString());
            int activistOid = Integer.valueOf(data[1].toString());
            int votedCount = Integer.valueOf(data[2].toString());
            Map<Integer, Integer> adminData = activistsVotingRateMap.computeIfAbsent(adminOid, k -> new HashMap<>());
            adminData.put(activistOid, votedCount);
        }

        Map<Integer, Map<Integer, ActivistVotingRate>> activistsVotingRateObjectsMap = new HashMap<>();
        for (Integer adminOid : activistsDataMap.keySet()) {
            Map<Integer, Integer> activistsDataForAdmin = activistsDataMap.get(adminOid);
            if (activistsDataForAdmin != null) {
                for (Integer activistOid : activistsDataForAdmin.keySet()) {
                    Integer supportersCount = activistsDataForAdmin.get(activistOid);
                    if (supportersCount != null) {
                        Map<Integer, Integer> activistsVotingRateForAdmin = activistsVotingRateMap.get(adminOid);
                        int votedSupporters = 0;
                        if (activistsVotingRateForAdmin != null) {
                            Integer votedCount = activistsVotingRateForAdmin.get(activistOid);
                            if (votedCount != null) {
                                votedSupporters = votedCount;
                            }
                        }
                        Map<Integer, ActivistVotingRate> adminActivistsVotingRateObjects = activistsVotingRateObjectsMap.computeIfAbsent(adminOid, k -> new HashMap<>());
                        adminActivistsVotingRateObjects.put(activistOid, new ActivistVotingRate(activistOid, activistsDataMap.get(adminOid).get(activistOid), votedSupporters));
                    }
                }
            }
        }
        return activistsVotingRateObjectsMap;

    }

    public List<ObserverBallotBoxMapObject> getObserversBallotBoxesMapForBallotBox(int adminOid, int ballotBoxOid) {
        return getQuerySession().createQuery("FROM ObserverBallotBoxMapObject o WHERE o.adminUserObject.oid=:adminOid AND o.ballotBoxObject.oid=:ballotBoxOid AND o.deleted=FALSE AND o.observerObject.deleted=FALSE")
                .setInteger(PARAM_ADMIN_OID, adminOid).setInteger(PARAM_BALLOT_BOX_OID, ballotBoxOid)
                .list();
    }

    public Map<Integer, Integer> votersSupportStatusMap() {
        Map<Integer, Integer> votersSupportStatusMap = new HashMap<>();
        boolean error = false;
        List<Object[]> votersData = (getQuerySession().createSQLQuery("SELECT v.oid, v.support_status FROM voters v WHERE v.deleted=FALSE").list());
        for (Object[] data : votersData) {
            if (data.length == 2) {
                votersSupportStatusMap.put(Integer.valueOf(data[0].toString()), Integer.valueOf(data[1].toString()));
            } else {
                error = true;
            }
        }
        if (error) {
            LOGGER.warn("votersSupportStatusMap, some data was not retrieved");
        }
        return votersSupportStatusMap;
    }

    public void removeActivistVoterMapObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE ActivistVoterMapObject a SET deleted=TRUE WHERE a.voter.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeCallerVoterMapObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE CallerVoterMapObject c SET deleted=TRUE WHERE c.voterObject.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeVoterCustomGroupMappingObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE VoterCustomGroupMappingObject v SET deleted=TRUE WHERE v.voterObject.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeVoterElectionDayCallObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE VoterElectionDayCallObject v SET deleted=TRUE WHERE v.voterObject.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeDriveObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE DriveObject d SET deleted=TRUE WHERE d.voterObject.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public void removeSupporterBirthdayObjects(List<Integer> oidsList) {
        getQuerySession().createQuery("UPDATE SupporterBirthdayObject s SET deleted=TRUE WHERE s.supporter.oid IN(:oidsList)")
                .setParameterList(PARAM_OIDS_LIST, oidsList)
                .executeUpdate();
    }

    public List<VoterObject> getVotersByAdmin(int adminUserOid, int type) {
        List<VoterObject> voters = new ArrayList<>();
        try {
            String supportFilter = EMPTY;
            if (type != PARAM_SUPPORT_STATUS_ALL) {
                supportFilter = String.format(" AND supportStatus=%s", type);
            }
            Query query = getQuerySession().createQuery(String.format("FROM VoterObject WHERE adminUserObject.oid=:adminUserOid %s AND deleted=FALSE", supportFilter));
            query.setCacheable(true);
            query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid);
            voters = query.list();
        } catch (Exception e) {
            LOGGER.error("getVotersByAdmin", String.format("error fetching voters, adminOid: %s, type: %s", adminUserOid, true), e);
        }
        LOGGER.info("Return {} voters", voters.size());
        return voters;

    }

    public <T> List<T> getListByAdminOid(Class<T> clazz, int adminOid, Integer limit, boolean reverse) {
        Query query = getQuerySession().createQuery(
                String.format("FROM %s WHERE adminUserObject.oid=:adminUserOid AND deleted=FALSE %s",
                        clazz.getSimpleName(),
                        reverse ? " ORDER BY oid DESC" : EMPTY));
        query.setInteger(PARAM_ADMIN_USER_OID, adminOid);
        if (limit != null) {
            query.setMaxResults(limit);
        }
        return (List<T>) query.list();
    }

    public Map<Integer, Integer> calculateCandidateSupportStats(int adminOid) {
        Map<Integer, Integer> stats = new HashMap<>();
        Integer unknown = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM voters WHERE admin_user_oid=:adminUserOid AND support_status=:supportStatus AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_UNKNOWN).setInteger(PARAM_ADMIN_USER_OID, adminOid).uniqueResult()).intValue();
        Integer supporting = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM voters WHERE admin_user_oid=:adminUserOid AND support_status=:supportStatus AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_SUPPORTING).setInteger(PARAM_ADMIN_USER_OID, adminOid).uniqueResult()).intValue();
        Integer notSupporting = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM voters WHERE admin_user_oid=:adminUserOid AND support_status=:supportStatus AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_NOT_SUPPORTING).setInteger(PARAM_ADMIN_USER_OID, adminOid).uniqueResult()).intValue();
        Integer unverifiedSupporting = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM voters WHERE admin_user_oid=:adminUserOid AND support_status=:supportStatus AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING).setInteger(PARAM_ADMIN_USER_OID, adminOid).uniqueResult()).intValue();
        stats.put(PARAM_SUPPORT_STATUS_UNKNOWN, unknown);
        stats.put(PARAM_SUPPORT_STATUS_SUPPORTING, supporting);
        stats.put(PARAM_SUPPORT_STATUS_NOT_SUPPORTING, notSupporting);
        stats.put(PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING, unverifiedSupporting);
        return stats;
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>>  getActivistsDataMap (boolean today) {
        Map<Integer, LinkedHashMap<Integer, Integer>> activistsDataMap = new LinkedHashMap<>();
        List<Object[]> rawData = (getQuerySession().createSQLQuery(
                String.format("SELECT " +
                        "  a.admin_user_oid, " +
                        "  activist_oid, " +
                        "  COUNT(activist_oid) " +
                        "FROM activists a " +
                        "INNER JOIN activists_voters_map avm ON a.oid = avm.activist_oid " +
                        "INNER JOIN admin_users au ON a.admin_user_oid = au.oid " +
                        " WHERE avm.deleted=FALSE AND au.deleted=FALSE AND au.active=TRUE %s" +
                        "GROUP BY activist_oid;", today ? " AND avm.date > TIMESTAMP(current_date) " : EMPTY)).list());
        for (Object[] data : rawData) {
            int adminOid = Integer.valueOf(data[0].toString());
            int activistOid = Integer.valueOf(data[1].toString());
            int supportersCount = Integer.valueOf(data[2].toString());
            LinkedHashMap<Integer, Integer> activists = activistsDataMap.computeIfAbsent(adminOid, k -> new LinkedHashMap<>());
            activists.put(activistOid, supportersCount);
        }
        return activistsDataMap;
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>>  activistsDataFromYesterday () {
        Map<Integer, LinkedHashMap<Integer, Integer>> activistsDataMap = new LinkedHashMap<>();
        List<Object[]> rawData = (getQuerySession().createSQLQuery("SELECT " +
                "  admin_user_oid, " +
                "  activist_oid, " +
                "  COUNT(activist_oid) " +
                "FROM activists_voters_map avm " +
                "WHERE DATE(date) = DATE(NOW() - INTERVAL 1 DAY) AND avm.deleted = FALSE " +
                "GROUP BY activist_oid;").list());
        for (Object[] data : rawData) {
            int adminOid = Integer.valueOf(data[0].toString());
            int activistOid = Integer.valueOf(data[1].toString());
            int supportersCount = Integer.valueOf(data[2].toString());
            LinkedHashMap<Integer, Integer> activists = activistsDataMap.computeIfAbsent(adminOid, k -> new LinkedHashMap<>());
            activists.put(activistOid, supportersCount);
        }
        return activistsDataMap;
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>>  activistsSupportStats (boolean onlyYesterday) {
        Map<Integer, LinkedHashMap<Integer, Integer>> activistsDataMap = new LinkedHashMap<>();
        List<Object[]> rawData = (getQuerySession().createSQLQuery(
                String.format(
                        "SELECT " +
                                "  a.admin_user_oid, " +
                                "  activist_oid, " +
                                "  COUNT(activist_oid) " +
                                "FROM activists_voters_map avm " +
                                "  INNER JOIN activists a ON avm.activist_oid = a.oid " +
                                "WHERE " +
                                " avm.deleted = FALSE " +
                                " AND a.deleted = FALSE " +
                                " %s " +
                                "GROUP BY activist_oid;", onlyYesterday ? " AND DATE(date) = DATE(NOW() - INTERVAL 1 DAY) " : EMPTY
                )).list());
        for (Object[] data : rawData) {
            int adminOid = Integer.valueOf(data[0].toString());
            int activistOid = Integer.valueOf(data[1].toString());
            int supportersCount = Integer.valueOf(data[2].toString());
            LinkedHashMap<Integer, Integer> activists = activistsDataMap.computeIfAbsent(adminOid, k -> new LinkedHashMap<>());
            activists.put(activistOid, supportersCount);
        }
        return activistsDataMap;
    }

    public List<VoterObject> getVotersThatNeedRide() {
        return (List<VoterObject>) getQuerySession().createQuery("FROM VoterObject v WHERE v.needsRide=1").list();
    }

    public List<ActivistDataDailyObject> getHighestPerformanceActivistsYesterday () {
        return (List<ActivistDataDailyObject>) getQuerySession().createQuery(
                "FROM ActivistDataDailyObject a " +
                        "WHERE a.dailyRank=1 AND a.date=TIMESTAMP(current_date) AND a.activistObject.mobileRegistrationToken IS NOT NULL").list();
    }

    public Long countClients(int campaignOid) {
        return ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(oid) FROM admin_users a WHERE a.campaign_oid=:campaignOid AND a.deleted=FALSE")
                .setInteger(PARAM_CAMPAIGN_OID, campaignOid)
                .uniqueResult()).longValue();
    }

    public ContactsGroupObject getContactsGroupByNameInService(String nameInService) {
        return (ContactsGroupObject) getQuerySession().createQuery("FROM ContactsGroupObject WHERE nameInService=:nameInService AND deleted=FALSE")
                .setString(PARAM_NAME_IN_SERVICE, nameInService).uniqueResult();
    }

    public List<VoterObject> getVotersWithPhoneNumbers (int adminOid, int supportStatus) {
        String supportFilter = EMPTY;
        if (supportStatus != PARAM_SUPPORT_STATUS_ALL) {
            supportFilter = String.format(" AND supportStatus=%s", supportStatus);
        }
        return (List<VoterObject>) getQuerySession().createQuery(
                String.format("FROM VoterObject v " +
                        "WHERE v.adminUserObject.oid=:adminOid AND (v.phone<>'' OR v.homePhone<>'' OR v.extraPhone<>'') %s ", supportFilter)).
                setInteger(PARAM_ADMIN_OID, adminOid).list();
    }

    public <T> void clearCache(Class<T> clazz, int oid) {
        T object = sessionFactory.getCurrentSession().get(clazz, oid);
        sessionFactory.getCache().evictEntity(clazz, oid);
        sessionFactory.getCurrentSession().evict(object);
    }

    public List<VoterObject> getSupportersWithNoActivists(int adminUserOid) {
        List<VoterObject> supporters = getQuerySession().createQuery("FROM VoterObject WHERE adminUserObject.oid=:adminUserOid AND supportStatus=:supportStatus AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_SUPPORTING).setInteger(PARAM_ADMIN_USER_OID, adminUserOid).list();
        if (!supporters.isEmpty()) {
            List<ActivistVoterMapObject> activistVoterMapObjects = getQuerySession().createQuery("FROM ActivistVoterMapObject WHERE adminUserObject.oid=:adminUserOid AND deleted=FALSE").setInteger(PARAM_ADMIN_USER_OID, adminUserOid).list();
            if (!activistVoterMapObjects.isEmpty()) {
                for (ActivistVoterMapObject activistVoterMapObject : activistVoterMapObjects) {
                    VoterObject supporter = activistVoterMapObject.getVoter();
                    if (supporters.contains(supporter)) {
                        supporters.remove(activistVoterMapObject.getVoter());
                    }
                }
            }
        }
        return supporters;
    }

    public List<VoterCallObject> getCallsByAdminOid(int adminUserOid, String startDate, String endDate, boolean onlyWithMessages) {
        String dateFilter = EMPTY;
        String messagesFilter = EMPTY;
        if (hasText(startDate)) {
            dateFilter = dateFilter + String.format(" AND time >= '%s 00:00:00' ", startDate);
        }
        if (hasText(endDate)) {
            dateFilter = dateFilter + String.format(" AND time <= '%s 23:59:59' ", endDate);
        }
        if (onlyWithMessages) {
            messagesFilter = " AND c.comment IS NOT NULL AND LENGTH(c.comment) > 1";
        }
        Query query = getQuerySession().createQuery(String.format("FROM VoterCallObject c WHERE c.callerObject.adminUserObject.oid=:adminUserOid AND deleted=FALSE %s %s ORDER BY time ASC", dateFilter, messagesFilter));
        query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid);
        return (List<VoterCallObject>) query.list();

    }

    public List<VoterObject> getNotVotedSupporters(int adminUserOid) {
        Query query = getQuerySession().createQuery("FROM VoterObject v WHERE v.voted=FALSE AND v.adminUserObject.oid=:adminUserOid AND supportStatus=:supportStatus AND allowRecall=TRUE AND deleted=FALSE")
                .setInteger(PARAM_SUPPORT_STATUS, PARAM_SUPPORT_STATUS_SUPPORTING);
        query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid);
        return (List<VoterObject>) query.list();
    }

    public List<ActivistVoterMapObject> getActivistsVotersMapObjectsByAdminUserOid(int adminUserOid) {
        return (List<ActivistVoterMapObject>) getQuerySession().createQuery("FROM ActivistVoterMapObject a " +
                "WHERE a.voter.adminUserObject.oid=:adminUserOid " +
                " AND a.activist.deleted=FALSE AND a.voter.deleted=FALSE " +
                "AND a.deleted=FALSE").
                setInteger(PARAM_ADMIN_USER_OID, adminUserOid).
                list();
    }

    public List<VoterObject> getNotVotedSupportersWithNoActivist(int adminUserOid) {
        return (List<VoterObject>) getQuerySession().createQuery("FROM VoterObject v WHERE v.oid NOT IN (SELECT voter.oid FROM ActivistVoterMapObject a WHERE a.adminUserObject.oid=:adminUserOid) AND deleted=FALSE").
                setInteger(PARAM_ADMIN_USER_OID, adminUserOid).
                list();
    }

    public Long countActivistSupporters(int activistOid) {
        return ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(oid) FROM activists_voters_map a WHERE a.activist_oid=:activistOid AND a.deleted=FALSE")
                .setInteger(PARAM_ACTIVIST_OID, activistOid)
                .uniqueResult()).longValue();
    }

    public boolean isActivistManager(int activistOid) {
        return ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(oid) FROM activists_masters_slaves_map a WHERE a.master_oid=:activistOid")
                .setInteger(PARAM_ACTIVIST_OID, activistOid).setMaxResults(1)
                .uniqueResult()).longValue() > 0;
    }

    public long countSlavesSupporters(int activistOid) {
        return ((BigInteger) getQuerySession().createSQLQuery(
                "SELECT COUNT(avm.oid) " +
                        "FROM activists_masters_slaves_map amsm " +
                        "INNER JOIN activists_voters_map avm " +
                        "ON slave_oid=activist_oid " +
                        "WHERE master_oid=:activistOid " +
                        "AND amsm.deleted=FALSE " +
                        "AND avm.deleted=FALSE;")
                .setInteger(PARAM_ACTIVIST_OID, activistOid)
                .uniqueResult()).longValue();
    }

    public List<ActivistVoterMapObject> getActivistVoterMapByActivistOid(int activistOid) {
        return (List<ActivistVoterMapObject>) getQuerySession().createQuery(String.format("FROM ActivistVoterMapObject WHERE activist.oid=%s AND deleted=FALSE", activistOid)).list();
    }

    public List<SupporterActionObject> getOpenSupporterActions(int adminUserOid, Integer limit, boolean onlyOpen, Integer type) {
        List<SupporterActionObject> actions = new ArrayList<>();
        final String openTasksFilter = " AND s.done=FALSE";
        Query query = null;
        if (type == null || type == SUPPORTER_ACTION_TYPE_BILLBOARD) {
            query = getQuerySession().createQuery(
                    String.format("FROM SupporterBillboardRequestObject s " +
                                    "WHERE s.supporter.adminUserObject=:adminUserOid AND deleted=FALSE %s ORDER BY creationDate DESC",
                            onlyOpen ? openTasksFilter : EMPTY)).
                    setInteger(PARAM_ADMIN_USER_OID, adminUserOid);
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<SupporterBillboardRequestObject> billboardRequests = query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid).list();
            actions.addAll(billboardRequests);
        }

        if (type == null || type == SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY) {
            query = getQuerySession().createQuery(
                    String.format("FROM SupporterElectionAssemblyRequestObject s " +
                                    "WHERE s.supporter.adminUserObject=:adminUserOid AND deleted=FALSE %s ORDER BY creationDate DESC",
                            onlyOpen ? openTasksFilter : EMPTY));
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<SupporterElectionAssemblyRequestObject> electionAssemblyRequests = query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid).list();
            actions.addAll(electionAssemblyRequests);

        }

        if (type == null || type == SUPPORTER_ACTION_TYPE_VOLUNTEER) {
            query = getQuerySession().createQuery(
                    String.format("FROM SupporterVolunteerObject s " +
                                    "WHERE s.supporter.adminUserObject=:adminUserOid AND deleted=FALSE %s ORDER BY creationDate DESC",
                            onlyOpen ? openTasksFilter : EMPTY));
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<SupporterVolunteerObject> volunteers = query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid).list();
            actions.addAll(volunteers);
        }

        if (type == null || type == SUPPORTER_ACTION_TYPE_SUPPORTERS_LIST) {
            query = getQuerySession().createQuery(
                    String.format("FROM SupporterSupportersListDelivery s " +
                                    "WHERE s.supporter.adminUserObject=:adminUserOid AND deleted=FALSE %s ORDER BY creationDate DESC",
                            onlyOpen ? openTasksFilter : EMPTY));
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<SupporterSupportersListDelivery> supportersLists = query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid).list();
            actions.addAll(supportersLists);
        }

        if (type == null || type == SUPPORTER_ACTION_TYPE_CAR_STICKER) {
            query = getQuerySession().createQuery(
                    String.format("FROM SupporterCarStickerObject s " +
                                    "WHERE s.supporter.adminUserObject=:adminUserOid AND deleted=FALSE %s ORDER BY creationDate DESC",
                            onlyOpen ? openTasksFilter : EMPTY));
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<SupporterCarStickerObject> catStickerObjects = query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid).list();
            actions.addAll(catStickerObjects);
        }

        if (type == null || type == SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY_PARTICIPATION) {
            query = getQuerySession().createQuery(
                    String.format("FROM SupporterElectionAssemblyParticipationObject s " +
                                    "WHERE s.supporter.adminUserObject=:adminUserOid AND deleted=FALSE %s ORDER BY creationDate DESC",
                            onlyOpen ? openTasksFilter : EMPTY));
            if (limit != null) {
                query.setMaxResults(limit);
            }
            List<SupporterElectionAssemblyParticipationObject> supporterElectionAssemblyParticipationObjects = query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid).list();
            actions.addAll(supporterElectionAssemblyParticipationObjects);
        }

        return actions;
    }

    public List<BallotBoxObject>  getBallotBoxesByCities(List<Integer> citiesOids) {
        return getQuerySession().createQuery("FROM BallotBoxObject b WHERE b.cityObject.oid IN (:citiesOids) AND b.deleted=FALSE")
                .setParameterList(PARAM_CITIES_OIDS, citiesOids)
                .list();
    }

    public List<CityObject> getCitiesByCampaign (int campaignOid) {
        return getQuerySession().createQuery("SELECT c.cityObject FROM CampaignCityMapObject c WHERE c.campaignObject.oid=:campaignOid")
                .setInteger(PARAM_CAMPAIGN_OID, campaignOid)
                .list();

    }

    public List<BallotBoxObject>  getBallotBoxesByObserver(int observerOid) {
        return getQuerySession().createQuery("SELECT ballotBoxObject FROM ObserverBallotBoxMapObject o WHERE o.observerObject.oid=:observerOid AND o.deleted=FALSE")
                .setInteger(PARAM_OBSERVER_OID, observerOid)
                .list();
    }

    public List<ActivistVoterMapObject> getActivistsVotersMapObjectsForNotVotedVotersByAdminUserOid(int adminUserOid) {
        return (List<ActivistVoterMapObject>) getQuerySession().createQuery("FROM ActivistVoterMapObject a " +
                "WHERE a.voter.adminUserObject.oid=:adminUserOid " +
                " AND a.activist.deleted=FALSE AND a.voter.deleted=FALSE " +
                "AND a.deleted=FALSE AND (a.voter.voted IS NULL OR a.voter.voted=FALSE)").
                setInteger(PARAM_ADMIN_USER_OID, adminUserOid).
                list();
    }

    public List<VoterElectionDayCallObject> getElectionDayCallsByAdminOid(int adminUserOid) {
        Query query = getQuerySession().createQuery(
                "FROM VoterElectionDayCallObject v WHERE v.callerObject.adminUserObject.oid=:adminUserOid AND v.deleted=FALSE ORDER BY time ASC");
        query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid);
        return (List<VoterElectionDayCallObject>) query.list();
    }

    public List<ActivistMasterSlaveMapObject> getAdminMasterSlaveMap(int managerOid) {
        return getQuerySession().createQuery("FROM ActivistMasterSlaveMapObject a WHERE a.deleted=FALSE AND a.master.deleted=FALSE AND a.master.groupManagerObject IS NOT NULL AND a.master.groupManagerObject.oid=:managerOid")
                .setInteger(PARAM_MANAGER_OID, managerOid)
                .list();
    }

    public Map<String, String> slavesStats(int activistOid, List<Integer> oids) {
        Map<String, String> stats = new HashMap<>();
        Long ownSupporters = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(voter_oid) FROM activists_voters_map WHERE activist_oid=:activistOid AND deleted=FALSE")
                .setInteger(PARAM_ACTIVIST_OID, activistOid).uniqueResult()).longValue();
        stats.put(PARAM_OWN_SUPPORTERS_COUNT, String.valueOf(ownSupporters));
        Long totalSupporters = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(voter_oid) FROM activists_voters_map WHERE activist_oid IN (:oidsList) AND deleted=FALSE")
                .setParameterList(PARAM_OIDS_LIST, oids).uniqueResult()).longValue();
        stats.put(PARAM_SUPPORTERS_COUNT, String.valueOf(totalSupporters));
        oids.add(activistOid);
        Long uniqueSupporters = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(DISTINCT(voter_oid)) FROM activists_voters_map WHERE activist_oid IN (:oidsList) AND deleted=FALSE")
                .setParameterList(PARAM_OIDS_LIST, oids).uniqueResult()).longValue();
        stats.put(PARAM_UNIQUE_SUPPORTERS_COUNT, String.valueOf(uniqueSupporters));
        Long uniqueVotedSupporters = ((BigInteger) getQuerySession().createSQLQuery(
                "SELECT COUNT(DISTINCT (voter_oid)) " +
                        "FROM activists_voters_map avm INNER JOIN voters v ON avm.voter_oid = v.oid " +
                        "WHERE avm.activist_oid IN (:oidsList) AND avm.deleted = FALSE AND voted=TRUE;"
        )
                .setParameterList(PARAM_OIDS_LIST, oids).uniqueResult()).longValue();
        stats.put(PARAM_UNIQUE_VOTED_SUPPORTERS_COUNT, String.valueOf(uniqueVotedSupporters));
        return stats;
    }


    public Map<Integer, Integer> getActivistsVotersCountMap(List<Integer> activistsOids) {
        Map<Integer, Integer> activistsVotersCountMap = new HashMap<>();
        List<Object[]> callersData = getQuerySession().createSQLQuery(
                "SELECT " +
                        "  avm.activist_oid, " +
                        "  COUNT(activist_oid) " +
                        "FROM activists_voters_map avm " +
                        "WHERE avm.deleted = FALSE AND activist_oid IN(:oidsList)" +
                        "GROUP BY avm.activist_oid")
                .setParameterList(PARAM_OIDS_LIST, activistsOids)
                .list();
        for (Object[] data : callersData) {
            if (data.length == 2) {
                activistsVotersCountMap.put(Integer.valueOf(data[0].toString()), Integer.valueOf(data[1].toString()));
            } else {
                activistsVotersCountMap = null;
                break;
            }
        }

        return activistsVotersCountMap;

    }

    public Map<Integer, Set<String>> getVotersActivistsMap(List<Integer> activistsOids) {
        Map<Integer, Set<String>> votersActivistsMap = new HashMap<>();
        List<Object[]> callersData = getQuerySession().createSQLQuery(
                "SELECT " +
                        "  voter_oid, " +
                        "  CONCAT(a.first_name, ' ', a.last_name) " +
                        "FROM activists_voters_map avm " +
                        "  INNER JOIN activists a ON avm.activist_oid = a.oid " +
                        "WHERE activist_oid IN (:oidsList) AND avm.deleted = FALSE AND a.deleted=FALSE;")
                .setParameterList(PARAM_OIDS_LIST, activistsOids)
                .list();
        for (Object[] data : callersData) {
            if (data.length == 2) {
                Integer voterOid = Integer.valueOf(data[0].toString());
                String activistName = data[1].toString();
                Set<String> activists = votersActivistsMap.computeIfAbsent(voterOid, k -> new HashSet<>());
                activists.add(activistName);
            } else {
                votersActivistsMap = null;
                break;
            }
        }
        return votersActivistsMap;

    }

    public List<Integer> getBallotBoxesOidsByBlock(int blockOid) {
        Query query = getQuerySession().createQuery("SELECT b.ballotBoxObject.oid FROM BallotBoxBlockMapObject b WHERE b.ballotBoxBlockObject.oid=:blockOid AND b.deleted=FALSE");
        query.setInteger(PARAM_BLOCK_OID, blockOid);
        return (List<Integer>) query.list();
    }

    public Map<Integer, Map<Integer, Set<String>>> getActiveActivistsVotersMap() {
        Query query = getQuerySession().createQuery(
                "FROM ActivistVoterMapObject avm " +
                        "WHERE avm.deleted=FALSE AND " +
                        "avm.voter.deleted=FALSE AND " +
                        "avm.activist.deleted=FALSE AND " +
                        "avm.adminUserObject.deleted=FALSE AND " +
                        "avm.adminUserObject.active=TRUE");
        List<ActivistVoterMapObject> activistVoterMapObjects = query.list();
        Map<Integer, Map<Integer, Set<String>>> votersActivistsMap = new HashMap<>();
        for (ActivistVoterMapObject activistVoterMapObject : activistVoterMapObjects) {
            int adminOid = activistVoterMapObject.getAdminUserObject().getOid();
            int voterOid = activistVoterMapObject.getVoter().getOid();
            ActivistObject activistObject = activistVoterMapObject.getActivist();
            Map<Integer, Set<String>> adminVotersActivistsMap = votersActivistsMap.computeIfAbsent(adminOid, k -> new HashMap<>());
            Set<String> activists = adminVotersActivistsMap.computeIfAbsent(voterOid, k -> new HashSet<>());
            activists.add(activistObject.getFullName());
        }
        return votersActivistsMap;
    }

    public List<PublicInquiryObject> getOpenPublicInquiries (int adminOid) {
        Query query = getQuerySession().createQuery(
                "FROM PublicInquiryObject p " +
                        "WHERE p.contactObject.adminUserObject.oid=:adminOid " +
                        "AND open=TRUE " +
                        "AND p.deleted=FALSE " +
                        "ORDER BY p.oid DESC");
        query.setInteger(PARAM_ADMIN_OID, adminOid);
        return (List<PublicInquiryObject>) query.list();
    }

    public Map<Integer, Integer> getPublicInquiriesByTopics(int adminOid) {
        Map<Integer, Integer> inquiriesTopicsMap = new HashMap<>();
        List<Object[]> inquiriesData = getQuerySession().createSQLQuery(
                String.format("SELECT public_inquiry_topic_oid, COUNT(public_inquiry_topic_oid) " +
                        "FROM public_inquiries pi " +
                        "INNER JOIN voters v on pi.voter_oid = v.oid " +
                        "WHERE admin_user_oid=%s " +
                        "GROUP BY public_inquiry_topic_oid;", adminOid))
                .list();
        for (Object[] data : inquiriesData) {
            if (data.length == 2) {
                inquiriesTopicsMap.put(Integer.valueOf(data[0].toString()), Integer.valueOf(data[1].toString()));
            } else {
                inquiriesTopicsMap = null;
                break;
            }
        }
        return inquiriesTopicsMap;
    }

    public LandingPageDataObject getLandingPageDataObjectByName(String name) {
        return (LandingPageDataObject) getQuerySession().createQuery(
                "FROM LandingPageDataObject l " +
                        "WHERE l.nameInUrl=:name AND l.deleted=FALSE")
                .setString(PARAM_NAME, name).uniqueResult();
    }


    public List<Integer> getGroupVotersOids(int groupOid) {
        Query query = getQuerySession().createQuery(
                "SELECT v.voterObject.oid " +
                        "FROM VoterCustomGroupMappingObject v " +
                        "WHERE v.customGroupObject.oid=:oid " +
                        "AND v.deleted=FALSE " +
                        "AND v.voterObject.deleted=FALSE " +
                        "ORDER BY v.voterObject.oid");
        query.setInteger(PARAM_OID, groupOid);
        return (List<Integer>) query.list();

    }

    public List<Integer> getVoterGroups (int voterOid) {
        Query query = getQuerySession().createQuery(
                "SELECT v.customGroupObject.oid " +
                        "FROM VoterCustomGroupMappingObject v " +
                        "WHERE v.voterObject.oid=:voterOid " +
                        "AND v.deleted=FALSE " +
                        "AND v.customGroupObject.deleted=FALSE " +
                        "AND v.voterObject.deleted=FALSE");
        query.setInteger(PARAM_VOTER_OID, voterOid);
        return (List<Integer>) query.list();
    }

    public VoterCustomGroupMappingObject isVoterMemberInGroup (int voterOid, int groupOid) {
        return (VoterCustomGroupMappingObject)getQuerySession().createQuery(
                "FROM VoterCustomGroupMappingObject v " +
                        "WHERE v.customGroupObject.oid=:groupOid " +
                        "AND v.voterObject.oid=:voterOid " +
                        "AND v.deleted=FALSE")
                .setInteger(PARAM_VOTER_OID, voterOid).setInteger(PARAM_GROUP_OID, groupOid).uniqueResult();
    }

    public boolean isNameAvailable (String name, int oid) {
        return getQuerySession().createQuery(
                "FROM LandingPageDataObject l " +
                        "WHERE l.nameInUrl=:name " +
                        "AND l.deleted=FALSE AND oid <> :oid")
                .setString(PARAM_NAME, name).setInteger(PARAM_OID, oid).list().isEmpty();
    }


    public List<Integer> getVotersOidsByActivistOid(int activistOid) {
        return (List<Integer>)
                getQuerySession().
                        createQuery(
                                "SELECT a.voter.oid FROM ActivistVoterMapObject a WHERE a.activist.oid=:activistOid AND a.deleted=FALSE")
                        .setInteger(PARAM_ACTIVIST_OID, activistOid)
                        .list();
    }

    public List<Integer> getGroupsVotersOids(List<Integer> oidsList) {
        Query query = getQuerySession().createQuery(
                "SELECT v.voterObject.oid " +
                        "FROM VoterCustomGroupMappingObject v " +
                        "WHERE v.customGroupObject.oid IN(:oidsList) " +
                        "AND v.deleted=FALSE " +
                        "AND v.voterObject.deleted=FALSE " +
                        "ORDER BY v.voterObject.oid");
        query.setParameterList(PARAM_OIDS_LIST, oidsList);
        return (List<Integer>) query.list();

    }

    public List<Integer> getCustomGroupsOidsForCaller (int callerOid) {
        Query query = getQuerySession().createQuery(
                "SELECT c.customGroupObject.oid FROM CallerCustomGroupMapObject c WHERE c.callerObject.oid=:callerOid AND c.deleted=FALSE");
        query.setInteger(PARAM_CALLER_OID, callerOid);
        return (List<Integer>) query.list();
    }

    public void removeAllCustomGroupFromCaller (int callerOid) {
        Query query = getQuerySession().createSQLQuery(
                "UPDATE callers_custom_groups_map c SET c.deleted=TRUE WHERE c.caller_oid=:callerOid");
        query.setInteger(PARAM_CALLER_OID, callerOid).executeUpdate();
    }

    public int countVotersInGroup (int groupOid) {
        return  ((BigInteger) getQuerySession().createSQLQuery(
                "SELECT COUNT(*) " +
                        "FROM voters_custom_groups_mapping vcgm " +
                        "INNER JOIN custom_groups cg on vcgm.custom_group_oid = cg.oid " +
                        "INNER JOIN voters v on vcgm.voter_oid = v.oid " +
                        "WHERE cg.oid=:groupOid AND v.deleted=FALSE AND vcgm.deleted=FALSE")
                .setInteger(PARAM_GROUP_OID, groupOid).uniqueResult()).intValue();
    }

    public List<String> getVotersIds (int oid, boolean voters) {
        Query query = null;
        if (voters) {
            query = getQuerySession().createQuery("SELECT v.voterId FROM VoterObject v WHERE v.adminUserObject.oid=:oid AND deleted=FALSE").setInteger(PARAM_OID, oid);
        } else {
            query = getQuerySession().createQuery("SELECT v.voterId FROM CampaignVoterObject v WHERE v.campaignObject.oid=:oid AND deleted=FALSE").setInteger(PARAM_OID, oid);
        }
        return query.list();
    }

    public List<String> getCampaignVotersIds (CampaignObject campaignObject) {
        return getQuerySession().createQuery(
                String.format("SELECT voterId FROM %s o WHERE o.campaignObject.oid=%s AND o.deleted=FALSE",
                        campaignObject.getSubClassName(), campaignObject.getOid())).list();
    }

    public VoterObject getVoterById(int adminUserOid, String voterId) {
        long startTime = System.currentTimeMillis();
        Query query = getQuerySession().createQuery("FROM VoterObject WHERE adminUserObject.oid=:adminUserOid AND voterId=:voterId AND deleted=FALSE");
        query.setInteger(PARAM_ADMIN_USER_OID, adminUserOid).setString(PARAM_VOTER_ID, voterId);
        VoterObject voter = (VoterObject) query.uniqueResult();
        LOGGER.debug("getVoterById took {} ms", System.currentTimeMillis() - startTime);
        return voter;
    }

    public Map<String, Integer> votersIdsMap(int adminOid) {
        Map<String, Integer> votersIdsMap = new HashMap<>();
        List<Object[]> votersData =
                (getQuerySession().createSQLQuery("SELECT v.oid, v.voter_id FROM voters v WHERE v.admin_user_oid=:adminOid AND v.deleted=FALSE")
                        .setInteger(PARAM_ADMIN_OID, adminOid)
                        .list());
        for (Object[] data : votersData) {
            String voterId = data[1].toString();
            int oid = Integer.valueOf(data[0].toString());
            votersIdsMap.put(voterId, oid);
            votersIdsMap.put(Utils.addLeadingZerosToVoterId(voterId), oid);
            votersIdsMap.put(Utils.eliminateLeadingZeros(voterId), oid);
        }
        return votersIdsMap;
    }

    public void updateSupportStatus (int voterOid, int supportStatus) {
        Query query = getQuerySession().createQuery("UPDATE VoterObject v SET v.supportStatus=:supportStatus WHERE v.oid=:oid");
        query.setInteger(PARAM_SUPPORT_STATUS, supportStatus).setInteger(PARAM_OID, voterOid).executeUpdate();
    }

    public ActivistVoterMapObject getActivistVoterMap(int activistOid, int voterOid) {
        return (ActivistVoterMapObject) getQuerySession().createQuery(
                "FROM ActivistVoterMapObject a WHERE a.activist.oid=:activistOid AND a.voter.oid=:voterOid AND a.deleted=FALSE")
                .setInteger(PARAM_ACTIVIST_OID, activistOid)
                .setInteger(PARAM_VOTER_OID, voterOid)
                .setMaxResults(1)
                .uniqueResult();
    }

    public void setVoted (int voterOid) {
        Query query = getQuerySession().createQuery("UPDATE VoterObject v SET v.voted=TRUE WHERE v.oid=:oid");
        query.setInteger(PARAM_OID, voterOid).executeUpdate();
    }

    public List<GroupManagerObject> getManagersForDynamicGroup(int groupOid) {
        return (List<GroupManagerObject>) getQuerySession().createQuery(
                "SELECT groupManagerObject FROM GroupManagerDynamicGroupMapObject g " +
                        "WHERE g.dynamicGroupObject.oid=:groupOid " +
                        "AND g.deleted=FALSE " +
                        "AND g.groupManagerObject.deleted=FALSE")
                .setInteger(PARAM_GROUP_OID, groupOid)
                .list();
    }

    public List<DynamicGroupObject> getDynamicGroupsForManager (int managerOid) {
        return (List<DynamicGroupObject>) getQuerySession().createQuery(
                "SELECT dynamicGroupObject FROM GroupManagerDynamicGroupMapObject g " +
                        "WHERE g.groupManagerObject.oid=:managerOid " +
                        "AND g.deleted=FALSE " +
                        "AND g.dynamicGroupObject.deleted=FALSE")
                .setInteger(PARAM_MANAGER_OID, managerOid)
                .list();
    }

    public void setGroupManagerDynamicGroupDeleted (int managerOid, int groupOid, int adminOid) {
        getQuerySession().createQuery(
                "UPDATE GroupManagerDynamicGroupMapObject g " +
                        "SET deleted=TRUE " +
                        "WHERE groupManagerObject.oid=:managerOid " +
                        "AND g.dynamicGroupObject.oid=:groupOid " +
                        "AND g.adminUserObject.oid=:adminOid ")
                .setInteger(PARAM_MANAGER_OID, managerOid)
                .setInteger(PARAM_GROUP_OID, groupOid)
                .setInteger(PARAM_ADMIN_OID, adminOid)
                .executeUpdate();
    }

    public Map<String, String> countUsers (int managerOid) {
        Map<String, String> stats = new HashMap<>();
        Long activists = ((BigInteger) getQuerySession().createSQLQuery(
                "SELECT COUNT(*) FROM activists WHERE group_manager_oid=:managerOid AND deleted=FALSE")
                .setInteger(PARAM_MANAGER_OID, managerOid)
                .uniqueResult()).longValue();
        stats.put(PARAM_ACTIVISTS_COUNT, String.valueOf(activists));
        Long callers = ((BigInteger) getQuerySession().createSQLQuery(
                "SELECT COUNT(*) FROM callers WHERE group_manager_oid=:managerOid AND deleted=FALSE")
                .setInteger(PARAM_MANAGER_OID, managerOid)
                .uniqueResult()).longValue();
        stats.put(PARAM_CALLERS_COUNT, String.valueOf(callers));
        Long observers = ((BigInteger) getQuerySession().createSQLQuery(
                "SELECT COUNT(*) FROM observers WHERE group_manager_oid=:managerOid AND deleted=FALSE")
                .setInteger(PARAM_MANAGER_OID, managerOid)
                .uniqueResult()).longValue();
        stats.put(PARAM_OBSERVERS_COUNT, String.valueOf(observers));
        Long drivers = ((BigInteger) getQuerySession().createSQLQuery(
                "SELECT COUNT(*) FROM drivers WHERE group_manager_oid=:managerOid AND deleted=FALSE")
                .setInteger(PARAM_MANAGER_OID, managerOid)
                .uniqueResult()).longValue();
        stats.put(PARAM_DRIVERS_COUNT, String.valueOf(drivers));
        return stats;
    }

    public boolean isPhoneNumberExist (String phone) {
        boolean exists = ((BigInteger) getQuerySession().createSQLQuery(
                "SELECT COUNT(*) FROM activists a WHERE a.phone=:phone AND a.deleted=FALSE")
                .setString(PARAM_PHONE, phone)
                .uniqueResult()).longValue() > 0;
        if (!exists) {
            exists = ((BigInteger) getQuerySession().createSQLQuery(
                    "SELECT COUNT(*) FROM callers a WHERE a.phone=:phone AND a.deleted=FALSE")
                    .setString(PARAM_PHONE, phone)
                    .uniqueResult()).longValue() > 0;
        }
        if (!exists) {
            exists = ((BigInteger) getQuerySession().createSQLQuery(
                    "SELECT COUNT(*) FROM observers a WHERE a.phone=:phone AND a.deleted=FALSE")
                    .setString(PARAM_PHONE, phone)
                    .uniqueResult()).longValue() > 0;
        }
        if (!exists) {
            exists = ((BigInteger) getQuerySession().createSQLQuery(
                    "SELECT COUNT(*) FROM drivers a WHERE a.phone=:phone AND a.deleted=FALSE")
                    .setString(PARAM_PHONE, phone)
                    .uniqueResult()).longValue() > 0;
        }
        if (!exists) {
            exists = ((BigInteger) getQuerySession().createSQLQuery(
                    "SELECT COUNT(*) FROM groups_managers a WHERE a.phone=:phone AND a.deleted=FALSE")
                    .setString(PARAM_PHONE, phone)
                    .uniqueResult()).longValue() > 0;
        }
        if (!exists) {
            exists = ((BigInteger) getQuerySession().createSQLQuery(
                    "SELECT COUNT(*) FROM admin_users a WHERE a.phone=:phone AND a.deleted=FALSE")
                    .setString(PARAM_PHONE, phone)
                    .uniqueResult()).longValue() > 0;
        }
        return exists;
    }

    public <T> List<T> getListByManagerOid(Class<T> clazz, int managerOid, Integer limit, boolean reverse) {
        Query query = getQuerySession().createQuery(
                String.format("FROM %s WHERE groupManagerObject.oid=:managerOid AND deleted=FALSE %s",
                        clazz.getSimpleName(),
                        reverse ? " ORDER BY oid DESC" : EMPTY));
        query.setInteger(PARAM_MANAGER_OID, managerOid);
        if (limit != null) {
            query.setMaxResults(limit);
        }
        return (List<T>) query.list();
    }

    public Map<Integer, Integer> countActivistsSupporters(int managerOid, boolean includeDeletedVoters) {
        Map<Integer, Integer> activistsSupportersMap = new HashMap<>();
        String sql = includeDeletedVoters ? "SELECT a.activist_oid, COUNT(a.oid) " +
                "FROM activists_voters_map a " +
                "INNER JOIN voters v on a.voter_oid = v.oid " +
                "INNER JOIN activists a2 on a.activist_oid = a2.oid " +
                "WHERE a2.group_manager_oid = :managerOid " +
                "AND a.deleted = FALSE " +
                "AND v.deleted = FALSE " +
                "GROUP BY activist_oid; "
                :
                "SELECT a.activist_oid, COUNT(a.oid) " +
                        "FROM activists_voters_map a " +
                        "INNER JOIN activists a2 on a.activist_oid = a2.oid " +
                        "WHERE a2.group_manager_oid = :group_manager_oid " +
                        "AND a.deleted = FALSE " +
                        "GROUP BY activist_oid; ";
        List<Object[]> activistsData = getQuerySession().createSQLQuery(
                sql)
                .setInteger(PARAM_MANAGER_OID, managerOid)
                .list();
        for (Object[] data : activistsData) {
            if (data.length == 2) {
                Integer activistOid = Integer.valueOf(data[0].toString());
                Integer supportersCount = Integer.valueOf(data[1].toString());
                activistsSupportersMap.put(activistOid, supportersCount);
            }
        }
        return activistsSupportersMap;
    }

    public Map<Integer, Integer> countVotedVotersForActivistsOids (List<Integer> oids) {
        Map<Integer, Integer> activistsOidsToTotalVotedVoters = new HashMap<>();
        List<Object[]> votedByActivistsData = getQuerySession().createSQLQuery(
                "SELECT " +
                        "  avm.activist_oid, " +
                        "  COUNT(DISTINCT (voter_oid)) " +
                        "FROM activists_voters_map avm INNER JOIN voters v ON avm.voter_oid = v.oid " +
                        "WHERE avm.activist_oid IN (:oidsList) AND avm.deleted = FALSE AND voted = TRUE " +
                        "GROUP BY avm.activist_oid;"
        ).setParameterList(PARAM_OIDS_LIST, oids).list();
        for (Object[] data : votedByActivistsData) {
            if (data.length == 2) {
                activistsOidsToTotalVotedVoters.put(Integer.valueOf(data[0].toString()), Integer.valueOf(data[1].toString()));
            }
        }
        return activistsOidsToTotalVotedVoters;
    }

    public Map<String, Integer> getActivistsTotalVotersMap(int managerOid, Integer limit) {
        final int FIRST_NAME = 0;
        final int LAST_NAME = 1;
        final int SUPPORTERS = 2;
        Map<String, Integer> activistsSupportersMap = new LinkedHashMap<>();
        Query query = getQuerySession().createQuery("SELECT m.activist.firstName, m.activist.lastName, COUNT(m.voter.oid) " +
                "FROM ActivistVoterMapObject m " +
                "WHERE m.activist.groupManagerObject.oid=:managerOid AND m.deleted=FALSE " +
                "GROUP BY m.activist.oid ORDER BY COUNT(m.voter.oid) DESC");
        if (limit != null) {
            query.setMaxResults(limit);
        }
        query.setInteger(PARAM_MANAGER_OID, managerOid);
        List list = query.list();
        for (Object item : list) {
            Object[] obj = (Object[]) item;
            if (obj.length == 3) {
                activistsSupportersMap.put(obj[FIRST_NAME] + " " + obj[LAST_NAME], Integer.valueOf(obj[SUPPORTERS].toString()));
            }
        }
        return activistsSupportersMap;
    }

    public Map<Date, Integer> getCallsCountByDates(int managerOid, int limit) {
        Map<Date, Integer> map = new LinkedHashMap<>();
        try {
            List<Object> list = ((List<Object>) getQuerySession().createSQLQuery(
                    "SELECT DATE(time) as date, COUNT(*) AS count " +
                            "FROM voters_calls vs " +
                            "INNER JOIN callers c " +
                            "ON vs.caller_oid = c.oid " +
                            "WHERE c.deleted=FALSE AND c.group_manager_oid=:managerOid " +
                            "GROUP BY DATE(time) " +
                            "ORDER BY DATE(time) DESC")
                    .setInteger(PARAM_MANAGER_OID, managerOid)
                    .setMaxResults(limit)
                    .list());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            list = Lists.reverse(list);
            for (Object o : list) {
                Object[] data = (Object[]) o;
                map.put(simpleDateFormat.parse(String.valueOf(data[0])), Integer.valueOf(String.valueOf(data[1])));
            }
        } catch (ParseException e) {
            LOGGER.error(String.format("cannot parse date for voters calls, managerOid; %s", managerOid), e);
        }
        return map;

    }

    public Long countVotersCalls(int managerOid) {
        return ((BigInteger) getQuerySession().createSQLQuery(
                "SELECT COUNT(*) FROM voters_calls vc " +
                        "INNER JOIN callers c ON vc.caller_oid=c.oid " +
                        "WHERE c.group_manager_oid=:managerOid")
                .setInteger(PARAM_MANAGER_OID, managerOid)
                .uniqueResult()).longValue();
    }

    private void performPostSaveCustomOperations(Object object) {
//        if (object instanceof VoterObject && !(object instanceof UnverifiedVoterObject)) {
//            new Thread(() -> {
//                try {
//                    VoterObject voterObject = (VoterObject)object;
//                    if (!voterObject.isCopyVotersBook()) {
//                        Map<String, Object> params = new HashMap<>();
//                        params.put(PARAM_ADMIN_OID, voterObject.getAdminUserObject().getOid());
//                        params.put(PARAM_DATA, JsonUtils.voterEditableFieldsToJson(voterObject));
//                        Boolean phonesChanged = ServicesApi.requestFromVotersImMemoryService("update-voter", params, PARAM_PHONES_CHANGED);
//                        if (!ConfigUtils.getConfig(ConfigEnum.force_election_day_functionality, true)) {
//                            if (phonesChanged) {
//                                params.clear();
//                                params.put(PARAM_TYPE, ENTITY_VOTER);
//                                params.put(PARAM_OID, ((VoterObject) object).getOid());
//                                ServicesApi.requestFromSmsService("add-contact-to-sync-queue", params, null);
//                            }
//
//                        }
//                    }
//                } catch (ServiceResponseException e) {
//                    LOGGER.error("Service response null", e);
//                }
//            }).start();
//        }
    }

    public boolean isVoterIdExistsForCandidate(int adminUserOid, String voterId) {
        return !getQuerySession().createQuery("FROM VoterObject WHERE adminUserObject.oid=:adminUserOid AND voterId=:voterId AND deleted=FALSE")
                .setInteger(PARAM_ADMIN_USER_OID, adminUserOid).setString(PARAM_VOTER_ID, voterId).list().isEmpty();
    }

    public VoterCustomPropertyObject getVoterCustomPropertyObject(int voterOid, int customPropertyOid) {
        Query query = getQuerySession().createQuery("FROM VoterCustomPropertyObject v WHERE v.voterObject.oid=:voterOid AND v.customPropertyObject.oid=:customPropertyOid AND v.deleted=FALSE");
        query.setInteger(PARAM_VOTER_OID, voterOid).setInteger(PARAM_CUSTOM_PROPERTY_OID, customPropertyOid).setMaxResults(1);
        return (VoterCustomPropertyObject) query.uniqueResult();
    }

    public List<ActivistObject> getActivistsForVoter(int voterOid) {
        return (List<ActivistObject>) getQuerySession().createQuery(
                "SELECT activist FROM ActivistVoterMapObject o " +
                        "WHERE o.voter.oid=:voterOid AND " +
                        "o.deleted=FALSE AND " +
                        "o.voter.deleted=FALSE").setInteger(PARAM_VOTER_OID, voterOid).list();
    }

    public Map<Integer, Integer> countSupportersForActivistsOids (List<Integer> oids) {
        Map<Integer, Integer> activistsOidsToSupportersCount = new HashMap<>();
        List<Object[]> votedByActivistsData = getQuerySession().createSQLQuery(
                "SELECT " +
                        "  avm.activist_oid, " +
                        "  COUNT(DISTINCT (voter_oid)) " +
                        "FROM activists_voters_map avm INNER JOIN voters v ON avm.voter_oid = v.oid " +
                        "WHERE avm.activist_oid IN (:oidsList) AND avm.deleted = FALSE AND v.deleted=FALSE " +
                        "GROUP BY avm.activist_oid;"
        ).setParameterList(PARAM_OIDS_LIST, oids).list();
        for (Object[] data : votedByActivistsData) {
            if (data.length == 2) {
                activistsOidsToSupportersCount.put(Integer.valueOf(data[0].toString()), Integer.valueOf(data[1].toString()));
            }
        }
        return activistsOidsToSupportersCount;
    }

    public ActivistVoterMapObject getActivistVoterMapObject(int voterOid, int activistOid) {
        Query query = getQuerySession().createQuery("FROM ActivistVoterMapObject a WHERE a.voter.oid=:voterOid AND a.activist.oid=:activistOid AND a.deleted=FALSE");
        query.setInteger(PARAM_VOTER_OID, voterOid).setInteger(PARAM_VOTER_OID, voterOid).setInteger(PARAM_ACTIVIST_OID, activistOid).setMaxResults(1);
        return (ActivistVoterMapObject) query.uniqueResult();
    }

    public List<SupporterActionObject> getAllTasks() {
        List<SupporterActionObject> actions = new ArrayList<>();
        actions.addAll(getQuerySession().createQuery("FROM SupporterBillboardRequestObject s WHERE deleted=FALSE").list());
        actions.addAll(getQuerySession().createQuery("FROM SupporterElectionAssemblyRequestObject s WHERE deleted=FALSE").list());
        actions.addAll(getQuerySession().createQuery("FROM SupporterVolunteerObject s WHERE deleted=FALSE").list());
        actions.addAll(getQuerySession().createQuery("FROM SupporterSupportersListDelivery s WHERE deleted=FALSE").list());
        actions.addAll(getQuerySession().createQuery("FROM SupporterCarStickerObject s WHERE deleted=FALSE").list());
        actions.addAll(getQuerySession().createQuery("FROM SupporterElectionAssemblyParticipationObject s WHERE deleted=FALSE").list());
        return actions;
    }


    public List<SupporterActionObject> getVoterPendingTasks(int voterOid) {
        List<SupporterActionObject> actions = new ArrayList<>();
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterBillboardRequestObject s " +
                        "WHERE s.deleted=FALSE AND " +
                        "s.supporter.oid = :voterOid " +
                        "AND s.done=FALSE " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_VOTER_OID, voterOid)
                .setMaxResults(1)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterElectionAssemblyRequestObject s " +
                        "WHERE s.deleted=FALSE " +
                        "AND s.supporter.oid = :voterOid " +
                        "AND s.done=FALSE " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_VOTER_OID, voterOid)
                .setMaxResults(1)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterVolunteerObject s " +
                        "WHERE s.deleted=FALSE AND " +
                        "s.supporter.oid = :voterOid " +
                        "AND s.done=FALSE " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_VOTER_OID, voterOid)
                .setMaxResults(1)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterSupportersListDelivery s " +
                        "WHERE s.deleted=FALSE " +
                        "AND s.supporter.oid = :voterOid " +
                        "AND s.done=FALSE " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_VOTER_OID, voterOid)
                .setMaxResults(1)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterCarStickerObject s " +
                        "WHERE s.deleted=FALSE AND " +
                        "s.supporter.oid = :voterOid " +
                        "AND s.done=FALSE " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_VOTER_OID, voterOid)
                .setMaxResults(1)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterElectionAssemblyParticipationObject s " +
                        "WHERE s.deleted=FALSE AND " +
                        "s.supporter.oid = :voterOid AND " +
                        " s.done=FALSE " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_VOTER_OID, voterOid)
                .setMaxResults(1)
                .list());
        return actions;
    }

    public void removeTaskFromVoter(Class clazz, int voterOid) {
        getQuerySession().createQuery(String.format("" +
                "UPDATE %s o SET deleted=TRUE " +
                "WHERE o.supporter.oid=:voterOid", clazz.getSimpleName()))
                .setInteger(PARAM_VOTER_OID, voterOid)
                .executeUpdate();
    }

    public List<ActivistMasterSlaveMapObject> getMasterSlaveMap(int masterOid) {
        return getQuerySession().createQuery(
                "FROM ActivistMasterSlaveMapObject a " +
                        "WHERE a.deleted=FALSE " +
                        "AND a.master.oid=:masterOid")
                .setInteger(PARAM_MASTER_OID, masterOid)
                .list();
    }

    public Long getCallerCallsCount(int callerOid) {
        return ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM callers_voters_map c WHERE caller_oid=:callerOid AND deleted=FALSE")
                .setInteger(PARAM_CALLER_OID, callerOid).uniqueResult()).longValue();
    }

    public List<VoterCallObject> getCallsByCallerOid(int callerOid) {
        Query query = getQuerySession().createQuery("FROM VoterCallObject WHERE callerObject.oid=:callerOid AND deleted=FALSE ORDER BY time DESC");
        query.setInteger(PARAM_CALLER_OID, callerOid);
        return (List<VoterCallObject>) query.list();

    }

    public List<SupporterActionObject> getAdminTasks(int adminOid) {
        List<SupporterActionObject> actions = new ArrayList<>();
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterBillboardRequestObject s " +
                        "WHERE s.deleted=FALSE " +
                        "AND s.supporter.adminUserObject.oid = :adminOid " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_ADMIN_OID, adminOid)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterElectionAssemblyRequestObject s " +
                        "WHERE s.deleted=FALSE " +
                        "AND s.supporter.adminUserObject.oid = :adminOid " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_ADMIN_OID, adminOid)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterVolunteerObject s " +
                        "WHERE s.deleted=FALSE " +
                        "AND s.supporter.adminUserObject.oid = :adminOid " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_ADMIN_OID, adminOid)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterSupportersListDelivery s " +
                        "WHERE s.deleted=FALSE " +
                        "AND s.supporter.adminUserObject.oid = :adminOid " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_ADMIN_OID, adminOid)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterCarStickerObject s " +
                        "WHERE s.deleted=FALSE " +
                        "AND s.supporter.adminUserObject.oid = :adminOid " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_ADMIN_OID, adminOid)
                .list());
        actions.addAll(getQuerySession().createQuery(
                "FROM SupporterElectionAssemblyParticipationObject s " +
                        "WHERE s.deleted=FALSE " +
                        "AND s.supporter.adminUserObject.oid = :adminOid " +
                        "ORDER BY s.oid DESC")
                .setInteger(PARAM_ADMIN_OID, adminOid)
                .list());
        return actions;
    }

    public Map<String, String> calculateManagerSystemStats(int managerOid) {
        Map<String, String> stats = new HashMap<>();
        Long activists = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM activists WHERE group_manager_oid =:managerOid AND deleted=FALSE").setInteger(PARAM_MANAGER_OID, managerOid).uniqueResult()).longValue();
        stats.put(PARAM_ACTIVISTS_COUNT, String.valueOf(activists));
        Long callers = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM callers WHERE group_manager_oid =:managerOid AND deleted=FALSE").setInteger(PARAM_MANAGER_OID, managerOid).uniqueResult()).longValue();
        stats.put(PARAM_CALLERS_COUNT, String.valueOf(callers));
        Long observers = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM observers WHERE group_manager_oid =:managerOid AND deleted=FALSE").setInteger(PARAM_MANAGER_OID, managerOid).uniqueResult()).longValue();
        stats.put(PARAM_OBSERVERS_COUNT, String.valueOf(observers));
        Long drivers = ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM drivers WHERE group_manager_oid =:managerOid AND deleted=FALSE").setInteger(PARAM_MANAGER_OID, managerOid).uniqueResult()).longValue();
        stats.put(PARAM_DRIVERS_COUNT, String.valueOf(drivers));
        return stats;
    }

    public List<VoterCallObject> getVoterCallsByManagerOid(int managerOid, Integer limit) {
        Query query = getQuerySession().createQuery("FROM VoterCallObject v WHERE v.callerObject.groupManagerObject.oid=:managerOid AND deleted=FALSE");
        query.setInteger(PARAM_MANAGER_OID, managerOid);
        if (limit != null) {
            query.setMaxResults(limit);
        }
        return (List<VoterCallObject>) query.list();
    }

    public Long countActivistsForVoter (int voterOid) {
        return ((BigInteger) getQuerySession().createSQLQuery("SELECT COUNT(*) FROM activists_voters_map avm WHERE avm.voter_oid =:voterOid AND avm.deleted=FALSE")
                .setInteger(PARAM_VOTER_OID, voterOid).uniqueResult()).longValue();
    }

    public List<VoterCustomPropertyObject> getCustomPropertiesForVoter (int voterOid) {
        return ((List<VoterCustomPropertyObject>) getQuerySession().createQuery("FROM VoterCustomPropertyObject v WHERE v.voterObject.oid = :voterOid AND v.deleted=FALSE")
                .setInteger(PARAM_VOTER_OID, voterOid).list());
    }

    public List<CityObject> getAdminCities (int adminOid) {
        return ((List<CityObject>) getQuerySession().createQuery(
                "SELECT a.cityObject FROM AdminCityMapObject a " +
                "WHERE a.adminUserObject.oid = :adminOid " +
                "AND a.deleted=FALSE")
                .setInteger(PARAM_ADMIN_OID, adminOid).list());
    }

    public LinkedHashMap<Integer, ActivistObject>  getActivistsDataMap (int adminOid, Integer managerOid) {
        LinkedHashMap<Integer, ActivistObject> activistsDataMap = new LinkedHashMap<>();
        String ownerUserFilter = managerOid == null ? "AND a.admin_user_oid = :userOid " : "AND a.group_manager_oid = :userOid ";
        List<Object[]> rawData = (getQuerySession().createSQLQuery(
                String.format("SELECT avm.activist_oid, a.first_name, a.last_name, COUNT(avm.activist_oid) " +
                        "FROM activists a " +
                        "INNER JOIN activists_voters_map avm ON a.oid = avm.activist_oid " +
                        "WHERE avm.deleted = FALSE " +
                        "%s " +
                        "AND avm.date > TIMESTAMP(current_date) " +
                        "GROUP BY activist_oid ORDER BY COUNT(avm.activist_oid) DESC;", ownerUserFilter))
                .setInteger(PARAM_USER_OID, managerOid == null ? adminOid : managerOid)
                .list());
        for (Object[] data : rawData) {
            int activistOid = Integer.valueOf(data[0].toString());
            String firstName = data[1].toString();
            String lastName = data[2].toString();
            Long supportersCount = Long.valueOf(data[3].toString());
            activistsDataMap.put(activistOid, new ActivistObject(activistOid, firstName, lastName, supportersCount));
        }
        return activistsDataMap;
    }

    public Map<Integer, Set<VoterObject>> loadVotersSuggestionsData() {
        Map<Integer, Set<VoterObject>> votersMap = new HashMap<>();
        List<Integer> activeAdmins = getQuerySession().createSQLQuery(
                "SELECT oid FROM admin_users au WHERE au.deleted=FALSE AND au.active=TRUE").list();
        for (Integer adminOid : activeAdmins) {
            Set<VoterObject> voterObjects = new HashSet<>();
            List<Object[]> votersData = getQuerySession().createSQLQuery(
                    "SELECT v.oid, v.first_name, v.last_name, v.voter_id, v.address, cv.city_oid " +
                            "FROM voters v INNER JOIN campaign_voters cv ON v.campaign_voter_oid = cv.oid " +
                            "WHERE v.admin_user_oid = :adminOid AND v.deleted=FALSE AND cv.deleted=FALSE")
                    .setInteger(PARAM_ADMIN_OID, adminOid)
                    .list();
            for (Object[] data : votersData) {
                VoterObject voterObject = new VoterObject();
                voterObject.setOid(Integer.valueOf(data[0].toString()));
                voterObject.setFirstName(data[1].toString());
                voterObject.setLastName(data[2].toString());
                voterObject.setVoterId(Utils.eliminateLeadingZeros(data[3].toString()));
                voterObject.setAddress(data[4].toString());
                if (data[5] != null) {
                    voterObject.setCityOid(Integer.valueOf(data[5].toString()));
                }
                voterObjects.add(voterObject);
            }
            votersMap.put(adminOid, voterObjects);
        }
        return votersMap;

    }

    public List<VoterObject> getActivistVoters (int activistOid) {
        return ((List<VoterObject>) getQuerySession().createQuery(
                "SELECT a.voter FROM ActivistVoterMapObject a " +
                        "WHERE a.activist.oid = :activistOid " +
                        "AND a.deleted=FALSE AND a.voter.deleted=FALSE")
                .setInteger(PARAM_ACTIVIST_OID, activistOid).list());
    }

    public Set<AbstractMap.SimpleEntry> getActivistVotersOids (int activistOid) {
        List<Object[]> data = ((List<Object[]>) getQuerySession().createQuery(
                "SELECT a.voter.oid, a.relation FROM ActivistVoterMapObject a " +
                        "WHERE a.activist.oid = :activistOid " +
                        "AND a.deleted=FALSE AND a.voter.deleted=FALSE")
                .setInteger(PARAM_ACTIVIST_OID, activistOid).list());
        Set<AbstractMap.SimpleEntry> votersData = new HashSet<>();
        for (Object[] item : data) {
            votersData.add(new AbstractMap.SimpleEntry(item[0], item[1]));
        }
        return votersData;
    }

    public boolean canObserverGetPollingStationVoters (int observerOid, int ballotBoxOid) {
        return getQuerySession().createQuery("FROM ObserverBallotBoxMapObject o WHERE o.observerObject.oid=:observerOid AND o.ballotBoxObject.oid=:ballotBoxOid AND o.deleted=FALSE")
                .setInteger(PARAM_OBSERVER_OID, observerOid).setInteger(PARAM_BALLOT_BOX_OID, ballotBoxOid).setMaxResults(1)
                .uniqueResult() != null;
    }

    public List<VotingStatusChangeObject> getObserverVotingLog(List<Integer> oids) {
        return getQuerySession().createQuery(
                "FROM VotingStatusChangeObject v WHERE v.initiatorType=5 AND v.initiatorOid IN(:oidsList) AND v.deleted=FALSE ORDER BY oid DESC"
        ).setParameterList(PARAM_OIDS_LIST, oids).setMaxResults(ConfigUtils.getConfig(ConfigEnum.max_results_observers_logs, 1000)).list();
    }

    public List<ObserverCheckInObject> getObserverCheckIns(List<Integer> oids) {
        return getQuerySession().createQuery(
                "FROM ObserverCheckInObject o WHERE o.observerObject.oid IN(:oidsList) AND o.deleted=FALSE"
        ).setParameterList(PARAM_OIDS_LIST, oids).list();
    }

    public List<ElectionDayAlert> getObserverAlerts(List<Integer> oids) {
        return getQuerySession().createQuery(
                "FROM ElectionDayAlert e WHERE e.senderType=5 AND e.senderOid IN(:oidsList) AND e.deleted=FALSE"
        ).setParameterList(PARAM_OIDS_LIST, oids).list();
    }

    public List<VoterObject> getVotersForPollingStations (int adminOid, int pollingStationOid) {
        return ((List<VoterObject>) getQuerySession().createQuery(
                "FROM VoterObject v " +
                        "WHERE v.campaignVoterObject.ballotBoxObject.oid = :pollingStationOid " +
                        "AND v.adminUserObject.oid = :adminOid " +
                        "AND v.deleted = FALSE")
                .setInteger(PARAM_ADMIN_OID, adminOid)
                .setInteger(PARAM_POLLING_STATION_OID, pollingStationOid)
                .list());
    }

    public boolean canObserverEditVoter (int voterOid, int observerOid) {
        List<Object[]> rawData = getQuerySession().createSQLQuery(
                "SELECT cv.ballot_box_oid " +
                        "FROM voters v INNER JOIN campaign_voters cv " +
                        "ON v.campaign_voter_oid = cv.oid " +
                        "WHERE v.oid = :voterOid " +
                        "AND cv.ballot_box_oid IN " +
                        "(SELECT ballot_box_oid " +
                        "FROM observers_ballot_boxes_map " +
                        "WHERE observer_oid = :observerOid);")
                .setInteger(PARAM_VOTER_OID, voterOid)
                .setInteger(PARAM_OBSERVER_OID, observerOid)
                .list();
        return !rawData.isEmpty();
    }

    public List<BallotBoxObject> getAdminPollingStations (int adminOid) {
        return ((List<BallotBoxObject>) getQuerySession().createQuery(
                "FROM BallotBoxObject b " +
                        "WHERE b.oid IN (SELECT DISTINCT(a.ballotBoxObject.oid) FROM AdminPollingStationStatsObject a WHERE a.adminUserObject.oid = :adminOid) " +
                        "AND b.deleted = FALSE")
                .setInteger(PARAM_ADMIN_OID, adminOid).setCacheable(true)
                .list());
    }

    public List<ObserverBallotBoxMapObject> getObserversBallotBoxesMapsByBallotBoxes(List<Integer> ballotBoxesOids, int adminOid) {
        return getQuerySession().createQuery("FROM ObserverBallotBoxMapObject o WHERE o.ballotBoxObject.oid IN(:oidsList) AND adminUserObject.oid=:adminUserOid AND o.deleted=FALSE")
                .setParameterList(PARAM_OIDS_LIST, ballotBoxesOids)
                .setInteger(PARAM_ADMIN_USER_OID, adminOid)
                .list();
    }


    public AdminPollingStationStatsObject getLastPollingStationStatsByPollingStationOid(int adminOid, int pollingStationOid) {
        return ((AdminPollingStationStatsObject) getQuerySession().createQuery(
                "FROM AdminPollingStationStatsObject a " +
                        "WHERE a.adminUserObject.oid =:adminOid " +
                        "AND a.deleted=FALSE " +
                        "AND a.ballotBoxObject.oid =:pollingStationOid " +
                        "ORDER BY a.oid DESC")
                .setInteger(PARAM_ADMIN_OID, adminOid)
                .setInteger(PARAM_POLLING_STATION_OID, pollingStationOid)
                .setMaxResults(1)
                .uniqueResult());
    }

    public List<ObserverObject> getObserversForBallotBox (int ballotBoxOid, int adminOid) {
        return getQuerySession().createQuery(
                "SELECT observerObject FROM ObserverBallotBoxMapObject o " +
                        "WHERE o.ballotBoxObject.oid=:ballotBoxOid AND o.adminUserObject.oid=:adminUserOid AND o.deleted=FALSE")
                .setInteger(PARAM_BALLOT_BOX_OID, ballotBoxOid)
                .setInteger(PARAM_ADMIN_USER_OID, adminOid)
                .list();
    }

    public Map<Integer, Set<AdminPollingStationStatsObject>> getMostRecentPollingStationsStats () {
        Map<Integer, Set<AdminPollingStationStatsObject>> adminPollingStationsStatsMap = new HashMap<>();
        List<Object[]> rawData = getQuerySession().createSQLQuery(
                "SELECT MAX(apss.oid), " +
                        "apss.statsTime, " +
                        "apss.admin_user_oid, " +
                        "apss.total_voters, " +
                        "apss.total_supporters, " +
                        "apss.total_unverified_supporters, " +
                        "apss.total_unknown_support_status, " +
                        "apss.total_not_supporting, " +
                        "apss.total_voted, " +
                        "apss.total_supporters_voted, " +
                        "bb.oid, " +
                        "bb.number, " +
                        "bb.address, " +
                        "bb.place, " +
                        "bb.disabled_access, " +
                        "c.name, c.oid AS city_oid " +
                        "FROM admins_polling_stations_stats apss " +
                        "INNER JOIN ballot_boxes bb on apss.ballot_box_oid = bb.oid " +
                        "INNER JOIN admin_users au on apss.admin_user_oid = au.oid " +
                        "INNER JOIN cities c on bb.city_oid = c.oid " +
                        "WHERE au.active = TRUE " +
                        "AND au.deleted = FALSE " +
//                        "AND bb.deleted = FALSE " +
                        "AND apss.deleted = FALSE " +
                        "GROUP BY apss.ballot_box_oid, apss.admin_user_oid;")
                .list();
        for (Object[] row : rawData) {
            try {
                Date statsTime = new Date(((Timestamp)row[1]).getTime());
                int adminOid = Integer.valueOf(row[2].toString());
                int totalVoters = Integer.valueOf(row[3].toString());
                int totalSupporters = Integer.valueOf(row[4].toString());
                int totalUnverifiedSupporters = Integer.valueOf(row[5].toString());
                int totalUnknownSupportStatus = Integer.valueOf(row[6].toString());
                int totalNotSupporting = Integer.valueOf(row[7].toString());
                int totalVoted = Integer.valueOf(row[8].toString());
                int totalSupportersVoted = Integer.valueOf(row[9].toString());
                int pollingStationOid = Integer.valueOf(row[10].toString());
                int pollingStationNumber = Integer.valueOf(row[11].toString());
                String pollingStationAddress = row[12] != null ? row[12].toString() : EMPTY;
                String pollingStationPlace =  row[13] != null ? row[13].toString() : EMPTY;
                boolean pollingStationDisabledAccess = row[14] != null && Boolean.valueOf(row[14].toString());
                String cityName = row[15].toString();
                int cityOid = Integer.valueOf(row[16].toString());
                CityObject cityObject = new CityObject(cityOid);
                cityObject.setName(cityName);
                BallotBoxObject ballotBoxObject = new BallotBoxObject();
                ballotBoxObject.setAddress(pollingStationAddress);
                ballotBoxObject.setPlace(pollingStationPlace);
                ballotBoxObject.setCityObject(cityObject);
                ballotBoxObject.setDisabledAccess(pollingStationDisabledAccess);
                ballotBoxObject.setOid(pollingStationOid);
                ballotBoxObject.setNumber(pollingStationNumber);
                AdminPollingStationStatsObject  adminPollingStationStatsObject = new AdminPollingStationStatsObject();
                adminPollingStationStatsObject.setBallotBoxObject(ballotBoxObject);
                adminPollingStationStatsObject.setStatsTime(statsTime);
                adminPollingStationStatsObject.setAdminUserObject(new AdminUserObject(adminOid));
                adminPollingStationStatsObject.setTotalVoters(totalVoters);
                adminPollingStationStatsObject.setTotalSupporters(totalSupporters);
                adminPollingStationStatsObject.setTotalUnverifiedSupporters(totalUnverifiedSupporters);
                adminPollingStationStatsObject.setTotalUnknownSupportStatus(totalUnknownSupportStatus);
                adminPollingStationStatsObject.setTotalNotSupporting(totalNotSupporting);
                adminPollingStationStatsObject.setTotalVoted(totalVoted);
                adminPollingStationStatsObject.setTotalSupportersVoted(totalSupportersVoted);
                Set<AdminPollingStationStatsObject> adminPollingStationStatsObjects = adminPollingStationsStatsMap.computeIfAbsent(adminOid, k -> new HashSet<>());
                adminPollingStationStatsObjects.add(adminPollingStationStatsObject);
            } catch (Exception e) {
                LOGGER.error("error parsing db data for pollingStationOid:{}", Integer.valueOf(row[10].toString()), e);
            }
        }
        return adminPollingStationsStatsMap;

    }

    public List<Integer> getPollingStationsWithCheckedInObservers (int adminOid) {
        return getQuerySession().createQuery(
                "SELECT o.ballotBoxObject.oid FROM ObserverBallotBoxMapObject o " +
                        "WHERE o.adminUserObject.oid=:adminUserOid AND o.deleted=FALSE AND o.checkedIn = TRUE")
                .setInteger(PARAM_ADMIN_USER_OID, adminOid)
                .list();
    }

    public Map<Integer, Set<Integer>> getPollingStationsThatHasObserversMap () {
        Map<Integer, Set<Integer>> pollingStationsThatHasObserversMap = new HashMap<>();
        List<Object[]> votersData = (getQuerySession().createSQLQuery(
                "SELECT DISTINCT ballot_box_oid, admin_user_oid " +
                        "FROM observers_ballot_boxes_map obbm " +
                        "INNER JOIN admin_users a on obbm.admin_user_oid = a.oid " +
                        "WHERE obbm.deleted = FALSE AND a.active = TRUE AND a.deleted = FALSE;").list());
        for (Object[] data : votersData) {
            int pollingStationOid = Integer.valueOf(data[0].toString());
            int adminOid = Integer.valueOf(data[1].toString());
            Set<Integer> pollingStationsOids = pollingStationsThatHasObserversMap.computeIfAbsent(adminOid, k -> new HashSet<>());
            pollingStationsOids.add(pollingStationOid);
        }
        return pollingStationsThatHasObserversMap;
    }


}