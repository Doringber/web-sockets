package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;
import java.util.List;

import static com.elector.Utils.Definitions.*;

public class CandidateDataDailyObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private Date date;
    private int supportingCount;
    private int notSupportingCount;
    private int unknownSupportCount;
    private int unverifiedSupportingCount;
    private GroupManagerObject groupManagerObject;

    public CandidateDataDailyObject () {

    }

    public CandidateDataDailyObject(AdminUserObject adminUserObject, Date date, int supportingCount, int notSupportingCount, int unknownSupportCount, int unverifiedSupportingCount) {
        this.adminUserObject = adminUserObject;
        this.date = date;
        this.supportingCount = supportingCount;
        this.notSupportingCount = notSupportingCount;
        this.unknownSupportCount = unknownSupportCount;
        this.unverifiedSupportingCount = unverifiedSupportingCount;
    }

    public CandidateDataDailyObject(AdminUserObject adminUserObject, GroupManagerObject groupManagerObject, Date date, int supportingCount, int notSupportingCount, int unknownSupportCount, int unverifiedSupportingCount) {
        this.adminUserObject = adminUserObject;
        this.groupManagerObject = groupManagerObject;
        this.date = date;
        this.supportingCount = supportingCount;
        this.notSupportingCount = notSupportingCount;
        this.unknownSupportCount = unknownSupportCount;
        this.unverifiedSupportingCount = unverifiedSupportingCount;
    }

    public CandidateDataDailyObject (Date date, List<DynamicGroupObject> dynamicGroupObjects, GroupManagerObject groupManagerObject) {
        this.date = date;
        this.groupManagerObject = groupManagerObject;
        this.adminUserObject = groupManagerObject.getAdminUserObject();
        for (DynamicGroupObject dynamicGroupObject : dynamicGroupObjects ) {
            incrementCount(PARAM_SUPPORT_STATUS_SUPPORTING, dynamicGroupObject.getTotalSupporters());
            incrementCount(PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING, dynamicGroupObject.getTotalUnverifiedSupporters());
            incrementCount(PARAM_SUPPORT_STATUS_UNKNOWN, dynamicGroupObject.getTotalUnknownSupportStatus());
            incrementCount(PARAM_SUPPORT_STATUS_NOT_SUPPORTING, dynamicGroupObject.getTotalNotSupporting());
        }
    }



    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSupportingCount() {
        return supportingCount;
    }

    public void setSupportingCount(int supportingCount) {
        this.supportingCount = supportingCount;
    }

    public int getNotSupportingCount() {
        return notSupportingCount;
    }

    public void setNotSupportingCount(int notSupportingCount) {
        this.notSupportingCount = notSupportingCount;
    }

    public int getUnknownSupportCount() {
        return unknownSupportCount;
    }

    public void setUnknownSupportCount(int unknownSupportCount) {
        this.unknownSupportCount = unknownSupportCount;
    }

    public int getUnverifiedSupportingCount() {
        return unverifiedSupportingCount;
    }

    public void setUnverifiedSupportingCount(int unverifiedSupportingCount) {
        this.unverifiedSupportingCount = unverifiedSupportingCount;
    }

    public GroupManagerObject getGroupManagerObject() {
        return groupManagerObject;
    }

    public void setGroupManagerObject(GroupManagerObject groupManagerObject) {
        this.groupManagerObject = groupManagerObject;
    }

    private void incrementCount (int supportStatus, int count) {
        switch (supportStatus) {
            case PARAM_SUPPORT_STATUS_SUPPORTING:
                this.supportingCount += count;
                break;
            case PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING:
                this.unverifiedSupportingCount += count;
                break;
            case PARAM_SUPPORT_STATUS_UNKNOWN:
                this.unknownSupportCount += count;
                break;
            case PARAM_SUPPORT_STATUS_NOT_SUPPORTING:
                this.notSupportingCount += count;
                break;
        }
    }
}
