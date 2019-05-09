package com.elector.Objects.Entities;

import java.util.Date;

public class UnverifiedVoterObject extends VoterObject {
    private int addingUserType;
    private int addingUserOid;
    private Date creationDate;

    public int getAddingUserType() {
        return addingUserType;
    }

    public void setAddingUserType(int addingUserType) {
        this.addingUserType = addingUserType;
    }

    public int getAddingUserOid() {
        return addingUserOid;
    }

    public void setAddingUserOid(int addingUserOid) {
        this.addingUserOid = addingUserOid;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
