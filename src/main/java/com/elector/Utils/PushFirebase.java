package com.elector.Utils;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.Entities.NotificationObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.raudi.pushraven.FcmResponse;
import us.raudi.pushraven.Notification;
import us.raudi.pushraven.Pushraven;

import java.util.Collections;
import java.util.List;

public class PushFirebase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushFirebase.class);
    private static final String KEY = "AAAALF_G0uk:APA91bGKX5A4kvkloWLerGZfoFHxokuoB_N5wwHwO96-vJHXXOb65GaPEQqnlnrK2P84wuGCU5wVJK7P29liSo7Q2uBNLT7kN79vKh3ESDHF2AxS1hwnXROKiPlFlvLUApdg8acD6vys";


    public static boolean push (String registrationId, NotificationObject notificationObject) {
        return push (Collections.singletonList(registrationId), notificationObject);
    }


    public static boolean push(List<String> registrationIds, NotificationObject notificationObject) {
        boolean success = false;
        try {
            if (ConfigUtils.getConfig(ConfigEnum.prod, false)) {
                Pushraven.setKey(KEY);
                if (registrationIds != null) {
                    Notification raven = new Notification();
                    raven.collapse_key("deposited")
                            .registration_ids(registrationIds)
//                        .sound("default")
//                        .addRequestAttribute("vibrate", true)
//                        .addNotificationAttribute("vibrate", true)
//                        .icon("notification_icon")
//                        .restricted_package_name("com.eback.cashback")
                            .title(notificationObject.getTitle())
                            .body(notificationObject.getBody());
                    FcmResponse response = Pushraven.push(raven);
                    LOGGER.info(String.format("notification oid %s, response: %s, error: %s", notificationObject.getOid(), response.toString(), response.getErrorMessage()));
                    success = true;
                }
            } else {
                success = true;
            }
        } catch (Exception e) {
            LOGGER.error("Error while sending push message", e);
        }
        return success;
    }
}
