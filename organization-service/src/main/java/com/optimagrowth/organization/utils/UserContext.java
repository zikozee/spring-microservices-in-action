package com.optimagrowth.organization.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @author: Ezekiel Eromosei
 * @created: 29 June 2023
 */

@Component
//@Getter
//@Setter
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN     = "Authorization";
    public static final String USER_ID        = "tmx-user-id";
    public static final String ORG_ID         = "tmx-org-id";

    private static final ThreadLocal<String> correlationId =
            new ThreadLocal<>();
    private static final ThreadLocal<String> authToken =
            new ThreadLocal<>();
    private static final ThreadLocal<String> userId =
            new ThreadLocal<>();
    private static final ThreadLocal<String> orgId =
            new ThreadLocal<>();

//    public static HttpHeaders getHttpHeaders(){
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set(CORRELATION_ID, getCorrelationId());
//
//        return httpHeaders;
//    }
}
