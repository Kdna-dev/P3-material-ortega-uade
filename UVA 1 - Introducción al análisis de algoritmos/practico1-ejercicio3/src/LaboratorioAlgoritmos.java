import java.util.Random;

public class LaboratorioAlgoritmos {

    public static void main(String[] args) {
        // Tamaños de entrada definidos en la guía práctica
        int[] tamanos = {1000, 10000, 100000};

        System.out.println("=======================================================================");
        System.out.println("     EXPERIMENTO DE MEDIPCIÓN EMPÍRICA VS TEÓRICA (PROGRAMACIÓN III)");
        System.out.println("=======================================================================\n");

        for (int n : tamanos) {
            System.out.println("--- Pruebas para n = " + n + " ---");

            // Generamos un arreglo con n elementos aleatorios
            int[] arreglo = generarArregloAleatorio(n);

            // --- PRUEBA ALGORITMO A: O(n) ---
            long inicioA = System.currentTimeMillis();
            int maxA = buscarMaximoLineal(arreglo);
            long finA = System.currentTimeMillis();
            long tiempoA = finA - inicioA;

            System.out.println("Algoritmo A - Lineal O(n):");
            System.out.println("  -> Máximo encontrado: " + maxA);
            System.out.println("  -> Tiempo de ejecución: " + tiempoA + " ms\n");

            // --- PRUEBA ALGORITMO B: O(n^2) ---
            // Atención: Para n = 100.000 el Algoritmo B puede demorar varios segundos/minutos.
            System.out.println("Algoritmo B - Cuadrático O(n^2):");

            if (n > 50000) {
                System.out.println("  -> [Aviso] Ejecutando O(n^2) para n = " + n + "...");
            }

            long inicioB = System.currentTimeMillis();
            int maxB = buscarMaximoCuadratico(arreglo);
            long finB = System.currentTimeMillis();
            long tiempoB = finB - inicioB;

            System.out.println("  -> Máximo encontrado: " + maxB);
            System.out.println("  -> Tiempo de ejecución: " + tiempoB + " ms\n");
            System.out.println("-----------------------------------------------------------------------\n");
        }
    }

    /**
     * ALGORITMO A: Búsqueda Lineal - O(n)
     * Recorre el arreglo una única vez manteniendo una variable con el máximo actual.
     */
    public static int buscarMaximoLineal(int[] v) {
        int max = v[0];
        for (int i = 1; i < v.length; i++) {
            if (v[i] > max) {
                max = v[i];
            }
        }
        return max;
    }

    /**
     * ALGORITMO B: Comparación Par a Par (Ingenuo) - O(n^2)
     * Compara cada elemento contra todos los demás para verificar si es el mayor.
     */
    public static int buscarMaximoCuadratico(int[] v) {
        int n = v.length;
        for (int i = 0; i < n; i++) {
            boolean esElMayor = true;
            for (int j = 0; j < n; j++) {
                if (v[j] > v[i]) {
                    esElMayor = false;
                    break; // Pasa a verificar el siguiente elemento
                }
            }
            if (esElMayor) {
                return v[i];
            }
        }
        return v[0];
    }

    /**
     * Método auxiliar para generar un arreglo cargado con números aleatorios.
     */
    public static int[] generarArregloAleatorio(int tamano) {
        Random rand = new Random();
        int[] arreglo = new int[tamano];
        for (int i = 0; i < tamano; i++) {
            arreglo[i] = rand.nextInt(1_000_000); // Números aleatorios entre 0 y 999.999
        }
        return arreglo;
    }
}
