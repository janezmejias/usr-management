package com.usermanagement.core.application.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CustomConfiguration {

    /**
     * Creates and returns a primary ObjectMapper bean for the application.
     * This method is responsible for creating a primary ObjectMapper object that is used for converting Java
     * objects to and from JSON format throughout the application.
     *
     * @return A new primary instance of ObjectMapper.
     */
    @Bean
    @Primary
    public ObjectMapper objectMapperPrimary() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    /**
     * Creates and returns a primary ObjectWriter bean for the application.
     * This method is responsible for creating a primary ObjectWriter object that is used for converting Java objects
     * to JSON format. The ObjectWriter is configured using the provided ObjectMapper instance.
     *
     * @param objectMapper An instance of the ObjectMapper class, which is responsible for converting Java objects
     *                     to and from JSON format.
     * @return A new primary instance of ObjectWriter configured with the given ObjectMapper.
     */
    @Bean
    @Primary
    public ObjectWriter objectWriterPrimary(ObjectMapper objectMapper) {
        return objectMapper.writer();
    }

}
