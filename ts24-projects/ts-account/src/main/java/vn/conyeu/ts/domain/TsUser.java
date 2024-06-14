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
@JsonIgnoreProperties({"account"})
@AttributeOverride(name = "id", column = @Column(name = "accountId"))
//@formatter:on
public class TsUser extends LongIdDate<TsUser> {

    @JsonProperty("full_name")
    @Column(length = 100)
    private String fullName;

    @JsonProperty("user_code")
    @Column(length = 50)
    private String userCode;

    @JsonProperty("room_code")
    @Column(length = 50)
    private String roomCode;

    @ColumnDefault("0")
    @JsonProperty("required_update")
    private Boolean requiredUpdate;

    public TsUser(Long entityId) {
        super(entityId);
    }

    @JsonProperty("user_id")
    public Long getId() {
        return super.getId();
    }
}