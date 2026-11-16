package com.example.dominio;

public interface EstrategiaDescuento {

    // Devuelve el porcentaje de descuento que corresponde a la factura
    double calcularPorcentaje(Factura factura);
}
