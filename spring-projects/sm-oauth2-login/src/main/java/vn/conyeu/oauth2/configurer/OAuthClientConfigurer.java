package vn.conyeu.oauth2.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import vn.conyeu.identity.security.CommonSecurity;

public class OAuthClientConfigurer extends CommonSecurity {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {

    }
}
