package com.example.dominio;

import java.util.EnumMap;
import java.util.Map;

public class DescuentoPorFormaPago implements EstrategiaDescuento {

    private final Map<MetodoPago, Double> porcentajes = new EnumMap<>(MetodoPago.class);

    public DescuentoPorFormaPago() {
        porcentajes.put(MetodoPago.EFECTIVO, 10.0);
        porcentajes.put(MetodoPago.TRANSFERENCIA, 5.0);
        porcentajes.put(MetodoPago.TARJETA_DEBITO, 3.0);
        porcentajes.put(MetodoPago.TARJETA_CREDITO, 0.0);
    }

    @Override
    public double calcularPorcentaje(Factura factura) {
        return porcentajes.getOrDefault(factura.getMetodoPago(), 0.0);
    }
}
