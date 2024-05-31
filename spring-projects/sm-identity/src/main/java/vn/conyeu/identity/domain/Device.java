package vn.conyeu.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.StringIdDate;
import vn.conyeu.identity.domain.enums.DeviceStatus;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "deviceId"))
//@formatter:on
public class Device extends StringIdDate<Device> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @Enumerated(EnumType.ORDINAL)
    private DeviceStatus status;

    @Column(length = 255, nullable = false)
    @Comment("Display name of the device")
    private String displayName; //

    @Column(length = 17)
    @Comment("International Mobile Equipment Identity (IMEI) of the device")
    private String imei;

    @ColumnDefault("0")
    @Comment("Indicates if the device is jailbroken or rooted. Only applicable to IOS and ANDROID platforms")
    private Boolean integrityJailbreak;

    @Column(length = 127)
    @Comment("Name of the manufacturer of the device")
    private String manufacturer;

    @Column(length = 127)
    @Comment("Model of the device")
    private String model;

    @Column(length = 127)
    @Comment("Version of the device OS")
    private String osVersion;

    @Column(length = 127, nullable = false)
    @Comment("OS platform of the device: WINDOW, MAC, ANDROID,IOS,...")
    private String platform;

    @Column(nullable = false)
    @ColumnDefault("1")
    @Comment("Indicates if the device is registered at local")
    private Boolean registered;

    @ColumnDefault("0")
    @Comment("Indicates if the device contains a secure hardware functionality")
    private Boolean secureHardwarePresent;

    @Column(length = 127)
    @Comment("Indicates if the device contains a secure hardware functionality")
    private String serialNumber;

    @Column(length = 256)
    @Comment("Windows Security identifier of the device")
    private String sid;

    @Comment("Windows Trusted Platform Module hash value")
    private String tpmPublicKeyHash;

    @Column(length = 16)
    private String ipV4;

    @Column(length = 100)
    private String ipV6;




}