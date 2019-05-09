package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class SurveyVoterAnswerObject extends BaseEntity {
    private SurveyObject surveyObject;
    private VoterObject voterObject;
    private SurveyAnswerObject answerObject;
    private Date date;

    public SurveyVoterAnswerObject() {
    }

    public SurveyVoterAnswerObject(SurveyObject surveyObject, VoterObject voterObject, SurveyAnswerObject answerObject) {
        this.surveyObject = surveyObject;
        this.voterObject = voterObject;
        this.answerObject = answerObject;
        this.date = new Date();
    }

    public SurveyObject getSurveyObject() {
        return surveyObject;
    }

    public void setSurveyObject(SurveyObject surveyObject) {
        this.surveyObject = surveyObject;
    }

    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SurveyAnswerObject getAnswerObject() {
        return answerObject;
    }

    public void setAnswerObject(SurveyAnswerObject answerObject) {
        this.answerObject = answerObject;
    }
}
