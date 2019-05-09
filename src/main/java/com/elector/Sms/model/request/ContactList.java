package com.elector.Sms.model.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

public class ContactList {

    private Integer id;
    private String name;
    private List<Destination> destinatons = new ArrayList<>();

    @XmlElementWrapper(name="destinations")
    @XmlElement(name="destination")
    public List<Destination> getDestinatons() {
        return destinatons;
    }

    public void setDestinatons(List<Destination> destinatons) {
        this.destinatons = destinatons;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    @XmlElement
    public void setId(Integer id) {
        this.id = id;
    }
}
