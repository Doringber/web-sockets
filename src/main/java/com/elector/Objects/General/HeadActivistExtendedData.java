package com.elector.Objects.General;

import com.elector.Objects.Entities.ActivistObject;

import java.util.Map;
import java.util.Set;

public class HeadActivistExtendedData extends HeadActivistData {

    private Set<ActivistObject> slaves;

    public HeadActivistExtendedData(int activistOid, int slavesCount, Map<String, String> slavesStats, Set<ActivistObject> slaves) {
        super(activistOid, slavesCount, slavesStats);
        this.slaves = slaves;
    }

    public Set<ActivistObject> getSlaves() {
        return slaves;
    }

    public void setSlaves(Set<ActivistObject> slaves) {
        this.slaves = slaves;
    }
}
