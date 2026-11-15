package com.example.presentacion;

import com.example.dominio.*;
import com.example.dominio.excepciones.*;

import com.example.servicio.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuConsola {

    private final Scanner scanner = new Scanner(System.in);
    // Servicios que usa el menú
    private final SistemaServicio sistema;
    private final CatalogoServicio catalogoServicio;
    private final PersonaServicio personaServicio;
    private final DepartamentoServicio departamentoServicio;
    private final FacturaServicio facturaServicio;
    private final ExportacionServicio exportacionServicio;

    public MenuConsola(SistemaServicio sistema, CatalogoServicio catalogoServicio, PersonaServicio personaServicio,
                       DepartamentoServicio departamentoServicio, FacturaServicio facturaServicio, ExportacionServicio exportacionServicio) {
        this.sistema = sistema;
        this.catalogoServicio = catalogoServicio;
        this.personaServicio = personaServicio;
        this.departamentoServicio = departamentoServicio;
        this.facturaServicio = facturaServicio;
        this.exportacionServicio = exportacionServicio;
    }

    public void iniciar() {
        // Carga de datos guardados desde JSON
        try {
            if (sistema.cargarDatos()) {
                System.out.println("Datos cargados correctamente desde la carpeta datos/");
            } else {
                // Si el sistema no tiene datos guardados, inicializa datos de prueba
                System.out.println("No se encontraron archivos previos. Cargando datos por defecto...");
                sistema.cargarDatosDePrueba();
                guardarDatos();
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los datos guardados: " + e.getMessage());
            sistema.cargarDatosDePrueba();
        }

        int opcion = -1;
        do {
            System.out.println("\n==================================================");
            System.out.println("           SISTEMA DE GESTIÓN COMERCIAL           ");
            System.out.println("==================================================");
            System.out.println("1. Gestión de Catálogo (Productos / Servicios)");
            System.out.println("2. Gestión de Personas (Clientes / Empleados / Proveedores)");
            System.out.println("3. Gestión Organizativa (Departamentos)");
            System.out.println("4. Facturación y Ventas (Emisión ARCA / Consultas)");
            System.out.println("0. Salir del Sistema");
            System.out.println("==================================================");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> menuCatalogo();
                    case 2 -> menuPersonas();
                    case 3 -> menuDepartamentos();
                    case 4 -> menuFacturacion();
                    case 0 -> {
                        guardarDatos();
                        System.out.println("Guardando datos y saliendo del sistema...");
                    }
                    default -> System.out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            }
        } while (opcion != 0);
    }

    private void guardarDatos() {
        try {
            sistema.guardarDatos();
            System.out.println("Todos los datos fueron actualizados en la carpeta datos/");
        } catch (IOException e) {
            System.out.println("Error al guardar en archivos JSON: " + e.getMessage());
        }
    }

    // =========================================================================
    // SUBMENÚ: CATÁLOGO COMERCIAL
    // =========================================================================
    private void menuCatalogo() {
        System.out.println("\n--- SUBMENÚ: CATÁLOGO COMERCIAL ---");
        System.out.println("1. Listar Productos y Servicios");
        System.out.println("2. Cargar Nuevo Producto");
        System.out.println("3. Cargar Nuevo Servicio");
        System.out.println("4. Eliminar Ítem del Catálogo");
        System.out.println("5. Exportar Catálogo a .txt");
        System.out.println("0. Volver al menú principal");
        System.out.print("Opción: ");
        try {
            int op = Integer.parseInt(scanner.nextLine());
            switch (op) {
                case 1 -> listarCatalogo();
                case 2 -> cargarProducto();
                case 3 -> cargarServicio();
                case 4 -> eliminarItemCatalogo();
                case 5 -> exportarTxt(catalogoServicio.listar(), "exportaciones/catalogo.txt");
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en ingreso de datos.");
        }
    }

    private void listarCatalogo() {
        List<ItemFacturable> catalogo = catalogoServicio.listar();
        System.out.println("\n=== CATÁLOGO DE ÍTEMS ===");
        if (catalogo.isEmpty()) {
            System.out.println("No hay ítems registrados.");
            return;
        }
        for (ItemFacturable item : catalogo) {
            System.out.println("[" + item.getCodigo() + "] " + item.getNombre() + " - $" + item.getPrecio() + " (" + item.getDescripcion() + ")");
        }
    }

    private void cargarProducto() {
        List<Proveedor> proveedores = personaServicio.listarProveedores();
        if (proveedores.isEmpty()) {
            System.out.println(" Error: Primero debe haber al menos un Proveedor registrado.");
            return;
        }
        System.out.println("\n-- Carga de Nuevo Producto --");
        System.out.print("Código (ej. P002): ");
        String cod = scanner.nextLine();
        System.out.print("Nombre: ");
        String nom = scanner.nextLine();
        System.out.print("Precio: ");
        double prec = Double.parseDouble(scanner.nextLine());
            if (prec <= 0) {
            System.out.println("El precio debe ser mayor a cero. Carga cancelada.");
            return;
        }
        System.out.print("Categoría/Tipo (Hardware/Sop. Técnico): ");
        String tipo = scanner.nextLine();

        // nueva modificación ------------------------------------------------------------------------------------------------------------------------------------------------------
        System.out.println("\n-- Seleccione el Proveedor --");
        for (int i = 0; i < proveedores.size(); i++) {
            System.out.println((i + 1) + ". " + proveedores.get(i).getRazonSocial());
        }
        System.out.print("Opción: ");
        int idxProv = Integer.parseInt(scanner.nextLine()) - 1;
        if (idxProv < 0 || idxProv >= proveedores.size()) {
            System.out.println("Proveedor no válido. Carga cancelada.");
            return;
        }
        Proveedor prov = proveedores.get(idxProv);
        Producto p = new Producto(cod, nom, prec, tipo, prov);
        catalogoServicio.agregar(p);
        System.out.println("¡Producto guardado exitosamente!");
    }

    private void cargarServicio() {
        List<Proveedor> proveedores = personaServicio.listarProveedores();
        if (proveedores.isEmpty()) {
                System.out.println(" Error: Primero debe haber al menos un Proveedor registrado.");
                return;
            }
        System.out.println("\n-- Carga de Nuevo Servicio --");
        System.out.print("Código (ej. S002): ");
        String cod = scanner.nextLine();
        System.out.print("Nombre: ");
        String nom = scanner.nextLine();
        System.out.print("Precio: ");
        double prec = Double.parseDouble(scanner.nextLine());
            if (prec <= 0) {
                System.out.println("El precio debe ser mayor a cero. Carga cancelada.");
                return;
            }
        System.out.print("Especialidad/Tipo: ");
        String tipo = scanner.nextLine();

        // nueva modificación ------------------------------------------------------------------------------------------------------------------------------------------------------
        System.out.println("\n-- Seleccione el Proveedor --");
        for (int i = 0; i < proveedores.size(); i++) {
            System.out.println((i + 1) + ". " + proveedores.get(i).getRazonSocial());
        }
        System.out.print("Opción: ");
        int idxProv = Integer.parseInt(scanner.nextLine()) - 1;
        if (idxProv < 0 || idxProv >= proveedores.size()) {
            System.out.println("Proveedor no válido. Carga cancelada.");
            return;
        }
        Proveedor prov = proveedores.get(idxProv);
        Servicio s = new Servicio(cod, nom, prec, tipo, prov);
        catalogoServicio.agregar(s);
        System.out.println("¡Servicio guardado exitosamente!");
    }

    // =========================================================================
    // SUBMENÚ: GESTIÓN DE PERSONAS
    // =========================================================================
    private void menuPersonas() {
        System.out.println("\n--- SUBMENÚ: GESTIÓN DE PERSONAS ---");
        System.out.println("1. Listar Clientes");
        System.out.println("2. Listar Empleados");
        System.out.println("3. Listar Proveedores");
        System.out.println("4. Cargar Cliente");
        System.out.println("5. Cargar Empleado");
        System.out.println("6. Cargar Proveedor");
        System.out.println("7. Eliminar Cliente"); 
        System.out.println("8. Eliminar Empleado");
        System.out.println("9. Eliminar Proveedor");
        System.out.println("10. Ver Ficha de Cliente");
        System.out.println("11. Exportar Clientes a .txt");
        System.out.println("0. Volver al menú principal");
        System.out.print("Opción: ");
        try {
            int op = Integer.parseInt(scanner.nextLine());
            switch (op) {
                case 1 -> listarClientes();
                case 2 -> listarEmpleados();
                case 3 -> listarProveedores();
                case 4 -> cargarCliente();
                case 5 -> cargarEmpleado();
                case 6 -> cargarProveedor();
                case 7 -> eliminarCliente(); 
                case 8 -> eliminarEmpleado();
                case 9 -> eliminarProveedor();
                case 10 -> verFichaCliente();
                case 11 -> exportarTxt(personaServicio.listarClientes(), "exportaciones/clientes.txt");
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en ingreso de datos.");
        }
    }

    private void listarClientes() {
        List<Cliente> clientes = personaServicio.listarClientes();
        System.out.println("\n=== CLIENTES REGISTRADOS ===");
        for (Cliente c : clientes) {
            System.out.println("- " + c.getNombre() + " | DNI/CUIT: " + c.getDni() + " | IVA: " + c.getCondicionIVA());
        }
    }

    private void verFichaCliente() {
        List<Cliente> clientes = personaServicio.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println((i + 1) + ". " + clientes.get(i).getNombre());
        }
        System.out.print("Ingrese el número del cliente: ");
        int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx >= 0 && idx < clientes.size()) {
            Imprimible ficha = clientes.get(idx);
            ficha.ImprimibleTicket();
        } else {
            System.out.println("Cliente no válido.");
        }
    }

    private void listarEmpleados() {
        List<Empleado> empleados = personaServicio.listarEmpleados();
        System.out.println("\n=== EMPLEADOS REGISTRADOS ===");
        for (Empleado e : empleados) {
            System.out.println("- " + e.getNombre() + " | Cargo: " + e.getPuesto() + " | Depto: " + (e.getDepartamento() != null ? e.getDepartamento().getNombre() : "Sin Asignar"));
        }
    }

    private void listarProveedores() {
        List<Proveedor> proveedores = personaServicio.listarProveedores();
        System.out.println("\n=== PROVEEDORES REGISTRADOS ===");
        for (Proveedor p : proveedores) {
            System.out.println("- " + p.getRazonSocial() + " | CUIT: " + p.getCuit());
        }
    }

    private void cargarCliente() {
        System.out.println("\n-- Carga de Nuevo Cliente --");
        System.out.print("Nombre completo: ");
        String nom = scanner.nextLine();
        System.out.print("DNI o CUIT: ");
        String dni = scanner.nextLine();
        System.out.print("Domicilio: ");
        String dom = scanner.nextLine();
        System.out.print("Teléfono: ");
        String tel = scanner.nextLine();
        CondicionIVA iva = seleccionarCondicionIVA();
        System.out.print("Límite de Crédito: ");
        double lim = Double.parseDouble(scanner.nextLine());

        Cliente c = new Cliente(nom, dni, dom, tel, iva, lim, "NUEVO");
        personaServicio.registrarCliente(c);
        System.out.println(" ¡Cliente registrado exitosamente!");
    }

    private void cargarEmpleado() {
        List<Departamento> departamentos = departamentoServicio.listar();
        if (departamentos.isEmpty()) {
            System.out.println(" Error: Cree primero un departamento para asignar al empleado.");
            return;
        }
        System.out.println("\n-- Carga de Nuevo Empleado --");
        System.out.print("Nombre completo: ");
        String nom = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Domicilio: ");
        String dom = scanner.nextLine();
        System.out.print("Teléfono: ");
        String tel = scanner.nextLine();
        CondicionIVA iva = seleccionarCondicionIVA();
        System.out.print("Salario: ");
        double sal = Double.parseDouble(scanner.nextLine());
        if (sal <= 0) {
            System.out.println("El salario debe ser un monto mayor a cero. Carga cancelada.");
            return;
        }

        System.out.print("Puesto: ");
        String puesto = scanner.nextLine();

        // nueva modificación ------------------------------------------------------------------------------------------------------------------------------------------------------
        System.out.println("\n-- Seleccione el Departamento --");
        for (int i = 0; i < departamentos.size(); i++) {
            System.out.println((i + 1) + ". " + departamentos.get(i).getNombre());
        }
        System.out.print("Opción: ");
        int idxDep = Integer.parseInt(scanner.nextLine()) - 1;
        if (idxDep < 0 || idxDep >= departamentos.size()) {
            System.out.println("Departamento no válido. Carga cancelada.");
            return;
        }
        Departamento dep = departamentos.get(idxDep);
        Empleado e = new Empleado(nom, dni, dom, tel, iva, sal, puesto, LocalDate.now(), dep);
        personaServicio.registrarEmpleado(e);
        System.out.println("¡Empleado registrado exitosamente!");
    }

    private void cargarProveedor() {
        System.out.println("\n-- Carga de Nuevo Proveedor --");
        System.out.print("Razón Social: ");
        String rsoc = scanner.nextLine();
        System.out.print("CUIT: ");
        String cuit = scanner.nextLine();
        System.out.print("Domicilio: ");
        String dom = scanner.nextLine();
        System.out.print("Teléfono: ");
        String tel = scanner.nextLine();
        CondicionIVA iva = seleccionarCondicionIVA();

        Proveedor p = new Proveedor(rsoc, cuit, dom, tel, iva);
        personaServicio.registrarProveedor(p);
        System.out.println("¡Proveedor registrado exitosamente!");
    }

    // =========================================================================
    // SUBMENÚ: DEPARTAMENTOS
    // =========================================================================
    private void menuDepartamentos() {
        System.out.println("\n--- SUBMENÚ: DEPARTAMENTOS ---");
        System.out.println("1. Listar Departamentos");
        System.out.println("2. Crear Departamento");
        System.out.println("3. Eliminar Departamento");
        System.out.println("0. Volver al menú principal");
        System.out.print("Opción: ");
        try {
            int op = Integer.parseInt(scanner.nextLine());
            switch (op) {
                case 1 -> {
                    System.out.println("\n=== DEPARTAMENTOS DE LA EMPRESA ===");
                    for (Departamento d : departamentoServicio.listar()) {
                        System.out.println("Área: " + d.getNombre() + " | Presupuesto: $" + d.getPresupuesto() + " | Miembros: " + d.getEmpleados().size());
                    }
                }
                case 2 -> {
                    System.out.println("\n-- Crear Departamento --");
                    System.out.print("Nombre del Área: ");
                    String nom = scanner.nextLine();
                    System.out.print("Presupuesto: ");
                    double pres = Double.parseDouble(scanner.nextLine());
                    if (pres <= 0) {
                        System.out.println("El presupuesto debe ser un monto mayor a cero.");
                        break;
                    }
                    departamentoServicio.agregar(new Departamento(nom, pres));
                    System.out.println("¡Departamento creado!");
                }
                    default -> System.out.println("Opción inválida.");

                case 3 ->{ eliminarDepartamento();
                }

                case 0 -> System.out.println("Volviendo al menú principal...");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en ingreso de datos.");
        }
    }

    // =========================================================================
    // SUBMENÚ: FACTURACIÓN Y VENTAS
    // =========================================================================
    private void menuFacturacion() {
        System.out.println("\n--- SUBMENÚ: FACTURACIÓN Y VENTAS ---");
        System.out.println("1. Emitir Nueva Factura (Venta Interactiva)");
        System.out.println("2. Listar Historial de Comprobantes");
        System.out.println("3. Ver Comprobante Impreso ");
        System.out.println("4. Eliminar / Anular Factura");
        System.out.println("5. Listar Facturas Ordenadas por Número");
        System.out.println("6. Listar Facturas Ordenadas por Monto (mayor a menor)");
        System.out.println("7. Registrar Pago de Factura");
        System.out.println("8. Exportar Facturas a .txt");
        System.out.println("0. Volver al menú principal");
        System.out.print("Opción: ");
        try {
            int op = Integer.parseInt(scanner.nextLine());
            switch (op) {
                case 1 -> emitirNuevaFactura();
                case 2 -> {
                    System.out.println("\n=== HISTORIAL DE FACTURAS ===");
                    for (Factura f : facturaServicio.listar()) {
                        System.out.println("N°: " + f.getPuntoVenta() + "-" + f.getNumeroComprobante() + " | Cliente: " + f.getCliente().getNombre() + " | Total: $" + f.calcularTotal() + " | CAE: " + f.getCae());
                    }
                }
                case 3 -> {
                    List<Factura> facturas = facturaServicio.listar();
                    if (facturas.isEmpty()) {
                        System.out.println("No hay facturas emitidas en el sistema.");
                    } else {
                        System.out.println("\n=== SELECCIONAR COMPROBANTE A IMPRIMIR ===");
                        for (int i = 0; i < facturas.size(); i++) {
                            Factura f = facturas.get(i);
                            System.out.println((i + 1) + ". N° " + f.getPuntoVenta() + "-" + f.getNumeroComprobante() 
                                + " | Cliente: " + f.getCliente().getNombre() 
                                + " | Total: $" + f.calcularTotal());
                        }
                        System.out.print("Ingrese el número de la factura a ver: ");
                        int idx = Integer.parseInt(scanner.nextLine()) - 1;

                        if (idx >= 0 && idx < facturas.size()) {
                            Imprimible comprobante = facturas.get(idx);
                            comprobante.ImprimibleTicket();
                        } else {
                            System.out.println("Opción de factura no válida.");
                        }
                    }
                }
                    default -> System.out.println("Opción inválida.");
                
                case 4 -> { eliminarFactura();
                }

                case 5 -> listarFacturasPorNumero();

                case 6 -> listarFacturasPorMonto();

                case 7 -> registrarPagoFactura();

                case 8 -> exportarTxt(facturaServicio.listar(), "exportaciones/facturas.txt");

                case 0 -> System.out.println("Volviendo al menú principal...");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en ingreso de datos.");
        }
    }

    private void listarFacturasPorNumero() {
        mostrarListaFacturas("FACTURAS ORDENADAS POR NÚMERO", facturaServicio.listarPorNumero());
    }

    private void listarFacturasPorMonto() {
        mostrarListaFacturas("FACTURAS ORDENADAS POR MONTO", facturaServicio.listarPorMonto());
    }

    private void mostrarListaFacturas(String titulo, List<Factura> lista) {
        System.out.println("\n=== " + titulo + " ===");
        if (lista.isEmpty()) {
            System.out.println("No hay facturas emitidas en el sistema.");
            return;
        }
        for (Factura f : lista) {
            System.out.println("N°: " + f.getPuntoVenta() + "-" + f.getNumeroComprobante() + " | Cliente: " + f.getCliente().getNombre() + " | Total: $" + f.calcularTotal());
        }
    }

    // Sirve para cualquier lista de objetos Exportable (facturas, clientes, catálogo)
    private void exportarTxt(List<? extends Exportable> elementos, String ruta) {
        try {
            exportacionServicio.exportar(elementos, ruta);
            System.out.println("Datos exportados en " + ruta);
        } catch (IOException e) {
            System.out.println("Error al exportar el archivo: " + e.getMessage());
        }
    }

    private void registrarPagoFactura() {
        List<Factura> facturas = facturaServicio.listar();
        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas.");
            return;
        }
        for (int i = 0; i < facturas.size(); i++) {
            Factura f = facturas.get(i);
            System.out.println((i + 1) + ". Factura N° " + f.getPuntoVenta() + "-" + f.getNumeroComprobante() + " | Cliente: " + f.getCliente().getNombre() + " | Total: $" + f.calcularTotal() + " | Estado: " + f.getEstadoPago());
        }
        System.out.print("Ingrese el número de la factura a pagar: ");
        int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx < 0 || idx >= facturas.size()) {
            System.out.println("Factura no válida.");
            return;
        }

        Factura factura = facturas.get(idx);
        System.out.println("Saldo pendiente: $" + factura.calcularSaldo());
        System.out.print("Monto a pagar: ");
        double monto = Double.parseDouble(scanner.nextLine());
        if (monto <= 0) {
            System.out.println("El monto debe ser mayor a cero.");
            return;
        }

        try {
            facturaServicio.registrarPago(factura, monto);
            guardarDatos();
            System.out.println("Pago registrado. Estado de la factura: " + factura.getEstadoPago());
        } catch (PagoExedenteExcepcion e) {
            System.out.println("Pago rechazado: " + e.getMessage());
        }
    }

    private void emitirNuevaFactura() {
        List<Cliente> clientes = personaServicio.listarClientes();
        List<Empleado> empleados = personaServicio.listarEmpleados();
        List<ItemFacturable> catalogo = catalogoServicio.listar();
        if (clientes.isEmpty() || empleados.isEmpty() || catalogo.isEmpty()) {
            System.out.println(" Error: Debe registrar previamente al menos 1 Cliente, 1 Empleado y 1 Ítem en el Catálogo.");
            return;
        }

        System.out.println("\n=== NUEVA VENTA / EMISIÓN DE FACTURA ===");

        // 1. SELECCIÓN DE CLIENTE
        System.out.println("\n-- Seleccione el Cliente --");
        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            System.out.println((i + 1) + ". " + c.getNombre() + " (DNI/CUIT: " + c.getDni() + ")");
        }
        System.out.print("Ingrese el número del cliente: ");
        int idxCliente = Integer.parseInt(scanner.nextLine()) - 1;

        if (idxCliente < 0 || idxCliente >= clientes.size()) {
            System.out.println("Cliente no válido. Operación cancelada.");
            return;
        }
        Cliente clienteSeleccionado = clientes.get(idxCliente);

        // 2. SELECCIÓN DE EMPLEADO
        System.out.println("\n-- Seleccione el Empleado (Vendedor) --");
        for (int i = 0; i < empleados.size(); i++) {
            Empleado e = empleados.get(i);
            System.out.println((i + 1) + ". " + e.getNombre() + " (" + e.getPuesto() + ")");
        }
        System.out.print("Ingrese el número del empleado: ");
        int idxEmpleado = Integer.parseInt(scanner.nextLine()) - 1;

        if (idxEmpleado < 0 || idxEmpleado >= empleados.size()) {
            System.out.println("Empleado no válido. Operación cancelada.");
            return;
        }
        Empleado empleadoSeleccionado = empleados.get(idxEmpleado);

        List<LineaFactura> lineas = new ArrayList<>();

        // 3. CARGA DINÁMICA DE ÍTEMS
        boolean agregarMas = true;
        while (agregarMas) {
            System.out.println("\n-- Catálogo Disponible --");
            for (int i = 0; i < catalogo.size(); i++) {
                ItemFacturable item = catalogo.get(i);
                System.out.println((i + 1) + ". [" + item.getCodigo() + "] " + item.getNombre() + " - $" + item.getPrecio() + " (" + item.getDescripcion() + ")");
            }
            System.out.print("Seleccione el producto/servicio a vender: ");
            int idxItem = Integer.parseInt(scanner.nextLine()) - 1;

            if (idxItem >= 0 && idxItem < catalogo.size()) {
                ItemFacturable itemSeleccionado = catalogo.get(idxItem);
                System.out.print("Ingrese la cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());
                
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor a cero.");
                    continue; // Vuelve a pedir el ítem sin agregarlo a la factura
                }

                lineas.add(new LineaFactura(itemSeleccionado, cantidad, 0.0));
                System.out.println("Ítem agregado a la factura.");
            } else {
                System.out.println("Opción de ítem no válida.");
            }

            System.out.print("¿Desea agregar otro producto/servicio a esta factura? (s/n): ");
            String resp = scanner.nextLine().trim();
            if (!resp.equalsIgnoreCase("s")) {
                agregarMas = false;
            }
        }

        // 4. AUTORIZACIÓN Y GUARDADO
        Factura nuevaFactura;
        try {
            nuevaFactura = facturaServicio.emitir(clienteSeleccionado, empleadoSeleccionado, lineas);
        } catch (FacturaSinItemsExcepcion e) {
            System.out.println(e.getMessage() + " Operación cancelada.");
            return;
        } catch (LimiteCreditoExcedidoExcepcion e) {
            System.out.println("Venta rechazada: " + e.getMessage());
            return;
        }

        // Guardado automático en JSON
        guardarDatos();

        System.out.println("\n ¡VENTA REGISTRADA CON ÉXITO Y AUTORIZADA POR ARCA!");
        nuevaFactura.mostrarDetalle();
    }

    // =========================================================================
    // MÉTODOS AUXILIARES DE SELECCIÓN
    // =========================================================================
    private CondicionIVA seleccionarCondicionIVA() {
        System.out.println("Seleccione Condición IVA:");
        System.out.println("1. CONSUMIDOR_FINAL");
        System.out.println("2. RESPONSABLE_INSCRIPTO");
        System.out.println("3. MONOTRIBUTO");
        System.out.print("Opción (por defecto 1): ");
        try {
            int op = Integer.parseInt(scanner.nextLine());
            return switch (op) {
                case 2 -> CondicionIVA.RESPONSABLE_INSCRIPTO;
                case 3 -> CondicionIVA.MONOTRIBUTO;
                default -> CondicionIVA.CONSUMIDOR_FINAL;
            };
        } catch (NumberFormatException e) {
            return CondicionIVA.CONSUMIDOR_FINAL;
        }
    }

    // =========================================================================
    // MÉTODOS DE ELIMINACIÓN (BAJA)
    // =========================================================================

    private void eliminarItemCatalogo() {
        List<ItemFacturable> catalogo = catalogoServicio.listar();
        if (catalogo.isEmpty()) {
            System.out.println("El catálogo está vacío.");
            return;
        }
        listarCatalogo();
        System.out.print("Ingrese el número (índice 1 a N) del ítem a eliminar: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < catalogo.size()) {
                catalogoServicio.eliminar(catalogo.get(idx));
                guardarDatos();
                System.out.println("Ítem eliminado correctamente.");
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en el ingreso.");
        }
    }

    private void eliminarCliente() {
        List<Cliente> clientes = personaServicio.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println((i + 1) + ". " + clientes.get(i).getNombre());
        }
        System.out.print("Ingrese el número del cliente a eliminar: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < clientes.size()) {
                personaServicio.eliminarCliente(clientes.get(idx));
                guardarDatos();
                System.out.println("Cliente eliminado correctamente.");
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en el ingreso.");
        }
    }

    private void eliminarEmpleado() {
        List<Empleado> empleados = personaServicio.listarEmpleados();
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        for (int i = 0; i < empleados.size(); i++) {
            System.out.println((i + 1) + ". " + empleados.get(i).getNombre());
        }
        System.out.print("Ingrese el número del empleado a eliminar: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < empleados.size()) {
                personaServicio.eliminarEmpleado(empleados.get(idx));
                guardarDatos();
                System.out.println("Empleado eliminado correctamente.");
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en el ingreso.");
        }
    }

    private void eliminarProveedor() {
        List<Proveedor> proveedores = personaServicio.listarProveedores();
        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados.");
            return;
        }
        for (int i = 0; i < proveedores.size(); i++) {
            System.out.println((i + 1) + ". " + proveedores.get(i).getRazonSocial());
        }
        System.out.print("Ingrese el número del proveedor a eliminar: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < proveedores.size()) {
                personaServicio.eliminarProveedor(proveedores.get(idx));
                guardarDatos();
                System.out.println("Proveedor eliminado correctamente.");
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en el ingreso.");
        }
    }

    private void eliminarDepartamento() {
        List<Departamento> departamentos = departamentoServicio.listar();
        if (departamentos.isEmpty()) {
            System.out.println("No hay departamentos registrados.");
            return;
        }
        for (int i = 0; i < departamentos.size(); i++) {
            System.out.println((i + 1) + ". " + departamentos.get(i).getNombre());
        }
        System.out.print("Ingrese el número del departamento a eliminar: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < departamentos.size()) {
                departamentoServicio.eliminar(departamentos.get(idx));
                guardarDatos(); 
                System.out.println("Departamento eliminado correctamente.");
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en el ingreso.");
        }
    }

    private void eliminarFactura() {
        List<Factura> facturas = facturaServicio.listar();
        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas.");
            return;
        }
        for (int i = 0; i < facturas.size(); i++) {
            Factura f = facturas.get(i);
            System.out.println((i + 1) + ". Factura N° " + f.getPuntoVenta() + "-" + f.getNumeroComprobante() + " | Cliente: " + f.getCliente().getNombre());
        }
        System.out.print("Ingrese el número de la factura a eliminar: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < facturas.size()) {
                facturaServicio.eliminar(facturas.get(idx));
                guardarDatos();
                System.out.println("Factura eliminada correctamente.");
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en el ingreso.");
        }
    }
}