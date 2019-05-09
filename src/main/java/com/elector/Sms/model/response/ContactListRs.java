package com.elector.Sms.model.response;

import javax.xml.bind.annotation.XmlElement;

public class ContactListRs {

    @XmlElement
    private int cl_id;

    @XmlElement
    private String phone;

    @XmlElement
    private String name;

    public int getCl_id() {
        return cl_id;
    }

    public void setCl_id(int cl_id) {
        this.cl_id = cl_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
