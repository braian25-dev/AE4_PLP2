package com.example.excepciones;

public class LimiteCreditoExcedidoExcepcion extends Exception {
    public LimiteCreditoExcedidoExcepcion(String mensaje) {
        super(mensaje);
    }
}