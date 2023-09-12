package com.optimagrowth.organization.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.stream.Collectors;

//public class CustomJwtAuthenticationTokenConverter implements Converter<Jwt, JwtAuthenticationToken>{
//
//    @Override
//    public JwtAuthenticationToken convert(Jwt source) {
//        List<String> authorities = (List<String>)source.getClaims().get("authorities");
//        JwtAuthenticationToken authenticationToken =
//                new JwtAuthenticationToken(source, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        return authenticationToken;
//    }
//}


public class CustomJwtAuthenticationTokenConverter implements Converter<Jwt, CustomJwtAuthenticationToken>{

    @Override
    public CustomJwtAuthenticationToken convert(Jwt source) {
        List<String> authorities = (List<String>)source.getClaims().get("authorities");
        CustomJwtAuthenticationToken authenticationToken =
                new CustomJwtAuthenticationToken(source, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        return authenticationToken;
    }
}