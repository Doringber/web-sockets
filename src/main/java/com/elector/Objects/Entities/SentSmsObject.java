package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseContact;
import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class SentSmsObject extends BaseEntity {
    private String recipientPhone;
    private String senderPhone;
    private String text;
    private AdminUserObject adminUserObject;
    private Date time;
    private Boolean success;
    private String fullName;
    private Integer contactOid;
    private Integer contactType;
    private boolean allowSend;
    private GroupManagerObject groupManagerObject;

    public SentSmsObject() {
    }

    public SentSmsObject(String recipientPhone, String senderPhone, String text, AdminUserObject adminUserObject,
                         Date time, Boolean success, BaseContact contact, GroupManagerObject groupManagerObject) {
        this.recipientPhone = recipientPhone;
        this.senderPhone = senderPhone;
        this.text = text;
        this.adminUserObject = adminUserObject;
        this.time = time;
        this.success = success;
        if (contact != null) {
            this.fullName = contact.getFullName();
            this.contactOid = contact.getOid();
            this.contactType = contact.getUserType();
        }
        this.groupManagerObject = groupManagerObject;
    }


    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone.trim();
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getContactOid() {
        return contactOid;
    }

    public void setContactOid(Integer contactOid) {
        this.contactOid = contactOid;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public boolean isAllowSend() {
        return allowSend;
    }

    public void setAllowSend(boolean allowSend) {
        this.allowSend = allowSend;
    }

    public GroupManagerObject getGroupManagerObject() {
        return groupManagerObject;
    }

    public void setGroupManagerObject(GroupManagerObject groupManagerObject) {
        this.groupManagerObject = groupManagerObject;
    }
}
