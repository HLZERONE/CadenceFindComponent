package Cadence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ErrorSave {

        public static void SaveErrorGraphToFile(int loop, int bigGComponentNum, int bigGEdgeNum,
                        int targetGComponentNum,
                        int targetGEdgeNum) {
                CadenceSolution2 graphSolver = new CadenceSolution2();
                double totalRuntime = 0;
                int errorCount = 0;

                // Use LocalDateTime and DateTimeFormatter to generate a timestamp
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd_HHmmss");
                LocalDateTime now = LocalDateTime.now();

                String formattedDateTime = now.format(formatter); // Example: "240307_152835"

                // Include the formatted date and time in the filename
                String errorFilename;

                for (int i = 0; i < loop; i++) {
                        errorFilename = "error\\error_graphs_test_" + formattedDateTime + "_" + errorCount + ".json";

                        Graph AGraph = GenerateRandom.generateRandomGraph(bigGComponentNum, bigGEdgeNum);
                        Graph Asmall = GenerateRandom.generateRandomSubgraph(AGraph, targetGComponentNum,
                                        targetGEdgeNum);

                        List<Graph> matcher = graphSolver.findAllGraph(AGraph, Asmall);
                        totalRuntime += graphSolver.getLastRunTime();
                        for (Graph g : matcher) {
                                if (!Asmall.equals(g)) {
                                        errorCount++;
                                        // Create graph to file
                                        GraphParserJSON.combineGraphsToJsonFile(AGraph, Asmall, errorFilename);
                                }
                        }
                }

                System.out.println("AVG RUNTIME(MS): " + totalRuntime / loop);
                System.out.println("Error Rate(%): " + (double) errorCount / loop * 100);

        }

        public static void main(String[] args) {
                SaveErrorGraphToFile(10000, 10000, 9999, 1000, 999);

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
