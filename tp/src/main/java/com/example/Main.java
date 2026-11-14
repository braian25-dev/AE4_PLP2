package com.example;

import com.example.consola.MenuConsola;
import com.example.persistencia.CatalogoRepository;
import com.example.persistencia.ClienteRepository;
import com.example.persistencia.EmpleadoRepository;
import com.example.persistencia.FacturaRepository;
import com.example.persistencia.ProveedorRepository;

public class Main {
    public static void main(String[] args) {
        MenuConsola menu = new MenuConsola(new ClienteRepository(), new EmpleadoRepository(), new ProveedorRepository(),
                                           new CatalogoRepository(), new FacturaRepository());
        menu.iniciar();
    }
} 