package vn.conyeu.google.db;

import vn.conyeu.google.drives.DriveService;
import vn.conyeu.google.sheet.XslSheetService;

public class DbApp {
    private final DriveService drives;
    private final XslSheetService sheets;

    public DbApp(DriveService drives, XslSheetService sheets) {
        this.drives = drives;
        this.sheets = sheets;
    }


}