package vn.conyeu.identity.helper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.common.exception.Unauthorized;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.domain.Principal;

import java.io.IOException;
import java.io.PrintWriter;

public class IdentityHelper {

    public static UsernamePasswordAuthenticationToken setAuthentication(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public static String extractCode(JwtException ex) {
        String name = ex.getClass().getSimpleName();
        name = name.replace("JwtException", "");
        name = name.replace("Exception", "");
        return "jwt."+name.trim().toLowerCase();
    }

    public static void sendResponse(int status, HttpServletResponse response, Object jsonData) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(status);

        PrintWriter out = response.getWriter();
        out.print(ObjectMap.fromJson(jsonData).toJson());
        out.flush();
    }

    public static void sendError(HttpServletResponse response, ObjectMap jsonData) throws IOException {
       sendResponse(401, response, jsonData);
    }


    public static BaseException notUser(String field, Object data) {
        return new NotFound("user.404")
                .message("Lỗi không tìm thấy người dùng")
                .arguments(field, data);
    }

    public static Long extractUserId() {
       return getPrincipal().getUserId();
    }

    public static Principal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new Unauthorized("not_login").message("Người dùng chưa đăng nhập");
        if(authentication.getPrincipal() instanceof Principal principal) return principal;
        else throw new Unauthorized("not_principal");
    }
}