package com.elector.Utils.Spreadsheet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static com.elector.Utils.Definitions.*;

public class XLSReader implements SpreadsheetReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(XLSReader.class);
    private String path;
    private Workbook wb;
    private Sheet sheet;
    private Iterator<Row> rowIterator;
    private DataFormatter dataFormatter = new DataFormatter();

    public XLSReader(String path) throws InvalidFormatException, IOException {
        LOGGER.info("Create XLS reader for path = {}", path);
        this.path = path;
        wb = WorkbookFactory.create(new File(path));
        Iterator<Sheet> sheetIterator = wb.sheetIterator();
        if(sheetIterator.hasNext()) {
            LOGGER.info("Retrieving Sheets");
            sheet = sheetIterator.next();
            LOGGER.info("Retrieving Row iterator");
            rowIterator = sheet.rowIterator();
        }
    }

    @Override
    public String readRow() {
        StringBuilder line = new StringBuilder();
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            for (int i = 0; i < 11; i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    line.append(COMMA).append(MINUS);
                } else {
                    String cellValue = dataFormatter.formatCellValue(cell).replace(COMMA, EMPTY);
                    if(line.length() != 0) {
                        line.append(COMMA);
                    }
                    line.append(cellValue);

                }

            }
            return line.toString();
        }
        else {
            return null;
        }
    }

    public void close () {
        if (wb != null) {
            try {
                wb.close();
            } catch (IOException e) {
                LOGGER.error("close", e);
            }
        }
    }

}
