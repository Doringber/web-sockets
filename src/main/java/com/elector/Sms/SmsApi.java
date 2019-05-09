package com.elector.Sms;

import com.elector.Objects.General.BaseContact;

import java.util.List;

public interface SmsApi {

    int createGroup(String groupName);

    boolean addContactToGroup(int groupId, String email, String firstName, String lastName,
                                     String city, String cellPhone);

    boolean addContactToGroup(int groupId, String email, String firstName, String lastName,
                              String city, String cellPhone, String birthDate);

    boolean sendSmsToGroup (String campaignName, String fromNumber, String text, int groupId);

    boolean removeContactFromGroup(int groupId, String phone);

    boolean sendSingleSms(String fromNumber, String toNumber, String text);

    List<String> getContactsEmailsFromGroup(int groupId);

    List<String> getContactsPhonesFromGroup(int groupId);

    boolean clearGroup(int groupId);

    int getSmsBalance();

    boolean addContactsToGroup(int groupId, List<BaseContact> contacts);

    boolean removeContactsFromGroup(int groupId, List<String> phones);

    public boolean isPhoneExistsInGroup(String phoneToCheck, int groupId);

//    public void requestContacts ();

}