package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.ts.dtocls.Converters;
import vn.conyeu.ts.odcore.domain.ClsUser;

//@formatter:off
@Getter @Setter @NoArgsConstructor @DynamicInsert @DynamicUpdate
@Entity @Table(uniqueConstraints = @UniqueConstraint(name = "USER_API_UID", columnNames = "apiId,userId"))
@AttributeOverride(name = "id", column = @Column(name = "uniqueId"))
@JsonIgnoreProperties({"api", "id"})
//@formatter:on
public class UserApi extends LongUId<UserApi> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private TsUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apiId")
    private ApiInfo api;

    @JsonProperty("user_name")
    @Column(length = 100, nullable = false)
    private String userName;

    @Column(length = 100, nullable = false)
    private String password;

    @ColumnDefault("1")
    @JsonProperty("auto_login")
    private Boolean autoLogin;

    @Column(length = 150)
    @JsonProperty("csrf_token")
    private String csrfToken;

    @Column(length = 1000)
    private String cookie;

    @ColumnDefault("0")
    @JsonProperty("allow_edit")
    private Boolean allowEdit;

    @Convert(converter = Converters.ClsUserConvert.class)
    @Column(columnDefinition = "json")
    private ClsUser userInfo;

    public boolean isAutoLogin() {
        return autoLogin != null && autoLogin;
    }

    @JsonProperty("api_code")
    public String getApiCode() {
        return api == null ? null : api.getCode();
    }

    public void setUniqueId(Long userId, ApiInfo api) {
        setApi(api);
        setUser(new TsUser(userId));
    }

    /**
     * Returns the allowEdit
     */
    public boolean isAllowEdit() {
        return allowEdit != null && allowEdit;
    }

    public ClsUser getClsUserBasic() {
        if(userInfo == null) return null;
        else return new ClsUser().setName(userInfo.getName())
                .setEmail(userInfo.getEmail()).setId(userInfo.getId());
    }
}