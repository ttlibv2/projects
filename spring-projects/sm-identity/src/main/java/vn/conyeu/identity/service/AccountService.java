package vn.conyeu.identity.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongIdService;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.repository.AccountRepo;

import java.util.Optional;

@Service
public class AccountService extends LongIdService<Account, AccountRepo> {

    public AccountService(AccountRepo accountRepo) {
        super(accountRepo);
    }

    public Account createUser(Account info, PasswordEncoder encoder) {
        String pwdEncode = encoder.encode(info.getPassword());
        info.setPassword(pwdEncode);
        return createNew(info);
    }

    public void changePass(Long accountId, String newPassword) {
        entityRepo.updatePassword(accountId, newPassword);
    }

    public Optional<Account> findByEmail(String email) {
        return entityRepo.findByEmail(email);
    }

    public Optional<Account> findByPhone(String phone) {
        return entityRepo.findByPhone(phone);
    }


    public boolean existsByEmail(String email) {
        return entityRepo.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return entityRepo.existsByPhone(phone);
    }
}