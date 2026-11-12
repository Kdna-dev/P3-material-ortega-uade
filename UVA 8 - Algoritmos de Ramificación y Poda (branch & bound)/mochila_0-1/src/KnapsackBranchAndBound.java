import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KnapsackBranchAndBound {

    // Estructura para representar un ítem con su valor, peso y índice original
    static class Item implements Comparable<Item> {
        int id;
        int value;
        int weight;
        double ratio;

        public Item(int id, int value, int weight) {
            this.id = id;
            this.value = value;
            this.weight = weight;
            this.ratio = (double) value / weight;
        }

        // Ordenar de mayor a menor según la relación valor/peso (para hacer las cotas óptimas)
        @Override
        public int compareTo(Item other) {
            return Double.compare(other.ratio, this.ratio);
        }
    }

    static class Node {
        int level;          // Nivel en el árbol (índice del objeto actual)
        int profit;         // Beneficio acumulado real
        int weight;         // Peso acumulado real
        double bound;       // Cota superior estimada (Upper Bound)
        List<Integer> path; // Lista de índices de los objetos seleccionados

        public Node(int level, int profit, int weight, double bound, List<Integer> path) {
            this.level = level;
            this.profit = profit;
            this.weight = weight;
            this.bound = bound;
            this.path = new ArrayList<>(path);
        }
    }

    // Calcula la cota superior (Upper Bound) usando la mochila fraccionaria
    private static double calculateBound(Node u, int n, int capacity, Item[] items) {
        if (u.weight >= capacity) {
            return 0;
        }

        double profitBound = u.profit;
        int j = u.level + 1;
        int totalWeight = u.weight;

        // Rellenar con los siguientes elementos completos mientras quepan
        while (j < n && totalWeight + items[j].weight <= capacity) {
            totalWeight += items[j].weight;
            profitBound += items[j].value;
            j++;
        }

        // Si queda espacio, incluir la fracción del siguiente elemento
        if (j < n) {
            profitBound += (capacity - totalWeight) * items[j].ratio;
        }

        return profitBound;
    }

    public static void solve(int[] values, int[] weights, int capacity) {
        int n = values.length;
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(i + 1, values[i], weights[i]);
        }

        // Ordenar los ítems por ratio de valor/peso descendente
        Arrays.sort(items);

        // Cola basada en prioridad por cota superior (Maximum Bound First)
        java.util.PriorityQueue<Node> pq = new java.util.PriorityQueue<>(
                (a, b) -> Double.compare(b.bound, a.bound)
        );

        List<Integer> emptyPath = new ArrayList<>();
        Node u = new Node(-1, 0, 0, 0, emptyPath);
        u.bound = calculateBound(u, n, capacity, items);
        pq.add(u);

        int maxProfit = 0;
        List<Integer> bestPath = new ArrayList<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // Si la cota del nodo es menor que la mejor ganancia encontrada, se poda
            if (current.bound > maxProfit) {
                int nextLevel = current.level + 1;

                if (nextLevel < n) {
                    // RAMA 1: Incluir el siguiente objeto (si no excede la capacidad)
                    if (current.weight + items[nextLevel].weight <= capacity) {
                        int p = current.profit + items[nextLevel].value;
                        int w = current.weight + items[nextLevel].weight;
                        List<Integer> newPath = new ArrayList<>(current.path);
                        newPath.add(items[nextLevel].id);

                        Node withItem = new Node(nextLevel, p, w, 0, newPath);
                        withItem.bound = calculateBound(withItem, n, capacity, items);

                        if (p > maxProfit) {
                            maxProfit = p;
                            bestPath = newPath;
                        }

                        if (withItem.bound > maxProfit) {
                            pq.add(withItem);
                        }
                    }

                    // RAMA 2: NO incluir el siguiente objeto
                    Node withoutItem = new Node(nextLevel, current.profit, current.weight, 0, current.path);
                    withoutItem.bound = calculateBound(withoutItem, n, capacity, items);

                    if (withoutItem.bound > maxProfit) {
                        pq.add(withoutItem);
                    }
                }
            }
        }

        // Mostrar resultados
        System.out.println("==================================================");
        System.out.println("     SOLUCIÓN ÓPTIMA MOCHILA 0-1 (BRANCH & BOUND) ");
        System.out.println("==================================================");
        System.out.println("Valor / Beneficio Máximo: " + maxProfit);
        System.out.print("Objetos seleccionados (IDs originales): ");
        for (int id : bestPath) {
            System.out.print("Obj #" + id + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] values = {10, 10, 12, 18};
        int[] weights = {2, 4, 6, 9};
        int capacity = 15;

        solve(values, weights, capacity);
    }
}
