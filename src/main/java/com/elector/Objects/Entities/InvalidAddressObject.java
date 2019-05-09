package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class InvalidAddressObject extends BaseEntity{

    private String address;

    public InvalidAddressObject() {
    }

    public InvalidAddressObject(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
