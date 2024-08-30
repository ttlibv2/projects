package vn.conyeu.google.drives;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import vn.conyeu.google.drives.builder.FileBuilder;
import vn.conyeu.google.drives.builder.PermissionBuilder;
import vn.conyeu.google.sheet.builder.ConsumerReturn;

import static vn.conyeu.google.core.ExecuteFunc.simple;

public class DriveService {
    private final Drive service;
    private final Drive.Files files;
    private final Drive.Permissions permissions;

    public DriveService(Drive service) {
        this.service = service;
        this.files = service.files();
        this.permissions = service.permissions();
    }

    public File getRoot() {
        return simple(() -> files.get("root"));
    }

    public File openById(String fileId, String fields) {
        return simple(() -> files.get(fileId).setFields(fields));
    }

    public void updateFile(String fileId, File model) {
        simple(() -> files.update(fileId, model));
    }

    public File createFolder(ConsumerReturn<FileBuilder> consumer) {
        FileBuilder builder = consumer.accept(new FileBuilder()).mimeType(GMime.FOLDER);
        return simple(() -> files.create(builder.build()));
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

    //----------------------- PERMISSION

    public Permission createPermission(String fileId, ConsumerReturn<PermissionBuilder> consumer) {
        PermissionBuilder builder = consumer.accept(new PermissionBuilder());
        return simple(() -> permissions.create(fileId, builder.build()));
    }

    public PermissionList getPermission(String fileId, int pageSize, String pageToken) {
        return simple(() -> permissions.list(fileId).setPageSize(pageSize).setPageToken(pageToken));
    }

}