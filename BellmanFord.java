import java.util.Arrays;
class BellmanFord {
    int V, E;
    Edge[] edges;

    BellmanFord(int V, int E) {
        this.V = V;
        this.E = E;
        edges = new Edge[E];
    }

    void addEdge(int source, int destination, int weight, int edgeCount) {

    }

    void bellmanFord(int source) {
        int[] distance = new int[V];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[source] = 0;

        for (int i = 1; i < V; i++) {
            for (int j = 0; j < E; j++) {
                int u = edges[j].getId();
                Component a = edges[j].getComponentA();
                Component b = edges[j].getComponentB();
                if (distance[u] != Integer.MAX_VALUE && distance[u] + u < distance[i]) {
                    distance[u] = distance[u] + u;
                }
            }
        }

    }
}