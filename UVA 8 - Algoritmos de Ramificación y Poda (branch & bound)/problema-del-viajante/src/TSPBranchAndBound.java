import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class TSPBranchAndBound {

    // Nombres de los nodos según la imagen
    private static final String[] CITIES = {"a", "b", "c", "d", "e", "f"};
    private static final int N = CITIES.length;
    private static final int INF = Integer.MAX_VALUE;

    // Matriz de distancias/costos del grafo presentado
    private static final int[][] DIST = {
            //a  b  c  d  e  f
            { 0, 4, 1, 5, 1, 4 }, // a
            { 4, 0, 5, 1, 2, 3 }, // b
            { 1, 5, 0, 3, 2, 5 }, // c
            { 5, 1, 3, 0, 3, 3 }, // d
            { 1, 2, 2, 3, 0, 2 }, // e
            { 4, 3, 5, 3, 2, 0 }  // f
    };

    // Estructura de cada nodo en el árbol de búsqueda
    static class Node implements Comparable<Node> {
        List<Integer> path;      // Lista con la secuencia de vértices visitados
        boolean[] visited;       // Marcas de vértices visitados
        int currentCost;         // Costo acumulado hasta el momento
        int lowerBound;          // Cota inferior estimada para este subárbol
        int level;               // Cantidad de nodos visitados en el camino

        public Node(List<Integer> parentPath, boolean[] parentVisited, int currentCost, int lowerBound, int level) {
            this.path = new ArrayList<>(parentPath);
            this.visited = parentVisited.clone();
            this.currentCost = currentCost;
            this.lowerBound = lowerBound;
            this.level = level;
        }

        // Criterio de orden para LC-BB (Primero el de menor cota/costo)
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.lowerBound, other.lowerBound);
        }
    }

    // Calcula el costo de las dos aristas incidentes de menor peso para cada nodo no visitado
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
        // PriorityQueue actúa como la estructura principal para la estrategia LC-BB
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Partimos del origen: vértice 'a' (índice 0)
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
            // Extraer el nodo con menor costo/cota inferior estimado (Least Cost)
            Node minNode = pq.poll();

            // PODA 1: Si la cota actual supera o iguala la mejor solución global, descartamos
            if (minNode.lowerBound >= bestCost) {
                continue;
            }

            // Caso base: Se han visitado todos los vértices
            if (minNode.level == N) {
                int lastNode = minNode.path.get(N - 1);
                int returnCost = DIST[lastNode][0]; // Retorno al nodo origen ('a')
                int totalCost = minNode.currentCost + returnCost;

                if (totalCost < bestCost) {
                    bestCost = totalCost;
                    bestPath = new ArrayList<>(minNode.path);
                    bestPath.add(0); // Cerrar el ciclo
                }
                continue;
            }

            // RAMIFICACIÓN: Probar avanzar hacia cada ciudad no visitada
            int u = minNode.path.get(minNode.path.size() - 1);

            for (int v = 0; v < N; v++) {
                if (!minNode.visited[v]) {
                    int newCost = minNode.currentCost + DIST[u][v];

                    // PODA 2: Si el costo actual acumulado ya supera la mejor solución, no ramificamos
                    if (newCost >= bestCost) {
                        continue;
                    }

                    List<Integer> newPath = new ArrayList<>(minNode.path);
                    newPath.add(v);

                    boolean[] newVisited = minNode.visited.clone();
                    newVisited[v] = true;

                    int estimatedRemainingBound = calculateLowerBound(newPath, newVisited);
                    int estimatedTotalBound = newCost + estimatedRemainingBound;

                    // PODA 3: Si la cota total estimada supera o iguala al óptimo incumbente, no se añade a la cola
                    if (estimatedTotalBound < bestCost) {
                        pq.add(new Node(newPath, newVisited, newCost, estimatedTotalBound, minNode.level + 1));
                    }
                }
            }
        }

        // Salida de resultados
        System.out.println("==================================================");
        System.out.println("    SOLUCIÓN ÓPTIMA TSP (BRANCH AND BOUND - LC)   ");
        System.out.println("==================================================");
        System.out.print("Ciclo Hamiltoniano de mínimo costo: ");
        for (int i = 0; i < bestPath.size(); i++) {
            System.out.print(CITIES[bestPath.get(i)] + (i < bestPath.size() - 1 ? " -> " : ""));
        }
        System.out.println("\nCosto total del recorrido: " + bestCost);
    }

    public static void main(String[] args) {
        solve();
    }
}
