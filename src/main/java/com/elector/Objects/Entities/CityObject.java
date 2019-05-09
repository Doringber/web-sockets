package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Utils.Utils;

import java.util.List;

public class CityObject extends BaseEntity {
    private String name;
    private String symbol;
    private String aliases;

    public CityObject () {
    }

    public CityObject (int oid) {
        this.oid = oid;
    }

    public CityObject(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public CityObject(String name, List<String> aliases) {
        this.name = name;
        this.aliases = Utils.stringListToText(aliases);
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

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }
}
