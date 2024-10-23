package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.restclient.ClientLogger.ResponseLog;
import vn.conyeu.restclient.ClientLogger.RequestLog;
import vn.conyeu.restclient.ClientLogger;
import vn.conyeu.ts.domain.HttpLog;
import vn.conyeu.ts.domain.TsUser;
import vn.conyeu.ts.repository.HttpLogRepo;

import java.util.List;
import java.util.Optional;

@Service
public class HttpLogService extends LongUIdService<HttpLog, HttpLogRepo> {

    public HttpLogService(HttpLogRepo domainRepo) {
        super(domainRepo);
    }

    public Optional<HttpLog> findByRequestId(String requestId) {
        return repo().findByRequestId(requestId);
    }

    public List<HttpLog> findByModel(String modelName, String modelId) {
        return repo().findByModel(modelName, modelId);
    }


    public HttpLog save( String requestId, ClientLogger logger) {
        Asserts.hasLength(requestId, "The request id required");
        if(!existsByRequestId(requestId)) {
            HttpLog log = new HttpLog(requestId);
            applyRequestLog(log, logger.request());
            applyResponseLog(log, logger.response());
            return saveAndReturn(log);
        }

        else {
           return save(requestId, logger.response());
        }
    }

    public HttpLog save(String requestId, RequestLog request) {
        Optional<HttpLog> optional = findByRequestId(requestId);
        if(optional.isPresent()) return optional.get();
        else {
            HttpLog log = new HttpLog(requestId);
            applyRequestLog(log, request);
            return saveAndReturn(log);
        }
    }

    public HttpLog save(String requestId, ResponseLog responseLog) {
        HttpLog log = findByRequestId(requestId).orElseThrow(()-> noRequestId(requestId));
        applyResponseLog(log, responseLog);
        return saveAndReturn(log);
    }

    public boolean existsByRequestId(String requestId) {
        return repo().existsByRequestId(requestId);
    }

    public BaseException noRequestId(String requestId) {
        return new NotFound("requestid.404")
                .detail("requestid", requestId).message("The request id not exist.");
    }

    private void applyRequestLog(HttpLog log, RequestLog request) {
        Long userLogin = request.userLogin();
        if(userLogin != null) log.setUser(new TsUser(userLogin));

        log.setModelId(request.headerRemoveFirst("tsModelId"));
        log.setModel(request.headerRemoveFirst("tsModelName"));
        log.setRequest(request.map());
    }

    private void applyResponseLog(HttpLog log, ResponseLog response) {
        log.setResponse(response.map());
        log.setStatus(response.getStatus());
    }
}