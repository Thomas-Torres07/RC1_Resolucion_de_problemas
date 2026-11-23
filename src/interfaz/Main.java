package interfaz;

import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import negocio.GestionInventario;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static GestionInventario inventario = new GestionInventario();

    public static void main(String[] args) {
        cargarDatosIniciales();
        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            String entrada = scanner.nextLine().trim();

            if (!esEntero(entrada, false)) {
                System.out.println("ERROR: Debe ingresar un número válido del menú.");
            } else {
                opcion = Integer.parseInt(entrada);
                procesarOpcion(opcion);
            }
        }
        System.out.println("Programa finalizado.");
    }

    private static void mostrarMenu() {
        System.out.println("\n=======================================================");
        System.out.println("   SISTEMA DE GESTIÓN DE INVENTARIO - ECOESENCIA");
        System.out.println("=======================================================");
        System.out.println("1. Registrar nuevo producto");
        System.out.println("2. Listar inventario completo");
        System.out.println("3. Buscar producto por código");
        System.out.println("4. Actualizar stock - Entrada/Salida");
        System.out.println("5. Alertas de vencimiento y valoración total");
        System.out.println("6. Eliminar producto");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarProductoUI();
                break;
            case 2:
                listarInventarioUI();
                break;
            case 3:
                buscarProductoUI();
                break;
            case 4:
                actualizarStockUI();
                break;
            case 5:
                reporteAlertasYValoracionUI();
                break;
            case 6:
                eliminarProductoUI();
                break;
            case 0:
                break;
            default:
                System.out.println("ERROR: Opción inválida (0-6).");
                break;
        }
    }

    private static void registrarProductoUI() {
        System.out.println("\n--- REGISTRO DE NUEVO PRODUCTO ---");
        // 1. Validar tipo de producto
        String tipoStr = "";
        while (!tipoStr.equals("1") && !tipoStr.equals("2")) {
            System.out.print("Tipo (1 = Perecedero, 2 = No Perecedero): ");
            tipoStr = scanner.nextLine().trim();
            if (!tipoStr.equals("1") && !tipoStr.equals("2")) {
                System.out.println("ERROR: Debe seleccionar 1 o 2.");
            }
        }
        // 2. Validar código
        String codigo = "";
        while (codigo.isEmpty()) {
            System.out.print("Código: ");
            codigo = scanner.nextLine().trim();
            if (codigo.isEmpty()) {
                System.out.println("ERROR: El código es obligatorio y no puede estar vacío.");
            }
        }
        // 3. Validar nombre
        String nombre = "";
        while (nombre.isEmpty()) {
            System.out.print("Nombre del producto: ");
            nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("ERROR: El nombre del producto es obligatorio.");
            }
        }
        // 4. Validar cantidad inicial
        int cantidad = 0;
        boolean cantidadValida = false;
        while (!cantidadValida) {
            System.out.print("Cantidad inicial: ");
            String cantStr = scanner.nextLine().trim();
            if (cantStr.isEmpty()) {
                System.out.println("ERROR: La cantidad es obligatoria y no puede estar vacía.");
            } else if (!esEntero(cantStr, false)) {
                System.out.println("ERROR: La cantidad debe ser un número entero positivo.");
            } else {
                cantidad = Integer.parseInt(cantStr);
                cantidadValida = true;
            }
        }
        // 5. Validar precio unitario
        double precio = 0.0;
        boolean precioValido = false;
        while (!precioValido) {
            System.out.print("Precio unitario ($): ");
            String precioStr = scanner.nextLine().trim();
            if (precioStr.isEmpty()) {
                System.out.println("ERROR: El precio unitario es obligatorio.");
            } else if (!esDouble(precioStr)) {
                System.out.println("ERROR: El precio debe ser un número válido.");
            } else {
                precio = Double.parseDouble(precioStr);
                if (precio <= 0) {
                    System.out.println("ERROR: El precio debe ser mayor a 0.");
                } else {
                    precioValido = true;
                }
            }
        }
        // 6. Datos específicos según herencia
        Producto nuevo;
        if (tipoStr.equals("1")) {
            String fecha = "";
            boolean fechaValida = false;
            while (!fechaValida) {
                System.out.print("Fecha de caducidad (AAAA-MM-DD): ");
                fecha = scanner.nextLine().trim();
                if (!esFechaValida(fecha)) {
                    System.out.println("ERROR: Formato de fecha inválido. Use AAAA-MM-DD (ej. 2026-12-31).");
                } else {
                    fechaValida = true;
                }
            }
            int diasParaVencer = 0;
            boolean diasValidos = false;
            while (!diasValidos) {
                System.out.print("Días restantes para caducar: ");
                String diasStr = scanner.nextLine().trim();
                if (!esEntero(diasStr, true)) {
                    System.out.println("ERROR: Los días restantes deben ser un número entero.");
                } else {
                    diasParaVencer = Integer.parseInt(diasStr);
                    diasValidos = true;
                }
            }
            String lote = "";
            while (lote.isEmpty()) {
                System.out.print("Número de lote: ");
                lote = scanner.nextLine().trim();
                if (lote.isEmpty()) {
                    System.out.println("ERROR: El lote es obligatorio.");
                }
            }
            nuevo = new ProductoPerecedero(codigo, nombre, cantidad, precio, fecha, diasParaVencer, lote);
        } else {
            int garantia = 0;
            boolean garantiaValida = false;
            while (!garantiaValida) {
                System.out.print("Meses de garantía: ");
                String garantiaStr = scanner.nextLine().trim();
                if (!esEntero(garantiaStr, false)) {
                    System.out.println("ERROR: Los meses de garantía deben ser un número entero positivo.");
                } else {
                    garantia = Integer.parseInt(garantiaStr);
                    garantiaValida = true;
                }
            }
            String material = "";
            while (material.isEmpty()) {
                System.out.print("Material principal: ");
                material = scanner.nextLine().trim();
                if (material.isEmpty()) {
                    System.out.println("ERROR: El material es obligatorio.");
                }
            }

            nuevo = new ProductoNoPerecedero(codigo, nombre, cantidad, precio, garantia, material);
        }
        String resultado = inventario.registrarProducto(nuevo);
        if (resultado.equals("OK")) {
            System.out.println("ÉXITO: Producto registrado correctamente.");
        } else {
            System.out.println("[VALIDACIÓN] " + resultado);
        }
    }

    private static void listarInventarioUI() {
        System.out.println("\n--- INVENTARIO ACTUAL ---");
        ArrayList<Producto> productos = inventario.listarTodos();
        if (productos.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        for (int i = 0; i < productos.size(); i++) {
            System.out.println(productos.get(i).mostrarDetalle());
        }
    }

    private static void buscarProductoUI() {
        String codigo = "";
        while (codigo.isEmpty()) {
            System.out.print("\nIngrese el código del producto a buscar: ");
            codigo = scanner.nextLine().trim();
            if (codigo.isEmpty()) {
                System.out.println("ERROR: Debe ingresar un código.");
            }
        }

        Producto p = inventario.buscarPorCodigo(codigo);
        if (p == null) {
            System.out.println("[AVISO] Producto no encontrado.");
        } else {
            System.out.println("[ENCONTRADO] " + p.mostrarDetalle());
        }
    }

    private static void actualizarStockUI() {
        String codigo = "";
        while (codigo.isEmpty()) {
            System.out.print("\nCódigo del producto: ");
            codigo = scanner.nextLine().trim();
            if (codigo.isEmpty()) {
                System.out.println("ERROR: Debe ingresar un código.");
            }
        }

        int delta = 0;
        boolean deltaValido = false;
        while (!deltaValido) {
            System.out.print("Cantidad a sumar (positivo) o restar (negativo): ");
            String deltaStr = scanner.nextLine().trim();
            if (!esEntero(deltaStr, true)) {
                System.out.println("ERROR: Ingrese un número entero válido.");
            } else {
                delta = Integer.parseInt(deltaStr);
                deltaValido = true;
            }
        }
        String resultado = inventario.actualizarStock(codigo, delta);
        if (resultado.equals("OK")) {
            System.out.println("[ÉXITO] Stock actualizado correctamente.");
        } else {
            System.out.println("[VALIDACIÓN] " + resultado);
        }
    }

    private static void reporteAlertasYValoracionUI() {
        System.out.println("\n--- ALERTAS DE VENCIMIENTO Y VALORACIÓN ---");
        ArrayList<ProductoPerecedero> alertas = inventario.obtenerProximosAVencer();
        if (alertas.isEmpty()) {
            System.out.println("No hay productos vencidos ni próximos a vencer.");
        } else {
            System.out.println("Productos que requieren rotación urgente (descuento del 20%):");
            for (int i = 0; i < alertas.size(); i++) {
                System.out.println(" -> " + alertas.get(i).mostrarDetalle());
            }
        }
        System.out.printf("VALOR TOTAL ESTIMADO EN BODEGA: $%.2f%n", inventario.calcularValorTotalBodega());
    }

    private static void eliminarProductoUI() {
        String codigo = "";
        while (codigo.isEmpty()) {
            System.out.print("\nIngrese el código del producto a eliminar: ");
            codigo = scanner.nextLine().trim();
            if (codigo.isEmpty()) {
                System.out.println("ERROR: Debe ingresar un código.");
            }
        }

        if (inventario.eliminarProducto(codigo)) {
            System.out.println("[ÉXITO] Producto eliminado del sistema.");
        } else {
            System.out.println("[AVISO] No se encontró ningún producto con ese código.");
        }
    }

    private static boolean esEntero(String texto, boolean permitirNegativo) {
        if (texto == null || texto.isEmpty()) {
            return false;
        }
        int inicio = 0;
        if (texto.charAt(0) == '-') {
            if (!permitirNegativo || texto.length() == 1) {
                return false;
            }
            inicio = 1;
        }
        for (int i = inicio; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean esDouble(String texto) {
        if (texto == null || texto.isEmpty()) {
            return false;
        }
        int puntos = 0;
        boolean tieneDigitos = false;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '.') {
                puntos++;
                if (puntos > 1) {
                    return false;
                }
            } else if (Character.isDigit(c)) {
                tieneDigitos = true;
            } else {
                return false;
            }
        }
        return tieneDigitos;
    }

    private static boolean esFechaValida(String fecha) {
        if (fecha == null || fecha.length() != 10) {
            return false;
        }
        if (fecha.charAt(4) != '-' || fecha.charAt(7) != '-') {
            return false;
        }
        for (int i = 0; i < fecha.length(); i++) {
            if (i != 4 && i != 7 && !Character.isDigit(fecha.charAt(i))) {
                return false;
            }
        }
        int mes = Integer.parseInt(fecha.substring(5, 7));
        int dia = Integer.parseInt(fecha.substring(8, 10));
        return mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31;
    }
    private static void cargarDatosIniciales() {
        inventario.registrarProducto(new ProductoPerecedero(
                "P001", "Crema Hidratante Aloe", 20, 12.50,
                "2026-12-02", 10, "L-2026A"));
        inventario.registrarProducto(new ProductoPerecedero(
                "P002", "Aceite Esencial Lavanda", 15, 18.00,
                "2027-05-20", 180, "L-2026B"));
        inventario.registrarProducto(new ProductoNoPerecedero(
                "P003", "Cepillo Facial de Bambú", 40, 6.50,
                12, "Bambú Orgánico"));
    }
}