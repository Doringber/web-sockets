package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class VoterCustomPropertyObject extends BaseEntity{
    private VoterObject voterObject;
    private String value;
    private CustomPropertyObject customPropertyObject;

    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }

    public CustomPropertyObject getCustomPropertyObject() {
        return customPropertyObject;
    }

    public void setCustomPropertyObject(CustomPropertyObject customPropertyObject) {
        this.customPropertyObject = customPropertyObject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
