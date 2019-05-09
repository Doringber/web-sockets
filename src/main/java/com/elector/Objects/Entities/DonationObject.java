package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

/**
 * Created by Sigal on 4/2/2018.
 */
public class DonationObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private double sum;
    private Date date;
    private String donorId;
    private String donorFirstName;
    private String donorLastName;
    private Date donorBirthDate;
    private String donorAddress;
    private String donorPhone;
    private String relationToDonor;
    private boolean paymentConfirmationImage;

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDonorFirstName() {
        return donorFirstName;
    }

    public void setDonorFirstName(String donorFirstName) {
        this.donorFirstName = donorFirstName;
    }

    public String getDonorLastName() {
        return donorLastName;
    }

    public void setDonorLastName(String donorLastName) {
        this.donorLastName = donorLastName;
    }

    public Date getDonorBirthDate() {
        return donorBirthDate;
    }

    public void setDonorBirthDate(Date donorBirthDate) {
        this.donorBirthDate = donorBirthDate;
    }

    public String getDonorAddress() {
        return donorAddress;
    }

    public void setDonorAddress(String donorAddress) {
        this.donorAddress = donorAddress;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public void setDonorPhone(String donorPhone) {
        this.donorPhone = donorPhone;
    }

    public String getRelationToDonor() {
        return relationToDonor;
    }

    public void setRelationToDonor(String relationToDonor) {
        this.relationToDonor = relationToDonor;
    }

    public boolean isPaymentConfirmationImage() {
        return paymentConfirmationImage;
    }

    public void setPaymentConfirmationImage(boolean paymentConfirmationImage) {
        this.paymentConfirmationImage = paymentConfirmationImage;
    }
}
