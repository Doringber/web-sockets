package com.elector.Models;

import com.elector.Objects.General.BaseUser;

public class BaseUserModel {
    private int oid;
    private String name;
    private int type;

    public BaseUserModel () {
    }

    public BaseUserModel (BaseUser baseUser) {
        this.oid = baseUser.getOid();
        this.name = baseUser.getFullName();
        this.type = baseUser.getUserType();
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
