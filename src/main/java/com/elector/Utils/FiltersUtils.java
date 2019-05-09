package com.elector.Utils;

import com.elector.Annotations.*;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static com.elector.Utils.Definitions.*;

@Component
public class FiltersUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FiltersUtils.class);

    private static Map<String, List<Integer>> permissionsMap;

    @PostConstruct
    private void init() {
        buildPermissionsMap();
    }

    public static Set<Class<? extends Object>> getControllers() throws ClassNotFoundException {
        Reflections reflections = new Reflections("com.elector.Controllers", new SubTypesScanner(false));
        Set<Class<? extends Object>> controllers =
                reflections.getSubTypesOf(Object.class);
        LOGGER.info("Found {} controllers classes", controllers.size());
        return controllers;
    }

    private static void addIfAllowed(Class cls, Method method, Class<? extends Annotation> annotation, int type, List<Integer> list) {
        if (cls.isAnnotationPresent(annotation) || method.isAnnotationPresent(annotation)) {
            list.add(type);
        }
    }

    public static void buildPermissionsMap() {
        permissionsMap = new HashMap<>();
        try {
            Set<Class<? extends Object>> classes = getControllers();
            for (Class cls : classes) {
                if (cls.isAnnotationPresent(RestController.class)) {
                    Method[] methods = cls.getMethods();
                    for (Method method : methods) {
                        Annotation annotation = method.getAnnotation(RequestMapping.class);
                        RequestMapping urlMapping = (RequestMapping) annotation;
                        if (urlMapping != null) {
                            for (String url : urlMapping.value()) {
                                List<Integer> allowedUserTypes = new ArrayList<>();
                                addIfAllowed(cls, method, CallerRestricted.class, PARAM_ADMIN_USER_TYPE_CALLER, allowedUserTypes);
                                addIfAllowed(cls, method, ActivistRestricted.class, PARAM_ADMIN_USER_TYPE_ACTIVIST, allowedUserTypes);
                                addIfAllowed(cls, method, AdminRestricted.class, PARAM_ADMIN_USER_TYPE_CANDIDATE, allowedUserTypes);
                                addIfAllowed(cls, method, SuperAdminRestricted.class, PARAM_ADMIN_USER_TYPE_SUPER, allowedUserTypes);
                                addIfAllowed(cls, method, ObserverRestricted.class, PARAM_ADMIN_USER_TYPE_OBSERVER, allowedUserTypes);
                                addIfAllowed(cls, method, GroupManagerRestricted.class, PARAM_ADMIN_USER_TYPE_GROUP_MANAGER, allowedUserTypes);
                                permissionsMap.put(url, allowedUserTypes);
//                                LOGGER.info("Put permissions for url={}, allowedUserTypes={}", url, allowedUserTypes);
                            }
                        }
                    }
                }
            }
            LOGGER.info("Create permission map size = {}", permissionsMap.size());
        } catch (Exception e) {
            LOGGER.error("buildPermissionsMap", e);
        }
    }

    public static boolean isUserTypeAllowed(int type, List<String> urls) {
        boolean allow = false;
        for (String url : urls) {
            if (permissionsMap.get(url) == null
                    || permissionsMap.get(url).isEmpty()
                    || permissionsMap.get(url).contains(type)) {
                allow = true;
            }
        }
        return allow;
    }

}
