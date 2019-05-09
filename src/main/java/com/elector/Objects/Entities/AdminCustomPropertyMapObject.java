package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class AdminCustomPropertyMapObject extends BaseEntity{
    private AdminUserObject adminUserObject;
    private CustomPropertyObject customPropertyObject;


    public AdminCustomPropertyMapObject() {
    }

    public AdminCustomPropertyMapObject(AdminUserObject adminUserObject, CustomPropertyObject customPropertyObject) {
        this.adminUserObject = adminUserObject;
        this.customPropertyObject = customPropertyObject;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public CustomPropertyObject getCustomPropertyObject() {
        return customPropertyObject;
    }

    public void setCustomPropertyObject(CustomPropertyObject customPropertyObject) {
        this.customPropertyObject = customPropertyObject;
    }
}
