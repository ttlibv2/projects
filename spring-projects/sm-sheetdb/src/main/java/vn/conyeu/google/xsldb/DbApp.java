package vn.conyeu.google.xsldb;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.drives.DriveService;
import vn.conyeu.google.drives.GFolder;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslService;
import vn.conyeu.google.sheet.XslBook;
import vn.conyeu.google.sheet.builder.ConsumerReturn;
import vn.conyeu.google.sheet.builder.SheetBuilder;
import vn.conyeu.google.sheet.builder.XslBuilder;

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
        GFolder folder = driveApp.createFolder(b -> b.name(name).properties("isDb", "true"));
        return new SheetDb(this, folder);
    }

    public SheetDb openById(String dbId) {
        GFolder folder = driveApp.getFolderById(dbId);
        return new SheetDb(this, folder);
    }

    protected XslBook createBook(String folderId, String name, ConsumerReturn<XslBuilder> consumer) {
        XslBook xslBook = xslApp.create(name, consumer);

        // move xsl to folder db
        driveApp.update(xslBook.getId(), b -> b
                .addParents(folderId)
                .properties("isTable", "true")
        );

        return xslBook;

    }


    SheetBuilder applyXslSheet(GFolder folder, SheetBuilder sheet) {
        String owner = folder.getOwner().getEmailAddress();
        sheet.sheetId(1).rowCount(2).columnCount(2).frozenRowCount(1);
        sheet.getGrid(0).editRow(0, r -> r
                .editCells(c -> c.fontFamily("Consolas").bold(true))
                .protect(p -> p.description("Only Owner Editor").users(owner))
        );




        return sheet;
    }
}