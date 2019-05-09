package com.elector.Objects.General;

import com.elector.Objects.Entities.DynamicDropdownOptionObject;
import com.elector.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AdditionalCampaignFieldObject {
    private int type;
    private String key;
    private List<Option> options;
    private boolean editable;
    private String translation;

    public AdditionalCampaignFieldObject(int type, String key, List<Option> options, boolean editable) {
        this.type = type;
        this.key = key;
        this.options = options;
        this.editable = editable;
        this.translation = Utils.getTranslation(key);
    }

    public AdditionalCampaignFieldObject(boolean editable, int type, String key, List<DynamicDropdownOptionObject> options) {
        this.type = type;
        this.key = key;
        this.editable = editable;
        if (options != null) {
            this.options = new ArrayList<>();
            for (DynamicDropdownOptionObject optionObject : options) {
                this.options.add(new Option(optionObject));
            }

        }
        this.translation = Utils.getTranslation(key);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public static class Option {
        public int key;
        public String value;

        public Option(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public Option(DynamicDropdownOptionObject dynamicDropdownOptionObject) {
            this.key = dynamicDropdownOptionObject.getOid();
            this.value = dynamicDropdownOptionObject.getText();
        }

    }

}
