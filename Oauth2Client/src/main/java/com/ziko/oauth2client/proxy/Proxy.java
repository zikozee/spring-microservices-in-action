package com.ziko.oauth2client.proxy;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 24 Nov, 2023
 */

@Component
@RequiredArgsConstructor
public class Proxy {

    // note if the token is still valid, the OAuth2AuthorizedClientManager will not call authorization Server rather it caches
    private final OAuth2AuthorizedClientManager manager;


    public String getTokenFromAuthorizationServer(){
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("oauthClient")
                .principal("client")
                .build();
        OAuth2AuthorizedClient authorize = manager.authorize(request); // request to the authorization server

        return authorize.getAccessToken().getTokenValue(); // to the the Authorization Header of the request you wanna make "Bearer ..."
    }
}
