package com.elector.Objects.Entities;

import java.util.Date;

public class SupporterSupportersListDelivery extends SupporterActionObject {
    private boolean firstList;

    public SupporterSupportersListDelivery() {
    }

    public SupporterSupportersListDelivery(VoterObject supporter, int initiatorOid, int initiatorType, Date creationDate, int type, boolean firstList) {
        super(supporter, initiatorOid, initiatorType, creationDate, type);
        this.firstList = firstList;
    }

    public boolean isFirstList() {
        return firstList;
    }

    public void setFirstList(boolean firstList) {
        this.firstList = firstList;
    }
}
