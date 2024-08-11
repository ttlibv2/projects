package vn.conyeu.identity.restapi;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.identity.domain.*;
import vn.conyeu.identity.dtocls.SignUpDto;
import vn.conyeu.identity.service.AccountService;
import vn.conyeu.identity.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthRest {
    private final AuthenticationManager manager;
    private final AccountService service;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    @Autowired
    public AuthRest(AuthenticationManager manager, AccountService accountService, PasswordEncoder encoder,TokenService tokenService) {
        this.manager = manager;
        this.service = accountService;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    @PostMapping("check-email")
    public boolean checkEmail(@RequestParam String email) {
        return service.existsByEmail(email);
    }

    @PostMapping("check-phone")
    public boolean checkPhone(@RequestParam String phone) {
        return service.existsByPhone(phone);
    }

    @PostMapping("signup")
    public Object signUp(@RequestBody @Valid SignUpDto dto) {

        if (Objects.notBlank(dto.getEmail()) && checkEmail(dto.getEmail())) {
            throw new BadRequest("email_exist").message("The email exist.")
                    .arguments("email", dto.getEmail());
        }

        if (Objects.notBlank(dto.getPhone()) && checkPhone(dto.getPhone())) {
            throw new BadRequest("phone_exist").message("The phone exist.")
                    .arguments("phone", dto.getPhone());
        }

        SignupType signupType = dto.getSignupType();
        if (signupType == SignupType.email) {
            if (Objects.isBlank(dto.getEmail())) throw new BadRequest("account.emailEmpty")
                    .message("The email empty");
        } //
        else if (signupType == SignupType.phone) {
            if (Objects.isBlank(dto.getPhone())) throw new BadRequest("account.phoneEmpty")
                    .message("The phone empty");
        }

        AccountInfo info = new AccountInfo();
        info.assignFromMap(ObjectMap.fromJson(dto));

        String rawPwd = dto.getDecodePwd();

        Account account = new Account();
        account.setEmail(dto.getEmail());
        account.setPhone(dto.getPhone());
        account.setSignupType(dto.getSignupType());
        account.setPassword(encoder.encode(rawPwd));
        account.setRawPassword(rawPwd);
        account.setInfo(info);

        account = service.createNew(account);
        return tokenService.buildToken(account);
    }
}