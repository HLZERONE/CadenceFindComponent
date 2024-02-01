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

                                if (tokens[0].equals("c")) {
                                        // Parse component attributes
                                        int id = Integer.parseInt(tokens[1]);
                                        int resource = Integer.parseInt(tokens[2]);
                                        int density = Integer.parseInt(tokens[3]);
                                        Component component = new Component(id, resource, density);
                                        componentList.add(component);
                                        componentMap.put(id, component);
                                } else if (tokens[0].equals("e")) {
                                        // Parse edge attributes
                                        int id = Integer.parseInt(tokens[1]);
                                        int componentAId = Integer.parseInt(tokens[2]);
                                        int componentBId = Integer.parseInt(tokens[3]);
                                        int delay = Integer.parseInt(tokens[4]);
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
                		CadenceSolution2 graphSolver = new CadenceSolution2();
                        Graph mainGraph = buildGraphFromFile("mainGraph.txt");
                        Graph subGraph = buildGraphFromFile("subGraph.txt");

                        System.out.println(subGraph.edges.length);

                        List<Graph> matcher = graphSolver.findAllGraph(mainGraph, subGraph);
                    	System.out.println(matcher.size());
                    	for(Graph g: matcher) {
                    		System.out.println(g.edges.length);
                    		Graph.printGraph(g);
                    		
                    		System.out.println("----");
                    	}
                    	//Graph.printGraph(subGraph);
                        
                        // Get2Dconnection get = new Get2Dconnection();

                        // Graph g = GenerateRandom.generateRandomGraph(5, 4);
                        // System.out.println("number of components: " + g.components.length);
                        // for (int i = 0; i < g.components.length; i++) {
                        //         List<Edge> e = g.components[i].edges;
                        //         System.out.println(e);
                        // }
                        // System.out.println("number of edges: " + g.edges.length);
                        // int[][] r = get.getConnection(g);
                        // for (int i = 0; i < r.length; i++) {
                        //         for (int j = 0; j < r[i].length; j++) {
                        //                 System.out.print(r[i][j] + " ");
                        //         }
                        //         System.out.println();
                        // }

                        // System.out.println();

                        // int[][] testConnection = get.getConnection(g);
                        // for (int i = 0; i < testConnection.length; i++) {
                        //         for (int j = 0; j < testConnection[i].length; j++) {
                        //                 System.out.print(testConnection[i][j] + " ");
                        //         }
                        //         System.out.println();
                        // }

                        // System.out.println("test subgraph generation");
                        // int[][] array = get.getConnection(GenerateRandom.generateRandomSubgraph(g, 3, 2));
                        // for (int[] row : array) {
                        //         for (int element : row) {
                        //                 System.out.print(element + " ");
                        //         }
                        //         System.out.println();
                        // }

                } catch (IOException e) {
                        System.out.println(e);
                }

        }
}
