package com.zikozee.authserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;

@Entity
@Table(name = "clients")
@Getter @Setter
@ToString
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String clientId;
    private String secret;
    private String scope;
    private String authMethod;
    private String grantType;
    private String redirectUri;
    private String tokenFormat;

    public static Client from(RegisteredClient registeredClient){
        Client client = new Client();

        client.setClientId(registeredClient.getClientId());
        client.setSecret(registeredClient.getClientSecret());

        client.setRedirectUri( // NOT CLEAN CODE
                registeredClient.getRedirectUris().stream().findAny().get()
        ); // use a list here, to save all, maintain a 1 - * client to redirect Uris
        client.setScope(
                registeredClient.getScopes().stream().findAny().get()
        ); // same as above
        client.setAuthMethod(
                registeredClient.getClientAuthenticationMethods().stream().findAny().get().getValue()
        ); // same as above
        client.setGrantType(
                registeredClient.getAuthorizationGrantTypes().stream().findAny().get().getValue()
        );// same as above
        client.setTokenFormat(registeredClient.getTokenSettings().getAccessTokenFormat().getValue());

        return client;
    }

    public static RegisteredClient from(Client client){
        return RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(client.getClientId())
                .clientSecret(client.getSecret())
                .scope(client.getScope())
                .redirectUri(client.redirectUri)
                .clientAuthenticationMethod(new ClientAuthenticationMethod(client.getAuthMethod()))
                .authorizationGrantType(new AuthorizationGrantType(client.getGrantType()))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(new OAuth2TokenFormat(client.getTokenFormat()))
                        .accessTokenTimeToLive(Duration.ofHours(24)).build())
                .build();
    }
}
