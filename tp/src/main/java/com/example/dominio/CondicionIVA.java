package com.example.dominio;


public enum CondicionIVA {
    RESPONSABLE_INSCRIPTO(TipoComprobante.FACTURA_A),
    MONOTRIBUTO(TipoComprobante.FACTURA_C),
    CONSUMIDOR_FINAL(TipoComprobante.FACTURA_C),
    EXENTO(TipoComprobante.FACTURA_C);

    private final TipoComprobante tipoComprobante;

    CondicionIVA(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }
}

/*
 * Se toma como ejemplo la forma en la que trabaja ARCA, en Argentina.

 * Basicamente, esta clase modela las situaciones fiscales ante ARCA en Argentina.
 * Permite validar el tipo de factura a emitir entre emisor y cliente.
 */