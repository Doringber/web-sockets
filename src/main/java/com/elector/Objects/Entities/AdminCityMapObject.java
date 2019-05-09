package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class AdminCityMapObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private CityObject cityObject;

    public AdminCityMapObject() {
    }

    public AdminCityMapObject(AdminUserObject adminUserObject, CityObject cityObject) {
        this.adminUserObject = adminUserObject;
        this.cityObject = cityObject;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public CityObject getCityObject() {
        return cityObject;
    }

    public void setCityObject(CityObject cityObject) {
        this.cityObject = cityObject;
    }
}
