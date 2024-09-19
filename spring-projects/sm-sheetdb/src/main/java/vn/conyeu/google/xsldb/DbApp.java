package vn.conyeu.google.xsldb;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.drives.DriveService;
import vn.conyeu.google.drives.GFolder;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslService;

@Slf4j
public class DbApp {
    private final DriveService drives;
    private final XslService sheets;

    protected final DriveApp driveApp;
    protected final XslApp xslApp;

    public DbApp(DriveService drives, XslService sheets) {
        this.drives = Asserts.notNull(drives);
        this.sheets = Asserts.notNull(sheets);
        this.driveApp = new DriveApp(drives);
        this.xslApp = new XslApp(sheets);
    }

    /**
     * Create database without name
     * @param name the database name
     * */
    public SheetDb create(String name) {
        GFolder folder = driveApp.createFolder(b -> b.name(name).property("isDb", "true"));
        return new SheetDb(this, folder);
    }

    public SheetDb openById(String dbId) {
        GFolder folder = driveApp.getFolderById(dbId);
        return new SheetDb(this, folder).loadSchema();
    }

}