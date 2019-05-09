package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class NotificationObject extends BaseEntity {
    private String title;
    private String body;
    private Date date;
    private AdminUserObject adminUserObject;

    public NotificationObject() {
    }

    public NotificationObject(String title, String body, AdminUserObject adminUserObject) {
        this.title = title;
        this.body = body;
        this.date = new Date();
        this.adminUserObject = adminUserObject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }
}
