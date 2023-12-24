package com.dkkhoa.possystem.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    private String secret = "JX5//l1yUIR4CA/32DT9HSRGkyJcmVqs4e5UhW8D/IG5aCC06fPlCFdF7flYoasJPq8HiQIbw7+tFNl9p3REAq4V7fREMABSiGauycXxzyxFN5qvXPlEMHTCCn3uyH36MAISjyHXS62Og/E5hj49mAB4VA5Wzx86PT+cOxNwNl7bukdVJ53fqk12bNGzh7oUPnTGQ0XwxHz4rBEEQonsM4eIHyvhEWctWClA+g==";
    private long expireIn = 60 * 1000; // 1 min

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + expireIn);


        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        System.out.println("Generated token: " + token);
        return token;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

}
