package com.elector.Sms;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.General.BaseContact;
import com.elector.Sms.model.request.*;
import com.elector.Sms.model.response.BalanceRs;
import com.elector.Sms.model.response.Response;
import com.elector.Utils.ConfigUtils;
import com.elector.Utils.Http;
import com.elector.Utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import static com.elector.Utils.Definitions.EMPTY;
import static com.elector.Utils.Definitions.MINUS;

@Service
@Profile("new_sms_api")
public class NewSmsApi implements SmsApi {
    private static final Logger LOGGER = LoggerFactory.getLogger("smsApiLogger");
    //    private static final String URL = "https://www.019sms.co.il:8090/api/test"; //DEV URL
    private static final String URL = "https://www.019sms.co.il/api"; //PROD URL
    private static String USER_ID = ConfigUtils.getConfig(ConfigEnum.new_sms_api_username, "elector464sms");
    private static String PASSWORD = ConfigUtils.getConfig(ConfigEnum.new_sms_api_password, "Shai4646Elector");
    private static final User USER = new User(USER_ID, PASSWORD);
    private static Map<Integer, Set<String>> contactsFromServiceMap = new HashMap<>();

    @PostConstruct
    public void init() {
//        new Thread(this::requestContacts).start();
    }

    private final String FAKE_NUMBER = "0551234567";

    private <T> T getResponse(String xml, Class<T> clazz) {
        T response = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            response = (T) jaxbUnmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            LOGGER.error("Error while parsing response xml", e);
        }
        return response;
    }

    public int createGroup(String groupName) {
        NewCL newCL = new NewCL();
        newCL.setUser(USER);

        ContactList cl = new ContactList();
        cl.setName(groupName);

        //This api can't add just group without numbers.
        //Add one stub destination
        Destination destination = new Destination();
        destination.setPhone(FAKE_NUMBER);
        cl.getDestinatons().add(destination);
        newCL.setCl(cl);

        StringWriter sw = new StringWriter();
        JAXB.marshal(newCL, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "createGroup", URL, sw.toString()));
        String xmlResponse = Http.post(URL, sw.toString());
//        LOGGER.info(String.format("method: %s, response: %s", "createGroup", xmlResponse));
        Response response = getResponse(xmlResponse, Response.class);
        if (response.getStatus() == 0 && !response.getIdentifiers().isEmpty()) {
            return response.getIdentifiers().get(0);
        } else {
            throw new RuntimeException("Error response from sms server:" + response);
        }
    }

    public boolean addContactToGroup(int groupId, String email, String firstName, String lastName,
                                     String city, String cellPhone) {
        AddNumCL asddCL = new AddNumCL();
        asddCL.setUser(USER);

        ContactList cl = new ContactList();
        cl.setId(groupId);
        Destination destination = new Destination();
        destination.setPhone(cellPhone);
        destination.setDf1(email);
        destination.setDf2(firstName);
        destination.setDf3(lastName);
        destination.setDf4(city);
        cl.getDestinatons().add(destination);
        asddCL.setCl(cl);

        StringWriter sw = new StringWriter();
        JAXB.marshal(asddCL, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "addContactToGroup", URL, sw.toString()));
        String xmlResponse = Http.post(URL, sw.toString());
//        LOGGER.info(String.format("method: %s, response: %s", "addContactToGroup", xmlResponse));
        Response response = getResponse(xmlResponse, Response.class);
        return response.getStatus() == 0;
    }

    private void addDestination(ContactList cl, String phone, BaseContact baseContact) {
        Destination destination = new Destination();
        destination.setPhone(phone);
        destination.setDf1(baseContact.getEmail());
        destination.setDf2(baseContact.getFirstName());
        destination.setDf3(baseContact.getLastName());
        destination.setDf4(EMPTY);
        cl.getDestinatons().add(destination);
    }

    public boolean addContactsToGroup(int groupId, List<BaseContact> contacts) {
        AddNumCL asddCL = new AddNumCL();
        asddCL.setUser(USER);
        ContactList cl = new ContactList();
        cl.setId(groupId);
        for (BaseContact baseContact : contacts) {
            if (Utils.isValidCellPhone(baseContact.getPhone())) {
                addDestination(cl, baseContact.getPhone(), baseContact);
            }
            if (Utils.isValidCellPhone(baseContact.getHomePhone())) {
                addDestination(cl, baseContact.getHomePhone(), baseContact);
            }
            if (Utils.isValidCellPhone(baseContact.getExtraPhone())) {
                addDestination(cl, baseContact.getExtraPhone(), baseContact);
            }
        }
        asddCL.setCl(cl);

        StringWriter sw = new StringWriter();
        JAXB.marshal(asddCL, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "addContactsToGroup", URL, sw.toString()));
        String xmlResponse = Http.post(URL, sw.toString());
        Response response = getResponse(xmlResponse, Response.class);
//        LOGGER.info(String.format("method: %s, response: %s", "addContactsToGroup", xmlResponse));
        return response.getStatus() == 0;
    }

    public boolean addContactToGroup(int groupId, String email, String firstName, String lastName, String city, String cellPhone, String birthDate) {
        AddNumCL asdNumCL = new AddNumCL();
        asdNumCL.setUser(USER);

        ContactList cl = new ContactList();
        cl.setId(groupId);
        Destination destination = new Destination();
        destination.setPhone(cellPhone);
        destination.setDf1(email);
        destination.setDf2(firstName);
        destination.setDf3(lastName);
        destination.setDf4(city);
        destination.setDf5(birthDate);
        cl.getDestinatons().add(destination);
        asdNumCL.setCl(cl);

        StringWriter sw = new StringWriter();
        JAXB.marshal(asdNumCL, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "addContactToGroup", URL, sw.toString()));
        String xmlResponse = Http.post(URL, sw.toString());
//        LOGGER.info(String.format("method: %s, response: %s", "addContactToGroup", xmlResponse));
        Response response = getResponse(xmlResponse, Response.class);
        return response.getStatus() == 0;
    }

    public boolean sendSmsToGroup(String campaignName, String fromNumber, String text, int groupId) {
        LOGGER.info("invoked NewSmsApi.sendSmsToGroup");
        Sms sms = new Sms();
        sms.setUser(USER);
        sms.setSource(fromNumber);
        sms.setMessage(text);
        sms.getDestinations().add(groupId);

        StringWriter sw = new StringWriter();
        JAXB.marshal(sms, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "sendSmsToGroup", URL, sw.toString()));
        String xmlResponse = Http.post(URL, sw.toString());
        Response response = getResponse(xmlResponse, Response.class);
//        LOGGER.info(String.format("method: %s, response: %s", "sendSmsToGroup", xmlResponse));
        LOGGER.info(String.format("group sms: groupId: %s, request params: %s, response: %s", groupId, sw.toString(), response));
        return response.getStatus() == 0;
    }

    private int addSmsCampaign(String name, String fromNumber, String text) {
        throw new UnsupportedOperationException();
    }

    boolean sendSmsCampaign(String groupId, String smsCampaignId) {
        throw new UnsupportedOperationException();
    }

    public boolean removeContactFromGroup(int groupId, String phone) {
        RmNumCL rmNumCL = new RmNumCL();
        rmNumCL.setUser(USER);

        ContactList cl = new ContactList();
        cl.setId(groupId);
        Destination destination = new Destination();
        destination.setPhone(phone);
        cl.getDestinatons().add(destination);
        rmNumCL.setCl(cl);

        StringWriter sw = new StringWriter();
        JAXB.marshal(rmNumCL, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "removeContactFromGroup", URL, sw.toString()));
        String xmlResponse = Http.post(URL, sw.toString());
        Response response = getResponse(xmlResponse, Response.class);
//        LOGGER.info(String.format("method: %s, response: %s", "removeContactFromGroup", xmlResponse));
        return response.getStatus() == 0;
    }

    public boolean removeContactsFromGroup(int groupId, List<String> phones) {
        RmNumCL rmNumCL = new RmNumCL();
        rmNumCL.setUser(USER);

        ContactList cl = new ContactList();
        cl.setId(groupId);
        Destination destination = new Destination();
        for (String phone : phones) {
            destination.setPhone(phone);
        }
        cl.getDestinatons().add(destination);
        rmNumCL.setCl(cl);

        StringWriter sw = new StringWriter();
        JAXB.marshal(rmNumCL, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "removeContactsFromGroup", URL, sw.toString()));
        String xmlResponse = Http.post(URL, sw.toString().replace("<destination>", "").replace("</destination>", ""));
//        LOGGER.info(String.format("method: %s, response: %s", "removeContactsFromGroup", xmlResponse));
        Response response = getResponse(xmlResponse, Response.class);
        return response.getStatus() == 0;
    }

    public boolean sendSingleSms(String fromNumber, String toNumber, String text) {
        SingleSms sms = new SingleSms();
        sms.setUser(USER);
        sms.setSource(fromNumber);
        sms.setMessage(text);
        sms.getDestinations().add(toNumber);

        StringWriter sw = new StringWriter();
        JAXB.marshal(sms, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "sendSingleSms", URL, sw.toString()));

        String xmlResponse = Http.post(URL, sw.toString());
        Response response = getResponse(xmlResponse, Response.class);
//        LOGGER.info(String.format("method: %s, response: %s", "sendSingleSms", xmlResponse));
        return response.getStatus() == 0;
    }


    public List<String> getContactsEmailsFromGroup(int groupId) {
        throw new UnsupportedOperationException();
    }

    public List<String> getContactsPhonesFromGroup(int groupId) {
        Set<String> contacts = contactsFromServiceMap.get(groupId);
        return contacts != null ? new ArrayList<>(contacts) : new ArrayList<>();
    }

    public boolean isPhoneExistsInGroup(String phoneToCheck, int groupId) {
        boolean exists = false;
        if (contactsFromServiceMap != null) {
            Set<String> numbers = contactsFromServiceMap.get(groupId);
            if (numbers != null) {
                if (numbers.contains(phoneToCheck.replaceAll(MINUS, EMPTY).substring(1))) {
                    exists = true;
                }
            }
        }
        return exists;
    }


    public boolean clearGroup(int groupId) {
        RemoveCL removeCL = new RemoveCL();
        removeCL.setUser(USER);

        ContactList cl = new ContactList();
        cl.setId(groupId);
        removeCL.setCl(cl);

        StringWriter sw = new StringWriter();
        JAXB.marshal(removeCL, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "clearGroup", URL, sw.toString()));

        String xmlResponse = Http.post(URL, sw.toString());
        Response response = getResponse(xmlResponse, Response.class);
//        LOGGER.info(String.format("method: %s, response: %s", "clearGroup", xmlResponse));
        return response.getStatus() == 0;
    }

    public int getSmsBalance() {
        Balance balance = new Balance();
        balance.setUser(USER);

        StringWriter sw = new StringWriter();
        JAXB.marshal(balance, sw);
//        LOGGER.info(String.format("method: %s, url: %s, query string: %s", "getSmsBalance", URL, sw.toString()));

        String xmlResponse = Http.post(URL, sw.toString());
//        LOGGER.info(String.format("method: %s, response: %s", "getSmsBalance", xmlResponse));

        BalanceRs response = getResponse(xmlResponse, BalanceRs.class);
        if (response.getStatus() == 0) {
            return response.getBalance();
        } else {
            throw new RuntimeException("Error while getting balance");
        }
    }

//    //runs every 2 hours
//    @Scheduled(cron = "0 0 0/2 ? * *")
//    public void requestContacts() {
//        LOGGER.info("requesting contacts");
//        long start = System.currentTimeMillis();
//        GetCL getCL = new GetCL();
//        getCL.setUser(USER);
//        StringWriter sw = new StringWriter();
//        JAXB.marshal(getCL, sw);
//        String xmlResponse = null;
//        xmlResponse = Http.post(URL, sw.toString());
//        Map<Integer, Set<String>> tempContactsMap = new HashMap<>();
//        if (xmlResponse != null) {
//            try {
//                JSONArray jsonArray = XML.toJSONObject(xmlResponse).getJSONObject("sms").getJSONObject("contact_lists").getJSONArray("contact_list");
//                for (Object object : jsonArray) {
//                    JSONObject contactData = (JSONObject) object;
//                    int groupIdFromJson = Integer.valueOf((contactData.get("cl_id").toString()));
//                    String phoneNumberFromJson = String.valueOf(contactData.get("phone"));
//                    Set<String> phonesFromJson = tempContactsMap.computeIfAbsent(groupIdFromJson, k -> new HashSet<>());
//                    phonesFromJson.add(phoneNumberFromJson);
//                }
//                contactsFromServiceMap = tempContactsMap;
//
//            } catch (Exception e) {
//                LOGGER.error(String.format("error parsing xml response, xml: %s", xmlResponse));
//            }
//        }
//        LOGGER.info(String.format("contacts refreshed, request took: %s", Utils.timePassedAsString(System.currentTimeMillis() - start)));
//    }

    public static boolean contactsMapHasValues() {
        return !contactsFromServiceMap.isEmpty();
    }

    public Set<String> getContactListByID(Integer id) {

        GetCLbyID cLByID = new GetCLbyID();
        ContactList contactList = new ContactList();
        contactList.setId(id);
        cLByID.setUser(USER);
        cLByID.setCl(contactList);
        String xmlResponse = null;
        Set<String> phoneNumbers = new HashSet<>();
        StringWriter sw = new StringWriter();
        try {
            JAXB.marshal(cLByID, sw);
            xmlResponse = Http.post(URL, sw.toString());
            JSONArray jsonArray = XML.toJSONObject(xmlResponse).getJSONObject("sms").getJSONObject("contact_lists").getJSONArray("contact_list");
            for (Object object : jsonArray) {
                JSONObject contactData = (JSONObject) object;
                if (contactData.has("PHONE")) {
                    String phoneNumberFromJson = String.valueOf(contactData.get("PHONE"));
                    phoneNumbers.add(phoneNumberFromJson);
                }
            }
        } catch (Exception e) {
            LOGGER.error("error getContactListByID", e);
        }
        return phoneNumbers;

    }


}
