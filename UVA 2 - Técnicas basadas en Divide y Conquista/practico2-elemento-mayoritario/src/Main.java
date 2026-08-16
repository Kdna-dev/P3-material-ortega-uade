import java.util.Arrays;
import java.util.Optional;

public class Main {

    // Método main con los casos de prueba solicitados
    public static void main(String[] args) {
        int[][] casosPrueba = {
                {1, 2, 1, 1, 3, 1, 1}, // Caso 1: Esperado = 1
                {1, 2, 3, 4, 5, 6},    // Caso 2: No existe
                {2, 2, 2, 4, 5, 6}     // Caso 3: No existe (3/6 no es > 3)
        };

        for (int i = 0; i < casosPrueba.length; i++) {
            int[] vector = casosPrueba[i];
            Optional<Integer> resultado = ElementoMayoritario.obtenerElementoMayoritario(vector);

            System.out.println("----------------------------------------");
            System.out.println("Caso " + (i + 1) + ": " + Arrays.toString(vector));
            if (resultado.isPresent()) {
                System.out.println("Resultado: Elemento Mayoritario = " + resultado.get());
            } else {
                System.out.println("Resultado: No existe elemento mayoritario");
            }
        }
        System.out.println("----------------------------------------");
    }
}
