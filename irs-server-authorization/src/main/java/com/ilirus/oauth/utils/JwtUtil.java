package com.ilirus.oauth.utils;

import com.ilirus.oauth.jwt.JWTConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUtil {
    @Value("${jwt.jwt-secret-key}")
    private static String APP_SECRET_KEY;

    private static byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(APP_SECRET_KEY);
    private static SecretKey secretKey = Keys.hmacShaKeyFor(apiKeySecretBytes);

    public static String generateToken(String username, List<String> roles, boolean isRemember) {
        long expiration = isRemember? JWTConfig.EXPIRATION_REMEMBER:JWTConfig.EXPIRATION;
        Date date = new Date();

        String token = Jwts.builder()
                .setHeaderParam("typ",JWTConfig.TOKEN_TYPE)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .claim(JWTConfig.ROLE_CLAIMS, String.join(",",roles))
                .setIssuer("Ilirus")
                .setIssuedAt(date)
                .setSubject(username)
                .setExpiration(new Date(date.getTime() + expiration))
                .compact();
        return JWTConfig.TOKEN_PREFIX+token;
    }

    // 获取用户所有权限
    public static List<SimpleGrantedAuthority> getUserRolesInToken(String token) {
        String role = (String) getTokenBody(token)
                .get(JWTConfig.ROLE_CLAIMS);
        return Arrays.stream(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static String getUsernameInToken(String token) {
        return getTokenBody(token).getSubject();
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        Date expiredDate = getTokenBody(token).getExpiration();
        return expiredDate.before(new Date());
    }

}
