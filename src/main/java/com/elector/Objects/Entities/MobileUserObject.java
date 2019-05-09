package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class MobileUserObject extends BaseEntity {
    private boolean android;
    private Integer userOid;
    private Integer userType;
    private String uuid;
    private String device;
    private String phone;
    private String version;

    public MobileUserObject () {
    }

    public MobileUserObject(boolean android, Integer userOid, Integer userType, String uuid, String device, String phone, String version) {
        this.android = android;
        this.userOid = userOid;
        this.userType = userType;
        this.uuid = uuid;
        this.device = device;
        this.phone = phone;
        this.version = version;
    }

    public boolean isAndroid() {
        return android;
    }

    public void setAndroid(boolean android) {
        this.android = android;
    }

    public Integer getUserOid() {
        return userOid;
    }

    public void setUserOid(Integer userOid) {
        this.userOid = userOid;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
