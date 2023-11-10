package com.optimagrowth.license.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${jwks.uri}")
    private String jwksUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(resourceCustomizer ->
                resourceCustomizer
                        .jwt(jwtCustomizer ->
                                        jwtCustomizer.jwkSetUri(jwksUri)
                                                .jwtAuthenticationConverter(new CustomJwtAuthenticationTokenConverter())
                        )
        );

        http.authorizeHttpRequests(c -> c.anyRequest().authenticated());
        return http.build();
    }
}
