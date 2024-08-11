package vn.conyeu.identity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.DateHelper;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.domain.AuthToken;
import vn.conyeu.identity.domain.AccountToken;
import vn.conyeu.identity.domain.Principal;
import vn.conyeu.identity.repository.AccountTokenRepo;
import vn.conyeu.identity.security.JwtConfig;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class TokenService extends LongUIdService<AccountToken, AccountTokenRepo> {
    private final JwtConfig config;

    public TokenService(AccountTokenRepo domainRepo, JwtConfig config) {
        super(domainRepo);
        this.config = config;
    }

    public JwtConfig getConfig() {
        return config;
    }

    public String resolveToken(HttpServletRequest request) {
        String tokenType = config.getTokenType();
        String header = request.getHeader(config.getHeaderAuth());
        if (header != null && header.startsWith(tokenType)) {
            return header.substring(tokenType.length()).trim();
        }
        return null;
    }

    public SecretKey buildSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(config.getSecret());
        return new SecretKeySpec(keyBytes, config.getAlgorithm());
    }

    /**
     * Returns the JWT payload, either a {@code Claims} instance.
     * @return the JWT payload
     */
    public Claims extractClaim(String token) {
        return Jwts.parser()
                .verifyWith(buildSecretKey()).build()
                .parseSignedClaims(token.trim()).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsMapper) {
        Claims claims = extractClaim(token);
        return claimsMapper.apply(claims);
    }

    /**
     * Returns the JWT subject value
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Returns the JWT <a href="https://www.rfc-editor.org/rfc/rfc7519.html#section-4.1.4">
     * <code>exp</code></a> (expiration) timestamp or {@code null} if not present.
     *
     * <p>A JWT obtained after this timestamp should not be used.</p>
     *
     * @return the JWT {@code exp} value or {@code null} if not present.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Returns true id token is valid
     * */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private String buildTokenId(Long accountId) {
        long milis = DateHelper.epochMilli();
        return "session.v2."+accountId+milis;
    }

    private JwtBuilder buildJwt(Principal principal) {
        Asserts.notBlank(principal.getSessionId(), "Principal#getSessionId()");
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + config.getExpiration());
        return Jwts.builder().signWith(buildSecretKey())
                .subject(principal.getSessionId())
                .issuedAt(issuedAt).expiration(expiration)
                .claim("authorities", principal.getAuthorities());
    }

    public JwtBuilder buildJwt(Account account, String sessionId) {
        return buildJwt(new Principal(account, sessionId));
    }

    public AuthToken buildToken(Principal principal) {
        return buildToken(buildJwt(principal));
    }

    public AuthToken buildToken(Account account) {
        String sessionId = createAndSaveSessionId(account.getId());
        return buildToken(buildJwt(account, sessionId));
    }

    private AuthToken buildToken(JwtBuilder tokenBuilder) {
        return new AuthToken()
                .setExpiresIn(config.getExpiration())
                .setTokenType(config.getTokenType())
                .setAccessToken(tokenBuilder.compact());
    }

    public String createAndSaveSessionId(Long accountId) {
        String token = buildTokenId(accountId);
        AccountToken accountToken = new AccountToken();
        accountToken.setAccount(new Account(accountId));
        accountToken.setToken(token);
        return save(accountToken).getToken();
    }

    public boolean isTokenValid(String token, Principal principal) {
        String udUser = principal.getSessionId();
        final String sessionId = extractSubject(token);
        return sessionId.equals(udUser) && !isTokenExpired(token);
    }


    public Optional<AccountToken> findByToken(String token) {
        return repo().findByToken(token);
    }
}