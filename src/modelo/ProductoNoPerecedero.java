package modelo;

public class ProductoNoPerecedero extends Producto {
    private int mesesGarantia;
    private String material;

    public ProductoNoPerecedero(String codigo, String nombre, int cantidad, double precioUnitario,
                                int mesesGarantia, String material) {
        super(codigo, nombre, cantidad, precioUnitario);
        this.mesesGarantia = mesesGarantia;
        this.material = material.trim();
    }

    @Override
    public String mostrarDetalle() {
        return "[NO PERECEDERO] " + super.mostrarDetalle() +
                " | Material: " + material + " | Garantía: " + mesesGarantia + " meses";
    }
}