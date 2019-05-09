package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class RequestStatsObject extends BaseEntity {
    private String uri;
    private Integer count;
    private Date lastRequestDate;
    private Date firstRequestDate;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastRequestDate() {
        return lastRequestDate;
    }

    public void setLastRequestDate(Date lastRequestDate) {
        this.lastRequestDate = lastRequestDate;
    }

    public void incrementCount (int count) {
        if (this.count == null) {
            this.count = 0;
        }
        this.count += count;
    }

    public Date getFirstRequestDate() {
        return firstRequestDate;
    }

    public void setFirstRequestDate(Date firstRequestDate) {
        this.firstRequestDate = firstRequestDate;
    }


}
