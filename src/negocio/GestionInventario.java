package negocio;

import modelo.Producto;
import modelo.ProductoPerecedero;
import java.util.ArrayList;

public class GestionInventario {
    private ArrayList<Producto> listaProductos;
    private final int CAPACIDAD_MAXIMA = 500;

    public GestionInventario() {
        this.listaProductos = new ArrayList<>();
    }

    public String registrarProducto(Producto nuevo) {
        if (listaProductos.size() >= CAPACIDAD_MAXIMA) {
            return "Error: Capacidad máxima de bodega alcanzada.";
        }
        if (buscarPorCodigo(nuevo.getCodigo()) != null) {
            return "Error: Ya existe un producto con el código " + nuevo.getCodigo();
        }
        listaProductos.add(nuevo);
        return "OK";
    }

    public Producto buscarPorCodigo(String codigo) {
        for (int i = 0; i < listaProductos.size(); i++) {
            Producto p = listaProductos.get(i);
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return p;
            }
        }
        return null;
    }

    public String actualizarStock(String codigo, int cantidadMovimiento) {
        Producto encontrado = buscarPorCodigo(codigo);
        if (encontrado == null) {
            return "Error: No se encontró el producto con código " + codigo;
        }
        int nuevoStock = encontrado.getCantidad() + cantidadMovimiento;
        if (nuevoStock < 0) {
            return "Error: Stock insuficiente. Disponible: " + encontrado.getCantidad();
        }
        encontrado.setCantidad(nuevoStock);
        return "OK";
    }

    public boolean eliminarProducto(String codigo) {
        Producto encontrado = buscarPorCodigo(codigo);
        if (encontrado != null) {
            listaProductos.remove(encontrado);
            return true;
        }
        return false;
    }

    public ArrayList<Producto> listarTodos() {
        return listaProductos;
    }

    public ArrayList<ProductoPerecedero> obtenerProximosAVencer() {
        ArrayList<ProductoPerecedero> alertas = new ArrayList<>();
        for (int i = 0; i < listaProductos.size(); i++) {
            Producto p = listaProductos.get(i);
            if (p instanceof ProductoPerecedero) {
                ProductoPerecedero perecedero = (ProductoPerecedero) p;
                if (perecedero.estaProximoAVencer() || perecedero.getDiasParaVencer() < 0) {
                    alertas.add(perecedero);
                }
            }
        }
        return alertas;
    }

    public double calcularValorTotalBodega() {
        double total = 0.0;
        for (int i = 0; i < listaProductos.size(); i++) {
            total += listaProductos.get(i).calcularValorTotal();
        }
        return total;
    }
}