package Cadence;
import javax.swing.*;
import java.util.*;


public class GenerateRandom {
    // For a connected graph, the number of edges depends on the number of components
    // the number of edges should be within the range of n-1 to n(n-1)/2, where n is the number of components
    // the Result class in this java file is useless now, made separate function to capture 2D connections
    // example:
    // Graph g = GenerateRandom.generateRandomGraph(5, 4);
    // - generating a random graph with 5 vertices and 4 edges
    public static Graph generateRandomGraph(int numOfComponents, int numOfEdges){

        if((numOfEdges > numOfComponents*(numOfComponents-1)/2) || numOfEdges < numOfComponents-1) {
            System.out.println("the number of edges depends on the number of vertices \n" +
                    " should be within the range of [" + (numOfComponents-1) + ", " +
                    numOfComponents + "*" + (numOfComponents-1) + "/2 ]");
        }

        Component[] components = new Component[numOfComponents];
        Edge[] edges = new Edge[numOfEdges];

        HashMap<Integer, Integer>[] connectionInfo = new HashMap[numOfComponents];
        for (int i = 0; i < numOfComponents; i++){
            connectionInfo[i] = new HashMap<>();
        }

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

            connectionInfo[i].put(i+1, randomDelay);
            connectionInfo[i+1].put(i, randomDelay);

            currentNumOfEdges += 1;
        }

        outerloop:
        for (int j = 0; j < numOfComponents; j++){
            for(int i = 0; i < numOfComponents; i++){
                if (currentNumOfEdges == numOfEdges){
                    break  outerloop;
                }
                if (j != i && connectionInfo[j].containsKey(i) == false) {
                    int randomIndex = random.nextInt(edgeDelays.length);
                    int randomDelay = edgeDelays[randomIndex];
                    Edge randomEdge = new Edge(currentNumOfEdges, components[j], components[i], randomDelay);
                    edges[currentNumOfEdges] = randomEdge;

                    connectionInfo[j].put(i, randomDelay);
                    connectionInfo[i].put(j, randomDelay);
                    currentNumOfEdges += 1;
                }
            }
        }

        return new Graph(components, edges);
    }

    public static Graph generateRandomSubgraph(Graph systemGraph, int componentNumber, int edgeNumber){
        //tries its best to meet the specified number of components and edges in the argument,
        //but if the random starting vertex fails to locate such a subgraph, it will return the closest subgraph.
        //example:
        // Graph bigGraph = GenerateRandom.generateRandomGraph(5, 4);
        // Graph subGraph = GenerateRandom.generateRandomSubgraph(bigGraph, 3, 2)

        if(componentNumber > systemGraph.numComponents()) {
            System.out.println("too many components, should be less than or equal to: " + systemGraph.numComponents());
        }
        if(edgeNumber > systemGraph.numEdges()){
            System.out.println("too many edges, should be less than or equal to: " + systemGraph.numEdges());
        }
        if(edgeNumber < componentNumber - 1){
            System.out.println("too little edges, should be at least " + (componentNumber - 1));
        }


        ArrayList<Component> components = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();

        List<Edge> currentNeighborEdges = new ArrayList<>();

        HashSet<Integer> visitedComponents = new HashSet<>();

        Random random = new Random();
        int currentComponentIndex = random.nextInt(systemGraph.numComponents());
        Component currentSysComponent = systemGraph.getComponent(currentComponentIndex);

        visitedComponents.add(currentSysComponent.id);
        int currentSubgraphComponents = 0;
        int currentSubgraphEdges = 0;

        Component currentSubComponent = new Component(currentSubgraphComponents, currentSysComponent.resource, currentSysComponent.density);
        components.add(currentSubComponent);
        currentSubgraphComponents += 1;

        currentNeighborEdges = currentSysComponent.edges;

        outerloop:
        while (currentSubgraphComponents != componentNumber && currentNeighborEdges.isEmpty() == false){
            Edge randomEdge = null;
            for(Edge e : currentNeighborEdges){
                if (visitedComponents.contains(e.getOtherComponent(currentSysComponent).id) == false){
                    randomEdge = e;
                    break;
                }
            }

            if(randomEdge == null){
                break outerloop;
            }

            visitedComponents.add(randomEdge.getOtherComponent(currentSysComponent).id);
            Component newSubComponent = new Component(currentSubgraphComponents, randomEdge.getOtherComponent(currentSysComponent).resource, randomEdge.getOtherComponent(currentSysComponent).density);
            components.add(newSubComponent);


            Edge newSubEdge = new Edge(currentSubgraphEdges, currentSubComponent, newSubComponent, randomEdge.getDelay());
            edges.add(newSubEdge);
            currentSubgraphComponents += 1;
            currentSubgraphEdges += 1;

            currentSubComponent = newSubComponent;
            currentSysComponent = randomEdge.getOtherComponent(currentSubComponent);
            currentNeighborEdges = currentSysComponent.edges;

        }

        Component[] c = components.toArray(new Component[0]);
        Edge[] e = edges.toArray(new Edge[0]);
        return new Graph(c, e);
    }

    public static void main(String[] args) {
        // generates a fully connected graph with 5 vertices

        Graph sysG = GenerateRandom.generateRandomGraph(200000, 900000);
        System.out.println("number of components: " + sysG.components.length);
        System.out.println("number of connections: " + sysG.edges.length);
        HashMap<Integer, Integer>[] aL = AdjacencyList.generateAdjacencyList(sysG);

        Graph subG = GenerateRandom.generateRandomSubgraph(sysG, 10, 10);
        System.out.println("number of components: " + subG.components.length);
        System.out.println("number of connections: " + subG.edges.length);
        HashMap<Integer, Integer>[] alSub = AdjacencyList.generateAdjacencyList(subG);

        //SwingUtilities.invokeLater(() -> new GraphVisualizationBig(gg, subG));
        /*
        SwingUtilities.invokeLater(() -> new GraphVisualizationSmall(twoD, gg, subG));
        SwingUtilities.invokeLater(() -> new GraphVisualizationSmall(subTwoD, subG, new Graph(new Component[0], new Edge[0])));

         */
        /*
        for(int i = 0; i < g.components.length; i++){
            List<Edge> e = g.components[i].edges;
            System.out.println(e);
        }
         */


        /*
        Component[] components = new Component[100000];
        Edge[] edges = new Edge[99999];
        for(int i = 0; i < 100000; i++){
            Component c = new Component(i, 2, 3);
            components[i] = c;
        }
        int cNum = 0;
        for(int j = 0; j < 99999; j++){
            Edge e = new Edge(j, components[cNum], components[cNum+1], 10);
            edges[j] = e;
            cNum = cNum + 1;
        }
        Graph g = new Graph(components, edges);
        System.out.println("number of edges: " + g.edges.length);

         */
        /*
        int[][] r = get.getConnection(g);
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[i].length; j++) {
                System.out.print(r[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();

        int[][] testConnection = get.getConnection(g);
        for (int i = 0; i < testConnection.length; i++) {
            for (int j = 0; j < testConnection[i].length; j++) {
                System.out.print(testConnection[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("test subgraph generation");
        int[][] array = get.getConnection(GenerateRandom.generateRandomSubgraph(g, 3, 2));
        for (int[] row : array) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }

         */
    }
}