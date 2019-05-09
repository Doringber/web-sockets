package com.elector.Objects.Entities.LaborParty;

import com.elector.Objects.General.BaseCampaignEditableDataObject;

public class IsraelLaborVoterEditableDataObject extends BaseCampaignEditableDataObject {
    private String activityDistrict;
    private String activityBranch;
    private String partyDistrict;

    public IsraelLaborVoterEditableDataObject () {
    }

    public IsraelLaborVoterEditableDataObject (IsraelLaborPartyVoterObject laborPartyVoterObject) {
        this.activityDistrict = laborPartyVoterObject.getActivityDistrict();
        this.activityBranch = laborPartyVoterObject.getActivityBranch();
        this.partyDistrict = laborPartyVoterObject.getPartyDistrict();
    }


    public String getActivityDistrict() {
        return activityDistrict;
    }

    public void setActivityDistrict(String activityDistrict) {
        this.activityDistrict = activityDistrict;
    }

    public String getActivityBranch() {
        return activityBranch;
    }

    public void setActivityBranch(String activityBranch) {
        this.activityBranch = activityBranch;
    }

    public String getPartyDistrict() {
        return partyDistrict;
    }

    public void setPartyDistrict(String partyDistrict) {
        this.partyDistrict = partyDistrict;
    }
}
