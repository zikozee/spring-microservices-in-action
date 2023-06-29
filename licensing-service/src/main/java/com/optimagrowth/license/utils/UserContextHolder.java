package com.optimagrowth.license.utils;

import org.springframework.util.Assert;

/**
 * @author: Ezekiel Eromosei
 * @created: 29 June 2023
 */

public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static UserContext getContext(){
        UserContext context = userContext.get();

        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);

        }
        return userContext.get();
    }

    public static void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }

    public static UserContext createEmptyContext(){
        return new UserContext();
    }
}
