package com.elector.Objects.General;

import com.elector.Objects.Entities.*;
import com.elector.Utils.Utils;

import java.util.Date;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Definitions.PARAM_ADMIN_USER_TYPE_GROUP_MANAGER;

public class BaseUser extends BaseContact {
    protected int type;
    protected String email;
    protected String password;
    protected Date lastLoginDate;
    protected String firstName;
    protected String lastName;
    protected String mobileRegistrationToken;
    protected boolean workOnElectionDay;
    private Integer language;
    private Integer defaultTableSize;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName () {
        return String.format("%s %s", this.firstName == null ? EMPTY : this.firstName, this.lastName == null ? EMPTY : this.lastName);
    }

    public String getMobileRegistrationToken() {
        return mobileRegistrationToken;
    }

    public void setMobileRegistrationToken(String mobileRegistrationToken) {
        this.mobileRegistrationToken = mobileRegistrationToken;
    }

    public boolean isLoggedIn () {
        return this.oid > 0;
    }

    public int getUserTypeByClass () {
        int type = 0;
        if (this.getClass().equals(AdminUserObject.class)) {
            type = this.oid == SUPER_ADMIN_OID ? PARAM_ADMIN_USER_TYPE_SUPER : PARAM_ADMIN_USER_TYPE_CANDIDATE;
        } else if (this.getClass().equals(CallerObject.class)) {
            type = PARAM_ADMIN_USER_TYPE_CALLER;
        } else if (this.getClass().equals(ObserverObject.class)) {
            type = PARAM_ADMIN_USER_TYPE_OBSERVER;
        } else if (this.getClass().equals(ActivistObject.class)) {
            type = PARAM_ADMIN_USER_TYPE_ACTIVIST;
        } else if (this.getClass().equals(DriverObject.class)) {
            type = PARAM_ADMIN_USER_TYPE_DRIVER;
        } else if (this.getClass().equals(GroupManagerObject.class)) {
            type = PARAM_ADMIN_USER_TYPE_GROUP_MANAGER;
        }
        return type;
    }

    @Override
    public boolean equals(Object other) {
        return this.getClass() == other.getClass() && this.oid == ((BaseEntity)other).getOid();
    }

    public String getSmsSenderNameToShow () {
        String sender = SERVER_SMS_SENDER_NUMBER;
        if (this instanceof AdminUserObject) {
            if (((AdminUserObject) this).getSmsSenderName() != null) {
                sender = ((AdminUserObject) this).getSmsSenderName();
            }
        } else {
            AdminUserObject adminUserObject = this.getAdminUserObject();
            if (adminUserObject != null && adminUserObject.getSmsSenderName() != null) {
                sender = adminUserObject.getSmsSenderName();
            }
        }
        return sender;
    }

    public String getMD5Password () {
        return Utils.getMD5String(this.password);
    }

    public boolean isWorkOnElectionDay() {
        return workOnElectionDay;
    }

    public void setWorkOnElectionDay(boolean workOnElectionDay) {
        this.workOnElectionDay = workOnElectionDay;
    }

    public AdminUserObject getCampaignManager () {
        return this instanceof AdminUserObject ? (AdminUserObject) this : this.adminUserObject;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getDefaultTableSize() {
        return defaultTableSize;
    }

    public void setDefaultTableSize(Integer defaultTableSize) {
        this.defaultTableSize = defaultTableSize;
    }
}
