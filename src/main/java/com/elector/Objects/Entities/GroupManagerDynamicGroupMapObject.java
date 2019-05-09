package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class GroupManagerDynamicGroupMapObject extends BaseEntity {
    private Date creationDate;
    private GroupManagerObject groupManagerObject;
    private DynamicGroupObject dynamicGroupObject;
    private AdminUserObject adminUserObject;

    public GroupManagerDynamicGroupMapObject() {
    }

    public GroupManagerDynamicGroupMapObject(GroupManagerObject groupManagerObject, DynamicGroupObject dynamicGroupObject, AdminUserObject adminUserObject) {
        this.creationDate = new Date();
        this.groupManagerObject = groupManagerObject;
        this.dynamicGroupObject = dynamicGroupObject;
        this.adminUserObject = adminUserObject;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public GroupManagerObject getGroupManagerObject() {
        return groupManagerObject;
    }

    public void setGroupManagerObject(GroupManagerObject groupManagerObject) {
        this.groupManagerObject = groupManagerObject;
    }

    public DynamicGroupObject getDynamicGroupObject() {
        return dynamicGroupObject;
    }

    public void setDynamicGroupObject(DynamicGroupObject dynamicGroupObject) {
        this.dynamicGroupObject = dynamicGroupObject;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }
}
