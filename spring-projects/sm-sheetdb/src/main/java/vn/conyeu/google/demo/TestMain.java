package vn.conyeu.google.demo;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.google.core.DbConfig;
import vn.conyeu.google.core.GoogleApp;
import vn.conyeu.google.xsldb.*;

import java.io.IOException;

@Slf4j
public class TestMain {

    static String folderId = "16l065WmbNKx8b_SuSZ4Vqy0pTwg60UOo";

    public static void main(String[] args) throws IOException {
        DbConfig config = new DbConfig();
        config.setAppName("Google Login");
        config.setTokenDir("tokens");

        GoogleApp google = new GoogleApp(config);

        DbApp dbApp = google.loginDb();
        SheetDb sheetDb = dbApp.openById(folderId);

        /*List<Column> columns = List.of(
                new Column("userId", ColumnType.STRING),
                new Column("name", ColumnType.STRING),
                new Column("email", ColumnType.STRING),
                new Column("dob", ColumnType.DATE),
                new Column("createdAt", ColumnType.DATE_TIME)
        );

        SheetTb sheetTb = sheetDb.createTable("users2", columns);*/

        SheetTb sheetTb = sheetDb.openTable("users2");
        log.warn("{}", sheetTb.getUrl());

        /*sheetTb.addColumns(List.of(
                new Column().name("age31").type(ColumnType.INT),
                new Column().name("age11").type(ColumnType.INT),
                new Column().name("age21").type(ColumnType.INT)));

        sheetTb.deleteColumns("age11");*/

        sheetTb.moveColumn(sheetTb.getColumn("name"), 2);


    }
}