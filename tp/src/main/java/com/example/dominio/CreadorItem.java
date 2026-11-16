package com.example.dominio;

public interface CreadorItem {

    // Método fábrica: cada creador decide qué tipo de ítem instancia
    ItemFacturable crearItem(String codigo, String nombre, double precio, String tipo, Proveedor proveedor);
}
