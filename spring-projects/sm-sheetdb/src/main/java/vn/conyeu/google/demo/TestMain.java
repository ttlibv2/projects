package vn.conyeu.google.demo;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.google.core.DbConfig;
import vn.conyeu.google.core.GoogleLogin;
import vn.conyeu.google.db.DbApp;

import java.io.IOException;

@Slf4j
public class TestMain {

    public static void main(String[] args) throws IOException {
        DbConfig config = new DbConfig();
        config.setAppName("Google Login");
        config.setTokenDir("tokens");

        GoogleLogin google = new GoogleLogin(config);
        DbApp dbApp = google.dbApp();

        dbApp.create("tskpi");














    }
}