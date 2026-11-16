package com.example.dominio;

import java.util.HashMap;
import java.util.Map;

public class DescuentoPorCategoria implements EstrategiaDescuento {

    private final Map<String, Double> porcentajes = new HashMap<>();

    public DescuentoPorCategoria() {
        porcentajes.put("NUEVO", 0.0);
        porcentajes.put("REGULAR", 5.0);
        porcentajes.put("PREMIUM", 10.0);
    }

    @Override
    public double calcularPorcentaje(Factura factura) {
        return porcentajes.getOrDefault(factura.getCliente().getCategoria(), 0.0);
    }
}
