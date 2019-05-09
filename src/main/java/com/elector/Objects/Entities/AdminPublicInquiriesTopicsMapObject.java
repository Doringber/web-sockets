package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class AdminPublicInquiriesTopicsMapObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private PublicInquiryTopicObject publicInquiryTopicObject;

    public AdminPublicInquiriesTopicsMapObject() {
    }

    public AdminPublicInquiriesTopicsMapObject(AdminUserObject adminUserObject, PublicInquiryTopicObject publicInquiryTopicObject) {
        this.adminUserObject = adminUserObject;
        this.publicInquiryTopicObject = publicInquiryTopicObject;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public PublicInquiryTopicObject getPublicInquiryTopicObject() {
        return publicInquiryTopicObject;
    }

    public void setPublicInquiryTopicObject(PublicInquiryTopicObject publicInquiryTopicObject) {
        this.publicInquiryTopicObject = publicInquiryTopicObject;
    }
}
