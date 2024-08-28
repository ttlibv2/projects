package vn.conyeu.google.drives;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.function.Consumer;

public class DriveApp {
    private final Drive service;

    public DriveApp(Drive service) {
        this.service = service;
    }

    public GFolder getRoot() {
        try {
            File model = service.files().get("root").execute();
            GFolder folder = newFolder(model);
            folder.hasRoot = true;
            return folder;
        } catch (IOException exp) {
            throw new DriveException(exp);
        }
    }

    public GFolder getFolderById(String folderId) throws IOException {
        File model = service.files().get(folderId).execute();
        return newFolder(model);
    }

    /**
     * Creates a folder in the root of the user's Drive with the given name.
     *
     * @param name The name of the new folder.
     */
    public GFolder createFolder(String name) throws IOException {
        return createFolder(null, name, null);
    }

    /**
     * Creates a folder in the root of the user's Drive with the given name.
     *
     * @param name The name of the new folder.
     */
    public GFolder createFolder(String folderId, String name) throws IOException {
        return createFolder(folderId, name, null);
    }

    /**
     * Creates a folder in the root of the user's Drive with the given name.
     *
     * @param name The name of the new folder.
     */
    public GFolder createFolder(String folderId, String name, Consumer<File> fileConsumer) throws IOException {
        Consumer<File> consumer = file -> {
            if (fileConsumer != null) fileConsumer.accept(file);
            file.setMimeType(GMime.FOLDER.mime);
        };

        File file = GHelper.createFile(service.files(), folderId, name, consumer);
        return newFolder(file);
    }

    private GFolder newFolder(File model) {
        return new GFolder(service, model);
    }

    public enum Access {
        USER,
        GROUP,
        DOMAIN,
        ANYONE
    }

    public enum Role {

        /**The user owns the file or folder.*/
        OWNER("owner"),

        /**Users who can organize files and folders within a shared drive*/
        ORGANIZER("organizer"),

        /**Users who can edit, trash, and move content within a shared drive.*/
        FILE_ORGANIGER("fileOrganizer"),

        /**Users who can access the file or folder are able to edit it.*/
        WRITER("writer"),

        /**Users who can access the file or folder are able only to view it, copy it, or comment on it*/
        COMMENTER("commenter"),

        /**
         * Users who can access the file or folder are able only to view it or copy it.
         */
        READER("reader"),

        /**The user does not have any permissions for the file or folder.*/
        NONE(null);

        final String value;

        Role(String value) {
            this.value = value;
        }

    }
}