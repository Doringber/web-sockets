package com.elector.Objects.General;

import java.util.Map;

import static com.elector.Utils.Definitions.PARAM_OWN_SUPPORTERS_COUNT;
import static com.elector.Utils.Definitions.PARAM_SUPPORTERS_COUNT;
import static com.elector.Utils.Definitions.PARAM_UNIQUE_SUPPORTERS_COUNT;

public class HeadActivistData {
    private int activistOid;
    private int slavesCount;
    private int ownSupportersCount;
    private int totalSupportersCount;
    private int uniqueSupportersCount;

    public HeadActivistData (int activistOid, int slavesCount, Map<String, String> slavesStats) {
        this.activistOid = activistOid;
        this.slavesCount = slavesCount;
        this.ownSupportersCount = Integer.valueOf(slavesStats.get(PARAM_OWN_SUPPORTERS_COUNT));
        this.totalSupportersCount = Integer.valueOf(slavesStats.get(PARAM_SUPPORTERS_COUNT));
        this.uniqueSupportersCount = Integer.valueOf(slavesStats.get(PARAM_UNIQUE_SUPPORTERS_COUNT));
    }

    public int getActivistOid() {
        return activistOid;
    }

    public void setActivistOid(int activistOid) {
        this.activistOid = activistOid;
    }

    public int getSlavesCount() {
        return slavesCount;
    }

    public void setSlavesCount(int slavesCount) {
        this.slavesCount = slavesCount;
    }

    public int getOwnSupportersCount() {
        return ownSupportersCount;
    }

    public void setOwnSupportersCount(int ownSupportersCount) {
        this.ownSupportersCount = ownSupportersCount;
    }

    public int getTotalSupportersCount() {
        return totalSupportersCount;
    }

    public void setTotalSupportersCount(int totalSupportersCount) {
        this.totalSupportersCount = totalSupportersCount;
    }

    public int getUniqueSupportersCount() {
        return uniqueSupportersCount;
    }

    public void setUniqueSupportersCount(int uniqueSupportersCount) {
        this.uniqueSupportersCount = uniqueSupportersCount;
    }
}
