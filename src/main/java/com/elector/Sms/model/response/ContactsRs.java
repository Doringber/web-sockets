package com.elector.Sms.model.response;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="sms")
public class ContactsRs {

    @XmlElement
    private String message;

    @XmlElement
    private int status;

    @XmlElementWrapper(name="contact_lists")
    @XmlElement(name="contact_list")
    private List<ContactListRs> contacts;

}
