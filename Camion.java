import java.util.ArrayList;

public class Camion {

    private String id;
    private int capacidad;
    private int capacidadDisponible;
    private boolean esRefrigerado;
    private ArrayList<Paquete> paquetes;

    public Camion(String id, int capacidad, boolean esRefrigerado) {
        this.paquetes = new ArrayList<>();
        this.id = id;
        this.esRefrigerado = esRefrigerado;
        this.capacidad = capacidad;
        this.capacidadDisponible = capacidad;
    }

    public String getId() {
        return id;
    }

    public int getCapacidad() {
        return capacidad;
    }
    
    public boolean esRefrigerado(){
        return esRefrigerado;
    }
    
     public boolean puedeTransportar(Paquete p) {

        if (p.contieneAlimentos() && !this.esRefrigerado)
            return false;
            
        return p.getPeso() <= getCapacidad();
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