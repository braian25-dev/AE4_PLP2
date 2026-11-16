package com.example.persistencia;

import com.example.dominio.Empleado;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepository implements Repositorio<Empleado> {

    private final ObjectMapper mapper;
    private final File archivo;

    public EmpleadoRepository() {
        this.mapper = MapeadorJson.getInstancia().getMapper();
        this.archivo = new File("datos/empleados.json");
    }

    @Override
    public void guardarTodos(List<Empleado> empleados) throws IOException {
        if (archivo.getParentFile() != null && !archivo.getParentFile().exists()) {
            archivo.getParentFile().mkdirs();
        }
        mapper.writerFor(new TypeReference<List<Empleado>>() {})
              .withDefaultPrettyPrinter()
              .writeValue(archivo, empleados);
    }

    @Override
    public List<Empleado> obtenerTodos() throws IOException {
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        return mapper.readValue(archivo, new TypeReference<List<Empleado>>() {});
    }
}