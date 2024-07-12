package vn.conyeu.oauth2.login.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUIdDate;

//@formatter:off
@Entity @Table
@Getter() @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "rowId"))
//@formatter:on
public class AuthProvider extends LongUIdDate<AuthProvider> {

    @Column(length = 20, unique = true)
    private String providerName;

    @Column(nullable = false)
    private String authorizationUri;

    @Column(nullable = false)
    private String tokenUri;

    @Column(nullable = false)
    private String userInfoUri;

    @Column(nullable = false)
    private String userInfoAuthenticationMethod;

    @Column(nullable = false)
    private String userNameAttribute;

    @Column(nullable = false)
    private String jwkSetUri;

    @Column(nullable = false)
    private String issuerUri;

}