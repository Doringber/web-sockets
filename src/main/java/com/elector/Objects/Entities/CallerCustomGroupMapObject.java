package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class CallerCustomGroupMapObject extends BaseEntity {
    private CallerObject callerObject;
    private CustomGroupObject customGroupObject;
    private Date creationDate;

    public CallerCustomGroupMapObject() {
    }

    public CallerCustomGroupMapObject(CallerObject callerObject, CustomGroupObject customGroupObject) {
        this.callerObject = callerObject;
        this.customGroupObject = customGroupObject;
        this.creationDate = new Date();
    }

    public CallerObject getCallerObject() {
        return callerObject;
    }

    public void setCallerObject(CallerObject callerObject) {
        this.callerObject = callerObject;
    }

    public CustomGroupObject getCustomGroupObject() {
        return customGroupObject;
    }

    public void setCustomGroupObject(CustomGroupObject customGroupObject) {
        this.customGroupObject = customGroupObject;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
