package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class BallotBoxBlockObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private String name;

    public BallotBoxBlockObject() {
    }


    public BallotBoxBlockObject(AdminUserObject adminUserObject, String name) {
        this.adminUserObject = adminUserObject;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }
}
