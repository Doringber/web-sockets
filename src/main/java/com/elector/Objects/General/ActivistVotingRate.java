package com.elector.Objects.General;

public class ActivistVotingRate {
    private int activistOid;
    private int supportersCount;
    private int votedCount;
    private int votedPercent;

    public ActivistVotingRate(int activistOid, int supportersCount, int votedCount) {
        this.activistOid = activistOid;
        this.supportersCount = supportersCount;
        this.votedCount = votedCount;
        this.votedPercent = (int)(((double)votedCount / (double)supportersCount) * 100);
    }

    public int getActivistOid() {
        return activistOid;
    }

    public void setActivistOid(int activistOid) {
        this.activistOid = activistOid;
    }

    public int getSupportersCount() {
        return supportersCount;
    }

    public void setSupportersCount(int supportersCount) {
        this.supportersCount = supportersCount;
    }

    public int getVotedCount() {
        return votedCount;
    }

    public void setVotedCount(int votedCount) {
        this.votedCount = votedCount;
    }

    public int getVotedPercent() {
        return votedPercent;
    }

    public void setVotedPercent(int votedPercent) {
        this.votedPercent = votedPercent;
    }
}
