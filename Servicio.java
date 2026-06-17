import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Servicio {

    private int menorPesoNoAsignado = Integer.MAX_VALUE;
    private int estadosGenerados = 0;
    private HashMap<String, Camion> camiones;
    private HashMap<String, Paquete> paquetes;

    public Servicio(String pathCamiones, String pathPaquetes) { // O(n) = n seria la suma de camiones y paquetes que tiene que procesar
        camiones = new HashMap<>();
        paquetes = new HashMap<>();

        leerCamiones(pathCamiones);
        leerPaquetes(pathPaquetes);
    }

    private void leerCamiones(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            String linea;
            while ((linea = br.readLine()) != null) {

                String[] partes = linea.split(";");

                String id = partes[0];
                int capacidad = Integer.parseInt(partes[1]);

                Camion camion = new Camion(id, capacidad, true);

                camiones.put(id, camion);
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void leerPaquetes(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            String linea;
            while ((linea = br.readLine()) != null) {

                String[] partes = linea.split(";");

                String codigo = partes[0];
                int peso = Integer.parseInt(partes[1]);

                boolean contieneAlimentos = Integer.parseInt(partes[2]) == 1;

                int nivelUrgencia = Integer.parseInt(partes[3]);

                Paquete paquete = new Paquete(codigo, peso, contieneAlimentos, nivelUrgencia);

                paquetes.put(codigo, paquete);
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // SERVICIO 1
    public Paquete servicio1(String codigoPaquete) { //O(1): Porque al hacer un hashmap la busqueda es instantanea
    //  utilizando clave valor, en este caso la clave es la patente y el valor es el paquete
        return paquetes.get(codigoPaquete);
    }

    // SERVICIO 2
    public List<Paquete> servicio2(boolean contieneAlimentos) { // O(n) = n son los paquetes

        List<Paquete> resultado = new ArrayList<>();

        for (Paquete p : paquetes.values()) {
            
            if (p.contieneAlimentos() == contieneAlimentos) {
                resultado.add(p);
            }
        }

        return resultado;
    }
    
    //SERVICIO 3
    public List<Paquete> servicio3(int urgenciaMinima, int urgenciaMaxima) {

        List<Paquete> resultado = new ArrayList<>();

        for (Paquete p : paquetes.values()) { //O(n) = n son los paquetes

            if (p.getNivelUrgencia() >= urgenciaMinima &&
                p.getNivelUrgencia() <= urgenciaMaxima) {

                resultado.add(p);
            }
        }

        return resultado;
    }
    
    /* Estrategia de Greedy:
    La estrategia elegida consiste en ordenar los paquetes en forma descendente segun su peso. Luego, para 
    cada paquete, se busca entre los camiones disponibles aquel que pueda transportarlo respetando las 
    restricciones del problema y que, una vez asignado el paquete, deje la menor capacidad libre posible.
     De esta manera buscamos minimizar el pesoo de los paquetes que no fueron asigandos
    
       Complejidad:
    O(P*C) -> P=paquetes, C=Camiones
    */
    public void asignarPaquetes(){
    
        ArrayList<Paquete> paquetesOrdenados = new ArrayList<>();
        
        for (Paquete p : paquetes.values()) {
                paquetesOrdenados.add(p);
        }
        
        // Ordenamos los paquetes por peso de forma descendente
        Collections.sort(paquetesOrdenados, new Comparator<Paquete>() {
            public int compare(Paquete p1, Paquete p2) {
                return Integer.compare(p2.getPeso(), p1.getPeso()); 
            }
        });
        
        for (Paquete p : paquetesOrdenados){
            Camion mejor = null;
            int menorEspacioLibre = Integer.MAX_VALUE;
            
            for (Camion c : camiones.values()){
                
                if (c.puedeTransportar(p)){
                    
                    int espacioLibre = c.getCapacidad() - p.getPeso();
                    
                    if (espacioLibre >= 0 && espacioLibre < menorEspacioLibre){
                        menorEspacioLibre = espacioLibre;
                        mejor = c;
                        
                    }
                    
                }
            }
            if (mejor != null){
                mejor.asignar(p);
            }
        }
    }
    
    public void asignarPaquetesBack(){
        
        ArrayList<Paquete> paquetesBack = new ArrayList<>();
        for (Paquete p : paquetes.values()) {
                paquetesBack.add(p);
        }
        
        ArrayList<Camion> camionesBack = new ArrayList<>();
        for (Camion c : camiones.values()) {
                camionesBack.add(c);
        }
        
        backtracking(paquetesBack, camionesBack, 0, 0);
        
    }
    
    /*El algoritmo realiza una exploración en profundidad (DFS) buscando minimizar el peso total no asignado.
     Para optimizar el rendimiento y reducir la cantidad de estados generados, incorporamos una poda para que 
     sea mas eficiente: si en una solución parcial el peso acumulado de los paquetes sueltos ya iguala o supera
      al mejor resultado registrado hasta el momento, la rama se corta inmediatamente.*/
    private void backtracking(ArrayList<Paquete> paq, ArrayList<Camion> cam, int pesoNoAsignado, int pos) {
        estadosGenerados++; // Contamos cada estado del arbol
        
        if (pesoNoAsignado >= menorPesoNoAsignado){ // PODA
            return;
        }
        // Caso base: procesamos todos los paquetes
        if (pos == paq.size()) {
            if (pesoNoAsignado < menorPesoNoAsignado) {
                menorPesoNoAsignado = pesoNoAsignado;
            }
            return;
        }
    
        Paquete p = paq.get(pos);
    
        for (Camion c : cam) { // Recorremos los camiones
            if (c.puedeTransportar(p)) {
                c.asignar(p); 
                
                backtracking(paq, cam, pesoNoAsignado, pos + 1); // Llamada recursiva: obtenemos el siguente paquete
                
                c.desasignar(p); 
            }
        }
        //Paquete no asginado
        backtracking(paq, cam, pesoNoAsignado + p.getPeso(), pos + 1);
    }
}