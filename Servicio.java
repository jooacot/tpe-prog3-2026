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
    private int candidatosGreedy = 0; 
    
    private HashMap<Integer, ArrayList<Paquete>> mejorSolucion = new HashMap<>();

    private HashMap<String, Camion> camiones;
    private HashMap<String, Paquete> paquetes;
    private ArrayList<Paquete> paquetesConAlimentos;
    private ArrayList<Paquete> paquetesSinAlimentos;

    public Servicio(String pathCamiones, String pathPaquetes) {
        camiones = new HashMap<>();
        paquetes = new HashMap<>();
        paquetesConAlimentos = new ArrayList<>();
        paquetesSinAlimentos = new ArrayList<>();

        leerCamiones(pathCamiones);
        leerPaquetes(pathPaquetes);
    }

    private void agregarCamion(Camion camion) {
        camiones.put(camion.getPatente(), camion);
    }

    private void agregarPaquete(Paquete paquete) {
        paquetes.put(paquete.getCodigo(), paquete);
        if (paquete.contieneAlimentos()) {
            paquetesConAlimentos.add(paquete);
        } else {
            paquetesSinAlimentos.add(paquete);
        }
    }

       private void leerCamiones(String path) {
    try {
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine(); 
        String linea;
        while ((linea = br.readLine()) != null) {
            // Si la línea está vacía o no tiene contenido real, la saltea
            if (linea.trim().isEmpty()) continue; 

            String[] partes = linea.split(";");
            
            if (partes.length < 4) continue; 

            int id = Integer.parseInt(partes[0].trim());
            String patente = partes[1].trim();
            boolean esRefrigerado = Integer.parseInt(partes[2].trim()) == 1;
            int capacidad = Integer.parseInt(partes[3].trim());

            Camion camion = new Camion(id, capacidad, esRefrigerado, patente);
            agregarCamion(camion);
        }
        br.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void leerPaquetes(String path) {
    try {
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine(); 
        String linea;
        while ((linea = br.readLine()) != null) {
            // Si la línea está vacía, la saltea
            if (linea.trim().isEmpty()) continue; 

            String[] partes = linea.split(";");
            
            if (partes.length < 5) continue; 

            int id = Integer.parseInt(partes[0].trim());
            String codigo = partes[1].trim();
            int peso = Integer.parseInt(partes[2].trim());
            boolean contieneAlimentos = Integer.parseInt(partes[3].trim()) == 1;
            int nivelUrgencia = Integer.parseInt(partes[4].trim());

            Paquete paquete = new Paquete(id, codigo, peso, contieneAlimentos, nivelUrgencia);
            agregarPaquete(paquete);
        }
        br.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public Paquete servicio1(String codigoPaquete) { //O(1)
        return paquetes.get(codigoPaquete);
    }

    public List<Paquete> servicio2(boolean contieneAlimentos) { //O(1)
        if (contieneAlimentos) {
            return paquetesConAlimentos;
        } else {
            return paquetesSinAlimentos;
        }
    }

    public List<Paquete> servicio3(int urgenciaMinima, int urgenciaMaxima) {//O(n) : n son los paquetes
        List<Paquete> resultado = new ArrayList<>();
        for (Paquete p : paquetes.values()) {
            if (p.getNivelUrgencia() >= urgenciaMinima && p.getNivelUrgencia() <= urgenciaMaxima) {
                resultado.add(p);
            }
        }
        return resultado;
    }
    
    /*
     * Estrategia de Greedy:
     * La estrategia elegida consiste en ordenar los paquetes en forma descendente
     * segun su peso. Luego, para cada paquete, se busca entre los camiones
     * disponibles aquel que pueda transportarlo respetando las restricciones del
     * problema y que, una vez asignado el paquete, deje la menor capacidad libre
     * posible. De esta manera buscamos minimizar el peso de los paquetes que no
     * fueron asigandos
     * 
     * Complejidad:
     * O(P*C) -> P=paquetes, C=Camiones
     */
    public void asignarPaquetes() {
        candidatosGreedy = 0; 
        ArrayList<Paquete> paquetesOrdenados = new ArrayList<>(paquetes.values());

        Collections.sort(paquetesOrdenados, new Comparator<Paquete>() {
            public int compare(Paquete p1, Paquete p2) {
                return Integer.compare(p2.getPeso(), p1.getPeso());
            }
        });

        for (Paquete p : paquetesOrdenados) {
            Camion mejor = null;
            int menorEspacioLibre = Integer.MAX_VALUE;

            for (Camion c : camiones.values()) {
                candidatosGreedy++; // Contamos el candidato evaluado

                if (c.puedeTransportar(p)) {
                    int espacioLibre = c.getCapacidadDisponible() - p.getPeso();
                    if (espacioLibre >= 0 && espacioLibre < menorEspacioLibre) {
                        menorEspacioLibre = espacioLibre;
                        mejor = c;
                    }
                }
            }
            if (mejor != null) {
                mejor.asignar(p);
            }
        }
    }
    
    public int getPesoNoAsignadoGreedy() {
        int pesoTotal = 0;
        for (Paquete p : paquetes.values()) {
            pesoTotal += p.getPeso();
        }
        int pesoAsignado = 0;
        for (Camion c : camiones.values()) {
            for (Paquete p : c.getPaquetes()) {
                pesoAsignado += p.getPeso();
            }
        }
        return pesoTotal - pesoAsignado;
    }

    public void imprimirAsignacionCamiones() {
        for (Camion c : camiones.values()) {
            System.out.println("  Camion " + c.getId() + " (Patente: " + c.getPatente() + "): " + c.getPaquetes());
        }
    }

    public void asignarPaquetesBack() {
        estadosGenerados = 0;
        menorPesoNoAsignado = Integer.MAX_VALUE;
        mejorSolucion.clear();

        ArrayList<Paquete> paquetesBack = new ArrayList<>(paquetes.values());
        ArrayList<Camion> camionesBack = new ArrayList<>(camiones.values());

        backtracking(paquetesBack, camionesBack, 0, 0);

        // al terminar, le devolvemos a los camiones los paquetes de la mejor solucion
        for (Camion c : camiones.values()) {
            ArrayList<Paquete> asignados = mejorSolucion.get(c.getId());
            if (asignados != null) {
                for (Paquete p : asignados) {
                    c.asignar(p);
                }
            }
        }
    }
    
    /*
     * El algoritmo realiza una exploración en profundidad (DFS) buscando minimizar
     * el peso total no asignado. Para optimizar el rendimiento y reducir la
     * cantidad de estados generados, incorporamos una poda para que sea mas
     * eficiente: si en una solución parcial el peso acumulado de los paquetes
     * sueltos ya iguala o supera al mejor resultado registrado hasta el momento, la
     * rama se corta inmediatamente.
     * Complejidad : O(C^P) donde C son los campiones y P los paquetes y es elevado
     * porque cada paquete puede asignarse a cualquiera de los camiones
     */
    private void backtracking(ArrayList<Paquete> paq, ArrayList<Camion> cam, int pesoNoAsignado, int pos) {
        estadosGenerados++; 

        if (pesoNoAsignado >= menorPesoNoAsignado) { 
            return;
        }
        
        if (pos == paq.size()) {
            if (pesoNoAsignado < menorPesoNoAsignado) {
                menorPesoNoAsignado = pesoNoAsignado;
                
                // guardamos el estado ganador actual
                mejorSolucion.clear();
                for (Camion c : cam) {
                    mejorSolucion.put(c.getId(), new ArrayList<>(c.getPaquetes()));
                }
            }
            return;
        }

        Paquete p = paq.get(pos);

        for (Camion c : cam) { 
            if (c.puedeTransportar(p)) {
                c.asignar(p);
                backtracking(paq, cam, pesoNoAsignado, pos + 1); 
                c.desasignar(p);
            }
        }
        
        backtracking(paq, cam, pesoNoAsignado + p.getPeso(), pos + 1);
    }

    public int getCantidadEstados(){
        return this.estadosGenerados;
    }

    public int getCandidatosGreedy() {
        return this.candidatosGreedy;
    }

    public int getMenorPesoNoAsignado() {
        return this.menorPesoNoAsignado;
    }

    public java.util.HashMap<String, Camion> getCamiones() {
        return this.camiones;
    }
    
    public java.util.HashMap<String, Paquete> getPaquetes() {
        return this.paquetes;
    }
}