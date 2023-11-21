package com.optimagrowth.organization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${introspection.uri}")
    private String introspectionUri;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(resourceCustomizer ->
                resourceCustomizer.opaqueToken(opaqueCustomizer ->
                        opaqueCustomizer.introspectionUri(introspectionUri)
                                .introspectionClientCredentials(clientId, clientSecret) //inject from vault
                )
        );

        http.authorizeHttpRequests(c -> c.anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
