package vn.conyeu.ts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ObjectMapToString;
import vn.conyeu.common.domain.LongId;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "apiId"))
//@formatter:on
public class ApiInfo extends LongId<ApiInfo> {

    @Column(length = 100, nullable = false, unique = true, updatable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 500, nullable = false, unique = true)
    private String baseUrl;

    @Column(length = 100, nullable = false)
    private String loginPath;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap defaultHeader;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap defaultQuery;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap loginInfo;

    @Column(length = 100)
    private String defaultUser;

    @Column(length = 100)
    private String defaultSecret;

    public void setUpdateUser(String userName) {
        if(Objects.notBlank(userName)) {
            setDefaultUser(userName);
        }
    }

    public void setUpdatePwd(String secret) {
        if(Objects.notBlank(secret)) {
            setDefaultSecret(secret);
        }
    }

    public ApiInfo copy() {
        ApiInfo cfg = new ApiInfo();
        cfg.id = id;
        cfg.code = code;
        cfg.title = title;
        cfg.baseUrl = baseUrl;
        cfg.loginPath = loginPath;
        cfg.defaultUser = defaultUser;
        cfg.defaultSecret = defaultSecret;
        cfg.defaultHeader = ObjectMap.clone(defaultHeader);
        cfg.defaultQuery = ObjectMap.clone(defaultQuery);
        return cfg;
    }


}