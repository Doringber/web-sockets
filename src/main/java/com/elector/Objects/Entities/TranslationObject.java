package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import static com.elector.Utils.Definitions.*;

/**
 * Created by Sigal on 9/2/2016.
 */
public class TranslationObject extends BaseEntity{
    private String translationKey;
    private String translationValue;
    private String en;
    private String ru;

    public TranslationObject() {
    }

    public TranslationObject(String translationKey, String translationValue, String en) {
        this.translationKey = translationKey;
        this.translationValue = translationValue;
        this.en = en;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationValue() {
        return translationValue;
    }

    public void setTranslationValue(String translationValue) {
        this.translationValue = translationValue;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getTranslation(Integer language) {
        String translation = translationValue;
        if (language != null) {
            switch (language) {
                case PARAM_LANGUAGE_RUSSIAN:
                    translation = ru;
                    break;
                case PARAM_LANGUAGE_AMHARIC:
                    break;
                case PARAM_LANGUAGE_ENGLISH:
                    break;
                case PARAM_LANGUAGE_FRENCH:
                    break;
                case PARAM_LANGUAGE_SPANISH:
                    break;
                case PARAM_LANGUAGE_ARAB:
                    break;
                case PARAM_LANGUAGE_HEBREW:
                    translation = translationValue;
                default:
                    break;

            }
        }
        return translation;

    }
}
