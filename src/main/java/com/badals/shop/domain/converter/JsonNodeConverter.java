//package com.badals.shop.domain.converter;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import javax.persistence.*;
//
//@Converter
//public class JsonNodeConverter implements AttributeConverter<JsonNode, String>{
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public String convertToDatabaseColumn(JsonNode jsonNode) {
//        try {
//            return objectMapper.writeValueAsString(jsonNode);
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting JsonNode to String", e);
//        }
//    }
//
//    @Override
//    public JsonNode convertToEntityAttribute(String jsonString) {
//        try {
//            return objectMapper.readTree(jsonString);
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting String to JsonNode", e);
//        }
//    }
//
//}
