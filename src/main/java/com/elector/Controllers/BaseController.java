package com.elector.Controllers;

import com.elector.Controllers.CustomPropertiesEditors.CustomBooleanEditor;
import com.elector.Enums.ConfigEnum;
import com.elector.Objects.Entities.AdminUserObject;
import com.elector.Objects.Entities.GroupManagerObject;
import com.elector.Objects.Entities.SimulationObject;
import com.elector.Objects.General.BaseUser;
import com.elector.Services.GeneralManager;
import com.elector.Utils.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.elector.Utils.Definitions.*;

@Component
public class BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected Utils utils;

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private SmsUtils smsUtils;

    protected static Map<String, BaseUser> sessionsMap = new HashMap<>();
    public static ConcurrentHashMap<String, Integer> requestsStatsMap = new ConcurrentHashMap<>();
    private static Set<String> invalidSessions = new HashSet<>();
    protected static List<String> requestsData = new ArrayList<>();

    private boolean test = false;


    protected boolean isLoggedIn (String sessionId) {
        return sessionsMap.containsKey(sessionId);
    }

    protected boolean isLoggedIn (JSONObject results) {
        return results != null && results.get(PARAM_SESSION_ID) != null && isLoggedIn(String.valueOf(results.get(PARAM_SESSION_ID)));
    }

    protected boolean isUserOwner(String sessionId, int oid) {
        return isLoggedIn(sessionId) && sessionsMap.get(sessionId).getOid() == oid;
    }

    protected boolean isUserOwner(JSONObject results, Integer oid) {
        boolean success = false;
        try {
            if (oid != null) {
                List<String> requestUrls = getRequestUrls();
                BaseUser user = sessionsMap.get(String.valueOf(results.get(PARAM_SESSION_ID)));
                if (user != null) {
                    AdminUserObject adminUserObject = (user instanceof AdminUserObject) ? (AdminUserObject)user : user.getAdminUserObject();
                    success = test || (
                                    adminUserObject.isActive() &&
                                    isLoggedIn(results) &&
                                    user.getOid() == oid &&
                                    FiltersUtils.isUserTypeAllowed(user.getUserType(),
                                            requestUrls)
                    );
                }
            }
        } catch (JSONException e) {
            LOGGER.error(String.format("isUserOwner, oid: %s", oid), e);
            results.put(PARAM_ERROR, true);
        }
        return success;
    }

    protected boolean allowAccessToAdminUserData (JSONObject results, int adminOid) {
        boolean allow = false;
        List<String> requestUrls = getRequestUrls();
        BaseUser user = sessionsMap.get(String.valueOf(results.get(PARAM_SESSION_ID)));
        if (user != null) {
            AdminUserObject adminUserObject = (user instanceof AdminUserObject) ? (AdminUserObject)user : user.getAdminUserObject();
            allow = test || isUserOwner(results, adminOid) || (
                    adminUserObject.isActiveUserWithCampaign() &&
                            belongsToSuperUser(results, adminOid) &&
                            isLoggedIn(results)
            );
        }
        return allow;
    }

    private boolean belongsToSuperUser (JSONObject results, int adminOid) {
        boolean belongs = false;
        BaseUser user = sessionsMap.get(String.valueOf(results.get(PARAM_SESSION_ID)));
        if (user.getAdminUserObject().getOid() == adminOid) {
            belongs = true;
        }
        return belongs;
    }

    @SuppressWarnings("unchecked")
    protected  <T extends BaseUser> T getUserObject(Class<T> clazz, JSONObject results) {
        return (T) sessionsMap.get(String.valueOf(results.get(PARAM_SESSION_ID)));
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
        binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor());
    }

    private void requestStats(HttpServletRequest request) {
        try {
            LOGGER.info(String.format("request: %s", request.getRequestURI()));
            String uri = request.getRequestURI();
            Integer count = requestsStatsMap.get(uri);
            if (count == null) {
                count = 0;
            }
            count = count + 1;
            requestsStatsMap.put(uri, count);
        } catch (Exception e) {
            LOGGER.error("error in request stats", e);
        }
    }

    @ModelAttribute
    public JSONObject populateJsonObject (HttpServletRequest request, HttpServletResponse response, String sessionId) {
        requestStats(request);
        if (sessionsMap.containsKey(sessionId) && Utils.stringToIntegerList(ConfigUtils.getConfig(ConfigEnum.oids_to_debug, EMPTY)).contains(sessionsMap.get(sessionId).getOid())) {
            String oidToDebug = ConfigUtils.getConfig(ConfigEnum.oids_to_debug, EMPTY);
            if (Utils.hasText(oidToDebug)) {
                List<Integer> oids = Utils.stringToIntegerList(oidToDebug);
                if (oids.contains(sessionsMap.get(sessionId).getOid())) {
                    Utils.printRequestData(request);
                }
            }
        }
        BaseUser user = null;
        JSONObject results = new JSONObject();
        boolean invalidSession = false;
        if (sessionId != null) {
            printUndefinedParams(request, sessionId);
            if (!sessionsMap.containsKey(sessionId)) {
                String decodedSessionId = Utils.getDecodedString(sessionId);
                BaseUser tempUser = EncryptionUtils.decryptUser(decodedSessionId);
                if (tempUser == null) {
                    tempUser = EncryptionUtils.decryptUser(sessionId);
//                    LOGGER.info(String.format("NOT ENCODED: url - %s, uri - %s, web: %s", request.getRequestURL(), request.getRequestURI(), isWebRequest(request)));
                }
                if (tempUser != null) {
                    user = generalManager.loadObject(tempUser.getClass(), tempUser.getOid());
                    if (user != null) {
                        sessionsMap.put(sessionId, user);
                        invalidSessions.remove(sessionId);
                        if (user.getMD5Password().equals(tempUser.getPassword()) || user.getPassword().equals(tempUser.getPassword()) || tempUser.getPassword().contains("?") || !isWebRequest(request)) {
                        } else {
                            if (ConfigUtils.getConfig(ConfigEnum.log_wrong_password_comparison, false)) {
                                LOGGER.warn(String.format(
                                        "wrong password comparison - " +
                                                "uri: %s, " +
                                                "params: %s, user type: %s, " +
                                                "user from sessionId: oid - %s, email - %s, password - %s, md5: %s" +
                                                "user from DB: oid - %s, email - %s, password - %s, md5: %s",
                                        request.getRequestURI(), request.getQueryString(), Utils.getPrintableContactType(user.getClass(), false),
                                        tempUser.getOid(), tempUser.getEmail(), tempUser.getPassword(), tempUser.getMD5Password(),
                                        user.getOid(), user.getEmail(), user.getPassword(), user.getMD5Password()));
                            }
//                            results.put(PARAM_SEND_LOGOUT, true);
//                            invalidSessions.add(sessionId);
//                            invalidSession = true;
                        }
                    }
                }
            } else if (invalidSessions.contains(sessionId)) {
//                LOGGER.warn(String.format("uri: %s, params: %s ", request.getRequestURI(), request.getQueryString()));
//                results.put(PARAM_SEND_LOGOUT, true);
            }
        }
        int userType = 0;
        int userOid = 0;
        if (sessionId != null && !invalidSession) {
            results.put(PARAM_SESSION_ID, sessionId);
            user = getUserObject(BaseUser.class, results);
            if (user != null) {
                userType = user.getUserType();
                userOid = user.getOid();
            }
        }
        utils.setDefaultParameters(request, response, results);
        if (ConfigUtils.getConfig(ConfigEnum.save_requests_to_db, true)) {
            final int finalUserType = userType;
            final int finalUserOid = userOid;
            new Thread(() -> {
                try {
                    if (request.getRequestURI() != null) {
                        requestsData.add(String.format("%s%s%s%s%s%s%s%s%s",
                                request.getRequestURI(), REQUESTS_TOKENS_SEPARATOR,
                                request.getQueryString(),REQUESTS_TOKENS_SEPARATOR,
                                Utils.getIpFromRequest(request), REQUESTS_TOKENS_SEPARATOR,
                                finalUserType, REQUESTS_TOKENS_SEPARATOR, finalUserOid));
                    }
                } catch (Exception e) {
                    LOGGER.error("error extracting request data", e);
                }
            }).start();
        }
        return results;
    }

    private List<String> getRequestUrls () {
        List<String> urls = new ArrayList<>();
        Class cls = null;
        int i = 0;
        try {
            do {
                String className = Thread.currentThread().getStackTrace()[i].getClassName();
                cls = Class.forName(className);
                i++;
            }while (!cls.isAnnotationPresent(RestController.class));
            String methodName = Thread.currentThread().getStackTrace()[(i - 1)].getMethodName();
            Method[] methods = cls.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Annotation annotation = method.getAnnotation(RequestMapping.class);
                    RequestMapping urlMapping = (RequestMapping) annotation;
                    if (urlMapping != null) {
                        Collections.addAll(urls, urlMapping.value());
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("getRequestUrls", e);
        }
        return urls;
    }

    private void printUndefinedParams (HttpServletRequest request, String sessionId) {
        Enumeration<String> params = request.getParameterNames();
        boolean undefined = false;
        while (params.hasMoreElements()) {
            if (request.getParameter(params.nextElement()).trim().toLowerCase().equals("undefined")) {
                undefined = true;
                break;
            }
        }
        if (undefined) {
            LOGGER.warn(String.format("undefined parameter in url: %s, query string: %s, session id: %s", request.getRequestURL(), request.getQueryString(), sessionId));
        }
    }

    protected boolean isWebRequest (HttpServletRequest request) {
        return request.getParameter("w") != null && request.getParameter("w").equals("true");
    }


    boolean updateBaseUser (BaseUser newData, JSONObject results) {
        boolean error = false;
        BaseUser oldUserData = generalManager.loadObject(newData.getClass(), newData.getOid());
        String oldPhone = oldUserData.getPhone();
        if (oldPhone.equals(newData.getPhone()) || !generalManager.isPhoneNumberExist(newData.getPhone())) {
            oldUserData.setObject(newData);
            generalManager.save(oldUserData);
        } else {
            error = true;
            results.put(PARAM_CODE, PARAM_ERROR_PHONE_NUMBER_EXISTS);
        }
        return !error;
    }

    protected void updateAdminUserInSessionsMap (GroupManagerObject groupManagerObject) {
        Map<String, BaseUser> tempSessionsMap = new HashMap<>();
        for (String sessionId : sessionsMap.keySet()) {
            BaseUser user = sessionsMap.get(sessionId);

            if ((user instanceof GroupManagerObject)) {
                if (((user.getOid() == groupManagerObject.getOid()))) {
                    tempSessionsMap.put(sessionId, groupManagerObject);
                }
            } else if (user.getGroupManagerObject().getOid() == groupManagerObject.getOid()){
                user.setGroupManagerObject(groupManagerObject);
            } else {
                tempSessionsMap.put(sessionId, user);
            }
        }
        sessionsMap = tempSessionsMap;
    }

    protected void updateSessionMap(JSONObject results, BaseUser user) {
        String invalidSession = results.getString(PARAM_SESSION_ID);
        removeSessionIdFromMap(invalidSession);
        invalidSessions.add(invalidSession);
        results.put(PARAM_SESSION_ID, Utils.getEncodedString(EncryptionUtils.encryptUser(user)));
    }

    void removeSessionIdFromMap (String sessionId) {
        sessionsMap.remove(sessionId);
    }






}
