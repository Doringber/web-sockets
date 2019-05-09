package com.elector.Objects.Entities.LaborParty;

import com.elector.Objects.Entities.CityObject;
import com.elector.Objects.General.BaseEntity;

public class LaborPartyOfficeObject extends BaseEntity {
    private CityObject cityObject;
    private String street;
    private Integer houseNumber;
    private Integer zipCode;

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

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }
}
