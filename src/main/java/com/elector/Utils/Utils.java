package com.elector.Utils;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.Entities.*;
import com.elector.Objects.General.AdditionalCampaignFieldObject;
import com.elector.Objects.General.BaseEntity;
import com.elector.Objects.General.BaseUser;
import com.elector.Objects.General.GeneralManagerUserObject;
import com.elector.Services.GeneralManager;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfig;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Definitions.FIELD_TYPE_CHECKBOX;
import static com.elector.Utils.Definitions.FIELD_TYPE_DROPDOWN;

/**
 * Created by Sigal on 5/16/2016.
 */
@Component
public class Utils {

    public static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    @Autowired
    public TemplateUtils templateUtils;

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private VelocityConfig velocityConfig;

    public static Map<String, TranslationObject> translations = null;
    private static Map<String, Long> blockedIpsMap = new ConcurrentHashMap<>();
    private static Map<String, Integer> requestsPerIpMap = new ConcurrentHashMap<>();
    public static boolean enableBlocking = true;
    public static StandardPBEStringEncryptor encryptor;
    private static Map<Integer, Integer> totalVotersCallsByAdmin = new HashMap<>();
    private static List<AdditionalCampaignFieldObject.Option> likudCollectionCodeOptions = new ArrayList<>();
    public static Map<Integer, Set<Integer>> adminsPollingStationsWithObserversMap = new HashMap<>();


    @PostConstruct
    private void initialize() {
        new Thread(() -> {
            long start = System.currentTimeMillis();
            translations = generalManager.getAllTranslations();
            encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(ConfigUtils.getConfig(ConfigEnum.encryption_key, EMPTY));
        }).start();
    }

    @Scheduled(fixedRate = 2 * MINUTE)
    public void loadPollingStationsWithObservers () {
        adminsPollingStationsWithObserversMap = generalManager.getPollingStationsThatHasObserversMap();
    }



    public static long calculateObjectSize(Object object) {
        return ObjectSizeCalculator.getObjectSize(object);
    }

    public static long calculateStringSize(Object object) {
        return object.toString().getBytes(Charsets.UTF_8).length;
    }

    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString("UTF-8");
        return outStr;
    }

    private int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public Date getCurrentMonthFirstDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        LocalDateTime now = LocalDateTime.now();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), getMonth(date), 1);
        return cal.getTime();
    }

    public Date getNextMonthFirstDate(Date date) {
        int month = getMonth(date);
        int nextMonth = 0;
        if (month == 12) {
            nextMonth = 1;
        } else {
            nextMonth = ++month;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), nextMonth, 1);
        return cal.getTime();
    }


    private String getCookieValueByName(HttpServletRequest request, String name) {
        String value = "0";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return hasText(value) ? value : "0";
    }

    public Date dateWithNoTime(Date dateWithTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateWithTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date yesterdayWithNoTime(Date dateWithTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateWithTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public long timeToNextBirthday(Date now, Date birthday) {
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(now);
        calendarNow.set(Calendar.HOUR_OF_DAY, 0);
        calendarNow.set(Calendar.MINUTE, 0);
        calendarNow.set(Calendar.SECOND, 0);
        calendarNow.set(Calendar.MILLISECOND, 0);
        Calendar calendarBirthday = Calendar.getInstance();
        calendarBirthday.setTime(birthday);
        calendarBirthday.set(Calendar.SECOND, 1);
        calendarBirthday.set(Calendar.YEAR, calendarNow.get(Calendar.YEAR));
        if (calendarNow.after(calendarBirthday)) {
            calendarBirthday.add(Calendar.YEAR, 1);
        }
        return calendarBirthday.getTimeInMillis() - calendarNow.getTimeInMillis();
    }

    public boolean isToday(Date date) {
        return dateWithNoTime(new Date()).equals(dateWithNoTime(date));
    }

    public boolean isTomorrow(Date date) {
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(new Date());
        calendarNow.add(Calendar.DAY_OF_MONTH, 1);
        return dateWithNoTime(calendarNow.getTime()).equals(dateWithNoTime(date));
    }

    public List<Object> jsonToListOfObjects(String data, Class className) {
        List<Object> objectList = null;
        try {
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createParser(data);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
            objectList = mapper.readValue(parser, mapper.getTypeFactory().constructCollectionType(List.class, className));
        } catch (Exception e) {
            LOGGER.error("jsonToListOfObjects", e);
        }
        return objectList;
    }

    public static boolean hasText(String str) {
        return str != null && str.length() > 0;
    }

    public boolean hasElements(List list) {
        return list != null && !list.isEmpty();
    }

    public void setDefaultParameters(HttpServletRequest request, HttpServletResponse response, JSONObject results) {
        results.put(PARAM_SITE_NAME, ConfigUtils.getConfig(ConfigEnum.site_name, "Elector"));
    }

    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

    public static String dateStringWithNoTime(Date date) {
        return date == null ? MINUS : new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public <T extends BaseUser> Class<T> getUserClassByType(int userType) {
        Class baseUserClass = null;
        switch (userType) {
            case PARAM_ADMIN_USER_TYPE_SUPER:
            case PARAM_ADMIN_USER_TYPE_CANDIDATE:
                baseUserClass = AdminUserObject.class;
                break;
            case PARAM_ADMIN_USER_TYPE_CALLER:
                baseUserClass = CallerObject.class;
                break;
            case PARAM_ADMIN_USER_TYPE_OBSERVER:
                baseUserClass = ObserverObject.class;
                break;
            case PARAM_ADMIN_USER_TYPE_ACTIVIST:
                baseUserClass = ActivistObject.class;
                break;
            case PARAM_ADMIN_USER_TYPE_DRIVER:
                baseUserClass = DriverObject.class;
                break;
        }
        return baseUserClass;
    }

    public boolean canLoginAs(BaseUser masterUser, BaseUser slaveUser) {
        return (masterUser.getAdminUserObject() == null && masterUser.getType() == PARAM_ADMIN_USER_TYPE_SUPER) || (slaveUser.getAdminUserObject() != null && slaveUser.getAdminUserObject().getOid() == masterUser.getOid());
    }

    public static String getTranslation(String key) {
        return translations != null && translations.get(key) != null ? translations.get(key).getTranslationValue() : key;
    }

    public static String getTranslation(String key, Integer language) {
        return translations != null && translations.get(key) != null ? translations.get(key).getTranslation(language) : key;
    }

    public String electionTimeFrontendFormat(Date electionDate) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(electionDate);
        return String.format("%s-%s-%s-%s-%s", calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.YEAR));
    }

    public static boolean isElectionOpen(Date electionDate) {
        return electionDate.before(new Date());
    }

    public static boolean isElectionOpen(CampaignObject campaignObject) {
        return campaignObject.getDate().before(new Date()) && campaignObject.getEndDate().after(new Date());
    }

    public boolean isDatesSameDay(Date date1, Date date2) {
        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = GregorianCalendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    public Date getMidnightDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static JSONObject invokeUrlGet(String domain, Map<String, Object> params) {
        String response = EMPTY;
        StringBuilder paramsBuilder = new StringBuilder();
        try {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value != null) {
                    paramsBuilder.append(AND).append(key).append(EQUALS).append(Utils.getEncodedString(value.toString()));
                }
            }
            HttpURLConnection con = (HttpURLConnection) new URL(domain + paramsBuilder.toString()).openConnection();
            con.setRequestMethod(HTTP_REQUEST_GET);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            int responseCode = con.getResponseCode();
            if (responseCode != HttpStatus.SC_OK) {
                LOGGER.info(String.format("Response Code: %s, domain: %s, params: %s", responseCode, domain, paramsBuilder.toString()));
            } else {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder responseBuilder = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    responseBuilder.append(inputLine);
                }
                in.close();
                response = responseBuilder.toString();
            }
        } catch (IOException e) {
            LOGGER.error(String.format("invokeUrlGet url: %s, params: %s", domain, paramsBuilder), e);
        }
        return !Utils.hasText(response) ? new JSONObject() : new JSONObject(response);
    }

    public String parseVelocity(ModelMap model, String file) {
        String text = EMPTY;
        try {
            text = VelocityEngineUtils.mergeTemplateIntoString(velocityConfig.getVelocityEngine(), file, UTF_8, model);
        } catch (Exception e) {
            LOGGER.error("parseVelocity", e);
        }
        return text;
    }

    public static String generateRandomPassword() {
        return RandomStringUtils.random(8, false, true);
    }

    public static <T> String getPrintableContactType(Class<T> clazz, boolean translation) {
        String contactType = EMPTY;
        if (clazz.equals(ActivistObject.class)) {
            contactType = "activist";
        } else if (clazz.equals(CallerObject.class)) {
            contactType = "caller";
        } else if (clazz.equals(ObserverObject.class)) {
            contactType = "observer";
        } else if (clazz.equals(VoterObject.class)) {
            contactType = "voter";
        } else if (clazz.equals(AdminUserObject.class)) {
            contactType = "campaign.manager";
        } else if (clazz.equals(ObserverObject.class)) {
            contactType = "observer";
        }
        return translation ? getTranslation(contactType) : contactType;
    }

    public static String formatDate(Date date, String format) {
        return date == null ? EMPTY : new SimpleDateFormat(format).format(date);
    }

    public static long yearsBetween(Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        LocalDate start = LocalDate.of(year, month, day);
        cal.setTime(endDate);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        LocalDate end = LocalDate.of(year, month, day);
        return ChronoUnit.YEARS.between(start, end);
    }

    public static boolean isFileType(String fileName, int requiredType) {
        List<String> typesToAllow = new ArrayList<>();
        switch (requiredType) {
            case FILE_TYPE_IMAGE:
                typesToAllow = Arrays.asList("png", "jpg", "jpeg");
                break;
            case FILE_TYPE_SPREADSHEET:
                typesToAllow = Arrays.asList("csv", "xls", "xlsx");
                break;
            case FILE_TYPE_TEXT:
                typesToAllow = Arrays.asList("txt");
                break;
        }
        return typesToAllow.contains(FilenameUtils.getExtension(fileName));
    }

    private static String symbolRemove(String symbol, String string) {
        return string != null ? string.replace(symbol, EMPTY).trim() : EMPTY;
    }

    public static String formatPhoneNumber(String phone) {
        return symbolRemove(MINUS, phone);
    }


    public <T extends BaseEntity> Class<T> getClassByType(int type) {
        Class entityClass = null;
        switch (type) {
            case ENTITY_VOTER:
                entityClass = VoterObject.class;
                break;
            case ENTITY_ACTIVIST:
                entityClass = ActivistObject.class;
                break;
            case ENTITY_CALLER:
                entityClass = CallerObject.class;
                break;
            case ENTITY_OBSERVER:
                entityClass = ObserverObject.class;
                break;
            case ENTITY_DRIVER:
                entityClass = DriverObject.class;
                break;
            case ENTITY_DONATION:
                entityClass = DonationObject.class;
                break;
            case ENTITY_ADMIN:
                entityClass = AdminUserObject.class;
                break;
            case ENTITY_BLOCK:
                entityClass = BallotBoxBlockObject.class;
                break;
        }
        return entityClass;
    }

    public static Date tryParseDate(String dateString) {
        Date date = null;
        if (dateString != null) {
            List<String> dateFormats = Arrays.asList
                    ("dd-MM-yy", "dd-MM-yyyy", "dd/MM/yy", "dd/MM/yyyy", "dd.MM.yy", "dd.MM.yyyy");
            for (String format : dateFormats) {
                try {
                    date = new SimpleDateFormat(format).parse(dateString);
                    break;
                } catch (ParseException e) {
                }
            }
        }
        return date;

    }

    public static String getStringFromList(List<String> stringList, Integer index) {
        String value = null;
        if (index != null) {
            value = stringList.get(index);
            if (value.equals(EMPTY)) {
                value = null;
            }
        }
        return value;
    }

    public static  <T extends SupporterActionObject> Class<T> getSupporterActionClassByType(int actionType) {
        Class baseActionType = null;
        switch (actionType) {
            case SUPPORTER_ACTION_TYPE_BILLBOARD:
                baseActionType = SupporterBillboardRequestObject.class;
                break;
            case SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY:
                baseActionType = SupporterElectionAssemblyRequestObject.class;
                break;
            case SUPPORTER_ACTION_TYPE_VOLUNTEER:
                baseActionType = SupporterVolunteerObject.class;
                break;
            case SUPPORTER_ACTION_TYPE_SUPPORTERS_LIST:
                baseActionType = SupporterSupportersListDelivery.class;
                break;
            case SUPPORTER_ACTION_TYPE_CAR_STICKER:
                baseActionType = SupporterCarStickerObject.class;
                break;
            case SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY_PARTICIPATION:
                baseActionType = SupporterElectionAssemblyParticipationObject.class;
                break;

        }
        return baseActionType;
    }

    public boolean equalPhoneNumbers(String phone1, String phone2) {
        return phone1.replace(MINUS, EMPTY).trim().equals(phone2.replace(MINUS, EMPTY).trim());
    }

    public <T extends BaseEntity> Class<T> getClassByGroupType(int groupType) {
        Class entityClass = null;
        switch (groupType) {
            case GROUP_TYPE_ACTIVISTS:
                entityClass = ActivistObject.class;
                break;
            case GROUP_TYPE_CALLERS:
                entityClass = CallerObject.class;
                break;
            case GROUP_TYPE_OBSERVERS:
                entityClass = ObserverObject.class;
                break;
            case GROUP_TYPE_VOTERS:
            case GROUP_TYPE_SUPPORTERS:
            case GROUP_TYPE_UNVERIFIED_SUPPORTERS:
            case GROUP_TYPE_UNKNOWN_SUPPORT_STATUS:
            case GROUP_TYPE_NOT_SUPPORTING:
                entityClass = VoterObject.class;
                entityClass = VoterObject.class;
                break;
            case GROUP_TYPE_DRIVERS:
                entityClass = DriverObject.class;
                break;
        }
        return entityClass;
    }

    public static String getIpFromRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!(hasText(ip))) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static int ballotBoxType(String description) {
        int type = -1;
        description = description.trim();
        if (description.equals("כולו")) {
            type = BALLOT_BOX_TYPE_ALL;
        } else if (description.equals("רציף")) {
            type = BALLOT_BOX_TYPE_CONTINUOUS;
        } else if (description.equals("זוגי")) {
            type = BALLOT_BOX_TYPE_EVEN;
        } else if (description.equals("אי-זוגי")) {
            type = BALLOT_BOX_TYPE_ODD;
        } else if (description.equals("1 אות משפ'")) {
            type = BALLOT_BOX_TYPE_ONE_LAST_NAME;
        } else if (description.equals("-")) {
            type = BALLOT_BOX_TYPE_MINUS;
        } else if (description.equals("2 אותיות משפ'")) {
            type = BALLOT_BOX_TWO_LAST_NAME;
        } else if (description.equals("1 משפ' + 1 פרטי")) {
            type = BALLOT_BOX_ONE_FIRST_ONE_LAST;
        } else if (description.equals("2 משפ' + 2 פרטי")) {
            type = BALLOT_BOX_TWO_FIRST_TWO_LAST;
        } else {
            System.out.println("-----" + description);
        }
        return type;
    }

    public static String eliminateLeadingZeros(String source) {
        return source.replaceFirst("^0+(?!$)", EMPTY);
    }

    public static String reformatVoterId(String voterId) {
        voterId = voterId.replaceAll(MINUS, EMPTY).trim();
        if (StringUtils.isNumeric(voterId)) {
            if (voterId.length() != 9) {
                if (voterId.length() == 8) {
                    voterId = "0" + voterId;
                } else {
                    voterId = EMPTY;
                }
            }
        } else {
            voterId = EMPTY;
        }
        return voterId;
    }

    public static String reformatPhone(String phone) {
        boolean invalidPhone = false;
        phone = phone.replace(MINUS, EMPTY).trim();
        if (!phone.startsWith("0")) {
            phone = "0" + phone;
        }
        if (phone.startsWith("05") && phone.length() != 10) {
            invalidPhone = true;
        }
        if (!phone.startsWith("05") && phone.length() != 9) {
            invalidPhone = true;
        }
        if (invalidPhone) {
            phone = EMPTY;
        }
        return phone;
    }

    public static List<String> jsonArrayToStringList(String data) {
        List<String> result = new ArrayList<>();
        if (data != null) {
            data = data.replace("[", EMPTY).replace("]", EMPTY);
            result = Arrays.asList(data.split(COMMA));
        }
        return Arrays.asList(data.split(COMMA));
    }

    public static Field[] getAllEntityFields(Class clazz) {
        Field[] fields = new Field[0];
        do {
            fields = (Field[]) ArrayUtils.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();

        } while (!clazz.equals(BaseEntity.class));
        return fields;
    }

    public static boolean isValidPhone(String phone) {
        return isValidCellPhone(phone) || isValidHomePhone(phone);
    }

    public static boolean isValidCellPhone(String phone) {
        return phone != null && Pattern.compile("^05[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]").matcher(phone).find() && phone.trim().length() == 10;
    }

    public static boolean isValidHomePhone(String phone) {
        return hasText(phone);
    }

    public static List<String> stringToStringList(String data) {
        List<String> stringList = new ArrayList<>();
        try {
            if (hasText(data)) {
                List<String> tempStrings = Arrays.asList(data.split(COMMA));
                for (String temp : tempStrings) {
                    if (hasText(temp)) {
                        stringList.add(temp);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("stringToStringList", e);
        }
        return stringList;
    }

    public static List<Integer> stringToIntegerList(String data) {
        List<Integer> integerList = new ArrayList<>();
        try {
            if (hasText(data)) {
                List<String> tempStrings = Arrays.asList(data.split(COMMA));
                for (String temp : tempStrings) {
                    if (hasText(temp)) {
                        integerList.add(Integer.valueOf(temp));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("stringToIntegerList", e);
        }
        return integerList;
    }

    public static String getRequestIP(HttpServletRequest request) {
        String ip = null;
        try {
            String header = request.getHeader("x-forwarded-for");
            ip = hasText(header) ? header : request.getRemoteAddr();
        } catch (Exception e) {
            LOGGER.error(String.format("cannot get ip from url: %s, query string: %s, x-forwarded-for: %s", request.getRequestURL(), request.getQueryString(), request.getHeader("x-forwarded-for")), e);
        }
        return ip;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        list = Lists.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String getPrintableSupportStatus(int supportStatus) {
        String text = EMPTY;
        switch (supportStatus) {
            case PARAM_SUPPORT_STATUS_UNKNOWN:
                text = Utils.getTranslation("support.unknown");
                break;
            case PARAM_SUPPORT_STATUS_SUPPORTING:
                text = Utils.getTranslation("supporting");
                break;
            case PARAM_SUPPORT_STATUS_NOT_SUPPORTING:
                text = Utils.getTranslation("not.supporting");
                break;
            case PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING:
                text = Utils.getTranslation("unverified.supporting");
                break;
        }
        return text;
    }

    public static void addTranslation(TranslationObject translationObject) {
        translations.put(translationObject.getTranslationKey(), translationObject);
    }

    public static TranslationObject getTranslationObject(String key) {
        return translations != null && translations.get(key) != null ? translations.get(key) : null;
    }

    public static int calculateAge(Date birthDate) {
        int age = 0;
        if ((birthDate != null)) {
            age = Period.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getYears();
        }
        return age;
    }

    public static String getDecodedString(String source) {
        String decoded = EMPTY;
        try {
            decoded = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(String.format("error decoding string %s", source), e);
            decoded = URLDecoder.decode(source);
        }
        return decoded;
    }

    public static String getEncodedString(String source) {
        String encoded = EMPTY;
        try {
            encoded = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(String.format("error encoding string %s", source), e);
            encoded = URLEncoder.encode(source);
        }
        return encoded;
    }

    public static String getMD5String(String source) {
        String result = EMPTY;
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(source.getBytes("UTF-8"));
            StringBuilder ret = new StringBuilder(bytes.length);
            for (byte bt : bytes) {
                String hex = Integer.toHexString(0x0100 + (bt & 0x00FF)).substring(1);
                ret.append(hex.length() < 2 ? "0" : "").append(hex);
            }
            result = ret.toString();
        } catch (Exception e) {
            LOGGER.error(String.format("error generating md5 string from %s", source), e);

        }
        return result;
    }

    public static String timePassedAsString(long millis) {
        return String.format("%d minutes and %d seconds",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public static void printRequestData(HttpServletRequest request) {
        LOGGER.info(String.format(
                "request data: url - %s, query string - %s, ip - %s, referrer - %s, user-agent: %s",
                request.getRequestURI(),
                request.getQueryString(),
                getIpFromRequest(request),
                request.getHeader("referer"),
                request.getHeader("User-Agent")
        ));
    }

    public static String getPhoneWithoutCountryCodePrefix(String phone) {
        phone = phone.trim();
        if ((phone.length() == 13 && phone.startsWith("+972"))) {
            phone = phone.substring(4);
        } else if ((phone.length() == 12 && phone.startsWith("972"))) {
            phone = phone.substring(3);
        }
        return phone;
    }

    public static boolean isAllowedIp(VisitObject visitObject,ConfigEnum ipConfigEnum) {
        boolean isAllowed = false;
        String strIps = ConfigUtils.getConfig(ipConfigEnum, EMPTY);
        if (strIps != null && !strIps.trim().equals(EMPTY)) {
            List<String> ips = Arrays.asList(strIps.split(COMMA));
            if (ips.contains(visitObject.getIp())) {
                isAllowed = true;
            }
        }
        return isAllowed;
    }

    public static String stringWithSign (int number) {
        return String.format(number > 0 ? "+%s" : "%s", number);
    }

    public static String getPrintableNeedsRideStatus(int needsRideStatus) {
        String text = EMPTY;
        switch (needsRideStatus) {
            case PARAM_NEED_RIDE_STATUS_UNKNOWN:
                text = Utils.getTranslation("unknown");
                break;
            case PARAM_NEED_RIDE_STATUS_YES:
                text = Utils.getTranslation("yes");
                break;
            case PARAM_NEED_RIDE_STATUS_NO:
                text = Utils.getTranslation("no");
                break;
        }
        return text;
    }

    public static void sleep (int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("sleep", e);
        }
    }

    public static boolean allowElectionDayFunctionality(GeneralManagerUserObject generalManagerUserObject) {
        return (
                generalManagerUserObject.isSimulation()) ||
                ConfigUtils.getConfig(ConfigEnum.force_election_day_functionality, false) ||
                isAdminOnElectionDay(generalManagerUserObject.getOid());
    }

    public List<Integer> entitiesToOidsList (List<? extends BaseEntity> entities) {
        List<Integer> oids = new ArrayList<>();
        if (entities != null) {
            oids = entities.stream().map(BaseEntity::getOid).collect(Collectors.toList());
        }
        return oids;
    }

    private Integer incrementAndGetRequestsCount (String ip) {
        Integer count = 0;
        if (hasText(ip)) {
            count = requestsPerIpMap.get(ip);
            if (count == null) {
                count = 0;
            }
            count++;
            requestsPerIpMap.put(ip, count);
        }
        return count;
    }




    public void clearAllBlocked () {
        blockedIpsMap.clear();
    }

    public void disableBlocking () {
        enableBlocking = false;
    }

    public String removeAllUnicodeChars (String str) {
        return str.replaceAll("\\P{Print}", EMPTY);
    }

    public static String integerListToText (Collection<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (!list.isEmpty()) {
                for (Integer number : list) {
                    stringBuilder.append(number).append(COMMA);
                }
            }
        } catch (Exception e) {
            LOGGER.error("integerListToText", e);
        }
        return stringBuilder.toString();
    }

    public static void zipFolder(String zipFileDestination, String sourceFolder) {
        byte[] buffer = new byte[1024];
        File folder = new File(sourceFolder);
        String source = new File(sourceFolder).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFileDestination);
            zos = new ZipOutputStream(fos);
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file: files) {
                    ZipEntry ze = new ZipEntry(source + File.separator + file.getName());
                    zos.putNextEntry(ze);
                    try (FileInputStream in = new FileInputStream(sourceFolder + File.separator + file.getName())) {
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                    }
                }
            }
            zos.closeEntry();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String unansweredReasonToText (Integer code) {
        String text = EMPTY;
        if (code != null) {
            switch (code) {
                case PARAM_CALL_UNANSWERED_REASON_UNANSWERED:
                    text = "voter.did.not.answer";
                    break;
                case PARAM_CALL_UNANSWERED_REASON_PHONE_OFF:
                    text = "phone.off";
                    break;
                case PARAM_CALL_UNANSWERED_REASON_LINE_NOT_CONNECTED:
                    text = "line.not.connected";
                    break;
                case PARAM_CALL_UNANSWERED_REASON_CALL_REJECTED:
                    text = "call.rejected";
                    break;
                case PARAM_CALL_UNANSWERED_REASON_OTHER:
                    text = "other";
                    break;
            }

        }
        return  getTranslation(text);
    }

    public static String encrypt(String str){
        int code;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            code = Math.round((float) Math.random()*8+1);
            result.append(code).append(Integer.toHexString(((int) str.charAt(i)) ^ code)).append("-");
        }
        return result.substring(0, result.lastIndexOf("-"));
    }

    public static String decrypt(String str){
        str = str.replace("-", "");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i+=3) {
            String hex =  str.substring(i+1, i+3);
            result.append((char) (Integer.parseInt(hex, 16) ^ (Integer.parseInt(String.valueOf(str.charAt(i))))));
        }
        return result.toString();
    }

    public static String addLeadingZerosToVoterId(String voterId) {
        voterId = voterId.replaceAll(MINUS, EMPTY).trim();
        if (StringUtils.isNumeric(voterId)) {
            StringBuilder voterIdBuilder = new StringBuilder(voterId);
            while (voterIdBuilder.length() < 9) {
                voterIdBuilder.insert(0, "0");
            }
            voterId = voterIdBuilder.toString();
        } else {
            voterId = EMPTY;
        }
        return voterId;
    }

    public static String stringListToText (Collection<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (!list.isEmpty()) {
                for (String text : list) {
                    stringBuilder.append(text).append(COMMA);
                }
            }
        } catch (Exception e) {
            LOGGER.error("stringListToText", e);
        }
        return stringBuilder.toString();
    }

    public static String reformatPhoneNumber (String phone) {
        String formattedPhone = null;
        if (Utils.hasText(phone)) {
            phone = phone.replaceAll(MINUS, EMPTY).trim();
            if (!phone.equals("0")) {
                if (phone.length() > 0) {
                    phone = Utils.getPhoneWithoutCountryCodePrefix(phone);
                    if (!Utils.isValidCellPhone(phone.replaceAll(MINUS, EMPTY)) && Utils.isValidPhone("0" + phone.replaceAll(MINUS, EMPTY))) {
                        phone = "0" + phone;
                    }
                    formattedPhone = phone;
                }
            } else {
                formattedPhone = EMPTY;
            }
        }
        return formattedPhone;
    }

    public static String eliminateDuplicatedHeadAndTailQuotes(String string) {
        string = string.trim();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if ((i == 0 && c == '"') || //is leading quotes
                    (i == (string.length() - 1) && c == '"') || //is trailing quotes
                    (i < (string.length() - 1) && c == '"' && string.charAt(i + 1) == '"') ||//is duplicate quotes
                    (i < (string.length() - 1) && c == '"' && string.charAt(i + 1) == ' ')) { // is quotes before space
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static String cleanString (String source) {
        return source.replace("\uFEFF", EMPTY).replace("\u0000", EMPTY).trim();
    }

    public Integer getTotalCallsByAdmin (int adminOid) {
        Integer totalCalls = totalVotersCallsByAdmin.get(adminOid);
        if (totalCalls == null) {
            addTotalVotesCallsToMap(adminOid);
            totalCalls = totalVotersCallsByAdmin.get(adminOid);
        }
        return totalCalls;
    }

    private void addTotalVotesCallsToMap (int adminOid) {
        Long totalCalls = generalManager.countVotersCalls(adminOid);
        int totalCallsInt = 0;
        if (totalCalls != null) {
            totalCallsInt = totalCalls.intValue();
        }
        totalVotersCallsByAdmin.put(adminOid, totalCallsInt);
    }



    public static String calculateMaximalBirthDate (int years) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.YEAR, years * (-1));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(calendar.getTime());
    }


    public static String getApplicationIp () {
        String ip = EMPTY;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            LOGGER.error("cannot get application ip");
        }
        return ip;
    }

    public void notifyActivistsAboutSupporterVoted(VoterObject voterObject) {
        new Thread(() -> {
            try {
//                List<ActivistObject> activistObjects = votersManager.getActivistsForVoter(voterObject.getOid());
//                if (!activistObjects.isEmpty()) {
//                    VoterObject voterWithData = (voterObject.getAdminUserObject() == null || voterObject.getFullName() == null) ? generalManager.loadObject(VoterObject.class, voterObject.getOid()) : voterObject;
//                    List<String> tokens = new ArrayList<>();
//                    NotificationObject notificationObject = new NotificationObject(
//                            String.format("%s הצביע!", voterWithData.getFullName()),
//                            String.format("%s שנמצא ברשימת התומכים שלך כבר הצביע. המשך לתומכים האחרים ברשימתך.", voterWithData.getFullName()),
//                            voterWithData.getAdminUserObject());
//                    generalManager.save(notificationObject);
//                    List<UserNotificationObject> userNotificationObjects = new ArrayList<>();
//                    for (ActivistObject activistObject : activistObjects) {
//                        tokens.add(activistObject.getMobileRegistrationToken());
//                        userNotificationObjects.add(new UserNotificationObject(activistObject, notificationObject, true));
//                    }
//                    generalManager.save(userNotificationObjects);
//                }
            } catch (Exception e) {
                LOGGER.error(String.format("error notify activist about supporter voted, oid: %s", voterObject.getOid()), e);
            }
        }).start();
    }

    public static double calculatePercent (int amount, int total) {
        return total == 0 ? 0 : (float)(((float)amount / (float)total) * 100);
    }

    public static boolean isObserverConnectedToVoterPollingStation (VoterObject voterObject) {
        boolean pollingStationHasObserver = false;
        int pollingStationOid = 0;
        Set<Integer> adminPollingStationsWithObservers = null;
        try {
            pollingStationOid = voterObject.getBaseCampaignVoterObject().getBallotBoxObject().getOid();
            int adminOid = voterObject.getAdminUserObject().getOid();
            adminPollingStationsWithObservers = adminsPollingStationsWithObserversMap.get(adminOid);
            pollingStationHasObserver = adminPollingStationsWithObservers != null && adminPollingStationsWithObservers.contains(pollingStationOid);
        } catch (Exception e) {
            LOGGER.error("error checking if voter {} has observer in its polling station", voterObject.getOid(), e);
        }
        return pollingStationHasObserver;
    }

    public static boolean isAdminOnElectionDay(int adminOid) {
        String oidsToForceElectionDayString = ConfigUtils.getConfig(ConfigEnum.election_day_oids, EMPTY);
        List<Integer> oidsToForceElectionDay = Utils.stringToIntegerList(oidsToForceElectionDayString);
        return oidsToForceElectionDay.contains(adminOid);
    }


}
