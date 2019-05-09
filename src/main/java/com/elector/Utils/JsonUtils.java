package com.elector.Utils;

import com.elector.Objects.Entities.*;
import com.elector.Objects.Entities.LaborParty.IsraelLaborPartyVoterObject;
import com.elector.Objects.Entities.LaborParty.IsraelLaborVoterEditableDataObject;
import com.elector.Objects.Entities.LikudParty.LikudPartyVoterObject;
import com.elector.Objects.Entities.LikudParty.LikudVoterEditableDataObject;
import com.elector.Objects.General.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.elector.Utils.Definitions.*;

/**
 * Created by Sigal on 9/9/2017.
 */
public class JsonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);



    public static void populateJsonObjectWithSupporterData(JSONObject jsonObject, VoterObject supporter) {
        jsonObject.put(PARAM_OID, supporter.getOid());
        jsonObject.put(PARAM_FIRST_NAME, supporter.getFirstName());
        jsonObject.put(PARAM_LAST_NAME, supporter.getLastName());
        jsonObject.put(PARAM_ADDRESS, supporter.getAddress() != null ? supporter.getAddress() : MINUS);
        jsonObject.put(PARAM_BIRTH_DATE, supporter.getFormattedBirthDate());
        jsonObject.put(PARAM_ACTIVIST, supporter.getActivistObject() != null && !supporter.getActivistObject().isDeleted());
        jsonObject.put(PARAM_SUPPORT_STATUS, supporter.getSupportStatus());

    }


    public static JSONArray campaignsListToJsonObject(List<CampaignObject> campaigns) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (CampaignObject campaignObject : campaigns) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, campaignObject.getOid());
            jsonObject.put(PARAM_NAME, campaignObject.getName() != null ? campaignObject.getName() : MINUS);
            jsonObject.put(PARAM_ACTIVE, campaignObject.getActive() != null ? campaignObject.getActive() : false);
            jsonObject.put(PARAM_CLIENTS_COUNTER, campaignObject.getClientsCounter());
            jsonObject.put(PARAM_VOTERS_BOOK, campaignObject.isVotersBook());
            jsonObject.put(PARAM_ELECTION_TIME, Utils.formatDate(campaignObject.getDate(), "dd-MM-yyyy"));
            jsonObject.put(PARAM_CITY, campaignObject.getCoordinatesObject() != null && campaignObject.getCoordinatesObject().getAddress() != null ? campaignObject.getCoordinatesObject().getAddress() : MINUS);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray adminsListToJsonObject(List<AdminUserObject> admins) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (AdminUserObject user : admins) {
            if (user.getOid() != SUPER_ADMIN_OID) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(PARAM_OID, user.getOid());
                jsonObject.put(PARAM_NAME, user.getName() != null ? user.getName() : MINUS);
                jsonObject.put(PARAM_PASSWORD, user.getPassword() != null ? user.getPassword() : MINUS);
                jsonObject.put(PARAM_EMAIL, user.getEmail() != null ? user.getEmail() : MINUS);
                jsonObject.put(PARAM_CAMPAIGN_NAME, user.getCampaignObject() != null ? user.getCampaignObject().getName() : MINUS);
                jsonObject.put(PARAM_MAX_USERS, user.getMaxUsers());
                jsonObject.put(PARAM_BUSINESS_ID, user.getBusinessId());
                jsonObject.put(PARAM_BUSINESS_NAME, user.getBusinessName());
                jsonObject.put(PARAM_EXPIRATION_DATE, String.format("%s %s", Utils.dateStringWithNoTime(user.getExpirationDate()), user.isActiveByExpiration() ? EMPTY : "(אינו פעיל) "));
                jsonObject.put(PARAM_MAIN_OFFICE_ADDRESS, user.getMainOfficeAddress());
                jsonObject.put(PARAM_MAIN_OFFICE_PHONE, user.getMainOfficePhone());
                jsonObject.put(PARAM_COST, user.getCost());
                jsonObject.put(PARAM_TOTAL_PURCHASED_SMS, user.getTotalSmsPurchased());
                jsonObject.put(PARAM_TOTAL_REMAINING_SMS, user.getRemainingSms());
                jsonObject.put(PARAM_PHONE, user.getPhone());
                jsonObject.put(PARAM_CREATION_TIME, Utils.formatDate(user.getCreationDate(), "dd-MM-yy HH:mm:ss"));
                jsonObject.put(PARAM_USERS_COUNT, user.getUsersCount());
                jsonObject.put(PARAM_ADMIN_ID, user.getAdminId() == null ? MINUS : user.getAdminId());
                jsonObject.put(PARAM_ALLOW_EXCEL_UPLOAD, user.getAllowExcelUpload() != null && user.getAllowExcelUpload());
                jsonObject.put(PARAM_ACTIVE, user.isActive());
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray;
    }

    public static JSONArray configToJsonObject(List<ConfigObject> config) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (ConfigObject item : config) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, item.getOid());
            jsonObject.put(PARAM_CONFIG_KEY, item.getConfigKey());
            jsonObject.put(PARAM_CONFIG_VALUE, item.getConfigValue());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static <T> List<T> jsonToObjectsList(Class<T> clazz, JSONArray data) {
        List<T> objects = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            for (Object item : data) {
                objects.add(objectMapper.readValue(item.toString(), clazz));
            }
        } catch (IOException e) {
            LOGGER.error("jsonToObjectsList", e);
        }
        return objects;

    }

//    public static JSONArray ballotBoxesListToJsonObject(List<BallotBoxObject> ballotBoxObjects) throws Exception {
//        return ballotBoxesListToJsonObject(ballotBoxObjects, null);
//    }
//
//    public static JSONArray ballotBoxesListToJsonObject(List<BallotBoxObject> ballotBoxObjects, List<ObserverObject> observerObjects) throws Exception {
//        JSONArray jsonArray = new JSONArray();
//        Map<Integer, List<ObserverObject>> ballotBoxesOidsToObservers = new HashMap<>();
//        if (observerObjects != null) {
//            for (ObserverObject observerObject : observerObjects) {
//                if (observerObject.getBallotBoxObject() != null) {
//                    int ballotBoxOid = observerObject.getBallotBoxObject().getOid();
//                    List<ObserverObject> ballotBoxObservers = ballotBoxesOidsToObservers.get(ballotBoxOid);
//                    if (ballotBoxObservers == null) {
//                        ballotBoxObservers = new ArrayList<>();
//                    }
//                    ballotBoxObservers.add(observerObject);
//                    ballotBoxesOidsToObservers.put(observerObject.getBallotBoxObject().getOid(), ballotBoxObservers);
//                }
//            }
//        }
//
//        for (BallotBoxObject ballotBoxObject : ballotBoxObjects) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put(PARAM_OID, ballotBoxObject.getOid());
//            jsonObject.put(PARAM_NUMBER, ballotBoxObject.getNumber());
//            jsonObject.put(PARAM_ADDRESS, ballotBoxObject.getAddress());
//            if (observerObjects != null) {
//                List<ObserverObject> ballotBoxObservers = ballotBoxesOidsToObservers.get(ballotBoxObject.getOid());
//                JSONArray observers = new JSONArray();
//                if (ballotBoxObservers != null) {
//                    for (ObserverObject observerObject : ballotBoxObservers) {
//                        JSONObject observerJson = new JSONObject();
//                        observerJson.put(PARAM_OID, observerObject.getOid());
//                        observerJson.put(PARAM_NAME, observerObject.getFullName());
//                        observers.put(observerJson);
//                    }
//                    jsonObject.put(PARAM_OBSERVER, observers);
//                }
//            }
//            jsonArray.put(jsonObject);
//        }
//        return jsonArray;
//    }

    public static JSONArray observersListToJsonObject(List<ObserverObject> observerObjectList, List<ObserverBallotBoxMapObject> mapObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (ObserverObject observerObject : observerObjectList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, observerObject.getOid());
            jsonObject.put(PARAM_FIRST_NAME, observerObject.getFirstName());
            jsonObject.put(PARAM_LAST_NAME, observerObject.getLastName());
            jsonObject.put(PARAM_PHONE, observerObject.getPhone());
            jsonObject.put(PARAM_EMAIL, observerObject.getEmail());
            jsonObject.put(PARAM_PASSWORD, observerObject.getPassword());
            jsonObject.put(PARAM_WORKS_ON_ELECTION_DAY, observerObject.isWorkOnElectionDay());
            jsonObject.put(PARAM_ELECTION_DAY_MORNING_SHIFT, observerObject.isElectionDayMorningShift());
            jsonObject.put(PARAM_ELECTION_DAY_EVENING_SHIFT, observerObject.isElectionDayEveningShift());
            jsonObject.put(PARAM_SUPER_OBSERVER, observerObject.isSuperObserver());
            JSONArray ballotBoxes = new JSONArray();
            StringBuilder ballotBoxesBuilder = new StringBuilder();
            for (ObserverBallotBoxMapObject observerBallotBoxMapObject : mapObjects) {
                if (observerBallotBoxMapObject.getObserverObject().getOid() == observerObject.getOid()) {
                    JSONObject ballotBoxJson = new JSONObject();
                    ballotBoxJson.put(PARAM_OID, observerBallotBoxMapObject.getBallotBoxObject().getOid());
                    int number = observerBallotBoxMapObject.getBallotBoxObject().getNumber();
                    String place = observerBallotBoxMapObject.getBallotBoxObject().getPlace();
                    ballotBoxJson.put(PARAM_NUMBER, number);
                    ballotBoxJson.put(PARAM_PLACE, place);
                    ballotBoxes.put(ballotBoxJson);
                    ballotBoxesBuilder.append(String.format("%s (%s), ", place, number));
                }
            }
            jsonObject.put(PARAM_BALLOT_BOXES, ballotBoxes);
            String ballotBoxesString = ballotBoxesBuilder.length() > 1 ?  ballotBoxesBuilder.substring(0, ballotBoxesBuilder.length() - 2) : EMPTY;
            jsonObject.put(PARAM_BALLOT_BOXES_STRING, ballotBoxesString);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static Map<Integer, Integer> jsonToObserversBallotBoxesMap(JSONArray data) {
        Map<Integer, Integer> observerOidsToBallotBoxesOids = new HashMap<>();
        for (Object item : data) {
            int observerOid = ((JSONObject) item).getInt(PARAM_OBSERVER_OID);
            int ballotBoxOid = ((JSONObject) item).getInt(PARAM_BALLOT_BOX_OID);
            observerOidsToBallotBoxesOids.put(observerOid, ballotBoxOid);
        }
        return observerOidsToBallotBoxesOids;
    }

    public static Map<Integer, ObserverObject> jsonToBallotBoxesObserversMap(JSONArray data) {
        Map<Integer, ObserverObject> observerObjectsToBallotBoxesOids = new HashMap<>();
        for (Object item : data) {
            int observerOid = ((JSONObject) item).getInt(PARAM_OID);
            String observerName = ((JSONObject) item).getString(PARAM_NAME);
            ObserverObject observerObject = new ObserverObject();
            observerObject.setOid(observerOid);
            observerObject.setFirstName(observerName);
            int ballotBoxOid = ((JSONObject) item).getInt(PARAM_BALLOT_BOX_OID);
            observerObjectsToBallotBoxesOids.put(ballotBoxOid, observerObject);
        }
        return observerObjectsToBallotBoxesOids;
    }

    public static JSONArray callersListToJsonObject(List<CallerObject> callerObjectList) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (CallerObject callerObject : callerObjectList) {
            jsonArray.put(callerToJson(callerObject));
        }
        return jsonArray;
    }

    public static JSONObject callerToJson(CallerObject callerObject) {
        return callerToJson(callerObject, null);
    }

    public static JSONObject callObjectToJson(VoterCallObject callObject, boolean withCallerName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, callObject.getOid());
        jsonObject.put(PARAM_VOTER_NAME, callObject.getVoterObject().getFullName());
        jsonObject.put(PARAM_PREVIOUS_STATUS, callObject.getPreviousStatus());
        jsonObject.put(PARAM_NEW_STATUS, callObject.getNewStatus());
        jsonObject.put(PARAM_DATE, new SimpleDateFormat("dd-MM-yy hh:mm").format(callObject.getTime()));
        jsonObject.put(PARAM_ANSWERED, Utils.booleanToInt(callObject.isAnswered()));
        if (withCallerName) {
            jsonObject.put(PARAM_CALLER_NAME, callObject.getCallerObject().getFullName());
        }
        jsonObject.put(PARAM_UNANSWERED_REASON, callObject.getUnansweredReason() == null ? MINUS : callObject.getUnansweredReason());
        return jsonObject;
    }

    public static JSONArray callsListToJsonObject(List<VoterCallObject> callObjectList, boolean withCallerName) {
        JSONArray jsonArray = new JSONArray();
        for (VoterCallObject call : callObjectList) {
            jsonArray.put(callObjectToJson(call, withCallerName));
        }
        return jsonArray;
    }

    public static JSONObject callerToJson(CallerObject callerObject, List<VoterCallObject> calls) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, callerObject.getOid());
        jsonObject.put(PARAM_FIRST_NAME, callerObject.getFirstName());
        jsonObject.put(PARAM_LAST_NAME, callerObject.getLastName());
        jsonObject.put(PARAM_PHONE, callerObject.getPhone());
        jsonObject.put(PARAM_VOTERS_COUNT, callerObject.getVotersToCall());
        jsonObject.put(PARAM_PASSWORD, callerObject.getPassword());
        jsonObject.put(PARAM_ACCESS_ACTIVISTS, callerObject.isAccessActivists());
        jsonObject.put(PARAM_ACCESS_TASKS, callerObject.isAccessTasks());
        if (calls != null) {
            jsonObject.put(PARAM_CALLS, callsListToJsonObject(calls, false));
        }
        jsonObject.put(PARAM_WORKS_ON_ELECTION_DAY, callerObject.isWorkOnElectionDay());
        return jsonObject;
    }

    public static JSONArray candidateMessagesListToJsonObject(List<CandidateMessageObject> candidateMessageObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (CandidateMessageObject candidateMessageObject : candidateMessageObjects) {
            jsonArray.put(candidateMessageToJson(candidateMessageObject));
        }
        return jsonArray;
    }

    public static JSONObject candidateMessageToJson(CandidateMessageObject candidateMessageObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, candidateMessageObject.getOid());
        if (candidateMessageObject.getType() == PARAM_CANDIDATE_MESSAGE_TYPE_ACTIVIST_SUPPORTER_COMMENT) {
            jsonObject.put(PARAM_TITLE, String.format("%s %s", Utils.getTranslation("regarding.voter"), candidateMessageObject.getSubjectVoter().getFullName()));
        } else {
            jsonObject.put(PARAM_TITLE, candidateMessageObject.getTitle());
        }
        jsonObject.put(PARAM_BODY, candidateMessageObject.getBody());
        jsonObject.put(PARAM_DATE, Utils.formatDate(candidateMessageObject.getDate(), "dd-MM-yy HH:mm:ss"));
        jsonObject.put(PARAM_TYPE, candidateMessageObject.getType());
        jsonObject.put(PARAM_SENDER, candidateMessageObject.getSenderOid());
        if (candidateMessageObject.getSubjectVoter() != null) {
            jsonObject.put(PARAM_VOTER_OID, candidateMessageObject.getSubjectVoter().getOid());
            jsonObject.put(PARAM_VOTER_NAME, candidateMessageObject.getSubjectVoter().getFullName());
        }
        jsonObject.put(PARAM_CLOSED, candidateMessageObject.isClosed());
        return jsonObject;
    }


    public static JSONObject innerMessageToJson(InnerMessageObject innerMessageObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, innerMessageObject.getOid());
        jsonObject.put(PARAM_TITLE, innerMessageObject.getTitle());
        jsonObject.put(PARAM_BODY, innerMessageObject.getBody());
        jsonObject.put(PARAM_DATE, innerMessageObject.getDate());
        jsonObject.put(PARAM_SENDER, innerMessageObject.getSenderName());
        jsonObject.put(PARAM_CLOSED, innerMessageObject.isClosed());
        return jsonObject;
    }

    public static JSONArray innerMessagesListToJsonObject(List<InnerMessageObject> innerMessageObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (InnerMessageObject innerMessageObject : innerMessageObjects) {
            jsonArray.put(innerMessageToJson(innerMessageObject));
        }
        return jsonArray;
    }

    public static JSONArray activistsListToJsonObject(List<ActivistObject> activistObjects, boolean electionDay, Map<Integer, Set<Integer>> activistMasterSlaveMap) {
        JSONArray jsonArray = new JSONArray();
        for (ActivistObject activistObject : activistObjects) {
            jsonArray.put(activistToJsonObject(activistObject, electionDay, activistMasterSlaveMap));
        }
        return jsonArray;
    }

    public static JSONArray activistsListToJsonObject(List<ActivistObject> activistObjects, boolean electionDay) {
        return activistsListToJsonObject(activistObjects, electionDay, null);
    }

    public static JSONObject activistToJsonObject (ActivistObject activistObject, boolean electionDay, Map<Integer, Set<Integer>> activistMasterSlaveMap) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, activistObject.getOid());
        jsonObject.put(PARAM_FIRST_NAME, activistObject.getFirstName());
        jsonObject.put(PARAM_LAST_NAME, activistObject.getLastName());
        jsonObject.put(PARAM_PHONE, activistObject.getPhone());
        jsonObject.put(PARAM_SUPPORTERS_COUNT, activistObject.getSupportersCount());
        jsonObject.put(PARAM_WORKS_ON_ELECTION_DAY, activistObject.isWorkOnElectionDay());
        jsonObject.put(PARAM_ALLOW_ACCESS_TO_SUPPORT_STATUS, activistObject.isAllowAccessToSupportStatus());
        jsonObject.put(PARAM_ALLOW_EDIT_VOTE_STATUS, activistObject.isAllowEditVoteStatus());
        if (!electionDay) {
            jsonObject.put(PARAM_BIRTH_DATE, activistObject.getFormattedBirthDate());
            jsonObject.put(PARAM_EMAIL, activistObject.getEmail());
            jsonObject.put(PARAM_PASSWORD, activistObject.getPassword());
            jsonObject.put(PARAM_AUTO_VERIFY_SUPPORTERS, activistObject.isAutoVerifySupporters());
        } else {
            jsonObject.put(PARAM_TOTAL_VOTED, activistObject.getTotalVoted());
            jsonObject.put(PARAM_TOTAL_NOT_VOTED, activistObject.getTotalNotVoted());
            jsonObject.put(PARAM_VOTED_PERCENT,
                    activistObject.getTotalVoted() == 0 && activistObject.getTotalNotVoted() == 0 ?
                            0 :
                            BigDecimal.valueOf(activistObject.getTotalVoted() * 100 / ((double) activistObject.getTotalVoted() + activistObject.getTotalNotVoted())).setScale(2, RoundingMode.HALF_DOWN).doubleValue());
            jsonObject.put(PARAM_LAST_CALL_TIME, activistObject.getLastElectionDayCallTime());
        }
        jsonObject.put(PARAM_ALLOW_EXCEL_UPLOAD, activistObject.getAllowExcelUpload() != null && activistObject.getAllowExcelUpload());
        if (activistMasterSlaveMap != null) {
            Set<Integer> slavesOids = activistMasterSlaveMap.get(activistObject.getOid());
            JSONArray slavesJson = new JSONArray(slavesOids);
            jsonObject.put(PARAM_SLAVES_OIDS, slavesJson);
        }
        if (activistObject.getVotedSupportersCount() != null) {
            jsonObject.put(PARAM_VOTED_SUPPORTERS_COUNT, activistObject.getVotedSupportersCount());
        }
        return jsonObject;
    }

    public static JSONArray groupManagerDataDailyListToJsonArray(List<CandidateDataDailyObject> groupManagerDataDailyObjects) {
        JSONArray jsonArray = new JSONArray();
        for (CandidateDataDailyObject groupManagerDataDailyObject : groupManagerDataDailyObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_NAME, TemplateUtils.formatDateExcludeTime(groupManagerDataDailyObject.getDate()));
            jsonObject.put(PARAM_SUPPORTING, groupManagerDataDailyObject.getSupportingCount());
            jsonObject.put(PARAM_NOT_SUPPORTING, groupManagerDataDailyObject.getNotSupportingCount());
            jsonObject.put(PARAM_SUPPORT_UNKNOWN, groupManagerDataDailyObject.getUnknownSupportCount());
            jsonObject.put(PARAM_SUPPORT_UNKNOWN, groupManagerDataDailyObject.getUnknownSupportCount());
            jsonObject.put(PARAM_UNVERIFIED_SUPPORTING, groupManagerDataDailyObject.getUnverifiedSupportingCount());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONObject coordinatesToJsonObject(GeocodeCoordinatesObject geocodeCoordinatesObject) {
        JSONObject jsonObject = new JSONObject();
        if (geocodeCoordinatesObject != null) {
            jsonObject.put(PARAM_ADDRESS, geocodeCoordinatesObject.getAddress());
            jsonObject.put(PARAM_ZOOM, geocodeCoordinatesObject.getZoom());
            jsonObject.put(PARAM_LAT, geocodeCoordinatesObject.getLat());
            jsonObject.put(PARAM_LNG, geocodeCoordinatesObject.getLng());
            jsonObject.put(PARAM_TYPE, geocodeCoordinatesObject.getType());
        }
        return jsonObject;
    }

    public static JSONArray coordinatesListToJsonArray(List<GeocodeCoordinatesObject> geocodeCoordinatesObjectList) {
        JSONArray jsonArray = new JSONArray();
        for (GeocodeCoordinatesObject geocodeCoordinatesObject : geocodeCoordinatesObjectList) {
            jsonArray.put(coordinatesToJsonObject(geocodeCoordinatesObject));
        }
        return jsonArray;
    }

    public static JSONArray callsMapToChartJsonObject(Map<Date, Integer> callsMap) {
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        for (Date date : callsMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_NAME, simpleDateFormat.format(date));
            jsonObject.put(PARAM_COUNT, callsMap.get(date));
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray activistsMapToChartJsonObject(Map<String, Integer> activistsMap, Integer limit) {
        JSONArray jsonArray = new JSONArray();
        for (String name : activistsMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_NAME, name);
            jsonObject.put(PARAM_COUNT, activistsMap.get(name));
            jsonArray.put(jsonObject);
            if (limit != null && jsonArray.length() == limit) {
                break;
            }
        }
        return jsonArray;
    }

    public static JSONArray sentSmsListToJsonArray(List<SentSmsObject> sentSmsObjects) {
        JSONArray jsonArray = new JSONArray();
        for (SentSmsObject sentSmsObject : sentSmsObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, sentSmsObject.getOid());
            jsonObject.put(PARAM_TIME, Utils.formatDate(sentSmsObject.getTime(), "dd-MM-yy HH:mm:ss"));
            jsonObject.put(PARAM_PHONE, sentSmsObject.getRecipientPhone());
            jsonObject.put(PARAM_TEXT, sentSmsObject.getText());
            jsonObject.put(PARAM_FULL_NAME, sentSmsObject.getFullName() != null ? sentSmsObject.getFullName() : MINUS);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray baseUsersListToJsonArray(List<BaseUser> baseUsers) {
        JSONArray jsonArray = new JSONArray();
        for (BaseUser user : baseUsers) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, user.getOid());
            jsonObject.put(PARAM_FULL_NAME, user.getFullName());
            jsonObject.put(PARAM_PHONE, user.getPhone());
            jsonObject.put(PARAM_EMAIL, user.getEmail());
            jsonObject.put(PARAM_PASSWORD, user.getPassword());
            jsonObject.put(PARAM_LAST_LOGIN_DATE, user.getLastLoginDate());
            jsonObject.put(PARAM_USER_TYPE, Utils.getPrintableContactType(user.getClass(), true));
            jsonObject.put(PARAM_TYPE, user.getUserType());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray electionDayAlertsToJson(List<ElectionDayAlert> electionDayAlerts) {
        JSONArray jsonArray = new JSONArray();
        for (ElectionDayAlert alert : electionDayAlerts) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, alert.getOid());
            jsonObject.put(PARAM_FULL_NAME, alert.getUser().getFullName());
            jsonObject.put(PARAM_TIME, Utils.formatDate(alert.getTime(), "dd-MM-yy HH:mm:ss"));
            jsonObject.put(PARAM_HANDLED, alert.isHandled());
            jsonObject.put(PARAM_TEXT, alert.getText());
            jsonObject.put(PARAM_SENDER_TYPE, Utils.getPrintableContactType(alert.getUser().getClass(), true));
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray drivesToJson(List<DriveObject> driveObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (DriveObject drive : driveObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_VOTER_NAME, drive.getVoterObject().getFullName());
            if (drive.getRequestedTime() != null) {
                jsonObject.put(PARAM_REQUESTED_TIME, Utils.formatDate(drive.getRequestedTime(), "dd-MM-yy HH:mm"));
            }
            jsonObject.put(PARAM_START_ADDRESS, Utils.hasText(drive.getStartAddress()) ? drive.getStartAddress() : drive.getVoterObject().getAddress());
            jsonObject.put(PARAM_BALLOT_BOX_ADDRESS, drive.getVoterObject().getBaseCampaignVoterObject().getBallotBoxObject().getAddress());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray tasksToJsonList(List<SupporterActionObject> openTasks) {
        JSONArray jsonArray = new JSONArray();
        for (SupporterActionObject task : openTasks) {
            jsonArray.put(taskToJsonObject(task));
        }
        return jsonArray;
    }

    public static JSONObject taskToJsonObject(SupporterActionObject task) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, task.getOid());
        jsonObject.put(PARAM_VOTER_OID, task.getSupporter().getOid());
        jsonObject.put(PARAM_FULL_NAME, task.getSupporter().getFullName());
        jsonObject.put(PARAM_PHONE, task.getSupporter().getPhone());
        jsonObject.put(PARAM_CREATION_TIME, TemplateUtils.formatDateExcludeTime(task.getCreationDate()));
        if (task.getInitiator() != null) {
            jsonObject.put(PARAM_INITIATOR_FULL_NAME, task.getInitiator().getFullName());
            jsonObject.put(PARAM_INITIATOR_TYPE, Utils.getPrintableContactType(task.getInitiator().getClass(), true));

        }
        if (task instanceof SupporterBillboardRequestObject) {
            jsonObject.put(PARAM_TYPE, SUPPORTER_ACTION_TYPE_BILLBOARD);
            if (((SupporterBillboardRequestObject) task).getTimeToCome() != null) {
                jsonObject.put(PARAM_TIME_TO_COME, Utils.formatDate(((SupporterBillboardRequestObject) task).getTimeToCome(), "dd-MM-yy HH:mm:ss"));
                jsonObject.put(PARAM_ADDRESS, task.getSupporter().getAddress());
            }
        } else if (task instanceof SupporterElectionAssemblyRequestObject) {
            jsonObject.put(PARAM_TYPE, SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY);
            jsonObject.put(PARAM_DESIRED_DATE, TemplateUtils.formatDateExcludeTime(((SupporterElectionAssemblyRequestObject) task).getDesiredDate()));
            jsonObject.put(PARAM_ESTIMATED_PARTICIPANTS_COUNT, ((SupporterElectionAssemblyRequestObject) task).getEstimatedParticipantsCount());
        } else if (task instanceof SupporterVolunteerObject) {
            jsonObject.put(PARAM_TYPE, SUPPORTER_ACTION_TYPE_VOLUNTEER);
            jsonObject.put(PARAM_MOBILE, ((SupporterVolunteerObject) task).isMobile());
            jsonObject.put(PARAM_ROLE, ((SupporterVolunteerObject) task).getRole());
        } else if (task instanceof SupporterCarStickerObject) {
            jsonObject.put(PARAM_TYPE, SUPPORTER_ACTION_TYPE_CAR_STICKER);
            if (((SupporterCarStickerObject) task).getTimeToCome() != null) {
                jsonObject.put(PARAM_TIME_TO_COME, Utils.formatDate(((SupporterCarStickerObject) task).getTimeToCome(), "dd-MM-yy HH:mm:ss"));
                jsonObject.put(PARAM_ADDRESS, task.getSupporter().getAddress());
            }
        } else if (task instanceof SupporterSupportersListDelivery) {
            jsonObject.put(PARAM_TYPE, SUPPORTER_ACTION_TYPE_SUPPORTERS_LIST);
        } else if (task instanceof SupporterElectionAssemblyParticipationObject) {
            jsonObject.put(PARAM_TYPE, SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY_PARTICIPATION);
        }
        jsonObject.put(PARAM_ID, task.createUiKey());
        jsonObject.put(PARAM_DONE, task.isDone());
        return jsonObject;
    }

    public static JSONArray groupsToJson(List<CustomGroupObject> customGroupObjects) {
        JSONArray jsonArray = new JSONArray();
        if (customGroupObjects != null) {
            for (CustomGroupObject customGroupObject : customGroupObjects) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(PARAM_OID, customGroupObject.getOid());
                jsonObject.put(PARAM_TITLE, customGroupObject.getDescription());
                jsonObject.put(PARAM_SIZE, customGroupObject.getSize());
                jsonObject.put(PARAM_IN_SURVEY, customGroupObject.isInSurvey());
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray;
    }

    public static JSONArray donationsToJson(List<DonationObject> donations) {
        JSONArray jsonArray = new JSONArray();
        for (DonationObject donation : donations) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, donation.getOid());
            jsonObject.put(PARAM_DONOR_FIRST_NAME, donation.getDonorFirstName());
            jsonObject.put(PARAM_DONOR_LAST_NAME, donation.getDonorLastName());
            jsonObject.put(PARAM_DONOR_PHONE, donation.getDonorPhone());
            jsonObject.put(PARAM_SUM, donation.getSum());
            jsonObject.put(PARAM_DATE, Utils.formatDate(donation.getDate(), "dd-MM-yy HH:mm:ss"));
            jsonObject.put(PARAM_RELATION_TO_DONOR, donation.getRelationToDonor());
            jsonObject.put(PARAM_PAYMENT_CONFIRMATION_IMAGE, donation.isPaymentConfirmationImage());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray driversToJson(List<DriverObject> driverObjects) {
        JSONArray jsonArray = new JSONArray();
        for (DriverObject driverObject : driverObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, driverObject.getOid());
            jsonObject.put(PARAM_FIRST_NAME, driverObject.getFirstName());
            jsonObject.put(PARAM_LAST_NAME, driverObject.getLastName());
            jsonObject.put(PARAM_PHONE, driverObject.getPhone());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray mapToJsonArray(Map map) {
        JSONArray jsonArray = new JSONArray();
        for (Object key : map.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_KEY, key);
            jsonObject.put(PARAM_VALUE, map.get(key));
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONObject pendingVotersListToJsonObject(VoterObject voterObject, List<PendingVoterObject> pendingVoterObjects) {
        JSONObject jsonObject = new JSONObject();
        for (PendingVoterObject pendingVoterObject : pendingVoterObjects) {
            addPendingVoterPropertyToJsonArray(jsonObject, PARAM_FIRST_NAME, voterObject.getFirstName(), pendingVoterObject.getOid(), pendingVoterObject.getFirstName());
            addPendingVoterPropertyToJsonArray(jsonObject, PARAM_LAST_NAME, voterObject.getLastName(), pendingVoterObject.getOid(), pendingVoterObject.getLastName());
            addPendingVoterPropertyToJsonArray(jsonObject, PARAM_ADDRESS, voterObject.getAddress(), pendingVoterObject.getOid(), pendingVoterObject.getAddress());
            addPendingVoterPropertyToJsonArray(jsonObject, PARAM_PHONE, voterObject.getPhone(), pendingVoterObject.getOid(), pendingVoterObject.getPhone());
            addPendingVoterPropertyToJsonArray(jsonObject, PARAM_BIRTH_DATE, voterObject.getFormattedBirthDate(), pendingVoterObject.getOid(), pendingVoterObject.getFormattedBirthDate());
            addPendingVoterPropertyToJsonArray(jsonObject, PARAM_EMAIL, voterObject.getEmail(), pendingVoterObject.getOid(), pendingVoterObject.getEmail());
            addPendingVoterPropertyToJsonArray(jsonObject, PARAM_GENDER, String.valueOf(voterObject.getGenderCode()), pendingVoterObject.getOid(), String.valueOf(pendingVoterObject.getGenderCode()));
        }
        return jsonObject;
    }

    private static void addPendingVoterPropertyToJsonArray(JSONObject jsonObject, String propertyKey, String existing, int pendingOid, String toAdd) {
        if ((existing == null && toAdd != null) || (existing != null && !existing.equals(toAdd))) {
            if (jsonObject.isNull(propertyKey)) {
                JSONArray array = new JSONArray();
                jsonObject.put(propertyKey, array);
            }
            JSONArray array = (JSONArray) jsonObject.get(propertyKey);
            JSONObject data = new JSONObject();
            data.put(PARAM_OID, pendingOid);
            data.put(PARAM_VALUE, toAdd);
            array.put(data);

        }
    }


    public static JSONArray callsHistoryToJson(List<VoterCallObject> callObjectList) {
        JSONArray jsonArray = new JSONArray();
        for (VoterCallObject callObject : callObjectList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, callObject.getOid());
            jsonObject.put(PARAM_CALLER_NAME, callObject.getCallerObject().getFullName());
            jsonObject.put(PARAM_DATE, new SimpleDateFormat("dd-MM-yy hh:mm").format(callObject.getTime()));
            jsonObject.put(PARAM_ANSWERED, Utils.booleanToInt(callObject.isAnswered()));
            jsonObject.put(PARAM_COMMENT, callObject.getComment());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray citiesToJsonObject(List<CityObject> cityObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (CityObject cityObject : cityObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, cityObject.getOid());
            jsonObject.put(PARAM_NAME, cityObject.getName());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray streetsToJsonObject(List<StreetObject> streetObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (StreetObject streetObject : streetObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, streetObject.getOid());
            jsonObject.put(PARAM_NAME, streetObject.getName());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static  <T extends BaseContact> JSONArray baseContactsToJsonArray(List<T> baseContacts) {
        JSONArray jsonArray = new JSONArray();
        for (BaseContact baseContact : baseContacts) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_FULL_NAME, baseContact.getFullName());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray ballotBoxesRoomsListToJsonObject(List<BallotBoxRoomObject> ballotBoxRoomObjects, List<ObserverBallotBoxMapObject> mapObjects) throws Exception {
        Map<Integer, BallotBoxObject> ballotBoxObjectMap = new LinkedHashMap<>();
        for (BallotBoxRoomObject ballotBoxRoomObject : ballotBoxRoomObjects) {
            BallotBoxObject ballotBoxObject = ballotBoxObjectMap.get(ballotBoxRoomObject.getBallotBoxObject().getOid());
            if (ballotBoxObject == null) {
                ballotBoxObject = ballotBoxRoomObject.getBallotBoxObject();
                ballotBoxObjectMap.put(ballotBoxObject.getOid(), ballotBoxObject);
            }
            ballotBoxObject.addRoom(ballotBoxRoomObject);
            if (mapObjects != null) {
                for (ObserverBallotBoxMapObject observerBallotBoxMapObject : mapObjects) {
                    if (observerBallotBoxMapObject.getBallotBoxObject().getOid() == ballotBoxObject.getOid()) {
                        ballotBoxObject.addObserver(observerBallotBoxMapObject.getObserverObject());
                    }
                }
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (BallotBoxObject ballotBoxObject : ballotBoxObjectMap.values()) {
            JSONObject jsonObject = voterDataBallotBox(ballotBoxObject);
            JSONArray roomsArray = new JSONArray();
            for (BallotBoxRoomObject ballotBoxRoomObject : ballotBoxObject.getRooms()) {
                JSONObject roomObject = new JSONObject();
                roomObject.put(PARAM_OID, ballotBoxRoomObject.getOid());
                roomObject.put(PARAM_STREET, ballotBoxRoomObject.getStreet());
                roomObject.put(PARAM_TYPE, ballotBoxRoomObject.getType());
                roomObject.put(PARAM_FROM_HOUSE, ballotBoxRoomObject.getFromHouse());
                roomObject.put(PARAM_TO_HOUSE, ballotBoxRoomObject.getToHouse());
                roomObject.put(PARAM_FROM_LETTER, ballotBoxRoomObject.getFromLetter());
                roomObject.put(PARAM_TO_LETTER, ballotBoxRoomObject.getToLetter());
                roomsArray.put(roomObject);
            }
            jsonObject.put(PARAM_ROOMS, roomsArray);
            JSONArray observersArray = new JSONArray();
            if (ballotBoxObject.hasObservers()) {
                for (ObserverObject observerObject : ballotBoxObject.getObservers()) {
                    observersArray.put(observerObject.getOid());
                }
                jsonObject.put(PARAM_OBSERVERS_COUNT, ballotBoxObject.getObservers().size());
            } else {
                jsonObject.put(PARAM_OBSERVERS_COUNT, 0);
            }
            jsonObject.put(PARAM_OBSERVERS, observersArray);
            JSONObject blockJson = new JSONObject();
            if (ballotBoxObject.getBallotBoxBlockObject() != null) {
                blockJson.put(PARAM_OID ,ballotBoxObject.getBallotBoxBlockObject().getOid());
                blockJson.put(PARAM_NAME ,ballotBoxObject.getBallotBoxBlockObject().getName());
                jsonObject.put(PARAM_BALLOT_BOX_BLOCK, blockJson);
            }
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    public static JSONArray voterDataFromActivistMapObjects(List<ActivistVoterMapObject> activistVoterMapObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (ActivistVoterMapObject activistVoterMapObject : activistVoterMapObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_ACTIVIST_NAME, activistVoterMapObject.getActivist().getFullName());
            jsonObject.put(PARAM_DATE, TemplateUtils.formatDateExcludeTime(activistVoterMapObject.getDate()));
            jsonObject.put(PARAM_RELATION, activistVoterMapObject.getRelation());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray voterDataFromCalls(List<VoterCallObject> callObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (VoterCallObject callObject : callObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_DATE, TemplateUtils.formatDateExcludeSeconds(callObject.getTime()));
            jsonObject.put(PARAM_CALLER_NAME, callObject.getCallerObject().getFullName());
            jsonObject.put(PARAM_ANSWERED, callObject.isAnswered());
            jsonObject.put(PARAM_COMMENT, callObject.getComment());
            jsonObject.put(PARAM_UNANSWERED_REASON, callObject.getUnansweredReason());
            jsonObject.put(PARAM_PREVIOUS_STATUS, callObject.getPreviousStatus());
            jsonObject.put(PARAM_NEW_STATUS, callObject.getNewStatus());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONObject voterDataBallotBox (BallotBoxObject ballotBoxObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, ballotBoxObject.getOid());
        jsonObject.put(PARAM_NUMBER, ballotBoxObject.getNumberWithDot());
        jsonObject.put(PARAM_ADDRESS, ballotBoxObject.getAddress());
        jsonObject.put(PARAM_PLACE, ballotBoxObject.getPlace());
        jsonObject.put(PARAM_CITY, ballotBoxObject.getCityObject().getName());
        return jsonObject;
    }

    public static JSONArray voterDataFromSupporterVotersList(List<SupporterVotersListItemMapObject> supporterVotersListItemMapObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (SupporterVotersListItemMapObject supporterVotersListItemMapObject : supporterVotersListItemMapObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_DATE, TemplateUtils.formatDateExcludeSeconds(supporterVotersListItemMapObject.getDate()));
            jsonObject.put(PARAM_VOTER_NAME, supporterVotersListItemMapObject.getSupporterFromList().getFullName());
            jsonObject.put(PARAM_CURRENT_SUPPORT_STATUS, supporterVotersListItemMapObject.getSupporterFromList().getSupportStatus());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray blocksToJsonArray(List<BallotBoxBlockObject> blockObjects) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (BallotBoxBlockObject block : blockObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, block.getOid());
            jsonObject.put(PARAM_NAME, block.getName());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray voterCustomPropertiesToJson(List<CustomPropertyObject> customPropertyObjects, boolean meta) {
        JSONArray jsonArray = new JSONArray();
        if (customPropertyObjects != null) {
            for (CustomPropertyObject propertyObject : customPropertyObjects) {
            }
        }
        return jsonArray;
    }

    public static JSONObject customPropertyToJson(CustomPropertyObject propertyObject, List<CustomPropertyOptionObject> options, boolean meta) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, propertyObject.getOid());
        jsonObject.put(PARAM_NAME, propertyObject.getName());
        if (meta) {
            jsonObject.put(PARAM_TRANSLATION_KEY, propertyObject.getTranslationKey());
            jsonObject.put(PARAM_JSON_KEY, propertyObject.getJsonKey());
            jsonObject.put(PARAM_TYPE, propertyObject.getType());
            if (options != null && !options.isEmpty()) {
                JSONArray jsonArray = new JSONArray();
                for (CustomPropertyOptionObject option : options) {
                    jsonArray.put(customPropertyOptionToJson(option));
                }
                jsonObject.put(PARAM_OPTIONS, jsonArray);
            }
        } else {
            jsonObject.put(PARAM_VALUE, propertyObject.getValue());
        }
        return jsonObject;
    }


    public static JSONArray ballotBoxesListToJsonObject(List<BallotBoxObject> ballotBoxObjects, List<ObserverBallotBoxMapObject> mapObjects) throws Exception {
        Map<Integer, BallotBoxObject> ballotBoxObjectMap = new LinkedHashMap<>();
        for (BallotBoxObject ballotBoxObject : ballotBoxObjects) {
            ballotBoxObjectMap.put(ballotBoxObject.getOid(), ballotBoxObject);
            if (mapObjects != null) {
                for (ObserverBallotBoxMapObject observerBallotBoxMapObject : mapObjects) {
                    if (observerBallotBoxMapObject.getBallotBoxObject().getOid() == ballotBoxObject.getOid()) {
                        ballotBoxObject.addObserver(observerBallotBoxMapObject.getObserverObject());
                    }
                }
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (BallotBoxObject ballotBoxObject : ballotBoxObjectMap.values()) {
            JSONObject jsonObject = voterDataBallotBox(ballotBoxObject);
            JSONArray roomsArray = new JSONArray();
            jsonObject.put(PARAM_ROOMS, roomsArray);
            JSONArray observersArray = new JSONArray();
            if (ballotBoxObject.hasObservers()) {
                for (ObserverObject observerObject : ballotBoxObject.getObservers()) {
                    observersArray.put(observerObject.getOid());
                }
                jsonObject.put(PARAM_OBSERVERS_COUNT, ballotBoxObject.getObservers().size());
            } else {
                jsonObject.put(PARAM_OBSERVERS_COUNT, 0);
            }
            jsonObject.put(PARAM_OBSERVERS, observersArray);
            JSONObject blockJson = new JSONObject();
            if (ballotBoxObject.getBallotBoxBlockObject() != null) {
                blockJson.put(PARAM_OID ,ballotBoxObject.getBallotBoxBlockObject().getOid());
                blockJson.put(PARAM_NAME ,ballotBoxObject.getBallotBoxBlockObject().getName());
                jsonObject.put(PARAM_BALLOT_BOX_BLOCK, blockJson);
            }
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    public static JSONObject tasksStatusesToJson(List<SupporterActionObject> tasks) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_BILLBOARD, PARAM_TASK_STATUS_NONE);
        jsonObject.put(PARAM_ELECTION_ASSEMBLY, PARAM_TASK_STATUS_NONE);
        jsonObject.put(PARAM_VOLUNTEER, PARAM_TASK_STATUS_NONE);
        jsonObject.put(PARAM_CAR_STICKER, PARAM_TASK_STATUS_NONE);
        jsonObject.put(PARAM_SUPPORTERS_LIST, PARAM_TASK_STATUS_NONE);
        jsonObject.put(PARAM_ELECTION_ASSEMBLY_PARTICIPATION, PARAM_TASK_STATUS_NONE);
        for (SupporterActionObject task : tasks) {
            if (task instanceof SupporterBillboardRequestObject) {
                jsonObject.put(PARAM_BILLBOARD, task.isDone() ? PARAM_TASK_STATUS_DONE : PARAM_TASK_STATUS_PENDING);
            } else if (task instanceof SupporterElectionAssemblyRequestObject) {
                jsonObject.put(PARAM_ELECTION_ASSEMBLY, task.isDone() ? PARAM_TASK_STATUS_DONE : PARAM_TASK_STATUS_PENDING);
            } else if (task instanceof SupporterVolunteerObject) {
                jsonObject.put(PARAM_VOLUNTEER, task.isDone() ? PARAM_TASK_STATUS_DONE : PARAM_TASK_STATUS_PENDING);
            } else if (task instanceof SupporterCarStickerObject) {
                jsonObject.put(PARAM_CAR_STICKER, task.isDone() ? PARAM_TASK_STATUS_DONE : PARAM_TASK_STATUS_PENDING);
            } else if (task instanceof SupporterSupportersListDelivery) {
                jsonObject.put(PARAM_SUPPORTERS_LIST, task.isDone() ? PARAM_TASK_STATUS_DONE : PARAM_TASK_STATUS_PENDING);
            } else if (task instanceof SupporterElectionAssemblyParticipationObject) {
                jsonObject.put(PARAM_ELECTION_ASSEMBLY_PARTICIPATION, task.isDone() ? PARAM_TASK_STATUS_DONE : PARAM_TASK_STATUS_PENDING);
            }
        }
        return jsonObject;
    }


    public static JSONArray customPropertiesDataToJson(List<CustomPropertyObject> customPropertyObjects, List<CustomPropertyOptionObject> allOptions) {
        JSONArray jsonArray = new JSONArray();
        Map<Integer, List<CustomPropertyOptionObject>> optionsMap = new HashMap<>();
        for (CustomPropertyOptionObject option : allOptions) {
            List<CustomPropertyOptionObject> customPropertyOptions = optionsMap.computeIfAbsent(option.getCustomPropertyObject().getOid(), k -> new ArrayList<>());
            customPropertyOptions.add(option);
        }

        for (CustomPropertyObject propertyObject : customPropertyObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, propertyObject.getOid());
            jsonObject.put(PARAM_NAME, propertyObject.getName());
            jsonObject.put(PARAM_TYPE, propertyObject.getType());
            jsonObject.put(PARAM_TRANSLATION_KEY, propertyObject.getTranslationKey());
            jsonObject.put(PARAM_JSON_KEY, propertyObject.getJsonKey());
            addTranslationToJsonObject(jsonObject, propertyObject.getTranslationKey());
            JSONArray optionsJson = new JSONArray();
            List<CustomPropertyOptionObject> propertyOptions = optionsMap.get(propertyObject.getOid());
            if (propertyOptions != null) {
                for (CustomPropertyOptionObject optionObject : propertyOptions) {
                    optionsJson.put(customPropertyOptionToJson(optionObject));
                }
            }
            jsonObject.put(PARAM_OPTIONS, optionsJson);
            jsonArray.put(jsonObject);

        }
        return jsonArray;
    }

    private static JSONObject customPropertyOptionToJson (CustomPropertyOptionObject optionObject) {
        JSONObject option = new JSONObject();
        option.put(PARAM_OID, optionObject.getOid());
        option.put(PARAM_TRANSLATION_KEY, optionObject.getTranslationKey());
        addTranslationToJsonObject(option, optionObject.getTranslationKey());
        return option;
    }

    private static void addTranslationToJsonObject (JSONObject jsonObject, String key) {
        TranslationObject translationObject = Utils.getTranslationObject(key);
        if (translationObject != null) {
            jsonObject.put(PARAM_HEBREW, translationObject.getTranslationValue());
            jsonObject.put(PARAM_ENGLISH, translationObject.getEn());
        } else {
            jsonObject.put(PARAM_HEBREW, MINUS);
            jsonObject.put(PARAM_ENGLISH, MINUS);
        }

    }


    public static JSONArray  adminCustomPropertiesToJson (List<CustomPropertyObject> customPropertyObjects, List<AdminCustomPropertyMapObject> adminCustomPropertyMapObjects) {
        JSONArray jsonArray = new JSONArray();
        List<Integer> adminCustomPropertiesOids = new ArrayList<>();
        for (AdminCustomPropertyMapObject adminCustomPropertyMapObject : adminCustomPropertyMapObjects) {
            adminCustomPropertiesOids.add(adminCustomPropertyMapObject.getCustomPropertyObject().getOid());
        }
        for (CustomPropertyObject propertyObject : customPropertyObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, propertyObject.getOid());
            TranslationObject translationObject = Utils.getTranslationObject(propertyObject.getTranslationKey());
            jsonObject.put(PARAM_NAME, translationObject == null ? propertyObject.getName() : translationObject.getTranslationValue());
            jsonObject.put(PARAM_ACTIVE, adminCustomPropertiesOids.contains(propertyObject.getOid()));
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static void addCustomPropertiesToJson (JSONObject jsonObject, List<CustomPropertyObject> customPropertyObjects) {
        for (CustomPropertyObject customPropertyObject : customPropertyObjects) {
            jsonObject.put(customPropertyObject.getJsonKey(), customPropertyObject.getValue());
        }
    }

    public static JSONArray activistDataDailyObjectsToJson (LinkedHashMap<Integer, ActivistObject> activistDataDailyObjects) {
        JSONArray array = new JSONArray();
        int rank = 1;
        for (Integer activistOid : activistDataDailyObjects.keySet()) {
            JSONObject activistDataDaily = new JSONObject();
            ActivistObject activistObject = activistDataDailyObjects.get(activistOid);
            activistDataDaily.put(PARAM_ACTIVIST_OID, activistObject.getOid());
            activistDataDaily.put(PARAM_ACTIVIST_NAME, activistObject.getFullName());
            activistDataDaily.put(PARAM_SUPPORTERS_COUNT, activistObject.getSupportersCount());
            activistDataDaily.put(PARAM_RANK, rank);
            rank++;
            array.put(activistDataDaily);
        }
        return array;
    }

    public static JSONArray notificationsToJson (List<NotificationObject> notifications) {
        JSONArray array = new JSONArray();
        for (NotificationObject notificationObject : notifications) {
            array.put(notificationToJson(notificationObject));
        }
        return array;
    }

    public static JSONObject notificationToJson (NotificationObject notificationObject) {
        JSONObject notification = new JSONObject();
        notification.put(PARAM_OID, notificationObject.getOid());
        notification.put(PARAM_TITLE, notificationObject.getTitle());
        notification.put(PARAM_BODY, notificationObject.getBody());
        notification.put(PARAM_DATE, TemplateUtils.formatDateExcludeSeconds(notificationObject.getDate()));
        return notification;
    }

    public static JSONObject sentSmsToJson(SentSmsObject sentSmsObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, sentSmsObject.getOid());
        jsonObject.put(PARAM_TIME, Utils.formatDate(sentSmsObject.getTime(), "dd-MM-yy HH:mm:ss"));
        jsonObject.put(PARAM_PHONE, sentSmsObject.getRecipientPhone());
        jsonObject.put(PARAM_TEXT, sentSmsObject.getText());
        jsonObject.put(PARAM_FULL_NAME, sentSmsObject.getFullName() != null ? sentSmsObject.getFullName() : MINUS);
        jsonObject.put(PARAM_CONTACT_TYPE, sentSmsObject.getContactType() != null ? sentSmsObject.getContactType() : MINUS);
        jsonObject.put(PARAM_SENDER, sentSmsObject.getSenderPhone());
        return jsonObject;
    }

    public static JSONArray supportStatusChangesToJson (List<SupportStatusChangeObject> supportStatusChangeObjects) {
        JSONArray array = new JSONArray();
        for (SupportStatusChangeObject supportStatusChangeObject : supportStatusChangeObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_DATE, Utils.formatDate(supportStatusChangeObject.getDate(), "dd-MM-yyyy HH:mm"));
            jsonObject.put(PARAM_INITIATOR_FULL_NAME, supportStatusChangeObject.getInitiator().getFullName());
            jsonObject.put(PARAM_INITIATOR_TYPE, Utils.getPrintableContactType(supportStatusChangeObject.getInitiator().getClass(), true));
            jsonObject.put(PARAM_PREVIOUS_STATUS, supportStatusChangeObject.getPreviousStatus());
            jsonObject.put(PARAM_NEW_STATUS, supportStatusChangeObject.getNewStatus());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONArray activistsVotingRatesToJson (List<ActivistVotingRate> activistVotingRates) {
        JSONArray array = new JSONArray();
        for (ActivistVotingRate activistVotingRate : activistVotingRates) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, activistVotingRate.getActivistOid());
            jsonObject.put(PARAM_SUPPORTERS_COUNT, activistVotingRate.getSupportersCount());
            jsonObject.put(PARAM_VOTED, activistVotingRate.getVotedCount());
            jsonObject.put(PARAM_VOTED_PERCENT, activistVotingRate.getVotedPercent());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONArray electionDayWorkersToJson (List<BaseUser> electionDayWorkers) {
        JSONArray array = new JSONArray();
        for (BaseUser worker : electionDayWorkers) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, worker.getOid());
            jsonObject.put(PARAM_FULL_NAME, worker.getFullName());
            jsonObject.put(PARAM_USER_TYPE, Utils.getPrintableContactType(worker.getClass(), true));
            jsonObject.put(PARAM_TYPE, worker.getUserType());
            jsonObject.put(PARAM_PHONE, worker.getPhone());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONArray observerLogsToJson (List<ObserverLogItem> observerLogItems) {
        JSONArray array = new JSONArray();
        for (ObserverLogItem log : observerLogItems) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_DATE, Utils.formatDate(log.getDate(), "dd-MM-yy HH:mm:ss"));
            jsonObject.put(PARAM_TYPE, log.getType());
            jsonObject.put(PARAM_TEXT, log.getText());
            jsonObject.put(PARAM_VOTER_OID, log.getVoterOid());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONObject alertToJson(ElectionDayAlert alert) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, alert.getOid());
        jsonObject.put(PARAM_FULL_NAME, alert.getUser().getFullName());
        jsonObject.put(PARAM_TIME, Utils.formatDate(alert.getTime(), "dd-MM-yy HH:mm:ss"));
        jsonObject.put(PARAM_HANDLED, alert.isHandled());
        jsonObject.put(PARAM_TEXT, alert.getText());
        jsonObject.put(PARAM_SENDER_TYPE, Utils.getPrintableContactType(alert.getUser().getClass(), true));
        jsonObject.put(PARAM_SENDER_PHONE, alert.getUser().getPhone());
        return jsonObject;
    }

    public static JSONArray observersLogsToJson (List<ObserverLogItem> observersLogItems) {
        JSONArray array = new JSONArray();
        for (ObserverLogItem log : observersLogItems) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OBSERVER_OID, log.getObserverOid());
            jsonObject.put(PARAM_DATE, Utils.formatDate(log.getDate(), "dd-MM-yy HH:mm:ss"));
            jsonObject.put(PARAM_TYPE, log.getType());
            jsonObject.put(PARAM_TEXT, log.getText());
            jsonObject.put(PARAM_VOTER_OID, log.getVoterOid());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONObject candidateDataDailyObjectToJson (CandidateDataDailyObject candidateDataDailyObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_SUPPORTING_COUNT, candidateDataDailyObject.getSupportingCount());
        jsonObject.put(PARAM_NOT_SUPPORTING_COUNT, candidateDataDailyObject.getNotSupportingCount());
        jsonObject.put(PARAM_UNKNOWN_SUPPORT_COUNT, candidateDataDailyObject.getUnknownSupportCount());
        jsonObject.put(PARAM_UNVERIFIED_SUPPORTING_COUNT, candidateDataDailyObject.getUnverifiedSupportingCount());
        return jsonObject;
    }

    public static JSONArray translationsToJson () {
        JSONArray array = new JSONArray();
        for (TranslationObject translationObject : Utils.translations.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, translationObject.getOid());
            jsonObject.put(PARAM_KEY, translationObject.getTranslationKey());
            jsonObject.put(PARAM_VALUE, translationObject.getTranslationValue());
            jsonObject.put(PARAM_ENGLISH, translationObject.getEn());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONArray publicInquiriesToJson (List<PublicInquiryObject> publicInquiryObjects) {
        JSONArray array = new JSONArray();
        for (PublicInquiryObject inquiryObject : publicInquiryObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, inquiryObject.getOid());
            jsonObject.put(PARAM_TOPIC_DESCRIPTION, inquiryObject.getPublicInquiryTopicObject().getDescription());
            jsonObject.put(PARAM_SUBJECT, inquiryObject.getSubject());
            jsonObject.put(PARAM_CREATION_DATE, Utils.formatDate(inquiryObject.getCreationDate(), "dd-MM-yyyy HH:mm"));
            if (inquiryObject.getVoterObject() != null) {
                jsonObject.put(PARAM_FULL_NAME, inquiryObject.getVoterObject().getFullName());
            } else {
                jsonObject.put(PARAM_FULL_NAME, inquiryObject.getContactObject().getFullName());
            }
            jsonObject.put(PARAM_PHONE, inquiryObject.getContactObject().getPhone());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONObject publicInquiryToJson (PublicInquiryObject publicInquiryObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, publicInquiryObject.getOid());
        jsonObject.put(PARAM_TOPIC_DESCRIPTION, publicInquiryObject.getPublicInquiryTopicObject().getDescription());
        if (publicInquiryObject.getVoterObject() != null) {
            jsonObject.put(PARAM_FULL_NAME, publicInquiryObject.getVoterObject().getFullName());
            jsonObject.put(PARAM_PHONE, publicInquiryObject.getVoterObject().getPhone());
            jsonObject.put(PARAM_EMAIL, publicInquiryObject.getVoterObject().getEmail());
            jsonObject.put(PARAM_VOTER_OID, publicInquiryObject.getVoterObject().getOid());
        } else {
            jsonObject.put(PARAM_FULL_NAME, publicInquiryObject.getContactObject().getFullName());
            jsonObject.put(PARAM_PHONE, publicInquiryObject.getContactObject().getPhone());
            jsonObject.put(PARAM_EMAIL, publicInquiryObject.getContactObject().getEmail());
        }
        jsonObject.put(PARAM_SUBJECT, publicInquiryObject.getSubject());
        jsonObject.put(PARAM_DETAILS, publicInquiryObject.getDetails());
        jsonObject.put(PARAM_OPEN, publicInquiryObject.isOpen());
        jsonObject.put(PARAM_COMMENT, publicInquiryObject.getComment());
        jsonObject.put(PARAM_CREATION_DATE, Utils.formatDate(publicInquiryObject.getCreationDate(), "dd-MM-yyyy HH:mm"));
        if (!publicInquiryObject.isOpen() && publicInquiryObject.getCloseDate() != null) {
            jsonObject.put(PARAM_CLOSE_DATE, Utils.formatDate(publicInquiryObject.getCloseDate(), "dd-MM-yyyy HH:mm"));
        }
        return jsonObject;
    }

    public static JSONArray publicInquiriesTopicsMapToJson(List<AdminPublicInquiriesTopicsMapObject> publicInquiriesTopicsMapObjects, Map<Integer, Integer> topicsInquiriesCountMap) {
        JSONArray array = new JSONArray();
        for (AdminPublicInquiriesTopicsMapObject adminPublicInquiriesTopicsMapObject : publicInquiriesTopicsMapObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, adminPublicInquiriesTopicsMapObject.getOid());
            jsonObject.put(PARAM_TOPIC_OID, adminPublicInquiriesTopicsMapObject.getPublicInquiryTopicObject().getOid());
            jsonObject.put(PARAM_DESCRIPTION, adminPublicInquiriesTopicsMapObject.getPublicInquiryTopicObject().getDescription());
            if (topicsInquiriesCountMap != null) {
                Integer count = topicsInquiriesCountMap.get(adminPublicInquiriesTopicsMapObject.getPublicInquiryTopicObject().getOid());
                jsonObject.put(PARAM_COUNT, count == null ? 0 : count);
            } else {
                jsonObject.put(PARAM_COUNT, 0);
            }
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONArray surveysToJson (List<SurveyObject> surveyObjects) {
        JSONArray array = new JSONArray();
        for (SurveyObject surveyObject : surveyObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, surveyObject.getOid());
            jsonObject.put(PARAM_QUESTION, surveyObject.getShortQuestion());
            jsonObject.put(PARAM_CREATION_DATE,  Utils.formatDate(surveyObject.getCreationDate(), "dd-MM-yyyy HH:mm"));
            jsonObject.put(PARAM_TOTAL_RECIPIENTS, surveyObject.getRecipients());
            jsonObject.put(PARAM_PUBLISHED, surveyObject.isPublished());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONArray landingPagesToJson (List<LandingPageDataObject> landingPageDataObjects) {
        JSONArray array = new JSONArray();
        for (LandingPageDataObject landingPageDataObject : landingPageDataObjects) {
            array.put(landingPageToJson(landingPageDataObject));
        }
        return array;
    }

    public static JSONObject landingPageToJson (LandingPageDataObject landingPageDataObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, landingPageDataObject.getOid());
        jsonObject.put(PARAM_TITLE, landingPageDataObject.getTitle());
        jsonObject.put(PARAM_URL, landingPageDataObject.getUrl());
        jsonObject.put(PARAM_NAME_IN_URL, landingPageDataObject.getNameInUrl());
        jsonObject.put(PARAM_TEXT, landingPageDataObject.getText());
        return jsonObject;
    }

    public static JSONObject voterGroupsToJson(List<CustomGroupObject> customGroupObjects, List<Integer> voterGroupsOids) {
        JSONObject jsonObject = new JSONObject();
        JSONArray notMemberArray = new JSONArray();
        JSONArray memberArray = new JSONArray();
        jsonObject.put(PARAM_MEMBER, memberArray);
        jsonObject.put(PARAM_NOT_MEMBER, notMemberArray);
        for (CustomGroupObject customGroupObject : customGroupObjects) {
            JSONObject voterJson = new JSONObject();
            voterJson.put(PARAM_OID, customGroupObject.getOid());
            voterJson.put(PARAM_TITLE, customGroupObject.getDescription());
            if (voterGroupsOids.contains(customGroupObject.getOid())) {
                memberArray.put(voterJson);
            } else {
                notMemberArray.put(voterJson);
            }
        }
        return jsonObject;
    }

    public static JSONArray votersSurveyAnswersToJson (List<SurveyVoterAnswerObject> surveyVoterAnswerObjects) {
        JSONArray array = new JSONArray();
        for (SurveyVoterAnswerObject surveyVoterAnswerObject : surveyVoterAnswerObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_VOTER_OID, surveyVoterAnswerObject.getVoterObject().getOid());
            jsonObject.put(PARAM_DATE, Utils.formatDate(surveyVoterAnswerObject.getDate(), "dd-MM-yyyy HH:mm:ss"));
            jsonObject.put(PARAM_FULL_NAME, surveyVoterAnswerObject.getVoterObject().getFullName());
            jsonObject.put(PARAM_ANSWER, surveyVoterAnswerObject.getAnswerObject().getText());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONObject surveyStatsToJson (SurveyStats surveyStats) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_TOTAL_RECIPIENTS, surveyStats.getTotalRecipients());
        jsonObject.put(PARAM_TOTAL_ANSWERED, surveyStats.getTotalAnswered());
        jsonObject.put(PARAM_CREATION_DATE, surveyStats.getCreationDate());
        JSONArray answersTotal = new JSONArray();
        for (String answer : surveyStats.getTotalAnswersMap().keySet()) {
            JSONObject answerTotalJson = new JSONObject();
            answerTotalJson.put(PARAM_TEXT, answer);
            answerTotalJson.put(PARAM_COUNT, surveyStats.getTotalAnswersMap().get(answer));
            answerTotalJson.put(PARAM_PERCENT, surveyStats.getPercentAnswersMap().get(answer));
            answersTotal.put(answerTotalJson);
        }
        jsonObject.put(PARAM_ANSWERS_TOTAL, answersTotal);

        return jsonObject;
    }

    public static JSONObject groupStatsToJson (GroupStatsObject groupStatsObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_TOTAL_CALLS_MADE, groupStatsObject.getTotalCallsMade());
        jsonObject.put(PARAM_TOTAL_ANSWERED_CALLS, groupStatsObject.getTotalAnsweredCalls());
        jsonObject.put(PARAM_LAST_CALL_TIME, groupStatsObject.getLastCallTime());
        jsonObject.put(PARAM_TOTAL_VOTERS_THAT_GOT_CALL, groupStatsObject.getTotalVotersThatGotCall());
        jsonObject.put(PARAM_TOTAL_VOTERS_THAT_DID_NOT_GET_CALL, groupStatsObject.calculateTotalVotersThatDidNotGetCall());
        return jsonObject;
    }


    public static JSONArray excelUploadsToJson (List<ExcelUploadObject> excelUploadObjects) {
        JSONArray array = new JSONArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ExcelUploadObject excelUploadObject : excelUploadObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, excelUploadObject.getOid());
            jsonObject.put(PARAM_DATE, simpleDateFormat.format(excelUploadObject.getDate()));
            jsonObject.put(PARAM_ACTION, excelUploadObject.getAction());
            jsonObject.put(PARAM_TOTAL_IDS, excelUploadObject.getTotalIds());
            jsonObject.put(PARAM_EXISTING_IDS, excelUploadObject.getExistingIds());
            jsonObject.put(PARAM_NON_EXISTING_IDS, excelUploadObject.getNonExistingIds());
            jsonObject.put(PARAM_EXTRA_INFO, excelUploadObject.getExtraInfo());
            array.put(jsonObject);
        }
        return array;
    }



    public static JSONArray dynamicGroupsToJson (List<DynamicGroupObject> dynamicGroupObjects) {
        JSONArray array = new JSONArray();
        for (DynamicGroupObject dynamicGroupObject : dynamicGroupObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, dynamicGroupObject.getOid());
            jsonObject.put(PARAM_NAME, dynamicGroupObject.getTitle());
            jsonObject.put(PARAM_TOTAL_VOTERS, dynamicGroupObject.getTotalVoters());
            jsonObject.put(PARAM_TOTAL_SUPPORTERS, dynamicGroupObject.getTotalSupporters());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONObject dynamicGroupDataToJson (DynamicGroupObject dynamicGroupObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_NAME, dynamicGroupObject.getTitle());
        jsonObject.put(PARAM_CREATION_DATE, dynamicGroupObject.getCreationDate() != null ? new SimpleDateFormat().format(dynamicGroupObject.getCreationDate()) : EMPTY);
        jsonObject.put(PARAM_STATS_TIME, dynamicGroupObject.getStatsTime() != null ? new SimpleDateFormat().format(dynamicGroupObject.getStatsTime()): EMPTY);
        jsonObject.put(PARAM_TOTAL_VOTERS, dynamicGroupObject.getTotalVoters());
        jsonObject.put(PARAM_TOTAL_SUPPORTERS, dynamicGroupObject.getTotalSupporters());
        jsonObject.put(PARAM_TOTAL_UNVERIFIED_SUPPORTERS, dynamicGroupObject.getTotalUnverifiedSupporters());
        jsonObject.put(PARAM_TOTAL_UNKNOWN_SUPPORT_STATUS_VOTERS, dynamicGroupObject.getTotalUnknownSupportStatus());
        jsonObject.put(PARAM_TOTAL_NOT_SUPPORTING, dynamicGroupObject.getTotalNotSupporting());
        jsonObject.put(PARAM_FIRST_NAME, dynamicGroupObject.getFirstName());
        jsonObject.put(PARAM_LAST_NAME, dynamicGroupObject.getLastName());
        jsonObject.put(PARAM_VOTER_ID, dynamicGroupObject.getVoterId());
        jsonObject.put(PARAM_ADDRESS, dynamicGroupObject.getAddress());
        jsonObject.put(PARAM_SUPPORT_STATUS, dynamicGroupObject.getSupportStatus());
        jsonObject.put(PARAM_PHONE, dynamicGroupObject.getPhone());
        jsonObject.put(PARAM_HOME_PHONE, dynamicGroupObject.getHomePhone());
        jsonObject.put(PARAM_EXTRA_PHONE, dynamicGroupObject.getExtraPhone());
        jsonObject.put(PARAM_EMAIL, dynamicGroupObject.getEmail());
        jsonObject.put(PARAM_GENDER, dynamicGroupObject.getGender());
        jsonObject.put(PARAM_COMMENT, dynamicGroupObject.getComment());
        jsonObject.put(PARAM_LANGUAGE, dynamicGroupObject.getLanguage());
        jsonObject.put(PARAM_SUPPORT_SIGN, dynamicGroupObject.getSupportSign());
        jsonObject.put(PARAM_ALLOW_RECALL, dynamicGroupObject.getAllowRecall());
        jsonObject.put(PARAM_AGE_TYPE, dynamicGroupObject.getAgeType());
        jsonObject.put(PARAM_AGE, dynamicGroupObject.getAge());
        jsonObject.put(PARAM_HANDLING_ACTIVISTS_TYPE, dynamicGroupObject.getHandlingActivistsType());
        jsonObject.put(PARAM_HANDLING_ACTIVISTS, dynamicGroupObject.getHandlingActivists());
        jsonObject.put(PARAM_CUSTOM_PROPERTIES_VALUES, dynamicGroupObject.getCustomPropertiesValues());
        jsonObject.put(PARAM_NEEDS_RIDE, dynamicGroupObject.getNeedsRide());
        jsonObject.put(PARAM_VOTE_STATUS, dynamicGroupObject.getNeedsRide());
        jsonObject.put(PARAM_VOTE_INTENTION, dynamicGroupObject.getVoteIntention());
        jsonObject.put(PARAM_AGE_RANGE, dynamicGroupObject.getAgeRange());
        jsonObject.put(PARAM_CITY_OID, dynamicGroupObject.getCityObject() != null ? dynamicGroupObject.getCityObject().getOid() : -1);
        return jsonObject;
    }

    public static JSONObject groupsManagersToJson (GroupManagerObject groupManagerObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_FIRST_NAME, groupManagerObject.getFirstName());
        jsonObject.put(PARAM_LAST_NAME, groupManagerObject.getLastName());
        jsonObject.put(PARAM_PHONE, groupManagerObject.getPhone());
        jsonObject.put(PARAM_PASSWORD, groupManagerObject.getPassword());
        return jsonObject;
    }

    public static JSONArray activistsListToJsonObject(List<ActivistObject> activistObjects, boolean electionDay, Map<Integer, Set<Integer>> activistMasterSlaveMap, Map<Integer, Integer> activistsVotingStats) {
        JSONArray jsonArray = new JSONArray();
        for (ActivistObject activistObject : activistObjects) {
            if (activistsVotingStats != null) {
                Integer votedCount = activistsVotingStats.get(activistObject.getOid());
                activistObject.setVotedSupportersCount(votedCount == null ? 0 : votedCount);
            }
            jsonArray.put(activistToJsonObject(activistObject, electionDay, activistMasterSlaveMap));
        }
        return jsonArray;
    }

    public static JSONObject voterEditableFieldsToJson (VoterObject voterObject) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, voterObject.getOid());
        jsonObject.put(PARAM_VOTED, voterObject.getVoted());
        jsonObject.put(PARAM_ALLOW_RECALL, voterObject.getAllowRecall());
        jsonObject.put(PARAM_SUPPORT_SIGN, voterObject.getSupportSign());
        jsonObject.put(PARAM_COMMENT, voterObject.getComment());
        jsonObject.put(PARAM_LANGUAGE, voterObject.getLanguage());
        jsonObject.put(PARAM_NEEDS_RIDE, voterObject.getNeedsRide());
//        jsonObject.put(PARAM_VOTER_ID, voterObject.getVoterId());
//        jsonObject.put(PARAM_ADDRESS, voterObject.getAddress());
        if (voterObject.getBirthDate() != null) {
            jsonObject.put(PARAM_BIRTH_DATE, voterObject.getBirthDate().getTime());
        }
        jsonObject.put(PARAM_MALE, voterObject.getMale());
        jsonObject.put(PARAM_SUPPORT_STATUS, voterObject.getSupportStatus());
        jsonObject.put(PARAM_SUPPORTERS_LIST_STATUS, voterObject.getSupportersListStatus());
        jsonObject.put(PARAM_PHONE, voterObject.getPhone());
        jsonObject.put(PARAM_EMAIL, voterObject.getEmail());
//        jsonObject.put(PARAM_FIRST_NAME, voterObject.getFirstName());
//        jsonObject.put(PARAM_LAST_NAME, voterObject.getLastName());
        jsonObject.put(PARAM_HOME_PHONE, voterObject.getHomePhone());
        jsonObject.put(PARAM_EXTRA_PHONE, voterObject.getExtraPhone());
        jsonObject.put(PARAM_VOTED_ROUND_1, voterObject.getVotedRound1());
        jsonObject.put(PARAM_VOTE_INTENTION, voterObject.getVoteIntention());
        if (voterObject.isEditableCampiagnFieldsChanged()) {
            try {
                BaseCampaignEditableDataObject editableDataObject = voterObject.getBaseCampaignVoterEditableDataObject();
                JSONObject editableCampaignFieldsObject = new JSONObject();
                switch (voterObject.getCampaignType()) {
                    case PARAM_CAMPAIGN_TYPE_MUNICIPAL:
                    case PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION:
                        break;
                    case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LABOR:
                        jsonObject.put(PARAM_ISRAEL_LABOR_VOTER_EDITABLE_DATA_OBJECT, editableCampaignFieldsObject);
                        editableCampaignFieldsObject.put(PARAM_ACTIVITY_DISTRICT,  ((IsraelLaborVoterEditableDataObject) editableDataObject).getActivityDistrict());
                        editableCampaignFieldsObject.put(PARAM_ACTIVITY_BRANCH,  ((IsraelLaborVoterEditableDataObject) editableDataObject).getActivityBranch());
                        editableCampaignFieldsObject.put(PARAM_PARTY_DISTRICT,  ((IsraelLaborVoterEditableDataObject) editableDataObject).getPartyDistrict());
                        break;
                    case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LIKUD:
                        jsonObject.put(PARAM_LIKUD_VOTER_EDITABLE_DATA_OBJECT, editableCampaignFieldsObject);
                        editableCampaignFieldsObject.put(PARAM_COLLECTION_CODE,  ((LikudVoterEditableDataObject) editableDataObject).getCollectionCode());
                        editableCampaignFieldsObject.put(PARAM_BIRTH_COUNTRY, ((LikudVoterEditableDataObject) editableDataObject).getBirthCountry());
                        break;
                }
            } catch (Exception e) {
                LOGGER.error("error building json from editable campaign fields", e);
            }
        }
        return jsonObject;
    }

    public static JSONArray customPropertyObjectsToJson (List<CustomPropertyObject> customPropertyObjects) {
        JSONArray array = new JSONArray();
        for (CustomPropertyObject customPropertyObject : customPropertyObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, customPropertyObject.getOid());
            jsonObject.put(PARAM_JSON_KEY, customPropertyObject.getJsonKey());
            jsonObject.put(PARAM_VALUE, customPropertyObject.getValue());
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONObject dynamicGroupsToChartConfigJson (List<DynamicGroupObject> dynamicGroupObjects) {
        JSONObject config = new JSONObject();
        JSONArray supporters = new JSONArray();
        JSONArray unverifiedSupporters = new JSONArray();
        JSONArray swingVoters = new JSONArray();
        JSONArray notSupporting = new JSONArray();
        JSONArray groupsNames = new JSONArray();
        for (DynamicGroupObject dynamicGroupObject : dynamicGroupObjects) {
            int supportersCount = dynamicGroupObject.getTotalSupporters();
            int unverifiedSupportersCount = dynamicGroupObject.getTotalUnverifiedSupporters();
            int swingVotersCount = dynamicGroupObject.getTotalUnknownSupportStatus();
            int notSupportingCount = dynamicGroupObject.getTotalNotSupporting();
            if (supportersCount > MIN_VOTERS_FOR_GROUPS_CHART || unverifiedSupportersCount > MIN_VOTERS_FOR_GROUPS_CHART || swingVotersCount > MIN_VOTERS_FOR_GROUPS_CHART || notSupportingCount > MIN_VOTERS_FOR_GROUPS_CHART) {
                supporters.put(dynamicGroupObject.getTotalSupporters());
                unverifiedSupporters.put(dynamicGroupObject.getTotalUnverifiedSupporters());
                swingVoters.put(dynamicGroupObject.getTotalUnknownSupportStatus());
                notSupporting.put(dynamicGroupObject.getTotalNotSupporting());
                groupsNames.put(dynamicGroupObject.getTitle());
            }
        }
        config.put(PARAM_SUPPORTING, supporters);
        config.put(PARAM_UNVERIFIED_SUPPORTING, unverifiedSupporters);
        config.put(PARAM_SUPPORT_UNKNOWN, swingVoters);
        config.put(PARAM_NOT_SUPPORTING, notSupporting);
        config.put(PARAM_GROUPS_NAMES, groupsNames);
        return config;
    }

    public static JSONObject voterLoadToJson(VoterObject voterObject) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_OID, voterObject.getOid());
        jsonObject.put(PARAM_VOTER_ID, voterObject.getVoterId());
        jsonObject.put(PARAM_FIRST_NAME, voterObject.getFirstName());
        jsonObject.put(PARAM_LAST_NAME, voterObject.getLastName());
        jsonObject.put(PARAM_ADDRESS, voterObject.getCampaignAddress());
        jsonObject.put(PARAM_PHONE, voterObject.getPhone());
        jsonObject.put(PARAM_HOME_PHONE, voterObject.getHomePhone());
        jsonObject.put(PARAM_EXTRA_PHONE, voterObject.getExtraPhone());
        jsonObject.put(PARAM_GENDER,voterObject.getGenderCode());
        if (voterObject.getBirthDate() != null) {
            jsonObject.put(PARAM_BIRTH_DATE_STRING, simpleDateFormat.format(voterObject.getBirthDate()));
        }
        jsonObject.put(PARAM_SUPPORT_STATUS, voterObject.getSupportStatus());
        jsonObject.put(PARAM_SUPPORT_SIGN, voterObject.getSupportSign());
        if (voterObject.getCampaignType() == PARAM_CAMPAIGN_TYPE_MUNICIPAL || voterObject.getCampaignType() == PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION) {
            jsonObject.put(PARAM_FATHER_NAME, ((CampaignVoterObject) voterObject.getBaseCampaignVoterObject()).getFatherName());
            jsonObject.put(PARAM_VOTER_NUMBER, ((CampaignVoterObject) voterObject.getBaseCampaignVoterObject()).getVoterNumber());
        }
        jsonObject.put(PARAM_MALE, voterObject.getMale());
        jsonObject.put(PARAM_COMMENT, voterObject.getComment());
        if (voterObject.getActivistObject() != null) {
            JSONObject activistJson = new JSONObject();
            jsonObject.put(PARAM_ACTIVIST_OBJECT, activistJson);
            activistJson.put(PARAM_DELETED, voterObject.getActivistObject().isDeleted());
        }
        jsonObject.put(PARAM_EMAIL, voterObject.getEmail());
        BaseCampaignVoterObject campaignVoterObject = voterObject.getBaseCampaignVoterObject();
        if (campaignVoterObject != null) {
            JSONObject campaignVoterJson = new JSONObject();
            jsonObject.put(PARAM_CAMPAIGN_VOTER_OBJECT, campaignVoterJson);
            BallotBoxObject ballotBoxObject = campaignVoterObject.getBallotBoxObject();
            if (ballotBoxObject != null) {
                JSONObject ballotBoxJson = new JSONObject();
                campaignVoterJson.put(PARAM_BALLOT_BOX_OBJECT, ballotBoxJson);
                ballotBoxJson.put(PARAM_NUMBER, ballotBoxObject.getNumberWithDot());
                ballotBoxJson.put(PARAM_ADDRESS, ballotBoxObject.getAddress());
            }
        }
        jsonObject.put(PARAM_SUPPORTERS_LIST_STATUS, voterObject.getSupportersListStatus());
        JSONArray customPropertiesJson = new JSONArray();
        jsonObject.put(PARAM_CUSTOM_PROPERTIES, customPropertiesJson);
        jsonObject.put(PARAM_VOTE_INTENTION, voterObject.getVoteIntention());
        if (voterObject.getCustomProperties() != null) {
            for (VoterCustomPropertyObject voterCustomPropertyObject : voterObject.getCustomProperties()) {
                JSONObject property = new JSONObject();
                property.put(PARAM_OID, voterCustomPropertyObject.getCustomPropertyObject().getOid());
                property.put(PARAM_TYPE, voterCustomPropertyObject.getCustomPropertyObject().getType());
                property.put(PARAM_VALUE, voterCustomPropertyObject.getValue());
                customPropertiesJson.put(property);
            }
        }
        JSONObject adminJson = new JSONObject();
        adminJson.put(PARAM_OID, voterObject.getAdminUserObject().getOid());
        jsonObject.put(PARAM_ADMIN_USER_OBJECT, adminJson);
        if (Utils.allowElectionDayFunctionality(voterObject.getAdminUserObject())) {
            jsonObject.put(PARAM_VOTED, voterObject.hasVoted());
        }
        jsonObject.put(PARAM_LANGUAGE, voterObject.getLanguage());
        jsonObject.put(PARAM_TOTAL_HANDLING_ACTIVISTS, voterObject.getTotalHandlingActivists());
        jsonObject.put(PARAM_ACTIVIST, voterObject.isActivist());
        jsonObject.put(PARAM_NEEDS_RIDE,voterObject.getNeedsRide());
        jsonObject.put(PARAM_CAN_CALL, voterObject.canCall());
        return jsonObject;
    }

    public static JSONArray suggestionDataToJsonArray(Set<VoterObject> voters, Integer type) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (VoterObject voterObject : voters) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, voterObject.getOid());
            jsonObject.put(PARAM_FIRST_NAME, voterObject.getFirstName());
            jsonObject.put(PARAM_LAST_NAME, voterObject.getLastName());
            jsonObject.put(PARAM_VOTER_ID, voterObject.getVoterId());
            jsonObject.put(PARAM_ADDRESS, voterObject.getAddress());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray voterFromMobileDashboard (List<VoterObject> voterObjects) {
        JSONArray array = new JSONArray();
        boolean electionDay = false;
        if (!voterObjects.isEmpty()) {
            AdminUserObject adminUserObject = voterObjects.get(0).getAdminUserObject();
            if (adminUserObject != null) {
                electionDay = Utils.allowElectionDayFunctionality(adminUserObject);
            }
        }

        for (VoterObject voterObject : voterObjects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, voterObject.getOid());
            jsonObject.put(PARAM_FIRST_NAME, voterObject.getFirstName());
            jsonObject.put(PARAM_LAST_NAME, voterObject.getLastName());
            jsonObject.put(PARAM_VOTER_ID, voterObject.getVoterId());
            jsonObject.put(PARAM_PHONE, voterObject.getPhone());
            jsonObject.put(PARAM_SUPPORT_STATUS, voterObject.getSupportStatus());
            if (electionDay) {
                jsonObject.put(PARAM_VOTED, voterObject.hasVoted());
            }
            array.put(jsonObject);
        }
        return array;
    }

    public static JSONArray votersForPollingStationToJsonObject(List<VoterObject> voterObjectList) throws Exception {
        JSONArray jsonArray = new JSONArray();
        if (!voterObjectList.isEmpty()) {
            VoterObject voter = voterObjectList.get(0);
            int campaignType = voter.getCampaignType();
            boolean electionDay = Utils.allowElectionDayFunctionality(voter.getAdminUserObject());
            for (VoterObject voterObject : voterObjectList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(PARAM_OID, voterObject.getOid());
                jsonObject.put(PARAM_FIRST_NAME, voterObject.getFirstName());
                jsonObject.put(PARAM_LAST_NAME, voterObject.getLastName());
                jsonObject.put(PARAM_VOTER_ID, voterObject.getVoterId());
                jsonObject.put(PARAM_PHONE, voterObject.getPhone());
                jsonObject.put(PARAM_SUPPORT_STATUS, voterObject.getSupportStatus());
                if (campaignType == PARAM_CAMPAIGN_TYPE_MUNICIPAL || campaignType == PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION || campaignType == PARAM_CAMPAIGN_TYPE_LAWYERS) {
                    jsonObject.put(PARAM_VOTER_NUMBER, ((CampaignVoterObject)voterObject.getBaseCampaignVoterObject()).getVoterNumber());
                }
                if (electionDay) {
                    jsonObject.put(PARAM_VOTED, voterObject.hasVoted());
                }
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray;
    }


    public static JSONObject adminPollingStationStatsToJson(AdminPollingStationStatsObject adminPollingStationStatsObject) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PARAM_STATS_TIME, simpleDateFormat.format(adminPollingStationStatsObject.getStatsTime()));
        jsonObject.put(PARAM_TOTAL_VOTERS, adminPollingStationStatsObject.getTotalVoters());
        jsonObject.put(PARAM_TOTAL_SUPPORTERS, adminPollingStationStatsObject.getTotalSupporters());
        jsonObject.put(PARAM_TOTAL_UNVERIFIED_SUPPORTERS, adminPollingStationStatsObject.getTotalUnverifiedSupporters());
        jsonObject.put(PARAM_TOTAL_UNKNOWN_SUPPORT_STATUS_VOTERS, adminPollingStationStatsObject.getTotalUnknownSupportStatus());
        jsonObject.put(PARAM_TOTAL_NOT_SUPPORTING, adminPollingStationStatsObject.getTotalNotSupporting());
        jsonObject.put(PARAM_TOTAL_VOTED, adminPollingStationStatsObject.getTotalVoted());
        jsonObject.put(PARAM_PERCENT_VOTED, adminPollingStationStatsObject.getPercentVoted());
        jsonObject.put(PARAM_PERCENT_SUPPORTERS, adminPollingStationStatsObject.getPercentSupporters());
        jsonObject.put(PARAM_TOTAL_SUPPORTERS_VOTED, adminPollingStationStatsObject.getTotalSupportersVoted());
        jsonObject.put(PARAM_PERCENT_VOTED_SUPPORTERS, adminPollingStationStatsObject.getPercentVotedSupporters());
        BallotBoxObject ballotBoxObject = adminPollingStationStatsObject.getBallotBoxObject();
        JSONObject ballotBoxJson = new JSONObject();
        ballotBoxJson.put(PARAM_NUMBER, ballotBoxObject.getNumberWithDot());
        ballotBoxJson.put(PARAM_CITY, ballotBoxObject.getCityObject().getName());
        ballotBoxJson.put(PARAM_ADDRESS, ballotBoxObject.getAddress());
        ballotBoxJson.put(PARAM_PLACE, ballotBoxObject.getPlace());
        jsonObject.put(PARAM_BALLOT_BOX, ballotBoxJson);
        return jsonObject;
    }

    public static JSONArray observersListToAutocompleteJsonObject(List<ObserverObject> observerObjectList) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (ObserverObject observerObject : observerObjectList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_OID, observerObject.getOid());
            jsonObject.put(PARAM_NAME, observerObject.getFullName());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }


    public static JSONArray pollingStationsListToJsonObject(
            List<AdminPollingStationStatsObject> pollingStationStatsObjects,
            boolean electionDay,
            List<Integer> pollingStationsWithCheckedInObservers
    ) {
        JSONArray jsonArray = new JSONArray();
        for (AdminPollingStationStatsObject adminPollingStationStatsObject : pollingStationStatsObjects) {
            JSONObject pollingStationJson = new JSONObject();
            BallotBoxObject ballotBoxObject = adminPollingStationStatsObject.getBallotBoxObject();
            pollingStationJson.put(PARAM_OID, ballotBoxObject.getOid());
            pollingStationJson.put(PARAM_NUMBER, ballotBoxObject.getNumberWithDot());
            pollingStationJson.put(PARAM_ADDRESS, ballotBoxObject.getAddress());
            pollingStationJson.put(PARAM_CITY, ballotBoxObject.getCityObject().getName());
            pollingStationJson.put(PARAM_PLACE, ballotBoxObject.getPlace());
            pollingStationJson.put(PARAM_VOTERS_COUNT, adminPollingStationStatsObject.getTotalVoters());
            pollingStationJson.put(PARAM_SUPPORTERS_COUNT, adminPollingStationStatsObject.getTotalSupporters());
            pollingStationJson.put(PARAM_SUPPORTERS_PERCENT, String.format("%.2f%%",Utils.calculatePercent(adminPollingStationStatsObject.getTotalSupporters(), adminPollingStationStatsObject.getTotalVoters())));
            if (electionDay) {
                pollingStationJson.put(PARAM_VOTED_VOTERS_COUNT, adminPollingStationStatsObject.getTotalVoted());
                pollingStationJson.put(PARAM_CHECKED_IN_OBSERVER, pollingStationsWithCheckedInObservers.contains(ballotBoxObject.getOid()));
            }
            jsonArray.put(pollingStationJson);
        }
        return jsonArray;
    }

    public static JSONArray cityPollingStationsListToJsonObject(
            List<AdminPollingStationStatsObject> pollingStationStatsObjects
    ) {
        JSONArray jsonArray = new JSONArray();
        for (AdminPollingStationStatsObject adminPollingStationStatsObject : pollingStationStatsObjects) {
            JSONObject pollingStationJson = new JSONObject();
            BallotBoxObject ballotBoxObject = adminPollingStationStatsObject.getBallotBoxObject();
            pollingStationJson.put(PARAM_NUMBER, ballotBoxObject.getNumberWithDot());
            pollingStationJson.put(PARAM_ADDRESS, ballotBoxObject.getAddress());
            pollingStationJson.put(PARAM_OID, ballotBoxObject.getOid());
            pollingStationJson.put(PARAM_PLACE, ballotBoxObject.getPlace());
            jsonArray.put(pollingStationJson);
        }
        return jsonArray;
    }


}
