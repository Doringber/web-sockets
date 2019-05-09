package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Objects.General.BaseUser;

import java.util.Date;

public class ElectionDayAlert extends BaseEntity {
    private AdminUserObject adminUserObject;
    private int senderType;
    private int senderOid;
    private String text;
    private Date time;
    private BaseUser user;
    private boolean handled;
    private VoterObject voterObject;
    private boolean simulation;

    public ElectionDayAlert(){
    }

    public ElectionDayAlert(BaseUser user, String text, VoterObject voterObject) {
        this.adminUserObject = user.getCampaignManager();
        this.senderType = user.getUserType();
        this.senderOid = user.getOid();
        this.text = text;
        this.time = new Date();
        this.handled = false;
        this.voterObject = voterObject;
        this.simulation = user.getCampaignManager().isSimulation();
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BaseUser getUser() {
        return user;
    }

    public void setUser(BaseUser user) {
        this.user = user;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }
}
