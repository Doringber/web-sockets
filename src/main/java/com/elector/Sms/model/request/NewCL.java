package com.elector.Sms.model.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NewCL {

    User user;
    ContactList cl;

    public User getUser() {
        return user;
    }

    @XmlElement
    public void setUser(User user) {
        this.user = user;
    }

    public ContactList getCl() {
        return cl;
    }

    @XmlElement
    public void setCl(ContactList cl) {
        this.cl = cl;
    }
}
