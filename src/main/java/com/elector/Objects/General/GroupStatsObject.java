package com.elector.Objects.General;

public class GroupStatsObject {
    private int totalCallsMade;
    private int totalAnsweredCalls;
    private String lastCallTime;
    private int totalVotersThatGotCall;
    private int groupSize;

    public GroupStatsObject () {
    }

    public int getTotalCallsMade() {
        return totalCallsMade;
    }

    public void setTotalCallsMade(int totalCallsMade) {
        this.totalCallsMade = totalCallsMade;
    }

    public int getTotalAnsweredCalls() {
        return totalAnsweredCalls;
    }

    public void setTotalAnsweredCalls(int totalAnsweredCalls) {
        this.totalAnsweredCalls = totalAnsweredCalls;
    }

    public String getLastCallTime() {
        return lastCallTime;
    }

    public void setLastCallTime(String lastCallTime) {
        this.lastCallTime = lastCallTime;
    }

    public int getTotalVotersThatGotCall() {
        return totalVotersThatGotCall;
    }

    public void setTotalVotersThatGotCall(int totalVotersThatGotCall) {
        this.totalVotersThatGotCall = totalVotersThatGotCall;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public int calculateTotalVotersThatDidNotGetCall () {
        return this.groupSize - this.totalVotersThatGotCall;
    }


}
