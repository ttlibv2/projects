package vn.conyeu.identity.domain.enums;

public enum DeviceStatus {
    /**Use activated devices to create and delete Device user links*/
    ACTIVE,

    /**	Deactivation causes a Device to lose all device user links. Set the Device status to DEACTIVATED before deleting it.*/
    DEACTIVATED,

    /**Use suspended devices to create and delete device user links. You can only unsuspend or deactivate suspended devices.*/
    SUSPENDED,

    /**Returns a suspended Device to ACTIVE.*/
    UNSUSPENDED
}