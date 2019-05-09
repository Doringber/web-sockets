package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.*;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Utils.hasText;

public class BallotBoxObject extends BaseEntity {
    private CampaignObject campaignObject;
    private Integer number;
    private String address;
    private CityObject cityObject;
    private String place;
    private List<BallotBoxRoomObject> rooms;
    private Set<ObserverObject> observers;
    private BallotBoxBlockObject ballotBoxBlockObject;
    private boolean disabledAccess;
    private boolean disabledVotingArrangements;
    private String district;
    private String regionalCouncil;

    public BallotBoxObject () {

    }

    public BallotBoxObject (Integer number, CityObject cityObject) {
        this.number = number;
        this.cityObject = cityObject;
    }

    public CampaignObject getCampaignObject() {
        return campaignObject;
    }

    public void setCampaignObject(CampaignObject campaignObject) {
        this.campaignObject = campaignObject;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CityObject getCityObject() {
        return cityObject;
    }

    public void setCityObject(CityObject cityObject) {
        this.cityObject = cityObject;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<BallotBoxRoomObject> getRooms() {
        return rooms;
    }

    public void setRooms(List<BallotBoxRoomObject> rooms) {
        this.rooms = rooms;
    }

    public Set<ObserverObject> getObservers() {
        return observers;
    }

    public void setObservers(Set<ObserverObject> observers) {
        this.observers = observers;
    }

    public BallotBoxBlockObject getBallotBoxBlockObject() {
        return ballotBoxBlockObject;
    }

    public void setBallotBoxBlockObject(BallotBoxBlockObject ballotBoxBlockObject) {
        this.ballotBoxBlockObject = ballotBoxBlockObject;
    }

    public void addRoom (BallotBoxRoomObject room) {
        if (this.rooms == null) {
            this.rooms = new ArrayList<>();
        }
        this.rooms.add(room);
    }

    public void addObserver (ObserverObject observerObject) {
        if (this.observers == null) {
            this.observers = new HashSet<>();
        }
        this.observers.add(observerObject);
    }

    public void clearObservers () {
        if (this.observers != null && !this.observers.isEmpty()) {
            this.observers.clear();
        }
    }

    public boolean hasObservers () {
        return this.observers != null && !this.observers.isEmpty();
    }

    public boolean isDisabledAccess() {
        return disabledAccess;
    }

    public void setDisabledAccess(boolean disabledAccess) {
        this.disabledAccess = disabledAccess;
    }

    public boolean isDisabledVotingArrangements() {
        return disabledVotingArrangements;
    }

    public void setDisabledVotingArrangements(boolean disabledVotingArrangements) {
        this.disabledVotingArrangements = disabledVotingArrangements;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRegionalCouncil() {
        return regionalCouncil;
    }

    public void setRegionalCouncil(String regionalCouncil) {
        this.regionalCouncil = regionalCouncil;
    }

    public void readBallotBoxesData (String row, boolean regional) {
        List<String> fields = Arrays.asList(row.split(COMMA));
        if (regional) {
            this.number = Integer.valueOf(fields.get(REGIONAL_BALLOT_BOXES_DATA_NUMBER).trim().replace(DOT, EMPTY).replaceAll("\"",EMPTY));
            this.place = fields.get(REGIONAL_BALLOT_BOXES_DATA_PLACE).trim().replaceAll("\"",EMPTY);
            this.address = fields.get(REGIONAL_BALLOT_BOXES_DATA_ADDRESS).trim().replaceAll("\"",EMPTY);
            this.disabledAccess = hasText(fields.get(REGIONAL_BALLOT_BOXES_DATA_DISABLED_ACCESS).trim().replaceAll("\"",EMPTY));
            this.disabledVotingArrangements = hasText(fields.get(REGIONAL_BALLOT_BOXES_DATA_DISABLED_ACCESS).trim().replaceAll("\"",EMPTY));
            this.district = fields.get(REGIONAL_BALLOT_BOXES_DATA_DISTRICT).trim().replaceAll("\"",EMPTY);
            this.regionalCouncil = fields.get(REGIONAL_BALLOT_BOXES_DATA_REGION_NAME).trim().replaceAll("\"",EMPTY);

        } else {
            this.number = Integer.valueOf(fields.get(BALLOT_BOXES_DATA_NUMBER).trim().replace(DOT, EMPTY).replaceAll("\"",EMPTY));
            this.place = fields.get(BALLOT_BOXES_DATA_PLACE).trim().replaceAll("\"",EMPTY);
            this.address = fields.get(BALLOT_BOXES_DATA_ADDRESS).trim().replaceAll("\"",EMPTY);
            this.disabledAccess = hasText(fields.get(BALLOT_BOXES_DATA_DISABLED_ACCESS).trim().replaceAll("\"",EMPTY));
            this.disabledVotingArrangements = hasText(fields.get(BALLOT_BOXES_DATA_DISABLES_VOTING_ARRANGEMENTS).trim().replaceAll("\"",EMPTY));
            this.district = fields.get(BALLOT_BOXES_DATA_DISTRICT).trim().replaceAll("\"",EMPTY);
        }
    }

    public String getUiDescription () {
        return String.format("%s - %s, %s", this.number, this.place, this.address);
    }

    public BallotBoxObject customClone() {
        BallotBoxObject cloned = new BallotBoxObject();
        cloned.setOid(this.oid);
        cloned.setCampaignObject(this.campaignObject);
        cloned.setNumber(this.number);
        cloned.setAddress(this.address);
        cloned.setCityObject(this.cityObject);
        cloned.setObservers(this.observers);
        cloned.setBallotBoxBlockObject(this.ballotBoxBlockObject);
        cloned.setDisabledAccess(this.disabledAccess);
        cloned.setDisabledVotingArrangements(this.disabledVotingArrangements);
        return cloned;
    }

    public String getNumberWithDot () {
        String numberAsString = Integer.toString(this.number);
        return new StringBuilder(numberAsString).insert(numberAsString.length()-1, ".").toString();
    }

}

