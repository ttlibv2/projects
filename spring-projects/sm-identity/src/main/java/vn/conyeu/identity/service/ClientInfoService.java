package vn.conyeu.identity.service;

import org.springframework.stereotype.Service;
import vn.conyeu.identity.domain.ClientInfo;
import vn.conyeu.identity.repository.ClientInfoRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class ClientInfoService extends LongIdService<ClientInfo, ClientInfoRepo> {

    public ClientInfoService(ClientInfoRepo domainRepo) {
        super(domainRepo);
    }
}