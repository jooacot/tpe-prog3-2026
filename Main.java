public class Main {
    public static void main(String[] args) {
        
        String pathCamiones = "C:\\Users\\Notebook\\Desktop\\tp3-prog3-2026\\tpe-prog3-2026\\camiones.txt";
        String pathPaquetes = "C:\\Users\\Notebook\\Desktop\\tp3-prog3-2026\\tpe-prog3-2026\\paquetes.txt";
        
        Servicio servicio = new Servicio(pathCamiones, pathPaquetes);
        
        //Servicio 1
        Paquete p = servicio.servicio1("P001");
        if (p != null) {
            System.out.println("    Encontrado: " + p + " - Peso: " + p.getPeso() + "kg - Urgencia: " + p.getNivelUrgencia());
        } else {
            System.out.println("    El paquete no existe.");
        }

        System.out.println("--------------------------------------------------");

        //Servicio 2
        System.out.println("Paquetes con alimentos");
        System.out.println(servicio.servicio2(true));
        System.out.println("Paquetes sin alimentos");
        System.out.println(servicio.servicio2(false));

        System.out.println("--------------------------------------------------");

        //Servicio3
        System.out.println("Paquetes con cierto nivel de urgencia(entre dos limites)");
        System.out.println(servicio.servicio3(10, 90));

        System.out.println("--------------------------------------------------");

        System.out.println("Greedy");
        System.out.println("Solución obtenida:");
        Servicio servicioGreedy = new Servicio(pathCamiones, pathPaquetes);
        servicioGreedy.asignarPaquetes();
        
        servicioGreedy.imprimirAsignacionCamiones();
        System.out.println("Peso no asignado: " + servicioGreedy.getPesoNoAsignadoGreedy() + " kg.");
        System.out.println("Métrica (candidatos considerados): " + servicioGreedy.getCandidatosGreedy());

        System.out.println("--------------------------------------------------");

        System.out.println("Backtracking");
        System.out.println("Solución obtenida:");
        Servicio servicioBack = new Servicio(pathCamiones, pathPaquetes);
        servicioBack.asignarPaquetesBack();
        
        servicioBack.imprimirAsignacionCamiones();
        System.out.println("Peso no asignado: " + servicioBack.getMenorPesoNoAsignado() + " kg.");
        System.out.println("Métrica (estados generados): " + servicioBack.getCantidadEstados());
    }
}