package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class CallerVoterMapObject extends BaseEntity {
    private CallerObject callerObject;
    private VoterObject voterObject;
    private AdminUserObject adminUserObject;

    public CallerVoterMapObject() {
    }

    public CallerVoterMapObject(CallerObject callerObject, VoterObject voterObject, AdminUserObject adminUserObject) {
        this.callerObject = callerObject;
        this.voterObject = voterObject;
        this.adminUserObject = adminUserObject;
    }

    public CallerObject getCallerObject() {
        return callerObject;
    }

    public void setCallerObject(CallerObject callerObject) {
        this.callerObject = callerObject;
    }

    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

}
