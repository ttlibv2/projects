package vn.conyeu.google.drives;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class GHelper {

    public static boolean isFolder(File file) {
        return GMime.FOLDER.mime.equals(file.getMimeType());
    }

    /**
     * Creates a folder in the root of the user's Drive with the given name.
     * @param name The name of the new folder.
     */
    public static File createFile(Drive.Files service, String folderId, String name, Consumer<File> fileConsumer) throws IOException {
        File meta = new File();

        if(fileConsumer != null) {
            fileConsumer.accept(meta);
        }

        if(folderId != null) {
            meta.setParents(List.of(folderId));
        }

        meta.setName(name);
        return service.create(meta).execute();
    }
}