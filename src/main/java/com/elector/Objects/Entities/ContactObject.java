package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class ContactObject extends BaseEntity {
    protected String phone;
    protected String email;
    protected String firstName;
    protected String lastName;
    protected AdminUserObject adminUserObject;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public String getFullName () {
        return String.format("%s %s", this.firstName, this.lastName);
    }

}
