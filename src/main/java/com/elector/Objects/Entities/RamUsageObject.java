package com.elector.Objects.Entities;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.General.BaseEntity;
import com.elector.Utils.ConfigUtils;

import java.util.Date;

import static com.elector.Utils.Definitions.EMPTY;
import static com.elector.Utils.Definitions.MB;

public class RamUsageObject extends BaseEntity {
    private Date date;
    private long totalMb;
    private long freeMb;
    private long usedMb;

    public RamUsageObject () {
    }

    public RamUsageObject(Date date) {
        this.totalMb = Runtime.getRuntime().totalMemory() / (MB);
        this.freeMb = Runtime.getRuntime().freeMemory() / (MB);
        this.usedMb = (this.totalMb - this.freeMb);
        this.date = date;
    }

    public RamUsageObject(Date date, long totalMb, long freeMb, long usedMb) {
        this.date = date;
        this.totalMb = totalMb;
        this.freeMb = freeMb;
        this.usedMb = usedMb;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTotalMb() {
        return totalMb;
    }

    public void setTotalMb(long totalMb) {
        this.totalMb = totalMb;
    }

    public long getFreeMb() {
        return freeMb;
    }

    public void setFreeMb(long freeMb) {
        this.freeMb = freeMb;
    }

    public long getUsedMb() {
        return usedMb;
    }

    public void setUsedMb(long usedMb) {
        this.usedMb = usedMb;
    }

    public String getAlertMessage (int gb) {
        String text = EMPTY;
        if (this.usedMb > (gb * 1024)) {
            text = String.format("%s: , Total: %s, Free: %s, Used: %s", ConfigUtils.getConfig(ConfigEnum.prod, false) ? "PROD" : "DEV",this.totalMb, this.freeMb, this.usedMb);
        }
        return text;
    }
}
