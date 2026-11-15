package com.example.servicio;

import com.example.dominio.Cliente;
import com.example.dominio.CondicionIVA;
import com.example.dominio.Departamento;
import com.example.dominio.Empleado;
import com.example.dominio.EstadoPago;
import com.example.dominio.Factura;
import com.example.dominio.LineaFactura;
import com.example.dominio.MetodoPago;
import com.example.dominio.Producto;
import com.example.dominio.Proveedor;
import com.example.dominio.Servicio;
import com.example.dominio.TipoComprobante;
import java.io.IOException;
import java.time.LocalDate;

public class SistemaServicio {

    private final CatalogoServicio catalogo;
    private final PersonaServicio personas;
    private final DepartamentoServicio departamentos;
    private final FacturaServicio facturas;

    public SistemaServicio(CatalogoServicio catalogo, PersonaServicio personas, DepartamentoServicio departamentos, FacturaServicio facturas) {
        this.catalogo = catalogo;
        this.personas = personas;
        this.departamentos = departamentos;
        this.facturas = facturas;
    }

    // Devuelve false si no había datos guardados
    public boolean cargarDatos() throws IOException {
        personas.cargar();
        catalogo.cargar();
        facturas.cargar();
        boolean sinDatos = personas.listarClientes().isEmpty() && personas.listarEmpleados().isEmpty()
                && catalogo.listar().isEmpty() && facturas.listar().isEmpty();
        return !sinDatos;
    }

    public void guardarDatos() throws IOException {
        personas.guardar();
        catalogo.guardar();
        facturas.guardar();
    }

    public void cargarDatosDePrueba() {
        Proveedor prov1 = new Proveedor("Tech Supply S.A.", "30-11223344-5", "Av. Industrial 500", "011-44332211", CondicionIVA.RESPONSABLE_INSCRIPTO);
        personas.registrarProveedor(prov1);

        Producto prod1 = new Producto("P001", "Mouse Inalámbrico", 15000.0, "Hardware", prov1);
        Servicio serv1 = new Servicio("S001", "Instalación de Software", 8000.0, "Soporte Técnico", prov1);
        catalogo.agregar(prod1);
        catalogo.agregar(serv1);

        Cliente cli1 = new Cliente("Maria Gomez", "20-87654321-8", "Av. Siempreviva 742", "011-55554444", CondicionIVA.CONSUMIDOR_FINAL, 50000.0, "REGULAR");
        personas.registrarCliente(cli1);

        Departamento depSistemas = new Departamento("Sistemas y Soporte", 500000.0);
        departamentos.agregar(depSistemas);

        Empleado emp1 = new Empleado("Juan Perez", "20-12345678-9", "Calle Falsa 123", "011-11223344", CondicionIVA.RESPONSABLE_INSCRIPTO, 350000.0, "Técnico", LocalDate.of(2023, 3, 15), depSistemas);
        personas.registrarEmpleado(emp1);
        depSistemas.setResponsable(emp1);

        Factura fac1 = new Factura(1, 29, TipoComprobante.FACTURA_C, LocalDate.now(), cli1, emp1, MetodoPago.TRANSFERENCIA);
        fac1.agregarLinea(new LineaFactura(prod1, 2, 0.0));
        fac1.agregarLinea(new LineaFactura(serv1, 1, 1000.0));
        fac1.setEstadoPago(EstadoPago.CANCELADO);
        fac1.autorizarARCA("74453966403949", LocalDate.now().plusDays(10), "https://www.arca.gob.ar/fe/qr/?p=eyJ2ZXJzaW9uIjoxfQ==");
        facturas.registrar(fac1);
    }
}
