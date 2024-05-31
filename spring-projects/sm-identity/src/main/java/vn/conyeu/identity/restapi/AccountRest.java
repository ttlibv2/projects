package vn.conyeu.identity.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.common.restapi.LongIdRest;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.service.AccountService;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
public class AccountRest extends LongIdRest<Account, AccountService> {

    public AccountRest(AccountService service) {
        super(service);
    }

}