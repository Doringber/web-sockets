package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class ObserverBallotBoxMapObject extends BaseEntity{
    private ObserverObject observerObject;
    private BallotBoxObject ballotBoxObject;
    private AdminUserObject adminUserObject;
    private boolean checkedIn;

    public ObserverBallotBoxMapObject() {
    }


    public ObserverBallotBoxMapObject(ObserverObject observerObject, BallotBoxObject ballotBoxObject, AdminUserObject adminUserObject) {
        this.observerObject = observerObject;
        this.ballotBoxObject = ballotBoxObject;
        this.adminUserObject = adminUserObject;
    }

    public ObserverObject getObserverObject() {
        return observerObject;
    }

    public void setObserverObject(ObserverObject observerObject) {
        this.observerObject = observerObject;
    }

    public BallotBoxObject getBallotBoxObject() {
        return ballotBoxObject;
    }

    public void setBallotBoxObject(BallotBoxObject ballotBoxObject) {
        this.ballotBoxObject = ballotBoxObject;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
