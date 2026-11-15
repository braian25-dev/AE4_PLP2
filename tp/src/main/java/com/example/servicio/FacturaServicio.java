package com.example.servicio;

import com.example.dominio.AutorizadorARCA;
import com.example.dominio.Cliente;
import com.example.dominio.ComparadorFacturaPorMonto;
import com.example.dominio.Empleado;
import com.example.dominio.EstadoPago;
import com.example.dominio.Factura;
import com.example.dominio.LineaFactura;
import com.example.dominio.MetodoPago;
import com.example.dominio.excepciones.FacturaSinItemsExcepcion;
import com.example.dominio.excepciones.LimiteCreditoExcedidoExcepcion;
import com.example.dominio.excepciones.PagoExedenteExcepcion;
import com.example.persistencia.Repositorio;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FacturaServicio {

    private final Repositorio<Factura> repositorio;
    private final AutorizadorARCA autorizador = new AutorizadorARCA();
    private List<Factura> facturas = new ArrayList<>();

    public FacturaServicio(Repositorio<Factura> repositorio) {
        this.repositorio = repositorio;
    }

    public void cargar() throws IOException {
        facturas = repositorio.obtenerTodos();
    }

    public void guardar() throws IOException {
        repositorio.guardarTodos(facturas);
    }

    public List<Factura> listar() {
        return facturas;
    }

    // Guarda la factura y la asocia a su cliente
    public void registrar(Factura factura) {
        facturas.add(factura);
        factura.getCliente().agregarFactura(factura);
    }

    public Factura emitir(Cliente cliente, Empleado empleado, List<LineaFactura> lineas)
            throws FacturaSinItemsExcepcion, LimiteCreditoExcedidoExcepcion {
        int proxNro = facturas.size() + 101;
        Factura factura = new Factura(1, proxNro, cliente.getCondicionIVA().getTipoComprobante(), LocalDate.now(), cliente, empleado, MetodoPago.EFECTIVO);
        for (LineaFactura linea : lineas) {
            factura.agregarLinea(linea);
        }
        factura.procesarFactura();
        factura.setEstadoPago(EstadoPago.PENDIENTE);
        autorizador.autorizar(factura);
        registrar(factura);
        return factura;
    }

    public void registrarPago(Factura factura, double monto) throws PagoExedenteExcepcion {
        factura.registrarPago(monto);
    }

    public List<Factura> listarPorNumero() {
        List<Factura> ordenadas = new ArrayList<>(facturas);
        Collections.sort(ordenadas);
        return ordenadas;
    }

    public List<Factura> listarPorMonto() {
        List<Factura> ordenadas = new ArrayList<>(facturas);
        Collections.sort(ordenadas, new ComparadorFacturaPorMonto());
        return ordenadas;
    }

    public void eliminar(Factura factura) {
        facturas.remove(factura);
    }
}
