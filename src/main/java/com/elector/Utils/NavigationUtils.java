package com.elector.Utils;

import com.elector.Objects.General.EmailObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.elector.Utils.Definitions.EMPTY;
import static com.elector.Utils.Definitions.TECHNICAL_ISSUES_EMAIL;

@Component
public class NavigationUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationUtils.class);


    public static final String PATH_TO_ELECTOR_DIRECTORY = "C:\\elector";
    private static final String PATH_TO_IMAGES = String.format("%s\\images", PATH_TO_ELECTOR_DIRECTORY);
    private static final String PATH_TO_PAYMENT_CONFIRMATION_IMAGES = String.format("%s\\payment_confirmation_images", PATH_TO_IMAGES);
    private static final String PATH_TO_VOTERS_LISTS = String.format("%s\\voters_lists", PATH_TO_ELECTOR_DIRECTORY);
    private static final String PATH_TO_COMMON_VOTERS_BOOKS = String.format("%s\\voters_books", PATH_TO_ELECTOR_DIRECTORY);
    public static final String PATH_TO_CSV_FILES = String.format("%s\\csv\\", PATH_TO_ELECTOR_DIRECTORY);
    public static final String PATH_TO_ACTIVIST_VOTERS_FILES = String.format("%s\\activist-voters\\", PATH_TO_CSV_FILES);
    public static final String PATH_TO_VOTERS_FILES = String.format("%s\\voters\\", PATH_TO_CSV_FILES);
    public static final String PATH_TO_SUPPORTERS_FILES = String.format("%s\\supporters\\", PATH_TO_CSV_FILES);
    public static final String PATH_TO_SUPER_ADMIN_FILES = String.format("%s\\super_admin", PATH_TO_ELECTOR_DIRECTORY);
    public static final String PATH_TO_BALLOT_BOXES_DATA_FILE = String.format("%s\\ballot-boxes-data.", PATH_TO_SUPER_ADMIN_FILES);
    public static final String PATH_TO_VOTERS_IDS_FILES = String.format("%s\\voters_ids\\", PATH_TO_SUPER_ADMIN_FILES);

    private List<String> paths = Arrays.asList(
            PATH_TO_ELECTOR_DIRECTORY, PATH_TO_IMAGES, PATH_TO_PAYMENT_CONFIRMATION_IMAGES, PATH_TO_VOTERS_LISTS, PATH_TO_COMMON_VOTERS_BOOKS,
            PATH_TO_CSV_FILES, PATH_TO_ACTIVIST_VOTERS_FILES, PATH_TO_VOTERS_FILES, PATH_TO_SUPPORTERS_FILES, PATH_TO_SUPER_ADMIN_FILES,
            PATH_TO_SUPER_ADMIN_FILES

    );

    @Autowired
    private EmailUtils emailUtils;

    @PostConstruct
    private void createDirectories () {
        new Thread(() -> {
            boolean error = false;
            for (String path : paths) {
                error = createDirectory(path);
                if (error) {
                    EmailObject emailObject = new EmailObject(
                            "fatal error",
                            "required directories were not create, need to create them manually",
                            TECHNICAL_ISSUES_EMAIL);
                    emailUtils.sendEmailViaGmail(emailObject);

                    LOGGER.error("createDirectories", "required directories were not create, need to create them manually");
                    break;
                }
            }
        }).start();
    }

    private boolean createDirectory (String path) {
        boolean error = false;
        File directory = new File(path);
        if (!directory.exists()){
            if (!directory.mkdir()) {
                error = true;
                LOGGER.error("createDirectory", directory.getAbsolutePath() + " was not created");
            }
        }
        return error;
    }

    public static String getPathToPaymentConfirmationImage (int donationOid) {
        return String.format("%s\\%s.png", PATH_TO_PAYMENT_CONFIRMATION_IMAGES, donationOid);
    }

    public static String getPathToVotersListFile (int adminOid, String extension) {
        return String.format("%s\\%s_%s.%s", PATH_TO_VOTERS_LISTS, adminOid, System.currentTimeMillis(), extension);
    }

    public static String getPathToCommonVotersBookFiles (int campaignOid) {
        return String.format("%s\\%s.csv", PATH_TO_COMMON_VOTERS_BOOKS, campaignOid);
    }

    public static String getPathToBallotBoxesFile (String extension) {
        return String.format("%s%s", PATH_TO_BALLOT_BOXES_DATA_FILE, extension);
    }

    public static String getPathToCommonVotersBookFiles (int campaignOid, boolean updated) {
        return String.format("%s\\%s%s.csv", PATH_TO_COMMON_VOTERS_BOOKS, campaignOid, updated ? "_2" : EMPTY);
    }

    public static String getPathToVotersIdsFiles (int userType, int userOid, String extension) {
        return String.format("%s\\%s_%s_%s.%s", PATH_TO_VOTERS_IDS_FILES, userType, userOid, System.currentTimeMillis(), extension);
    }


}
