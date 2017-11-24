package com.guqiankun.ssmtemplate.infrastructure.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.datatype.joda.JodaMapper;

public final class JsonConvertor {

    private static final Logger logger = LoggerFactory.getLogger(JsonConvertor.class);

    private static final JodaMapper mapper = new JodaMapper();

    static {
        mapper.setWriteDatesAsTimestamps(false);
    }

    public static String serialize(Object obj) {
        if (obj == null) {
            return "null";
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("", e);
            return "exception";
        }
    }

    public static <T> T deserialize(String json, Class<T> classType) {
        try {
            return mapper.readValue(json, classType);
        } catch (JsonParseException e) {
            logger.error("", e);
            return null;
        } catch (JsonMappingException e) {
            logger.error("", e);
            return null;
        } catch (IOException e) {
            logger.error("", e);
            return null;
        }
    }

}
