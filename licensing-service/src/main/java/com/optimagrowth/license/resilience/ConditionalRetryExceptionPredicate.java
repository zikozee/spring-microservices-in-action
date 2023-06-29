package com.optimagrowth.license.resilience;

import java.util.function.Predicate;

/**
 * @author: Ezekiel Eromosei
 * @created: 29 June 2023
 */

public class ConditionalRetryExceptionPredicate implements Predicate<CustomRetryException> {

    @Override
    public boolean test(CustomRetryException e) {
        System.out.println("currently in Custom exception predicate");
        return e.getMessage().equalsIgnoreCase("a custom exception thrown");
    }
}
