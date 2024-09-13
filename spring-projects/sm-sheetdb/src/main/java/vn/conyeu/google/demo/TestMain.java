package vn.conyeu.google.demo;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.google.core.DbConfig;
import vn.conyeu.google.core.GoogleApp;
import vn.conyeu.google.xsldb.DbApp;
import vn.conyeu.google.xsldb.SheetDb;
import vn.conyeu.google.xsldb.SheetTb;

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
        log.warn("OpenDb: {}", sheetDb.getUrl());

        //sheetDb.loadSchema();
        //log.warn("{}", sheetDb.schemas());

        SheetTb usersTb = sheetDb.createTb("users");

        









    }
}