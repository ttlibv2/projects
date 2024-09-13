package vn.conyeu.google.drives;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import vn.conyeu.google.drives.builder.FileBuilder;
import vn.conyeu.google.drives.builder.PermissionBuilder;
import vn.conyeu.google.sheet.builder.ConsumerReturn;

import static vn.conyeu.google.core.ExecuteFunc.simple;

public class DriveService {
    //private final Drive service;
    private final Drive.Files files;
    private final Drive.Permissions permissions;

    public final String DEFAULT_FIELDS = "id,name,properties,owners,webViewLink,description,mimeType";

    public DriveService(Drive service) {
        //this.service = service;
        this.files = service.files();
        this.permissions = service.permissions();
    }



    public File getRoot() {
        return simple(() -> files.get("root"));
    }

    public File openById(String fileId, String fields) {
        return simple(() -> files.get(fileId).setFields(fields));
    }

    public File download(String fileId) {
        return simple(() -> files.get(fileId).setAlt("media"));
    }

    public void update(String fileId, File model) {
        simple(() -> files.update(fileId, model));
    }

    public File createFolder(ConsumerReturn<FileBuilder> consumer) {
        FileBuilder builder = consumer.accept(new FileBuilder()).mimeType(GMime.FOLDER);
        String fields = builder.getFields() == null ? DEFAULT_FIELDS : builder.getFields();
        return simple(() -> files.create(builder.build()).setFields(fields));
    }

    public File createFile(ConsumerReturn<FileBuilder> consumer) {
        FileBuilder builder = consumer.accept(new FileBuilder());
        AbstractInputStreamContent content = builder.getContent();
        File model = builder.build();
        return simple(() -> content == null ? files.create(model) : files.create(model, content));
    }

    public void deleteById(String fileId) {
        simple(() -> files.delete(fileId));
    }

    public FileList search(SearchBuilder b) {
        return simple(() -> files.list()
                .setFields(b.getFields())
                .setOrderBy(b.getOrderBy())
                .setPageSize(b.getPageSize())
                .setPageToken(b.getPageToken())
                .setQ(b.buildQuery()));
    }

    public File update(String sourceId, ConsumerReturn<FileBuilder> consumer) {
        FileBuilder builder = consumer.accept(new FileBuilder());
        if(builder.getFields() == null)builder.fields(DEFAULT_FIELDS);
        return simple(() -> files.update(sourceId, builder.build())
                .setRemoveParents(builder.getRemoveParents())
                .setAddParents(builder.getAddParents()));
    }

    //----------------------- PERMISSION

    public Permission createPermission(String fileId, ConsumerReturn<PermissionBuilder> consumer) {
        PermissionBuilder builder = consumer.accept(new PermissionBuilder());
        return simple(() -> permissions.create(fileId, builder.build()));
    }

    public PermissionList getPermission(String fileId, int pageSize, String pageToken) {
        return simple(() -> permissions.list(fileId).setPageSize(pageSize).setPageToken(pageToken));
    }


}