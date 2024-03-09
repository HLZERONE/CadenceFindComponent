package Cadence;

import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorSave {

        public static void SaveErrorGraphToFile(int loop, int bigGComponentNum, int bigGEdgeNum,
                        int targetGComponentNum,
                        int targetGEdgeNum) {
                CadenceSolution2 graphSolver = new CadenceSolution2();
                double totalRuntime = 0;
                int errorCount = 0;

                // Use LocalDateTime and DateTimeFormatter to generate a timestamp
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd_HHmmss");
                String formattedDateTime = now.format(formatter); // Example: "240307_152835"

                // Include the formatted date and time in the filename

                String errorFilename = "error\\error_graphs_test_" + formattedDateTime + ".csv";
                // Initialize BufferedWriter outside the loop to keep adding to the same file
                try (BufferedWriter errorWriter = new BufferedWriter(new FileWriter(errorFilename))) {
                        errorWriter.write(
                                        "Graph,EdgeID,EdgeDelay,NodeA_ID,NodeA_Resource,NodeA_Density,NodeB_ID,NodeB_Resource,NodeB_Density\n"); // Header

                        for (int i = 0; i < loop; i++) {
                                Graph AGraph = GenerateRandom.generateRandomGraph(bigGComponentNum, bigGEdgeNum);
                                Graph Asmall = GenerateRandom.generateRandomSubgraph(AGraph, targetGComponentNum,
                                                targetGEdgeNum);

                                List<Graph> matcher = graphSolver.findAllGraph(AGraph, Asmall);
                                totalRuntime += graphSolver.getLastRunTime();
                                for (Graph g : matcher) {
                                        if (!Asmall.equals(g)) {
                                                errorCount++;
                                                // Append the error graph details to the file
                                                appendGraphToFile(errorWriter, "system", AGraph, i);
                                                appendGraphToFile(errorWriter, "query", Asmall, i);
                                        }
                                }
                        }

                        System.out.println("AVG RUNTIME(MS): " + totalRuntime / loop);
                        System.out.println("Error Rate(%): " + (double) errorCount / loop * 100);

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        private static void appendGraphToFile(BufferedWriter writer, String graphName, Graph graph, int testId) throws IOException {
                for (Edge edge : graph.getEdges()) {
                        Component nodeA = edge.getComponentA();
                        Component nodeB = edge.getComponentB();
                        writer.write(String.format("%s,%d,%d,%d,%d,%d,%d,%d,%d\n",
                                        graphName,
                                        edge.getId(),
                                        edge.getDelay(),
                                        nodeA.id, nodeA.getResource(), nodeA.getDensity(),
                                        nodeB.id, nodeB.getResource(), nodeB.getDensity()));
                }
        }

        public static void main(String[] args) {
                SaveErrorGraphToFile(1000, 1000, 999, 10, 9);

                // CadenceSolution2 graphSolver = new CadenceSolution2();
                // Graph AGraph = GenerateRandom.generateRandomGraph(1000, 999);
                // Graph Asmall = GenerateRandom.generateRandomSubgraph(AGraph, 10, 9);

                // List<Graph> matcher = graphSolver.findAllGraph(AGraph, Asmall);
                // // Graph.printGraph(Asmall);
                // System.out.println("Number of Match: " + matcher.size());
                // System.out.println("Runtime in ms: " + graphSolver.getLastRunTime());
                // for (Graph g : matcher) {
                // System.out.println("isMatch: " + Asmall.equals(g));
                // // Graph.printGraph(g);
                // System.out.println("----");
                // }

                // //build small graph
                // System.out.println("Custom Graph");
                // Edge randomEdge = AGraph.getEdge(0).clone(); //select an edge to copy
                // Edge[] smallEdges = new Edge[] {randomEdge};
                // Component[] smallComponents = new Component[] {randomEdge.getComponentA(),
                // randomEdge.getComponentB()};
                // Graph smallGraph = new Graph(smallComponents, smallEdges);
                //
                // Graph.printGraph(smallGraph);
                // System.out.println("Number of Match: " + matcher.size());
                // System.out.println("Runtime in nanoseconds: " +
                // graphSolver.getLastRunTime());
                // //find the graph
                // matcher = graphSolver.findAllGraph(AGraph, smallGraph);
                // for(Graph g: matcher) {
                // System.out.println("isMatch: " + Asmall.equals(g));
                // Graph.printGraph(g);
                // System.out.println("----");
                // }

                // try not match graph
                // System.out.println("Unmatch Graph");
                // Graph BGraph = GenerateRandom.generateRandomGraph(10, 9);
                // List<Graph> ABmatcher = graphSolver.findAllGraph(AGraph, BGraph);
                // System.out.println("isMatch: "+ ABmatcher.size()); //should be zero
                // System.out.println("Runtime in nanoseconds: " +
                // graphSolver.getLastRunTime());
                // System.out.println("----");
                //

        }
}
