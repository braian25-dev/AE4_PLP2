package com.example.persistencia;

import com.example.dominio.Factura;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacturaRepository implements Repositorio<Factura> {

    private final ObjectMapper mapper;
    private final File archivo;

    public FacturaRepository() {
        this.mapper = MapeadorJson.getInstancia().getMapper();
        this.archivo = new File("datos/facturas.json");
    }

    @Override
    public void guardarTodos(List<Factura> facturas) throws IOException {
        if (archivo.getParentFile() != null && !archivo.getParentFile().exists()) {
            archivo.getParentFile().mkdirs();
        }
        mapper.writerFor(new TypeReference<List<Factura>>() {})
              .withDefaultPrettyPrinter()
              .writeValue(archivo, facturas);
    }

    @Override
    public List<Factura> obtenerTodos() throws IOException {
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        return mapper.readValue(archivo, new TypeReference<List<Factura>>() {});
    }
}