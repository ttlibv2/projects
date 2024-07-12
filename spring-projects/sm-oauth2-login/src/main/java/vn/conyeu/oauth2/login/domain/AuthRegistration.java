package vn.conyeu.oauth2.login.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.converter.SetString;
import vn.conyeu.common.domain.LongUIdDate;
import vn.conyeu.commons.beans.ObjectMap;
import java.util.Set;

//@formatter:off
@Entity @Table
@Getter() @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "rowId"))
//@formatter:on
public class AuthRegistration extends LongUIdDate<AuthRegistration> {

    @Column(nullable = false, unique = true)
    private String registrationId;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    @Column(nullable = false)
    private String redirectUri;

    @Convert(converter = SetString.class)
    private Set<String> scopes;

    private String clientName;

    private String grantType;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap configurationMetadata;

    @Column(length = 20, nullable = false)
    private String providerName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "providerId")
    private AuthProvider provider;

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
        if(provider != null) {
            this.providerName = provider.getProviderName();
        }
    }

}