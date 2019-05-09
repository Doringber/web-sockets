package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class VoterCustomGroupMappingObject extends BaseEntity {
    private VoterObject voterObject;
    private CustomGroupObject customGroupObject;

    public VoterCustomGroupMappingObject() {

    }

    public VoterCustomGroupMappingObject(int voterOid, int groupOid) {
        this.voterObject = new VoterObject(voterOid);
        this.customGroupObject = new CustomGroupObject(groupOid);
    }

    public VoterCustomGroupMappingObject(VoterObject voterObject, CustomGroupObject customGroupObject) {
        this.voterObject = voterObject;
        this.customGroupObject = customGroupObject;
    }

    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }

    public CustomGroupObject getCustomGroupObject() {
        return customGroupObject;
    }

    public void setCustomGroupObject(CustomGroupObject customGroupObject) {
        this.customGroupObject = customGroupObject;
    }
}
