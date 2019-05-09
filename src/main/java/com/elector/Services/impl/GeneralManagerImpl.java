package com.elector.Services.impl;

import com.elector.Exceptions.ServiceResponseException;
import com.elector.Objects.Entities.*;
import com.elector.Objects.General.*;
import com.elector.Persist;
import com.elector.Services.GeneralManager;
import com.elector.Utils.JsonUtils;
import com.elector.Utils.ServicesApi;
import com.elector.Utils.Utils;
import com.google.common.collect.Lists;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Utils.hasText;

/**
 * Created by Sigal on 5/21/2016.
 */

@Service
@DependsOn(value = {"persist"})
public class GeneralManagerImpl implements GeneralManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralManagerImpl.class);

    @Autowired
    private Persist persist;

    @Autowired
    private Utils utils;

    public <T> void updateObjects(List<T> list) {
        try {
            for (T item : list) {
                BaseEntity old = (BaseEntity) persist.loadObject(item.getClass(), ((BaseEntity) item).getOid());
                if (old != null) {
                    old.setObject((BaseEntity) item);
                    persist.save(old);
                } else {
                    persist.save(item);
                }
            }
        } catch (Exception e) {
            LOGGER.error("updateObjects", e);
        }
    }



    public <T> T loadObject(Class<T> clazz, int oid) {
        return persist.loadObject(clazz, oid);
    }

    public <T> void updateObject(T object) {
        persist.save(object);
    }

    public <T> List<T> getList(Class<T> clazz) {
        return persist.getList(clazz);
    }

    public <T> void delete(Class<T> clazz) {
        persist.delete(clazz);
    }

    public <T> void deleteByAdminOid(Class<T> clazz, int adminOid) {
        persist.deleteByAdminOid(clazz, adminOid);
    }

    public Map<Integer, Map<Integer, ActivistVotingRate>> getActivistsVotingRate() {
        return persist.getActivistsVotingRate();
    }

    public <T> List<T> loadList(Class<T> clazz) {
        return persist.loadList(clazz);
    }

    public List<ObserverBallotBoxMapObject>  getObserversBallotBoxesMapForBallotBox (int adminOid, int ballotBoxOid) {
        return persist.getObserversBallotBoxesMapForBallotBox(adminOid, ballotBoxOid);
    }

    public Map<Integer, Integer> votersSupportStatusMap () {
        return persist.votersSupportStatusMap();
    }

    public void removeActivistVoterMapObjects (List<Integer> oidsList) {
        persist.removeActivistVoterMapObjects(oidsList);
    }

    public void removeCallerVoterMapObjects (List<Integer> oidsList) {
        persist.removeCallerVoterMapObjects(oidsList);
    }

    public void removeVoterCustomGroupMappingObjects (List<Integer> oidsList) {
        persist.removeVoterCustomGroupMappingObjects(oidsList);
    }

    public void removeVoterElectionDayCallObjects (List<Integer> oidsList) {
        persist.removeVoterElectionDayCallObjects(oidsList);
    }

    public void removeDriveObjects (List<Integer> oidsList) {
        persist.removeDriveObjects(oidsList);
    }

    public void removeSupporterBirthdayObjects (List<Integer> oidsList) {
        persist.removeSupporterBirthdayObjects(oidsList);
    }

    public List<VoterObject> getVotersByAdmin(int adminUserOid, int type) {
        return persist.getVotersByAdmin(adminUserOid, type);
    }

    public <T> List<T> getListByAdminOid(Class<T> clazz, int adminOid) {
        return getListByAdminOid(clazz, adminOid, null, false);
    }

    public <T> List<T> getListByAdminOid(Class<T> clazz, int adminOid, Integer limit, boolean reverse) {
        return persist.getListByAdminOid(clazz, adminOid, limit, reverse);
    }

    public void save(BaseEntity entity) {
        persist.save(entity);
    }

    public <T> void delete(Class<T> clazz, int oid) {
        persist.delete(clazz, oid);
    }

    public <T> void delete(Class<T> clazz, List<Integer> oidsList) {
        persist.delete(clazz, oidsList);
    }

    public CandidateDataDailyObject createCandidateDataDailyObject(Date date, AdminUserObject adminUserObject) {
        Map<Integer, Integer> supportStats = calculateCandidateSupportStats(adminUserObject.getOid());
        return new CandidateDataDailyObject(
                adminUserObject,
                date,
                supportStats.get(PARAM_SUPPORT_STATUS_SUPPORTING),
                supportStats.get(PARAM_SUPPORT_STATUS_NOT_SUPPORTING),
                supportStats.get(PARAM_SUPPORT_STATUS_UNKNOWN),
                supportStats.get(PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING));
    }

    public Map<Integer, Integer> calculateCandidateSupportStats (int adminOid) {
        return persist.calculateCandidateSupportStats(adminOid);
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>>  getActivistsDataMap (boolean today) {
        return persist.getActivistsDataMap(today);
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>> activistsDataFromYesterday () {
        return persist.activistsDataFromYesterday();
    }

    public Map<Integer, LinkedHashMap<Integer, Integer>>  activistsSupportStats (boolean onlyYesterday) {
        return persist.activistsSupportStats(onlyYesterday);
    }

    public List<VoterObject> getVotersThatNeedRide() {
        return persist.getVotersThatNeedRide();
    }

    public<T> void setDeleted (Class<T> clazz, int oid) {
        persist.setDeleted(clazz, oid);
    }

    public List<ActivistDataDailyObject> getHighestPerformanceActivistsYesterday () {
        return persist.getHighestPerformanceActivistsYesterday();
    }

    public <T extends BaseEntity> void save(List<T> list) {
        List<List<T>> chunks = Lists.partition(list, MAX_ENTITIES_TO_SAVE);
        for (List<T> chunk : chunks) {
            persist.saveAll(chunk);
        }
    }

    public long countClients (int campaignOid) {
        return persist.countClients(campaignOid);
    }

    public ContactsGroupObject getContactsGroupByNameInService (String nameInService) {
        return persist.getContactsGroupByNameInService(nameInService);
    }

    public List<VoterObject> getVotersWithPhoneNumbers (int adminOid, int supportStatus) {
        return persist.getVotersWithPhoneNumbers(adminOid, supportStatus);
    }

    public <T> void clearCache(Class<T> clazz, int oid) {
        persist.clearCache(clazz, oid);
    }

    public List<VoterObject> getSupportersWithNoActivists (int adminUserOid) {
        return persist.getSupportersWithNoActivists(adminUserOid);
    }

    public List<VoterCallObject> getCallsByAdminOid(int adminUserOid, String startDate, String endDate, boolean onlyWithMessages) {
        return persist.getCallsByAdminOid(adminUserOid, startDate, endDate, onlyWithMessages);
    }

    public List<VoterObject> getNotVotedSupporters (int adminUserOid) {
        return persist.getNotVotedSupporters(adminUserOid);
    }

    public Map<Integer, List<VoterObject>> getActivistVotersMap(int adminUserOid) {
        List<ActivistVoterMapObject> activistVoterMapObjects = persist.getActivistsVotersMapObjectsByAdminUserOid(adminUserOid);
        return createActivistsVotersMapFromData(activistVoterMapObjects);

    }

    private Map<Integer, List<VoterObject>> createActivistsVotersMapFromData (List<ActivistVoterMapObject> activistVoterMapObjects) {
        Map<Integer, List<VoterObject>> activistOidToVotersListMap = new HashMap<>();
        for (ActivistVoterMapObject activistVoterMapObject : activistVoterMapObjects) {
            List<VoterObject> voterObjects = activistOidToVotersListMap.get(activistVoterMapObject.getActivist().getOid());
            if (voterObjects == null) {
                voterObjects = new ArrayList<>();
            }
            voterObjects.add(activistVoterMapObject.getVoter());
            activistOidToVotersListMap.put(activistVoterMapObject.getActivist().getOid(), voterObjects);
        }
        return activistOidToVotersListMap;

    }

    public List<VoterObject> getNotVotedSupportersWithNoActivist (int adminUserOid) {
        return persist.getNotVotedSupportersWithNoActivist(adminUserOid);
    }

    public Long countActivistSupporters(int activistOid) {
        return persist.countActivistSupporters(activistOid);
    }

    public boolean isActivistManager(int activistOid) {
        return persist.isActivistManager(activistOid);
    }

    public long countSlavesSupporters(int activistOid) {
        return persist.countSlavesSupporters(activistOid);
    }

    public List<VoterObject> getVotersByActivistOid(int activistOid) {
        List<ActivistVoterMapObject> activistVoterMapObjects = persist.getActivistVoterMapByActivistOid(activistOid);
        List<VoterObject> voters = new ArrayList<>();
        for (ActivistVoterMapObject activistVoterMapObject : activistVoterMapObjects) {
            VoterObject voterObject = activistVoterMapObject.getVoter();
            if (voterObject != null) {
                voterObject.setRelationToActivist(activistVoterMapObject.getRelation());
                voterObject.setActivistMappingOid(activistVoterMapObject.getOid());
                voters.add(voterObject);
            }
        }
        return voters;
    }

    public List<SupporterActionObject> getOpenSupporterActions (int adminUserOid, Integer limit, boolean onlyOpen, Integer type) {
        return persist.getOpenSupporterActions(adminUserOid, limit, onlyOpen, type);
    }

    public List<SupporterActionObject> getOpenSupporterActions (int adminUserOid, Integer limit, boolean onlyOpen) {
        return persist.getOpenSupporterActions(adminUserOid, limit, onlyOpen, null);
    }

    public List<BallotBoxObject> getBallotBoxesByCampaign (int campaignOid) {
        List<BallotBoxObject> ballotBoxObjects = new ArrayList<>();
        List<Integer> citiesOids = getCitiesOidsByCampaign(campaignOid);
        if (!citiesOids.isEmpty()) {
            ballotBoxObjects = getBallotBoxesByCities(citiesOids);
        }
        return ballotBoxObjects;
    }

    public List<Integer> getCitiesOidsByCampaign (int campaignOid) {
        List<CityObject> cityObjects = getCitiesByCampaign(campaignOid);
        List<Integer> citiesOids = new ArrayList<>();
        if (cityObjects != null && !cityObjects.isEmpty()) {
            for (CityObject cityObject : cityObjects) {
                citiesOids.add(cityObject.getOid());
            }
        }
        return citiesOids;
    }

    public List<BallotBoxObject>  getBallotBoxesByCities(List<Integer> citiesOids) {
        return persist.getBallotBoxesByCities(citiesOids);
    }

    public List<CityObject> getCitiesByCampaign (int campaignOid) {
        return persist.getCitiesByCampaign(campaignOid);
    }

    public List<BallotBoxObject>  getBallotBoxesByObserver(int observerOid) {
        return persist.getBallotBoxesByObserver(observerOid);
    }

    public Map<Integer, List<VoterObject>> getActivistVotersMapForNotVotedVoters (int adminUserOid) {
        List<ActivistVoterMapObject> activistVoterMapObjects = persist.getActivistsVotersMapObjectsForNotVotedVotersByAdminUserOid(adminUserOid);
        return createActivistsVotersMapFromData(activistVoterMapObjects);

    }

    public List<VoterElectionDayCallObject> getElectionDayCallsByAdminOid(int adminUserOid) {
        return persist.getElectionDayCallsByAdminOid(adminUserOid);
    }

    public List<ActivistMasterSlaveMapObject> getAdminMasterSlaveMap(int managerOid) {
        return persist.getAdminMasterSlaveMap(managerOid);
    }

    public Map<String, String> slavesStats(int activistOid, List<Integer> oids) {
        return persist.slavesStats(activistOid, oids);
    }

    public Map<Integer, Integer> getActivistsVotersCountMap(List<Integer> activistsOids) {
        return persist.getActivistsVotersCountMap(activistsOids);
    }

    public Map<Integer, Set<String>> getVotersActivistsMap(List<Integer> activistsOids) {
        return persist.getVotersActivistsMap(activistsOids);
    }

    public Map<String, TranslationObject> getAllTranslations() {
        List<TranslationObject> translationObjectList = getList(TranslationObject.class);
        Map<String, TranslationObject> translations = new HashMap<>();
        for (TranslationObject translationObject : translationObjectList) {
            translations.put(translationObject.getTranslationKey(), translationObject);
        }
        return translations;
    }

    public List<Integer> getBallotBoxesOidsByBlock(int blockOid) {
        return persist.getBallotBoxesOidsByBlock(blockOid);
    }

    public Map<Integer, Map<Integer, Set<String>>> getActiveActivistsVotersMap() {
        return persist.getActiveActivistsVotersMap();
    }

    public List<PublicInquiryObject> getOpenPublicInquiries (int adminOid) {
        return persist.getOpenPublicInquiries(adminOid);
    }

    public Map<Integer, Integer> getPublicInquiriesByTopics(int adminOid) {
        return persist.getPublicInquiriesByTopics(adminOid);
    }

    public LandingPageDataObject getLandingPageDataObjectByName(String name) {
        return persist.getLandingPageDataObjectByName(name);
    }

    public List<Integer> getGroupVotersOids(int groupOid) {
        return persist.getGroupVotersOids(groupOid);
    }

    public List<Integer> getVoterGroups (int voterOid) {
        return persist.getVoterGroups(voterOid);
    }

    public VoterCustomGroupMappingObject isVoterMemberInGroup (int voterOid, int groupOid) {
        return persist.isVoterMemberInGroup(voterOid, groupOid);
    }

    public boolean isNameAvailable (String name, int oid) {
        return persist.isNameAvailable(name, oid);
    }

    public List<Integer> getVotersOidsByActivistOid(int activistOid) {
        return persist.getVotersOidsByActivistOid(activistOid);
    }

    public List<Integer> getGroupsVotersOids(List<Integer> oidsList) {
        return persist.getGroupsVotersOids(oidsList);
    }

    public List<Integer> getCustomGroupsOidsForCaller (int callerOid) {
        return persist.getCustomGroupsOidsForCaller(callerOid);
    }

    public void removeAllCustomGroupFromCaller (int callerOid) {
        persist.removeAllCustomGroupFromCaller(callerOid);
    }

    public int countVotersInGroup (int groupOid) {
        return persist.countVotersInGroup(groupOid);
    }

    public List<String> getVotersIds (int oid, boolean voters) {
        return persist.getVotersIds(oid, voters);
    }

    public List<String> getCampaignVotersIds (CampaignObject campaignObject) {
        return persist.getCampaignVotersIds(campaignObject);
    }

    public VoterObject getVoterById (int adminUserOid, String voterId) {
        return persist.getVoterById(adminUserOid, voterId);
    }

    public Map<String, Integer> votersIdsMap(int adminOid) {
        return persist.votersIdsMap(adminOid);
    }

    public void updateSupportStatus (int voterOid, int supportStatus) {
        persist.updateSupportStatus(voterOid, supportStatus);
    }

    public ActivistVoterMapObject getActivistVoterMap(int activistOid, int voterOid) {
        return persist.getActivistVoterMap(activistOid, voterOid);
    }

    public void setVoted (int voterOid) {
        persist.setVoted(voterOid);
    }

    public List<GroupManagerObject> getManagersForDynamicGroup(int groupOid) {
        return persist.getManagersForDynamicGroup(groupOid);
    }

    public List<DynamicGroupObject> getDynamicGroupsForManager (int managerOid) {
        return persist.getDynamicGroupsForManager(managerOid);
    }

    public void setGroupManagerDynamicGroupDeleted (int managerOid, int groupOid, int adminOid) {
        persist.setGroupManagerDynamicGroupDeleted(managerOid, groupOid, adminOid);
    }

    public Map<String, String> countUsers (int managerOid) {
        return persist.countUsers(managerOid);
    }

    public boolean isPhoneNumberExist (String phone) {
        return persist.isPhoneNumberExist(phone);
    }

    public <T> List<T> getListByManagerOid(Class<T> clazz, int managerOid, Integer limit, boolean reverse) {
        return persist.getListByManagerOid(clazz, managerOid, limit, reverse);
    }

    public <T> List<T> getListByManagerOid(Class<T> clazz, int managerOid) {
        return persist.getListByManagerOid(clazz, managerOid, null, false);
    }

    public Map<Integer, Integer> countActivistsSupporters(int managerOid, boolean includeDeletedVoters) {
        return persist.countActivistsSupporters(managerOid, includeDeletedVoters);
    }

    public Map<Integer, Integer> countVotedVotersForActivistsOids (List<Integer> oids) {
        return persist.countVotedVotersForActivistsOids(oids);
    }

    public Map<String, Integer> getActivistsTotalVotersMap(int managerOid, Integer limit) {
        return persist.getActivistsTotalVotersMap(managerOid, limit);
    }

    public Map<Date, Integer> getCallsCountByDates(int managerOid, int limit) {
        return persist.getCallsCountByDates(managerOid, limit);
    }

    public Long countVotersCalls(int managerOid) {
        return persist.countVotersCalls(managerOid);
    }

    public boolean isVoterIdExistsForCandidate (int adminUserOid, String voterId, JSONObject results) {
        boolean exists = false;
        if (hasText(voterId)) {
            exists = persist.isVoterIdExistsForCandidate(adminUserOid, voterId);
        }
        if (exists) {
            results.put(PARAM_CODE, PARAM_ERROR_ID_EXISTS);
        }
        return exists;
    }

    public int getContactsGroupChange(VoterObject oldVoter, VoterObject newVoter) {
        int changeType = NO_CHANGE;
        if (newVoter != null && newVoter.getSupportStatus() != null) {
            if (oldVoter == null) {
                if (newVoter.getSupportStatus() == PARAM_SUPPORT_STATUS_SUPPORTING) {
                    changeType = ADD_TO_SUPPORTERS_GROUP;
                } else {
                    changeType = REMOVE_FROM_SUPPORTERS_GROUP;
                }
            } else if (!oldVoter.getSupportStatus().equals(newVoter.getSupportStatus())) {
                if (oldVoter.getSupportStatus() == PARAM_SUPPORT_STATUS_UNKNOWN && newVoter.getSupportStatus() == PARAM_SUPPORT_STATUS_SUPPORTING) {
                    changeType = ADD_TO_SUPPORTERS_GROUP;
                } else if (oldVoter.getSupportStatus() == PARAM_SUPPORT_STATUS_SUPPORTING) {
                    changeType = REMOVE_FROM_SUPPORTERS_GROUP;
                }
            }
        }
        return changeType;
    }

    public void updateVoterCustomPropertyObjects(String data, VoterObject voterObject) throws ServiceResponseException {
        List<CustomPropertyObject> customPropertyObjects = new ArrayList<>();
        if (data != null) {
            List<String> items = Arrays.asList(data.split(";"));
            if (!items.isEmpty()) {
                for (String item : items) {
                    if (Utils.hasText(item)) {
                        List<String> tokens = Arrays.asList(item.split(":"));
                        if (tokens.size() == 2)  {
                            int customPropertyOid = Integer.valueOf(tokens.get(0));
                            VoterCustomPropertyObject voterCustomPropertyObject = getVoterCustomPropertyObject(voterObject.getOid(), customPropertyOid);
                            if (voterCustomPropertyObject == null) {
                                CustomPropertyObject customPropertyObject = loadObject(CustomPropertyObject.class, customPropertyOid);
                                if (customPropertyObject != null) {
                                    voterCustomPropertyObject = new VoterCustomPropertyObject();
                                    voterCustomPropertyObject.setVoterObject(voterObject);
                                    voterCustomPropertyObject.setCustomPropertyObject(customPropertyObject);
                                }
                            }
                            if (voterCustomPropertyObject != null) {
                                voterCustomPropertyObject.setValue(tokens.get(1));
                                save(voterCustomPropertyObject);
                                customPropertyObjects.add(new CustomPropertyObject(voterCustomPropertyObject));
                            }
                        }
                    }
                }
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_ADMIN_OID, voterObject.getAdminUserObject().getOid());
        params.put(PARAM_VOTER_OID, voterObject.getOid());
        params.put(PARAM_CUSTOM_PROPERTIES, JsonUtils.customPropertyObjectsToJson(customPropertyObjects));
        ServicesApi.requestFromVotersImMemoryService("update-voter-custom-properties", params, null);

    }

    public VoterCustomPropertyObject getVoterCustomPropertyObject(int voterOid, int customPropertyOid) {
        return persist.getVoterCustomPropertyObject(voterOid, customPropertyOid);
    }

    public List<ActivistObject> getActivistsForVoter (int voterOid) {
        return persist.getActivistsForVoter(voterOid);
    }

    public Map<Integer, Integer> countSupportersForActivistsOids (List<Integer> oids) {
        return persist.countSupportersForActivistsOids(oids);
    }

    public ActivistVoterMapObject getActivistVoterMapObject(int voterOid, int activistOid) {
        return persist.getActivistVoterMapObject(voterOid, activistOid);
    }

    public List<SupporterActionObject> getAllTasks() {
        return persist.getAllTasks();
    }

    public List<SupporterActionObject> getVoterPendingTasks(int voterOid) {
        return persist.getVoterPendingTasks(voterOid);
    }

    public void removeTaskFromVoter(Class clazz, int voterOid) {
        persist.removeTaskFromVoter(clazz, voterOid);
    }

    public List<ActivistMasterSlaveMapObject> getMasterSlaveMap(int masterOid) {
        return persist.getMasterSlaveMap(masterOid);
    }

    public Long getCallerCallsCount(int callerOid) {
        return persist.getCallerCallsCount(callerOid);
    }

    public List<VoterCallObject> getCallsByCallerOid(int callerOid) {
        return persist.getCallsByCallerOid(callerOid);
    }


    public List<SupporterActionObject> getAdminTasks(int adminOid) {
        return persist.getAdminTasks(adminOid);
    }

    public Map<String, String> calculateManagerSystemStats(int managerOid) {
        return persist.calculateManagerSystemStats(managerOid);
    }

    public Map<Date, Integer> getVoterCallsByManagerOid (int managerOid, Integer limit) {
        List<VoterCallObject> calls = persist.getVoterCallsByManagerOid(managerOid, limit);
        Map<Date, Integer> callsMap = new TreeMap<>();
        for (VoterCallObject call : calls) {
            Date time = utils.getMidnightDate(call.getTime());
            Integer count = callsMap.get(time);
            if (count == null) {
                count = 0;
            }
            count++;
            callsMap.put(time, count);
        }
        return callsMap;
    }

    public Long countActivistsForVoter (int voterOid) {
        return persist.countActivistsForVoter(voterOid);
    }

    public List<VoterCustomPropertyObject> getCustomPropertiesForVoter (int voterOid) {
        return persist.getCustomPropertiesForVoter(voterOid);
    }

    public List<CityObject> getAdminCities (int adminOid) {
        return persist.getAdminCities(adminOid);
    }

    public LinkedHashMap<Integer, ActivistObject>  getActivistsDataMap (int adminOid, Integer managerOid) {
        return persist.getActivistsDataMap(adminOid, managerOid);
    }

    public Map<Integer, Set<VoterObject>> loadVotersSuggestionsData() {
        return persist.loadVotersSuggestionsData();
    }

    public List<VoterObject> getActivistVoters (int activistOid) {
        return persist.getActivistVoters(activistOid);
    }

    public Set<AbstractMap.SimpleEntry> getActivistVotersOids (int activistOid) {
        return persist.getActivistVotersOids(activistOid);
    }

    public boolean canObserverGetPollingStationVoters (int observerOid, int ballotBoxOid) {
        return persist.canObserverGetPollingStationVoters(observerOid, ballotBoxOid);
    }

    public List<VotingStatusChangeObject> getObserverVotingLog(List<Integer> oids) {
        return persist.getObserverVotingLog(oids);
    }

    public List<ObserverCheckInObject> getObserverCheckIns(List<Integer> oids) {
        return persist.getObserverCheckIns(oids);
    }

    public List<ElectionDayAlert> getObserverAlerts(List<Integer> oids) {
        return persist.getObserverAlerts(oids);
    }

    public List<VoterObject> getVotersForPollingStations (int adminOid, int pollingStationOid) {
        return persist.getVotersForPollingStations(adminOid, pollingStationOid);
    }

    public boolean canObserverEditVoter (int voterOid, int observerOid) {
        return persist.canObserverEditVoter(voterOid, observerOid);
    }

    public List<BallotBoxObject> getAdminPollingStations (int adminOid) {
        return persist.getAdminPollingStations(adminOid);
    }

    public List<ObserverBallotBoxMapObject> getObserversBallotBoxesMapsByBallotBoxes(List<Integer> ballotBoxesOids, int adminOid) {
        List<ObserverBallotBoxMapObject> mapObjects = new ArrayList<>();
        if (!ballotBoxesOids.isEmpty()) {
            mapObjects = persist.getObserversBallotBoxesMapsByBallotBoxes(ballotBoxesOids, adminOid);
        }
        return mapObjects;
    }

    public AdminPollingStationStatsObject getLastPollingStationStatsByPollingStationOid(int adminOid, int pollingStationOid) {
        return persist.getLastPollingStationStatsByPollingStationOid(adminOid, pollingStationOid);
    }

    public List<ObserverObject> getObserversForBallotBox (int ballotBoxOid, int adminOid) {
        return persist.getObserversForBallotBox(ballotBoxOid, adminOid);
    }

    public Map<Integer, Set<AdminPollingStationStatsObject>> getMostRecentPollingStationsStats ()  {
        return persist.getMostRecentPollingStationsStats();
    }

    public List<Integer> getPollingStationsWithCheckedInObservers (int adminOid) {
        return persist.getPollingStationsWithCheckedInObservers(adminOid);
    }

    public Map<Integer, Set<Integer>> getPollingStationsThatHasObserversMap () {
        return persist.getPollingStationsThatHasObserversMap();
    }

}
