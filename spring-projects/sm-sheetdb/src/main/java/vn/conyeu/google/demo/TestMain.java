package vn.conyeu.google.demo;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.google.core.DbConfig;
import vn.conyeu.google.core.GoogleLogin;
import vn.conyeu.google.sheetdb.SheetApp;
import vn.conyeu.google.sheetdb.XslBook;
import vn.conyeu.google.sheetdb.XslSheet;

import java.io.IOException;

@Slf4j
public class TestMain {

    public static void main(String[] args) throws IOException {
        DbConfig config = new DbConfig();
        config.setAppName("Google Login");
        config.setTokenDir("tokens");

        GoogleLogin google = new GoogleLogin(config);

        //DriveApp driveApp = sheetApp.driveApp();
        //GFolder folder = driveApp.getFolderById("13AVkV1QZf6diCyN5OUmdY5cVfv_gEiMT");
        //folder.addFolder("folder1");

        SheetApp sheetApp = google.sheetApp();

        //        String url = sheetApp.create("sheetApp", xsl -> {
//            //xsl.theme(theme -> theme.defaultData());
//
//            SheetBuilder sheet = xsl.addSheet("table1", 10, 2).frozenRowCount(1);
//            sheet.getRow(0).formatCells(0, 2, cell -> cell
//                    .textFormat(tx -> tx.bold(true).fontFamily("Roboto"))
//                    .horizontalAlignment(HorizontalAlign.CENTER)
//            );
//            return xsl;
//        }).getUrl();

        XslBook wb = sheetApp.openById("1henZsYYsmEvgK4HmFxgM3EIA-ae5oIxcrxWCLlI_8zM");
        XslSheet sheet = wb.getSheetByName("table1");
        sheet.insertColumnBefore(1);













    }
}