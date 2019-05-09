package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class PublicInquiryObject extends BaseEntity {
    private PublicInquiryTopicObject publicInquiryTopicObject;
    private ContactObject contactObject;
    private String subject;
    private String details;
    private boolean open;
    private String comment;
    private Date creationDate;
    private Date closeDate;
    private VoterObject voterObject;

    public PublicInquiryTopicObject getPublicInquiryTopicObject() {
        return publicInquiryTopicObject;
    }

    public void setPublicInquiryTopicObject(PublicInquiryTopicObject publicInquiryTopicObject) {
        this.publicInquiryTopicObject = publicInquiryTopicObject;
    }

    public ContactObject getContactObject() {
        return contactObject;
    }

    public void setContactObject(ContactObject contactObject) {
        this.contactObject = contactObject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }
}
