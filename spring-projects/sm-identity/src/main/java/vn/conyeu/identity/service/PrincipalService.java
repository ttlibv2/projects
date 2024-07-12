package vn.conyeu.identity.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import vn.conyeu.common.domain.I18N;
import vn.conyeu.common.exception.Unauthorized;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.domain.AccountToken;
import vn.conyeu.identity.domain.Principal;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.identity.helper.InvalidToken;
import vn.conyeu.identity.repository.AccountRepo;

import java.util.Optional;

@Service
public class PrincipalService implements UserDetailsService, UserDetailsChecker, MessageSourceAware {
    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private final AccountRepo accountRepo;
    private final TokenService tokenRepo;

    public PrincipalService(TokenService tokenRepo, AccountRepo accountRepo) {
        this.tokenRepo = tokenRepo;
        this.accountRepo = accountRepo;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        messages = new MessageSourceAccessor(messageSource);
    }


    //@Cacheable(value = "principal", key = "#username")
    public Principal loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username.startsWith("uid::")) {
            String uid = username.substring("uid::".length());
            return findById(Long.parseLong(uid)).orElseThrow(() -> noUser("uid", uid));
        }

        else if(username.startsWith("phone::")) {
            String phone = username.substring("phone::".length());
            return findByPhone(phone).orElseThrow(() ->noUser("phone", phone));
        }

        else if(username.startsWith("email::")) {
            String email = username.substring("email::".length());
            return findByEmail(email).orElseThrow(() ->noUser("email", email));
        }

        else throw noUser("username", username);
    }

    protected Optional<Principal> findByEmail(String email) {
        return accountRepo.findByEmail(email).map(this::createPrincipal);
    }

    protected Optional<Principal> findByPhone(String phone) {
        return accountRepo.findByPhone(phone).map(this::createPrincipal);
    }

    protected Optional<Principal> findById(Long accountId) {
        return accountRepo.findById(accountId).map(this::createPrincipal);
    }

    protected Principal createPrincipal(Account account) {
        return tokenRepo.buildPrincipal(account);
    }

    protected Unauthorized noUser(String field, Object data) {
        return new Unauthorized("user.404")
                .message(I18N.get("user.404", "User not exists."), field, data);
    }

    public void check(UserDetails user) {

        if (!user.isAccountNonLocked()) {
            throw new LockedException(
                    this.messages.getMessage("account.locked", "User account is locked"));
        }

        if (!user.isEnabled()) {
            throw new DisabledException(
                    this.messages.getMessage("account.disabled", "User is disabled"));
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException(
                    this.messages.getMessage("account.expired", "User account has expired"));
        }

        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(this.messages
                    .getMessage("account.credentialsExpired", "User credentials have expired"));
        }
    }

    public Principal authenticationByToken(String accessToken) {

        // session.v2.1234567899789
        String subject = tokenRepo.extractSubject(accessToken);

        // find token
        Optional<AccountToken> optional = tokenRepo.findByToken(subject);
        if(optional.isEmpty()) {
            String message = this.messages.getMessage("token.invalid", "The token not exist");
            throw new InvalidToken(message);
        }

        Account account = optional.get().getAccount();
        Principal principal = new Principal(account, subject);

        // validate info + state user
        check(principal);

        // update authentication to security
        IdentityHelper.setAuthentication(principal);

        return principal;
    }

}