package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class CampaignCityMapObject extends BaseEntity {
    private CampaignObject campaignObject;
    private CityObject cityObject;

    public CampaignCityMapObject () {
    }

    public CampaignCityMapObject(CampaignObject campaignObject, CityObject cityObject) {
        this.campaignObject = campaignObject;
        this.cityObject = cityObject;
    }

    public CampaignObject getCampaignObject() {
        return campaignObject;
    }

    public void setCampaignObject(CampaignObject campaignObject) {
        this.campaignObject = campaignObject;
    }

    public CityObject getCityObject() {
        return cityObject;
    }

    public void setCityObject(CityObject cityObject) {
        this.cityObject = cityObject;
    }
}
