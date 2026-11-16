package com.example.dominio;

public class CreadorServicio implements CreadorItem {

    @Override
    public ItemFacturable crearItem(String codigo, String nombre, double precio, String tipo, Proveedor proveedor) {
        return new Servicio(codigo, nombre, precio, tipo, proveedor);
    }
}
