package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class DynamicGroupObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private String title;
    private Date creationDate;
    private Date statsTime;
    private int totalVoters;
    private int totalSupporters;
    private int totalUnverifiedSupporters;
    private int totalUnknownSupportStatus;
    private int totalNotSupporting;
    private String firstName;
    private String lastName;
    private String voterId;
    private String address;
    private Integer supportStatus;
    private String phone;
    private String homePhone;
    private String extraPhone;
    private String email;
    private Integer gender;
    private String comment;
    private Integer language;
    private Integer supportSign;
    private Integer allowRecall;
    private Integer ageType;
    private Integer age;
    private Integer handlingActivistsType;
    private Integer handlingActivists;
    private String customPropertiesValues;
    private Integer needsRide;
    private Integer voteStatus;
    private Integer voteStatusRound1;
    private Integer seniorityMonths;
    private Integer seniorityMonthsType;
    private Integer collectionCode;
    private Integer canVote;
    private Integer groupOid;
    private Integer voteIntention;
    private Integer ageRange;
    private CityObject cityObject;

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStatsTime() {
        return statsTime;
    }

    public void setStatsTime(Date statsTime) {
        this.statsTime = statsTime;
    }

    public int getTotalVoters() {
        return totalVoters;
    }

    public void setTotalVoters(int totalVoters) {
        this.totalVoters = totalVoters;
    }

    public int getTotalSupporters() {
        return totalSupporters;
    }

    public void setTotalSupporters(int totalSupporters) {
        this.totalSupporters = totalSupporters;
    }

    public int getTotalUnverifiedSupporters() {
        return totalUnverifiedSupporters;
    }

    public void setTotalUnverifiedSupporters(int totalUnverifiedSupporters) {
        this.totalUnverifiedSupporters = totalUnverifiedSupporters;
    }

    public int getTotalUnknownSupportStatus() {
        return totalUnknownSupportStatus;
    }

    public void setTotalUnknownSupportStatus(int totalUnknownSupportStatus) {
        this.totalUnknownSupportStatus = totalUnknownSupportStatus;
    }

    public int getTotalNotSupporting() {
        return totalNotSupporting;
    }

    public void setTotalNotSupporting(int totalNotSupporting) {
        this.totalNotSupporting = totalNotSupporting;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(Integer supportStatus) {
        this.supportStatus = supportStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getExtraPhone() {
        return extraPhone;
    }

    public void setExtraPhone(String extraPhone) {
        this.extraPhone = extraPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getSupportSign() {
        return supportSign;
    }

    public void setSupportSign(Integer supportSign) {
        this.supportSign = supportSign;
    }

    public Integer getAllowRecall() {
        return allowRecall;
    }

    public void setAllowRecall(Integer allowRecall) {
        this.allowRecall = allowRecall;
    }

    public Integer getAgeType() {
        return ageType;
    }

    public void setAgeType(Integer ageType) {
        this.ageType = ageType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHandlingActivistsType() {
        return handlingActivistsType;
    }

    public void setHandlingActivistsType(Integer handlingActivistsType) {
        this.handlingActivistsType = handlingActivistsType;
    }

    public Integer getHandlingActivists() {
        return handlingActivists;
    }

    public void setHandlingActivists(Integer handlingActivists) {
        this.handlingActivists = handlingActivists;
    }

    public String getCustomPropertiesValues() {
        return customPropertiesValues;
    }

    public void setCustomPropertiesValues(String customPropertiesValues) {
        this.customPropertiesValues = customPropertiesValues;
    }

    public Integer getNeedsRide() {
        return needsRide;
    }

    public void setNeedsRide(Integer needsRide) {
        this.needsRide = needsRide;
    }

    public Integer getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(Integer voteStatus) {
        this.voteStatus = voteStatus;
    }

    public Integer getVoteStatusRound1() {
        return voteStatusRound1;
    }

    public void setVoteStatusRound1(Integer voteStatusRound1) {
        this.voteStatusRound1 = voteStatusRound1;
    }

    public Integer getSeniorityMonths() {
        return seniorityMonths;
    }

    public void setSeniorityMonths(Integer seniorityMonths) {
        this.seniorityMonths = seniorityMonths;
    }

    public Integer getSeniorityMonthsType() {
        return seniorityMonthsType;
    }

    public void setSeniorityMonthsType(Integer seniorityMonthsType) {
        this.seniorityMonthsType = seniorityMonthsType;
    }

    public Integer getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(Integer collectionCode) {
        this.collectionCode = collectionCode;
    }

    public Integer getCanVote() {
        return canVote;
    }

    public void setCanVote(Integer canVote) {
        this.canVote = canVote;
    }

    public Integer getGroupOid() {
        return groupOid;
    }

    public void setGroupOid(Integer groupOid) {
        this.groupOid = groupOid;
    }

    public Integer getVoteIntention() {
        return voteIntention;
    }

    public void setVoteIntention(Integer voteIntention) {
        this.voteIntention = voteIntention;
    }

    public Integer getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(Integer ageRange) {
        this.ageRange = ageRange;
    }

    public CityObject getCityObject() {
        return cityObject;
    }

    public void setCityObject(CityObject cityObject) {
        this.cityObject = cityObject;
    }
}
