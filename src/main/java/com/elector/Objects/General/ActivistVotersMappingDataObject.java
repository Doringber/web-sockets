package com.elector.Objects.General;

import com.elector.Objects.Entities.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.elector.Utils.Definitions.*;

public class ActivistVotersMappingDataObject {
    private String activistName;
    private String activistPhone;
    private List<VoterObject> voters;

    public ActivistVotersMappingDataObject(JSONObject jsonObject) {
        this.activistName = jsonObject.getString(PARAM_ACTIVIST_NAME);
        this.activistPhone = jsonObject.getString(PARAM_ACTIVIST_PHONE);
        List<VoterObject> voterObjects = new ArrayList<>();
        JSONArray votersJson = jsonObject.getJSONArray(PARAM_VOTERS);
        for (int i = 0; i < votersJson.length(); i++) {
            VoterObject voterObject = new VoterObject();
            JSONObject voterJson = votersJson.getJSONObject(i);
            int oid = voterJson.getInt(PARAM_OID);
            String firstName = voterJson.getString(PARAM_FIRST_NAME);
            String lastName = voterJson.getString(PARAM_LAST_NAME);
            String address = voterJson.getString(PARAM_ADDRESS);
            int supportSign = voterJson.getInt(PARAM_SUPPORT_SIGN);
            int voterNumber = voterJson.getInt(PARAM_VOTER_NUMBER);
            AdminUserObject adminUserObject = new AdminUserObject();
            JSONObject adminJson = voterJson.getJSONObject(PARAM_ADMIN_USER_OBJECT);;
            if (adminJson != null) {
                adminUserObject.setOid(adminJson.getInt(PARAM_OID));
            }
            CampaignVoterObject campaignVoterObject = new CampaignVoterObject();
            JSONObject campaignVoterJson = voterJson.getJSONObject(PARAM_CAMPAIGN_VOTER_OBJECT);
            if (campaignVoterJson != null) {
                JSONObject ballotBoxJson = campaignVoterJson.getJSONObject(PARAM_BALLOT_BOX_OBJECT);
                if (ballotBoxJson != null) {
                    BallotBoxObject ballotBoxObject = new BallotBoxObject();
                    ballotBoxObject.setNumber(ballotBoxJson.getInt(PARAM_NUMBER));
                    ballotBoxObject.setAddress(ballotBoxJson.getString(PARAM_ADDRESS));
                    campaignVoterObject.setBallotBoxObject(ballotBoxObject);
                }
            }
            int supportStatus = voterJson.getInt(PARAM_SUPPORT_STATUS);
            String phone = voterJson.getString(PARAM_PHONE);
            String homePhone = voterJson.isNull(PARAM_HOME_PHONE) ? EMPTY :  voterJson.getString(PARAM_HOME_PHONE);
            String extraPhone = voterJson.isNull(PARAM_EXTRA_PHONE) ? EMPTY :   voterJson.getString(PARAM_EXTRA_PHONE);
            String voterId = voterJson.getString(PARAM_VOTER_ID);
            int supportersListStatus = voterJson.getInt(PARAM_SUPPORTERS_LIST_STATUS);
            Boolean allowRecall = voterJson.isNull(PARAM_ALLOW_RECALL) || voterJson.getBoolean(PARAM_ALLOW_RECALL);
            Boolean votedRound1 = voterJson.isNull(PARAM_VOTED_ROUND_1) || voterJson.getBoolean(PARAM_VOTED_ROUND_1);
            String email = voterJson.isNull(PARAM_EMAIL) ? EMPTY :   voterJson.getString(PARAM_EMAIL);
            voterObject.setOid(oid);
            voterObject.setSupportStatus(supportStatus);
            voterObject.setPhone(phone);
            voterObject.setVoterId(voterId);
            voterObject.setAdminUserObject(adminUserObject);
            voterObject.setCampaignVoterObject(campaignVoterObject);
            voterObject.setSupportersListStatus(supportersListStatus);
            voterObject.setEmail(email);
            voterObject.setFirstName(firstName);
            voterObject.setLastName(lastName);
            voterObject.setAddress(address);
            voterObject.setSupportSign(supportSign);
//            voterObject.setVoterNumber(voterNumber);
            voterObject.setHomePhone(homePhone);
            voterObject.setExtraPhone(extraPhone);
            voterObject.setAllowRecall(allowRecall);
            voterObject.setVotedRound1(votedRound1);
            voterObjects.add(voterObject);
        }

        this.voters = voterObjects;
    }

    public List<VoterObject> getVoters() {
        return voters;
    }

    public void setVoters(List<VoterObject> voters) {
        this.voters = voters;
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
}
