package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "apiId"))
//@formatter:on
public class ApiInfo extends LongUId<ApiInfo> {

    @Column(length = 100, nullable = false, unique = true, updatable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String title;

    @JsonProperty("base_url")
    @Column(length = 500, nullable = false, unique = true)
    private String baseUrl;

    @JsonProperty("login_path")
    @Column(length = 100, nullable = false)
    private String loginPath;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap headers;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap queries;

    @Transient
    private String username;

    @Transient
    private String password;


    @JsonProperty("api_id")
    public Long getId() {
        return super.getId();
    }

    public ApiInfo copy() {
        ApiInfo cfg = new ApiInfo();
        cfg.id = id;
        cfg.code = code;
        cfg.title = title;
        cfg.baseUrl = baseUrl;
        cfg.loginPath = loginPath;
        cfg.headers = ObjectMap.clone(headers);
        cfg.queries = ObjectMap.clone(queries);
        return cfg;
    }


}