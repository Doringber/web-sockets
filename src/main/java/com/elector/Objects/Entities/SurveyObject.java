package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseContactGroupObject;
import com.elector.Objects.General.BaseEntity;
import com.elector.Utils.NavigationUtils;

import java.util.Date;
import java.util.List;

import static com.elector.Utils.Definitions.EMPTY;

public class SurveyObject extends BaseEntity {
    private String question;
    private AdminUserObject adminUserObject;
    private Date creationDate;
    private int recipients;
    private String filter;
    private boolean active;
    private boolean published;
    private List<SurveyAnswerObject> answers;
    private List<BaseContactGroupObject> groups;
    private String smsSenderName;
    private String smsText;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getRecipients() {
        return recipients;
    }

    public void setRecipients(int recipients) {
        this.recipients = recipients;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<SurveyAnswerObject> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SurveyAnswerObject> answers) {
        this.answers = answers;
    }

    public String getShortQuestion () {
        return this.question == null ? EMPTY : this.question.substring(0, Math.min(70, this.question.length()));
    }

    public List<BaseContactGroupObject> getGroups() {
        return groups;
    }

    public void setGroups(List<BaseContactGroupObject> groups) {
        this.groups = groups;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getSmsSenderName() {
        return smsSenderName;
    }

    public void setSmsSenderName(String smsSenderName) {
        this.smsSenderName = smsSenderName;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

}
