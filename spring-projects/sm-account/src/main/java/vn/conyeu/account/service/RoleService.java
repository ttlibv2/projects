package vn.conyeu.account.service;

import org.springframework.stereotype.Service;
import vn.conyeu.account.domain.Role;
import vn.conyeu.account.repository.RoleRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class RoleService extends LongIdService<Role, RoleRepo> {

    public RoleService(RoleRepo domainRepo) {
        super(domainRepo);
    }
}