package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class BallotBoxBlockMapObject extends BaseEntity{
    private BallotBoxObject ballotBoxObject;
    private BallotBoxBlockObject ballotBoxBlockObject;
    private AdminUserObject adminUserObject;

    public BallotBoxBlockMapObject() {
    }


    public BallotBoxBlockMapObject(BallotBoxObject ballotBoxObject, BallotBoxBlockObject ballotBoxBlockObject, AdminUserObject adminUserObject) {
        this.ballotBoxObject = ballotBoxObject;
        this.ballotBoxBlockObject = ballotBoxBlockObject;
        this.adminUserObject = adminUserObject;
    }

    public BallotBoxObject getBallotBoxObject() {
        return ballotBoxObject;
    }

    public void setBallotBoxObject(BallotBoxObject ballotBoxObject) {
        this.ballotBoxObject = ballotBoxObject;
    }

    public BallotBoxBlockObject getBallotBoxBlockObject() {
        return ballotBoxBlockObject;
    }

    public void setBallotBoxBlockObject(BallotBoxBlockObject ballotBoxBlockObject) {
        this.ballotBoxBlockObject = ballotBoxBlockObject;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }
}
