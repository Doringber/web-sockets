package com.elector.Services;

import com.elector.Exceptions.ServiceResponseException;
import com.elector.Objects.Entities.*;
import com.elector.Objects.General.*;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Sigal on 5/21/2016.
 */

public interface GeneralManager {

    public <T> void updateObjects(List<T> list);

    public <T> T loadObject(Class<T> clazz, int oid);

    public <T> void updateObject(T object);

    public <T> List<T> getList(Class<T> clazz);

    public <T> void delete(Class<T> clazz);

    public <T> void deleteByAdminOid(Class<T> clazz, int adminOid);

    public Map<Integer, Map<Integer, ActivistVotingRate>> getActivistsVotingRate();

    public <T> List<T> loadList(Class<T> clazz);

    public List<ObserverBallotBoxMapObject> getObserversBallotBoxesMapForBallotBox (int adminOid, int ballotBoxOid);

    public Map<Integer, Integer> votersSupportStatusMap ();

    public void removeActivistVoterMapObjects (List<Integer> oidsList);

    public void removeCallerVoterMapObjects (List<Integer> oidsList);

    public void removeVoterCustomGroupMappingObjects (List<Integer> oidsList);

    public void removeVoterElectionDayCallObjects (List<Integer> oidsList);

    public void removeDriveObjects (List<Integer> oidsList);

    public void removeSupporterBirthdayObjects (List<Integer> oidsList);

    public List<VoterObject> getVotersByAdmin(int adminUserOid, int type);

    public <T> List<T> getListByAdminOid (Class<T> clazz, int adminOid);

    public void save(BaseEntity entity);

    public<T> void delete (Class<T> clazz, int oid);

    public <T> void delete(Class<T> clazz, List<Integer> oidsList);

    public CandidateDataDailyObject createCandidateDataDailyObject (Date date, AdminUserObject adminUserObject);

    public Map<Integer, LinkedHashMap<Integer, Integer>>  getActivistsDataMap (boolean today);

    public Map<Integer, LinkedHashMap<Integer, Integer>> activistsDataFromYesterday ();

    public Map<Integer, LinkedHashMap<Integer, Integer>>  activistsSupportStats (boolean onlyYesterday);

    public List<VoterObject> getVotersThatNeedRide();

    public<T> void setDeleted (Class<T> clazz, int oid);

    public List<ActivistDataDailyObject> getHighestPerformanceActivistsYesterday ();

    public <T extends BaseEntity> void save(List<T> list);

    public long countClients (int campaignOid);

    public ContactsGroupObject getContactsGroupByNameInService (String nameInService);

    public List<VoterObject> getVotersWithPhoneNumbers (int adminOid, int supportStatus);

    public <T> void clearCache(Class<T> clazz, int oid);

    public List<VoterObject> getSupportersWithNoActivists (int adminUserOid);

    public List<VoterCallObject> getCallsByAdminOid(int adminUserOid, String startDate, String endDate, boolean onlyWithMessages);

    public List<VoterObject> getNotVotedSupporters (int adminUserOid);

    public Map<Integer, List<VoterObject>> getActivistVotersMap(int adminUserOid);

    public List<VoterObject> getNotVotedSupportersWithNoActivist (int adminUserOid);

    public Long countActivistSupporters(int activistOid);

    public boolean isActivistManager(int activistOid);

    public long countSlavesSupporters(int activistOid);

    public List<VoterObject> getVotersByActivistOid(int activistOid);

    public List<SupporterActionObject> getOpenSupporterActions (int adminUserOid, Integer limit, boolean onlyOpen, Integer type);

    public List<SupporterActionObject> getOpenSupporterActions (int adminUserOid, Integer limit, boolean onlyOpen);

    public List<BallotBoxObject> getBallotBoxesByCampaign (int campaignOid);

    public List<BallotBoxObject>  getBallotBoxesByObserver(int observerOid);

    public Map<Integer, List<VoterObject>> getActivistVotersMapForNotVotedVoters (int adminUserOid);

    public List<VoterElectionDayCallObject> getElectionDayCallsByAdminOid(int adminUserOid);

    public List<ActivistMasterSlaveMapObject> getAdminMasterSlaveMap(int managerOid);

    public Map<String, String> slavesStats(int activistOid, List<Integer> oids);

    public Map<Integer, Integer> getActivistsVotersCountMap(List<Integer> activistsOids);

    Map<Integer, Set<String>> getVotersActivistsMap(List<Integer> activistsOids);

    public Map<String, TranslationObject> getAllTranslations();

    public List<Integer> getBallotBoxesOidsByBlock(int blockOid);

    public Map<Integer, Map<Integer, Set<String>>> getActiveActivistsVotersMap();

    public List<PublicInquiryObject> getOpenPublicInquiries (int adminOid);

    public Map<Integer, Integer> getPublicInquiriesByTopics(int adminOid);

    public LandingPageDataObject getLandingPageDataObjectByName(String name);

    public List<Integer> getGroupVotersOids(int groupOid);

    public List<Integer> getVoterGroups (int voterOid);

    public VoterCustomGroupMappingObject isVoterMemberInGroup (int voterOid, int groupOid);

    public boolean isNameAvailable (String name, int oid);

    public List<Integer> getVotersOidsByActivistOid(int activistOid);

    public List<Integer> getGroupsVotersOids(List<Integer> oidsList);

    public List<Integer> getCustomGroupsOidsForCaller (int callerOid);

    public void removeAllCustomGroupFromCaller (int callerOid);

    public int countVotersInGroup (int groupOid);

    public List<String> getVotersIds (int oid, boolean voters);

    public List<String> getCampaignVotersIds (CampaignObject campaignObject);

    public VoterObject getVoterById (int adminUserOid, String voterId);

    public Map<String, Integer> votersIdsMap(int adminOid);

    public void updateSupportStatus (int voterOid, int supportStatus);

    public ActivistVoterMapObject getActivistVoterMap(int activistOid, int voterOid);

    public void setVoted (int voterOid);

    public List<GroupManagerObject> getManagersForDynamicGroup(int groupOid);

    public List<DynamicGroupObject> getDynamicGroupsForManager (int managerOid);

    public void setGroupManagerDynamicGroupDeleted (int managerOid, int groupOid, int adminOid);

    public Map<String, String> countUsers (int managerOid);

    public boolean isPhoneNumberExist (String phone);

    public <T> List<T> getListByManagerOid(Class<T> clazz, int managerOid, Integer limit, boolean reverse);

    public <T> List<T> getListByManagerOid(Class<T> clazz, int managerOid);

    public Map<Integer, Integer> countActivistsSupporters(int managerOid, boolean includeDeletedVoters);

    public Map<Integer, Integer> countVotedVotersForActivistsOids (List<Integer> oids);

    public Map<String, Integer> getActivistsTotalVotersMap(int managerOid, Integer limit);

    public Map<Date, Integer> getCallsCountByDates(int managerOid, int limit);

    public Long countVotersCalls(int managerOid);

    public boolean isVoterIdExistsForCandidate (int adminUserOid, String voterId, JSONObject results);

    public int getContactsGroupChange(VoterObject oldVoter, VoterObject newVoter);

    public void updateVoterCustomPropertyObjects(String data, VoterObject voterObject) throws ServiceResponseException;

    public VoterCustomPropertyObject getVoterCustomPropertyObject(int voterOid, int customPropertyOid);

    public List<ActivistObject> getActivistsForVoter (int voterOid);

    public Map<Integer, Integer> countSupportersForActivistsOids (List<Integer> oids);

    public ActivistVoterMapObject getActivistVoterMapObject(int voterOid, int activistOid);

    public List<SupporterActionObject> getAllTasks();

    public List<SupporterActionObject> getVoterPendingTasks(int voterOid);

    public void removeTaskFromVoter(Class clazz, int voterOid);

    public List<ActivistMasterSlaveMapObject> getMasterSlaveMap(int masterOid);

    public Long getCallerCallsCount(int callerOid);

    public List<VoterCallObject> getCallsByCallerOid(int callerOid);


    public List<SupporterActionObject> getAdminTasks(int adminOid);

    public Map<String, String> calculateManagerSystemStats(int managerOid);

    public Map<Date, Integer> getVoterCallsByManagerOid (int managerOid, Integer limit);

    public Long countActivistsForVoter (int voterOid);

    public List<VoterCustomPropertyObject> getCustomPropertiesForVoter (int voterOid);

    public List<CityObject> getAdminCities (int adminOid);

    public LinkedHashMap<Integer, ActivistObject>  getActivistsDataMap (int adminOid, Integer managerOid);

    public Map<Integer, Set<VoterObject>> loadVotersSuggestionsData();

    public List<VoterObject> getActivistVoters (int activistOid);

    public Set<AbstractMap.SimpleEntry> getActivistVotersOids (int activistOid);

    public boolean canObserverGetPollingStationVoters (int observerOid, int ballotBoxOid);

    public List<VotingStatusChangeObject> getObserverVotingLog(List<Integer> oids);

    public List<ObserverCheckInObject> getObserverCheckIns(List<Integer> oids);

    public List<ElectionDayAlert> getObserverAlerts(List<Integer> oids);

    public List<VoterObject> getVotersForPollingStations (int adminOid, int pollingStationOid);

    public boolean canObserverEditVoter (int voterOid, int observerOid);

    public List<BallotBoxObject> getAdminPollingStations (int adminOid);

    public List<ObserverBallotBoxMapObject> getObserversBallotBoxesMapsByBallotBoxes(List<Integer> ballotBoxesOids, int adminOid);

    public AdminPollingStationStatsObject getLastPollingStationStatsByPollingStationOid(int adminOid, int pollingStationOid);

    public List<ObserverObject> getObserversForBallotBox (int ballotBoxOid, int adminOid);

    public Map<Integer, Set<AdminPollingStationStatsObject>> getMostRecentPollingStationsStats () ;

    public List<Integer> getPollingStationsWithCheckedInObservers (int adminOid);

    public Map<Integer, Set<Integer>> getPollingStationsThatHasObserversMap ();


}