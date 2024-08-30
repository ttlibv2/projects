package vn.conyeu.google.drives.builder;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import vn.conyeu.commons.utils.DateHelper;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.google.drives.DriveService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGFile {
    protected final DriveService service;
    protected File model;

    public AbstractGFile(DriveService service, File model) {
        validateModel(model);
        this.service = service;
        this.model = model;
    }

    protected void validateModel(File model) {
    }

    public void async() {
        this.model = service.openById(getId(), "*");
    }

    /**Gets the ID.*/
    public String getId() {
        return model.getId();
    }

    /**Gets the name*/
    public String getName() {
        return model.getName();
    }

    /**    Gets the URL that can be used to open*/
    public String getUrl() {
        return model.getWebViewLink();
    }

    public LocalDateTime getModifiedTime() {
        DateTime dt = model.getModifiedTime();
        return dt == null ? null : DateHelper.localDateTime(dt.getValue());
    }

    public LocalDateTime getCreatedTime() {
        DateTime dt = model.getCreatedTime();
        return dt == null ? null : DateHelper.localDateTime(dt.getValue());
    }

    public List<String> getPermissionIds() {
        List<String> list = model.getPermissionIds();
        return list == null ? new ArrayList<>() : list;
    }

    public List<Permission> getPermissions() {
        List<Permission> list = model.getPermissions();
        return list == null ? loadPermission() : list;
    }

    /**
     * Gets the list of viewers and commenters for this Folder/File.
     * If the user who executes the script does not have edit access to the Folder/File, this method returns an empty array.
     * */
    public List<Permission> getViewers() {
        return  getPermissions().stream().filter(p -> Objects.anyEquals(p.getRole(), "reader", "commenter")).toList();
    }

    public Permission getPermissionByEmail(String email) {
        return getPermissions().stream()
                .filter(p -> email.equals(p.getEmailAddress()))
                .findFirst().orElse(null);
    }

    public File setDescription(String description) {
        return model.setDescription(description);
    }

    public File setName(String name) {
        return model.setName(name);
    }

    public File setStarred(Boolean starred) {
        return model.setStarred(starred);
    }

    public void update()  {
        service.updateFile(getId(), model);
    }

    public Permission setOwner(String emailAddress, LocalDateTime expirationTime)  {
        return setSharing(Role.OWNER, Access.USER, emailAddress, expirationTime);
    }

    public Permission setSharing(Role role, Access access, String emailAddress, LocalDateTime expirationTime) {
        return service.createPermission(getId(), b -> b.role(role).type(access).emailAddress(emailAddress).expirationTime(expirationTime));
    }

    /**
     * Adds the given user to the list of editors for the Folder.
     * If the user was already on the list of viewers, this method promotes the user out of the list of viewers.
     *
     * @param emailAddress The email address of the user to add.
     */
    public Permission addEditor(String emailAddress)  {
        return service.createPermission(getId(), b -> b.editor(emailAddress));
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
        return service.createPermission(getId(), b -> b.viewer(emailAddress));
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

    public List<Permission> loadPermission()  {
            PermissionList list = service.getPermission(getId(), 1000, null);
            model.setPermissions(list.getPermissions());
            return list.getPermissions();
    }



}