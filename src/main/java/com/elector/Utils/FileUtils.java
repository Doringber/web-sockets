package com.elector.Utils;

import com.elector.Enums.VotersExcelColumnsEnum;
import com.elector.Exceptions.ServiceResponseException;
import com.elector.Objects.Entities.*;
import com.elector.Objects.Entities.LaborParty.IsraelLaborPartyVoterObject;
import com.elector.Objects.Entities.LikudParty.LikudPartyVoterObject;
import com.elector.Objects.General.EmailObject;
import com.elector.Objects.General.ExcelRowVoter;
import com.elector.Services.GeneralManager;
import com.elector.Utils.Spreadsheet.CSVReader;
import com.elector.Utils.Spreadsheet.SpreadsheetReader;
import com.elector.Utils.Spreadsheet.XLSReader;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.elector.Utils.Definitions.*;
import static com.elector.Utils.Definitions.LIKUD_PARTY_VOTERS_BOOK_BRANCH;

@Component
public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private EmailUtils emailUtils;

    private final int VALID = -2;
    private final int FIXED_EXCEL_PARAMS_COUNT = 8;


    public static Writer getWriter(String path) throws FileNotFoundException, UnsupportedEncodingException{
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path)), StandardCharsets.UTF_8));
    }

    public static Reader getReader(String path) throws FileNotFoundException, UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
    }



    public void readCommonVotersBook(CampaignObject campaignObject) {
        createCampaignCitiesMapObjects(campaignObject);
        String path = NavigationUtils.getPathToCommonVotersBookFiles(campaignObject.getOid());
        BufferedReader br = null;
        String line = EMPTY;
        try {
            LOGGER.info("reading process started");
            List<BallotBoxObject> ballotBoxObjects = generalManager.getBallotBoxesByCampaign(campaignObject.getOid());
            Map<Integer, Map<Integer, BallotBoxObject>> citiesBallotBoxesMap = new HashMap<>();
            for (BallotBoxObject ballotBoxObject : ballotBoxObjects) {
                Map<Integer, BallotBoxObject> ballotBoxNumbersToObjects = citiesBallotBoxesMap.computeIfAbsent(Integer.valueOf(ballotBoxObject.getCityObject().getSymbol()), k -> new HashMap<>());
                ballotBoxNumbersToObjects.put(ballotBoxObject.getNumber(), ballotBoxObject);
            }
            LOGGER.info("done loading ballot boxes");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            boolean headers = true;
            List<CityObject> cityObjectList = generalManager.getList(CityObject.class);
            Map<String, CityObject> citiesMap = new LinkedHashMap<>();
            for (CityObject cityObject : cityObjectList) {
                citiesMap.put(cityObject.getSymbol(), cityObject);
            }
            int counter = 0;
            List<String> votersIds = generalManager.getVotersIds(campaignObject.getOid(), false);
            LOGGER.info("done loading voters ids");
            while ((line = br.readLine()) != null) {
                if (!headers) {
                    if (Utils.hasText(line)) {
                        List<String> fields = Arrays.asList(line.split(COMMA));
                        Integer ballotBoxNumber = Integer.valueOf(fields.get(VOTERS_BOOK_BALLOT_BOX_NUMBER));
                        Integer citySymbol = Integer.valueOf(fields.get(VOTERS_BOOK_CITY_SYMBOL));
                        Map<Integer, BallotBoxObject> ballotBoxNumbersToObjects = citiesBallotBoxesMap.get(citySymbol);
                        if (ballotBoxNumbersToObjects != null) {
                            BallotBoxObject ballotBoxObject = ballotBoxNumbersToObjects.get(ballotBoxNumber);
                            if (ballotBoxObject != null) {
                                CityObject cityObject = citiesMap.get(String.valueOf(citySymbol));
                                CampaignVoterObject campaignVoterObject = new CampaignVoterObject();
                                campaignVoterObject.readVotersBookData(line);
                                if (!votersIds.contains(campaignVoterObject.getVoterId())) {
                                    campaignVoterObject.setBallotBoxObject(ballotBoxObject);
                                    campaignVoterObject.setCampaignObject(campaignObject);
                                    campaignVoterObject.setCityObject(cityObject);
                                    generalManager.save(campaignVoterObject);
                                    counter++;
                                    if (counter % 100 == 0) {
                                        LOGGER.info(String.format("%s voters was saved to database", counter));
                                    }
                                }
                            } else {
                                LOGGER.info(String.format("missing ballot box: city symbol - %s, ballot box number - %s", citySymbol, ballotBoxNumber)  );
                            }
                        }
                    }

                } else {
                    headers = false;
                }
            }
            campaignObject.setVotersBook(true);
            generalManager.save(campaignObject);
            LOGGER.info(String.format("done uploading voters book to db, campaign oid: %s", campaignObject.getOid()));


        } catch (Exception e) {
            LOGGER.error("readVotersBook", e);
        } finally {
            IOUtils.close(br);
        }


    }

    private void createCampaignCitiesMapObjects (CampaignObject campaignObject) {
        String path = NavigationUtils.getPathToCommonVotersBookFiles(campaignObject.getOid());
        BufferedReader br = null;
        String line = EMPTY;
        try {
            LOGGER.info("createCampaignCitiesMapObjects started");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            boolean headers = true;
            int counter = 0;
            List<CityObject> cityObjectList = generalManager.getList(CityObject.class);
            Map<String, CityObject> citiesMap = new LinkedHashMap<>();
            for (CityObject cityObject : cityObjectList) {
                citiesMap.put(cityObject.getSymbol(), cityObject);
            }
            List<Integer> savedCities = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (!headers) {
                    if (Utils.hasText(line)) {
                        List<String> fields = Arrays.asList(line.split(COMMA));
                        Integer citySymbol = Integer.valueOf(fields.get(VOTERS_BOOK_CITY_SYMBOL));
                        CityObject cityObject = citiesMap.get(String.valueOf(citySymbol));
                        if (cityObject == null) {
                            cityObject = new CityObject(fields.get(VOTERS_BOOK_CITY_NAME), fields.get(VOTERS_BOOK_CITY_SYMBOL));
                            generalManager.save(cityObject);
                            citiesMap.put(String.valueOf(citySymbol), cityObject);
                        }
                        if (!savedCities.contains(citySymbol)) {
                            generalManager.save(new CampaignCityMapObject(campaignObject, cityObject));
                            savedCities.add(citySymbol);
                            counter++;
                            if (counter % 100 == 0) {
                                LOGGER.info(String.format("%s CampaignCityMapObject was saved to database", counter));
                            }

                        }
                    }
                } else {
                    headers = false;
                }
            }
            LOGGER.info(String.format("done saving CampaignCityMapObject, campaign oid: %s", campaignObject.getOid()));


        } catch (Exception e) {
            LOGGER.error("readVotersBook", e);
        } finally {
            IOUtils.close(br);
        }
    }

    public void readLaborPartyCommonVotersBook(CampaignObject campaignObject) {
        String path = NavigationUtils.getPathToCommonVotersBookFiles(campaignObject.getOid());
        BufferedReader br = null;
        String line = EMPTY;
        try {
            LOGGER.info("reading process started");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
            boolean headers = true;
            int counter = 0;
            List<CityObject> cityObjectList = generalManager.getList(CityObject.class);
            Map<String, CityObject> citiesMap = new LinkedHashMap<>();
            for (CityObject cityObject : cityObjectList) {
                citiesMap.put(cityObject.getName(), cityObject);
            }
            Set<String> missingCities = new HashSet<>();
            List<String> votersIds = generalManager.getCampaignVotersIds(campaignObject);
            String birthDateFormat = null;
            while ((line = br.readLine()) != null) {
                if (!headers) {
                    if (Utils.hasText(line)) {
                        List<String> fields = Arrays.asList(line.split(COMMA));
                        if (birthDateFormat == null) {
                            String birthDateString = fields.get(ISRAEL_LABOR_PARTY_VOTERS_BOOK_BIRTH_DATE).trim();
                            //this is a patch since it reads the csv data in wrong direction
                            if (birthDateString.equals("09/08/1951") || birthDateString.equals("9/8/1951")) {
                                birthDateFormat = "dd/MM/yyyy";
                            } else if (birthDateString.equals("08/09/1951") || birthDateString.equals("8/9/1951")) {
                                birthDateFormat = "MM/dd/yyyy";
                            } else {
                                throw new Exception("birth date string " + birthDateString);
                            }
                        }
                        IsraelLaborPartyVoterObject campaignVoterObject = new IsraelLaborPartyVoterObject();
                        campaignVoterObject.readVotersBookData(line, birthDateFormat);
                        if (!votersIds.contains(campaignVoterObject.getVoterId())) {
                            String cityName = fields.get(ISRAEL_LABOR_PARTY_VOTERS_BOOK_CITY).trim();
                            String officeCityName = fields.get(ISRAEL_LABOR_PARTY_VOTERS_BOOK_OFFICE_CITY).trim();
                            CityObject cityObject = citiesMap.get(cityName);
                            if (cityObject == null) {
                                if (Utils.hasText(cityName)) {
                                    missingCities.add(cityName);
                                }
                            }
                            CityObject officeCityObject = citiesMap.get(officeCityName);
                            if (officeCityObject == null) {
                                if (Utils.hasText(officeCityName)) {
                                    missingCities.add(officeCityName);
                                }
                            }
                            campaignVoterObject.setCityObject(cityObject);
                            campaignVoterObject.setOfficeCityObject(officeCityObject);
                            campaignVoterObject.setCampaignObject(campaignObject);
                            generalManager.save(campaignVoterObject);
                            counter++;
                            if (counter % 100 == 0) {
                                LOGGER.info(String.format("%s voters was saved to database", counter));
                            }
                        }
                    }

                } else {
                    headers = false;
                }
            }
            smsUtils.sendSms(new SentSmsObject("0504730464", "0504730464", String.format("No city object for %s", missingCities.toString()), new AdminUserObject(69), new Date(), false, new AdminUserObject(SUPER_ADMIN_OID), null));
            campaignObject.setVotersBook(true);
            generalManager.save(campaignObject);
            LOGGER.info(String.format("done uploading voters book to db, campaign oid: %s", campaignObject.getOid()));


        } catch (Exception e) {
            LOGGER.error("readVotersBook", e);
        } finally {
            IOUtils.close(br);
        }
    }

    public void readLikudCommonVotersBook(CampaignObject campaignObject) {
        String path = NavigationUtils.getPathToCommonVotersBookFiles(campaignObject.getOid());
        BufferedReader br = null;
        String line = EMPTY;
        try {
            LOGGER.info("reading process started");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
            boolean headers = true;
            int counter = 0;
            List<CityObject> cityObjectList = generalManager.getList(CityObject.class);
            Map<String, CityObject> citiesMap = new LinkedHashMap<>();
            for (CityObject cityObject : cityObjectList) {
                citiesMap.put(cityObject.getName(), cityObject);
                citiesMap.put(cityObject.getName().replace("'", EMPTY), cityObject);
                citiesMap.put(cityObject.getName().replaceAll("\"", EMPTY), cityObject);
            }
            Set<String> missingCities = new HashSet<>();
            List<String> votersIds = generalManager.getCampaignVotersIds(campaignObject);
            while ((line = br.readLine()) != null) {
                if (!headers) {
                    if (Utils.hasText(line)) {
                        List<String> fields = Arrays.asList(line.split(COMMA));
                        if (fields.size() < LIKUD_PARTY_VOTERS_BOOK_MALE || !StringUtils.isNumeric(fields.get(LIKUD_PARTY_VOTERS_BOOK_VOTER_ID)) || line.contains("מספר זהות")) {
                            LOGGER.info("invalid row: " + counter);
                            continue;
                        }
                        LikudPartyVoterObject campaignVoterObject = new LikudPartyVoterObject();
                        campaignVoterObject.readVotersBookData(line);
                        if (!votersIds.contains(campaignVoterObject.getVoterId())) {
                            String cityName = fields.get(LIKUD_PARTY_VOTERS_BOOK_CITY).trim().replace("*", EMPTY).replaceAll("\"", EMPTY);
                            String branchCityName = fields.get(LIKUD_PARTY_VOTERS_BOOK_BRANCH).trim();
                            CityObject cityObject = citiesMap.get(cityName);
                            if (cityObject == null) {
                                if (Utils.hasText(cityName)) {
                                    missingCities.add(cityName);
                                }
                            }
                            CityObject branchCityObject = citiesMap.get(branchCityName);
                            if (branchCityObject == null) {
                                if (Utils.hasText(branchCityName)) {
                                    missingCities.add(branchCityName);
                                }
                            }
                            campaignVoterObject.setCityObject(cityObject);
                            campaignVoterObject.setBranchCity(branchCityObject);
                            campaignVoterObject.setCampaignObject(campaignObject);
                            generalManager.save(campaignVoterObject);
                            counter++;
                            if (counter % 100 == 0) {
                                LOGGER.info(String.format("%s voters was saved to database", counter));
                            }
                        }

                    }

                } else {
                    headers = false;
                }
            }
            smsUtils.sendSms(new SentSmsObject("0504730464", "0504730464", String.format("No city object for %s", missingCities.toString()), new AdminUserObject(69), new Date(), false, new AdminUserObject(SUPER_ADMIN_OID), null));
            campaignObject.setVotersBook(true);
            generalManager.save(campaignObject);
            LOGGER.info(String.format("done uploading voters book to db, campaign oid: %s", campaignObject.getOid()));


        } catch (Exception e) {
            LOGGER.error("readVotersBook", e);
        } finally {
            IOUtils.close(br);
        }
    }

    public void readVotersBook(AdminUserObject adminUserObject, String path, boolean supporters, Integer activistOid) {
        LOGGER.info("readVotersBook");
        long startReadTime = System.currentTimeMillis();
        SpreadsheetReader sr = null;
        int row = 0;
        try {
            String extension = FilenameUtils.getExtension(path);
            if("csv".equals(extension)) {
                sr = new CSVReader(path);
            }
            else if("xls".equals(extension) || "xlsx".equals(extension)) {
                sr = new XLSReader(path);
            }
            String line = EMPTY;
            //read excel to voters list
            boolean headers = true;
            Map<Integer, Integer> map = null;
            while ((line = sr.readRow()) != null) {
                if (!headers) {
                    if (line.replaceAll(COMMA, EMPTY).trim().length() > 3) {
                        if (row % 1000 == 0) {
                            LOGGER.info(String.format("read %s rows", row));
                        }
                        try {
                            VoterObject voterObject = new VoterObject();
                            voterObject.readVotersBookRow(adminUserObject, line, supporters, map);

                            VoterObject existing = generalManager.getVoterById(adminUserObject.getOid(), voterObject.getVoterId());
                            if (existing != null) {
                                LOGGER.debug("Update existing voter.");
                                existing.setObject(voterObject);
                                generalManager.save(existing);
                            } else {
                                LOGGER.info(String.format("Voter with id of %s does not exist in voters database", voterObject.getVoterId()));
                            }
                        } catch (Exception e) {
                            LOGGER.error(String.format("line %s in uploading excel has failed, admin oid: %s", line, adminUserObject.getOid()));
                        }
                    } else {
                        LOGGER.error(String.format("file in path %s is wrongly formatted, could not finish parsing, first error in line %s", path, line));
                        break;
                    }
                } else {
                    if (line.replaceAll(COMMA, EMPTY).trim().length() > 3) {
                        map = getOptionalHeadersAliases (line);
                        headers = false;
                    } else {
                        LOGGER.error(String.format("file in path %s is wrongly formatted, could not finish parsing, first error in line %s", path, line));
                        CandidateMessageObject candidateMessageObject = new CandidateMessageObject();
                        candidateMessageObject.setTitle("wrong.format.excel");
                        candidateMessageObject.setBody("the.excel.file.is.wrongly.formatted.check.line" + SPACE + line);
                        candidateMessageObject.setDate(new Date());
                        generalManager.save(candidateMessageObject);
                        break;
                    }
                }
                row++;
            }
        } catch (Exception e) {
            LOGGER.error("readVotersBook", e);
        } finally {
            if(sr != null) {
                sr.close();
            }
        }
        LOGGER.info("Saving excel file took {} ms, voters count = {}",
                System.currentTimeMillis() - startReadTime, row);
    }

    private Map<Integer, Integer> getOptionalHeadersAliases (String headers) {
        Map<String, Set<String>> aliasesMap = new HashMap<>();
        List<AliasObject> aliasObjects = generalManager.getList(AliasObject.class);
        for (AliasObject aliasObject : aliasObjects) {
            String aliases = aliasObject.getAliases();
            List<String> aliasesList = Arrays.asList(aliases.split(COMMA));
            Set<String> aliasesSet = new HashSet<>();
            for (String alias : aliasesList) {
                aliasesSet.add(alias.trim());
            }
            aliasesMap.put(aliasObject.getSource(), aliasesSet);
        }
        Map<Integer, Integer> columnToPositionMap = new HashMap<>();
        List<String> headersList = Arrays.asList(headers.split(COMMA));
        for (int i = 0; i < headersList.size(); i++) {
            for (String source : aliasesMap.keySet()) {
                Set<String> aliases = aliasesMap.get(source);
                if (aliases.contains(headersList.get(i).replace("\uFEFF", EMPTY).trim())) {
                    for (VotersExcelColumnsEnum header : VotersExcelColumnsEnum.values()) {
                        if (header.toString().equals(source)) {
                            columnToPositionMap.put(header.getValue(), i);
                            break;
                        }
                    }

                }
            }
        }

        return columnToPositionMap;
    }

    public void readBallotBoxesData(String path, boolean regional) throws IOException {
        LOGGER.info("readBallotBoxesData");
        long startReadTime = System.currentTimeMillis();
        SpreadsheetReader sr = null;
        BufferedReader br = null;
        try {
            String extension = FilenameUtils.getExtension(path);
            if("csv".equals(extension)) {
                sr = new CSVReader(path);
            }
            else if("xls".equals(extension) || "xlsx".equals(extension)) {
                sr = new XLSReader(path);
            }else if ("txt".equals(extension)) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            }
            String line = EMPTY;
            //read excel to voters list
            boolean headers = true;
            Map<Integer, Integer> map = null;
            int row = 0;

            List<CityObject> cityObjectList = generalManager.getList(CityObject.class);
            Map<String, CityObject> citiesMap = new LinkedHashMap<>();
            for (CityObject cityObject : cityObjectList) {
                String cityAlias1 = cityObject.getName()
                        .replace(")", "")
                        .replace("(", "")
                        .replace(".", "")
                        .replace(",", "")
                        .replace("'", "")
                        .replace("\"", "");
                citiesMap.put(cityAlias1, cityObject);
            }
            boolean start = false;
            Map<String, BallotBoxObject> ballotBoxObjectMap = new HashMap<>();
            while ((line = (sr != null ? sr.readRow() : br.readLine())) != null) {
                if (!headers) {
                    List<String> fields = Arrays.asList(line.split(COMMA));
                    List<String> trimmedFields = new ArrayList<>();
                    for (String field : fields) {
                        trimmedFields.add(field.trim().replaceAll("\"", ""));
                    }
                    fields = trimmedFields;
                    if (row % 500 == 0) {
                        LOGGER.info(String.format("read %s rows", row));
                    }
                    if (line.replaceAll(COMMA, EMPTY).trim().length() > 3) {
                        try {
                            int ballotBoxNumber = Integer.valueOf(fields.get(regional ? REGIONAL_BALLOT_BOXES_DATA_NUMBER : BALLOT_BOXES_DATA_NUMBER).trim().replace(DOT, EMPTY));
                            String cityName = Arrays.asList(line.split(COMMA)).get(regional ? REGIONAL_BALLOT_BOXES_DATA_CITY_NAME : BALLOT_BOXES_DATA_CITY_NAME).trim();
                            cityName = cityName
                                    .replace(")", "")
                                    .replace("(", "")
                                    .replace(".", "")
                                    .replace(",", "")
                                    .replace("'", "")
                                    .replace("\"", "");
                            CityObject cityObject = citiesMap.get(cityName);
                            if (cityObject != null) {
                                BallotBoxObject ballotBoxObject = ballotBoxObjectMap.get(createBallotBoxUniqueKey(cityObject.getSymbol(), ballotBoxNumber));
                                if (ballotBoxObject == null) {
                                    ballotBoxObject = new BallotBoxObject();
                                    ballotBoxObject.readBallotBoxesData(line, regional);
                                    ballotBoxObject.setCityObject(cityObject);
                                    generalManager.save(ballotBoxObject);
                                    ballotBoxObjectMap.put(createBallotBoxUniqueKey(cityName, ballotBoxNumber), ballotBoxObject);
                                }
//                                BallotBoxRoomObject ballotBoxRoomObject = new BallotBoxRoomObject(ballotBoxObject);
//                                ballotBoxRoomObject.readBallotBoxRoomData(line);
//                                generalManager.save(ballotBoxRoomObject);

                            } else {
                                com.elector.Objects.General.EmailObject emailObject = new EmailObject(
                                        "city not found",
                                        String.format("city symbold %s not found in db", cityName),
                                        TECHNICAL_ISSUES_EMAIL);
                                emailUtils.sendEmailViaGmail(emailObject);
                                LOGGER.info(String.format("city symbold %s not found in db", cityName));
                            }

                        } catch (Exception e) {
                            LOGGER.error(String.format("line %s in uploading excel has failed, line", e));
                        }
                    }
                } else {
                    headers = false;
                }
                row++;
            }
            LOGGER.info("done reading ballot boxes data");
        } catch (Exception e) {
            LOGGER.error("readBallotBoxesData", e);
        } finally {
            if(sr != null) {
                sr.close();
                br.close();
            }
        }
//        LOGGER.info("Saving excel file took {} ms, voters count = {}",
//                System.currentTimeMillis() - startReadTime, voters.size());
    }

    private String createBallotBoxUniqueKey (String citySymbol, int ballotBoxNumber) {
        return String.format("%s_%s", citySymbol, ballotBoxNumber);
    }

    private int validateExcelFile(String line) {
        final int GENERAL_ERROR = -3;
        final int FIELDS_COUNT = -1;
        int errorCode = VALID;
        try {

            String[] tempFields = line.split(COMMA);
            List<String> fields = new ArrayList<>();
            for (String tempField : tempFields) {
                fields.add(Utils.cleanString(tempField));
            }

            if (fields.size() < FIXED_EXCEL_PARAMS_COUNT) {
                errorCode = FIELDS_COUNT;
            } else if (!fields.get(EXCEL_ROW_VOTER_ID).trim().equals("תעודת זהות")) {
                errorCode = EXCEL_ROW_VOTER_ID;
            } else if (!fields.get(EXCEL_ROW_VOTER_SUPPORT_STATUS).trim().equals("סטטוס תמיכה")) {
                errorCode = EXCEL_ROW_VOTER_SUPPORT_STATUS;
            } else if (!fields.get(EXCEL_ROW_VOTER_BIRTH_DATE).trim().equals("תאריך לידה")) {
                errorCode = EXCEL_ROW_VOTER_BIRTH_DATE;
            } else if (!fields.get(EXCEL_ROW_VOTER_GENDER).trim().equals("מין")) {
                errorCode = EXCEL_ROW_VOTER_GENDER;
            } else if (!fields.get(EXCEL_ROW_VOTER_CELLULAR_PHONE).trim().equals("טלפון סלולרי")) {
                errorCode = EXCEL_ROW_VOTER_CELLULAR_PHONE;
            } else if (!fields.get(EXCEL_ROW_VOTER_HOME_PHONE).trim().equals("טלפון בבית")) {
                errorCode = EXCEL_ROW_VOTER_HOME_PHONE;
            } else if (!fields.get(EXCEL_ROW_VOTER_EXTRA_PHONE).trim().equals("טלפון נוסף")) {
                errorCode = EXCEL_ROW_VOTER_HOME_PHONE;
            } else if (!fields.get(EXCEL_ROW_VOTER_EMAIL).trim().equals("דואר אלקטרוני")) {
                errorCode = EXCEL_ROW_VOTER_EMAIL;
            }
        } catch (Exception e) {
            LOGGER.error("error parsing excel", e);
            errorCode = GENERAL_ERROR;
        }
        return errorCode;

    }

    private Map<Integer, CustomPropertyObject> readCustomProperties (String line) {
        Map<Integer, CustomPropertyObject> customPropertiesMap = new HashMap<>();
        List<String> fields = Arrays.asList(line.split(COMMA));
        if (fields.size() > FIXED_EXCEL_PARAMS_COUNT) {
            List<CustomPropertyObject> customPropertyObjects = generalManager.loadList(CustomPropertyObject.class);
            List<CustomPropertyOptionObject> customPropertyOptionObjects = generalManager.loadList(CustomPropertyOptionObject.class);
            Map<Integer, Set<Integer>> propertiesOidsToOptionsSet = new HashMap<>();
            for (CustomPropertyOptionObject customPropertyOptionObject : customPropertyOptionObjects) {
                Set<Integer> optionsSet = propertiesOidsToOptionsSet.get(customPropertyOptionObject.getCustomPropertyObject().getOid());
                if (optionsSet == null) {
                    optionsSet = new HashSet<>();
                }
                optionsSet.add(customPropertyOptionObject.getOid());
                propertiesOidsToOptionsSet.put(customPropertyOptionObject.getCustomPropertyObject().getOid(), optionsSet);
            }
            for (CustomPropertyObject customPropertyObject : customPropertyObjects) {
                Set<Integer> options = propertiesOidsToOptionsSet.get(customPropertyObject.getOid());
                if (options != null) {
                    customPropertyObject.setOptionsOids(options);
                }
            }
            Map<String, CustomPropertyObject> propertiesNamesToOids = new HashMap<>();
            for (CustomPropertyObject customPropertyObject : customPropertyObjects) {
                String hebrew = Utils.getTranslation(customPropertyObject.getTranslationKey());
                if (hebrew != null) {
                    propertiesNamesToOids.put(hebrew.trim(), customPropertyObject);
                    propertiesNamesToOids.put(customPropertyObject.getTranslationKey(), customPropertyObject);
                }
            }
            for (int i = FIXED_EXCEL_PARAMS_COUNT; i < fields.size(); i++) {
                String field = fields.get(i);
                boolean error = false;
                if (field != null) {
                    field = field.trim();
                    CustomPropertyObject customPropertyObject = propertiesNamesToOids.get(field);
                    if (customPropertyObject != null) {
                        customPropertiesMap.put(i, customPropertyObject);
                    } else {
                        error = true;
                    }
                } else {
                    error = true;
                }
                if (error) {
                    LOGGER.warn(String.format("no custom property was found for index %s, name: %s", i, field));
                }
            }
        }
        return customPropertiesMap;

    }

    private void changeSupportStatusForAll (ExcelUploadObject excelUploadObject, List<String> votersIds, int supportStatus) {
        List<String> nonExistingIds = new ArrayList<>();
        Map<String, Integer> votersIdsMap = generalManager.votersIdsMap(excelUploadObject.getUserOid());
        excelUploadObject.setTotalIds(votersIds.size());
        int i = 0;
        for (String id : votersIds) {
            Integer oid = votersIdsMap.get(id);
            if (oid != null) {
                generalManager.updateSupportStatus(oid, supportStatus);
            } else {
                nonExistingIds.add(id);
            }
            if (i % 100 == 0) {
                LOGGER.info(String.format("passed %s items of %s", i, votersIds.size()));
            }
            i++;
        }
        excelUploadObject.setNonExistingIdsList(nonExistingIds);
    }

    private void addActivistVoterMapObject (ExcelUploadObject excelUploadObject, List<String> votersIds, int activistOid, boolean upgradeStatus) {
        List<String> nonExistingIds = new ArrayList<>();
        Map<String, Integer> votersIdsMap = generalManager.votersIdsMap(excelUploadObject.getUserOid());
        int i = 0;
        for (String id : votersIds) {
            Integer voterOid = votersIdsMap.get(id);
            if (voterOid != null) {
                ActivistVoterMapObject activistVoterMapObject = generalManager.getActivistVoterMap(activistOid, voterOid);
                VoterObject voterObject = null;
                if (activistVoterMapObject == null) {
                    activistVoterMapObject = new ActivistVoterMapObject(new VoterObject(voterOid), new ActivistObject(activistOid), new AdminUserObject(excelUploadObject.getUserOid()));
                    generalManager.save(activistVoterMapObject);
                } else {
                    voterObject = activistVoterMapObject.getVoter();
                }
                if (upgradeStatus) {
                    if (voterObject == null) {
                        voterObject = generalManager.loadObject(VoterObject.class, voterOid);
                    }
                    if (voterObject.getSupportStatus() == PARAM_SUPPORT_STATUS_UNKNOWN || voterObject.getSupportStatus() == PARAM_SUPPORT_STATUS_NOT_SUPPORTING) {
                        voterObject.setSupportStatus(PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING);
                        generalManager.save(voterObject);
                    }

                }
            } else {
                nonExistingIds.add(id);
            }
            if (i % 100 == 0) {
                LOGGER.info(String.format("passed %s items of %s", i, votersIds.size()));
            }
            i++;

        }
        excelUploadObject.setNonExistingIdsList(nonExistingIds);
        excelUploadObject.setExtraInfo(String.format("activistOid: %s, upgradeStatus: %s", activistOid, upgradeStatus));
    }

    private void addVotersToGroup (ExcelUploadObject excelUploadObject, List<String> votersIds, int groupOid) {
        List<String> nonExistingIds = new ArrayList<>();
        List<String> idsAlreadyInGroup = new ArrayList<>();
        Map<String, Integer> votersIdsMap = generalManager.votersIdsMap(excelUploadObject.getUserOid());
        int i = 0;
        List<Integer> votersOidsInTheGroup = generalManager.getGroupVotersOids(groupOid);
        for (String id : votersIds) {
            Integer voterOid = votersIdsMap.get(id);
            if (voterOid != null) {
                if (!votersOidsInTheGroup.contains(voterOid)) {
                    generalManager.save(new VoterCustomGroupMappingObject(voterOid, groupOid));
                } else {
                    idsAlreadyInGroup.add(id);
                }
            } else {
                nonExistingIds.add(id);
            }
            if (i % 100 == 0) {
                LOGGER.info(String.format("passed %s items of %s", i, votersIds.size()));
            }
            i++;
        }
        CustomGroupObject customGroupObject = generalManager.loadObject(CustomGroupObject.class, groupOid);
        customGroupObject.setSize(generalManager.countVotersInGroup(customGroupObject.getOid()));
        generalManager.save(customGroupObject);
        excelUploadObject.setNonExistingIdsList(nonExistingIds);
        excelUploadObject.setExtraInfo(String.format("groupOid: %s ;Ids already in group: %s", groupOid, idsAlreadyInGroup.size()));
    }

    private void markVotersAsVoted (ExcelUploadObject excelUploadObject, List<String> votersIds) {
        List<String> nonExistingIds = new ArrayList<>();
        Map<String, Integer> votersIdsMap = generalManager.votersIdsMap(excelUploadObject.getUserOid());
        int i = 0;
        for (String id : votersIds) {
            Integer oid = votersIdsMap.get(id);
            if (oid != null) {
                generalManager.setVoted(oid);
            } else {
                nonExistingIds.add(id);
            }
            if (i % 100 == 0) {
                LOGGER.info(String.format("passed %s items of %s", i, votersIds.size()));
            }
            i++;
        }
        excelUploadObject.setNonExistingIdsList(nonExistingIds);
    }

    public void readGeneralElectionVotersBook(CampaignObject campaignObject) {
        addMissingCities(campaignObject);
        String path = NavigationUtils.getPathToCommonVotersBookFiles(campaignObject.getOid());
        BufferedReader br = null;
        String line = EMPTY;
        try {
            LOGGER.info("reading process started");
            List<BallotBoxObject> ballotBoxObjects = generalManager.loadList(BallotBoxObject.class);
            Map<Integer, Map<Integer, BallotBoxObject>> citiesBallotBoxesMap = new HashMap<>();
            for (BallotBoxObject ballotBoxObject : ballotBoxObjects) {
                Map<Integer, BallotBoxObject> ballotBoxNumbersToObjects = citiesBallotBoxesMap.computeIfAbsent(Integer.valueOf(ballotBoxObject.getCityObject().getSymbol()), k -> new HashMap<>());
                ballotBoxNumbersToObjects.put(ballotBoxObject.getNumber(), ballotBoxObject);
            }
            LOGGER.info("done loading ballot boxes");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "Windows-1255"));
            boolean headers = true;
            List<CityObject> cityObjectList = generalManager.getList(CityObject.class);
            Map<String, CityObject> citiesMap = new LinkedHashMap<>();
            for (CityObject cityObject : cityObjectList) {
                citiesMap.put(cityObject.getSymbol(), cityObject);
            }
            int savedCounter = 0;
            int passedCounter = 0;
            List<String> savedVotersIds = generalManager.getVotersIds(campaignObject.getOid(), false);
            LOGGER.info("done loading voters ids");
            while ((line = br.readLine()) != null) {
                if (Utils.hasText(line)) {
                    List<String> fields = Arrays.asList(line.split(COMMA));
                    String currentVoterId = fields.get(VOTERS_BOOK_VOTER_ID);
                    if (currentVoterId != null && !savedVotersIds.contains(currentVoterId)) {
                        Integer ballotBoxNumber = Integer.valueOf(fields.get(VOTERS_BOOK_BALLOT_BOX_NUMBER));
                        Integer citySymbol = Integer.valueOf(fields.get(VOTERS_BOOK_CITY_SYMBOL));
                        Map<Integer, BallotBoxObject> ballotBoxNumbersToObjects = citiesBallotBoxesMap.get(citySymbol);
                        if (ballotBoxNumbersToObjects != null) {
                            BallotBoxObject ballotBoxObject = ballotBoxNumbersToObjects.get(ballotBoxNumber);
                            CityObject cityObject = citiesMap.get(String.valueOf(citySymbol));
                            if (ballotBoxObject == null) {
                                LOGGER.info(String.format("missing ballot box: city symbol - %s, ballot box number - %s", citySymbol, ballotBoxNumber));
                                ballotBoxObject = new BallotBoxObject(ballotBoxNumber, cityObject);
                                generalManager.save(ballotBoxObject);
                                ballotBoxNumbersToObjects.put(ballotBoxNumber, ballotBoxObject);
                            }
                            CampaignVoterObject campaignVoterObject = new CampaignVoterObject();
                            campaignVoterObject.readVotersBookData(line);
                            campaignVoterObject.setBallotBoxObject(ballotBoxObject);
                            campaignVoterObject.setCampaignObject(campaignObject);
                            campaignVoterObject.setCityObject(cityObject);
                            generalManager.save(campaignVoterObject);
                            savedCounter++;
                            if (savedCounter % 100 == 0) {
                                LOGGER.info(String.format("%s voters was saved to database", savedCounter));
                            }
                            passedCounter++;
                            if (passedCounter % 1000 == 0) {
                                LOGGER.info(String.format("%s voters was passed", savedCounter));
                            }
                        }

                    }
                }
            }
            campaignObject.setVotersBook(true);
            generalManager.save(campaignObject);
            LOGGER.info(String.format("done uploading voters book to db, campaign oid: %s", campaignObject.getOid()));

        } catch (Exception e) {
            LOGGER.error("readVotersBook", e);
        } finally {
            IOUtils.close(br);
        }

    }

    private void addMissingCities (CampaignObject campaignObject) {
        String path = NavigationUtils.getPathToCommonVotersBookFiles(campaignObject.getOid());
        BufferedReader br = null;
        String line = EMPTY;
        try {
            LOGGER.info("addMissingCities started");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "Windows-1255"));
            boolean headers = true;
            List<CityObject> cityObjectList = generalManager.getList(CityObject.class);
            Map<String, CityObject> citiesMap = new LinkedHashMap<>();
            for (CityObject cityObject : cityObjectList) {
                citiesMap.put(cityObject.getSymbol(), cityObject);
            }
            int totalCitiesAdded = 0;
            while ((line = br.readLine()) != null) {
                if (!headers) {
                    if (Utils.hasText(line)) {
                        List<String> fields = Arrays.asList(line.split(COMMA));
                        Integer citySymbol = Integer.valueOf(fields.get(VOTERS_BOOK_CITY_SYMBOL));
                        CityObject cityObject = citiesMap.get(String.valueOf(citySymbol));
                        if (cityObject == null) {
                            String cityName = fields.get(VOTERS_BOOK_CITY_NAME);
                            cityObject = new CityObject(cityName, fields.get(VOTERS_BOOK_CITY_SYMBOL));
                            generalManager.save(cityObject);
                            citiesMap.put(String.valueOf(citySymbol), cityObject);
                            totalCitiesAdded++;
                            LOGGER.info(String.format("added city: %s, total added cities: %s", cityName, totalCitiesAdded));
                        }
                    }
                } else {
                    headers = false;
                }
            }
        } catch (Exception e) {
            LOGGER.error("addMissingCities", e);
        } finally {
            IOUtils.close(br);
            LOGGER.info("done adding missing cities");

        }
    }

}
