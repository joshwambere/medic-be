package com.medic.servlet.utils;

import com.medic.servlet.db.Database;
import com.medic.servlet.user.dtos.Role;
import com.medic.servlet.user.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class TokenUtil {
    private static final String SECRET_KEY = "0123456789012345678901234567890123456789012345678901234567890123";// 1 hour

    public static String generateJWT(String username, Role role, String id) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put("username", username);

        Instant now = Instant.now();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = SECRET_KEY.getBytes();
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        String jwtToken = Jwts.builder()
                .claim("role", role)
                .claim("username", username)
                .setSubject(username)
                .setId(id)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.HOURS)))
                .signWith(signatureAlgorithm, signingKey)
                .compact();

        return jwtToken;
    }

    public static User getUserFromToken(String token) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = SECRET_KEY.getBytes();
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        return Database.findUser(username);
    }
}
