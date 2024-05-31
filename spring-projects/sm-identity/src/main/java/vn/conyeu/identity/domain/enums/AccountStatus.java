package vn.conyeu.identity.domain.enums;

public enum AccountStatus {

    /**
     * The Application User is provisioned and is enabled to use the app. This status also occurs if the app has the IMPORT_PROFILE_UPDATES feature enabled and user import is confirmed, or if the app doesn't have provisioning enabled.
     */
    ACTIVE,

    /**
     * The Application User is provisioned, but isn't enabled to use the app. Application Users in this status can be reactivated with a password reset or permanently deleted.
     */
    INACTIVE,

    /**
     * The Application User is created based on imported data.
     */
    IMPORTED,

    /**
     * The imported user is matched with an existing Application User.
     */
    MATCHED,

    /**
     * The Application User was imported, but the user-matching operation was skipped.
     */
    UNASSIGNED,

    /**
     * The Application User is provisioned, but isn't enabled to use the app. Application Users in this status can be reactivated without a password reset.
     */
    SUSPENDED,

    /**
     * The Application User is provisioned, but in a pending state and can't use the app. The status moves to ACTIVE when the Application User is activated.
     */
    PENDING,

    /**
     * The Application User was created but not provisioned. This status can occur when manual provisioning acknowledgment is required.
     */
    APPROVED,

    /**
     * The Application User is disabled and waiting for deprovisioning acknowledgment. The Application User can be deleted after deprovisioning acknowledgment.
     */
    REVOKED,

    /**
     * The Application User is now migrated to use implicit app assignment.
     */
    IMPLICIT,

    /**
     * The Application User doesn't have externalId set and the background provisioning operation is queued. This applies to apps with the PUSH_NEW_USERS feature enabled.
     */
    STAGED,

    /**
     * The background provisioning operation completed and the Application User was assigned an externalId successfully.
     */
    PROVISIONED
}