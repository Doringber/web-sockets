package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseUser;
import com.elector.Objects.General.GeneralManagerUserObject;
import com.elector.Utils.Utils;

import java.util.Date;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Definitions.UI_LINE_BREAK_REPLACEMENT;

public class AdminUserObject extends GeneralManagerUserObject {
    private Date creationDate;
    private Date expirationDate;
    private String name;
    private CampaignObject campaignObject;
    private String businessId;
    private String businessName;
    private Integer maxUsers;
    private Integer usersCount;
    private String mainOfficeAddress;
    private String mainOfficePhone;
    private Float cost;
    private Integer totalSmsPurchased;
    private Integer usedSms;
    private String adminId;
    private boolean active;
    private Boolean allowExcelUpload;
    private Boolean votersBookCopied;
    private String messagesPage;
    protected boolean dataEncrypted;

    public AdminUserObject () {
    }

    public AdminUserObject (int oid) {
        this.oid = oid;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CampaignObject getCampaignObject() {

        return campaignObject;
    }

    public void setCampaignObject(CampaignObject campaignObject) {
        this.campaignObject = campaignObject;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public boolean canAddUser() {
        return true;
        //        return this.usersCount < this.maxUsers;
    }

    public void incrementUsersCount() {
//        this.usersCount++;
    }

    public boolean isActiveByExpiration() {
        boolean active = false;
        if (this.expirationDate == null || this.expirationDate.getTime() > System.currentTimeMillis()) {
            active = true;
        }
        return active;
    }

    public String getMainOfficeAddress() {
        return mainOfficeAddress;
    }

    public void setMainOfficeAddress(String mainOfficeAddress) {
        this.mainOfficeAddress = mainOfficeAddress;
    }

    public String getMainOfficePhone() {
        return Utils.formatPhoneNumber(this.mainOfficePhone);
    }

    public void setMainOfficePhone(String mainOfficePhone) {
        this.mainOfficePhone = Utils.formatPhoneNumber(mainOfficePhone);
    }

    public String getFullName() {
        return this.name;
    }

    public int getRemainingSms() {
        return this.totalSmsPurchased - this.usedSms;
    }


    public boolean isActiveUserWithCampaign() {
        return
                this.oid == SUPER_ADMIN_OID || (
                        this.campaignObject != null &&
                                this.campaignObject.getEndDate() != null &&
                                this.campaignObject.getEndDate().after(new Date()) &&
                                this.expirationDate != null &&
                                this.expirationDate.after(new Date())
                );
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Boolean getAllowExcelUpload() {
        return allowExcelUpload;
    }

    public void setAllowExcelUpload(Boolean allowExcelUpload) {
        this.allowExcelUpload = allowExcelUpload;
    }

    public Boolean getVotersBookCopied() {
        return votersBookCopied;
    }

    public void setVotersBookCopied(Boolean votersBookCopied) {
        this.votersBookCopied = votersBookCopied;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Integer getTotalSmsPurchased() {
        return totalSmsPurchased;
    }

    public void setTotalSmsPurchased(Integer totalSmsPurchased) {
        this.totalSmsPurchased = totalSmsPurchased;
    }

    public Integer getUsedSms() {
        return usedSms;
    }

    public void setUsedSms(Integer usedSms) {
        this.usedSms = usedSms;
    }

    public String getMessagesPage() {
        return messagesPage;
    }

    public void setMessagesPage(String messagesPage) {
        this.messagesPage = messagesPage;
    }

    public String getUIElectionDayCallScenario () {
        return Utils.hasText(this.electionDayCallScenario) ? this.electionDayCallScenario.replace(BREAK, UI_LINE_BREAK_REPLACEMENT) : EMPTY;
    }

    public boolean canSendSms (int amount) {
        return this.sendSms && this.getRemainingSms() >= amount;
    }

    public boolean isDataEncrypted() {
        return dataEncrypted;
    }

    public void setDataEncrypted(boolean dataEncrypted) {
        this.dataEncrypted = dataEncrypted;
    }
}
