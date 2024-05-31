package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongIdDate;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"account", "tsSecret"})
@AttributeOverride(name = "id", column = @Column(name = "accountId"))
//@formatter:on
public class TsUser extends LongIdDate<TsUser> {

//    @MapsId
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "accountId", nullable = false)
//    private Account account;

    public static TsUser createDefaultUser() {
        TsUser user = new TsUser();
        user.setTsEmail("::EMAIL::");
        user.setTsName("::NAME::");
        user.setTsSecret("::SECRET::");
        user.setUserCode("::USER_CODE::");
        user.setRoomCode("::ROOM_CODE::");
        user.setRequiredUpdate(true);
        return user;
    }

    @JsonProperty("ts_email")
    @Column(length = 100, nullable = false)
    private String tsEmail;

    @Column(length = 100, nullable = false)
    private String tsSecret;

    @JsonProperty("ts_name")
    @Column(length = 100, nullable = false)
    private String tsName;

    @JsonProperty("user_code")
    @Column(length = 50, nullable = false)
    private String userCode;

    @JsonProperty("room_code")
    @Column(length = 50, nullable = false)
    private String roomCode;

    @ColumnDefault("0")
    private Boolean requiredUpdate;

    public TsUser(Long entityId) {
        super(entityId);
    }

    @JsonProperty("user_id")
    public Long getId() {
        return super.getId();
    }
}