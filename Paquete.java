public class Paquete {

    private String codigo;
    private int peso;
    private boolean contieneAlimentos;
    private int nivelUrgencia;

    public Paquete(String codigo, int peso, boolean contieneAlimentos, int nivelUrgencia) {
        this.codigo = codigo;
        this.peso = peso;
        this.contieneAlimentos = contieneAlimentos;
        this.nivelUrgencia = nivelUrgencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getPeso() {
        return peso;
    }

    public boolean contieneAlimentos() {
        return contieneAlimentos;
    }

    public int getNivelUrgencia() {
        return nivelUrgencia;
    }

    @Override
    public String toString() {
        return codigo;
    }
}