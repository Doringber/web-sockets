package com.elector.Utils.Spreadsheet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CSVReader implements SpreadsheetReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVReader.class);
    private String path;
    private BufferedReader br;

    public CSVReader(String path) throws Exception {
        LOGGER.info("Create CSV reader for path = {}", path);
        this.path = path;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
    }

    @Override
    public String readRow() throws IOException {
        return br.readLine();
    }

    public void close () {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                LOGGER.error("close", e);
            }
        }
    }
}
