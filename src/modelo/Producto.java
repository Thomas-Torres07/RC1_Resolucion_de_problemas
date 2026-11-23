package modelo;

public class Producto {
    private String codigo;
    private String nombre;
    private int cantidad;
    private double precioUnitario;

    public Producto(String codigo, String nombre, int cantidad, double precioUnitario) {
        this.codigo = codigo.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double calcularValorTotal() {
        return this.cantidad * this.precioUnitario;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String mostrarDetalle() {
        return "Código: " + codigo + " | Nombre: " + nombre +
                " | Stock: " + cantidad + " | Precio: $" + String.format("%.2f", precioUnitario) +
                " | Total: $" + String.format("%.2f", calcularValorTotal());
    }
}