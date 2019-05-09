package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

import static com.elector.Utils.Definitions.*;

/**
 * Created by Sigal on 7/22/2017.
 */
public class CampaignObject extends BaseEntity {
    private int oid;
    private String name;
    private Boolean active;
    private int clientsCounter;
    private Date date;
    private GeocodeCoordinatesObject coordinatesObject;
    private Date endDate;
    private boolean votersBook;
    private String mainCity;
    private Integer type;

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getClientsCounter() {
        return clientsCounter;
    }

    public void setClientsCounter(int clientsCounter) {
        this.clientsCounter = clientsCounter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GeocodeCoordinatesObject getCoordinatesObject() {
        return coordinatesObject;
    }

    public void setCoordinatesObject(GeocodeCoordinatesObject coordinatesObject) {
        this.coordinatesObject = coordinatesObject;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isVotersBook() {
        return votersBook;
    }

    public void setVotersBook(boolean votersBook) {
        this.votersBook = votersBook;
    }

    public void incrementClientsCounter () {
        this.clientsCounter++;
    }

    public String getMainCity() {
        return mainCity;
    }

    public void setMainCity(String mainCity) {
        this.mainCity = mainCity;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSubClassName () {
        String className = EMPTY;
        switch (this.type) {
            case PARAM_CAMPAIGN_TYPE_MUNICIPAL:
            case PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION:
                className = "CampaignVoterObject";
                break;
            case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LABOR:
                className = "IsraelLaborPartyVoterObject";
                break;
            case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LIKUD:
                className = "LikudPartyVoterObject";
                break;
        }
        return className;
    }
}
