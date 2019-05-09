package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

/**
 * Created by Sigal on 6/30/2017.
 */
public class ActivistVoterMapObject extends BaseEntity {
    private VoterObject voter;
    private ActivistObject activist;
    private Date date;
    private String relation;
    private AdminUserObject adminUserObject;

    public ActivistVoterMapObject () {

    }

    public ActivistVoterMapObject (VoterObject voterObject, ActivistObject activist, AdminUserObject adminUserObject) {
        this.voter = voterObject;
        this.activist = activist;
        this.adminUserObject = adminUserObject;
        this.date = new Date();
    }

    public VoterObject getVoter() {
        return voter;
    }

    public void setVoter(VoterObject voter) {
        this.voter = voter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ActivistObject getActivist() {
        return activist;
    }

    public void setActivist(ActivistObject activist) {
        this.activist = activist;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }
}
