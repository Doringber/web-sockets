package com.elector.Objects.General;

import com.elector.Objects.Entities.AdminUserObject;
import com.elector.Objects.Entities.GroupManagerObject;

public class BaseContactGroupObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private String description;
    private GroupManagerObject groupManagerObject;

    public BaseContactGroupObject() {
    }

    public BaseContactGroupObject(AdminUserObject adminUserObject, String description) {
        this.adminUserObject = adminUserObject;
        this.description = description;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupManagerObject getGroupManagerObject() {
        return groupManagerObject;
    }

    public void setGroupManagerObject(GroupManagerObject groupManagerObject) {
        this.groupManagerObject = groupManagerObject;
    }
}
