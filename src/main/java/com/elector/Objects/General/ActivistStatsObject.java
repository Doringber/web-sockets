package com.elector.Objects.General;

import com.elector.Objects.Entities.VoterObject;

import java.util.List;

import static com.elector.Utils.Definitions.*;

public class ActivistStatsObject {
    private int totalMappingCount;
    private int supportersCount;
    private int unverifiedSupportersCount;
    private int unknownSupportStatusCount;
    private int notSupportingCount;
    private String supportersPercent;
    private String unverifiedSupportersPercent;
    private String unknownSupportStatusPercent;
    private String notSupportingPercent;

    public ActivistStatsObject (List<VoterObject> voterObjectList) {
        for (VoterObject voterObject : voterObjectList) {
            switch (voterObject.getSupportStatus()) {
                case PARAM_SUPPORT_STATUS_SUPPORTING:
                    this.supportersCount++;
                    break;
                case PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING:
                    this.unverifiedSupportersCount++;
                    break;
                case PARAM_SUPPORT_STATUS_UNKNOWN:
                    this.unknownSupportStatusCount++;
                    break;
                case PARAM_SUPPORT_STATUS_NOT_SUPPORTING:
                    this.notSupportingCount++;
                    break;
            }
        }
        this.totalMappingCount = voterObjectList.size();
        this.supportersPercent = String.format("%.2f", this.supportersCount / (float)voterObjectList.size() * 100);
        this.unverifiedSupportersPercent = String.format("%.2f", this.unverifiedSupportersCount / (float)voterObjectList.size() * 100);
        this.unknownSupportStatusPercent = String.format("%.2f", this.unknownSupportStatusCount / (float)voterObjectList.size() * 100);
        this.notSupportingPercent = String.format("%.2f", this.notSupportingCount / (float)voterObjectList.size() * 100);
    }

    public int getSupportersCount() {
        return supportersCount;
    }

    public void setSupportersCount(int supportersCount) {
        this.supportersCount = supportersCount;
    }

    public int getUnverifiedSupportersCount() {
        return unverifiedSupportersCount;
    }

    public void setUnverifiedSupportersCount(int unverifiedSupportersCount) {
        this.unverifiedSupportersCount = unverifiedSupportersCount;
    }

    public int getUnknownSupportStatusCount() {
        return unknownSupportStatusCount;
    }

    public void setUnknownSupportStatusCount(int unknownSupportStatusCount) {
        this.unknownSupportStatusCount = unknownSupportStatusCount;
    }

    public int getNotSupportingCount() {
        return notSupportingCount;
    }

    public void setNotSupportingCount(int notSupportingCount) {
        this.notSupportingCount = notSupportingCount;
    }

    public String getSupportersPercent() {
        return supportersPercent;
    }

    public void setSupportersPercent(String supportersPercent) {
        this.supportersPercent = supportersPercent;
    }

    public String getUnverifiedSupportersPercent() {
        return unverifiedSupportersPercent;
    }

    public void setUnverifiedSupportersPercent(String unverifiedSupportersPercent) {
        this.unverifiedSupportersPercent = unverifiedSupportersPercent;
    }

    public String getUnknownSupportStatusPercent() {
        return unknownSupportStatusPercent;
    }

    public void setUnknownSupportStatusPercent(String unknownSupportStatusPercent) {
        this.unknownSupportStatusPercent = unknownSupportStatusPercent;
    }

    public String getNotSupportingPercent() {
        return notSupportingPercent;
    }

    public void setNotSupportingPercent(String notSupportingPercent) {
        this.notSupportingPercent = notSupportingPercent;
    }

    public int getTotalMappingCount() {
        return totalMappingCount;
    }

    public void setTotalMappingCount(int totalMappingCount) {
        this.totalMappingCount = totalMappingCount;
    }
}
