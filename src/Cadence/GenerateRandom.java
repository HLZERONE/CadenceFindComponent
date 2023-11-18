package Cadence;
import java.util.*;

class Result {
    // helper class to encapsulate a Graph object and a 2D int array to represent its connections
    // the 2D int array will be helpful in visualizing the graph in GraphVisualization.java
    private Graph graph;
    private int[][] connection2DArray;
    public Result(Graph g, int[][] a){
        this.graph = g;
        this.connection2DArray = a;
    }

    public Graph getGraph() {
        return graph;
    }

    public int[][] getConnection2DArray() {
        return connection2DArray;
    }

}
public class GenerateRandom {
    // For a connected graph, the number of edges depends on the number of components
    // the number of edges should be within the range of n-1 to n(n-1)/2, where n is the number of components
    // example:
    // Result r = GenerateRandom.generateRandomGraph(5, 5);
    // Graph g = r.getGraph();
    // int[][] c = r.getConnection2DArray();
    public static Result generateRandomGraph(int numOfComponents, int numOfEdges){
        try {
            if((numOfEdges > numOfComponents*(numOfComponents-1)/2) || numOfEdges < numOfComponents-1) {
                throw new Exception("the number of edges depends on the number of vertices \n" +
                        " should be within the range of [" + (numOfComponents-1) + ", " +
                        numOfComponents*(numOfComponents-1)/2 + "]");
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

        int[][] connection2DArray = new int[numOfComponents][numOfComponents];
        for (int i = 0; i < numOfComponents; i++) {
            for (int j = 0; j < numOfComponents; j++) {
                if(i == j){
                    connection2DArray[i][j] = 1;
                }
                else {
                    connection2DArray[i][j] = 0;
                }
            }
        }

        Component[] components = new Component[numOfComponents];
        Edge[] edges = new Edge[numOfEdges];
        Random random = new Random();

        for (int i = 0; i < numOfComponents; i++){
            int randomResource = random.nextInt(8) + 1;
            int randomDensity = random.nextInt(16) + 1;
            components[i] = new Component(i, randomResource, randomDensity);
        }

        int currentNumOfEdges = 0;
        int[] edgeDelays = {10, 100, 1000};
        for (int i = 0; i < components.length-1; i++){
            int randomIndex = random.nextInt(edgeDelays.length);
            int randomDelay = edgeDelays[randomIndex];

            Edge randomEdge = new Edge(currentNumOfEdges, components[i], components[i+1], randomDelay);
            edges[currentNumOfEdges] = randomEdge;
            connection2DArray[i][i+1] = 1;
            connection2DArray[i+1][i] = 1;

            currentNumOfEdges += 1;
        }

        while(currentNumOfEdges != numOfEdges){
            int randomComponentIndex = random.nextInt(numOfComponents);
            int randomIndex = random.nextInt(edgeDelays.length);
            int randomDelay = edgeDelays[randomIndex];

            int unconnectedComponentA = 0;
            int unconnectedComponentB = 0;
            outerLoop:
            for (int i = randomComponentIndex; i < randomComponentIndex + connection2DArray.length; i++) {
                int index = i % connection2DArray.length;
                for (int j = 0; j < connection2DArray[index].length; j++) {
                    if (connection2DArray[index][j] == 0) {
                        unconnectedComponentA = index;
                        unconnectedComponentB = j;
                        connection2DArray[index][j] = 1;
                        connection2DArray[j][index] = 1;
                        break outerLoop;
                    }
                }
            }

            Edge randomEdge = new Edge(currentNumOfEdges, components[unconnectedComponentA], components[unconnectedComponentB], randomDelay);
            edges[currentNumOfEdges] = randomEdge;
            currentNumOfEdges += 1;

        }

        Graph returnGraph = new Graph(components, edges);
        return new Result(returnGraph, connection2DArray);
    }

    //Warning!!! incomplete generateRandomSubgraph
    public static Graph generateRandomSubgraph(Graph systemGraph, boolean[][] connections, int componentNumber){
        try {
            if(componentNumber > systemGraph.numComponents()) {
                throw new Exception("too many components, should be less than: " + systemGraph.numEdges());
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

        Component[] components = new Component[componentNumber];
        Edge[] edges = new Edge[systemGraph.numEdges()];

        HashSet<Integer> visitedComponents = new HashSet<>();
        HashSet<Integer> visitedEdges = new HashSet<>();

        Random random = new Random();
        int currentComponentIndex = random.nextInt(systemGraph.numComponents());
        Component currentComponent = systemGraph.getComponent(currentComponentIndex);
        visitedComponents.add(currentComponent.id);

        int currentSubgraphComponents = 0;
        components[currentSubgraphComponents] = new Component(currentSubgraphComponents, currentComponent.resource, currentComponent.density);
        currentSubgraphComponents += 1;

        while (currentSubgraphComponents != componentNumber){
            int randomEdgeIndex = random.nextInt(systemGraph.components[currentComponentIndex].edges.size());
            Edge randomEdge = systemGraph.components[currentComponentIndex].edges.get(randomEdgeIndex);
            Component nextComponent = randomEdge.getOtherComponent(currentComponent);


            currentSubgraphComponents += 1;

        }

        return null;
    }

    public static void main(String[] args) {
        // generates a fully connected graph with 5 vertices
        Result r = generateRandomGraph(5, 9);
        Graph g = r.getGraph();
        System.out.println(g.components.length);
        for(int i = 0; i < g.components.length; i++){
            List<Edge> e = g.components[i].edges;
            System.out.println(e);
        }
        System.out.println(g.edges.length);
        for (int i = 0; i < r.getConnection2DArray().length; i++) {
            for (int j = 0; j < r.getConnection2DArray()[i].length; j++) {
                System.out.print(r.getConnection2DArray()[i][j] + " ");
            }
            System.out.println();
        }


    }
}
