package com.elector.Utils;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.Entities.*;
import com.elector.Objects.General.*;
import com.elector.Objects.General.EmailObject;
import com.elector.Services.GeneralManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Definitions.EMPTY;
import static com.elector.Utils.Definitions.PARAM_BALLOT_BOX_NUMBER;
import static com.elector.Utils.NavigationUtils.*;
import static com.elector.Utils.NavigationUtils.PATH_TO_ELECTOR_DIRECTORY;
import static com.elector.Utils.NavigationUtils.PATH_TO_VOTERS_FILES;

@Component
public class CsvUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvUtils.class);

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private EmailUtils emailUtils;

    private Map<Integer, Map<Integer, Object>> reportsData = new HashMap<>();

    private final Integer REPORT_TYPE_SUPPORTERS_NO_ACTIVISTS = 1;
    private final Integer REPORT_TYPE_ACTIVISTS = 2;
    private final Integer REPORT_TYPE_SUPPORTERS_NOT_YET_VOTED = 3;
    private final Integer REPORT_TYPE_SUPPORTERS_NOT_YET_VOTED_BY_ACTIVISTS = 4;
    private final Integer REPORT_TYPE_SUPPORTERS_NOT_YET_VOTED_WITH_NO_ACTIVIST = 5;
    private final Integer REPORT_TYPE_CALLS = 6;

    private Object getReportData(int candidateOid, int reportType) {
        Object data = null;
        Map<Integer, Object> candidateData = reportsData.get(candidateOid);
        if (candidateData != null) {
            data = candidateData.get(reportType);
        }
        return data;
    }

    private void validatePath(final Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (Exception e) {
            LOGGER.error("validatePath", e);
        }
    }

    public String csvFileName(int candidateOid, String reportName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        Path path = Paths.get(PATH_TO_CSV_FILES);
        validatePath(path);
        return String.format("%s_%s_%s_%s.csv", PATH_TO_CSV_FILES, reportName, candidateOid, simpleDateFormat.format(new Date()));
    }


    private void putReportData(int candidateOid, int reportType, Object data) {
        Map<Integer, Object> candidateData = reportsData.get(candidateOid);
        if (candidateData == null) {
            candidateData = new HashMap<>();
        }
        candidateData.put(reportType, data);
        reportsData.put(candidateOid, candidateData);
    }

    //language support for csv file
    private void csvFileLang(OutputStream outputStream) {
        try {
            outputStream.write(239);
            outputStream.write(187);
            outputStream.write(191);
        } catch (Exception e) {
            LOGGER.error("csvFileLang", e);
        }
    }

    public List<SupporterWithoutActivistsReportObject> supporterWithNoActivistsData(int candidateOid) {
        List<SupporterWithoutActivistsReportObject> supportersReports = null;
        try {
            supportersReports = (List<SupporterWithoutActivistsReportObject>) getReportData(candidateOid, REPORT_TYPE_SUPPORTERS_NO_ACTIVISTS);
            if (supportersReports == null) {
                List<VoterObject> supporters = generalManager.getSupportersWithNoActivists(candidateOid);
                if (!supporters.isEmpty()) {
                    supportersReports = new ArrayList<>();
                    for (VoterObject supporter : supporters) {
                        supportersReports.add(new SupporterWithoutActivistsReportObject(supporter));
                    }
                    putReportData(candidateOid, REPORT_TYPE_SUPPORTERS_NO_ACTIVISTS, supportersReports);

                }
            }
        } catch (Exception e) {
            LOGGER.error("supporterWithNoActivistsData", e);
        }
        return supportersReports;
    }

    public void supporterWithNoActivistsReport(File csv,int candidateOid, HttpServletResponse response) {
        try {
            List<SupporterWithoutActivistsReportObject> supportersReports = supporterWithNoActivistsData(candidateOid);
//            File csv = new File(csvFileName(candidateOid, "supporters.without.activists"));
//            FileWriter writer = new FileWriter(csv);
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

            writer.append(Utils.getTranslation("name")).append(COMMA).append(Utils.getTranslation("phone")).append(COMMA).append("\n");
            for (SupporterWithoutActivistsReportObject supporterWithoutActivistsReportObject : supportersReports) {
                writer.append(supporterWithoutActivistsReportObject.printAsCsvRow(Arrays.asList("fullName", "phone")));
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("supporterWithNoActivistsReport", e);
        }
    }

    public void callsReport(File csv,int candidateOid, HttpServletResponse response) {
        try {
            List<VoterCallObject> calls = generalManager.getCallsByAdminOid(candidateOid, null, null, false);
            Map<String, Map<String, Integer>> callsMap = new HashMap<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
            List<String> dates = new ArrayList<>();
            for (VoterCallObject callObject : calls) {
                CallerObject callerObject = callObject.getCallerObject();
                Map<String, Integer> callerCalls = callsMap.get(callerObject.getFullName());
                if (callerCalls == null) {
                    callerCalls = new HashMap<>();
                }
                String callTime = simpleDateFormat.format(callObject.getTime());
                if (!dates.contains(callTime)) {
                    dates.add(callTime);
                }
                Integer totalDateCalls = callerCalls.get(callTime);
                if (totalDateCalls == null) {
                    totalDateCalls = 0;
                }
                totalDateCalls = totalDateCalls + 1;
                callerCalls.put(callTime, totalDateCalls);
                callsMap.put(callerObject.getFullName(), callerCalls);
            }
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("calls")).append(COMMA);
            for (String date : dates) {
                writer.append(date).append(COMMA);
            }
            writer.append("\n");
            for (String name : callsMap.keySet()) {
                writer.append(name).append(COMMA);
                Map<String, Integer> callerCalls = callsMap.get(name);
                for (String date : dates) {
                    Integer totalCallsForDate = callerCalls.get(date);
                    if (totalCallsForDate == null) {
                        totalCallsForDate = 0;
                    }
                    writer.append(String.valueOf(totalCallsForDate)).append(COMMA);
                }
                writer.append("\n");
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("callsReport", e);
        }
    }

    public List<NotVotedSupporterReportObject> supportersNotYetVotedData(int candidateOid) {
        List<NotVotedSupporterReportObject> notVoted = null;
        try {
            notVoted = (List<NotVotedSupporterReportObject>) getReportData(candidateOid, REPORT_TYPE_SUPPORTERS_NOT_YET_VOTED);
            if (notVoted == null) {
                List<VoterObject> notVotedSupporters = generalManager.getNotVotedSupporters(candidateOid);
                if (!notVotedSupporters.isEmpty()) {
                    notVoted = new ArrayList<>();
                    for (VoterObject supporter : notVotedSupporters) {
                        notVoted.add(new NotVotedSupporterReportObject(supporter));
                    }
                }
                putReportData(candidateOid, REPORT_TYPE_SUPPORTERS_NOT_YET_VOTED, notVoted);
            }
        } catch (Exception e) {
            LOGGER.error("supportersNotYetVotedData", e);
        }
        return notVoted;
    }

    public void supportersNotYetVotedReport(File csv,int candidateOid, HttpServletResponse response) {
        try {

            List<NotVotedSupporterReportObject> notVoted = supportersNotYetVotedData(candidateOid);
            FileWriter writer = new FileWriter(csv);
            writer.append(Utils.getTranslation("name")).append(COMMA).
                    append(Utils.getTranslation("phone")).append(COMMA).
                    append(Utils.getTranslation("address")).append(COMMA).
                    append(BREAK);
            for (NotVotedSupporterReportObject supporter : notVoted) {
                writer.append(supporter.printAsCsvRow(Arrays.asList("fullName", "phone", "address")));
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("supportersNotYetVotedReport", e);
        }
    }

    public List<SupporterNotVotedByActivistsReportObject> supportersNotYetVoteByActivistsData(int candidateOid) {
        List<SupporterNotVotedByActivistsReportObject> notVoted = null;
        try {
            notVoted = (List<SupporterNotVotedByActivistsReportObject>) getReportData(candidateOid, REPORT_TYPE_SUPPORTERS_NOT_YET_VOTED_BY_ACTIVISTS);
            if (notVoted == null) {
                Map<Integer, List<VoterObject>> activistToVotersMap = generalManager.getActivistVotersMap(candidateOid);
                if (!activistToVotersMap.isEmpty()) {
                    notVoted = new ArrayList<>();
                    for (Integer activistOid : activistToVotersMap.keySet()) {
                        ActivistObject activistObject = generalManager.loadObject(ActivistObject.class, activistOid);
                        List<VoterObject> voterObjects = activistToVotersMap.get(activistOid);
                        for (VoterObject voterObject : voterObjects) {
                            notVoted.add(new SupporterNotVotedByActivistsReportObject(voterObject, activistObject));
                        }
                    }
                }
                putReportData(candidateOid, REPORT_TYPE_SUPPORTERS_NOT_YET_VOTED_BY_ACTIVISTS, notVoted);
            }
        } catch (Exception e) {
            LOGGER.error("supportersNotYetVoteByActivistsData", e);
        }
        return notVoted;

    }

    public void supportersNotYetVoteByActivistsReport(File csv,int candidateOid, HttpServletResponse response) {
        try {
            List<SupporterNotVotedByActivistsReportObject> notVoted = supportersNotYetVoteByActivistsData(candidateOid);
            FileWriter writer = new FileWriter(csv);
            writer.append(Utils.getTranslation("activist.name")).append(COMMA).
                    append(Utils.getTranslation("activist.phone")).append(COMMA).
                    append(Utils.getTranslation("supporter.id")).append(COMMA).
                    append(Utils.getTranslation("voter.name")).append(COMMA).
                    append(Utils.getTranslation("voter.phone")).append(COMMA).
                    append(BREAK);
            for (SupporterNotVotedByActivistsReportObject supporter : notVoted) {
                writer.append(supporter.printAsCsvRow(Arrays.asList("activistName", "activistPhone", "supporterId", "supporterFullName", "supporterPhone")));
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("supportersNotYetVoteByActivistsReport", e);
        }
    }

    public List<NotVotedSupporterReportObject> supportersNotYetVotedWithNoActivistData(int candidateOid) {
        List<NotVotedSupporterReportObject> supporters = null;
        try {
            supporters = (List<NotVotedSupporterReportObject>) getReportData(candidateOid, REPORT_TYPE_SUPPORTERS_NOT_YET_VOTED_WITH_NO_ACTIVIST);
            if (supporters == null) {
                List<VoterObject> notVoted = generalManager.getNotVotedSupportersWithNoActivist(candidateOid);
                if (!notVoted.isEmpty()) {
                    supporters = new ArrayList<>();
                    for (VoterObject supporter : notVoted) {
                        supporters.add(new NotVotedSupporterReportObject(supporter));
                    }
                }
                putReportData(candidateOid, REPORT_TYPE_SUPPORTERS_NOT_YET_VOTED_WITH_NO_ACTIVIST, supporters);
            }
        } catch (Exception e) {
            LOGGER.error("supportersNotYetVotedWithNoActivistData", e);
        }
        return supporters;
    }

    public void supportersNotYetVotedWithNoActivistReport(int candidateOid, HttpServletResponse response) {
        try {
            List<NotVotedSupporterReportObject> notVoted = supportersNotYetVotedWithNoActivistData(candidateOid);
            File csv = new File(csvFileName(candidateOid, "supporters.not.voted.with.no.activist"));
            FileWriter writer = new FileWriter(csv);
            writer.append(Utils.getTranslation("name")).append(COMMA).
                    append(Utils.getTranslation("voter.id")).append(COMMA).
                    append(Utils.getTranslation("voter.phone")).append(COMMA).
                    append(BREAK);
            for (NotVotedSupporterReportObject supporter : notVoted) {
                writer.append(supporter.printAsCsvRow(Arrays.asList("fullName", "supporterId", "phone")));
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("supportersNotYetVotedWithNoActivistReport", e);
        }
    }

    public List<ActivistObject> activistsData(int candidateOid) {
        List<ActivistObject> activistObjects = null;
        try {
            activistObjects = (List<ActivistObject>) getReportData(candidateOid, REPORT_TYPE_ACTIVISTS);
            if (activistObjects == null) {
                activistObjects = generalManager.getListByAdminOid(ActivistObject.class, candidateOid);
                putReportData(candidateOid, REPORT_TYPE_ACTIVISTS, activistObjects);
            }
        } catch (Exception e) {
            LOGGER.error("activistsData", e);
        }
        return activistObjects;
    }

    public void activistsReport(File csv,int candidateOid, HttpServletResponse response) {
        try {
            List<ActivistObject> activistObjects = generalManager.getListByAdminOid(ActivistObject.class, candidateOid);
            for (ActivistObject activistObject : activistObjects) {
                activistObject.setSupportersCount(generalManager.countActivistSupporters(activistObject.getOid()));
            }
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("first.name")).append(COMMA).
                    append(Utils.getTranslation("last.name")).append(COMMA).
                    append(Utils.getTranslation("phone")).append(COMMA).
                    append(Utils.getTranslation("direct.supporters.count")).append(COMMA).
                    append(Utils.getTranslation("is.activists.leader")).append(COMMA).
                    append(Utils.getTranslation("supporters.and.subsupporters.count")).append(COMMA).
                    append(BREAK);
            for (ActivistObject activistObject : activistObjects) {
                writer.append(getIfExists(activistObject.getFirstName())).append(COMMA).
                        append(getIfExists(activistObject.getLastName())).append(COMMA).
                        append(getIfExists(activistObject.getPhone())).append(COMMA).
                        append(String.valueOf((activistObject.getSupportersCount()))).append(COMMA).
                        append(Utils.getTranslation(generalManager.isActivistManager(activistObject.getOid()) ? "yes" : "no")).append(COMMA).
                        append(String.valueOf(generalManager.countSlavesSupporters(activistObject.getOid()) + activistObject.getSupportersCount())).append(COMMA).
                        append(BREAK);
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("activistsReport", e);
        }
    }


    public String csvOwnerFileName(int owner, String reportName, final String pathName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        Path path = Paths.get(pathName);
        validatePath(path);
        return String.format("%s_%s_%s_%s.csv", pathName, reportName, owner, simpleDateFormat.format(new Date()));
    }

    public void activistVotersReport(File csv,int activistOid, HttpServletResponse response) {
        try {
            List<VoterObject> voters = generalManager.getVotersByActivistOid(activistOid);
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

            writer.append(Utils.getTranslation("name")).append(COMMA).
                    append(Utils.getTranslation("phone")).append(COMMA).
                    append(Utils.getTranslation("relation")).append(COMMA).
                    append(Utils.getTranslation("voter.id")).append(COMMA).
                    append(Utils.getTranslation("address")).append(COMMA).
                    append(Utils.getTranslation("birth.date")).append(COMMA).
                    append(BREAK);
            for (VoterObject voterObject : voters) {
                writer.append(getIfExists(voterObject.getFullName())).append(COMMA).
                        append(getIfExists(voterObject.getPhone())).append(COMMA).
                        append(getIfExists(voterObject.getRelationToActivist())).append(COMMA).
                        append(getIfExists(voterObject.getVoterId())).append(COMMA).
                        append(getIfExists(voterObject.getAddress())).append(COMMA).
                        append(getIfExists(voterObject.getAgeInfo())).append(COMMA).
                        append(BREAK);
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("activistVotersReport", e);
        }
    }

    private String getIfExists(String text) {
        String value = MINUS;
        if (Utils.hasText(text)) {
            value = text.replace(COMMA, EMPTY);
        }
        return value;
    }

    public void tasksReport(File csv,int candidateOid, Integer type, HttpServletResponse response) {
        try {
            List<SupporterActionObject> tasks = generalManager.getOpenSupporterActions(candidateOid, null, true, type == SUPPORTER_ACTION_TYPE_ALL ? null : type);
            tasks.sort((o1, o2) -> o2.getCreationDate().compareTo(o1.getCreationDate()));
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("date")).append(COMMA).
                    append(Utils.getTranslation("supporter.name")).append(COMMA).
                    append(Utils.getTranslation("task.type")).append(COMMA).
                    append(Utils.getTranslation("cell.phone")).append(COMMA).
                    append(Utils.getTranslation("home.phone")).append(COMMA).
                    append(Utils.getTranslation("extra.phone")).append(COMMA).
                    append(Utils.getTranslation("address")).append(COMMA).
                    append(BREAK);
            for (SupporterActionObject task : tasks) {
                writer.append(simpleDateFormat.format(task.getCreationDate())).append(COMMA).
                        append(task.getSupporter().getFullName()).append(COMMA).
                        append(Utils.getTranslation(task.getPrintableType())).append(COMMA).
                        append(getIfExists(task.getSupporter().getPhone())).append(COMMA).
                        append(getIfExists(task.getSupporter().getHomePhone())).append(COMMA).
                        append(getIfExists(task.getSupporter().getExtraPhone())).append(COMMA).
                        append(getIfExists(task.getSupporter().getAddress())).append(COMMA).
                        append(BREAK);
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("tasksReport", e);
        }
    }

    public void callersReport(File csv,int candidateOid, HttpServletResponse response, String startDate, String endDate) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("name")).append(COMMA).
                    append(Utils.getTranslation("total.calls")).append(COMMA).
                    append(Utils.getTranslation("answered.calls")).append(COMMA).
                    append(Utils.getTranslation("unanswered.calls")).append(COMMA).
                    append(Utils.getTranslation("answered.calls.percent")).append(COMMA).
                    append(Utils.getTranslation("total.changed.to.supporters")).append(COMMA).
                    append(Utils.getTranslation("total.changed.to.not.supporting")).append(COMMA).
                    append(Utils.getTranslation("total.answered.unchanged")).append(COMMA).
                    append(Utils.getTranslation("percent.changed.to.supporters")).append(COMMA).
                    append(Utils.getTranslation("total.wants.billboard")).append(COMMA).
                    append(Utils.getTranslation("total.wants.to.volunteer")).append(COMMA).
                    append(Utils.getTranslation("total.wants.to.deliver.supporters.list")).append(COMMA).
                    append(BREAK);
            List<VoterCallObject> voterCallObjects = generalManager.getCallsByAdminOid(candidateOid, startDate, endDate, false);
            if (voterCallObjects != null) {
                Map<Integer, CallerStatisticsObject> callersStatisticsMap = new HashMap<>();
                for (VoterCallObject voterCallObject : voterCallObjects) {
                    Integer callerOid = voterCallObject.getCallerObject().getOid();
                    CallerStatisticsObject callerStatisticsObject = callersStatisticsMap.get(callerOid);
                    if (callerStatisticsObject == null) {
                        callerStatisticsObject = new CallerStatisticsObject(voterCallObject.getCallerObject());
                        callersStatisticsMap.put(callerOid, callerStatisticsObject);
                    }
                    callerStatisticsObject.updateStatistics(voterCallObject);
                }
                List<SupporterActionObject> tasks = generalManager.getOpenSupporterActions(candidateOid, null, false);
                if (tasks != null) {
                    for (SupporterActionObject task : tasks) {
                        Integer callerOid = task.getInitiatorOid();
                        CallerStatisticsObject callerStatisticsObject = callersStatisticsMap.get(callerOid);
                        if (callerStatisticsObject != null) {
                            callerStatisticsObject.updateTask(task);
                        }

                    }
                }


                for (CallerStatisticsObject callerStatisticsObject : callersStatisticsMap.values()) {
                    writer.append(
                            getIfExists(callerStatisticsObject.getCallerObject().getFullName())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalCalls())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalAnsweredCalls())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalUnansweredCalls())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getPercentAnsweredCalls())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalChangedToSupporters())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalChangedToNotSupporting())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalUnchanged())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getPercentChangedToSupporters())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalBillboardRequests())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalWantsToVolunteer())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalWantsToDeliverSupportersList())).append(COMMA).
                            append(BREAK);

                }

            }

            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("callersReport", e);
        }
    }

    public void callsMessagesReport(File csv,int adminOid, String startDate, String endDate, HttpServletResponse response) {
        try {
            List<VoterCallObject> voterCallObjects = generalManager.getCallsByAdminOid(adminOid, startDate, endDate, true);
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("date")).append(COMMA).
                    append(Utils.getTranslation("caller.name")).append(COMMA).
                    append(Utils.getTranslation("voter.name")).append(COMMA).
                    append(Utils.getTranslation("previous.status")).append(COMMA).
                    append(Utils.getTranslation("new.status")).append(COMMA).
                    append(Utils.getTranslation("message")).append(COMMA).
                    append(BREAK);

            if (voterCallObjects != null) {
                for (VoterCallObject voterCallObject : voterCallObjects) {
                    writer.append(voterCallObject.getTime().toString()).append(COMMA).
                            append(voterCallObject.getCallerObject().getFullName()).append(COMMA).
                            append(voterCallObject.getVoterObject().getFullName()).append(COMMA).
                            append(Utils.getPrintableSupportStatus(voterCallObject.getPreviousStatus())).append(COMMA).
                            append(getIfExists(Utils.getPrintableSupportStatus(voterCallObject.getNewStatus()))).append(COMMA).
                            append(getIfExists(voterCallObject.getComment())).append(COMMA).
                            append(BREAK);

                }
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("callsMessagesReport", e);
        }
    }

    public void ballotBoxesReport(File csv,AdminUserObject adminUserObject, HttpServletResponse response) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Map<String, Object> params = new HashMap<>();
            params.put(PARAM_ADMIN_OID, adminUserObject.getOid());
            JSONObject data = ServicesApi.requestFromVotersImMemoryService("candidate-polling-stations-stats", params, PARAM_BALLOT_BOXES);
            JSONArray pollingStationsData = data.getJSONArray(PARAM_POLLING_STATIONS_DATA);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            List<BallotBoxObject> ballotBoxObjects = generalManager.getBallotBoxesByCampaign(adminUserObject.getCampaignObject().getOid());
            writer.append(Utils.getTranslation("number")).append(COMMA).
                    append(Utils.getTranslation("address")).append(COMMA).
                    append(Utils.getTranslation("place")).append(COMMA).
                    append(Utils.getTranslation("disabled.voting.arrangements")).append(COMMA).
                    append(Utils.getTranslation("disabled.access")).append(COMMA).
                    append(Utils.getTranslation("observers")).append(COMMA).
                    append(Utils.getTranslation("voters.count")).append(COMMA).
                    append(Utils.getTranslation("supporters.count")).append(COMMA).
                    append(Utils.getTranslation("voted.round1.count")).append(COMMA).
                    append(Utils.getTranslation("voted.round2.count")).append(COMMA).
                    append(BREAK);
            List<ObserverBallotBoxMapObject> observerBallotBoxMapObjects = generalManager.getListByAdminOid(ObserverBallotBoxMapObject.class, adminUserObject.getOid());
            Map<Integer, Set<ObserverObject>> observerBallotBoxMapObjectMap = new HashMap<>();
            for (ObserverBallotBoxMapObject observerBallotBoxMapObject : observerBallotBoxMapObjects) {
                Set<ObserverObject> mapObjectList = observerBallotBoxMapObjectMap.computeIfAbsent(observerBallotBoxMapObject.getBallotBoxObject().getOid(), k -> new HashSet<>());
                mapObjectList.add(observerBallotBoxMapObject.getObserverObject());
            }
            for (BallotBoxObject ballotBoxObject : ballotBoxObjects) {
                int totalVoters=0;
                int totalSupporters=0;
                double totalVotedRound1=0;
                double totalVotedRound2=0;
                for (int i = 0; i < pollingStationsData.length(); i++) {
                    JSONObject pollingStation = pollingStationsData.getJSONObject(i);
                    if(pollingStation.getInt(PARAM_OID)==ballotBoxObject.getOid()){
                     totalVoters=pollingStation.getInt(PARAM_VOTERS_COUNT);
                     totalSupporters=pollingStation.getInt(PARAM_SUPPORTERS_COUNT);
                     totalVotedRound1=pollingStation.getInt(PARAM_VOTED_ROUND_1_COUNT);
                     totalVotedRound2=pollingStation.getInt(PARAM_VOTED_VOTERS_COUNT);
                    }
                }
                Set<ObserverObject> observerObjects = observerBallotBoxMapObjectMap.get(ballotBoxObject.getOid());
                String observers = EMPTY;
                if (observerObjects == null || observerObjects.isEmpty()) {
                    observers = MINUS;
                } else {
                    StringBuilder observersNamesBuilder = new StringBuilder();
                    for (ObserverObject observerObject : observerObjects) {
                        observersNamesBuilder.append(observerObject.getFullName()).append("; ");
                    }
                    observers = observersNamesBuilder.toString();
                }
                writer.append(String.valueOf(ballotBoxObject.getNumberWithDot())).append(COMMA).
                        append(ballotBoxObject.getAddress()).append(COMMA).
                        append(ballotBoxObject.getPlace()).append(COMMA).
                        append(Utils.getTranslation(ballotBoxObject.isDisabledVotingArrangements() ? "yes" : "no")).append(COMMA).
                        append(Utils.getTranslation(ballotBoxObject.isDisabledAccess() ? "yes" : "no")).append(COMMA).
                        append(observers).append(COMMA).
                        append(String.valueOf(totalVoters)).append(COMMA).
                        append(String.valueOf(totalSupporters)).append(COMMA).
                        append(String.valueOf((int)totalVotedRound1)).append(COMMA).
                        append(String.valueOf((int)totalVotedRound2)).append(COMMA).
                        append(BREAK);
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("ballotBoxesReport", e);
        }
    }




    public void supportStatusCountChanges(File csv,int adminOid, HttpServletResponse response) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            List<CandidateDataDailyObject> candidateDataDailyObjects = generalManager.getListByAdminOid(CandidateDataDailyObject.class, adminOid);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("date")).append(COMMA);
            writer.append(Utils.getTranslation("change.in.supporters.count")).append(COMMA);
            writer.append(Utils.getTranslation("change.in.unverified.supporters.count")).append(COMMA);
            writer.append(Utils.getTranslation("change.in.unknown.support.status.count")).append(COMMA);
            writer.append(Utils.getTranslation("change.in.not.supporting.count")).append(COMMA);
            writer.append(BREAK);
            for (int i = 0; i < candidateDataDailyObjects.size() - 1; i++) {
                CandidateDataDailyObject previousDay = candidateDataDailyObjects.get(i);
                CandidateDataDailyObject currentDay = candidateDataDailyObjects.get(i + 1);
                writer.append(Utils.formatDate(currentDay.getDate(), "dd-MM-yyyy")).append(COMMA);
                writer.append(Utils.stringWithSign(currentDay.getSupportingCount() - previousDay.getSupportingCount())).append(COMMA);
                writer.append(Utils.stringWithSign(currentDay.getUnverifiedSupportingCount() - previousDay.getUnverifiedSupportingCount())).append(COMMA);
                writer.append(Utils.stringWithSign(currentDay.getUnknownSupportCount() - previousDay.getUnknownSupportCount())).append(COMMA);
                writer.append(Utils.stringWithSign(currentDay.getNotSupportingCount() - previousDay.getNotSupportingCount())).append(COMMA);
                writer.append(BREAK);
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("votersByHouseNumberReport", e);
        }
    }

    public void headActivists(File csv,int adminOid, HttpServletResponse response, Map<Integer, HeadActivistExtendedData> headActivistDataList, Map<Integer, ActivistObject> activistsMap) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            for (Integer masterOid : headActivistDataList.keySet()) {
                ActivistObject master = activistsMap.get(masterOid);
                if (master != null) {
                    writer.append(Utils.getTranslation("head.activist.name")).append(COLON).append(COMMA).append(master.getFullName()).append(BREAK);
                    writer.append(Utils.getTranslation("phone")).append(COLON).append(COMMA).append(master.getPhone()).append(BREAK);
                    HeadActivistExtendedData headActivistExtendedData = headActivistDataList.get(masterOid);
                    writer.append(Utils.getTranslation("own.supporters.count")).append(COLON).append(COMMA).append(String.valueOf(headActivistExtendedData.getOwnSupportersCount())).append(BREAK);
                    writer.append(Utils.getTranslation("activists.count")).append(COLON).append(COMMA).append(String.valueOf(headActivistExtendedData.getSlavesCount())).append(BREAK);
                    writer.append(Utils.getTranslation("total.supporters.count")).append(COLON).append(COMMA).append(String.valueOf(headActivistExtendedData.getTotalSupportersCount())).append(BREAK);
                    writer.append(Utils.getTranslation("unique.supporters.count")).append(COLON).append(COMMA).append(String.valueOf(headActivistExtendedData.getUniqueSupportersCount())).append(BREAK);
                    Set<ActivistObject> slaves = headActivistExtendedData.getSlaves();
                    if (slaves != null && !slaves.isEmpty()) {
                        writer.append(BREAK);
                        writer.append(Utils.getTranslation("activists.data")).append(COLON).append(SPACE).append(BREAK);
                        writer.append(Utils.getTranslation("full.name")).append(COMMA).append(Utils.getTranslation("phone")).append(COMMA).append(Utils.getTranslation("supporters.count")).append(BREAK);
                        for (ActivistObject slave : slaves) {
                            writer.append(slave.getFullName()).append(COMMA).append(slave.getPhone()).append(COMMA).append(String.valueOf(slave.getSupportersCount())).append(BREAK);
                        }
                    }
                }
                writer.append(BREAK).append(BREAK);
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("headActivists", e);
        }
    }


    public void ballotBoxObserverForm(List<VoterObject> voterObjects, int adminOid, BallotBoxObject ballotBoxObject) {
        try {
            File csv = new File(String.format("%s\\%s_%s.csv", getFormsPath(adminOid), ballotBoxObject.getNumberWithDot(), ballotBoxObject.getCityObject().getName().replace("\"", EMPTY)));
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(String.format("%s: %s, %s, (%s)", Utils.getTranslation("polling.station.number"),
                    ballotBoxObject.getNumberWithDot(), ballotBoxObject.getPlace(), ballotBoxObject.getAddress())).append(BREAK);
            ballotBoxObserverForm(writer, voterObjects);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            LOGGER.error("ballotBoxVotersReport", e);
        }
    }

    public String getFormsPath (int adminOid) {
        String formsPath = String.format("%s\\%s\\forms", PATH_TO_ELECTOR_DIRECTORY, adminOid);
        Path path = Paths.get(formsPath);
        validatePath(path);
        return formsPath;

    }

    private void ballotBoxObserverForm(Writer writer, List<VoterObject> voterObjects) throws Exception {
        boolean municipal = !voterObjects.isEmpty() && (voterObjects.get(0).getCampaignType() == PARAM_CAMPAIGN_TYPE_MUNICIPAL || voterObjects.get(0).getCampaignType() == PARAM_CAMPAIGN_TYPE_GENERAL_ELECTION);
        if (municipal) {
            writer.append(Utils.getTranslation("voter.number")).append(COMMA);
        }
        writer.
                append(Utils.getTranslation("voter.id")).append(COMMA).
                append(Utils.getTranslation("first.name")).append(COMMA).
                append(Utils.getTranslation("last.name")).append(COMMA).
                append(Utils.getTranslation("voted")).append(COMMA).
                append(BREAK);
        for (VoterObject voterObject : voterObjects) {
            if (municipal) {
                writer.append(getIfExists(String.valueOf(((CampaignVoterObject)voterObject.getBaseCampaignVoterObject()).getVoterNumber()))).append(COMMA);
            }
            writer.
                    append(getIfExists(voterObject.getVoterId())).append(COMMA).
                    append(getIfExists(voterObject.getFirstName())).append(COMMA).
                    append(getIfExists(voterObject.getLastName())).append(COMMA).
                    append(BREAK);
        }
    }

    public void zipObserversForms(int adminOid, HttpServletResponse response) {
        try {
            String destination = String.format("%s\\zipped\\%s", PATH_TO_ELECTOR_DIRECTORY, adminOid);
            Path path = Paths.get(destination);
            validatePath(path);
            String zipFile = String.format("%s\\forms.zip", destination);
            Utils.zipFolder(zipFile, getFormsPath(adminOid));
            IOUtils.sendZipInResponse(response, new File(zipFile));
        } catch (Exception e) {
            LOGGER.error("zipObserversForms", e);
        }
    }

    public void callsByCallers(File csv,Map<Integer, List<VoterCallObject>> callsByCallersMap, Map<Integer, CallerObject> callersMap, int adminOid, HttpServletResponse response) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
            for (Integer callerOid : callsByCallersMap.keySet()) {
                CallerObject callerObject = callersMap.get(callerOid);
                if (callerObject != null) {
                    writer.append(Utils.getTranslation("caller.name")).append(COMMA).append(callerObject.getFullName()).append(BREAK);
                    writer.append(Utils.getTranslation("caller.phone.number")).append(COMMA).append(callerObject.getPhone()).append(BREAK);
                    writer.append(Utils.getTranslation("calls")).append(BREAK);
                    writer.append(EMPTY).append(COMMA).append(Utils.getTranslation("date")).append(COMMA).append(Utils.getTranslation("voter.id")).
                            append(COMMA).append(Utils.getTranslation("first.name")).append(COMMA).append(Utils.getTranslation("last.name")).append(COMMA).
                            append(Utils.getTranslation("call.answered")).append(COMMA).append(Utils.getTranslation("unanswered.reason")).append(COMMA).
                            append(Utils.getTranslation("previous.status")).append(COMMA).append(Utils.getTranslation("new.status")).append(COMMA)
                            .append(Utils.getTranslation("comment")).append(COMMA).append(BREAK);
                    List<VoterCallObject> calls = callsByCallersMap.get(callerOid);
                    int count = 1;
                    for (VoterCallObject callObject : calls) {
                        writer.append(String.valueOf(count)).append(COMMA).append(simpleDateFormat.format(callObject.getTime())).append(COMMA)
                                .append(callObject.getVoterObject().getVoterId()).append(COMMA).append(callObject.getVoterObject().getFirstName()).append(COMMA)
                                .append(callObject.getVoterObject().getLastName()).append(COMMA).append(Utils.getTranslation(callObject.isAnswered() ? "yes" : "no")).append(COMMA);
                        writer.append(Utils.unansweredReasonToText(callObject.getUnansweredReason())).append(COMMA).
                                append(Utils.getPrintableSupportStatus(callObject.getPreviousStatus())).
                                append(COMMA).append(Utils.getPrintableSupportStatus(callObject.getNewStatus()))
                                .append(COMMA).append(callObject.getComment()!=null ? callObject.getComment() : MINUS).append(BREAK);
                        count++;
                    }
                    writer.append(BREAK).append(BREAK);

                }
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("callsByCallers", e);
        }
    }

    public void observers(File csv,int adminOid, HttpServletResponse response) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("name")).append(COMMA);
            writer.append(Utils.getTranslation("polling.stations")).append(COMMA);
            writer.append(BREAK);
            List<ObserverObject> observerObjects = generalManager.getListByAdminOid(ObserverObject.class, adminOid);
            for (ObserverObject observerObject : observerObjects) {
                writer.append(observerObject.getFullName()).append(COMMA);
                List<BallotBoxObject> ballotBoxObjects = generalManager.getBallotBoxesByObserver(observerObject.getOid());
                for (BallotBoxObject ballotBoxObject : ballotBoxObjects) {
                    writer.append(ballotBoxObject.getUiDescription()).append(COMMA);
                }
                writer.append(BREAK);
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("observers", e);
        }
    }


    public void pollingStationsGeneralData (File csv,int adminOid, HttpServletResponse response) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("serial.number")).append(COMMA).append(Utils.getTranslation("number")).append(COMMA)
                    .append(Utils.getTranslation("address")).append(COMMA)
                    .append(Utils.getTranslation("block.name")).append(COMMA).append(Utils.getTranslation("voters.count")).append(COMMA).append(Utils.getTranslation("supporters.count")).append(BREAK);
            Map<String, Object> params = new HashMap<>();
            params.put(PARAM_ADMIN_OID, adminOid);
            JSONObject data = ServicesApi.requestFromVotersImMemoryService("candidate-polling-stations-stats", params, PARAM_BALLOT_BOXES);
            JSONArray pollingStationsData = data.getJSONArray(PARAM_POLLING_STATIONS_DATA);
            for (int i = 0; i < pollingStationsData.length(); i++) {
                JSONObject pollingStation = pollingStationsData.getJSONObject(i);
                writer.append(String.valueOf(i + 1)).append(COMMA).append(getIntAsStringJson(pollingStation, PARAM_NUMBER))
                        .append(COMMA).append(pollingStation.getString(PARAM_ADDRESS)).append(COMMA)
                        .append(pollingStation.getString(PARAM_BLOCK_NAME)).append(COMMA)
                        .append(getIntAsStringJson(pollingStation, PARAM_VOTERS_COUNT)).append(COMMA)
                        .append(getIntAsStringJson(pollingStation, PARAM_SUPPORTERS_COUNT)).append(BREAK);
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("pollingStationsGeneralData", e);
        }
    }

    private String getIntAsStringJson (JSONObject jsonObject, String key) {
        String value = MINUS;
        try {
            int number = jsonObject.getInt(key);
            value = String.valueOf(number);
        } catch (Exception e) {
            LOGGER.error(String.format("error extracting data json, %s", jsonObject.toString()), e);
        }
        return value;
    }

    public void electionDayCallersReport(File csv,int candidateOid, HttpServletResponse response) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("name")).append(COMMA).
                    append(Utils.getTranslation("total.calls")).append(COMMA).
                    append(Utils.getTranslation("answered.calls")).append(COMMA).
                    append(Utils.getTranslation("unanswered.calls")).append(COMMA).
                    append(Utils.getTranslation("answered.calls.percent")).append(COMMA).
                    append(BREAK);
            List<VoterElectionDayCallObject> voterCallObjects = generalManager.getElectionDayCallsByAdminOid(candidateOid);
            if (voterCallObjects != null) {
                Map<Integer, CallerStatisticsObject> callersStatisticsMap = new HashMap<>();
                for (VoterElectionDayCallObject voterCallObject : voterCallObjects) {
                    Integer callerOid = voterCallObject.getCallerObject().getOid();
                    CallerStatisticsObject callerStatisticsObject = callersStatisticsMap.get(callerOid);
                    if (callerStatisticsObject == null) {
                        callerStatisticsObject = new CallerStatisticsObject(voterCallObject.getCallerObject());
                        callersStatisticsMap.put(callerOid, callerStatisticsObject);
                    }
                    callerStatisticsObject.updateStatistics(voterCallObject);
                }
                for (CallerStatisticsObject callerStatisticsObject : callersStatisticsMap.values()) {
                    writer.append(
                            getIfExists(callerStatisticsObject.getCallerObject().getFullName())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalCalls())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalAnsweredCalls())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getTotalUnansweredCalls())).append(COMMA).
                            append(String.valueOf(callerStatisticsObject.getPercentAnsweredCalls())).append(COMMA).
                            append(BREAK);
                }
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("electionDayCallersReport", e);
        }
    }

    public void electionDayCallsByCallers(File csv,Map<Integer, List<VoterElectionDayCallObject>> callsByCallersMap, Map<Integer, CallerObject> callersMap, int adminOid, HttpServletResponse response) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
            for (Integer callerOid : callsByCallersMap.keySet()) {
                CallerObject callerObject = callersMap.get(callerOid);
                if (callerObject != null) {
                    writer.append(Utils.getTranslation("caller.name")).append(COMMA).append(callerObject.getFullName()).append(BREAK);
                    writer.append(Utils.getTranslation("caller.phone.number")).append(COMMA).append(callerObject.getPhone()).append(BREAK);
                    writer.append(Utils.getTranslation("calls")).append(BREAK);
                    writer.append(EMPTY)
                            .append(COMMA)
                            .append(Utils.getTranslation("date")).append(COMMA)
                            .append(Utils.getTranslation("voter.id"))
                            .append(COMMA).append(Utils.getTranslation("first.name"))
                            .append(COMMA).append(Utils.getTranslation("last.name")).append(COMMA)
                            .append(Utils.getTranslation("call.answered")).append(COMMA)
                            .append(Utils.getTranslation("comment")).append(COMMA).append(BREAK);
                    List<VoterElectionDayCallObject> calls = callsByCallersMap.get(callerOid);
                    int count = 1;
                    for (VoterElectionDayCallObject callObject : calls) {
                        writer.append(
                                String.valueOf(count)).append(COMMA)
                                .append(simpleDateFormat.format(callObject.getTime())).append(COMMA)
                                .append(callObject.getVoterObject().getVoterId()).append(COMMA)
                                .append(callObject.getVoterObject().getFirstName()).append(COMMA)
                                .append(callObject.getVoterObject().getLastName()).append(COMMA)
                                .append(Utils.getTranslation(callObject.isAnswered() ? "yes" : "no")).append(COMMA)
                                .append(COMMA).append(callObject.getComment()).append(BREAK);
                        count++;
                    }
                    writer.append(BREAK).append(BREAK);

                }
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);
        } catch (Exception e) {
            LOGGER.error("electionDayCallsByCallers", e);
        }
    }

    public void phonesList(int adminOid, List<String> phones, String text) {
        try {
            File csv = new File(csvOwnerFileName(adminOid, "phones", PATH_TO_VOTERS_FILES));
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("phone")).append(BREAK);
            for (String phone : phones) {
                writer.append(phone).append(BREAK);
            }
            String recipient = ConfigUtils.getConfig(ConfigEnum.info_email_address, "tzur@elector.co.il");
            com.elector.Objects.General.EmailObject emailObject = new EmailObject("shai@elector.co.il", "shai@elector.co.il", recipient,
                    "SMS LIST","TEXT: " + BREAK + text + BREAK  + BREAK + "DOWNLOAD" + BREAK + String.format("%s_%s_%s_%s","https://elector.co.il/api/phones-list?fileName=", "phones", adminOid, new SimpleDateFormat("dd-MM-yy").format(new Date())), recipient);
            emailUtils.sendEmailViaGmail(emailObject);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            LOGGER.error("phonesList", e);
        }
    }

    public void nextVotersToCallReport(File csv, JSONArray data, HttpServletResponse response) {
        try {
            OutputStream outputStream = new FileOutputStream(csv);
            csvFileLang(outputStream);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.append(Utils.getTranslation("first.name")).append(COMMA);
            writer.append(Utils.getTranslation("last.name")).append(COMMA);
            writer.append(Utils.getTranslation("voter.id")).append(COMMA);
            writer.append(Utils.getTranslation("voter.number")).append(COMMA);
            writer.append(Utils.getTranslation("support.status")).append(COMMA);
            writer.append(Utils.getTranslation("phone")).append(COMMA);
            writer.append(Utils.getTranslation("home.phone")).append(COMMA);
            writer.append(Utils.getTranslation("extra.phone")).append(COMMA);
            writer.append(Utils.getTranslation("total.handling.acitivsts")).append(COMMA);
            writer.append(Utils.getTranslation("last.call.time")).append(COMMA);
            writer.append(Utils.getTranslation("ballot.box.number")).append(COMMA);
            writer.append(Utils.getTranslation("ballot.box.address")).append(COMMA);
            writer.append(BREAK);
            for (int i = 0; i < data.length(); i++) {
                JSONObject voterJson = data.getJSONObject(i);
                writer.append(voterJson.getString(PARAM_FIRST_NAME)).append(COMMA);
                writer.append(voterJson.getString(PARAM_LAST_NAME)).append(COMMA);
                writer.append(voterJson.getString(PARAM_VOTER_ID)).append(COMMA);
                writer.append(String.valueOf(voterJson.getInt(PARAM_VOTER_NUMBER))).append(COMMA);
                writer.append(Utils.getPrintableSupportStatus(voterJson.getInt(PARAM_SUPPORT_STATUS))).append(COMMA);
                writer.append(voterJson.getString(PARAM_PHONE)).append(COMMA);
                writer.append(voterJson.getString(PARAM_HOME_PHONE)).append(COMMA);
                writer.append(voterJson.getString(PARAM_EXTRA_PHONE)).append(COMMA);
                writer.append(String.valueOf(voterJson.getInt(PARAM_TOTAL_HANDLING_ACTIVISTS))).append(COMMA);
                writer.append(voterJson.getString(PARAM_LAST_CALL_TIME)).append(COMMA);
                writer.append(String.valueOf(voterJson.getInt(PARAM_BALLOT_BOX_NUMBER))).append(COMMA);
                writer.append(voterJson.getString(PARAM_BALLOT_BOX_ADDRESS)).append(COMMA);
                writer.append(BREAK);
            }
            writer.flush();
            writer.close();
            IOUtils.sendFileInResponse(response, csv);

        } catch (Exception e) {
            LOGGER.error("nextVotersToCallReport", e);
        }
    }

}
