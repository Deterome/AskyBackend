package org.senla_project.application.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonParser {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T parseJsonToObject(String json, Class<T> objectClass) {
        try {
            return objectMapper.readValue(json, objectClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String parseObjectToJson(Object dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
