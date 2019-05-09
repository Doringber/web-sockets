package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class SurveyAnswerObject extends BaseEntity {
    private SurveyObject surveyObject;
    private String text;
    private Integer action;

    public SurveyObject getSurveyObject() {
        return surveyObject;
    }

    public void setSurveyObject(SurveyObject surveyObject) {
        this.surveyObject = surveyObject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}
