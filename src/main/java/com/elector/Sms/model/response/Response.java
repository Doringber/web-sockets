//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.17 at 09:30:52 PM KRAT 
//


package com.elector.Sms.model.response;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="sms")
public class Response {

    private int status;
    @XmlElement(required = true)
    private String message;

    @XmlElementWrapper(name="identifiers")
    @XmlElement(name="identifier")
    private List<Integer> identifiers;

    @XmlElementWrapper(name="errors")
    @XmlElement(name="error")
    private List<String> errors;

    @XmlElement
    private String shipmentId;

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public List<Integer> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Integer> identifiers) {
        this.identifiers = identifiers;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", identifiers=" + identifiers +
                ", errors=" + errors +
                '}';
    }
}
