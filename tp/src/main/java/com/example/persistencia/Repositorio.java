package com.example.persistencia;

import java.io.IOException;
import java.util.List;

public interface Repositorio<T> {

    List<T> obtenerTodos() throws IOException;

    void guardarTodos(List<T> elementos) throws IOException;
}
