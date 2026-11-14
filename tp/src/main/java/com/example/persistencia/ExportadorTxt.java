package com.example.persistencia;

import com.example.dominio.Exportable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ExportadorTxt implements Exportador {

    // Escribe una línea por cada elemento exportable
    @Override
    public void exportar(List<? extends Exportable> elementos, String ruta) throws IOException {
        File archivo = new File(ruta);
        if (archivo.getParentFile() != null && !archivo.getParentFile().exists()) {
            archivo.getParentFile().mkdirs();
        }
        List<String> lineas = new ArrayList<>();
        for (Exportable e : elementos) {
            lineas.add(e.generarFilatexto());
        }
        Files.write(archivo.toPath(), lineas);
    }
}
