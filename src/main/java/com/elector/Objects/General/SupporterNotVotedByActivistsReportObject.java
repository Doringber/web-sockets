package com.elector.Objects.General;

import com.elector.Objects.Entities.ActivistObject;
import com.elector.Objects.Entities.VoterObject;

/**
 * Created by Shai on 2/19/2018.
 */

public class SupporterNotVotedByActivistsReportObject extends ReportObject{
    private String activistName;
    private String activistPhone;
    private String supporterId;
    private String supporterFullName;
    private String supporterPhone;

    public SupporterNotVotedByActivistsReportObject (VoterObject supporter, ActivistObject activist) {
        this.activistName = activist.getFullName();
        this.activistPhone = activist.getPhone();
        this.supporterId = supporter.getVoterId();
        this.supporterFullName = supporter.getFullName();
        this.supporterPhone = supporter.getPhone();
    }

    public String getActivistName() {
        return activistName;
    }

    public void setActivistName(String activistName) {
        this.activistName = activistName;
    }

    public String getActivistPhone() {
        return activistPhone;
    }

    public void setActivistPhone(String activistPhone) {
        this.activistPhone = activistPhone;
    }

    public String getSupporterId() {
        return supporterId;
    }

    public void setSupporterId(String supporterId) {
        this.supporterId = supporterId;
    }

    public String getSupporterFullName() {
        return supporterFullName;
    }

    public void setSupporterFullName(String supporterFullName) {
        this.supporterFullName = supporterFullName;
    }

    public String getSupporterPhone() {
        return supporterPhone;
    }

    public void setSupporterPhone(String supporterPhone) {
        this.supporterPhone = supporterPhone;
    }
}
