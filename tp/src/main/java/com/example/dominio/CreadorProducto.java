package com.example.dominio;

public class CreadorProducto implements CreadorItem {

    @Override
    public ItemFacturable crearItem(String codigo, String nombre, double precio, String tipo, Proveedor proveedor) {
        return new Producto(codigo, nombre, precio, tipo, proveedor);
    }
}
