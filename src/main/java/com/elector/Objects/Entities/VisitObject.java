package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class VisitObject extends BaseEntity {
    private Date date;
    private String ip;
    private String referrer;

    public VisitObject(){
    }

    public VisitObject(HttpServletRequest request){
        this.date = new Date();
        this.ip = Utils.getIpFromRequest(request);
        this.referrer = request.getHeader("referer");
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
}
