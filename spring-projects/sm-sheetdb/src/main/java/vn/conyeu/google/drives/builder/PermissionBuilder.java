package vn.conyeu.google.drives.builder;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.Permission;
import vn.conyeu.commons.utils.DateHelper;
import vn.conyeu.google.sheet.builder.XmlBuilder;
import java.time.LocalDateTime;

public class PermissionBuilder implements XmlBuilder<Permission> {
    private final Permission permission = new Permission();

    @Override
    public Permission build() {
        return permission;
    }

    /**
     * Whether the permission allows the file to be discovered through search.
     * This is only applicable for permissions of type domain or anyone.
     * @param allowFileDiscovery allowFileDiscovery or {@code null} for none
     */
    public PermissionBuilder allowFileDiscovery(Boolean allowFileDiscovery) {
        permission.setAllowFileDiscovery(allowFileDiscovery);
        return this;
    }

    /**
     * Whether the account associated with this permission has been deleted.
     * This field only pertains to user and group permissions.
     * @param deleted deleted or {@code null} for none
     */
    public PermissionBuilder deleted(Boolean deleted) {
        permission.setDeleted(deleted);
        return this;
    }

    /**
     * The "pretty" name of the value of the permission. The following is a list
     * of examples for each type of permission: - user - User's full name, as
     * defined for their Google account, such as "Joe Smith."  - group - Name of
     * the Google Group, such as "The Company Administrators."  - domain -
     * String domain name, such as "thecompany.com."  - anyone - No displayName
     * is present.
     * @param displayName displayName or {@code null} for none
     */
    public PermissionBuilder displayName(String displayName) {
        permission.setDisplayName(displayName);
        return this;
    }

    /**
     * The domain to which this permission refers.
     * @param domain domain or {@code null} for none
     */
    public PermissionBuilder domain(String domain) {
        permission.setDomain(domain);
        return this;
    }

    /**
     * The email address of the user or group to which this permission refers.
     * @param emailAddress emailAddress or {@code null} for none
     */
    public PermissionBuilder emailAddress(String emailAddress) {
        permission.setEmailAddress(emailAddress);
        return this;
    }

    /**
     * The time at which this permission will expire (RFC 3339 date-time).
     * Expiration times have the following restrictions: - They can only be set
     * on user and group permissions  - The time must be in the future  - The
     * time cannot be more than a year in the future
     * @param time expirationTime or {@code null} for none
     */
    public PermissionBuilder expirationTime(LocalDateTime time) {
        DateTime dt = time == null ? null : new DateTime(DateHelper.epochMilli(time));
        permission.setExpirationTime(dt);
        return this;
    }

    /**
     * The ID of this permission. This is a unique identifier for the grantee,
     * and is published in User resources as permissionId. IDs should be treated
     * as opaque values.
     * @param id id or {@code null} for none
     */
    public PermissionBuilder permissionId(String id) {
        permission.setId(id);
        return this;
    }

    /**
     * A link to the user's profile photo, if available.
     * @param photoLink photoLink or {@code null} for none
     */
    public PermissionBuilder photoLink(String photoLink) {
        permission.setPhotoLink(photoLink);
        return this;
    }

    /**
     * The role granted by this permission. While new values may be supported in
     * the future, the following are currently allowed: - owner  - organizer  -
     * fileOrganizer  - writer  - commenter - reader
     * @param role role or {@code null} for none
     */
    public PermissionBuilder role(Role role) {
        permission.setRole(role == null ? null : role.name());
        return this;
    }

    /**
     * The type of the grantee. Valid values are: - user  - group  - domain  -
     * anyone  When creating a permission, if type is user or group, you must
     * provide an emailAddress for the user or group. When type is domain, you
     * must provide a domain. There isn't extra information required for a
     * anyone type.
     * @param type type or {@code null} for none
     */
    public PermissionBuilder type(Access type) {
        permission.setType(type == null ? null : type.name());
        return this;
    }

    /**
     * Adds the given user to the list of editors for the Folder.
     * If the user was already on the list of viewers, this method promotes the user out of the list of viewers.
     * @param emailAddress The email address of the user to add.
     */
    public PermissionBuilder editor(String emailAddress) {
        role(Role.WRITER);
        type(Access.USER);
        emailAddress(emailAddress);
        return this;
    }

    /**
     * Adds the given user to the list of viewers for the Folder. If the user was already on the list of editors, this method has no effect.
     * @param emailAddress The email address of the user to add.
     */
    public PermissionBuilder viewer(String emailAddress) {
        role(Role.READER);
        type(Access.USER);
        emailAddress(emailAddress);
        return this;
    }
}