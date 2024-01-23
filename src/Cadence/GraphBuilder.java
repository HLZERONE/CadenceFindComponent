package Cadence;
import java.util.*;
import java.io.*;

public class GraphBuilder {

        public static Graph buildGraphFromFile(String fileName) throws IOException {
                List<Component> componentList = new ArrayList<>();
                List<Edge> edgeList = new ArrayList<>();
                Map<Integer, Component> componentMap = new HashMap<>();

                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                                String[] tokens = line.split(",");

                                if (tokens[0].equals("Component")) {
                                        // Parse component attributes
                                        int id = Integer.parseInt(tokens[1]);
                                        int resource = Integer.parseInt(tokens[2]);
                                        int density = Integer.parseInt(tokens[3]);
                                        Component component = new Component(id, resource, density);
                                        componentList.add(component);
                                        componentMap.put(id, component);
                                } else if (tokens[0].equals("Edge")) {
                                        // Parse edge attributes
                                        int id = Integer.parseInt(tokens[1]);
                                        int delay = Integer.parseInt(tokens[2]);
                                        int componentAId = Integer.parseInt(tokens[3]);
                                        int componentBId = Integer.parseInt(tokens[4]);
                                        Component componentA = componentMap.get(componentAId);
                                        Component componentB = componentMap.get(componentBId);
                                        Edge edge = new Edge(id, componentA, componentB, delay);
                                        edgeList.add(edge);
                                }
                        }
                }

                Component[] components = componentList.toArray(new Component[0]);
                Edge[] edges = edgeList.toArray(new Edge[0]);

                return new Graph(components, edges);
        }

        public static void main(String[] args) {
                try {
                        Graph graph = buildGraphFromFile("input.txt");
                        Graph.printGraph(graph);
                } catch (IOException e) {
                        System.out.println(e);
                }
        }
}
