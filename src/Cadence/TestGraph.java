package Cadence;

import java.io.IOException;
import java.util.List;

public class TestGraph {
        public static void main(String[] args) {
                                try {
                        List<Graph> graphs = GraphBuilder.buildGraphFromFile("EdgeCase1.txt");
                        CadenceSolution2 graphSolver = new CadenceSolution2();

                        for (Graph graph : graphs) {
                                Graph.printGraph(graph);
                                System.out.println("----");
                        }

                        // System.out.println(graphs);
                        // System.out.println(graphs.get(0).edges.length);

                        List<Graph> matcher = graphSolver.findAllGraph(graphs.get(0), graphs.get(1));
                        System.out.println("Total match graph number: " + matcher.size());
                        for (Graph g : matcher) {
                        		System.out.println(g.components.length);
                                System.out.println(g.edges.length);
                                Graph.printGraph(g);
                                System.out.println("----");
                                //Graph.printGraph(graphs.get(1));
                        }

                        // Get2Dconnection get = new Get2Dconnection();

                        // Graph g = GenerateRandom.generateRandomGraph(5, 4);
                        // System.out.println("number of components: " + g.components.length);
                        // for (int i = 0; i < g.components.length; i++) {
                        // List<Edge> e = g.components[i].edges;
                        // System.out.println(e);
                        // }
                        // System.out.println("number of edges: " + g.edges.length);
                        // int[][] r = get.getConnection(g);
                        // for (int i = 0; i < r.length; i++) {
                        // for (int j = 0; j < r[i].length; j++) {
                        // System.out.print(r[i][j] + " ");
                        // }
                        // System.out.println();
                        // }

                        // System.out.println();

                        // int[][] testConnection = get.getConnection(g);
                        // for (int i = 0; i < testConnection.length; i++) {
                        // for (int j = 0; j < testConnection[i].length; j++) {
                        // System.out.print(testConnection[i][j] + " ");
                        // }
                        // System.out.println();
                        // }

                        // System.out.println("test subgraph generation");
                        // int[][] array = get.getConnection(GenerateRandom.generateRandomSubgraph(g, 3,
                        // 2));
                        // for (int[] row : array) {
                        // for (int element : row) {
                        // System.out.print(element + " ");
                        // }
                        // System.out.println();
                        // }

                } catch (IOException e) {
                        System.out.println(e);
                }
        }
}
