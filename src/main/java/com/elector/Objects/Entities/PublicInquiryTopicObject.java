package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class PublicInquiryTopicObject extends BaseEntity {
    private String description;

    public PublicInquiryTopicObject () {
    }

    public PublicInquiryTopicObject (int oid) {
        this.oid = oid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
