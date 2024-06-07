package vn.conyeu.identity.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.repository.AccountRepo;

import java.util.Optional;

@Service
public class AccountService extends LongUIdService<Account, AccountRepo> {

    public AccountService(AccountRepo accountRepo) {
        super(accountRepo);
    }

    public Account createUser(Account info, PasswordEncoder encoder) {
        String pwdEncode = encoder.encode(info.getPassword());
        info.setPassword(pwdEncode);
        return createNew(info);
    }

    @CacheEvict("principal")
    public void changePass(Long accountId, String newPassword) {
        repo().updatePassword(accountId, newPassword);
    }

   // @CacheEvict(value = "principal")
    public Account save(Account entity) {
        return super.save(entity);
    }

    public Optional<Account> findByEmail(String email) {
        return repo().findByEmail(email);
    }

    public Optional<Account> findByPhone(String phone) {
        return repo().findByPhone(phone);
    }


    public boolean existsByEmail(String email) {
        return repo().existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return repo().existsByPhone(phone);
    }
}