package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.elector.Utils.Definitions.*;

public class CampaignVoterObject extends BaseCampaignVoterObject {
    private String fatherName;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private int voterNumber;
    private String ballotBoxNumber;
    private CityObject cityObject;

    public CampaignVoterObject(){

    }

    public CampaignVoterObject(int oid){
        this.oid = oid;
    }

    public CampaignVoterObject(String firstName, String lastName, String voterId, BallotBoxObject ballotBoxObject, CampaignObject campaignObject) {
        super(firstName, lastName, voterId, ballotBoxObject, campaignObject);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public BallotBoxObject getBallotBoxObject() {
        return ballotBoxObject;
    }

    public void setBallotBoxObject(BallotBoxObject ballotBoxObject) {
        this.ballotBoxObject = ballotBoxObject;
    }

    public CampaignObject getCampaignObject() {
        return campaignObject;
    }

    public void setCampaignObject(CampaignObject campaignObject) {
        this.campaignObject = campaignObject;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public int getVoterNumber() {
        return voterNumber;
    }

    public void setVoterNumber(int voterNumber) {
        this.voterNumber = voterNumber;
    }

    public String getBallotBoxNumber() {
        return ballotBoxNumber;
    }

    public void setBallotBoxNumber(String ballotBoxNumber) {
        this.ballotBoxNumber = ballotBoxNumber;
    }

    public CityObject getCityObject() {
        return cityObject;
    }

    public void setCityObject(CityObject cityObject) {
        this.cityObject = cityObject;
    }

    public void readVotersBookData (String row) {
        List<String> fields = Arrays.asList(row.split(COMMA));
        this.voterId = fields.get(VOTERS_BOOK_VOTER_ID).trim();
        this.lastName = fields.get(VOTERS_BOOK_LAST_NAME).trim();
        this.firstName = fields.get(VOTERS_BOOK_FIRST_NAME).trim();
        this.fatherName = fields.get(VOTERS_BOOK_FATHER_NAME).trim();
        this.street = fields.get(VOTERS_BOOK_STREET).trim();
        this.houseNumber = fields.get(VOTERS_BOOK_HOUSE_NUMBER).trim();
        this.apartmentNumber = fields.get(VOTERS_BOOK_APARTMENT_NUMBER).trim();
        String voterNumber = fields.get(VOTERS_BOOK_VOTER_NUMBER);
        this.voterNumber = StringUtils.isNumeric(voterNumber) ? Integer.valueOf(voterNumber.trim()) : -999;
        this.ballotBoxNumber = fields.get(VOTERS_BOOK_BALLOT_BOX_NUMBER);
    }

    public boolean hasChanged (CampaignVoterObject other) {
        return !this.firstName.equals(other.getFirstName()) ||
                !this.lastName.equals(other.getLastName()) ||
                !this.street.equals(other.getStreet()) ||
                !this.houseNumber.equals(other.getHouseNumber()) ||
                !this.apartmentNumber.equals(other.getApartmentNumber()) ||
                this.voterNumber != other.getVoterNumber() ||
                (this.ballotBoxObject != null && other.getBallotBoxNumber() != null && this.ballotBoxObject.getOid() != other.getBallotBoxObject().getOid());
    }
}
