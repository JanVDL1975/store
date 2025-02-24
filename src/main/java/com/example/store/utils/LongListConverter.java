package com.example.store.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/*@Converter(autoApply = true)
public class LongListConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]"; // Return an empty JSON array for empty lists
        }
        try {
            // Convert List<Long> to JSON string using ObjectMapper
            return new ObjectMapper().writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]"; // In case of error, return an empty array
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return Collections.emptyList(); // Handle empty or null values
        }
        try {
            // Convert the JSON string back to a List<Long>
            return new ObjectMapper().readValue(dbData, new TypeReference<List<Long>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}*/






