package com.elector.Utils.Spreadsheet;

import java.io.IOException;

public interface SpreadsheetReader {

    String readRow() throws IOException;

    void close();
}
