package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class CustomPropertyOptionObject extends BaseEntity {
    private CustomPropertyObject customPropertyObject;
    private String translationKey;

    public CustomPropertyObject getCustomPropertyObject() {
        return customPropertyObject;
    }

    public void setCustomPropertyObject(CustomPropertyObject customPropertyObject) {
        this.customPropertyObject = customPropertyObject;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }
}
