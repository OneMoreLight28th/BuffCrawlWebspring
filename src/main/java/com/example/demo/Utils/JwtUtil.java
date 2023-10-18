package com.example.demo.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    public static final Long JWT_TTL = 24 * 60 * 60 * 1000L; // 24h
    public static final String JWT_KEY = "34fqf34aqefe352514adfegagf32fq34rf31dlhvjanv";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createJWT(String userId, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(userId, ttlMillis);
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String userId, Long ttlMillis) {
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setSubject(userId)
                .setIssuer("hmmftyv")
                .setIssuedAt(now)
                .signWith(secretKey)
                .setExpiration(expDate);
    }

    public static SecretKey generalKey() {
        return Keys.hmacShaKeyFor(JWT_KEY.getBytes());
    }

    public static TokenValidationResult parseJWT(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(generalKey())
                    .build()
                    .parseClaimsJws(jwt);
            Claims claims = claimsJws.getBody();

            if (!claims.getIssuer().equals("hmmftyv")) {
                System.out.println("Issuer错了");
                return new TokenValidationResult(false);
            }


            if (claims.getExpiration().before(new Date())) {
                System.out.println("时间错了");
                return new TokenValidationResult(false);
            }

            return new TokenValidationResult(true);

        } catch (Exception e) {
            System.out.println(e);
            return new TokenValidationResult(false);
        }
    }

    public static class TokenValidationResult {
        private boolean isValid;


        public TokenValidationResult(boolean isValid) {
            this.isValid = isValid;
        }

        public boolean isValid() {
            return isValid;
        }
    }


    //测试
    public static void main(String[] args) {
        // 示例用法
        String userId = "root";
        String jwt = createJWT(userId, JWT_TTL);
        System.out.println("Generated JWT: " + jwt);

        TokenValidationResult validationResult = parseJWT(jwt);
        System.out.println("Validation Result: " + validationResult.isValid());
        if (validationResult.isValid()) {
            System.out.println("Valid username: 成功");
        }
    }
}

