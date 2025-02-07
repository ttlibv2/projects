package vn.conyeu.google.drives.builder;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.User;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.DateHelper;
import vn.conyeu.commons.utils.Jsons;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.google.drives.DriveService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractGFile {
    protected final DriveService drives;
    protected final File fileModel;

    public AbstractGFile(DriveService drives, File file) {
        this.drives = Asserts.notNull(drives, "@DriveService");
        this.fileModel = Asserts.notNull(file);
        this.validateModel(file);
    }

    protected void validateModel(File model) {}

    /**Gets the ID.*/
    public String getId() {
        return fileModel.getId();
    }

    /**Gets the name*/
    public String getName() {
        return fileModel.getName();
    }

    /**    Gets the URL that can be used to open*/
    public String getUrl() {
        return fileModel.getWebViewLink();
    }

    /**
     * The MIME type of the file.
     * @return value or {@code null} for none
     */
    public String getMimeType() {
        return fileModel.getMimeType();
    }

    public List<String> getParents() {
        return asyncIf(fileModel::getParents, "parents");
    }

    public List<User> getOwners() {
        return asyncIf(fileModel::getOwners, "owners");
    }

    public Map<String, String> getProperties() {
        Map<String, String> map = asyncIf(fileModel::getProperties, "properties");
        if(map == null) {
            map = new HashMap<>();
            fileModel.setProperties(map);
        }
        return map;
    }

    public String getProperty(String field) {
        Map<String, String> map = getProperties();
        return map == null ? null : map.get(field);
    }

    public User getOwner() {
        List<User> userList = getOwners();
        return Objects.getItemAt(userList, 0);
    }

    public LocalDateTime getModifiedTime() {
        DateTime dt = fileModel.getModifiedTime();
        return dt == null ? null : DateHelper.localDateTime(dt.getValue());
    }

    public LocalDateTime getCreatedTime() {
        DateTime dt = fileModel.getCreatedTime();
        return dt == null ? null : DateHelper.localDateTime(dt.getValue());
    }

    public List<String> getPermissionIds() {
        List<String> list = fileModel.getPermissionIds();
        return list == null ? new ArrayList<>() : list;
    }

    public List<Permission> getPermissions() {
        return asyncIf(fileModel::getPermissions, "permissions");
    }

    /**
     * Gets the list of viewers and commenters for this Folder/File.
     * If the user who executes the script does not have edit access to the Folder/File, this method returns an empty array.
     * */
    public List<Permission> getViewers() {
        return  getPermissions().stream().filter(p -> Objects
                .anyEquals(p.getRole(), "reader", "commenter")).toList();
    }

    public Permission getPermissionByEmail(String email) {
        return getPermissions().stream()
                .filter(p -> email.equals(p.getEmailAddress()))
                .findFirst().orElse(null);
    }

    public File setDescription(String description) {
        return fileModel.setDescription(description);
    }

    public File setName(String name) {
        return fileModel.setName(name);
    }

    public File setStarred(Boolean starred) {
        return fileModel.setStarred(starred);
    }

    public void update()  {
        drives.update(getId(), fileModel);
    }

    public Permission setOwner(String emailAddress, LocalDateTime expirationTime)  {
        return setSharing(Role.OWNER, Access.USER, emailAddress, expirationTime);
    }

    public Permission setSharing(Role role, Access access, String emailAddress, LocalDateTime expirationTime) {
        return drives.createPermission(getId(), b -> b.role(role).type(access).emailAddress(emailAddress).expirationTime(expirationTime));
    }

    /**
     * Adds the given user to the list of editors for the Folder.
     * If the user was already on the list of viewers, this method promotes the user out of the list of viewers.
     *
     * @param emailAddress The email address of the user to add.
     */
    public Permission addEditor(String emailAddress)  {
        return drives.createPermission(getId(), b -> b.editor(emailAddress));
    }

    /**
     * Adds the given array of users to the list of editors for the Folder.
     * If any of the users were already on the list of viewers, this method promotes them out of the list of viewers.
     * @param emailAddresses An array of email addresses of the users to add.
     */
    public void addEditors(String[] emailAddresses)  {
        for (String emailAddress : emailAddresses) {
            addEditor(emailAddress);
        }
    }

    /**
     * Adds the given user to the list of viewers for the Folder. If the user was already on the list of editors, this method has no effect.
     * @param emailAddress The email address of the user to add.
     */
    public Permission addViewer(String emailAddress)  {
        return drives.createPermission(getId(), b -> b.viewer(emailAddress));
    }

    /**
     * Adds the given array of users to the list of viewers for the Folder.
     * If any of the users were already on the list of editors, this method has no effect for them.
     * @param emailAddresses An array of email addresses of the users to add.
     */
    public void addViewers(String[] emailAddresses) {
        for (String emailAddress : emailAddresses) {
            addViewer(emailAddress);
        }
    }

    protected File asyncFile(String fields) {
        updateModel(drives.openById(getId(), fields));
        return fileModel;
    }

    protected <E> E asyncIf(Supplier<E> supplier, String fields) {
        Asserts.notNull(supplier, "Supplier<E>");
        if(supplier == null) asyncFile(fields);
        return supplier.get();
    }


    protected AbstractGFile updateModel(File updateModel) {
        Jsons.update(this.fileModel, updateModel);
        return this;
    }

    public AbstractGFile moveTo(String folderId) {
        String removeParent = Objects.getItemAt(fileModel.getParents(), 0);
        File model = drives.update(getId(), f -> f.removeParents(removeParent).addParents(folderId));
        updateModel(model);
        return this;
    }



}