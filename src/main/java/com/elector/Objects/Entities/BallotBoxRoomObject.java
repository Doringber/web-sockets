package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Utils.Utils;

import java.util.Arrays;
import java.util.List;

import static com.elector.Utils.Definitions.*;

public class BallotBoxRoomObject extends BaseEntity{
    private BallotBoxObject ballotBoxObject;
    private String street;
    private Integer type;
    private Integer fromHouse;
    private Integer toHouse;
    private String fromLetter;
    private String toLetter;

    public BallotBoxRoomObject () {
    }

    public BallotBoxRoomObject(BallotBoxObject ballotBoxObject) {
        this.ballotBoxObject = ballotBoxObject;
    }

    public BallotBoxObject getBallotBoxObject() {
        return ballotBoxObject;
    }

    public void setBallotBoxObject(BallotBoxObject ballotBoxObject) {
        this.ballotBoxObject = ballotBoxObject;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFromHouse() {
        return fromHouse;
    }

    public void setFromHouse(Integer fromHouse) {
        this.fromHouse = fromHouse;
    }

    public Integer getToHouse() {
        return toHouse;
    }

    public void setToHouse(Integer toHouse) {
        this.toHouse = toHouse;
    }

    public String getFromLetter() {
        return fromLetter;
    }

    public void setFromLetter(String fromLetter) {
        this.fromLetter = fromLetter;
    }

    public String getToLetter() {
        return toLetter;
    }

    public void setToLetter(String toLetter) {
        this.toLetter = toLetter;
    }

    public void readBallotBoxRoomData (String row) {
        List<String> fields = Arrays.asList(row.split(COMMA));
        this.street = fields.get(BALLOT_BOXES_DATA_STREET);
        this.type = Utils.ballotBoxType(fields.get(BALLOT_BOXES_DATA_TYPE).trim());
        this.fromHouse = Integer.valueOf(fields.get(BALLOT_BOXES_DATA_FROM_HOUSE).trim());
        this.toHouse = Integer.valueOf(fields.get(BALLOT_BOXES_DATA_TO_HOUSE).trim());
        this.fromLetter = fields.get(BALLOT_BOXES_DATA_FROM_LETTER).trim();
        this.toLetter = fields.get(BALLOT_BOXES_DATA_TO_LETTER).trim();
    }
}
