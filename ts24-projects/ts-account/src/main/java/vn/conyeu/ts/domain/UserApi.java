package vn.conyeu.ts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ObjectMapToString;
import vn.conyeu.common.domain.DomainId;
import vn.conyeu.commons.beans.ObjectMap;

import java.io.Serializable;

//@formatter:off
@Getter @Setter @NoArgsConstructor @DynamicInsert @DynamicUpdate
@Entity @Table(uniqueConstraints = @UniqueConstraint(name = "USER_API_UID", columnNames = "apiId,userId"))
@AttributeOverride(name = "id", column = @Column(name = "uaId"))
//@formatter:on
public class UserApi extends DomainId<UserApi, UserApi.UserApiId> {

    @EmbeddedId
    private UserApiId id;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap headers;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap queries;

    @Column(length = 100)
    private String userName;

    @Column(length = 100)
    private String secret;

    @Column(length = 150)
    private String csrfToken;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap userInfo;

    /**
     * Returns the api
     */
    public ApiInfo getConfig() {
        return id == null ? null : id.api;
    }

    /**
     * Returns the user
     */
    public TsUser getUser() {
        return id == null ? null : id.user;
    }

    public void setApiId(Long userId, ApiInfo apiConfig) {
        setId(new UserApiId(new TsUser(userId), apiConfig));
    }

    public ObjectMap getHeaders() {
        headers = ObjectMap.ifNull(headers);
        return headers;
    }

    public ObjectMap getQueries() {
        queries = ObjectMap.ifNull(queries);
        return queries;
    }

    @Embeddable @NoArgsConstructor
    public static class UserApiId implements Serializable {

        //@Id
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "apiId", nullable = false)
        private ApiInfo api;

       // @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId", nullable = false)
        private TsUser user;

        public UserApiId(TsUser user, ApiInfo api) {
            this.api = api;
            this.user = user;
        }


    }

}