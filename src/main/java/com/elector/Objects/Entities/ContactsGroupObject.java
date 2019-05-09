package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseContactGroupObject;

import java.util.Date;

public class ContactsGroupObject extends BaseContactGroupObject {
    private int groupId;
    private Date creationDate;
    private String name;
    private String nameInService;

    public ContactsGroupObject() {
    }

    public ContactsGroupObject(AdminUserObject adminUserObject, int groupId, String name, String description, String nameInService) {
        super(adminUserObject, description);
        this.groupId = groupId;
        this.name = name;
        this.nameInService = nameInService;
        this.creationDate = new Date();
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameInService() {
        return nameInService;
    }

    public void setNameInService(String nameInService) {
        this.nameInService = nameInService;
    }
}
