package com.edu.empresa.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonManagerCDT {
    
    public ObjectMapper mapper;

    public JsonManagerCDT() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public <T> void guardarLista(String ruta, List<T> lista) {
        try {
            mapper.writeValue(new File(ruta), lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> leerLista(String ruta, Class<T> clazz) {
        try {
            File file = new File(ruta);
            if (file.exists()) {
                return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}