package com.elector.Objects.General;


import com.elector.Objects.Entities.*;
import com.elector.Utils.Utils;

import java.util.Date;

import static com.elector.Utils.Definitions.*;

public class ObserverLogItem {
    private Date date;
    private int type;
    private String text;
    private Integer voterOid;
    private Integer observerOid;

    public ObserverLogItem(Date date, int type) {
        this.date = date;
        this.type = type;
    }

    public ObserverLogItem(ElectionDayAlert alert) {
        this.date = alert.getTime();
        this.type = PARAM_OBSERVER_SENT_MESSAGE;
        this.text = alert.getText();
        this.observerOid = alert.getSenderOid();
    }


    public ObserverLogItem(ObserverCheckInObject observerCheckInObject) {
        this.date = observerCheckInObject.getDate();
        this.type = observerCheckInObject.isCheckIn() ? PARAM_OBSERVER_LOG_TYPE_CHECK_IN : PARAM_OBSERVER_LOG_TYPE_CHECK_OUT;
        this.observerOid = observerCheckInObject.getObserverObject().getOid();

    }


    public ObserverLogItem (VotingStatusChangeObject votingStatusChangeObject) {
        VoterObject voterObject = votingStatusChangeObject.getVoterObject();
        this.date = votingStatusChangeObject.getDate();
        this.type = votingStatusChangeObject.isNewStatus() ? PARAM_OBSERVER_LOG_TYPE_VOTING_STATUS_CHANGE_VOTED : PARAM_OBSERVER_LOG_TYPE_VOTING_STATUS_CHANGE_UNVOTED;
        try {
            Integer language = voterObject.getAdminUserObject().getLanguage();
            switch (voterObject.getCampaignType()) {
                case PARAM_CAMPAIGN_TYPE_MUNICIPAL:
                case PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION:
                    this.text =
                            String.format(
                                    "%s: %s, %s: %s, %s: %s",
                                    Utils.getTranslation("id", language), voterObject.getVoterId(),
                                    Utils.getTranslation("voter.number", language), ((CampaignVoterObject)voterObject.getBaseCampaignVoterObject()).getVoterNumber(),
                                    Utils.getTranslation("name", language), voterObject.getFullName()
                            );
                    break;
                case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LABOR:
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.voterOid = votingStatusChangeObject.getVoterObject().getOid();
        this.observerOid = votingStatusChangeObject.getInitiatorOid();

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getVoterOid() {
        return voterOid;
    }

    public void setVoterOid(Integer voterOid) {
        this.voterOid = voterOid;
    }

    public Integer getObserverOid() {
        return observerOid;
    }

    public void setObserverOid(Integer observerOid) {
        this.observerOid = observerOid;
    }
}
