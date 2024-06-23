package vn.conyeu.oauth2.helper;

import com.nimbusds.oauth2.sdk.GrantType;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.commons.utils.Objects;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public final class OAuth2Helper {

    public static ClientAuthenticationMethod forClientMethod(String authMethod) {
        try {
            if(Objects.isBlank(authMethod)) return null;
            else return ClientAuthMethods.valueOf(authMethod.toUpperCase()).method;
        }//
        catch (IllegalArgumentException exp) {
            return new ClientAuthenticationMethod(authMethod);
        }
    }

    public static AuthorizationGrantType forGrantTypeName(String grantType) {
           try{
               if(Objects.isBlank(grantType)) return null;
               else return GrantTypes.valueOf(grantType.toUpperCase()).gt;
           }//
        catch (IllegalArgumentException exp) {
              throw new BadRequest("grant_type.404").throwable(exp);
        }

    }

    public static AuthenticationMethod forUserInfoAuthMethod(String userInfoAuthenticationMethod) {
        try{
            if(Objects.isBlank(userInfoAuthenticationMethod)) return null;
            else return AuthenticationMethods.valueOf(userInfoAuthenticationMethod.toUpperCase()).at;
        }//
        catch (IllegalArgumentException exp) {
            return new AuthenticationMethod(userInfoAuthenticationMethod);
        }
    }


    public enum AuthenticationMethods {
        HEADER(AuthenticationMethod.HEADER),
        FORM(AuthenticationMethod.FORM),
        QUERY(AuthenticationMethod.QUERY);
        final AuthenticationMethod at;

        AuthenticationMethods(AuthenticationMethod authenticationMethod) {
            this.at = authenticationMethod;
        }
    }

    public enum ClientAuthMethods {

        /**
         * Clients that have received a client secret from the authorisation
         * server authenticate with the authorisation server in accordance with
         * section 3.2.1 of OAuth 2.0 using HTTP Basic authentication. This is
         * the default if no method has been registered for the client.
         */
        CLIENT_SECRET_BASIC(ClientAuthenticationMethod.CLIENT_SECRET_BASIC),

        /**
         * Clients that have received a client secret from the authorisation
         * server authenticate with the authorisation server in accordance with
         * section 3.2.1 of OAuth 2.0 by including the client credentials in
         * the request body.
         */
        CLIENT_SECRET_POST(ClientAuthenticationMethod.CLIENT_SECRET_POST),

        /**
         * Clients that have received a client secret from the authorisation
         * server, create a JWT using an HMAC SHA algorithm, such as HMAC
         * SHA-256. The HMAC (Hash-based Message Authentication Code) is
         * calculated using the value of client secret as the shared key. The
         * client authenticates in accordance with section 2.2 of (JWT) Bearer
         * Token Profiles and OAuth 2.0 Assertion Profile.
         */
        CLIENT_SECRET_JWT(ClientAuthenticationMethod.CLIENT_SECRET_JWT),

        /**
         * Clients that have registered a public key sign a JWT using the RSA
         * algorithm if a RSA key was registered or the ECDSA algorithm if an
         * Elliptic Curve key was registered (see JWA for the algorithm
         * identifiers). The client authenticates in accordance with section
         * 2.2 of (JWT) Bearer Token Profiles and OAuth 2.0 Assertion Profile.
         */
        PRIVATE_KEY_JWT(ClientAuthenticationMethod.PRIVATE_KEY_JWT),

        /**
         * PKI mutual TLS OAuth client authentication. See OAuth 2.0 Mutual TLS
         * Client Authentication and Certificate Bound Access Tokens (RFC
         * 8705), section 2.1.
         */
        TLS_CLIENT_AUTH(new ClientAuthenticationMethod("tls_client_auth")),

        /**
         * Self-signed certificate mutual TLS OAuth client authentication. See
         * OAuth 2.0 Mutual TLS Client Authentication and Certificate Bound
         * Access Tokens (RFC 8705), section 2.2.
         */
        SELF_SIGNED_TLS_CLIENT_AUTH(new ClientAuthenticationMethod("self_signed_tls_client_auth")),

        /**
         * Client authentication by means of a request object at the
         * authorisation or PAR endpoints. Intended for OpenID Connect
         * Federation 1.0 clients undertaking automatic registration. See
         * OpenID Connect Federation 1.0.
         */
        REQUEST_OBJECT(new ClientAuthenticationMethod("request_object")),

        /**
         * The client is a public client as defined in OAuth 2.0 and does not
         * have a client secret.
         */
        NONE(ClientAuthenticationMethod.NONE);
        private final ClientAuthenticationMethod method;

        ClientAuthMethods(ClientAuthenticationMethod method) {
            this.method = method;
        }

        public ClientAuthenticationMethod method() {
            return method;
        }
    }

    public enum GrantTypes {


        /**
         * Authorisation code. Client authentication required only for
         * confidential clients.
         */
        AUTHORIZATION_CODE(AuthorizationGrantType.AUTHORIZATION_CODE),

        /**
         * Implicit. Client authentication is not performed (except for signed
         * OpenID Connect authentication requests).
         */
        IMPLICIT(new AuthorizationGrantType("implicit")),


        /**
         * Refresh token. Client authentication required only for confidential
         * clients.
         */
        REFRESH_TOKEN(AuthorizationGrantType.REFRESH_TOKEN),

        /**
         * Client credentials. Client authentication is required.
         */
        CLIENT_CREDENTIALS(AuthorizationGrantType.CLIENT_CREDENTIALS),


        /**
         * JWT bearer, as defined in RFC 7523. Explicit client authentication
         * is optional.
         */
        JWT_BEARER(AuthorizationGrantType.JWT_BEARER),


        /**
         * SAML 2.0 bearer, as defined in RFC 7522. Explicit client
         * authentication is optional.
         */
        SAML2_BEARER(new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:saml2-bearer")),


        /**
         * Device Code, as defined in OAuth 2.0 Device Flow for
         * Browserless and Input Constrained Devices. Explicit client
         * authentication is optional.
         */
        DEVICE_CODE(AuthorizationGrantType.DEVICE_CODE),


        /**
         * Client Initiated Back-channel Authentication (CIBA), as defined in
         * OpenID Connect Client Initiated Backchannel Authentication Flow -
         * Core 1.0. Explicit client authentication is optional.
         */
        CIBA(new AuthorizationGrantType("urn:openid:params:grant-type:ciba")),


        /**
         * Token Exchange, as defined in RFC 8693. Explicit client
         * authentication is optional.
         */
        TOKEN_EXCHANGE(new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:token-exchange"));

        private final AuthorizationGrantType gt;

        GrantTypes(AuthorizationGrantType gt) {
            this.gt = gt;
        }

        public AuthorizationGrantType grantType() {
            return gt;
        }
    }

}
