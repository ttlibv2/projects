package vn.conyeu.oauth2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import vn.conyeu.common.converter.Converts;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.oauth2.helper.OAuth2Helper;
import vn.conyeu.oauth2.helper.OAuth2Helper.ClientAuthMethods;

import java.util.Set;

//@formatter:off
@Entity @Table
@Getter() @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "clientUid"))
//@formatter:on
public class ClientProvider extends LongUId<ClientProvider> {

    @Column(length = 50, nullable = false)
    private String registrationId;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    @Column(nullable = false)
    private String authMethod; // ClientAuthenticationMethod

    @Column(nullable = false)
    private String grantType; // AuthorizationGrantType
    private String redirectUri;

    @Convert(converter = Converts.StringSet.class)
    private Set<String> scopes;

    private String clientName;

    // the uri for the authorization endpoint.
    private String authorizationUri;

    // the uri for the token endpoint.
    private String tokenUri;

    // the uri for the user info endpoint.
    private String userInfoUri;

    // the authentication method for the user info endpoint.
    @Column(name = "user_info_method")
    private String userInfoAuthenticationMethod;

    // the attribute name used to access the user's name from the user info response.
    @Column(name = "user_attr_name")
    private String userNameAttributeName;

    // the uri for the JSON Web Key (JWK) Set endpoint.
    private String jwkSetUri;

    // the issuer identifier uri for the OpenID Connect 1.0 provider or the OAuth 2.0 Authorization Server.
    private String issuerUri;

    // the metadata describing the provider's configuration.
    @Convert(converter = Converts.MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap configurationMetadata;

    public ClientRegistration buildRegistration() {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId(clientId).clientSecret(clientSecret)
                .clientAuthenticationMethod(OAuth2Helper.forClientMethod(authMethod))
                .authorizationGrantType(OAuth2Helper.forGrantTypeName(grantType))
                .redirectUri(redirectUri).scope(scopes).clientName(clientName)
                .authorizationUri(authorizationUri).tokenUri(tokenUri)
                .userInfoUri(userInfoUri).userNameAttributeName(userNameAttributeName)
                .userInfoAuthenticationMethod(OAuth2Helper.forUserInfoAuthMethod(userInfoAuthenticationMethod))
                .jwkSetUri(jwkSetUri).issuerUri(issuerUri)
                .providerConfigurationMetadata(configurationMetadata).build();
    }



}
