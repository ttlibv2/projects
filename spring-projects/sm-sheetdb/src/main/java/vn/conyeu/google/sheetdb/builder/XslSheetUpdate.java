package vn.conyeu.google.sheetdb.builder;

import vn.conyeu.google.sheetdb.XslBatchUpdate;

public class XslSheetUpdate {
    private final XslBatchUpdate update;
    private final Integer sheetId;

    public XslSheetUpdate(XslBatchUpdate update, Integer sheetId) {
        this.update = update;
        this.sheetId = sheetId;
    }
}