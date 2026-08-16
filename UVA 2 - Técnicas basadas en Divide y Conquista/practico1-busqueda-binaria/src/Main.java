import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int n = 10_000_000;
        int[] S = new int[n];

        // Rellenar arreglo de forma ordenada
        for (int i = 0; i < n; i++) {
            S[i] = i * 2;
        }

        int x = -1; // Elemento no existente para probar el peor caso

        System.out.println("--- Probando Búsqueda Lineal ---");
        long inicioLineal = System.nanoTime();
        boolean halladoLineal = busquedaLineal(S, x);
        long finLineal = System.nanoTime();
        long tiempoLineal = (finLineal - inicioLineal);
        System.out.println("Resultado: " + halladoLineal);
        System.out.println("Tiempo Búsqueda Lineal: " + tiempoLineal / 1_000_000.0 + " ms\n");

        System.out.println("--- Probando Búsqueda Binaria Recursiva ---");
        long inicioBinaria = System.nanoTime();
        boolean halladoBinaria = BusquedaBinaria.buscar(S, x, 0, S.length - 1);
        long finBinaria = System.nanoTime();
        long tiempoBinaria = (finBinaria - inicioBinaria);
        System.out.println("Resultado: " + halladoBinaria);
        System.out.println("Tiempo Búsqueda Binaria: " + tiempoBinaria / 1_000_000.0 + " ms");
    }

    public static boolean busquedaLineal(int[] S, int x) {
        for (int num : S) {
            if (num == x) {
                return true;
            }
        }
        return false;
    }
}
