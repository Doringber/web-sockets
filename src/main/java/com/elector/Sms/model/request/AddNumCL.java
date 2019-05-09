package com.elector.Sms.model.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AddNumCL {

    @XmlElement
    private User user;

    @XmlElement
    private ContactList cl;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ContactList getCl() {
        return cl;
    }

    public void setCl(ContactList cl) {
        this.cl = cl;
    }
}
