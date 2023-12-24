package com.optimagrowth.license.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 20 Dec, 2023
 */

@Slf4j
public final class JsonUtil {

    public static <T> String toJson(T data, boolean prettify){
        ObjectMapper objectMapper =new ObjectMapper();

        try {
            if(prettify){
                return objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(data);
            }
            return  objectMapper.writeValueAsString(data);
        }catch (JsonProcessingException e) {
            log.error("error converting", e);
            return  null;
        }
    }

    public static <T> T fromJSON(String json, Class<T> type) {
        try {
            ObjectMapper objectMapper =new ObjectMapper();

            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
