package vn.conyeu.identity.helper;

import io.jsonwebtoken.JwtException;

public class InvalidToken extends JwtException  {

    public InvalidToken(String message) {
        super(message);
    }
}