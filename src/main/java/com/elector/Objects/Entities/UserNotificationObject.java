package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class UserNotificationObject extends BaseEntity{
    private ActivistObject activistObject;
    private NotificationObject notificationObject;
    private boolean success;

    public UserNotificationObject(){
    }

    public UserNotificationObject(ActivistObject activistObject, NotificationObject notificationObject, boolean success) {
        this.activistObject = activistObject;
        this.notificationObject = notificationObject;
        this.success = success;
    }

    public ActivistObject getActivistObject() {
        return activistObject;
    }

    public void setActivistObject(ActivistObject activistObject) {
        this.activistObject = activistObject;
    }

    public NotificationObject getNotificationObject() {
        return notificationObject;
    }

    public void setNotificationObject(NotificationObject notificationObject) {
        this.notificationObject = notificationObject;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
