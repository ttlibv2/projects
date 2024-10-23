package vn.conyeu.identity.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.domain.Principal;
import vn.conyeu.identity.service.AccountService;
import vn.conyeu.identity.service.TokenService;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
public class AccountRest extends LongUIdRest<Account, AccountService> {
    final TokenService tokenService;

    public AccountRest(AccountService service, TokenService tokenService) {
        super(service);
        this.tokenService = tokenService;
    }

    @PostMapping("signout")
    public Object signout(@AuthenticationPrincipal Principal principal) {
        tokenService.deleteById(principal.getToken().getId());
        SecurityContextHolder.getContext().setAuthentication(null);
        return ObjectMap.setNew("alert_msg", "Đăng xuất thành công");
    }
}