package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseVoterDataObject;

import java.util.Date;

public class PendingVoterObject extends BaseVoterDataObject {
    private Date duplicateDate;

    public PendingVoterObject () {
    }

    public PendingVoterObject (VoterObject voterObject) {
        this.voterId = voterObject.getVoterId();
        this.firstName = voterObject.getFirstName();
        this.lastName = voterObject.getLastName();
        this.address = voterObject.getAddress();
        this.birthDate = voterObject.getBirthDate();
        this.email = voterObject.getEmail();
        this.adminUserObject = voterObject.getAdminUserObject();
//        this.campaignVoterObject = voterObject.getCampaignVoterObject();
        this.male = voterObject.getMale();
        this.phone = voterObject.getPhone();
        this.supportStatus = voterObject.getSupportStatus();
    }

    public Date getDuplicateDate() {
        return duplicateDate;
    }

    public void setDuplicateDate(Date duplicateDate) {
        this.duplicateDate = duplicateDate;
    }
}
