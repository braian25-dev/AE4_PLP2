package com.example.persistencia;

import com.example.dominio.Exportable;
import java.io.IOException;
import java.util.List;

public interface Exportador {

    void exportar(List<? extends Exportable> elementos, String ruta) throws IOException;
}
