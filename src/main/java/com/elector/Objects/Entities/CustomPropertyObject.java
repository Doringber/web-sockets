package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.HashSet;
import java.util.Set;

public class CustomPropertyObject extends BaseEntity{
    private String name;
    private int type;
    private String value;
    private String translationKey;
    private String jsonKey;
    private Set<Integer> optionsOids = new HashSet<>();

    public CustomPropertyObject () {
    }


    public CustomPropertyObject (VoterCustomPropertyObject voterCustomPropertyObject) {
        this.oid = voterCustomPropertyObject.getCustomPropertyObject().getOid();
        this.name = voterCustomPropertyObject.getCustomPropertyObject().getName();
        this.type = voterCustomPropertyObject.getCustomPropertyObject().getType();
        this.jsonKey = voterCustomPropertyObject.getCustomPropertyObject().getJsonKey();
        this.value = voterCustomPropertyObject.getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public void setJsonKey(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public Set<Integer> getOptionsOids() {
        return optionsOids;
    }

    public void setOptionsOids(Set<Integer> optionsOids) {
        this.optionsOids = optionsOids;
    }

    public boolean isValidOption (int optionOid) {
        return this.optionsOids.contains(optionOid);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.getOid() == ((CustomPropertyObject)obj).getOid();
    }
}
