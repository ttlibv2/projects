package vn.conyeu.google.drives.builder;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.User;
import vn.conyeu.commons.utils.Lists;
import vn.conyeu.google.drives.GMime;
import vn.conyeu.google.sheet.builder.XmlBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBuilder implements XmlBuilder<File> {
    private final File file = new File();
    private Map<String, String> appProperties;
    private List<Permission> permissions;
    private Map<String, String> properties;
    private AbstractInputStreamContent content;

    @Override
    public File build() {
        file.setAppProperties(appProperties);
        file.setPermissions(permissions);
        file.setProperties(properties);

        return file;
    }

    /**
     * A collection of arbitrary key-value pairs which are private to the
     * requesting app. Entries with null values are cleared in update and copy
     * requests.
     */
    public FileBuilder appProperties(String field, String value) {
        appProperties().put(field, value);
        return this;
    }

    private Map<String, String> appProperties() {
        if(appProperties == null) {
            appProperties = new HashMap<>();
        }
        return appProperties;
    }

    /**
     * Capabilities the current user has on this file. Each capability
     * corresponds to a fine-grained action that a user may take.
     * @param capabilities capabilities or {@code null} for none
     */
    public FileBuilder capabilities(File.Capabilities capabilities) {
        file.setCapabilities(capabilities);
        return this;
    }

    /**
     * Additional information about the content of the file. These fields are
     * never populated in responses.
     * @param contentHints contentHints or {@code null} for none
     */
    public FileBuilder contentHints(File.ContentHints contentHints) {
        file.setContentHints(contentHints);
        return this;
    }

    /**
     * Whether the options to copy, print, or download this file, should be
     * disabled for readers and commenters.
     * @param copyRequiresWriterPermission copyRequiresWriterPermission or
     * {@code null} for none
     */
    public FileBuilder copyRequiresWriterPermission(Boolean copyRequiresWriterPermission) {
        file.setCopyRequiresWriterPermission(copyRequiresWriterPermission);
        return this;
    }

    /**
     * A short description of the file.
     * @param description description or {@code null} for none
     */
    public FileBuilder description(String description) {
        file.setDescription(description);
        return this;
    }

    /**
     * ID of the shared drive the file resides in. Only populated for items in
     * shared drives.
     * @param driveId driveId or {@code null} for none
     */
    public FileBuilder driveId(String driveId) {
        file.setDriveId(driveId);
        return this;
    }

    /**
     * Whether the file has been explicitly trashed, as opposed to recursively
     * trashed from a parent folder.
     * @param explicitlyTrashed explicitlyTrashed or {@code null} for none
     */
    public FileBuilder explicitlyTrashed(Boolean explicitlyTrashed) {
        file.setExplicitlyTrashed(explicitlyTrashed);
        return this;
    }

    /**
     * The final component of fullFileExtension. This is only available for
     * files with binary content in Google Drive.
     * @param fileExtension fileExtension or {@code null} for none
     */
    public FileBuilder fileExtension(String fileExtension) {
        file.setFileExtension(fileExtension);
        return this;
    }

    /**
     * The color for a folder as an RGB hex string. The supported colors are
     * published in the folderColorPalette field of the About resource. If an
     * unsupported color is specified, the closest color in the palette will be
     * used instead.
     * @param folderColorRgb folderColorRgb or {@code null} for none
     */
    public FileBuilder folderColorRgb(String folderColorRgb) {
        file.setFolderColorRgb(folderColorRgb);
        return this;
    }

    /**
     * The full file extension extracted from the name field. May contain
     * multiple concatenated extensions, such as "tar.gz". This is only
     * available for files with binary content in Google Drive. This is
     * automatically updated when the name field changes, however it is not
     * cleared if the new name does not contain a valid extension.
     * @param fullFileExtension fullFileExtension or {@code null} for none
     */
    public FileBuilder fullFileExtension(String fullFileExtension) {
        file.setFullFileExtension(fullFileExtension);
        return this;
    }


    /**
     * Whether this file has a thumbnail. This does not indicate whether the
     * requesting app has access to the thumbnail. To check access, look for the
     * presence of the thumbnailLink field.
     * @param hasThumbnail hasThumbnail or {@code null} for none
     */
    public FileBuilder hasThumbnail(Boolean hasThumbnail) {
        file.setHasThumbnail(hasThumbnail);
        return this;
    }

    /**
     * The ID of the file's head revision. This is currently only available for
     * files with binary content in Google Drive.
     * @param headRevisionId headRevisionId or {@code null} for none
     */
    public FileBuilder headRevisionId(String headRevisionId) {
        file.setHeadRevisionId(headRevisionId);
        return this;
    }

    /**
     * A static, unauthenticated link to the file's icon.
     * @param iconLink iconLink or {@code null} for none
     */
    public FileBuilder iconLink(String iconLink) {
        file.setIconLink(iconLink);
        return this;
    }

    /**
     * The ID of the file.
     * @param id id or {@code null} for none
     */
    public FileBuilder fileId(String id) {
        file.setId(id);
        return this;
    }

    /**
     * Additional metadata about image media, if available.
     * @param imageMediaMetadata imageMediaMetadata or {@code null} for none
     */
    public FileBuilder imageMediaMetadata(File.ImageMediaMetadata imageMediaMetadata) {
        file.setImageMediaMetadata(imageMediaMetadata);
        return this;
    }

    /**
     * The MIME type of the file. Google Drive will attempt to automatically
     * detect an appropriate value from uploaded content if no value is
     * provided. The value cannot be changed unless a new revision is uploaded.
     * If a file is created with a Google Doc MIME type, the uploaded content
     * will be imported if possible. The supported import formats are published
     * in the About resource.
     * @param mimeType mimeType or {@code null} for none
     */
    public FileBuilder mimeType(GMime mimeType) {
        file.setMimeType(mimeType == null ? null : mimeType.getMime());
        return this;
    }

    /**
     * The MIME type of the file. Google Drive will attempt to automatically
     * detect an appropriate value from uploaded content if no value is
     * provided. The value cannot be changed unless a new revision is uploaded.
     * If a file is created with a Google Doc MIME type, the uploaded content
     * will be imported if possible. The supported import formats are published
     * in the About resource.
     * @param mimeType mimeType or {@code null} for none
     */
    public FileBuilder mimeType(String mimeType) {
        file.setMimeType(mimeType);
        return this;
    }

    /**
     * The name of the file. This is not necessarily unique within a folder.
     * Note that for immutable items such as the top level folders of shared
     * drives, My Drive root folder, and Application Data folder the name is
     * constant.
     * @param name name or {@code null} for none
     */
    public FileBuilder name(String name) {
        file.setName(name);
        return this;
    }

    /**
     * The IDs of the parent folders which contain the file. If not specified as
     * part of a create request, the file will be placed directly in the user's
     * My Drive folder. If not specified as part of a copy request, the file
     * will inherit any discoverable parents of the source file. Update requests
     * must use the addParents and removeParents parameters to modify the
     * parents list.
     * @param parents parents or {@code null} for none
     */
    public FileBuilder parents(String... parents) {
        file.setParents(parents == null ? null : Lists.newList(parents));
        return this;
    }

    /**
     * The full list of permissions for the file.
     * @param permission permissions or {@code null} for none
     */
    public FileBuilder permissions(Permission permission) {
        permissions().add(permission);
        return this;
    }

    private List<Permission> permissions() {
        if(permissions == null) permissions = new ArrayList<>();
        return permissions;
    }

    /**
     * A collection of arbitrary key-value pairs which are visible to all apps.
     */
    public FileBuilder properties(String field, String value) {
        properties().put(field, value);
        return this;
    }

    private Map<String, String> properties() {
        if(properties == null) properties = new HashMap<>();
        return properties;
    }

    /**
     * Whether the user has starred the file.
     * @param starred starred or {@code null} for none
     */
    public FileBuilder starred(Boolean starred) {
        file.setStarred(starred);
        return this;
    }

    /**
     * Whether the file has been trashed, either explicitly or from a trashed
     * parent folder. Only the owner may trash a file, and other users cannot
     * see files in the owner's trash.
     * @param trashed trashed or {@code null} for none
     */
    public FileBuilder trashed(Boolean trashed) {
        file.setTrashed(trashed);
        return this;
    }

    /**
     * Set the content
     * @param content the value
     */
    public FileBuilder content(AbstractInputStreamContent content) {
        this.content = content;
        return this;
    }

    public AbstractInputStreamContent getContent() {
        return content;
    }
}