import java.util.Arrays;
import java.util.Optional;

public class ElementoMayoritario {

    /**
     * Método recursivo que reduce el arreglo agrupando elementos en pares
     * según el algoritmo de Divide y Conquista.
     *
     * @param v arreglo sobre el cual buscar el candidato
     * @param inicio índice inicial
     * @param fin índice final (inclusivo)
     * @return Integer con el candidato a mayoritario, o null si no se encuentra
     */
    public static Integer buscarCandidato(int[] v, int inicio, int fin) {
        int n = fin - inicio + 1;

        // Caso base: sub-arreglo vacío o inválido
        if (n <= 0) {
            return null;
        }

        // Caso base: sub-arreglo de 1 solo elemento
        if (n == 1) {
            return v[inicio];
        }

        // Caso base: sub-arreglo de 2 elementos
        if (n == 2) {
            if (v[inicio] == v[fin]) {
                return v[inicio];
            } else {
                return null; // Si no son iguales, se descartan ambos
            }
        }

        // Reducción por pares en la siguiente etapa
        int[] reduccion = new int[n / 2];
        int k = 0;

        for (int i = inicio; i < fin; i += 2) {
            if (v[i] == v[i + 1]) {
                reduccion[k++] = v[i];
            }
        }

        // Llamada recursiva sobre la reducción con los pares que coincidieron
        Integer candidato = buscarCandidato(reduccion, 0, k - 1);

        // Si el tamaño del sub-arreglo original era impar
        if (n % 2 != 0) {
            int ultimoElemento = v[fin];

            // Si la reducción no entregó candidato, el último elemento podría ser candidato
            if (candidato == null) {
                return ultimoElemento;
            }

            // Si los dos elementos compiten, verificamos su frecuencia dentro del sub-arreglo actual
            int countCandidato = 0;
            int countUltimo = 0;

            for (int i = inicio; i <= fin; i++) {
                if (v[i] == candidato) {
                    countCandidato++;
                }
                if (v[i] == ultimoElemento) {
                    countUltimo++;
                }
            }

            if (countUltimo > countCandidato) {
                return ultimoElemento;
            }
        }

        return candidato;
    }

    /**
     * Método principal que obtiene el elemento mayoritario y verifica
     * su frecuencia real con una pasada lineal O(n).
     *
     * @param v Arreglo original
     * @return Optional<Integer> conteniendo el elemento mayoritario si existe.
     */
    public static Optional<Integer> obtenerElementoMayoritario(int[] v) {
        if (v == null || v.length == 0) {
            return Optional.empty();
        }

        // Para evitar efectos secundarios / side effects en el arreglo original
        int[] copia = Arrays.copyOf(v, v.length);

        // Pasada 1: Obtener candidato por Divide y Conquista
        Integer candidato = buscarCandidato(copia, 0, copia.length - 1);

        if (candidato == null) {
            return Optional.empty();
        }

        // Pasada 2: Verificación lineal O(n) sobre el arreglo original
        int contador = 0;
        for (int x : v) {
            if (x == candidato) {
                contador++;
            }
        }

        // Verificación de frecuencia estrictamente mayor a n / 2
        if (contador > v.length / 2) {
            return Optional.of(candidato);
        } else {
            return Optional.empty();
        }
    }


}