package com.example.dominio;

import java.time.LocalDate;

public class AutorizadorARCA {

    // Simula la autorización de ARCA: genera un CAE aleatorio con vencimiento a 10 días
    public void autorizar(Factura factura) {
        String cae = String.valueOf((long) (Math.random() * 100000000000000L));
        factura.autorizarARCA(cae, LocalDate.now().plusDays(10), "https://www.arca.gob.ar/fe/qr/?p=eyJ2ZXJzaW9uIjoxfQ==");
    }
}
