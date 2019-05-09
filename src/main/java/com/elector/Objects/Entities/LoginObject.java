package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

/**
 * Created by Shai on 8/8/2018.
 */

public class LoginObject extends BaseEntity{
    private Date date;
    private int userType;
    private int userOid;
    private boolean web;
    private String ip;

    public LoginObject() {
    }

    public LoginObject(Date date, int userType, int userOid, boolean web, String ip) {
        this.date = date;
        this.userType = userType;
        this.userOid = userOid;
        this.web = web;
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserOid() {
        return userOid;
    }

    public void setUserOid(int userOid) {
        this.userOid = userOid;
    }

    public boolean isWeb() {
        return web;
    }

    public void setWeb(boolean web) {
        this.web = web;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
