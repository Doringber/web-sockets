package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class ObserverCheckInObject extends BaseEntity {
    private ObserverObject observerObject;
    private Date date;
    private boolean checkIn;

    public ObserverCheckInObject () {
    }

    public ObserverCheckInObject(ObserverObject observerObject, Date date, boolean checkIn) {
        this.observerObject = observerObject;
        this.date = date;
        this.checkIn = checkIn;
    }

    public ObserverObject getObserverObject() {
        return observerObject;
    }

    public void setObserverObject(ObserverObject observerObject) {
        this.observerObject = observerObject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }
}
