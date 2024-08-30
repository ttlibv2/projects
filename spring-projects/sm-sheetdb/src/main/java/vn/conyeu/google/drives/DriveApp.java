package vn.conyeu.google.drives;

import com.google.api.services.drive.model.File;
import vn.conyeu.google.drives.builder.FileBuilder;
import vn.conyeu.google.sheet.builder.ConsumerReturn;

public class DriveApp {
    private final DriveService service;

    public DriveApp(DriveService service) {
        this.service = service;
    }

    public GFolder getRoot() {
            return newFolder(service.getRoot());
    }

    public GFolder getFolderById(String folderId){
        File model = service.openById(folderId, "*");
        return newFolder(model);
    }

    /**
     * Creates a folder in the root of the user's Drive with the given name.
     *
     * @param name The name of the new folder.
     */
    public GFolder createFolder(String name)  {
        return createFolder(b -> b.name(name));
    }

    /**
     * Creates a folder in the root of the user's Drive with the given name.
     *
     * @param name The name of the new folder.
     */
    public GFolder createFolder(String folderId, String name)  {
        return createFolder(b -> b.parents(folderId).name(name));
    }

    /**
     * Creates a folder in the root of the user's Drive with the given name.
     */
    public GFolder createFolder(ConsumerReturn<FileBuilder> consumer)  {
        File file = service.createFolder(consumer);
        return newFolder(file);
    }

    private GFolder newFolder(File model) {
        return new GFolder(service, model);
    }

}