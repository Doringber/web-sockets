package com.elector.Objects.Entities;

import java.util.Date;

public class SupporterElectionAssemblyParticipationObject extends SupporterActionObject {
    private String extraDetails;
    private String preferredArea;

    public SupporterElectionAssemblyParticipationObject() {
    }

    public SupporterElectionAssemblyParticipationObject(VoterObject supporter, int initiatorOid, int initiatorType, Date creationDate, int type, String extraDetails, String preferredArea) {
        super(supporter, initiatorOid, initiatorType, creationDate, type);
        this.extraDetails = extraDetails;
        this.preferredArea = preferredArea;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public String getPreferredArea() {
        return preferredArea;
    }

    public void setPreferredArea(String preferredArea) {
        this.preferredArea = preferredArea;
    }
}
