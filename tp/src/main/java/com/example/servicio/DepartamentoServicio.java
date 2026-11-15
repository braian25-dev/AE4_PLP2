package com.example.servicio;

import com.example.dominio.Departamento;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoServicio {

    private final List<Departamento> departamentos = new ArrayList<>();

    public List<Departamento> listar() {
        return departamentos;
    }

    public void agregar(Departamento departamento) {
        departamentos.add(departamento);
    }

    public void eliminar(Departamento departamento) {
        departamentos.remove(departamento);
    }
}
