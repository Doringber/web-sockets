package com.elector.Sms.model.request;

import javax.xml.bind.annotation.XmlElement;

public class Destination  {
    String phone;
    String df1;
    String df2;
    String df3;
    String df4;
    String df5;
    String df6;

    public String getPhone() {
        return phone;
    }

    @XmlElement
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDf1() {
        return df1;
    }

    @XmlElement
    public void setDf1(String df1) {
        this.df1 = df1;
    }

    public String getDf2() {
        return df2;
    }

    @XmlElement
    public void setDf2(String df2) {
        this.df2 = df2;
    }

    public String getDf3() {
        return df3;
    }

    @XmlElement
    public void setDf3(String df3) {
        this.df3 = df3;
    }

    public String getDf4() {
        return df4;
    }

    @XmlElement
    public void setDf4(String df4) {
        this.df4 = df4;
    }

    public String getDf5() {
        return df5;
    }

    @XmlElement
    public void setDf5(String df5) {
        this.df5 = df5;
    }

    public String getDf6() {
        return df6;
    }

    @XmlElement
    public void setDf6(String df6) {
        this.df6 = df6;
    }
}
