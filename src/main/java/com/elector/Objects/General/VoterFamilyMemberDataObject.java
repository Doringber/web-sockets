package com.elector.Objects.General;

import com.elector.Objects.Entities.VoterFamilyMemberMapObject;

public class VoterFamilyMemberDataObject {
    private int familyMemberOid;
    private Integer relation;

    public VoterFamilyMemberDataObject (VoterFamilyMemberMapObject voterFamilyMemberMapObject) {
        this.familyMemberOid = voterFamilyMemberMapObject.getFamilyMember().getOid();
        this.relation = voterFamilyMemberMapObject.getRelation();
    }

    public int getFamilyMemberOid() {
        return familyMemberOid;
    }

    public void setFamilyMemberOid(int familyMemberOid) {
        this.familyMemberOid = familyMemberOid;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    @Override
    public boolean equals(Object other) {
        return this.getClass().equals(other.getClass()) && this.familyMemberOid == ((VoterFamilyMemberDataObject)other).getFamilyMemberOid();
    }
}
