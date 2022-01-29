package com.scheduler.service.impl;

import com.scheduler.service.IAuthService;
import com.scheduler.service.ISecretService;
import com.scheduler.service.ISettingService;
import com.scheduler.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 10/4/2021
 */
public class AuthService implements IAuthService {
    private static final String CYPHER_PARAMETER_NAME = "coach_helper.cypher";

    private ISecretService secretService;
    private ISettingService settingService;

    public AuthService(ISecretService secretService, ISettingService settingService) {
        this.secretService = secretService;
        this.settingService = settingService;
    }

    @Override
    public String validateUser(Map<String, Object> params) {
        Map<String, Object> headers = (Map) params.get("headers");
        String token = (String)headers.get("token");
        return validateToken(token);
    }

    private String validateToken(String token) {
        if (StringUtils.isBlank(token) || "null".equalsIgnoreCase(token)) {
            System.out.println("Token is null");
            return StringUtils.EMPTY_STRING;
        }
        String cypher = secretService.getParameterValue(CYPHER_PARAMETER_NAME);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(cypher.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
            return claims.get("user", String.class);
        } catch (SignatureException | ExpiredJwtException e) {
            System.out.println("Token is not valid");
            return StringUtils.EMPTY_STRING;
        }
    }

    @Override
    public String generateToken(Map<String, Object> params) {
        String cypher = secretService.getParameterValue(CYPHER_PARAMETER_NAME);
        int sessionInMinutes = settingService.getSessionDuration();
        return Jwts.builder()
                .setSubject("loginToken")
                .setClaims(params)
                .setExpiration(new Date(System.currentTimeMillis() + (sessionInMinutes * 60 * 1000)))
                .signWith(
                        SignatureAlgorithm.HS256,
                        cypher.getBytes(StandardCharsets.UTF_8))
                .compact();
    }
}
