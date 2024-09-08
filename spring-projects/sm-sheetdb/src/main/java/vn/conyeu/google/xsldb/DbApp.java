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

    private final DriveApp driveApp;
    private final XslApp xslApp;

    public DbApp(DriveService drives, XslService sheets) {
        this.drives = Asserts.notNull(drives);
        this.sheets = Asserts.notNull(sheets);
        this.driveApp = new DriveApp(drives);
        this.xslApp = new XslApp(sheets);
    }

    public SheetDb create(String name) {

        // create folder db
        GFolder folder = driveApp.createFolder(b -> b.name(name).properties("isDb", "true"));
        String owner = folder.getOwner().getEmailAddress();

        // create xsl schema
        XslBook xslBook = createBook(folder.getId(), "schema", b -> {
            b.addSheet("schema_tb", s -> applySheetBuilder(s.sheetId(0), owner));
            b.addSheet("schema_col", s -> applySheetBuilder(s.sheetId(1), owner));
            return b;
        });

        // update schema id
        driveApp.update(folder.getId(), m -> m.properties("schemaId", xslBook.getId()));


        return new SheetDb(this, folder, xslBook);
    }

    public SheetDb openById(String dbId) {
        return null;
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



}