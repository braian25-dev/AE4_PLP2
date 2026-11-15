package com.example.servicio;

import com.example.dominio.Cliente;
import com.example.dominio.Empleado;
import com.example.dominio.Proveedor;
import com.example.persistencia.Repositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonaServicio {

    private final Repositorio<Cliente> clienteRepo;
    private final Repositorio<Empleado> empleadoRepo;
    private final Repositorio<Proveedor> proveedorRepo;

    private List<Cliente> clientes = new ArrayList<>();
    private List<Empleado> empleados = new ArrayList<>();
    private List<Proveedor> proveedores = new ArrayList<>();

    public PersonaServicio(Repositorio<Cliente> clienteRepo, Repositorio<Empleado> empleadoRepo, Repositorio<Proveedor> proveedorRepo) {
        this.clienteRepo = clienteRepo;
        this.empleadoRepo = empleadoRepo;
        this.proveedorRepo = proveedorRepo;
    }

    public void cargar() throws IOException {
        clientes = clienteRepo.obtenerTodos();
        empleados = empleadoRepo.obtenerTodos();
        proveedores = proveedorRepo.obtenerTodos();
    }

    public void guardar() throws IOException {
        clienteRepo.guardarTodos(clientes);
        empleadoRepo.guardarTodos(empleados);
        proveedorRepo.guardarTodos(proveedores);
    }

    public List<Cliente> listarClientes() {
        return clientes;
    }

    public List<Empleado> listarEmpleados() {
        return empleados;
    }

    public List<Proveedor> listarProveedores() {
        return proveedores;
    }

    public void registrarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    // El empleado también se agrega a su departamento
    public void registrarEmpleado(Empleado empleado) {
        empleados.add(empleado);
        empleado.getDepartamento().agregarEmpleado(empleado);
    }

    public void registrarProveedor(Proveedor proveedor) {
        proveedores.add(proveedor);
    }

    public void eliminarCliente(Cliente cliente) {
        clientes.remove(cliente);
    }

    public void eliminarEmpleado(Empleado empleado) {
        empleados.remove(empleado);
    }

    public void eliminarProveedor(Proveedor proveedor) {
        proveedores.remove(proveedor);
    }
}
