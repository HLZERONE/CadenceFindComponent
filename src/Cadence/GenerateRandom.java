package Cadence;
import java.util.*;


public class GenerateRandom {
    // For a connected graph, the number of edges depends on the number of components
    // the number of edges should be within the range of n-1 to n(n-1)/2, where n is the number of components
    // the Result class in this java file is useless now, made separate function to capture 2D connections
    // example:
    // Graph g = GenerateRandom.generateRandomGraph(5, 4);
    // - generating a random graph with 5 vertices and 4 edges
    public static Graph generateRandomGraph(int numOfComponents, int numOfEdges){
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

        return new Graph(components, edges);
        //return new Result(returnGraph, connection2DArray);
    }

    public static Graph generateRandomSubgraph(Graph systemGraph, int componentNumber, int edgeNumber){
        //tries its best to meet the specified number of components and edges in the argument,
        //but if the random starting vertex fails to locate such a subgraph, it will return the closest subgraph.
        //example:
        // Graph bigGraph = GenerateRandom.generateRandomGraph(5, 4);
        // Graph subGraph = GenerateRandom.generateRandomSubgraph(bigGraph, 3, 2)
        try {
            if(componentNumber > systemGraph.numComponents()) {
                throw new Exception("too many components, should be less than: " + systemGraph.numEdges());
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

        ArrayList<Component> components = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Integer> edgeConnection = new ArrayList<Integer>();
        HashSet<Integer> availableEdgeIDs = new HashSet<>();

        HashSet<Integer> visitedComponents = new HashSet<>();
        HashSet<Integer> visitedEdges = new HashSet<>();

        Random random = new Random();
        int currentComponentIndex = random.nextInt(systemGraph.numComponents());
        Component startComponent = systemGraph.getComponent(currentComponentIndex);
        visitedComponents.add(startComponent.id);

        int currentSubgraphComponents = 0;
        int currentSubgraphEdges = 0;

        Component currentComponent = new Component(startComponent.id, startComponent.resource, startComponent.density);
        components.add(currentComponent);
        currentSubgraphComponents += 1;

        for (Edge e : startComponent.edges){
            availableEdgeIDs.add(e.getId());
        }

        while (currentSubgraphComponents != componentNumber && !availableEdgeIDs.isEmpty()){
            int randomEdgeIndex = random.nextInt(availableEdgeIDs.size());
            Iterator<Integer> it = availableEdgeIDs.iterator();
            for (int i = 0; i < randomEdgeIndex; i++){
                it.next();
            }
            Edge randomEdge = systemGraph.getEdge(it.next());
            
            if (visitedComponents.contains(randomEdge.getComponentA().id)){
                for (Edge e : randomEdge.getComponentB().edges){
                    availableEdgeIDs.add(e.getId());
                }
                Component newComponent = new Component(randomEdge.getComponentB().id, randomEdge.getComponentB().getResource(), randomEdge.getComponentB().getDensity());
                components.add(newComponent);
                visitedComponents.add(randomEdge.getComponentB().id);
                Edge newEdge;
                for (Component c : components){
                    if (c.id == randomEdge.getComponentA().id){
                        newEdge = new Edge(randomEdge.id, c, newComponent, randomEdge.delay);
                        edges.add(newEdge);
                        break;
                    }
                }
            }
            else{
                for (Edge e : randomEdge.getComponentA().edges){
                    availableEdgeIDs.add(e.getId());
                }
                Component newComponent = new Component(randomEdge.getComponentA().id, randomEdge.getComponentA().getResource(), randomEdge.getComponentA().getDensity());
                components.add(newComponent);
                visitedComponents.add(randomEdge.getComponentA().id);
                Edge newEdge;
                for (Component c : components){
                    if (c.id == randomEdge.getComponentB().id){
                        newEdge = new Edge(randomEdge.id, c, newComponent, randomEdge.delay);
                        edges.add(newEdge);
                        break;
                    }
                }

            }
            currentSubgraphComponents += 1;
            currentSubgraphEdges += 1;
            Integer toRemove = randomEdge.id;
            availableEdgeIDs.remove(toRemove);
        }

        Component[] c = components.toArray(new Component[0]);
        Edge[] e = edges.toArray(new Edge[0]);
        return new Graph(c, e);
    }

    public static void main(String[] args) {
        // generates a fully connected graph with 5 vertices
        get2Dconnection get = new get2Dconnection();

        Graph g = GenerateRandom.generateRandomGraph(5, 4);
        System.out.println("number of components: " + g.components.length);
        for(int i = 0; i < g.components.length; i++){
            List<Edge> e = g.components[i].edges;
            System.out.println(e);
        }
        System.out.println("number of edges: " + g.edges.length);
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


    }
}
