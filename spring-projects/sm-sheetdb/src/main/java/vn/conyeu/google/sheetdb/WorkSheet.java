package vn.conyeu.google.sheetdb;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;

public class WorkSheet {
    private final Sheets.Spreadsheets service;
    private final Sheet sheet;

    WorkSheet(Sheets.Spreadsheets service, Sheet sheet) {
        this.service = service;
        this.sheet = sheet;
    }
}