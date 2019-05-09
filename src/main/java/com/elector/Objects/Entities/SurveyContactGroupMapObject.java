package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class SurveyContactGroupMapObject extends BaseEntity {
    private SurveyObject surveyObject;
    private CustomGroupObject customGroupObject;
    private ContactsGroupObject contactsGroupObject;
    private Integer groupType;

    public SurveyContactGroupMapObject() {
    }

    public SurveyContactGroupMapObject(SurveyObject surveyObject, CustomGroupObject customGroupObject) {
        this.surveyObject = surveyObject;
        this.customGroupObject = customGroupObject;
    }

    public SurveyContactGroupMapObject(SurveyObject surveyObject, int groupType) {
        this.surveyObject = surveyObject;
        this.groupType = groupType;
    }



    public SurveyObject getSurveyObject() {
        return surveyObject;
    }

    public void setSurveyObject(SurveyObject surveyObject) {
        this.surveyObject = surveyObject;
    }

    public CustomGroupObject getCustomGroupObject() {
        return customGroupObject;
    }

    public void setCustomGroupObject(CustomGroupObject customGroupObject) {
        this.customGroupObject = customGroupObject;
    }

    public ContactsGroupObject getContactsGroupObject() {
        return contactsGroupObject;
    }

    public void setContactsGroupObject(ContactsGroupObject contactsGroupObject) {
        this.contactsGroupObject = contactsGroupObject;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }
}
