package vn.conyeu.google.core;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import vn.conyeu.commons.utils.Sets;
import vn.conyeu.google.db.DbApp;
import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.drives.DriveService;
import vn.conyeu.google.sheet.SheetApp;
import vn.conyeu.google.sheet.XslSheetService;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Set;

public final class GoogleLogin {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final Set<String> SCOPES = Sets.newHashSet(DriveScopes.all());
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private final DbConfig config;
    private Credential credential;

    public GoogleLogin(DbConfig config) {
        this.config = config;
    }

    public Credential getCredential() {
        if(credential == null) login();
        return credential;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        // Load client secrets.
        InputStream in = GoogleLogin.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        File tokenDir = new File(config.getTokenDir());

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(tokenDir))
                .setAccessType("offline").build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void login()  {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            this.credential = getCredentials( HTTP_TRANSPORT);
        }
        catch ( GeneralSecurityException | IOException exp) {
            throw new LoginException("Login error", exp);
        }
    }

    public DriveApp driveApp() {
        Credential credential = getCredential();
        HttpTransport HTTP_TRANSPORT = credential.getTransport();
        return new DriveApp(drive(credential, HTTP_TRANSPORT));
    }

    public SheetApp sheetApp() {
        Credential credential = getCredential();
        HttpTransport HTTP_TRANSPORT = credential.getTransport();
        return new SheetApp(sheets(credential, HTTP_TRANSPORT));
    }

    public DbApp dbApp() {
        Credential credential = getCredential();
        HttpTransport HTTP_TRANSPORT = credential.getTransport();
        Drive drive = drive(credential, HTTP_TRANSPORT);
        Sheets sheets = sheets(credential, HTTP_TRANSPORT);
        return new DbApp(drive, sheets);
    }

    private DriveService drive(Credential credential, HttpTransport HTTP_TRANSPORT) {
        return new DriveService(new Drive
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(config.getAppName()).build());
    }

    private XslSheetService sheets(Credential credential, HttpTransport HTTP_TRANSPORT) {
        return new XslSheetService(new Sheets
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(config.getAppName()).build());
    }


}