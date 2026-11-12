import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class HamiltonianTSPBB {

    // Nombres de los vértices según la imagen
    private static final String[] CITIES = {"A", "B", "C", "D", "E", "F"};
    private static final int N = CITIES.length;
    private static final int INF = 999999;

    // Matriz de adyacencia (Pesos: 1 para aristas verdes, 2 para rojas)
    /*private static final int[][] DIST = {
            //  A    B    C    D    E    F
            {  0,   1,   2,   2,   1,   2 }, // A
            {  1,   0,   1,   1,   2,   1 }, // B
            {  2,   1,   0,   1,   2,   2 }, // C
            {  2,   1,   1,   0,   1,   1 }, // D
            {  1,   2,   2,   1,   0,   1 }, // E
            {  2,   1,   2,   1,   1,   0 }  // F
    };*/
    //Opción 1: Desconectar una ciudad por completo (Crear una isla)
    private static final int[][] DIST = {
            //  A      B      C      D      E      F
            {   0,     1,     2,     2,     1, 999999 }, // A (Sin salida a F)
            {   1,     0,     1,     1,     2, 999999 }, // B
            {   2,     1,     0,     1,     2, 999999 }, // C
            {   2,     1,     1,     0,     1, 999999 }, // D
            {   1,     2,     2,     1,     0, 999999 }, // E
            { 999999, 999999, 999999, 999999, 999999, 0 } // F (Aislado del resto)
    };
    //Opción 2: Romper la simetría creando dos componentes separadas (Cuellos de botella)
    /*private static final int[][] DIST = {
            //  A    B    C        D        E        F
            {  0,   1,   2,  999999, 999999, 999999 }, // A (Solo se conecta a B y C)
            {  1,   0,   1,  999999, 999999, 999999 }, // B
            {  2,   1,   0,  999999, 999999, 999999 }, // C
            { 999999, 999999, 999999,   0,   1,   1 }, // D (Solo se conecta a E y F)
            { 999999, 999999, 999999,   1,   0,   1 }, // E
            { 999999, 999999, 999999,   1,   1,   0 }  // F
    };*/

    // Estructura de nodo para el árbol de búsqueda LC-BB
    static class Node implements Comparable<Node> {
        List<Integer> path;      // Secuencia de vértices recorridos
        boolean[] visited;       // Estado de visita de los vértices
        int currentCost;         // Costo real acumulado
        int lowerBound;          // Cota inferior estimada
        int level;               // Nivel de profundidad (ciudades visitadas)

        public Node(List<Integer> parentPath, boolean[] parentVisited, int currentCost, int lowerBound, int level) {
            this.path = new ArrayList<>(parentPath);
            this.visited = parentVisited.clone();
            this.currentCost = currentCost;
            this.lowerBound = lowerBound;
            this.level = level;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.lowerBound, other.lowerBound);
        }
    }

    // Calcula la cota inferior sumando las dos aristas mínimas de cada vértice no visitado
    private static int calculateLowerBound(List<Integer> path, boolean[] visited) {
        int boundSum = 0;
        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                int min1 = INF, min2 = INF;
                for (int j = 0; j < N; j++) {
                    if (i != j) {
                        if (DIST[i][j] < min1) {
                            min2 = min1;
                            min1 = DIST[i][j];
                        } else if (DIST[i][j] < min2) {
                            min2 = DIST[i][j];
                        }
                    }
                }
                boundSum += (min1 + min2);
            }
        }
        return (int) Math.ceil(boundSum / 2.0);
    }

    public static void solve() {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Partimos desde el nodo 'A' (índice 0)
        List<Integer> startPath = new ArrayList<>();
        startPath.add(0);
        boolean[] startVisited = new boolean[N];
        startVisited[0] = true;

        int initialBound = calculateLowerBound(startPath, startVisited);
        Node root = new Node(startPath, startVisited, 0, initialBound, 1);
        pq.add(root);

        int bestCost = INF;
        List<Integer> bestPath = null;

        while (!pq.isEmpty()) {
            Node minNode = pq.poll();

            // Poda por cota
            if (minNode.lowerBound >= bestCost) {
                continue;
            }

            // Si visitamos todas las ciudades, cerramos el ciclo volviendo a 'A'
            if (minNode.level == N) {
                int lastNode = minNode.path.get(N - 1);
                int returnCost = DIST[lastNode][0];
                int totalCost = minNode.currentCost + returnCost;

                if (totalCost < bestCost) {
                    bestCost = totalCost;
                    bestPath = new ArrayList<>(minNode.path);
                    bestPath.add(0); // Completar ciclo
                }
                continue;
            }

            // Ramificación hacia ciudades no visitadas
            int u = minNode.path.get(minNode.path.size() - 1);

            for (int v = 0; v < N; v++) {
                if (!minNode.visited[v]) {
                    int newCost = minNode.currentCost + DIST[u][v];
                    if (newCost >= bestCost) continue;

                    List<Integer> newPath = new ArrayList<>(minNode.path);
                    newPath.add(v);

                    boolean[] newVisited = minNode.visited.clone();
                    newVisited[v] = true;

                    int estimatedBound = newCost + calculateLowerBound(newPath, newVisited);

                    if (estimatedBound < bestCost) {
                        pq.add(new Node(newPath, newVisited, newCost, estimatedBound, minNode.level + 1));
                    }
                }
            }
        }

        // Mostrar resultados de forma segura
        System.out.println("==================================================");
        System.out.println("     SOLUCIÓN ÓPTIMA TSP / CICLO HAMILTONIANO     ");
        System.out.println("==================================================");

        if (bestPath != null && bestCost < INF) {
            System.out.print("Camino de costo mínimo: ");
            for (int i = 0; i < bestPath.size(); i++) {
                System.out.print(CITIES[bestPath.get(i)] + (i < bestPath.size() - 1 ? " -> " : ""));
            }
            System.out.println("\nCosto total del ciclo: " + bestCost);
        } else {
            System.out.println("❌ El grafo NO contiene un Ciclo Hamiltoniano válido.");
        }
    }

    public static void main(String[] args) {
        solve();
    }
}