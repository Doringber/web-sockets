package com.elector.Sms.model.request;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "sms")
@XmlAccessorType(XmlAccessType.FIELD)
public class SingleSms {

    @XmlElement
    private User user;

    @XmlElement
    private String source;

    @XmlElementWrapper(name="destinations")
    @XmlElement(name="phone")
    private List<String> destinations = new ArrayList<>();

    @XmlElement
    private String message;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
