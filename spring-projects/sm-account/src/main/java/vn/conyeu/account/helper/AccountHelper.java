package vn.conyeu.account.helper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import vn.conyeu.account.domain.Account;
import vn.conyeu.common.domain.LogDetail;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.common.exception.Unauthorized;
import vn.conyeu.commons.beans.ObjectMap;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

public final class AccountHelper {

    public static UsernamePasswordAuthenticationToken setAuthentication(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public static Long getPrincipalId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new Unauthorized("user.notLogin");
        Object principal = authentication.getPrincipal();
        if(principal instanceof Account acc) return acc.getId();
        else throw new Unauthorized().message("The principal not instanceof Account");
    }

    public static String extractCode(JwtException ex) {
       String name = ex.getClass().getSimpleName();
       name = name.replace("JwtException", "");
       name = name.replace("Exception", "");
       return "jwt."+name.trim().toLowerCase();
    }

    public static void sendError(HttpServletResponse response, ObjectMap jsonData) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        out.print(jsonData.toJson());
        out.flush();
    }


    public static BaseException notUser(String field, Object data) {
        return new NotFound("user.404")
                .message("Lỗi không tìm thấy người dùng")
                .arguments(field, data);
    }
}