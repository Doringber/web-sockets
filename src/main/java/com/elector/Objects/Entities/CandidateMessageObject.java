package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Objects.General.BaseUser;

import java.util.Date;

public class CandidateMessageObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private String title;
    private String body;
    protected Date date;
    private boolean opened;
    private boolean closed;
    private int type;
    private int senderType;
    private int senderOid;
    private VoterObject subjectVoter;
    private GroupManagerObject groupManagerObject;

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public int getSenderOid() {
        return senderOid;
    }

    public void setSenderOid(int senderOid) {
        this.senderOid = senderOid;
    }

    public VoterObject getSubjectVoter() {
        return subjectVoter;
    }

    public void setSubjectVoter(VoterObject subjectVoter) {
        this.subjectVoter = subjectVoter;
    }

    public GroupManagerObject getGroupManagerObject() {
        return groupManagerObject;
    }

    public void setGroupManagerObject(GroupManagerObject groupManagerObject) {
        this.groupManagerObject = groupManagerObject;
    }
}
