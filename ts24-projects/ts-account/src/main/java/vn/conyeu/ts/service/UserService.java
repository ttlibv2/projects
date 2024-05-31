package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.domain.AccountInfo;
import vn.conyeu.identity.repository.AccountInfoRepo;
import vn.conyeu.identity.repository.AccountRepo;
import vn.conyeu.identity.service.AccountService;
import vn.conyeu.ts.domain.TsUser;
import vn.conyeu.ts.repository.UserRepo;
import vn.conyeu.common.service.LongIdService;

import java.util.Optional;

@Service
public class UserService extends LongIdService<TsUser, UserRepo> {
    private final AccountRepo accountRepo;

    public UserService(UserRepo domainRepo, AccountRepo accountRepo) {
        super(domainRepo);
        this.accountRepo = accountRepo;
    }

    public TsUser checkUser(Long userId) {
        Optional<TsUser> userOptional = findById(userId);
        if(userOptional.isPresent()) return userOptional.get();
        else {
            TsUser user = TsUser.createDefaultUser();
            user.setId(userId);
            return createNew(user);
        }
    }
}