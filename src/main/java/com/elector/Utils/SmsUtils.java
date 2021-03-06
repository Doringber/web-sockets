package com.elector.Utils;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.Entities.*;
import com.elector.Objects.General.BaseContact;
import com.elector.Objects.General.BaseUser;
import com.elector.Objects.General.EmailObject;
import com.elector.Services.GeneralManager;
import com.elector.Sms.SmsApi;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.util.*;

import static com.elector.Utils.Definitions.*;
import static org.springframework.util.StringUtils.hasText;

@Component
public class SmsUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsUtils.class);

    @Autowired
    private Utils utils;

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private SmsApi smsApi;


//    @Autowired
//    private SyncContactsGroupJob syncContactsGroupJob;

    public static final int INVALID_ID = -1;


    private Map<Integer, Set<String>> groupsToPhonesOfVotedVoters = new HashMap<>();

    private Boolean allowSendingSms (AdminUserObject adminUserObject) {
        return (adminUserObject.getOid() == SUPER_ADMIN_OID || adminUserObject.canSendSms(1)) && ConfigUtils.getConfig(ConfigEnum.allow_send_sms, false);
    }

//    public int sendSms(List<SentSmsObject> sentSmsObjectList) {
//        int sentCount = 0;
//        for (SentSmsObject sentSmsObject : sentSmsObjectList) {
//            if (sendSms(sentSmsObject)) {
//                sentCount++;
//            }
//        }
//        return sentCount;
//    }

    public boolean createContactsGroupsForAllTypes(AdminUserObject adminUserObject) {
        return (createContactsGroup(adminUserObject, GROUP_TYPE_ACTIVISTS) &&
                createContactsGroup(adminUserObject, GROUP_TYPE_CALLERS) &&
                createContactsGroup(adminUserObject, GROUP_TYPE_OBSERVERS) &&
                createContactsGroup(adminUserObject, GROUP_TYPE_VOTERS) &&
                createContactsGroup(adminUserObject, GROUP_TYPE_SUPPORTERS) &&
                createContactsGroup(adminUserObject, GROUP_TYPE_DRIVERS) &&
                createContactsGroup(adminUserObject, GROUP_TYPE_UNVERIFIED_SUPPORTERS) &&
                createContactsGroup(adminUserObject, GROUP_TYPE_UNKNOWN_SUPPORT_STATUS) &&
                createContactsGroup(adminUserObject, GROUP_TYPE_NOT_SUPPORTING)
        );
    }

    public boolean createContactsGroup (AdminUserObject adminUserObject, int groupType) {
        boolean error = false;
        if (ConfigUtils.getConfig(ConfigEnum.prod, false)) {
            String groupNameInPulseemService = createGroupNameForService(adminUserObject.getOid(), groupType);
            int groupId = smsApi.createGroup(groupNameInPulseemService);
            if (groupId == INVALID_ID) {
                error = true;
                LOGGER.warn(String.format("createContactsGroup, could not create sms group for adminOid %s, userType: %s", adminUserObject.getOid(), groupType));
            } else {
                String groupTypeString = EMPTY;
                switch (groupType) {
                    case GROUP_TYPE_ACTIVISTS:
                        groupTypeString = "Activists";
                        break;
                    case GROUP_TYPE_CALLERS:
                        groupTypeString = "Callers";
                        break;
                    case GROUP_TYPE_OBSERVERS:
                        groupTypeString = "Observers";
                        break;
                    case GROUP_TYPE_VOTERS:
                        groupTypeString = "Voters";
                        break;
                    case GROUP_TYPE_SUPPORTERS:
                        groupTypeString = "Supporters";
                        break;
                    case GROUP_TYPE_UNVERIFIED_SUPPORTERS:
                        groupTypeString = "Unverified Supporters";
                        break;
                    case GROUP_TYPE_UNKNOWN_SUPPORT_STATUS:
                        groupTypeString = "Unknown Support Status";
                        break;
                    case GROUP_TYPE_NOT_SUPPORTING:
                        groupTypeString = "Not Supporting";
                        break;

                }
                String description = String.format("%s_%s", adminUserObject.getFullName(), groupTypeString);
                ContactsGroupObject contactsGroupObject = new ContactsGroupObject(adminUserObject, groupId, groupNameInPulseemService, description, groupNameInPulseemService);
                generalManager.updateObject(contactsGroupObject);
            }
        }
        return !error;
    }

    public static String createGroupNameForService (int adminOid, int groupType) {
        String groupTypeText = EMPTY;
        switch (groupType) {
            case GROUP_TYPE_ACTIVISTS:
                groupTypeText = "activists";
                break;
            case GROUP_TYPE_CALLERS:
                groupTypeText = "callers";
                break;
            case GROUP_TYPE_OBSERVERS:
                groupTypeText = "observers";
                break;
            case GROUP_TYPE_VOTERS:
                groupTypeText = "voters";
                break;
            case GROUP_TYPE_SUPPORTERS:
                groupTypeText = "supporters";
                break;
            case GROUP_TYPE_DRIVERS:
                groupTypeText = "drivers";
                break;
            case GROUP_TYPE_UNVERIFIED_SUPPORTERS:
                groupTypeText = "unverified_supporters";
                break;
            case GROUP_TYPE_UNKNOWN_SUPPORT_STATUS:
                groupTypeText = "unknown_support_status";
                break;
            case GROUP_TYPE_NOT_SUPPORTING:
                groupTypeText = "not_supporting";
                break;
        }
        return String.format("%s_%s_%s", adminOid, groupTypeText, ConfigUtils.getConfig(ConfigEnum.prod, false) ? "prod" : "dev");
    }

    public boolean removeContactFromGroup(int groupId, String fakeEmail) {
        return smsApi.removeContactFromGroup(groupId, fakeEmail);
    }

    public List<String> getFakeEmailForGroup (int adminOid ,int groupType) {
        List<String> fakeEmails = new ArrayList<>();
        String groupNameInService = createGroupNameForService(adminOid, groupType);
        ContactsGroupObject contactsGroupObject = generalManager.getContactsGroupByNameInService(groupNameInService);
        if (contactsGroupObject != null) {
            fakeEmails = smsApi.getContactsEmailsFromGroup(contactsGroupObject.getGroupId());
        }
        return fakeEmails;

    }

    public int getContactsGroupId (int adminOid ,int groupType) {
        int groupId = INVALID_ID;
        String groupNameInService = createGroupNameForService(adminOid, groupType);
        ContactsGroupObject contactsGroupObject = generalManager.getContactsGroupByNameInService(groupNameInService);
        if (contactsGroupObject != null) {
            groupId = contactsGroupObject.getGroupId();
        }
        return groupId;
    }

    public boolean sendSmsToGroup (AdminUserObject adminUserObject, String text, int groupType) {
        LOGGER.info("invoked SmsUtils.sendSmsToGroup");
        boolean success = false;
        String groupNameInService = createGroupNameForService(adminUserObject.getOid(), groupType);
        LOGGER.info(String.format("name of group in service: %s", groupNameInService));
        ContactsGroupObject contactsGroupObject = generalManager.getContactsGroupByNameInService(groupNameInService);
        if (contactsGroupObject != null) {
            LOGGER.info(String.format("found group in db for %s, oid: %s", groupNameInService, contactsGroupObject.getOid()));
            if (!allowSendingSms(adminUserObject)) {
                LOGGER.info(String.format("sms sending not allowed, oid: %s", contactsGroupObject.getOid()));
                success = true;
            } else {
                LOGGER.info(String.format("allow sending sms to group with oid: %s", contactsGroupObject.getOid()));
                success = smsApi.sendSmsToGroup(adminUserObject.getFullName() + System.currentTimeMillis(), adminUserObject.getSmsSenderNameToShow(), text, contactsGroupObject.getGroupId());
            }
        } else {
            LOGGER.info(String.format("group not found in db for %s", groupNameInService));
        }
        return success;
    }

    public void updatedJsonSmsData (JSONObject results, AdminUserObject adminUserObject) {
        JSONObject smsDataJson = new JSONObject();
        smsDataJson.put(PARAM_TOTAL_PURCHASED_SMS, adminUserObject.getTotalSmsPurchased());
        smsDataJson.put(PARAM_TOTAL_USED_SMS, adminUserObject.getUsedSms());
        smsDataJson.put(PARAM_TOTAL_REMAINING_SMS, adminUserObject.getRemainingSms());
        results.put(PARAM_SMS_DATA, smsDataJson);
    }

    public int getSmsBalance() {
        return smsApi.getSmsBalance();
    }

    public List<String> getContactsPhonesFromGroup(int adminOid, int groupType) {
        return smsApi.getContactsPhonesFromGroup(getContactsGroupId(adminOid, groupType));
    }

    public boolean addContactsToGroup(int adminOid, int groupType, List<BaseContact> contacts) {
        return smsApi.addContactsToGroup(getContactsGroupId(adminOid, groupType), contacts);
    }

    public boolean removeContactsFromGroup(int adminOid, int groupType, List<String> phones) {
        return smsApi.removeContactsFromGroup(getContactsGroupId(adminOid, groupType), phones);
    }

    public boolean isPhoneExistsInGroup(int adminOid, int groupType, String phoneToCheck) {
        return smsApi.isPhoneExistsInGroup(phoneToCheck, getContactsGroupId(adminOid, groupType));
    }

//    public void refreshContacts () {
//        smsApi.requestContacts();
//    }

    public Map<Integer, Set<String>> getAndClearGroupsToPhonesOfVotedVoters () {
        groupsToPhonesOfVotedVoters.put(123, new HashSet<>(Arrays.asList("123")));
        Map<Integer, Set<String>> cloned = new HashMap<>(groupsToPhonesOfVotedVoters);
        groupsToPhonesOfVotedVoters.clear();
        return cloned;
    }

    public void addVotedVoter (VoterObject voted, int adminOid) {
        int groupId = getVotedGroupId(adminOid);
        Set<String> numbers = groupsToPhonesOfVotedVoters.computeIfAbsent(groupId, k -> new HashSet<>());
        if (Utils.hasText(voted.getPhone())) {
            numbers.add(voted.getPhone());
        }
        if (Utils.hasText(voted.getHomePhone())) {
            numbers.add(voted.getHomePhone());
        }
        if (Utils.hasText(voted.getExtraPhone())) {
            numbers.add(voted.getExtraPhone());
        }
    }

    private int getVotedGroupId (int adminOid) {
        //TODO: get group id by adminOid and support status
        return 1;
    }



}
