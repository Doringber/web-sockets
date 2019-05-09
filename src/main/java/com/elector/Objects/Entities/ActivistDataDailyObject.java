package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

/**
 * Created by Shai on 8/15/2018.
 */

public class ActivistDataDailyObject extends BaseEntity {
    private ActivistObject activistObject;
    private Date date;
    private int supportersCount;
    private Integer rank;
    private Integer dailyRank;

    public ActivistDataDailyObject() {
    }

    public ActivistDataDailyObject(ActivistObject activistObject, Date date, int supportersCount, Integer rank, Integer dailyRank) {
        this.activistObject = activistObject;
        this.date = date;
        this.supportersCount = supportersCount;
        this.rank = rank;
        this.dailyRank = dailyRank;
    }

    public ActivistObject getActivistObject() {
        return activistObject;
    }

    public void setActivistObject(ActivistObject activistObject) {
        this.activistObject = activistObject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSupportersCount() {
        return supportersCount;
    }

    public void setSupportersCount(int supportersCount) {
        this.supportersCount = supportersCount;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getDailyRank() {
        return dailyRank;
    }

    public void setDailyRank(Integer dailyRank) {
        this.dailyRank = dailyRank;
    }
}
