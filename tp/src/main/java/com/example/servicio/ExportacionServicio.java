package com.example.servicio;

import com.example.dominio.Exportable;
import com.example.persistencia.Exportador;
import java.io.IOException;
import java.util.List;

public class ExportacionServicio {

    private final Exportador exportador;

    public ExportacionServicio(Exportador exportador) {
        this.exportador = exportador;
    }

    // Sirve para cualquier lista de objetos Exportable (facturas, clientes, catálogo)
    public void exportar(List<? extends Exportable> elementos, String ruta) throws IOException {
        exportador.exportar(elementos, ruta);
    }
}
