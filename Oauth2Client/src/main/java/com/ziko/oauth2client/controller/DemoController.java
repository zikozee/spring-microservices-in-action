package com.ziko.oauth2client.controller;

import com.ziko.oauth2client.proxy.Proxy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 24 Nov, 2023
 */

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final Proxy proxy;


    @GetMapping(path = "token")
    public String token() {
        return proxy.getTokenFromAuthorizationServer();
    }

}


