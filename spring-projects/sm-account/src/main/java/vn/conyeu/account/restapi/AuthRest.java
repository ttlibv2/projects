package vn.conyeu.account.restapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.account.domain.Account;
import vn.conyeu.account.domain.SignupType;
import vn.conyeu.account.dtocls.SignInDto;
import vn.conyeu.account.dtocls.SignUpDto;
import vn.conyeu.account.service.AccountService;
import vn.conyeu.account.service.JwtService;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

@RestController
@RequestMapping("/auth")
public class AuthRest {
    private final AccountService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthRest(AccountService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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
            if (Objects.isBlank(dto.getEmail())) throw new BadRequest("user.empty")
                    .message("The email empty");
        } //
        else if (signupType == SignupType.phone) {
            if (Objects.isBlank(dto.getPhone())) throw new BadRequest("user.empty")
                    .message("The phone empty");
        }

        Account acc = new Account();
        acc.setEmail(dto.getEmail());
        acc.setPhone(dto.getPhone());
        acc.setSignupType(dto.getSignupType());
        acc.setPassword(dto.getSecret());

        acc = service.createNew(acc);

        return createAccessToken(acc);
    }

    @PostMapping("signin")
    public Object signin(@RequestBody SignInDto dto) {
        Authentication auth = new UsernamePasswordAuthenticationToken(dto.getUserWithPrefix(), dto.getSecret());
        Account account = (Account) authenticationManager.authenticate(auth).getPrincipal();
        ObjectMap tokenMap = createAccessToken(account);
        return ResponseEntity.status(200)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenMap.getString("access_token"))
                .body(tokenMap);
    }

    private ObjectMap createAccessToken(Account account) {
        return ObjectMap.setNew("token_type", "Bearer")
                .set("access_token", jwtService.generateToken(account))
                .set("expires_in", jwtService.getExpirationTime());
    }




}