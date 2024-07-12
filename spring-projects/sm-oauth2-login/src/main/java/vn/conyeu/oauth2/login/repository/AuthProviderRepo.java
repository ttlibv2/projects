package vn.conyeu.oauth2.login.repository;

import vn.conyeu.common.repository.LongUIdRepo;
import vn.conyeu.oauth2.login.domain.AuthProvider;

import java.util.Optional;

public interface AuthProviderRepo extends LongUIdRepo<AuthProvider> {
    Optional<AuthProvider> findByProviderName(String providerName);
}