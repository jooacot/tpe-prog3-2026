import java.util.ArrayList;

public class Camion {

    private int id;
    private String patente;
    private int capacidad;
    private int capacidadDisponible;
    private boolean esRefrigerado;
    public ArrayList<Paquete> getPaquetes() {
        return paquetes;
    }

    private ArrayList<Paquete> paquetes;

    public Camion(int id, int capacidad, boolean esRefrigerado, String patente) {
        paquetes = new ArrayList<>();
        this.id = id;
        this.patente = patente;
        this.esRefrigerado = esRefrigerado;
        this.capacidad = capacidad;
        this.capacidadDisponible = capacidad;
    }

    public int getId() {
        return id;
    }

    public int getCapacidad() {
        return capacidad;
    }
    
    public int getCapacidadDisponible() {
        return capacidadDisponible;
    }
    
    public String getPatente() {
        return patente;
    }
    
    public boolean esRefrigerado(){
        return esRefrigerado;
    }
    
    public boolean puedeTransportar(Paquete p) {

        if (p.contieneAlimentos() && !this.esRefrigerado)
            return false;
            
        return p.getPeso() <= getCapacidadDisponible();
    }
    
    public void asignar(Paquete p){
        paquetes.add(p);
        this.capacidadDisponible -= p.getPeso();
    }
    
     public void desasignar(Paquete p){
        paquetes.remove(p);
        this.capacidadDisponible += p.getPeso();
    }

    @Override
    public String toString() {
        return id + " (" + capacidad + " kg)";
    }
}