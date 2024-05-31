package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.UserAg;
import vn.conyeu.ts.repository.UserAgRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class UserAgService extends LongIdService<UserAg, UserAgRepo> {

    public UserAgService(UserAgRepo domainRepo) {
        super(domainRepo);
    }
}