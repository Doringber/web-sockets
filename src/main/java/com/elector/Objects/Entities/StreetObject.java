package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class StreetObject extends BaseEntity {
    private String name;
    private String symbol;
    private CityObject city;

    public StreetObject () {
    }

    public StreetObject(String name, String symbol, CityObject city) {
        this.name = name;
        this.symbol = symbol;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public CityObject getCity() {
        return city;
    }

    public void setCity(CityObject city) {
        this.city = city;
    }
}
