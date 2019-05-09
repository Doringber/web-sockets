package com.elector.Objects.Entities;

import java.util.Date;

public class SupporterCarStickerObject extends SupporterActionObject {
    private Date timeToCome;
    private String extraDetails;
    private String carNumber;

    public SupporterCarStickerObject() {
    }

    public SupporterCarStickerObject(VoterObject supporter, int initiatorOid, int initiatorType, Date creationDate, int type) {
        super(supporter, initiatorOid, initiatorType, creationDate, type);
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

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
