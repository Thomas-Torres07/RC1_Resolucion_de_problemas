package modelo;

public class ProductoPerecedero extends Producto {
    private String fechaCaducidad;
    private int diasParaVencer;
    private String lote;

    public ProductoPerecedero(String codigo, String nombre, int cantidad, double precioUnitario,
                              String fechaCaducidad, int diasParaVencer, String lote) {
        super(codigo, nombre, cantidad, precioUnitario);
        this.fechaCaducidad = fechaCaducidad.trim();
        this.diasParaVencer = diasParaVencer;

        this.lote = lote.trim().toUpperCase();
    }

    public int getDiasParaVencer() {
        return diasParaVencer;
    }

    public boolean estaProximoAVencer() {
        return diasParaVencer >= 0 && diasParaVencer <= 15;
    }

    @Override
    public double calcularValorTotal() {
        if (diasParaVencer < 0) {
            return 0.0;
        } else if (diasParaVencer <= 15) {
            return super.calcularValorTotal() * 0.80;
        }
        return super.calcularValorTotal();
    }

    @Override
    public String mostrarDetalle() {
        String estado = "";
        if (diasParaVencer < 0) {
            estado = " [VENCIDO]";
        } else if (estaProximoAVencer()) {
            estado = " [PRÓXIMO A VENCER]";
        }
        return "[PERECEDERO] " + super.mostrarDetalle() +
                " | Lote: " + lote + " | Vence: " + fechaCaducidad +
                " (" + diasParaVencer + " días)" + estado;
    }
}