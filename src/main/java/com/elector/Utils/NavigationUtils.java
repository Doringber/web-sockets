package com.elector.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.List;


@Component
public class NavigationUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationUtils.class);


    public static final String PATH_TO_ELECTOR_DIRECTORY =  System.getProperty("user.home") + "/elector";
    private static final String PATH_TO_IMAGES = String.format("%s%simages", PATH_TO_ELECTOR_DIRECTORY, File.separator);
    private static final String PATH_TO_PAYMENT_CONFIRMATION_IMAGES = String.format("%s%spayment_confirmation_images", PATH_TO_IMAGES, File.separator);
    private static final String PATH_TO_VOTERS_LISTS = String.format("%s%svoters_lists", PATH_TO_ELECTOR_DIRECTORY, File.separator);
    private static final String PATH_TO_COMMON_VOTERS_BOOKS = String.format("%s%svoters_books", PATH_TO_ELECTOR_DIRECTORY, File.separator);
    public static final String PATH_TO_CSV_FILES = String.format("%s%scsv%s", PATH_TO_ELECTOR_DIRECTORY, File.separator, File.separator);
    public static final String PATH_TO_ACTIVIST_VOTERS_FILES = String.format("%s%sactivist-voters%s", PATH_TO_CSV_FILES, File.separator, File.separator);
    public static final String PATH_TO_VOTERS_FILES = String.format("%s%svoters%s", PATH_TO_CSV_FILES, File.separator, File.separator);
    public static final String PATH_TO_SUPPORTERS_FILES = String.format("%s%ssupporters%s", PATH_TO_CSV_FILES, File.separator, File.separator);
    public static final String PATH_TO_SUPER_ADMIN_FILES = String.format("%s%ssuper_admin", PATH_TO_ELECTOR_DIRECTORY, File.separator);
    public static final String PATH_TO_BALLOT_BOXES_DATA_FILE = String.format("%s%sballot-boxes-data.", PATH_TO_SUPER_ADMIN_FILES, File.separator);
    public static final String PATH_TO_VOTERS_IDS_FILES = String.format("%s%svoters_ids%s", PATH_TO_SUPER_ADMIN_FILES, File.separator, File.separator);
    private static final String PATH_TO_USERS_IMAGES = String.format("%s%susers_images", PATH_TO_ELECTOR_DIRECTORY, File.separator);

    private List<String> paths = Arrays.asList(
            PATH_TO_ELECTOR_DIRECTORY, PATH_TO_IMAGES, PATH_TO_PAYMENT_CONFIRMATION_IMAGES, PATH_TO_VOTERS_LISTS, PATH_TO_COMMON_VOTERS_BOOKS,
            PATH_TO_CSV_FILES, PATH_TO_ACTIVIST_VOTERS_FILES, PATH_TO_VOTERS_FILES, PATH_TO_SUPPORTERS_FILES, PATH_TO_SUPER_ADMIN_FILES,
            PATH_TO_SUPER_ADMIN_FILES, PATH_TO_USERS_IMAGES

    );

    @PostConstruct
    private void createDirectories () {
        new Thread(() -> {
            boolean error = false;
            for (String path : paths) {
                error = createDirectory(path);
                if (error) {
                    LOGGER.error("createDirectories", "required directories were not create, need to create them manually");
                    break;
                }
            }
        }).start();
    }

    private static boolean createDirectory (String path) {
        boolean error = false;
        File directory = new File(path);
        if (!directory.exists()){
            if (!directory.mkdir()) {
                error = true;
                LOGGER.error("error createDirectory, {}", directory.getAbsolutePath());
            }
        }
        return error;
    }

    public static String getPathToProfileImage (int contactType, int contactOid) {
        String userTypeDirectoryPath = String.format("%s%s%s", PATH_TO_USERS_IMAGES, File.separator, contactType);
        createDirectory(userTypeDirectoryPath);
        String userOidDirectoryPath = String.format("%s%s%s", userTypeDirectoryPath, File.separator, contactOid);
        createDirectory(userOidDirectoryPath);
        return String.format("%s%s%s.jpg", userOidDirectoryPath, File.separator, contactOid);
    }


}
