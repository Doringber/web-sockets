package com.elector.Objects.Entities;

import com.elector.Interfaces.Ownable;
import com.elector.Objects.Entities.LaborParty.IsraelLaborPartyVoterObject;
import com.elector.Objects.Entities.LaborParty.IsraelLaborVoterEditableDataObject;
import com.elector.Objects.Entities.LikudParty.LikudPartyVoterObject;
import com.elector.Objects.Entities.LikudParty.LikudVoterEditableDataObject;
import com.elector.Objects.General.BaseVoterDataObject;
import com.elector.Objects.General.ExcelRowVoter;
import com.elector.Objects.General.VoterFamilyMemberDataObject;
import com.elector.Utils.TemplateUtils;
import com.elector.Utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;

import java.util.*;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Definitions.PARAM_CAMPAIGN_VOTER_OID;
import static com.elector.Utils.Definitions.PARAM_VOTE_STATUS_VOTED;


/**
 * Created by Sigal on 6/30/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VoterObject extends BaseVoterDataObject implements Ownable {
    protected String relationToActivist;
    protected Integer activistMappingOid;
    protected Long totalHandlingActivists = 0L;
    protected Boolean voted;
    protected Boolean allowRecall;
    protected Integer supportSign;
    protected boolean inCallerList;
    protected boolean inActivistList;
    protected Set<Integer> activistSlavesOids;
    protected ActivistObject activistObject;
    protected List<VoterCustomPropertyObject> customProperties;
    protected Date lastCallDate;
    protected List<VoterFamilyMemberDataObject> familyMembers;
    protected Set<Integer> callersOids = new HashSet<>();
    protected String comment;
    protected Integer language;
    protected int needsRide;
    private boolean hasPendingCallReminder;
    private Date timeToRemind;
    private Integer reminderCallOid;
    private Boolean votedRound1;
    private Date familyMemberReminder;
    private boolean editableCampiagnFieldsChanged;
    private int voteIntention;
    private int cityOid;

    public VoterObject() {
        this.supportersListStatus = SUPPORTERS_LIST_STATUS_NOT_ASKED_YET;
    }

    public VoterObject(int oid) {
        this.oid = oid;
    }

    public VoterObject(JSONObject jsonObject) {
        this.oid = jsonObject.getInt(PARAM_OID);
        if (!jsonObject.isNull(PARAM_CAMPAIGN_VOTER_OID)) {
            this.campaignVoterObject = new CampaignVoterObject(jsonObject.getInt(PARAM_CAMPAIGN_VOTER_OID));
        }
    }

    public VoterObject (BaseCampaignVoterObject campaignVoterObject, AdminUserObject adminUserObject) throws Exception {
        this.adminUserObject = adminUserObject;
        this.firstName = campaignVoterObject.getFirstName();
        this.lastName = campaignVoterObject.getLastName();
        this.voterId = campaignVoterObject.getVoterId();
        switch (getCampaignType()) {
            case PARAM_CAMPAIGN_TYPE_MUNICIPAL:
            case PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION:
                CampaignVoterObject campaignVoter = (CampaignVoterObject)campaignVoterObject;
                setCampaignVoterObject(campaignVoter);
                if (campaignVoter.getBallotBoxObject() != null && campaignVoter.getCityObject() != null) {
                    this.address = String.format("%s %s, %s",
                            campaignVoter.getStreet(),
                            (Utils.hasText(Utils.eliminateLeadingZeros(campaignVoter.getApartmentNumber())) && !Utils.eliminateLeadingZeros(campaignVoter.getApartmentNumber()).equals("0")) ?
                                    String.format("%s/%s", Utils.eliminateLeadingZeros(campaignVoter.getHouseNumber()),
                                            Utils.eliminateLeadingZeros(campaignVoter.getApartmentNumber())) :
                                    Utils.eliminateLeadingZeros(campaignVoter.getHouseNumber()), campaignVoter.getCityObject() != null ? campaignVoter.getCityObject().getName() : campaignVoterObject.getCampaignObject().getMainCity());
                } else {
                    this.address = String.format("%s %s, %s",
                            campaignVoter.getStreet(),
                            (Utils.hasText(Utils.eliminateLeadingZeros(campaignVoter.getApartmentNumber())) && !Utils.eliminateLeadingZeros(campaignVoter.getApartmentNumber()).equals("0")) ?
                                    String.format("%s/%s", Utils.eliminateLeadingZeros(campaignVoter.getHouseNumber()),
                                            Utils.eliminateLeadingZeros(campaignVoter.getApartmentNumber())) :
                                    Utils.eliminateLeadingZeros(campaignVoter.getHouseNumber()), campaignVoter.getCityObject() != null ? campaignVoter.getCityObject().getName() : campaignVoterObject.getCampaignObject().getMainCity());

                }
                break;
            case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LABOR:
                IsraelLaborPartyVoterObject israelLaborPartyCampaignVoter = (IsraelLaborPartyVoterObject)campaignVoterObject;
                setIsraelLaborPartyVoterObject(israelLaborPartyCampaignVoter);
                this.address = String.format("%s %s, %s", israelLaborPartyCampaignVoter.getStreet(), israelLaborPartyCampaignVoter.getHouseNumber(), israelLaborPartyCampaignVoter.getCityObject() != null ? israelLaborPartyCampaignVoter.getCityObject().getName() : MINUS);
                this.phone = Utils.reformatPhoneNumber(israelLaborPartyCampaignVoter.getPhone());
                this.birthDate = israelLaborPartyCampaignVoter.getBirthDate();
                IsraelLaborVoterEditableDataObject israelLaborVoterEditableDataObject = new IsraelLaborVoterEditableDataObject(israelLaborPartyCampaignVoter);
                setIsraelLaborVoterEditableDataObject(israelLaborVoterEditableDataObject);
                break;
            case PARAM_CAMPAIGN_TYPE_ISRAEL_PRIMARY_LIKUD:
                LikudPartyVoterObject likudPartyVoterObject = (LikudPartyVoterObject)campaignVoterObject;
                setLikudPartyVoterObject(likudPartyVoterObject);
                this.address = String.format("%s %s, %s", likudPartyVoterObject.getStreet(), likudPartyVoterObject.getHouseNumber(), likudPartyVoterObject.getCityObject() != null ? likudPartyVoterObject.getCityObject().getName() : MINUS);
                this.phone = Utils.reformatPhoneNumber(likudPartyVoterObject.getPhone());
                this.homePhone = Utils.reformatPhoneNumber(likudPartyVoterObject.getHomePhone());
                this.male = likudPartyVoterObject.getMale();
                LikudVoterEditableDataObject likudVoterEditableDataObject = new LikudVoterEditableDataObject(likudPartyVoterObject);
                setLikudVoterEditableDataObject(likudVoterEditableDataObject);
                break;
        }
        this.supportStatus = PARAM_SUPPORT_STATUS_UNKNOWN;
        this.supportSign = PARAM_SUPPORT_SIGN_NONE;
        this.supportersListStatus = SUPPORTERS_LIST_STATUS_NOT_ASKED_YET;
        this.language = PARAM_LANGUAGE_UNDEFINED;
        this.copyVotersBook = true;
    }

    public String getRelationToActivist() {
        return relationToActivist;
    }

    public void setRelationToActivist(String relationToActivist) {
        this.relationToActivist = relationToActivist;
    }

    public Integer getActivistMappingOid() {
        return activistMappingOid;
    }

    public void setActivistMappingOid(Integer activistMappingOid) {
        this.activistMappingOid = activistMappingOid;
    }

    public Long getTotalHandlingActivists() {
        return totalHandlingActivists;
    }

    public void setTotalHandlingActivists(Long totalHandlingActivists) {
        this.totalHandlingActivists = totalHandlingActivists;
    }

    public Integer getSupportSign() {
        return supportSign;
    }

    public void setSupportSign(Integer supportSign) {
        this.supportSign = supportSign;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public Boolean getAllowRecall() {
        return allowRecall;
    }

    public void setAllowRecall(Boolean allowRecall) {
        this.allowRecall = allowRecall;
    }

    public void incrementTotalHandlingActivists () {
        this.totalHandlingActivists++;
    }

    public void decrementTotalHandlingActivists () {
        if (this.totalHandlingActivists > 0) {
            this.totalHandlingActivists--;
        }
    }

    public boolean isInCallerList() {
        return inCallerList;
    }

    public void setInCallerList(boolean inCallerList) {
        this.inCallerList = inCallerList;
    }

    public boolean isInActivistList() {
        return inActivistList;
    }

    public void setInActivistList(boolean inActivistList) {
        this.inActivistList = inActivistList;
    }

    public Set<Integer> getActivistSlavesOids() {
        return activistSlavesOids;
    }

    public void setActivistSlavesOids(Set<Integer> activistSlavesOids) {
        this.activistSlavesOids = activistSlavesOids;
    }

    public void addActivistSlavesOids (Integer oid) {
        if (this.activistSlavesOids == null) {
            this.activistSlavesOids = new HashSet<>();
        }
        this.activistSlavesOids.add(oid);
    }

    public List<VoterCustomPropertyObject> getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(List<VoterCustomPropertyObject> customProperties) {
        this.customProperties = customProperties;
    }

    public String getPrintableSupportSign () {
        String text = EMPTY;
        if (this.supportSign != null) {
            switch (this.supportSign) {
                case PARAM_SUPPORT_SIGN_NONE:
                    text = Utils.getTranslation("none");
                    break;
                case PARAM_SUPPORT_SIGN_CANDIDATE:
                    text = Utils.getTranslation("ours");
                    break;
                case PARAM_SUPPORT_SIGN_RIVAL:
                    text = Utils.getTranslation("others");
                    break;
            }
        }
        return text;
    }

    public String getPrintableSupportersListStatus () {
        String text = EMPTY;
        if (this.supportersListStatus != null) {
            switch (this.supportersListStatus) {
                case SUPPORTERS_LIST_STATUS_NOT_ASKED_YET:
                    text = Utils.getTranslation("not.asked.yet");
                    break;
                case SUPPORTERS_LIST_STATUS_NO:
                    text = Utils.getTranslation("doesnt.want");
                    break;
                case SUPPORTERS_LIST_STATUS_WANTS_TO_GIVE:
                    text = Utils.getTranslation("wants.to.give");
                    break;
                case SUPPORTERS_LIST_STATUS_GAVE:
                    text = Utils.getTranslation("already.delivered");
                    break;
            }
        } else {
            text = Utils.getTranslation("not.asked.yet");
        }
        return text;
    }

    public ActivistObject getActivistObject() {
        return activistObject;
    }

    public void setActivistObject(ActivistObject activistObject) {
        this.activistObject = activistObject;
    }

    public Date getLastCallDate() {
        return lastCallDate;
    }

    public void setLastCallDate(Date lastCallDate) {
        this.lastCallDate = lastCallDate;
    }

    public List<VoterFamilyMemberDataObject> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<VoterFamilyMemberDataObject> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public Set<Integer> getCallersOids() {
        return callersOids;
    }

    public void setCallersOids(Set<Integer> callersOids) {
        this.callersOids = callersOids;
    }

    public void addCallerOid (Integer callerOid) {
        if (this.callersOids != null) {
            this.callersOids.add(callerOid);
        }
    }

    public void removeCallerOid (Integer callerOid) {
        if (this.callersOids != null) {
            this.callersOids.remove(callerOid);
        }
    }

    public boolean inCallerList (Integer callerOid) {
        return this.callersOids != null && this.callersOids.contains(callerOid);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public void setExcelData (ExcelRowVoter excelData) {
        Integer excelSupportStatus = excelData.getSupportStatus();
        Date excelBirthDate = excelData.getBirthDate();
        Boolean excelMale = excelData.getMale();
        String excelCellPhone = excelData.getCellPhone();
        String excelHomePhone = excelData.getHomePhone();
        String excelExtraPhone = excelData.getExtraPhone();
        String excelEmail = excelData.getEmail();
        if (excelSupportStatus != null) {
            this.supportStatus = excelSupportStatus;
        }
        if (excelBirthDate != null) {
            this.birthDate = excelBirthDate;
        }
        if (excelMale != null) {
            this.male = excelMale;
        }
        if (excelCellPhone != null) {
            this.phone = excelCellPhone;
        }
        if (excelHomePhone != null) {
            this.homePhone = excelHomePhone;
        }
        if (excelExtraPhone != null) {
            this.extraPhone = excelExtraPhone;
        }
        if (excelEmail != null) {
            this.email = excelEmail;
        }
    }

    public boolean needToUpdateSmsGroups (VoterObject newVoterData) {
        boolean needToUpdate = false;
        if ((Utils.isValidCellPhone(newVoterData.getPhone()) && (this.phone == null || !newVoterData.getPhone().equals(this.phone))) ||
                (Utils.isValidCellPhone(newVoterData.getHomePhone()) && (this.homePhone == null || !newVoterData.getHomePhone().equals(this.homePhone))) ||
                (Utils.isValidCellPhone(newVoterData.getExtraPhone()) && (this.extraPhone == null || !newVoterData.getExtraPhone().equals(this.extraPhone))) ||
                (!this.supportStatus.equals(newVoterData.getSupportStatus()))
        ) {
            needToUpdate = true;
        }
        return needToUpdate;
    }

    public boolean isPotentialFamilyMember (VoterObject other) {
        return this.oid != other.getOid() && this.lastName.equals(other.getLastName()) && this.address.equals(other.getAddress());
    }

    public String getAgeInfo () {
        String ageInfo = MINUS;
        if (this.birthDate != null) {
            String birthDate = TemplateUtils.formatDateExcludeTime(this.birthDate);
            int age = Utils.calculateAge(this.birthDate);
            if (Utils.hasText(birthDate) && age > 0) {
                ageInfo = String.format("%s (%s)", birthDate, age);
            }
        }
        return ageInfo;
    }

    public int getNeedsRide() {
        return needsRide;
    }

    public void setNeedsRide(int needsRide) {
        this.needsRide = needsRide;
    }

    public boolean hasVoted() {
        return this.voted != null && this.voted;
    }

    public boolean updateSupportStatusByVoteStatus (int voteStatus) {
        boolean needsUpdate = false;
        if (supportStatus == PARAM_SUPPORT_STATUS_SUPPORTING && voteStatus == PARAM_VOTE_STATUS_VOTED_AGAINST) {
            supportStatus = PARAM_SUPPORT_STATUS_NOT_SUPPORTING;
            needsUpdate = true;
        } else if (supportStatus != PARAM_SUPPORT_STATUS_SUPPORTING && voteStatus == PARAM_VOTE_STATUS_VOTED_FOR) {
            supportStatus = PARAM_SUPPORT_STATUS_SUPPORTING;
            needsUpdate = true;
        }
        return needsUpdate;
    }

    public boolean updateVotingStatus (int votingStatus, boolean round1) {
        Boolean newVoted = null;
        switch (votingStatus) {
            case PARAM_VOTE_STATUS_NOT_YET_VOTED:
                newVoted = false;
                break;
            case PARAM_VOTE_STATUS_VOTED:
                newVoted= true;
                break;
        }
        boolean changed;
        if (round1) {
            changed = ((this.votedRound1 == null && newVoted != null) || (this.votedRound1 != null && newVoted == null) || (this.votedRound1 != null && !this.votedRound1.equals(newVoted)));
            this.voted = newVoted;
        } else {
            changed = ((this.voted == null && newVoted != null) || (this.voted != null && newVoted == null) || (this.voted != null && !this.voted.equals(newVoted)));
            this.voted = newVoted;
        }
        return changed;
    }

    public boolean isHasPendingCallReminder() {
        return hasPendingCallReminder;
    }

    public void setHasPendingCallReminder(boolean hasPendingCallReminder) {
        this.hasPendingCallReminder = hasPendingCallReminder;
    }

    public boolean isValidForElectionDayCall (boolean round1) {
        boolean valid;
        if (round1) {
            valid = (this.votedRound1 == null || !this.votedRound1) && this.hasValidPhoneNumber() && (this.allowRecall == null || this.allowRecall) && !hasPendingCallReminder && this.familyMemberReminder == null;
        } else {
            valid = (this.voted == null || !this.voted) && this.hasValidPhoneNumber() && (this.allowRecall == null || this.allowRecall) && !hasPendingCallReminder && this.familyMemberReminder == null;
        }
        return valid;
    }

    public Date getTimeToRemind() {
        return timeToRemind;
    }

    public void setTimeToRemind(Date timeToRemind) {
        this.timeToRemind = timeToRemind;
    }

    public Integer getReminderCallOid() {
        return reminderCallOid;
    }

    public void setReminderCallOid(Integer reminderCallOid) {
        this.reminderCallOid = reminderCallOid;
    }

    public String getPrintableVoteStatus () {
        return  Utils.getTranslation((this.voted == null || !this.voted) ? "not.yet.voted" : "voted");
    }

    public Boolean getVotedRound1() {
        return votedRound1;
    }

    public void setVotedRound1(Boolean votedRound1) {
        this.votedRound1 = votedRound1;
    }

    public boolean hasVotedRound1() {
        return this.votedRound1 != null && this.votedRound1;
    }

    public String getPrintableVoteStatusRound1 () {
        return  Utils.getTranslation((this.votedRound1 == null || !this.votedRound1) ? "not.yet.voted" : "voted");
    }

    public Date getFamilyMemberReminder() {
        return familyMemberReminder;
    }

    public void setFamilyMemberReminder(Date familyMemberReminder) {
        this.familyMemberReminder = familyMemberReminder;
    }

    public boolean isActivist () {
        return activistObject != null && !activistObject.isDeleted();
    }

    public boolean canCall () {
        return this.allowRecall == null || this.allowRecall;
    }

    public boolean isEditableCampiagnFieldsChanged() {
        return editableCampiagnFieldsChanged;
    }

    public void setEditableCampiagnFieldsChanged(boolean editableCampiagnFieldsChanged) {
        this.editableCampiagnFieldsChanged = editableCampiagnFieldsChanged;
    }

    public int getVoteIntention() {
        return voteIntention;
    }

    public void setVoteIntention(int voteIntention) {
        this.voteIntention = voteIntention;
    }

    @Override
    public boolean isEntityOwner(Integer adminOid, Integer managerOid) {
        return adminOid != null && this.adminUserObject.getOid() == adminOid;
    }

    public int getCityOid() {
        return cityOid;
    }

    public void setCityOid(int cityOid) {
        this.cityOid = cityOid;
    }
}
