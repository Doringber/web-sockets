package com.elector.Objects.General;

import com.elector.Objects.Entities.*;

import static com.elector.Utils.Definitions.PARAM_SUPPORT_STATUS_NOT_SUPPORTING;
import static com.elector.Utils.Definitions.PARAM_SUPPORT_STATUS_SUPPORTING;

public class CallerStatisticsObject {
    private CallerObject callerObject;
    private int totalAnsweredCalls;
    private int totalUnansweredCalls;
    private int totalChangedToSupporters;
    private int totalChangedToNotSupporting;
    private int totalUnchanged;
    private int totalBillboardRequests;
    private int totalWantsToVolunteer;
    private int totalWantsToDeliverSupportersList;

    public CallerStatisticsObject(CallerObject callerObject) {
        this.callerObject = callerObject;
    }

    public CallerObject getCallerObject() {
        return callerObject;
    }

    public void setCallerObject(CallerObject callerObject) {
        this.callerObject = callerObject;
    }

    public int getTotalAnsweredCalls() {
        return totalAnsweredCalls;
    }

    public void setTotalAnsweredCalls(int totalAnsweredCalls) {
        this.totalAnsweredCalls = totalAnsweredCalls;
    }

    public int getTotalUnansweredCalls() {
        return totalUnansweredCalls;
    }

    public void setTotalUnansweredCalls(int totalUnansweredCalls) {
        this.totalUnansweredCalls = totalUnansweredCalls;
    }

    public int getTotalChangedToSupporters() {
        return totalChangedToSupporters;
    }

    public void setTotalChangedToSupporters(int totalChangedToSupporters) {
        this.totalChangedToSupporters = totalChangedToSupporters;
    }

    public int getTotalChangedToNotSupporting() {
        return totalChangedToNotSupporting;
    }

    public void setTotalChangedToNotSupporting(int totalChangedToNotSupporting) {
        this.totalChangedToNotSupporting = totalChangedToNotSupporting;
    }

    public int getTotalUnchanged() {
        return totalUnchanged;
    }

    public void setTotalUnchanged(int totalUnchanged) {
        this.totalUnchanged = totalUnchanged;
    }

    public int getTotalBillboardRequests() {
        return totalBillboardRequests;
    }

    public void setTotalBillboardRequests(int totalBillboardRequests) {
        this.totalBillboardRequests = totalBillboardRequests;
    }

    public int getTotalWantsToVolunteer() {
        return totalWantsToVolunteer;
    }

    public void setTotalWantsToVolunteer(int totalWantsToVolunteer) {
        this.totalWantsToVolunteer = totalWantsToVolunteer;
    }

    public int getTotalWantsToDeliverSupportersList() {
        return totalWantsToDeliverSupportersList;
    }

    public void setTotalWantsToDeliverSupportersList(int totalWantsToDeliverSupportersList) {
        this.totalWantsToDeliverSupportersList = totalWantsToDeliverSupportersList;
    }

    public int getTotalCalls () {
        return this.totalAnsweredCalls + this.totalUnansweredCalls;
    }

    public int getPercentChangedToSupporters() {
        return (int)((this.totalChangedToSupporters / (double)this.totalAnsweredCalls) * 100);
    }

    public int getPercentAnsweredCalls() {
        return (int)((this.totalAnsweredCalls / (double)(this.totalAnsweredCalls + this.totalUnansweredCalls)) * 100);
    }


    public void updateStatistics (VoterCallObject voterCallObject) {
        if (voterCallObject.isAnswered()) {
            this.totalAnsweredCalls++;
        } else  {
            this.totalUnansweredCalls++;
        }
        if (voterCallObject.getPreviousStatus() == voterCallObject.getNewStatus()) {
            this.totalUnchanged++;
        } else {
            switch (voterCallObject.getNewStatus()) {
                case PARAM_SUPPORT_STATUS_SUPPORTING:
                    this.totalChangedToSupporters++;
                    break;
                case PARAM_SUPPORT_STATUS_NOT_SUPPORTING:
                    this.totalChangedToNotSupporting++;
                    break;
            }
        }
    }

    public void updateTask (SupporterActionObject task) {
        if (task instanceof SupporterBillboardRequestObject) {
            this.totalBillboardRequests++;
        } else if (task instanceof SupporterVolunteerObject) {
            this.totalWantsToVolunteer++;
        } else if (task instanceof SupporterSupportersListDelivery) {
            this.totalWantsToDeliverSupportersList++;
        }
    }


    public void updateStatistics (VoterElectionDayCallObject voterCallObject) {
        if (voterCallObject.isAnswered()) {
            this.totalAnsweredCalls++;
        } else  {
            this.totalUnansweredCalls++;
        }
    }




}
