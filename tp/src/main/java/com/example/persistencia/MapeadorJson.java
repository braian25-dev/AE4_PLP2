package com.example.persistencia;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MapeadorJson {

    private static MapeadorJson instancia;
    private final ObjectMapper mapper;

    private MapeadorJson() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static MapeadorJson getInstancia() {
        if (instancia == null) {
            instancia = new MapeadorJson();
        }
        return instancia;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
