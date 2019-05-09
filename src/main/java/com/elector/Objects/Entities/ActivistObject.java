package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseUser;
import com.elector.Utils.TemplateUtils;

import java.util.Date;

public class ActivistObject extends BaseUser {
    private Date birthDate;
    private int totalVoted;
    private int totalNotVoted;
    private String lastElectionDayCallTime;
    private Boolean allowExcelUpload;
    private Long supportersCount;
    private boolean autoVerifySupporters;
    private Integer votedSupportersCount;
    private boolean allowAccessToSupportStatus;
    private boolean allowEditVoteStatus;

    public ActivistObject () {

    }


    public ActivistObject (int oid) {
        this.oid = oid;
    }

    public ActivistObject (int oid, String firstName, String lastName, Long supportersCount) {
        this.oid = oid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.supportersCount = supportersCount;
    }

    public ActivistObject (VoterObject voterObject) {
        this.phone = voterObject.getPhone();
        this.firstName = voterObject.getFirstName();
        this.lastName = voterObject.getLastName();
        this.adminUserObject = voterObject.getAdminUserObject();
        this.birthDate = voterObject.getBirthDate();
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getFormattedBirthDate() {
        return birthDate != null ? TemplateUtils.formatDateExcludeTime(this.birthDate) : null;
    }

    public int getTotalVoted() {
        return totalVoted;
    }

    public void setTotalVoted(int totalVoted) {
        this.totalVoted = totalVoted;
    }

    public int getTotalNotVoted() {
        return totalNotVoted;
    }

    public void setTotalNotVoted(int totalNotVoted) {
        this.totalNotVoted = totalNotVoted;
    }

    public String getLastElectionDayCallTime() {
        return lastElectionDayCallTime;
    }

    public void setLastElectionDayCallTime(String lastElectionDayCallTime) {
        this.lastElectionDayCallTime = lastElectionDayCallTime;
    }

    public Boolean getAllowExcelUpload() {
        return allowExcelUpload;
    }

    public void setAllowExcelUpload(Boolean allowExcelUpload) {
        this.allowExcelUpload = allowExcelUpload;
    }

    public Long getSupportersCount() {
        return supportersCount;
    }

    public void setSupportersCount(Long supportersCount) {
        this.supportersCount = supportersCount;
    }

    public boolean isAutoVerifySupporters() {
        return autoVerifySupporters;
    }

    public void setAutoVerifySupporters(boolean autoVerifySupporters) {
        this.autoVerifySupporters = autoVerifySupporters;
    }

    public Integer getVotedSupportersCount() {
        return votedSupportersCount;
    }

    public void setVotedSupportersCount(Integer votedSupportersCount) {
        this.votedSupportersCount = votedSupportersCount;
    }

    public boolean isAllowAccessToSupportStatus() {
        return allowAccessToSupportStatus;
    }

    public void setAllowAccessToSupportStatus(boolean allowAccessToSupportStatus) {
        this.allowAccessToSupportStatus = allowAccessToSupportStatus;
    }

    public boolean isAllowEditVoteStatus() {
        return allowEditVoteStatus;
    }

    public void setAllowEditVoteStatus(boolean allowEditVoteStatus) {
        this.allowEditVoteStatus = allowEditVoteStatus;
    }
}
