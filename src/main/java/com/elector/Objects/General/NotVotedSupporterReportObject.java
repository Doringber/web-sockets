package com.elector.Objects.General;

import com.elector.Objects.Entities.VoterObject;

/**
 * Created by Shai on 2/19/2018.
 */

public class NotVotedSupporterReportObject extends ReportObject{
    private String fullName;
    private String phone;
    private String address;
    private String supporterId;

    public NotVotedSupporterReportObject(VoterObject supporter) {
        this.fullName = supporter.getFullName();
        this.phone = supporter.getPhone();
        this.address = supporter.getAddress();
        this.supporterId = supporter.getVoterId();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSupporterId() {
        return supporterId;
    }

    public void setSupporterId(String supporterId) {
        this.supporterId = supporterId;
    }
}
