package com.example.dominio.excepciones;

public class LimiteCreditoExcedidoExcepcion extends Exception {
    public LimiteCreditoExcedidoExcepcion(String mensaje) {
        super(mensaje);
    }
}