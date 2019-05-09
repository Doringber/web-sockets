package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Objects.General.BaseUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.elector.Utils.Definitions.EMPTY;
import static com.elector.Utils.Definitions.SUPPORTER_ACTION_TYPE_UNKNOWN;
import static com.elector.Utils.Definitions.UNDERSCORE;

public class SupporterActionObject extends BaseEntity {
    protected VoterObject supporter;
    protected int initiatorOid;
    protected int initiatorType;
    protected Date creationDate;
    private int type;
    protected boolean done;
    protected BaseUser initiator;

    public SupporterActionObject() {
    }

    public SupporterActionObject(VoterObject supporter, int initiatorOid, int initiatorType, Date creationDate, int type) {
        this.supporter = supporter;
        this.initiatorOid = initiatorOid;
        this.initiatorType = initiatorType;
        this.creationDate = creationDate;
        this.type = type;
    }

    public VoterObject getSupporter() {
        return supporter;
    }

    public void setSupporter(VoterObject supporter) {
        this.supporter = supporter;
    }

    public int getInitiatorOid() {
        return initiatorOid;
    }

    public void setInitiatorOid(int initiatorOid) {
        this.initiatorOid = initiatorOid;
    }

    public int getInitiatorType() {
        return initiatorType;
    }

    public void setInitiatorType(int initiatorType) {
        this.initiatorType = initiatorType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String createUiKey() {
        return EMPTY;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public BaseUser getInitiator() {
        return initiator;
    }

    public void setInitiator(BaseUser initiator) {
        this.initiator = initiator;
    }

    public static int getTypeByUiKey(String key) {
        int type = SUPPORTER_ACTION_TYPE_UNKNOWN;
        try {
            List<String> tokens = Arrays.asList(key.split(UNDERSCORE));
            if (tokens.size() == 2) {
                type = Integer.valueOf(tokens.get(0));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return type;

    }

    public static int getOidByUiKey(String key) {
        int oid = -1;
        try {
            List<String> tokens = Arrays.asList(key.split(UNDERSCORE));
            if (tokens.size() == 2) {
                oid = Integer.valueOf(tokens.get(1));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return oid;

    }

    public String getPrintableType () {
        String type = EMPTY;
        if (getClass().equals(SupporterSupportersListDelivery.class)) {
            type = "supporters.list";
        } else if (getClass().equals(SupporterBillboardRequestObject.class)) {
            type = "setup.billboard";
        } else if (getClass().equals(SupporterVolunteerObject.class)) {
            type = "coordinate.volunteer";
        } else if (getClass().equals(SupporterElectionAssemblyRequestObject.class)) {
            type = "coordinate.election.assembly";
        } else if (getClass().equals(SupporterCarStickerObject.class)) {
            type = "car.sticker";
        } else if (getClass().equals(SupporterElectionAssemblyParticipationObject.class)) {
            type = "election.assembly.participation";
        }

        return type;
    }


}
