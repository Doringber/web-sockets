package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.elector.Utils.Definitions.*;

public class ExcelUploadObject extends BaseEntity {
    private Date date;
    private int userType;
    private int userOid;
    private int action;
    private int totalIds;
    private int nonExistingIds;
    private String extraInfo;
    private AdminUserObject adminUserObject;
    private boolean reloadVoters;
    private List<String> nonExistingIdsList = new ArrayList<>();


    public ExcelUploadObject() {
    }

    public ExcelUploadObject(int userType, int userOid, int action, int totalIds, AdminUserObject adminUserObject) {
        this.date = new Date();
        this.userType = userType;
        this.userOid = userOid;
        this.action = action;
        this.totalIds = totalIds;
        this.adminUserObject = adminUserObject;
    }

    public ExcelUploadObject(int userType, int userOid, int action, int totalIds, int nonExistingIds, String extraInfo, boolean reloadVoters) {
        this.date = new Date();
        this.userType = userType;
        this.userOid = userOid;
        this.action = action;
        this.totalIds = totalIds;
        this.nonExistingIds = nonExistingIds;
        this.extraInfo = extraInfo;
        this.reloadVoters = reloadVoters;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserOid() {
        return userOid;
    }

    public void setUserOid(int userOid) {
        this.userOid = userOid;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getTotalIds() {
        return totalIds;
    }

    public void setTotalIds(int totalIds) {
        this.totalIds = totalIds;
    }

    public int getNonExistingIds() {
        return nonExistingIds;
    }

    public void setNonExistingIds(int nonExistingIds) {
        this.nonExistingIds = nonExistingIds;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public boolean isReloadVoters() {
        return reloadVoters;
    }

    public void setReloadVoters(boolean reloadVoters) {
        this.reloadVoters = reloadVoters;
    }

    public List<String> getNonExistingIdsList() {
        return nonExistingIdsList;
    }

    public void setNonExistingIdsList (List<String> nonExistingIdsList) {
        this.nonExistingIdsList = nonExistingIdsList;
        this.nonExistingIds = nonExistingIdsList.size();
    }

    public int getExistingIds () {
        return this.totalIds - this.nonExistingIds;
    }

    public boolean needToReloadVoters () {
        boolean reload = false;
        switch (this.action) {
            case VOTERS_IDS_ACTION_MAKE_ALL_SUPPORTERS:
            case VOTERS_IDS_ACTION_MAKE_ALL_UNVERIFIED_SUPPORTERS:
            case VOTERS_IDS_ACTION_MAKE_ALL_UNKNOWN_SUPPORT_STATUS:
            case VOTERS_IDS_ACTION_MAKE_ALL_NOT_SUPPORTING:
            case VOTERS_IDS_ACTION_ADD_VOTERS_TO_ACTIVIST_AND_UPGRADE_SUPPORT_STATUS:
            case VOTERS_IDS_ACTION_MARK_VOTERS_AS_VOTED:
                reload = true;
                break;
        }
        return reload;
    }

}
