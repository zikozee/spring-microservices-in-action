package com.optimagrowth.license.resilience;

/**
 * @author: Ezekiel Eromosei
 * @created: 29 June 2023
 */

public class CustomRetryException extends RuntimeException{

    public CustomRetryException(String message) {
        super(message);
    }
}
