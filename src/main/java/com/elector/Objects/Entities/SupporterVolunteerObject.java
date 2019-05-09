package com.elector.Objects.Entities;

import java.util.Date;

public class SupporterVolunteerObject extends SupporterActionObject {
    private boolean mobile;
    private String role;

    public SupporterVolunteerObject() {
    }

    public SupporterVolunteerObject (VoterObject supporter, int initiatorOid, int initiatorType, Date creationDate, int type) {
        super(supporter, initiatorOid, initiatorType, creationDate, type);
    }


    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
