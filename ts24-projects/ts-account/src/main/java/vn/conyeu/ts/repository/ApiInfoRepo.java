package vn.conyeu.ts.repository;

import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.common.repository.LongIdRepo;

import java.util.Optional;

public interface ApiInfoRepo extends LongIdRepo<ApiInfo> {

    Optional<ApiInfo> findByCode(String apiCode);

    Optional<ApiInfo> findByBaseUrl(String baseUrl);


}