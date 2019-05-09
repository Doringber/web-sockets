package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

public class ActivistMasterSlaveMapObject extends BaseEntity {
    private ActivistObject master;
    private ActivistObject slave;

    public ActivistMasterSlaveMapObject() {
    }

    public ActivistMasterSlaveMapObject(ActivistObject master, ActivistObject slave) {
        this.master = master;
        this.slave = slave;
    }

    public ActivistObject getMaster() {
        return master;
    }

    public void setMaster(ActivistObject master) {
        this.master = master;
    }

    public ActivistObject getSlave() {
        return slave;
    }

    public void setSlave(ActivistObject slave) {
        this.slave = slave;
    }
}
