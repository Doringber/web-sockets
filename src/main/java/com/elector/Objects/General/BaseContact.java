package com.elector.Objects.General;

import com.elector.Interfaces.Ownable;
import com.elector.Objects.Entities.*;
import com.elector.Utils.Utils;

import static com.elector.Utils.Definitions.*;

public class BaseContact extends BaseEntity implements Ownable {
    protected String phone;
    protected String email;
    protected String firstName;
    protected String lastName;
    protected AdminUserObject adminUserObject;
    protected String homePhone;
    protected String extraPhone;
    protected GroupManagerObject groupManagerObject;

    public String getPhone() {
        return Utils.formatPhoneNumber(this.phone);
    }

    public void setPhone(String phone) {
        this.phone = Utils.formatPhoneNumber(phone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getExtraPhone() {
        return extraPhone;
    }

    public void setExtraPhone(String extraPhone) {
        this.extraPhone = extraPhone;
    }

    public boolean canSendSms() {
        return this.phone != null;
    }

    public int getGroupType(boolean add) {
        int groupType = GROUP_TYPE_NONE;
        if (this instanceof ActivistObject) {
            groupType = GROUP_TYPE_ACTIVISTS;
        } else if (this instanceof CallerObject) {
            groupType = GROUP_TYPE_CALLERS;
        } else if (this instanceof ObserverObject) {
            groupType = GROUP_TYPE_OBSERVERS;
        } else if (this instanceof DriverObject) {
            groupType = GROUP_TYPE_DRIVERS;
        } else if (this instanceof VoterObject) {
            groupType = GROUP_TYPE_SUPPORTERS;
        }
        return groupType;
    }

    public boolean hasValidPhoneNumber() {
        return Utils.isValidCellPhone(this.phone) || Utils.isValidPhone(this.homePhone) || Utils.isValidPhone(this.extraPhone);
    }

    public int getUserType() {
        int userType = -1;
        if (this.oid == SUPER_ADMIN_OID) {
            userType = PARAM_ADMIN_USER_TYPE_SUPER;
        } else {
            if (this instanceof ActivistObject) {
                userType = PARAM_ADMIN_USER_TYPE_ACTIVIST;
            } else if (this instanceof CallerObject) {
                userType = PARAM_ADMIN_USER_TYPE_CALLER;
            } else if (this instanceof ObserverObject) {
                userType = PARAM_ADMIN_USER_TYPE_OBSERVER;
            } else if (this instanceof AdminUserObject) {
                userType = PARAM_ADMIN_USER_TYPE_CANDIDATE;
            } else if (this instanceof DriverObject) {
                userType = PARAM_ADMIN_USER_TYPE_DRIVER;
            } else if (this instanceof GroupManagerObject) {
                userType = PARAM_ADMIN_USER_TYPE_GROUP_MANAGER;
            }
        }
        return userType;
    }

    public int getContactType() {
        int contactType = -1;
        if (this instanceof VoterObject) {
            contactType = RECIPIENT_TYPE_VOTER;
        } else if (this instanceof ActivistObject) {
            contactType = RECIPIENT_TYPE_ACTIVIST;
        } else if (this instanceof CallerObject) {
            contactType = RECIPIENT_TYPE_CALLER;
        } else if (this instanceof ObserverObject) {
            contactType = RECIPIENT_TYPE_OBSERVER;
        }
        return contactType;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass()) && this.oid == ((BaseContact)obj).getOid();
    }

    public AdminUserObject getAdmin() {
        return adminUserObject = this instanceof AdminUserObject ? (AdminUserObject)this : getAdminUserObject();
    }

    public Integer getCampaignType () throws Exception {
        Integer campaignType = PARAM_CAMPAIGN_TYPE_MUNICIPAL;
        if (this.getAdmin().getCampaignObject() != null) {
            campaignType = this.adminUserObject.getCampaignObject().getType();
        }
        if (campaignType == null) {
            throw new Exception();
        }
        return campaignType;
    }

    public GroupManagerObject getGroupManagerObject() {
        return groupManagerObject;
    }

    public void setGroupManagerObject(GroupManagerObject groupManagerObject) {
        this.groupManagerObject = groupManagerObject;
    }


    @Override
    public boolean isEntityOwner(Integer adminOid, Integer managerOid) {
        boolean owner = false;
        if (managerOid != null) {
            if (this.groupManagerObject != null && this.groupManagerObject.getOid() == managerOid) {
                owner = true;
            }
        } else if (adminOid != null) {
            if (this.adminUserObject != null && this.adminUserObject.getOid() == adminOid) {
                owner = true;
            }
        }
        return  owner;
    }


    public GroupManagerObject getGroupManager () {
        GroupManagerObject groupManagerObject = null;
        if (!(this instanceof GroupManagerObject)) {
            groupManagerObject = this.groupManagerObject;
        } else {
            groupManagerObject = (GroupManagerObject) this;
        }
        return groupManagerObject;
    }
}
