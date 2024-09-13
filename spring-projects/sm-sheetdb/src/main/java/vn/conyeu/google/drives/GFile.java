package vn.conyeu.google.drives;

import com.google.api.services.drive.model.File;
import vn.conyeu.google.drives.builder.AbstractGFile;

public class GFile extends AbstractGFile {

    public GFile(DriveService service, File model) {
        super(service, model);
    }

    public static GFile cast(AbstractGFile file) {
        return (GFile) file;
    }

    @Override
    protected void validateModel(File model) {
        if(GHelper.isFolder(model)) {
            throw new DriveException("The file %s is folder", model.getName());
        }
    }

}