package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class AliasObject extends BaseEntity{
    private String source;
    private String aliases;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }
}
