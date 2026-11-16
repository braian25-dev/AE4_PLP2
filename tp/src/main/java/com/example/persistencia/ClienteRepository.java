package com.example.persistencia;

import com.example.dominio.Cliente;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository implements Repositorio<Cliente> {

    private final ObjectMapper mapper;
    private final File archivo;

    public ClienteRepository() {
        this.mapper = MapeadorJson.getInstancia().getMapper();
        this.archivo = new File("datos/clientes.json");
    }

    @Override
    public void guardarTodos(List<Cliente> clientes) throws IOException {
        if (archivo.getParentFile() != null && !archivo.getParentFile().exists()) {
            archivo.getParentFile().mkdirs();
        }
        mapper.writerFor(new TypeReference<List<Cliente>>() {})
              .withDefaultPrettyPrinter()
              .writeValue(archivo, clientes);
    }

    @Override
    public List<Cliente> obtenerTodos() throws IOException {
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        return mapper.readValue(archivo, new TypeReference<List<Cliente>>() {});
    }
}