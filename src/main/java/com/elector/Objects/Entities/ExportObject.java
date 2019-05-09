package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

/**
 * Created by Shai on 8/8/2018.
 */

public class ExportObject extends BaseEntity{
    private AdminUserObject adminUserObject;
    private int reportType;
    private Date date;

    public ExportObject() {
    }

    public ExportObject(AdminUserObject adminUserObject, int reportType, Date date) {
        this.adminUserObject = adminUserObject;
        this.reportType = reportType;
        this.date = date;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
