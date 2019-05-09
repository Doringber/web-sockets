package com.elector.Objects.General;

import com.elector.Objects.Entities.VoterObject;

/**
 * Created by Shai on 2/19/2018.
 */

public class SupporterWithoutActivistsReportObject extends ReportObject{
    private int oid;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String address;
    private String birthDate;


    public SupporterWithoutActivistsReportObject(VoterObject voterObject) {
        this.oid = voterObject.getOid();
        this.firstName = voterObject.getFirstName();
        this.lastName = voterObject.getLastName();
        this.fullName = voterObject.getFullName();
        this.phone = voterObject.getPhone();
        this.address = voterObject.getAddress();
        this.birthDate = voterObject.getFormattedBirthDate();

    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
