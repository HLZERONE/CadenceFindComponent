package Cadence;

import java.io.IOException;
import java.util.List;

public class TestGraph {
        public static void main(String[] args) {
            try {
                        
                        List<Graph> graphs = GraphParser.buildGraphFromFile("input\\EdgeCase1.csv");
                        CadenceSolution2 graphSolver = new CadenceSolution2();
                        
                        
                        Graph bigG = graphs.get(0);
                        Graph targetG = graphs.get(1);

                        System.out.println("Big Graph: ");
                        Graph.printGraph(bigG);
                        System.out.println("Target Graph: ");
                        Graph.printGraph(targetG);
                        
                        List<Graph> matcher = graphSolver.findAllGraph(bigG, targetG);
                        System.out.println("Total match graph number: " + matcher.size());
                        System.out.println("Runtime in nanoseconds: " + graphSolver.lastRunTime);
                        for (Graph g : matcher) {
                        		System.out.println("Component Number: " + g.components.length);
                                System.out.println("Edge Number: " + g.edges.length);
                                System.out.println("Match: " + targetG.equals(g));
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

                } catch (IOException e) {
                        System.out.println(e);
                }
        }
}
