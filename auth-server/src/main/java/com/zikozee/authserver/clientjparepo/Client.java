package com.zikozee.authserver.clientjparepo;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class Client {
    //@Id
    private String id;
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    private String clientName;
    //@Column(length = 1000)
    private String clientAuthenticationMethods;
    //@Column(length = 1000)
    private String authorizationGrantTypes;
    //@Column(length = 1000)
    private String redirectUris;
    //@Column(length = 1000)
    private String scopes;
    //@Column(length = 2000)
    private String clientSettings;
    //@Column(length = 2000)
    private String tokenSettings;
}
