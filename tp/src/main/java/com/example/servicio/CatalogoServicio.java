package com.example.servicio;

import com.example.dominio.ItemFacturable;
import com.example.persistencia.Repositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogoServicio {

    private final Repositorio<ItemFacturable> repositorio;
    private List<ItemFacturable> catalogo = new ArrayList<>();

    public CatalogoServicio(Repositorio<ItemFacturable> repositorio) {
        this.repositorio = repositorio;
    }

    public void cargar() throws IOException {
        catalogo = repositorio.obtenerTodos();
    }

    public void guardar() throws IOException {
        repositorio.guardarTodos(catalogo);
    }

    public List<ItemFacturable> listar() {
        return catalogo;
    }

    public void agregar(ItemFacturable item) {
        catalogo.add(item);
    }

    public void eliminar(ItemFacturable item) {
        catalogo.remove(item);
    }
}
