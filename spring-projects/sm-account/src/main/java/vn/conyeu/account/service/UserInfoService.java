package vn.conyeu.account.service;

import org.springframework.stereotype.Service;
import vn.conyeu.account.domain.AccountInfo;
import vn.conyeu.account.repository.UserInfoRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class UserInfoService extends LongIdService<AccountInfo, UserInfoRepo> {

    public UserInfoService(UserInfoRepo domainRepo) {
        super(domainRepo);
    }
}