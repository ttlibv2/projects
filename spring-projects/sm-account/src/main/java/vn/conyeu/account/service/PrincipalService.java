package vn.conyeu.account.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.conyeu.account.domain.Account;
import vn.conyeu.account.repository.AccountRepo;

import static vn.conyeu.account.helper.AccountHelper.notUser;

@Service("userDetailsService")
public class PrincipalService implements UserDetailsService {
    private final AccountRepo accountRepo;

    public PrincipalService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith("uid::")) {
            Long uid = Long.parseLong(username.substring(5));
            return accountRepo.findById(uid).orElseThrow(() -> notUser("uid", uid));
        }

        else if(username.startsWith("phone::")) {
            String phone = username.substring(7);
            return accountRepo.findByPhone(phone).orElseThrow(() -> notUser("phone", phone));
        }

        else if(username.startsWith("email::")) {
            String email = username.substring(7);
            return accountRepo.findByEmail(email).orElseThrow(() -> notUser("email", email));
        }

        else throw notUser("username", username);
    }
}