package com.elector.Objects.Entities.LikudParty;

import com.elector.Objects.General.BaseCampaignEditableDataObject;

public class LikudVoterEditableDataObject extends BaseCampaignEditableDataObject {
    private int collectionCode;
    private String birthCountry;

    public LikudVoterEditableDataObject () {
    }

    public LikudVoterEditableDataObject (LikudPartyVoterObject likudPartyVoterObject) {
        this.collectionCode = likudPartyVoterObject.getCollectionCode();
        this.birthCountry = likudPartyVoterObject.getBirthCountry();
    }

    public int getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(int collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }
}
