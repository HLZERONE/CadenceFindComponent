package Cadence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GraphParser {
        public static List<Graph> buildGraphFromFile(String fileName) throws IOException {

                Map<String, Set<Component>> graphComponents = new HashMap<>();
                Map<String, Set<Edge>> graphEdges = new HashMap<>();
                Map<Integer, Component> componentCache = new HashMap<>();

                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                        String line = reader.readLine();
                        while ((line = reader.readLine()) != null) {
                                String[] tokens = line.split(",");
                                String graphName = tokens[0];
                                int edgeId = Integer.parseInt(tokens[1]);
                                int edgeDelay = Integer.parseInt(tokens[2]);

                                // Component A
                                int nodeAId = Integer.parseInt(tokens[3]);
                                Component nodeA = componentCache.computeIfAbsent(nodeAId, id -> new Component(nodeAId,
                                                Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5])));

                                // Component B
                                int nodeBId = Integer.parseInt(tokens[6]);
                                Component nodeB = componentCache.computeIfAbsent(nodeBId, id -> new Component(nodeBId,
                                                Integer.parseInt(tokens[7]), Integer.parseInt(tokens[8])));

                                // Add components and edges to the graph
                                graphComponents.computeIfAbsent(graphName, k -> new HashSet<>()).add(nodeA);
                                graphComponents.computeIfAbsent(graphName, k -> new HashSet<>()).add(nodeB);
                                graphEdges.computeIfAbsent(graphName, k -> new HashSet<>())
                                                .add(new Edge(edgeId, nodeA, nodeB, edgeDelay));
                        }
                }

                List<Graph> graphs = new ArrayList<>();
                graphEdges.forEach((graphName, edgeList) -> {
                        Set<Component> uniqueComponents = new HashSet<>(graphComponents.get(graphName));
                        Graph graph = new Graph(uniqueComponents.toArray(new Component[0]),
                                        edgeList.toArray(new Edge[0]));
                        graphs.add(graph);
                });

                return graphs;
        }

        public static void saveGraphAsFile(String fileName, List<Graph> graphs) throws IOException {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                        // Write the header line
                        writer.write("Graph,EdgeID,EdgeDelay,NodeA_ID,NodeA_Resource,NodeA_Density,NodeB_ID,NodeB_Resource,NodeB_Density\n");

                        // Since graph names are not directly available, we'll use an index or a
                        // placeholder
                        // This assumes each Graph object represents a different graph or a part of a
                        // larger graph structure
                        int graphIndex = 1; // Starting index for graph naming

                        for (Graph graph : graphs) {
                                // You might want to generate or assign meaningful names based on your
                                // application logic
                                String graphName = "graph" + graphIndex++; // Increment for each graph

                                for (Edge edge : graph.getEdges()) {
                                        Component nodeA = edge.getComponentA();
                                        Component nodeB = edge.getComponentB();

                                        // Prepare the line to write based on the edge and its connected components
                                        String line = String.format("%s,%d,%d,%d,%d,%d,%d,%d,%d\n",
                                                        graphName,
                                                        edge.getId(),
                                                        edge.getDelay(),
                                                        nodeA.id, nodeA.getResource(), nodeA.getDensity(),
                                                        nodeB.id, nodeB.getResource(), nodeB.getDensity());

                                        // Write the line to the file
                                        writer.write(line);
                                }
                        }
                }
        }

        public static void main(String[] args) {
                try {
                        List<Graph> graphs = buildGraphFromFile("input\\EdgeCase1.csv");
                        List<Graph> graphsSolution = buildGraphFromFile("input\\EdgeCase1Solution.csv");

                        for (Graph graph : graphsSolution) {
                                Graph.printGraph(graph);
                                System.out.println("----");
                        }
                        for (Graph graph : graphs) {
                                Graph.printGraph(graph);
                                System.out.println("----");
                        }

                        // System.out.println(graphs);
                        // System.out.println(graphs.get(0).edges.length);

                        List<Graph> matcher = CadenceSolution.findAllGraph(graphs.get(0), graphs.get(1));
                        System.out.format("There are %d result graphs.\n", matcher.size());
                        for (Graph g : matcher) {
                                // System.out.println(g.edges.length);
                                Graph.printGraph(g);
                                System.out.println("----");
                                // Graph.printGraph(graphs.get(1));
                        }

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
