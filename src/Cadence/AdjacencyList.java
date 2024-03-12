package Cadence;
import java.util.HashMap;
public class AdjacencyList {
    public static HashMap<Integer, Integer>[] generateAdjacencyList(Graph graph) {
        int numOfComponents = graph.numComponents();
        HashMap<Integer, Integer>[] adjacencyList = new HashMap[numOfComponents];
        for (int i = 0; i < numOfComponents; i++){
            adjacencyList[i] = new HashMap<>();
        }

        for (Edge e : graph.getEdges()) {
            Component componentA = e.getComponentA();
            Component componentB = e.getComponentB();

            // Set the connection between components to 1 (indicating a connection)
            if (adjacencyList[componentA.id].containsKey(componentB.id) == false) {
                adjacencyList[componentA.id].put(componentB.id, e.delay);
                adjacencyList[componentB.id].put(componentA.id, e.delay);
            }
        }
        return adjacencyList;
    }

    public static void printAdjacencyList(HashMap<Integer, Integer>[] adjacencyList){
        for (int i = 0; i < adjacencyList.length; i++) {
            System.out.print("HashMap at index " + i + ": ");
            if (adjacencyList[i] == null) {
                System.out.println("null");
            } else {
                System.out.println(adjacencyList[i]);
            }
        }
    }
}
