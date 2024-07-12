package vn.conyeu.oauth2.login.repository;

import vn.conyeu.common.repository.LongUIdRepo;
import vn.conyeu.oauth2.login.domain.AuthRegistration;

import java.util.Optional;

public interface AuthRegistrationRepo extends LongUIdRepo<AuthRegistration> {
    Optional<AuthRegistration> findByRegistrationId(String registrationId);
}