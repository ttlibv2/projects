package vn.conyeu.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import vn.conyeu.account.domain.Account;
import vn.conyeu.account.repository.AccountRepo;
import vn.conyeu.common.service.LongIdService;

import java.util.Optional;

@Service
public class AccountService extends LongIdService<Account, AccountRepo>  {

    @Autowired
    public AccountService(AccountRepo domainRepo) {
        super(domainRepo);
    }

    public boolean existsByEmail(String email) {
        return entityRepo.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return entityRepo.existsByPhone(phone);
    }

    /**
     * Find account without accountId
     * @param userId the account id
     * */
    public Optional<Account> findById(Long userId) {
        return super.findById(userId);
    }

    /**
     * Find account without email
     * @param email the account email
     * */
    public Optional<Account> findByEmail(String email) {
        return entityRepo.findByEmail(email);
    }

    /**
     * Find account without phone
     * @param phone the account phone
     * */
    public Optional<Account> findByPhone(String phone) {
        return entityRepo.findByPhone(phone);
    }

}