package vn.conyeu.identity.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.service.AccountService;

import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
public class AccountRest extends LongUIdRest<Account, AccountService> {

    public AccountRest(AccountService service) {
        super(service);
    }


}