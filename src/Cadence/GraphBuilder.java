package Cadence;

import java.util.*;
import java.io.*;

/**
 * @brief Responsible for buidling graphs from a file.
 * 
 *        This class provides a static method to parse the file and construct a list of
 *        graphs based on the contents of the file.
 */
public class GraphBuilder {

        /**
         * This method parses the file and constructs a list of graphs based on the
         * contents of the file.
         * 
         * @param fileName the name of the file
         * @return a list of graphs, index 0 is the system graph, index 1 is the query
         *         graph
         * @throws IOException This exception is thrown if the file is not found
         */
        public static List<Graph> buildGraphFromFile(String fileName) throws IOException {
                List<Graph> graphs = new ArrayList<>();
                List<Component> currentComponentList = new ArrayList<>();
                List<Edge> currentEdgeList = new ArrayList<>();

                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                        String line;
                        boolean isParsing = false;

                        while ((line = reader.readLine()) != null) {
                                line = line.trim();
                                if (line.equals("sub[") || line.equals("main[")) {
                                        currentComponentList = new ArrayList<>();
                                        currentEdgeList = new ArrayList<>();
                                        isParsing = true;
                                        continue;
                                } else if (line.equals("]")) {
                                        Graph graph = new Graph(currentComponentList.toArray(new Component[0]),
                                                        currentEdgeList.toArray(new Edge[0]));
                                        graphs.add(graph);
                                        isParsing = false;
                                        continue;
                                }

                                if (isParsing) {
                                        String[] tokens = line.split(",");
                                        if (tokens[0].equals("c")) {
                                                int id = Integer.parseInt(tokens[1]);
                                                int resource = Integer.parseInt(tokens[2]);
                                                int density = Integer.parseInt(tokens[3]);
                                                Component component = new Component(id, resource, density);
                                                currentComponentList.add(component);

                                        } else if (tokens[0].equals("e")) {
                                                int id = Integer.parseInt(tokens[1]);
                                                int componentAId = Integer.parseInt(tokens[2]);
                                                int componentBId = Integer.parseInt(tokens[3]);
                                                int delay = Integer.parseInt(tokens[4]);
                                                Component componentA = findComponentById(currentComponentList,
                                                                componentAId);
                                                Component componentB = findComponentById(currentComponentList,
                                                                componentBId);
                                                Edge edge = new Edge(id, componentA, componentB, delay);
                                                currentEdgeList.add(edge);
                                        }

                                }
                        }
                }
                return graphs;
        }

        /**
         * This method finds a component in a list of components based on the id of the
         * component.
         * 
         * @param components the list of components
         * @param id         the id of the component
         * @return the component with the given id, or null if no such component exists
         */
        private static Component findComponentById(List<Component> components, int id) {
                for (Component component : components) {
                        if (component.id == id) {
                                return component;
                        }
                }
                return null;
        }

        public static void main(String[] args) {
                try {
                        List<Graph> graphs = buildGraphFromFile("EdgeCase1.txt");
                        CadenceSolution graphSolver = new CadenceSolution();

                        // for (Graph graph : graphs) {
                        //         Graph.printGraph(graph);
                        //         System.out.println("----");
                        // }

                        // System.out.println(graphs);
                        // System.out.println(graphs.get(0).edges.length);

                        List<Graph> matcher = graphSolver.findAllGraph(graphs.get(0), graphs.get(1));
                        System.out.println(matcher.size());
                        for (Graph g : matcher) {
                                System.out.println(g.edges.length);
                                Graph.printGraph(g);
                                System.out.println("----");
                                Graph.printGraph(graphs.get(1));
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
