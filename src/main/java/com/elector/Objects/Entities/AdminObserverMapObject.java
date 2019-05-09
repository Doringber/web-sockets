package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class AdminObserverMapObject extends BaseEntity{
    private AdminUserObject adminUserObject;
    private ObserverObject observerObject;
    private Date creationDate;

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public ObserverObject getObserverObject() {
        return observerObject;
    }

    public void setObserverObject(ObserverObject observerObject) {
        this.observerObject = observerObject;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
