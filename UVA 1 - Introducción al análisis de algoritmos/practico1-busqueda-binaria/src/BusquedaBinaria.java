public class BusquedaBinaria {
    private static int nroOperacion = 0;

    /**
     * Búsqueda binaria recursiva aplicando Divide y Conquista.
     */
    public static boolean buscar(int[] S, int x, int inicio, int fin) {
        nroOperacion++;
        System.out.println("Nro operación: "+nroOperacion);
        // Caso base 1: El rango es inválido (el elemento no existe)
        if (inicio > fin) {
            return false;
        }

        // Se calcula el punto medio (evitando desbordamiento de int)
        int mitad = inicio + (fin - inicio) / 2;

        // Caso base 2: Elemento encontrado en la mitad
        if (S[mitad] == x) {
            return true;
        }

        // Subproblemas
        if (x > S[mitad]) {
            // Buscar en la mitad derecha: (mitad + 1) a fin
            return buscar(S, x, mitad + 1, fin);
        } else {
            // Buscar en la mitad izquierda: inicio a (mitad - 1)
            return buscar(S, x, inicio, mitad - 1);
        }
    }
}
