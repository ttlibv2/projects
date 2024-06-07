package vn.conyeu.ts.repository;

import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.common.repository.LongUIdRepo;

import java.util.Optional;

public interface ApiInfoRepo extends LongUIdRepo<ApiInfo> {

    Optional<ApiInfo> findByCode(String apiCode);

    Optional<ApiInfo> findByBaseUrl(String baseUrl);

    boolean existsByCode(String apiCode);
}