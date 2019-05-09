package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class RivalObject  extends BaseEntity {
    private AdminUserObject adminUserObject;
    private String name;

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
