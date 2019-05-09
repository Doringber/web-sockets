package com.elector.Objects.General;

import com.elector.Enums.VotersExcelColumnsEnum;
import com.elector.Objects.Entities.*;
import com.elector.Objects.Entities.LaborParty.IsraelLaborPartyVoterObject;
import com.elector.Objects.Entities.LaborParty.IsraelLaborVoterEditableDataObject;
import com.elector.Objects.Entities.LikudParty.LikudPartyVoterObject;
import com.elector.Objects.Entities.LikudParty.LikudVoterEditableDataObject;
import com.elector.Utils.TemplateUtils;
import com.elector.Utils.Utils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Definitions.SLASH;

public class BaseVoterDataObject extends BaseContact {
    protected String voterId;
    protected String address;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date birthDate;
    protected Boolean male;
    protected Integer supportStatus;
    protected CampaignVoterObject campaignVoterObject;
    private IsraelLaborPartyVoterObject israelLaborPartyVoterObject;
    protected Integer supportersListStatus;
    private LikudPartyVoterObject likudPartyVoterObject;
    private LikudVoterEditableDataObject likudVoterEditableDataObject;
    private IsraelLaborVoterEditableDataObject israelLaborVoterEditableDataObject;
    protected boolean copyVotersBook;
    private String campaignAddress;

    public String getVoterId () {
        String voterId = EMPTY;
        try {
            if (Utils.hasText(this.voterId)) {
                voterId = this.voterId;
            } else {
                BaseCampaignVoterObject baseCampaignVoterObject = this.getBaseCampaignVoterObject();
                if (baseCampaignVoterObject != null) {
                    voterId = baseCampaignVoterObject.getVoterId();
                }
            }
            if (voterId.startsWith(FAKE_LICENSE_ID_PREFIX)) {
                voterId = EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public Integer getSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(Integer supportStatus) {
        this.supportStatus = supportStatus;
    }

    public BaseCampaignVoterObject getBaseCampaignVoterObject() throws Exception {
        BaseCampaignVoterObject baseCampaignVoterObject = null;
        switch (getCampaignType()) {
            case PARAM_CAMPAIGN_TYPE_MUNICIPAL:
            case PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION:
            case PARAM_CAMPAIGN_TYPE_LAWYERS:
                baseCampaignVoterObject = campaignVoterObject;
                break;
            case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LABOR:
                baseCampaignVoterObject = israelLaborPartyVoterObject;
                break;
            case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LIKUD:
                baseCampaignVoterObject = likudPartyVoterObject;
                break;
            default:
                baseCampaignVoterObject = campaignVoterObject;
                break;
        }
        return baseCampaignVoterObject;
    }

    private CampaignVoterObject getCampaignVoterObject() {
        return campaignVoterObject;
    }

    protected void setCampaignVoterObject(CampaignVoterObject campaignVoterObject) {
        this.campaignVoterObject = campaignVoterObject;
    }

    public Integer getSupportersListStatus() {
        return supportersListStatus;
    }

    public void setSupportersListStatus(Integer supportersListStatus) {
        this.supportersListStatus = supportersListStatus;
    }

    public void readVotersBookRow (AdminUserObject adminUserObject, String row, boolean supporting, Map<Integer, Integer> map) {
        List<String> fields = Arrays.asList(row.split(COMMA));
        this.adminUserObject = adminUserObject;
        this.voterId = Utils.reformatVoterId(Utils.getStringFromList(fields, map.get(VotersExcelColumnsEnum.id.getValue())));
        this.firstName = Utils.getStringFromList(fields, map.get(VotersExcelColumnsEnum.first_name.getValue()));
        this.lastName = Utils.getStringFromList(fields, map.get(VotersExcelColumnsEnum.last_name.getValue()));
        this.phone = Utils.reformatPhone(Utils.getStringFromList(fields, map.get(VotersExcelColumnsEnum.phone.getValue())));
        this.address = Utils.getStringFromList(fields, map.get(VotersExcelColumnsEnum.address.getValue()));
        this.birthDate = Utils.tryParseDate(Utils.getStringFromList(fields, map.get(VotersExcelColumnsEnum.birth_date.getValue())));
        this.supportStatus = supporting ? PARAM_SUPPORT_STATUS_SUPPORTING : PARAM_SUPPORT_STATUS_UNKNOWN;
    }

    public int getGenderCode () {
        int code = PARAM_GENDER_UNKNOWN;
        if (this.male != null) {
            if (this.male) {
                code = PARAM_GENDER_MALE;
            } else {
                code = PARAM_GENDER_FEMALE;
            }
        }
        return code;
    }

    public String getFormattedBirthDate() {
        return birthDate != null ? TemplateUtils.formatDateExcludeTime(this.birthDate) : EMPTY;
    }

    public String getPrintableSupportStatus () {
        String text = EMPTY;
        switch (this.supportStatus) {
            case PARAM_SUPPORT_STATUS_UNKNOWN:
                text = Utils.getTranslation("support.unknown");
                break;
            case PARAM_SUPPORT_STATUS_SUPPORTING:
                text = Utils.getTranslation("supporting");
                break;
            case PARAM_SUPPORT_STATUS_NOT_SUPPORTING:
                text = Utils.getTranslation("not.supporting");
                break;
            case PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING:
                text = Utils.getTranslation("unverified.supporting");
                break;
        }
        return text;
    }

    public String getPrintableGender () {
        String text = EMPTY;
        switch (this.getGenderCode()) {
            case PARAM_GENDER_UNKNOWN:
                text = Utils.getTranslation("unknown");
                break;
            case PARAM_GENDER_MALE:
                text = Utils.getTranslation("male");
                break;
            case PARAM_GENDER_FEMALE:
                text = Utils.getTranslation("female");
                break;
        }
        return text;
    }

    public Integer getCampaignType () throws Exception {
        Integer campaignType = PARAM_CAMPAIGN_TYPE_MUNICIPAL;
        if (adminUserObject != null) {
            CampaignObject campaignObject = adminUserObject.getCampaignObject();
            if (campaignObject != null) {
                campaignType = campaignObject.getType();
            }
        }
        if (campaignType == null) {
            throw new Exception();
        }
        return campaignType;
    }

    private IsraelLaborPartyVoterObject getIsraelLaborPartyVoterObject() {
        return israelLaborPartyVoterObject;
    }

    protected void setIsraelLaborPartyVoterObject(IsraelLaborPartyVoterObject israelLaborPartyVoterObject) {
        this.israelLaborPartyVoterObject = israelLaborPartyVoterObject;
    }

    private LikudPartyVoterObject getLikudPartyVoterObject() {
        return likudPartyVoterObject;
    }

    protected void setLikudPartyVoterObject(LikudPartyVoterObject likudPartyVoterObject) {
        this.likudPartyVoterObject = likudPartyVoterObject;
    }

    private LikudVoterEditableDataObject getLikudVoterEditableDataObject() {
        return likudVoterEditableDataObject;
    }

    protected void setLikudVoterEditableDataObject(LikudVoterEditableDataObject likudVoterEditableDataObject) {
        this.likudVoterEditableDataObject = likudVoterEditableDataObject;
    }

    private IsraelLaborVoterEditableDataObject getIsraelLaborVoterEditableDataObject() {
        return israelLaborVoterEditableDataObject;
    }

    protected void setIsraelLaborVoterEditableDataObject(IsraelLaborVoterEditableDataObject israelLaborVoterEditableDataObject) {
        this.israelLaborVoterEditableDataObject = israelLaborVoterEditableDataObject;
    }

    public BaseCampaignEditableDataObject getBaseCampaignVoterEditableDataObject() throws Exception {
        BaseCampaignEditableDataObject editableDataObject = null;
        switch (getCampaignType()) {
            case PARAM_CAMPAIGN_TYPE_MUNICIPAL:
            case PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION:
                break;
            case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LABOR:
                editableDataObject = israelLaborVoterEditableDataObject;
                if (editableDataObject == null) {
                    editableDataObject = new IsraelLaborVoterEditableDataObject();
                }
                break;
            case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LIKUD:
                editableDataObject = likudVoterEditableDataObject;
                if (editableDataObject == null) {
                    editableDataObject = new LikudVoterEditableDataObject();
                }

                break;

        }
        return editableDataObject;
    }

    public boolean isCopyVotersBook() {
        return copyVotersBook;
    }

    public void setCopyVotersBook(boolean copyVotersBook) {
        this.copyVotersBook = copyVotersBook;
    }

    public String getCampaignAddress() {
        String address = EMPTY;
        if (this.campaignAddress == null) {
            try {
                StringBuilder addressBuilder = new StringBuilder();
                switch (getCampaignType()) {
                    case PARAM_CAMPAIGN_TYPE_MUNICIPAL:
                    case PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION:
                        if (this.campaignVoterObject != null) {
                            String street = this.campaignVoterObject.getStreet();
                            String houseNumber = this.campaignVoterObject.getHouseNumber();
                            String apartmentNumber = this.campaignVoterObject.getApartmentNumber();
                            CityObject cityObject = this.campaignVoterObject.getCityObject();
                            if (Utils.hasText(street)) {
                                addressBuilder.append(street);
                                if (Utils.hasText(houseNumber)) {
                                    addressBuilder.append(SPACE);
                                    addressBuilder.append(Utils.eliminateLeadingZeros(houseNumber));
                                }
                                if (Utils.hasText(apartmentNumber)) {
                                    apartmentNumber = Utils.eliminateLeadingZeros(apartmentNumber);
                                    if (!apartmentNumber.equals(ZERO_STRING)) {
                                        addressBuilder.append(SLASH);
                                        addressBuilder.append(Utils.eliminateLeadingZeros(apartmentNumber));
                                    }
                                }
                                addressBuilder.append(COMMA);
                                addressBuilder.append(SPACE);
                            }
                            if (cityObject != null) {
                                addressBuilder.append(cityObject.getName());
                            }
                        }
                        address = addressBuilder.toString();
                        break;

                    case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LABOR:
                        if (this.israelLaborPartyVoterObject != null) {
                            if (Utils.hasText(this.israelLaborPartyVoterObject.getStreet())) {
                                addressBuilder.append(this.israelLaborPartyVoterObject.getStreet());
                                if (Utils.hasText(this.israelLaborPartyVoterObject.getHouseNumber()) && !this.israelLaborPartyVoterObject.getHouseNumber().equals("0")) {
                                    addressBuilder.append(SPACE).append(this.israelLaborPartyVoterObject.getHouseNumber());
                                }
                            }
                            if (this.israelLaborPartyVoterObject.getCityObject() != null) {
                                addressBuilder.append(COMMA).append(SPACE).append(this.israelLaborPartyVoterObject.getCityObject().getName());
                            }
                            address = addressBuilder.toString();
                        }
                        break;
                    case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LIKUD:
                        if (this.likudPartyVoterObject != null) {
                            boolean hasStreet = false;
                            if (Utils.hasText(this.likudPartyVoterObject.getStreet())) {
                                addressBuilder.append(this.likudPartyVoterObject.getStreet());
                                if (Utils.hasText(this.likudPartyVoterObject.getHouseNumber()) && !this.likudPartyVoterObject.getHouseNumber().equals("0")) {
                                    addressBuilder.append(SPACE).append(this.likudPartyVoterObject.getHouseNumber());
                                }
                                hasStreet = true;
                            }
                            if (this.likudPartyVoterObject.getCityObject() != null) {
                                if (hasStreet) {
                                    addressBuilder.append(COMMA).append(SPACE);
                                }
                                addressBuilder.append(this.likudPartyVoterObject.getCityObject().getName());
                            }
                            address = addressBuilder.toString();
                        }
                        break;
                }
                this.campaignAddress = address;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            address = this.campaignAddress;
        }
        return address;
    }

    public String getFirstName () {
        String firstName = EMPTY;
        try {
            if (Utils.hasText(this.firstName)) {
                firstName = this.firstName;
            } else {
                BaseCampaignVoterObject baseCampaignVoterObject = this.getBaseCampaignVoterObject();
                if (baseCampaignVoterObject != null) {
                    firstName = baseCampaignVoterObject.getFirstName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstName;
    }

    public String getLastName () {
        String lastName = EMPTY;
        try {
            if (Utils.hasText(this.lastName)) {
                lastName = this.lastName;
            } else {
                BaseCampaignVoterObject baseCampaignVoterObject = this.getBaseCampaignVoterObject();
                if (baseCampaignVoterObject != null) {
                    lastName = baseCampaignVoterObject.getLastName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastName;
    }

    public String getFullName () {
        String fullName = EMPTY;
        try {
            BaseCampaignVoterObject baseCampaignVoterObject = this.getBaseCampaignVoterObject();
            if (baseCampaignVoterObject != null) {
                String firstName = getFirstName();
                String lastName = getLastName();
                fullName = String.format("%s %s", firstName, lastName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullName;
    }

}
