package com.elector.Objects.General;

import com.elector.Utils.TemplateUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public class CallsStatsObject {
    private int totalDays;
    private int totalCalls;
    private String maxCallsDate;
    private int maxCallsInDay;
    private int averageCallsInDay;

    public CallsStatsObject (Map<Date, Integer> callsMap) {
        this.totalDays = callsMap.size();
        this.totalCalls = callsMap.values().stream().mapToInt(Number::intValue).sum();
        Date date = Collections.max(callsMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        this.maxCallsDate = TemplateUtils.formatDateExcludeTime(date);
        this.maxCallsInDay = callsMap.get(date);
        this.averageCallsInDay = totalCalls / totalDays;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(int totalCalls) {
        this.totalCalls = totalCalls;
    }

    public String getMaxCallsDate() {
        return maxCallsDate;
    }

    public void setMaxCallsDate(String maxCallsDate) {
        this.maxCallsDate = maxCallsDate;
    }

    public int getMaxCallsInDay() {
        return maxCallsInDay;
    }

    public void setMaxCallsInDay(int maxCallsInDay) {
        this.maxCallsInDay = maxCallsInDay;
    }

    public int getAverageCallsInDay() {
        return averageCallsInDay;
    }

    public void setAverageCallsInDay(int averageCallsInDay) {
        this.averageCallsInDay = averageCallsInDay;
    }
}
