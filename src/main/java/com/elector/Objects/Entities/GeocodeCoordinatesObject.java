package com.elector.Objects.Entities;


import com.elector.Objects.General.BaseEntity;


/**
 * Created by Sigal on 7/22/2017.
 */
public class GeocodeCoordinatesObject extends BaseEntity{
    private float zoom;
    private double lat;
    private double lng;
    private String address;
    private int type;

    public GeocodeCoordinatesObject () {

    }


    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
