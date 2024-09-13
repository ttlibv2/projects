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
import com.google.api.services.sheets.v4.SheetsScopes;
import vn.conyeu.commons.utils.Sets;
import vn.conyeu.google.xsldb.DbApp;
import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.drives.DriveService;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslService;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Set;

public final class GoogleApp {
    private static final JsonFactory JSON_FACTORY;
    private static final NetHttpTransport httpTransport;

    static {
        try {
            JSON_FACTORY = JacksonFactory.getDefaultInstance();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } //
        catch (GeneralSecurityException | IOException e) {
            throw new GoogleException("newTrustedTransport", e);
        }
    }

    private final DbConfig config;

    public GoogleApp(DbConfig config) {
        this.config = config;
    }

    /**
     * Creates an authorized Credential object.
     * @return An authorized Credential object.
     * @throws GoogleException If the credentials.json file cannot be found.
     */
    private Credential getCredential(String credentialPath, Set<String> scopes) {
        try{
            // Load client secrets.
            InputStream in = GoogleApp.class.getResourceAsStream(credentialPath);
            if (in == null) throw new FileNotFoundException("Resource not found: " + credentialPath);

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // build stored_id
            int slashIndex = credentialPath.lastIndexOf("/");
            int endDot = credentialPath.lastIndexOf(".");

            String storedId = slashIndex != -1 ? credentialPath.substring(slashIndex+1) : credentialPath;
            if(endDot !=-1) storedId = storedId.substring(0, endDot-1);

            // Build flow and trigger user authorization request.
            File tokenDir = new File(config.getTokenDir());
            FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(tokenDir);


            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                    .Builder(httpTransport, JSON_FACTORY, clientSecrets, scopes)
                    .setCredentialDataStore(dataStoreFactory.getDataStore(storedId))
                    .setAccessType("offline").build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        }//
        catch (IOException exp) {
            throw new GoogleException("getCredential(String,Set<Stringh>)", exp);
        }

    }

    public DbApp loginDb()  {
        Credential credential = getCredential("/db_credential.json", Sets.newHashSet(DriveScopes.all(), SheetsScopes.all()));
        HttpTransport HTTP_TRANSPORT = credential.getTransport();
        DriveService drive = drive(credential, HTTP_TRANSPORT);
        XslService sheets = sheets(credential, HTTP_TRANSPORT);
        return new DbApp(drive, sheets);
    }

    public DriveApp loginDrive() {
        Credential credential = getCredential("/drive_credential.json", DriveScopes.all());
        HttpTransport HTTP_TRANSPORT = credential.getTransport();
        return new DriveApp(drive(credential, HTTP_TRANSPORT));
    }

    public XslApp loginSheet() {
        Credential credential = getCredential("/sheet_credential.json", SheetsScopes.all());
        HttpTransport HTTP_TRANSPORT = credential.getTransport();
        return new XslApp(sheets(credential, HTTP_TRANSPORT));
    }

    private DriveService drive(Credential credential, HttpTransport HTTP_TRANSPORT) {
        return new DriveService(new Drive
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(config.getAppName()).build());
    }

    private XslService sheets(Credential credential, HttpTransport HTTP_TRANSPORT) {
        return new XslService(new Sheets
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(config.getAppName()).build());
    }


}