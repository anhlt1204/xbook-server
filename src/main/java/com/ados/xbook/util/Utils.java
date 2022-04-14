package com.ados.xbook.util;

import com.ados.xbook.exception.InvalidException;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

public class Utils {

    @Value("${jwt.secret}")
    private static String secret;

    public static String getUsernameFromToken(String token) {
        if (Strings.isNullOrEmpty(token)) {
            throw new InvalidException("Token is null or empty");
        }

        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
