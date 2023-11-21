package com.optimagrowth.license.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 21 Nov, 2023
 */

@Component
public class SecurityContextUtil {

    public String jwtToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken oauthToken = (JwtAuthenticationToken) authentication;
        return oauthToken.getToken().getTokenValue();
    }

    public HttpHeaders jwtHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken());
        return headers;
    }
}
