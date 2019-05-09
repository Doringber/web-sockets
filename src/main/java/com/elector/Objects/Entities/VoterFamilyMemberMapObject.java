package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class VoterFamilyMemberMapObject extends BaseEntity {
    private VoterObject sourceVoter;
    private VoterObject familyMember;
    private Integer relation;

    public VoterFamilyMemberMapObject () {
    }

    public VoterFamilyMemberMapObject(VoterObject sourceVoter, VoterObject familyMember, Integer relation) {
        this.sourceVoter = sourceVoter;
        this.familyMember = familyMember;
        this.relation = relation;
    }

    public VoterObject getSourceVoter() {
        return sourceVoter;
    }

    public void setSourceVoter(VoterObject sourceVoter) {
        this.sourceVoter = sourceVoter;
    }

    public VoterObject getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(VoterObject familyMember) {
        this.familyMember = familyMember;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }
}
