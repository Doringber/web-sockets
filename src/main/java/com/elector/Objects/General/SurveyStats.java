package com.elector.Objects.General;

import com.elector.Objects.Entities.SurveyAnswerObject;
import com.elector.Objects.Entities.SurveyObject;
import com.elector.Objects.Entities.SurveyVoterAnswerObject;
import com.elector.Utils.Utils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyStats  {
    private int totalRecipients;
    private int totalAnswered;
    private String creationDate;
    private Date endDate;
    private Map<String, Integer> totalAnswersMap;
    private Map<String, String> percentAnswersMap;

    public SurveyStats (SurveyObject surveyObject, List<SurveyVoterAnswerObject> voterAnswerObjectList) {
        this.totalRecipients = surveyObject.getRecipients();
        this.totalAnswered = voterAnswerObjectList.size();
        this.creationDate = Utils.formatDate(surveyObject.getCreationDate(), "dd-MM-yyyy HH:mm");
        this.totalAnswersMap = new HashMap<>();
        for (SurveyAnswerObject answerObject : surveyObject.getAnswers()) {
            totalAnswersMap.put(answerObject.getText(), 0);
        }
        for (SurveyVoterAnswerObject surveyVoterAnswerObject : voterAnswerObjectList) {
            String answer = surveyVoterAnswerObject.getAnswerObject().getText();
            Integer count = totalAnswersMap.get(answer);
            if (count != null) {
                totalAnswersMap.put(answer, count + 1);
            }
        }

        percentAnswersMap = new HashMap<>();
        for (String text : totalAnswersMap.keySet()) {
            percentAnswersMap.put(text, this.totalAnswered == 0 ? "0" : String.format("%.3f", ((totalAnswersMap.get(text) / (float)totalAnswered) * 100)));
        }
    }

    public int getTotalRecipients() {
        return totalRecipients;
    }

    public void setTotalRecipients(int totalRecipients) {
        this.totalRecipients = totalRecipients;
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(int totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Map<String, Integer> getTotalAnswersMap() {
        return totalAnswersMap;
    }

    public void setTotalAnswersMap(Map<String, Integer> totalAnswersMap) {
        this.totalAnswersMap = totalAnswersMap;
    }

    public Map<String, String> getPercentAnswersMap() {
        return percentAnswersMap;
    }

    public void setPercentAnswersMap(Map<String, String> percentAnswersMap) {
        this.percentAnswersMap = percentAnswersMap;
    }
}
