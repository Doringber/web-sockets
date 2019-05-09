package com.elector.Objects.Entities.LaborParty;

import com.elector.Objects.Entities.BaseCampaignVoterObject;
import com.elector.Objects.Entities.CityObject;
import com.elector.Utils.Utils;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.elector.Utils.Definitions.*;

public class IsraelLaborPartyVoterObject extends BaseCampaignVoterObject {
    private String phone;
    private String partyDistrict;
    private String branch;
    private String activityDistrict;
    private String activityBranch;
    protected Date birthDate;
    private CityObject cityObject;
    private String street;
    private String houseNumber;
    private Integer zipCode;
    private CityObject officeCityObject;
    private String officeStreet;
    private String officeHouseNumber;
    private Integer officeZipCode;
    private String extraPhone;
    private String partyBranch;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPartyDistrict() {
        return partyDistrict;
    }

    public void setPartyDistrict(String partyDistrict) {
        this.partyDistrict = partyDistrict;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getActivityDistrict() {
        return activityDistrict;
    }

    public void setActivityDistrict(String activityDistrict) {
        this.activityDistrict = activityDistrict;
    }

    public String getActivityBranch() {
        return activityBranch;
    }

    public void setActivityBranch(String activityBranch) {
        this.activityBranch = activityBranch;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

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

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public CityObject getOfficeCityObject() {
        return officeCityObject;
    }

    public void setOfficeCityObject(CityObject officeCityObject) {
        this.officeCityObject = officeCityObject;
    }

    public String getOfficeStreet() {
        return officeStreet;
    }

    public void setOfficeStreet(String officeStreet) {
        this.officeStreet = officeStreet;
    }

    public Integer getOfficeZipCode() {
        return officeZipCode;
    }

    public void setOfficeZipCode(Integer officeZipCode) {
        this.officeZipCode = officeZipCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getOfficeHouseNumber() {
        return officeHouseNumber;
    }

    public void setOfficeHouseNumber(String officeHouseNumber) {
        this.officeHouseNumber = officeHouseNumber;
    }

    public String getExtraPhone() {
        return extraPhone;
    }

    public void setExtraPhone(String extraPhone) {
        this.extraPhone = extraPhone;
    }

    public String getPartyBranch() {
        return partyBranch;
    }

    public void setPartyBranch(String partyBranch) {
        this.partyBranch = partyBranch;
    }

    public String getOfficeAddress () {
        StringBuilder addressBuilder = new StringBuilder();
        boolean hasNumber = false;
        if (Utils.hasText(this.officeStreet)) {
            addressBuilder.append(this.officeStreet);
        }
        if (Utils.hasText(this.officeHouseNumber)) {
            addressBuilder.append(SPACE);
            addressBuilder.append(this.officeHouseNumber).append(COMMA);
            hasNumber = true;
        }
        if (!hasNumber) {
            addressBuilder.append(COMMA);
        }
        addressBuilder.append(SPACE);
        if (this.officeCityObject != null) {
            addressBuilder.append(this.officeCityObject.getName());
        }
        return addressBuilder.toString();
    }

    public void readVotersBookData (String row, String datesFormat) {
        try {
            List<String> fields = Arrays.asList(row.split(COMMA));
            this.voterId = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_VOTER_ID, MINUS, true, EMPTY);
            this.partyDistrict = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_PARTY_DISTRICT, EMPTY, false, EMPTY);
            this.firstName = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_FIRST_NAME, MINUS, true, EMPTY);
            this.lastName = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_LAST_NAME, MINUS, true, EMPTY);
            this.phone = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_PHONE, EMPTY, false, EMPTY);
            this.extraPhone = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_EXTRA_PHONE, EMPTY, false, EMPTY);
            if (Utils.hasText(fields.get(ISRAEL_LABOR_PARTY_VOTERS_BOOK_BIRTH_DATE))) {
                String birthDateString = fields.get(ISRAEL_LABOR_PARTY_VOTERS_BOOK_BIRTH_DATE).trim();
                if (!birthDateString.equals(MINUS)) {
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datesFormat);
                        this.birthDate = simpleDateFormat.parse(birthDateString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.activityDistrict = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_ACTIVITY_DISTRICT, EMPTY, false, EMPTY);
            this.activityBranch = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_ACTIVITY_BRANCH, EMPTY, false, EMPTY);
            this.street = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_STREET, EMPTY, false, EMPTY);
            this.houseNumber = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_HOUSE_NUMBER, EMPTY, false, EMPTY);
            this.zipCode = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_ZIP_CODE, null, false, 0);
            this.officeStreet = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_OFFICE_STREET, EMPTY, false, EMPTY);
            this.officeHouseNumber = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_OFFICE_HOUSE_NUMBER, EMPTY, false, EMPTY);
            this.partyBranch = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_PARTY_BRANCH, EMPTY, false, EMPTY);
            this.officeZipCode = setValueIfExists(fields, ISRAEL_LABOR_PARTY_VOTERS_BOOK_OFFICE_ZIP_CODE, null, false, 0);
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
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < value.length(); i++) {
                        Character character = value.charAt(i);
                        if (Character.isDigit(character)) {
                            stringBuilder.append(character);
                        }
                    }
                    value = stringBuilder.toString();
                    if (Utils.hasText(value) && StringUtils.isNumeric(value)) {
                        genericValue = (T)Integer.valueOf(value);
                    } else {
                        System.out.println("invalid value");
                    }
                }
            } else if (clazz instanceof Boolean) {
                genericValue = (T)Boolean.valueOf(value);
            } else if (clazz instanceof Double) {
                genericValue = (T)Double.valueOf(value);
            }
        } else {
            genericValue = (T) defaultValue;
        }
        return genericValue;
    }


}
