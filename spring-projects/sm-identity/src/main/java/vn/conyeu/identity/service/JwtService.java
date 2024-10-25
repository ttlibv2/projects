package vn.conyeu.identity.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import vn.conyeu.identity.security.JwtConfig;

@Service
public class JwtService {
    private final JwtConfig config;

    public JwtService(JwtConfig config) {
        this.config = config;
    }

    public final JwtConfig getConfig() {
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
}