package Cadence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONObject;

public class GraphParserJSON {
        public static Graph buildSystemGraph(String file) throws IOException {
                ArrayList<Component> componentList = new ArrayList<>();
                ArrayList<Edge> edgeList = new ArrayList<>();
                HashMap<Integer, Component> componentMap = new HashMap<>();

                HashSet<Integer> seenEdgeIds = new HashSet<>();

                String fileName = new String(Files.readAllBytes(Paths.get(file)));

                JSONObject graphJson = new JSONObject(fileName);
                JSONObject components = graphJson.getJSONObject("system").getJSONObject("nodes");

                // Parse components (nodes)
                for (String key : components.keySet()) {
                        JSONObject component = components.getJSONObject(key);
                        int id = Integer.parseInt(key);
                        int resource = component.getInt("resource");
                        int density = component.getInt("density");
                        Component newComponent = new Component(id, resource, density);
                        componentList.add(newComponent);
                        componentMap.put(id, newComponent);
                }

                for (String key : components.keySet()) {
                        JSONObject component = components.getJSONObject(key);
                        JSONObject edges = component.getJSONObject("edges");
                        int sourceComponentId = Integer.parseInt(key);

                        for (String edgeKey : edges.keySet()) {
                                int edgeId = Integer.parseInt(edgeKey);
                                if (seenEdgeIds.contains(edgeId)) {
                                        continue;
                                }
                                JSONObject edge = edges.getJSONObject(edgeKey);
                                int targetNodeId = edge.getInt("node_id");
                                int edgeDelay = edge.getInt("edge_delay");

                                Edge newEdge = new Edge(edgeId, componentMap.get(sourceComponentId),
                                                componentMap.get(targetNodeId), edgeDelay);
                                edgeList.add(newEdge);
                                seenEdgeIds.add(edgeId);
                        }
                }

                Component[] componentsArray = componentList.toArray(new Component[0]);
                Edge[] edgesArray = edgeList.toArray(new Edge[0]);

                return new Graph(componentsArray, edgesArray);

        }

        public static Graph buildQueryGraph(String file) throws IOException {
                ArrayList<Component> componentList = new ArrayList<>();
                ArrayList<Edge> edgeList = new ArrayList<>();
                HashMap<Integer, Component> componentMap = new HashMap<>();

                HashSet<Integer> seenEdgeIds = new HashSet<>();

                String fileName = new String(Files.readAllBytes(Paths.get(file)));

                JSONObject graphJson = new JSONObject(fileName);
                JSONObject components = graphJson.getJSONObject("query").getJSONObject("nodes");

                // Parse components (nodes)
                for (String key : components.keySet()) {
                        JSONObject component = components.getJSONObject(key);
                        int id = Integer.parseInt(key);
                        int resource = component.getInt("resource");
                        int density = component.getInt("density");
                        Component newComponent = new Component(id, resource, density);
                        componentList.add(newComponent);
                        componentMap.put(id, newComponent);
                }

                for (String key : components.keySet()) {
                        JSONObject component = components.getJSONObject(key);
                        JSONObject edges = component.getJSONObject("edges");
                        int sourceComponentId = Integer.parseInt(key);

                        for (String edgeKey : edges.keySet()) {
                                int edgeId = Integer.parseInt(edgeKey);
                                if (seenEdgeIds.contains(edgeId)) {
                                        continue;
                                }
                                JSONObject edge = edges.getJSONObject(edgeKey);
                                int targetNodeId = edge.getInt("node_id");
                                int edgeDelay = edge.getInt("edge_delay");

                                Edge newEdge = new Edge(edgeId, componentMap.get(sourceComponentId),
                                                componentMap.get(targetNodeId), edgeDelay);
                                edgeList.add(newEdge);
                                seenEdgeIds.add(edgeId);
                        }
                }

                Component[] componentsArray = componentList.toArray(new Component[0]);
                Edge[] edgesArray = edgeList.toArray(new Edge[0]);

                return new Graph(componentsArray, edgesArray);

        }

        public static JSONObject graphToJson(Graph graph) {
                JSONObject componentsJson = new JSONObject();

                for (Component c : graph.getComponents()) {
                        JSONObject componentJson = new JSONObject();
                        componentJson.put("resource", String.valueOf(c.getResource()));
                        componentJson.put("density", String.valueOf(c.getDensity()));

                        JSONObject edgesJson = new JSONObject();
                        for (Edge e : c.edges) {
                                JSONObject edgeJson = new JSONObject();
                                edgeJson.put("node_id", String.valueOf(e.getOtherComponent(c).id));
                                edgeJson.put("edge_delay", String.valueOf(e.getDelay()));
                                edgesJson.put(String.valueOf(e.getId()), edgeJson);
                        }

                        componentJson.put("edges", edgesJson);
                        componentsJson.put(String.valueOf(c.id), componentJson);
                }

                return componentsJson;
        }

        public static void combineGraphsToJsonFile(Graph systemGraph, Graph queryGraph, String fileName) {
                JSONObject fullGraphJson = new JSONObject();
                fullGraphJson.put("system", new JSONObject().put("nodes", graphToJson(systemGraph)));
                fullGraphJson.put("query", new JSONObject().put("nodes", graphToJson(queryGraph)));
                try {
                        Files.write(Paths.get(fileName), fullGraphJson.toString(4).getBytes());

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public static void main(String[] args) {

                Graph systemGraph;
                Graph queryGraph;
                try {
                        systemGraph = buildSystemGraph("input\\EdgeCase1.json");
                        queryGraph = buildQueryGraph("input\\EdgeCase1.json");
                        // Graph queryGraph = buildQueryGraph("input\\EdgeCase1.json");
                        // try {
                        // String content = new
                        // String(Files.readAllBytes(Paths.get("input\\EdgeCase1.json")));

                        // System.out.println(new JSONObject(content).getJSONObject("system"));
                        // } catch (Exception e) {
                        // e.printStackTrace();
                        // }
                        // for (Graph graph : graphsSolution) {
                        // Graph.printGraph(systemGraph);
                        // System.out.println("----");
                        // }
                        // for (Graph graph : graphs) {
                        // Graph.printGraph(graph);
                        // System.out.println("----");
                        // }

                        // System.out.println(graphs);
                        // System.out.println(graphs.get(0).edges.length);

                        CadenceSolution2 graphSolver = new CadenceSolution2();
                        List<Graph> matcher = graphSolver.findAllGraph(systemGraph, queryGraph);
                        System.out.format("There are %d result graphs.\n", matcher.size());
                        for (Graph g : matcher) {
                                // System.out.println(g.edges.length);
                                Graph.printGraph(g);
                                System.out.println("----");
                                // Graph.printGraph(graphs.get(1));
                        }

                        // combineGraphsToJsonFile(systemGraph, queryGraph, "output\\EdgeCase1.json");
                        // systemGraph = buildSystemGraph("output\\EdgeCase1.json");
                        // queryGraph = buildQueryGraph("output\\EdgeCase1.json");
                        // System.out.format("There are %d result graphs.\n", matcher.size());
                        // for (Graph g : matcher) {
                        //         // System.out.println(g.edges.length);
                        //         Graph.printGraph(g);
                        //         System.out.println("----");
                        //         // Graph.printGraph(graphs.get(1));
                        // }

                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

        }
}
