package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;

//@formatter:off
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Entity @Table(indexes = @Index(name = "SUID_URL", columnList = "baseUrl,serviceUid", unique = true))
@AttributeOverride(name = "id", column = @Column(name = "apiId"))
//@formatter:on
public class ApiInfo extends LongUId<ApiInfo> {

    @Column(length = 100, nullable = false)
    @NotEmpty(message = "api.title.not_empty")
    private String title;

    @Column(length = 300)
    private String summary;

    @JsonProperty("base_url")
    @NotEmpty(message = "api.base_url.not_empty")
    @Column(length = 500, nullable = false)
    private String baseUrl;

    @JsonProperty("login_path")
    @NotEmpty(message = "api.login_path.not_empty")
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

    @JsonProperty("service_uid")
    @NotEmpty(message = "api.service_uid.not_empty")
    @Column(name = "service_uid", length = 100, nullable = false, updatable = false)
    private String serviceUid;

    @JsonProperty("service_name")
    @NotEmpty(message = "api.service_name.not_empty")
    @Column(length = 100, nullable = false, unique = true, updatable = false)
    private String serviceName;

    @ColumnDefault("0")
    @JsonProperty("is_system")
    @Column(updatable = false)
    private Boolean isSystem;

    @ColumnDefault("0")
    @JsonProperty("allow_copy")
    private Boolean allowCopy;

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
    public void setServiceUid(String unique_id) {
        this.serviceUid = unique_id;
        if(serviceName == null) {
            serviceName = unique_id;
        }
    }
}