/**
 * 
 */
package com.github.jerryxia.devhelper.support.json;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author guqk
 *
 */
public class JacksonProvider implements JsonComponentProvider {
    private static Logger log = LoggerFactory.getLogger(JacksonProvider.class);

    private ObjectMapper mapper;

    public JacksonProvider(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    
    @Override
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            log.warn("write to json string error:" + object, e);
            return null;
        }
    }

    @Override
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.length() == 0) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

}
