package com.elector.Objects.Entities.LikudParty;

import com.elector.Objects.Entities.BaseCampaignVoterObject;
import com.elector.Objects.Entities.CityObject;
import com.elector.Utils.Utils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.elector.Utils.Definitions.*;

public class LikudPartyVoterObject extends BaseCampaignVoterObject {
    private CityObject cityObject;
    private String street;
    private String houseNumber;
    private Integer zipCode;
    private int collectionCode;
    private String collectionDescription;
    private String phone;
    private String homePhone;
    private Date joinDate;
    private String birthCountry;
    private CityObject branchCity;
    private String primaryArea;
    private Boolean male;
    private String branch;

    public CityObject getCityObject() {
        return cityObject;
    }

    public void setCityObject(CityObject cityObject) {
        this.cityObject = cityObject;
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

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public int getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(int collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getCollectionDescription() {
        return collectionDescription;
    }

    public void setCollectionDescription(String collectionDescription) {
        this.collectionDescription = collectionDescription;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public CityObject getBranchCity() {
        return branchCity;
    }

    public void setBranchCity(CityObject branchCity) {
        this.branchCity = branchCity;
    }

    public String getPrimaryArea() {
        return primaryArea;
    }

    public void setPrimaryArea(String primaryArea) {
        this.primaryArea = primaryArea;
    }

    public Boolean getMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public void readVotersBookData (String row) {
        try {
            List<String> fields = Arrays.asList(row.split(COMMA));
            this.voterId = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_VOTER_ID, MINUS, true, EMPTY);
            this.firstName = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_FIRST_NAME, MINUS, true, EMPTY);
            this.lastName = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_LAST_NAME, MINUS, true, EMPTY);
            this.street = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_STREET, EMPTY, false, EMPTY);
            this.houseNumber = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_HOUSE_NUMBER, EMPTY, false, EMPTY);
            this.zipCode = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_ZIP_CODE, null, false, 0);
            this.collectionCode = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_COLLECTION_CODE, null, true, 0);
            this.collectionDescription = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_COLLECTION_DESCRIPTION, EMPTY, false, EMPTY);
            this.phone = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_PHONE, EMPTY, false, EMPTY);
            this.homePhone = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_HOME_PHONE, EMPTY, false, EMPTY);
            String joinDateString = fields.get(LIKUD_PARTY_VOTERS_BOOK_JOIN_DATE).trim();
            if (Utils.hasText(joinDateString) && !joinDateString.equals(MINUS)) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    this.joinDate = simpleDateFormat.parse(joinDateString);
                } catch (Exception e) {
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                        this.joinDate = simpleDateFormat.parse(joinDateString);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            this.birthCountry = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_BIRTH_COUNTRY, EMPTY, false, EMPTY);
            this.primaryArea = setValueIfExists(fields, LIKUD_PARTY_VOTERS_BOOK_PRIMARY_AREA, EMPTY, false, EMPTY);
            String genderString = fields.get(LIKUD_PARTY_VOTERS_BOOK_MALE).trim();
            if (Utils.hasText(genderString)) {
                if (genderString.equals("זכר")) {
                    this.male = true;
                } else if (genderString.equals("נקבה")) {
                    this.male = false;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> T setValueIfExists (List<String> rowFields, int index, String defaultValue, boolean required, T clazz) throws Exception {
        String value = null;
        if (rowFields.size() > index) {
            value = rowFields.get(index);
            if (value == null) {
                if (required) {
                    throw new Exception();
                } else {
                    value = defaultValue;
                }
            } else {
                if (value.equals(MINUS)) {
                    value = defaultValue;
                } else {
                    value = value.trim().replace(";", COMMA);
                }
            }
        } else {
            throw new Exception();
        }
        T genericValue = (T) value;
        if (Utils.hasText(value)) {
            if (clazz instanceof Integer) {
                if (StringUtils.isNumeric(value)) {
                    genericValue = (T)Integer.valueOf(value);
                } else {
                    genericValue = null;
                    System.out.println("invalid value");
                }
            } else if (clazz instanceof Boolean) {
                genericValue = (T)Boolean.valueOf(value);
            } else if (clazz instanceof Double) {
                genericValue = (T)Double.valueOf(value);
            }
        } else {
            genericValue = (T) defaultValue;
        }
        if (clazz instanceof String) {
            genericValue = (T) Utils.eliminateDuplicatedHeadAndTailQuotes((String)genericValue);
        }
        return genericValue;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
