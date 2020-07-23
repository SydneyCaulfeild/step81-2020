package com.google.flourbot.api;

import com.google.api.services.sheets.v4.*;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DriveCloudSheet implements CloudSheet {
  private static final String VALUE_INPUT_OPTION = "USER_ENTERED";
  private static final String INSERT_DATA_OPTION = "INSERT_ROWS";
  private Sheets sheetsService;
  private String spreadsheetId;

  public DriveCloudSheet(String documentId, Sheets documentService) {
    this.spreadsheetId = documentId;
    this.sheetsService = documentService;
  }

  private String getRange(int numEntryColumns, int numSkipColumns) {
    // Returns range for sheets request by calculating the alphabetic name of
    // the start and end column based on the number of columns filled and pre-skipped

    String startColumn = toAlphabetic(numSkipColumns);
    String endColumn = toAlphabetic(numSkipColumns + numEntryColumns - 1);
    return String.format("Sheet1!%d:%d", startColumn, endColumn);
  }

  private String toAlphabetic(int i) {
    // Recursive strategy to build a string that represents the alphabetic name
    // of a column based on column number i
    // e.g. 0 -> A, 1 -> B... 25 -> Z, 26 -> AA...
    
    // Negative numbers cause errors
    if (i < 0) {
      throw new IllegalArgumentException(String.format("Column number %d is invalid", i));
    }

    // Every calculation produces 1 letter
    int quotient = i / 26;
    int remainder = i % 26;
    char letter = (char)((int)'A' + remainder);
    if (quotient == 0) {
        return "" + letter;
    } else {
        return toAlphabetic(quotient-1) + letter;
    }
  }

  public void appendRow(List<String> values) throws IOException, GeneralSecurityException {
    // Method generates and sends request to append rows to bottom of sheet containing values

    // Calculate the range where the values will be inserted
    String range = this.getRange(values.size(), 0);
    
    // Convert the values into a List of Lists, which is needed to send the request
    String[] array = values.stream().toArray(String[]::new);
    List<List<Object>> valuesList = Arrays.asList(
      Arrays.asList(array)
    );

    ValueRange requestBody = new ValueRange().setValues(valuesList);

    // Send request
    Sheets.Spreadsheets.Values.Append request =
      this.sheetsService.spreadsheets().values().append(this.spreadsheetId, range, requestBody);
    request.setValueInputOption(VALUE_INPUT_OPTION);
    request.setInsertDataOption(INSERT_DATA_OPTION);
    
    AppendValuesResponse response = request.execute();
  }
}