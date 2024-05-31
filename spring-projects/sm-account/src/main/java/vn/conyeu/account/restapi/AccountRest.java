package vn.conyeu.account.restapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.account.domain.Account;
import vn.conyeu.account.service.AccountService;
import vn.conyeu.common.restapi.LongIdRest;

@Slf4j
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/db.account")
public class AccountRest extends LongIdRest<Account, AccountService> {


    public AccountRest(AccountService service) {
        super(service);
    }
}