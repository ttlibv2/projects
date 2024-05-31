package vn.conyeu.account.service;

import org.springframework.stereotype.Service;
import vn.conyeu.account.domain.Privilege;
import vn.conyeu.account.repository.PrivilegeRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class PrivilegeService extends LongIdService<Privilege, PrivilegeRepo> {

    public PrivilegeService(PrivilegeRepo domainRepo) {
        super(domainRepo);
    }
}