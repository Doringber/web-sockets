package com.elector.Objects.Entities;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.elector.Utils.Definitions.SUPPORTER_ACTION_TYPE_BILLBOARD;
import static com.elector.Utils.Definitions.SUPPORTER_ACTION_TYPE_UNKNOWN;
import static com.elector.Utils.Definitions.UNDERSCORE;

public class SupporterBillboardRequestObject extends SupporterActionObject{
    private Date timeToCome;
    private String extraDetails;

    public SupporterBillboardRequestObject() {
    }

    public SupporterBillboardRequestObject (VoterObject supporter, int initiatorOid, int initiatorType, Date creationDate, int type) {
        super(supporter, initiatorOid, initiatorType, creationDate, type);
    }

    public SupporterBillboardRequestObject(String key) {
        try {
            List<String> tokens = Arrays.asList(key.split(UNDERSCORE));
            if (tokens.size() == 2) {
                this.oid =Integer.valueOf(tokens.get(1));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    public Date getTimeToCome() {
        return timeToCome;
    }

    public void setTimeToCome(Date timeToCome) {
        this.timeToCome = timeToCome;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public String createUiKey () {
        return String.format("%s_%s", SUPPORTER_ACTION_TYPE_BILLBOARD, this.oid);
    }

}
