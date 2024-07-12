package vn.conyeu.oauth2.login.service;

import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.oauth2.login.domain.AuthRegistration;
import vn.conyeu.oauth2.login.repository.AuthRegistrationRepo;

public class AuthProviderService extends LongUIdService<AuthRegistration, AuthRegistrationRepo> {

    public AuthProviderService(AuthRegistrationRepo domainRepo) {
        super(domainRepo);
    }



}