package com.elector.Objects.Entities;

import com.elector.Objects.General.EventObject;
import com.elector.Utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SupporterBirthdayObject extends EventObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupporterBirthdayObject.class);

    private VoterObject supporter;

    public SupporterBirthdayObject() {
    }

    public SupporterBirthdayObject (VoterObject supporter) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(supporter.getBirthDate());
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.date = calendar.getTime();
        try {
            this.title = String.format("%s %s %s %s", "יום הולדת", Utils.yearsBetween(supporter.getBirthDate(), new Date()) ,"לתומך", supporter.getFullName());
        } catch (Exception e) {
            LOGGER.error("SupporterBirthdayObject", e);
        }
        this.adminUserObject = supporter.getAdminUserObject();
        this.supporter = supporter;
    }

    public VoterObject getSupporter() {
        return supporter;
    }

    public void setSupporter(VoterObject supporter) {
        this.supporter = supporter;
    }
}
