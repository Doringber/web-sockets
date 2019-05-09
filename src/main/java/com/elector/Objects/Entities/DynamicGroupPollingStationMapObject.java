package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class DynamicGroupPollingStationMapObject extends BaseEntity {
    private DynamicGroupObject dynamicGroupObject;
    private BallotBoxObject ballotBoxObject;

    public DynamicGroupPollingStationMapObject() {
    }

    public DynamicGroupPollingStationMapObject(DynamicGroupObject dynamicGroupObject, BallotBoxObject ballotBoxObject) {
        this.dynamicGroupObject = dynamicGroupObject;
        this.ballotBoxObject = ballotBoxObject;
    }

    public DynamicGroupObject getDynamicGroupObject() {
        return dynamicGroupObject;
    }

    public void setDynamicGroupObject(DynamicGroupObject dynamicGroupObject) {
        this.dynamicGroupObject = dynamicGroupObject;
    }


    public BallotBoxObject getBallotBoxObject() {
        return ballotBoxObject;
    }

    public void setBallotBoxObject(BallotBoxObject ballotBoxObject) {
        this.ballotBoxObject = ballotBoxObject;
    }
}
