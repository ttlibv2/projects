package vn.conyeu.identity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.identity.security.JwtConfig;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Returns the jwtConfig
     */
    public JwtConfig getConfig() {
        return jwtConfig;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public JwtBuilder generateToken( UserDetails userDetails) {
        return generateToken( userDetails.getUsername(),
                claims -> claims.add("authorities", userDetails.getAuthorities()));
    }

    public JwtBuilder generateToken( UserDetails userDetails, Map<String, Object> extraClaims) {
        return generateToken( userDetails.getUsername(), extraClaims);
    }

    public JwtBuilder generateToken(String subject, Map<String, Object> extraClaims) {
        return generateToken(subject, claims -> claims.add(extraClaims));
    }

    public JwtBuilder generateToken(String subject, Consumer<JwtBuilder.BuilderClaims> claimsConsumer) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + jwtConfig.getExpiration());

        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(subject).signWith(getSignInKey())
                .issuedAt(issuedAt).expiration(expiration).claims(null);

        if(claimsConsumer != null) {
            claimsConsumer.accept(jwtBuilder.claims());
        }

        return jwtBuilder;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String udUser = userDetails.getUsername();
        final String username = extractUsername(token);
        return username.equals(udUser) && !isTokenExpired(token);

    }

    public String getAuthHeader() {
        return jwtConfig.getHeaderAuth();
    }

    public String getTokenType() {
        return jwtConfig.getTokenType();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
            return Jwts.parser()
                    .verifyWith(getSignInKey()).build()
                    .parseSignedClaims(token.trim()).getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecretKey());
        return new SecretKeySpec(keyBytes, jwtConfig.getAlgorithm());
    }

    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(getAuthHeader());
        if(header != null && header.startsWith(getTokenType())) {
            return header.substring(getTokenType().length()).trim();
        }
        return null;
    }
}