package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import static com.elector.Utils.Definitions.PARAM_CAMPAIGN_TYPE_MUNICIPAL;

public class BaseCampaignVoterObject extends BaseEntity {
    protected String firstName;
    protected String lastName;
    protected String voterId;
    protected BallotBoxObject ballotBoxObject;
    protected CampaignObject campaignObject;


    public BaseCampaignVoterObject () {
    }

    public BaseCampaignVoterObject(String firstName, String lastName, String voterId, BallotBoxObject ballotBoxObject, CampaignObject campaignObject) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.voterId = voterId;
        this.ballotBoxObject = ballotBoxObject;
        this.campaignObject = campaignObject;
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

    public BallotBoxObject getBallotBoxObject() {
        return ballotBoxObject;
    }

    public void setBallotBoxObject(BallotBoxObject ballotBoxObject) {
        this.ballotBoxObject = ballotBoxObject;
    }

    public CampaignObject getCampaignObject() {
        return campaignObject;
    }

    public void setCampaignObject(CampaignObject campaignObject) {
        this.campaignObject = campaignObject;
    }

    public Integer getCampaignType () throws Exception {
        Integer campaignType = PARAM_CAMPAIGN_TYPE_MUNICIPAL;
        if (this.campaignObject != null) {
            campaignType = this.campaignObject.getType();
        }

        if (campaignType == null) {
            throw new Exception();
        }
        return campaignType;
    }

}
