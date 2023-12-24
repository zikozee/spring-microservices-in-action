package com.optimagrowth.organization.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

// in case we need to added more details to the JwtToken
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {

    private String blabla;

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
        blabla="blablabla";
    }
}
