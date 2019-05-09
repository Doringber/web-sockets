package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class DynamicDropdownOptionObject extends BaseEntity {
    private String jsonKey;
    private String text;

    public DynamicDropdownOptionObject() {
    }


    public DynamicDropdownOptionObject(String jsonKey, String text) {
        this.jsonKey = jsonKey;
        this.text = text;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public void setJsonKey(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
