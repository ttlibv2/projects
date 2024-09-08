package vn.conyeu.google.db;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.drives.DriveService;
import vn.conyeu.google.drives.GFolder;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslService;
import vn.conyeu.google.sheet.XslBook;
import vn.conyeu.google.sheet.builder.SheetBuilder;

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
        XslBook xslBook = xslApp.create("schema", b -> {
            b.addSheet("schema_tb", s -> applySheetBuilder(s.sheetId(0), owner));
            b.addSheet("schema_col", s -> applySheetBuilder(s.sheetId(1), owner));
            return b;
        });

        // move xsl to folder db
        driveApp.update(xslBook.getId(), b -> b
                .addParents(folder.getId())
                .properties("schemaTb", xslBook.getId())
        );

        return new SheetDb(drives, sheets, folder, xslBook);
    }

    public SheetDb openById(String dbId) {
        return null;
    }

    private SheetBuilder applySheetBuilder(SheetBuilder sheet, String owner) {
        sheet.rowCount(2).columnCount(2).frozenRowCount(1);
        sheet.protectedRange(p -> p.forRow(sheet.getSheetId(), 0)
                .description("Only Admin").editorUser(owner));

        sheet.getRow(0).formatCells(2, c -> c
                .textFormat(f -> f.bold(true).fontFamily("Consolas"))
        );

        sheet.getRow(1).formatCells(2, c -> c
                .textFormat(f -> f.fontFamily("Consolas"))
        );






        return sheet;
    }

}