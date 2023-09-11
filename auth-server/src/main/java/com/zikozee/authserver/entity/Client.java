package com.zikozee.authserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

@Entity
@Table(name = "clients")
@Getter @Setter
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
                .build();
    }
}
