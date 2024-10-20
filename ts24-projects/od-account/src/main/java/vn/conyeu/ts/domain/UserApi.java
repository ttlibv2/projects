package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.dtocls.Converters;
import vn.conyeu.ts.odcore.domain.ClsUser;

import java.util.List;

//@formatter:off
@Getter @Setter @NoArgsConstructor @DynamicInsert @DynamicUpdate
@Entity @Table(indexes = @Index(name = "USER_API_UID", columnList = "apiId,userId", unique = true))
@AttributeOverride(name = "id", column = @Column(name = "uniqueId"))
@JsonIgnoreProperties({"api", "user", "userInfo", "password", "apiId"})
//@formatter:on
public class UserApi extends LongUId<UserApi> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private TsUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apiId")
    private ApiInfo api;

    @JsonProperty("user_name")
    @NotEmpty(message = "api.user_name.notEmpty")
    @Column(length = 100, nullable = false)
    private String userName;

    @NotEmpty(message = "api.user_name.notEmpty")
    @Column(length = 100, nullable = false)
    private String password;

    @ColumnDefault("1")
    @JsonProperty("auto_login")
    private Boolean autoLogin;

    @Column(length = 150)
    @JsonProperty("csrf_token")
    private String csrfToken;

    @JsonProperty("cookie_value")
    @Column(length = 1000)
    private String cookie;

    @ColumnDefault("0")
    @JsonProperty("allow_edit")
    private Boolean allowEdit;

    @Convert(converter = Converters.ClsUserConvert.class)
    @Column(columnDefinition = "json")
    private ClsUser userInfo;

    @JsonIgnore
    public boolean isAutoLogin() {
        return autoLogin != null && autoLogin;
    }

    @JsonProperty("ua_id")
    public Long getId() {
        return super.getId();
    }

    @JsonProperty("app_uid")
    public String getServiceUid() {
        return api == null ? null : api.getAppUID();
    }

    @JsonProperty("app_name")
    public String getServiceName() {
        return api == null ? null : api.getAppName();
    }

    public void setUniqueId(Long userId, ApiInfo api) {
        setApi(api);
        setUser(new TsUser(userId));
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return user == null ? null : user.getId();
    }

    public Long getApiId() {
        return api == null ? null : api.getId();
    }

    /**
     * Returns the allowEdit
     */
    @JsonIgnore
    public boolean isAllowEdit() {
        return allowEdit != null && allowEdit;
    }

    @JsonProperty("user_info")
    public ClsUser getClsUserBasic() {
        if(userInfo == null) return null;
        else return new ClsUser().setName(userInfo.getName())
                .setEmail(userInfo.getEmail()).setId(userInfo.getId());
    }

    @JsonProperty("menu_links")
    public ObjectMap getMenuLinks() {
        if(userInfo == null) return null;
        return userInfo.getMenuLinks();
    }

    /**
     * Set the userInfo
     *
     * @param userInfo the value
     */
    public void setUserInfo(ClsUser userInfo) {
        this.userInfo = userInfo;
        if(userInfo != null) {
            setCookie(userInfo.getCookie());
            setCsrfToken(userInfo.getCsrfToken());
        }
    }

    public UserApi reset() {
        setId(null);
        password = null;
        csrfToken = null;
        cookie = null;
        userInfo = null;
        allowEdit = true;
        return this;
    }

    @Override
    public void assignFromMap(ObjectMap map, String... excludeFields) {
        super.assignFromMap(map, excludeFields);

        if(map.containsKey("password")) {
            List<String> fields = List.of(excludeFields);
            if(!fields.contains("password")) {
                setPassword(map.getString("password"));
            }
        }
    }
}