package com.elector.Objects.Entities;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.General.BaseEntity;
import com.elector.Utils.ConfigUtils;

public class LandingPageDataObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private String nameInUrl;
    private int type;
    private String title;
    private String text;

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public String getNameInUrl() {
        return nameInUrl;
    }

    public void setNameInUrl(String nameInUrl) {
        this.nameInUrl = nameInUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl () {
        return String.format("%s/%s", ConfigUtils.getConfig(ConfigEnum.inquiries_static_files_path, "http://localhost:8794/contact/"), this.nameInUrl);
    }
}
