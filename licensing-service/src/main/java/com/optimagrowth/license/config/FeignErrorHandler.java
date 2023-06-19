package com.optimagrowth.license.config;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * @author: Ezekiel Eromosei
 * @created: 19 June 2023
 */


public class FeignErrorHandler implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return null;
    }
}
