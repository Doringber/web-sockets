package com.elector.Objects.Entities;

import com.elector.Objects.General.EventObject;

public class ElectionAssemblyObject extends EventObject {
    private String address;
    private String extraDetails;
    private int estimatedParticipantsCount;
    private VoterObject supporter;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public VoterObject getSupporter() {
        return supporter;
    }

    public void setSupporter(VoterObject supporter) {
        this.supporter = supporter;
    }
}
