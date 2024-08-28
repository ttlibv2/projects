package vn.conyeu.google.drives;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class GFile extends AbstractGFile{

    GFile(Drive service, File model) {
        super(service, model);
    }

    @Override
    protected void validateModel(File model) {
        if(GHelper.isFolder(model)) {
            throw new DriveException("The file %s is folder", model.getName());
        }
    }

}