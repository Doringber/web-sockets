package com.elector.Objects.Entities;

import java.util.Date;

import static com.elector.Utils.Definitions.SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY;

public class SupporterElectionAssemblyRequestObject extends SupporterActionObject {
    private Date desiredDate;
    private String extraDetails;
    private int estimatedParticipantsCount;

    public SupporterElectionAssemblyRequestObject() {
    }

    public SupporterElectionAssemblyRequestObject (VoterObject supporter, int initiatorOid, int initiatorType, Date creationDate, int type) {
        super(supporter, initiatorOid, initiatorType, creationDate, type);
    }

    public Date getDesiredDate() {
        return desiredDate;
    }

    public void setDesiredDate(Date desiredDate) {
        this.desiredDate = desiredDate;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public int getEstimatedParticipantsCount() {
        return estimatedParticipantsCount;
    }

    public void setEstimatedParticipantsCount(int estimatedParticipantsCount) {
        this.estimatedParticipantsCount = estimatedParticipantsCount;
    }

    public String createUiKey () {
        return String.format("%s_%s", SUPPORTER_ACTION_TYPE_ELECTION_ASSEMBLY, this.oid);
    }

}
