package vn.conyeu.oauth2.login.utils;

import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public final class OAuth2Helper {

    public enum IAuthenticationMethod {
        HEADER(AuthenticationMethod.HEADER),
        FORM(AuthenticationMethod.FORM),
        QUERY(AuthenticationMethod.QUERY);
        final AuthenticationMethod method;

        IAuthenticationMethod(AuthenticationMethod method) {
            this.method = method;
        }

        public String value() {
            return method.getValue();
        }

        public static AuthenticationMethod formMethod(String name) {
            try {return IAuthenticationMethod.valueOf(name).method;}
            catch (Exception ignored) {return new AuthenticationMethod(name);}
        }

    }

    public enum IClientAuthenticationMethod {
        CLIENT_SECRET_BASIC(ClientAuthenticationMethod.CLIENT_SECRET_BASIC),
        CLIENT_SECRET_POST(ClientAuthenticationMethod.CLIENT_SECRET_POST),
        CLIENT_SECRET_JWT(ClientAuthenticationMethod.CLIENT_SECRET_JWT),
        PRIVATE_KEY_JWT(ClientAuthenticationMethod.PRIVATE_KEY_JWT),
        NONE(ClientAuthenticationMethod.NONE);
        final ClientAuthenticationMethod method;

        IClientAuthenticationMethod(ClientAuthenticationMethod method) {
            this.method = method;
        }

        public String value() {
            return method.getValue();
        }

        public static ClientAuthenticationMethod formMethod(String name) {
            try {return IClientAuthenticationMethod.valueOf(name).method;}
            catch (Exception ignored) {return new ClientAuthenticationMethod(name);}
        }
    }

    public enum IAuthorizationGrantType {
        AUTHORIZATION_CODE(AuthorizationGrantType.AUTHORIZATION_CODE),
        REFRESH_TOKEN(AuthorizationGrantType.REFRESH_TOKEN),
        CLIENT_CREDENTIALS(AuthorizationGrantType.CLIENT_CREDENTIALS),
        JWT_BEARER(AuthorizationGrantType.JWT_BEARER),
        DEVICE_CODE(AuthorizationGrantType.DEVICE_CODE);

        private final AuthorizationGrantType grantType;

        IAuthorizationGrantType(AuthorizationGrantType grantType) {
            this.grantType = grantType;
        }

        /**
         * Returns the grantType
         */
        public String value() {
            return grantType.getValue();
        }

        public static AuthorizationGrantType forGrantType(String name) {

            //for-name
            try {return IAuthorizationGrantType.valueOf(name).grantType;}
            catch (Exception ignored) { }

            //for-value
            for(IAuthorizationGrantType grantType:values()) {
                if(name.equalsIgnoreCase(grantType.value())){
                    return grantType.grantType;
                }
            }

            // other
            return new AuthorizationGrantType(name);
        }


    }
}