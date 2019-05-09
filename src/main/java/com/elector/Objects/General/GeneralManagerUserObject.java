package com.elector.Objects.General;

import com.elector.Objects.Entities.AdminUserObject;
import com.elector.Objects.Entities.CampaignObject;
import com.elector.Utils.Utils;

import static com.elector.Utils.Definitions.*;

public class GeneralManagerUserObject extends BaseUser {
    protected String smsSenderName;
    protected boolean sendSms;
    protected boolean simulation;
    protected String callScenario;
    protected boolean sendSupporterNotVerifiedSms;
    protected boolean sendSupporterNotVerifiedNotification;
    protected boolean showActivistsRank;
    protected boolean sendSupporterVerifiedSms;
    protected boolean sendSupporterVerifiedNotification;
    protected boolean allowCallersToChangeVotingStatus;
    protected boolean allowAddingSupportersWithoutPhoneNumbers;
    protected String electionDayCallScenario;
    protected int electionDayCallsSortType;
    protected boolean loadVotersToFrontend;

    public String getSmsSenderName() {
        return smsSenderName;
    }

    public void setSmsSenderName(String smsSenderName) {
        this.smsSenderName = smsSenderName;
    }

    public boolean isSendSms() {
        return sendSms;
    }

    public void setSendSms(boolean sendSms) {
        this.sendSms = sendSms;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public String getCallScenario() {
        return callScenario;
    }

    public void setCallScenario(String callScenario) {
        this.callScenario = callScenario;
    }

    public boolean isSendSupporterNotVerifiedSms() {
        return sendSupporterNotVerifiedSms;
    }

    public void setSendSupporterNotVerifiedSms(boolean sendSupporterNotVerifiedSms) {
        this.sendSupporterNotVerifiedSms = sendSupporterNotVerifiedSms;
    }

    public boolean isSendSupporterNotVerifiedNotification() {
        return sendSupporterNotVerifiedNotification;
    }

    public void setSendSupporterNotVerifiedNotification(boolean sendSupporterNotVerifiedNotification) {
        this.sendSupporterNotVerifiedNotification = sendSupporterNotVerifiedNotification;
    }

    public boolean isShowActivistsRank() {
        return showActivistsRank;
    }

    public void setShowActivistsRank(boolean showActivistsRank) {
        this.showActivistsRank = showActivistsRank;
    }

    public boolean isSendSupporterVerifiedSms() {
        return sendSupporterVerifiedSms;
    }

    public void setSendSupporterVerifiedSms(boolean sendSupporterVerifiedSms) {
        this.sendSupporterVerifiedSms = sendSupporterVerifiedSms;
    }

    public boolean isSendSupporterVerifiedNotification() {
        return sendSupporterVerifiedNotification;
    }

    public void setSendSupporterVerifiedNotification(boolean sendSupporterVerifiedNotification) {
        this.sendSupporterVerifiedNotification = sendSupporterVerifiedNotification;
    }

    public boolean isAllowCallersToChangeVotingStatus() {
        return allowCallersToChangeVotingStatus;
    }

    public void setAllowCallersToChangeVotingStatus(boolean allowCallersToChangeVotingStatus) {
        this.allowCallersToChangeVotingStatus = allowCallersToChangeVotingStatus;
    }

    public boolean isAllowAddingSupportersWithoutPhoneNumbers() {
        return allowAddingSupportersWithoutPhoneNumbers;
    }

    public void setAllowAddingSupportersWithoutPhoneNumbers(boolean allowAddingSupportersWithoutPhoneNumbers) {
        this.allowAddingSupportersWithoutPhoneNumbers = allowAddingSupportersWithoutPhoneNumbers;
    }

    public String getElectionDayCallScenario() {
        return electionDayCallScenario;
    }

    public void setElectionDayCallScenario(String electionDayCallScenario) {
        this.electionDayCallScenario = electionDayCallScenario;
    }

    public int getElectionDayCallsSortType() {
        return electionDayCallsSortType;
    }

    public void setElectionDayCallsSortType(int electionDayCallsSortType) {
        this.electionDayCallsSortType = electionDayCallsSortType;
    }

    public boolean isLoadVotersToFrontend() {
        return loadVotersToFrontend;
    }

    public void setLoadVotersToFrontend(boolean loadVotersToFrontend) {
        this.loadVotersToFrontend = loadVotersToFrontend;
    }


    public String getUICallScenario () {
        return Utils.hasText(this.callScenario) ? this.callScenario.replace(BREAK, UI_LINE_BREAK_REPLACEMENT) : EMPTY;
    }

    public String getUIElectionDayCallScenario () {
        return Utils.hasText(this.electionDayCallScenario) ? this.electionDayCallScenario.replace(BREAK, UI_LINE_BREAK_REPLACEMENT) : EMPTY;
    }

    public CampaignObject getCampaign () {
        AdminUserObject adminUserObject = null;
        if (this instanceof AdminUserObject) {
            adminUserObject = (AdminUserObject) this;
        } else {
            adminUserObject = getAdmin();
        }
        return adminUserObject.getCampaignObject();
    }



}
