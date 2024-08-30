package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.Sheets;

public class XslSheetUpdate {
    private final Sheets.Spreadsheets service;
    private final Integer sheetId;

    public XslSheetUpdate(Sheets.Spreadsheets service, Integer sheetId) {
        this.service = service;
        this.sheetId = sheetId;
    }


}