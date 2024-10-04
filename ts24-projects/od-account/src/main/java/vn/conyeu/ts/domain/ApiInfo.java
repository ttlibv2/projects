package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

//@formatter:off
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Entity @Table(indexes = @Index(name = "SUID_URL", columnList = "baseUrl,appUID", unique = true))
@AttributeOverride(name = "id", column = @Column(name = "apiId"))
//@formatter:on
public class ApiInfo extends LongUId<ApiInfo> {

    @Column(length = 100, nullable = false)
    @NotBlank(message = "api.title.notBlank")
    private String title;

    @Column(length = 300)
    private String summary;

    @JsonProperty("base_url")
    @NotBlank(message = "api.base_url.notBlank")
    @Column(length = 500, nullable = false)
    private String baseUrl;

    @JsonProperty("login_path")
    @NotBlank(message = "api.login_path.notBlank")
    @Column(length = 100, nullable = false)
    private String loginPath;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap headers;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap queries;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap links;

    @Convert(converter = MapString.class)
    @JsonProperty("cfg_link")
    @Column(columnDefinition = "json")
    private ObjectMap cfgLink;

    @JsonProperty("app_uid")
    @NotBlank(message = "api.app_uid.notBlank")
    @Column(name="app_uid" ,length = 100, nullable = false, updatable = false)
    private String appUID;

    @JsonProperty("app_name")
    @NotBlank(message = "api.app_name.notBlank")
    @Column(name="app_name", length = 100, nullable = false, unique = true, updatable = false)
    private String appName;

    @ColumnDefault("0")
    @JsonProperty("is_system")
    @Column(updatable = false)
    private Boolean isSystem;

    @ColumnDefault("0")
    @JsonProperty("allow_copy")
    private Boolean allowCopy;

    private Long targetId;

    @Transient
    @JsonProperty("user_api")
    private UserApi userApi;



    @JsonProperty("api_id")
    public Long getId() {
        return super.getId();
    }

    /**
     * Set the service unique_id
     * @param unique_id the value
     */
    public void setAppUID(String unique_id) {
        this.appUID = unique_id;
        if(appName == null) {
            appName = unique_id;
        }
    }

    /**
     * Set the userApi
     *
     * @param userApi the value
     */
    public ApiInfo userApi(UserApi userApi) {
        this.userApi = userApi;
        return this;
    }
}