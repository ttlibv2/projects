package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongIdService;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.repository.AccountRepo;
import vn.conyeu.ts.domain.TsUser;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.repository.UserRepo;

import java.util.Optional;

@Service
public class UserService extends LongIdService<TsUser, UserRepo> {
    private final AccountRepo accountRepo;

    public UserService(UserRepo domainRepo, AccountRepo accountRepo) {
        super(domainRepo);
        this.accountRepo = accountRepo;
    }

    @Override
    public Optional<TsUser> update(Long entityId, ObjectMap overrides) {
        return super.update(entityId, overrides, user -> user.setReqUpdate(false));
    }

    public Optional<TsUser> update(Long userId, ClsUser clsUser) {
        Optional<TsUser> optional = findById(userId);
        if(optional.isEmpty()) throw noId(userId);
        else {
            TsUser tsUser = optional.get();
            tsUser.setTsId(clsUser.getId());
            return Optional.ofNullable(save(tsUser));
        }
    }
}