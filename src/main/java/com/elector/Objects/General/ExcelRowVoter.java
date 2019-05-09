package com.elector.Objects.General;

import com.elector.Utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Utils.hasText;

public class ExcelRowVoter {
    private String voterId;
    private Integer supportStatus;
    private Date birthDate;
    private Boolean male;
    private String cellPhone;
    private String homePhone;
    private String extraPhone;
    private String email;
    private boolean valid;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelRowVoter.class);

    public ExcelRowVoter (String line) {
        List<String> fields = Arrays.asList(line.replaceAll(COMMA, SPACE + COMMA).split(COMMA));
        String voterId = fields.get(EXCEL_ROW_VOTER_ID);
        if (voterId != null) {
            voterId = voterId.trim();
            if (voterId.length() > 0 && StringUtils.isNumeric(voterId.trim())) {
                valid = true;
                this.voterId = voterId;
                String supportStatus =  fields.get(EXCEL_ROW_VOTER_SUPPORT_STATUS);
                if (supportStatus != null) {
                    supportStatus = supportStatus.trim();
                    if (supportStatus.length() > 0 && StringUtils.isNumeric(supportStatus) && Integer.valueOf(supportStatus) >= 0 && Integer.valueOf(supportStatus) <= 4) {
                        this.supportStatus = Integer.valueOf(supportStatus);
                    }
                }
                String birthDate =  fields.get(EXCEL_ROW_VOTER_BIRTH_DATE);
                if (hasText(birthDate)) {
                    birthDate = birthDate.trim();
                    Date date = Utils.tryParseDate(birthDate);
                    if (date != null) {
                        this.birthDate = date;
                    } else if (birthDate.length() == 4 && Integer.valueOf(birthDate) > 1920 && Integer.valueOf(birthDate) < 2002) {
                        try {
                            this.birthDate = new SimpleDateFormat("dd-MM-yyyy").parse(String.format("01-01-%s", birthDate));
                        } catch (ParseException e) {
                        }
                    }
                }
                String gender = fields.get(EXCEL_ROW_VOTER_GENDER);
                if (gender != null) {
                    gender = gender.trim();
                    if (gender.length() > 0 && StringUtils.isNumeric(gender) && Integer.valueOf(gender) >= 0 && Integer.valueOf(gender) <= 2) {
                        this.male = Integer.valueOf(gender) == PARAM_GENDER_MALE;
                    }
                }
                String cellPhone = fields.get(EXCEL_ROW_VOTER_CELLULAR_PHONE);
                if (cellPhone != null) {
                    cellPhone = cellPhone.replaceAll(MINUS, EMPTY).trim();
                    if (cellPhone.length() > 0) {
                        cellPhone = Utils.getPhoneWithoutCountryCodePrefix(cellPhone);
                        if (!Utils.isValidCellPhone(cellPhone.replaceAll(MINUS, EMPTY)) && Utils.isValidCellPhone("0" + cellPhone.replaceAll(MINUS, EMPTY))) {
                            cellPhone = "0" + cellPhone;
                        }
                        this.cellPhone = cellPhone;
                    }
                }
                String homePhone = fields.get(EXCEL_ROW_VOTER_HOME_PHONE);
                if (homePhone != null) {
                    homePhone = homePhone.trim();
                    homePhone = Utils.getPhoneWithoutCountryCodePrefix(homePhone);
                    if (homePhone.length() > 0) {
                        if (!homePhone.startsWith("0")) {
                            homePhone = "0" + homePhone;
                        }
                        this.homePhone = homePhone;
                    }
                }
                String extraPhone = fields.get(EXCEL_ROW_VOTER_EXTRA_PHONE);
                if (extraPhone != null) {
                    extraPhone =  extraPhone.trim();
                    extraPhone = Utils.getPhoneWithoutCountryCodePrefix(extraPhone);
                    if (extraPhone.length() > 0) {
                        if (!extraPhone.startsWith("0")) {
                            extraPhone = "0" + extraPhone;
                        }
                        this.extraPhone = extraPhone;
                    }
                }

                if (fields.size() > EXCEL_ROW_VOTER_EMAIL) {
                    String email = fields.get(EXCEL_ROW_VOTER_EMAIL);
                    if (email != null) {
                        email = email.trim();
                        if (email.length() > 0) {
                            this.email = email.trim();
                        }
                    }

                }

            }


        } else {
            valid = false;
        }
    }


    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public Integer getSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(Integer supportStatus) {
        this.supportStatus = supportStatus;
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

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
