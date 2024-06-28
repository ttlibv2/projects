package vn.conyeu.ts.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.repository.ApiInfoRepo;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ApiInfoService extends LongUIdService<ApiInfo, ApiInfoRepo> {

    public ApiInfoService(ApiInfoRepo apiRepo) {
        super(apiRepo);
    }

    @Cacheable(cacheNames = "apiInfo", key = "#apiCode")
    public Optional<ApiInfo> findByCode(String apiCode) {
        return repo().findByCode(apiCode);
    }

    public Optional<ApiInfo> findByBaseUrl(String baseUrl) {
        return repo().findByBaseUrl(baseUrl);
    }

    public boolean existsByCode(String apiCode) {
        return repo().existsByCode(apiCode);
    }

    /**
     * If apiCode exist then not save
     * @param apiCode the api code
     * @param consumer the consumer apply info
     * */
    public void tryCreateApi(String apiCode, Consumer<ApiInfo> consumer) {
        if(!existsByCode(apiCode)){
            ApiInfo info = new ApiInfo();
            consumer.accept(info);
            createNew(info);
        }
    }



}