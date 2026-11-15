package com.example;

import com.example.persistencia.CatalogoRepository;
import com.example.persistencia.ClienteRepository;
import com.example.persistencia.EmpleadoRepository;
import com.example.persistencia.ExportadorTxt;
import com.example.persistencia.FacturaRepository;
import com.example.persistencia.ProveedorRepository;
import com.example.presentacion.MenuConsola;
import com.example.servicio.CatalogoServicio;
import com.example.servicio.DepartamentoServicio;
import com.example.servicio.ExportacionServicio;
import com.example.servicio.FacturaServicio;
import com.example.servicio.PersonaServicio;
import com.example.servicio.SistemaServicio;

public class Aplicacion {

    // Arma los servicios con sus repositorios y arranca el menú
    public void iniciar() {
        CatalogoServicio catalogo = new CatalogoServicio(new CatalogoRepository());
        PersonaServicio personas = new PersonaServicio(new ClienteRepository(), new EmpleadoRepository(), new ProveedorRepository());
        DepartamentoServicio departamentos = new DepartamentoServicio();
        FacturaServicio facturas = new FacturaServicio(new FacturaRepository());
        ExportacionServicio exportacion = new ExportacionServicio(new ExportadorTxt());
        SistemaServicio sistema = new SistemaServicio(catalogo, personas, departamentos, facturas);

        MenuConsola menu = new MenuConsola(sistema, catalogo, personas, departamentos, facturas, exportacion);
        menu.iniciar();
    }
}
