package vn.conyeu.google.db;

import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.drives.DriveService;
import vn.conyeu.google.drives.GFolder;
import vn.conyeu.google.sheet.SheetService;
import vn.conyeu.google.sheet.XslBook;

public class SheetDb {
    private final DriveService drives;
    private final SheetService sheets;
    private final GFolder folder;
    private final XslBook schema;

    SheetDb(DriveService drives, SheetService sheets, GFolder folder, XslBook xslBook) {
        this.drives = Asserts.notNull(drives);
        this.sheets = Asserts.notNull(sheets);
        this.folder = Asserts.notNull(folder);
        this.schema = Asserts.notNull(xslBook);
    }

    public String getDbId() {
        return folder.getId();
    }

    public String getDbUrl() {
        return folder.getUrl();
    }


}