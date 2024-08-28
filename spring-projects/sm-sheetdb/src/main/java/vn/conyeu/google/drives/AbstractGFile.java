package vn.conyeu.google.drives;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import vn.conyeu.commons.utils.DateHelper;
import vn.conyeu.commons.utils.Objects;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractGFile {
    protected final Drive.Files files;
    protected final Drive drive;
    protected File model;

    AbstractGFile(Drive drive, File model) {
        validateModel(model);
        this.drive = drive;
        this.files = drive.files();
        this.model = model;
    }

    protected void validateModel(File model) {
    }

    public void async() throws IOException {
        this.model = files.get(getId()).setFields("*").execute();
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

    public void update() throws IOException {
        files.update(getId(), model).execute();
    }

    public Permission setOwner(String emailAddress, LocalDateTime expirationTime) throws IOException {
        return setSharing(DriveApp.Role.OWNER, DriveApp.Access.USER, emailAddress, expirationTime);
    }

    public Permission setSharing(DriveApp.Role role, DriveApp.Access access, String emailAddress, LocalDateTime expirationTime) throws IOException {
        Permission per = buildPermission(role, access, emailAddress, expirationTime);
        return drive.permissions().create(getId(), per).execute();
    }

    /**
     * Adds the given user to the list of editors for the Folder.
     * If the user was already on the list of viewers, this method promotes the user out of the list of viewers.
     *
     * @param emailAddress The email address of the user to add.
     */
    public void addEditor(String emailAddress) throws IOException {
        setSharing(DriveApp.Role.WRITER, DriveApp.Access.USER, emailAddress, null);
    }

    /**
     * Adds the given array of users to the list of editors for the Folder.
     * If any of the users were already on the list of viewers, this method promotes them out of the list of viewers.
     * @param emailAddresses An array of email addresses of the users to add.
     */
    public void addEditors(String[] emailAddresses) throws IOException {
        for (String emailAddress : emailAddresses) {
            addEditor(emailAddress);
        }
    }

    /**
     * Adds the given user to the list of viewers for the Folder. If the user was already on the list of editors, this method has no effect.
     * @param emailAddress The email address of the user to add.
     */
    public void addViewer(String emailAddress) throws IOException {
        setSharing(DriveApp.Role.READER, DriveApp.Access.USER, emailAddress, null);
    }

    /**
     * Adds the given array of users to the list of viewers for the Folder.
     * If any of the users were already on the list of editors, this method has no effect for them.
     * @param emailAddresses An array of email addresses of the users to add.
     */
    public void addViewers(String[] emailAddresses) throws IOException {
        for (String emailAddress : emailAddresses) {
            addViewer(emailAddress);
        }
    }

    public List<Permission> loadPermission()  {
        try {
            PermissionList list = drive.permissions().list(getId()).setPageSize(1000).execute();
            model.setPermissions(list.getPermissions());
            return list.getPermissions();
        }
        catch (IOException exp) {
            throw new DriveException(exp);
        }
    }


    protected Permission buildPermission(DriveApp.Role role, DriveApp.Access access, String emailAddress, LocalDateTime expirationTime) {
        Permission per = new Permission().setRole(role.value).setType(access.name());
        if(expirationTime != null) {
            DateTime dt = new DateTime(DateHelper.epochMilli(expirationTime));
            per.setExpirationTime(dt);
        }

        if(emailAddress != null) {
            per.setEmailAddress(emailAddress);
        }

        return per;
    }
}