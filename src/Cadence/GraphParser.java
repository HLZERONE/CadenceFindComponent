package Cadence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GraphParser {
        public static List<Graph> buildGraphFromFile(String fileName) throws IOException {

                Map<String, List<Component>> graphComponents = new HashMap<>();
                Map<String, List<Edge>> graphEdges = new HashMap<>();
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
                                graphComponents.computeIfAbsent(graphName, k -> new ArrayList<>()).add(nodeA);
                                graphComponents.computeIfAbsent(graphName, k -> new ArrayList<>()).add(nodeB);
                                graphEdges.computeIfAbsent(graphName, k -> new ArrayList<>())
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

        public static List<Graph> saveGraphAsFile(String fileName, List<Graph> graphs) throws IOException {
                
                return null;
        }

        public static void main(String[] args) {
                try {
                        List<Graph> graphs = buildGraphFromFile("EdgeCase1.csv");
                        List<Graph> graphsSolution = buildGraphFromFile("EdgeCase1Solution.csv");

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
