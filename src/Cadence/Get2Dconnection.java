package Cadence;

public class Get2Dconnection {
    public static int[][] getConnection(Graph graph){
        Component[] components = graph.getComponents();
        int numOfComponents = components.length;
        Edge[] edges = graph.getEdges();

        int[][] connection2DArray = new int[numOfComponents][numOfComponents];
        for (int i = 0; i < numOfComponents; i++) {
            for (int j = 0; j < numOfComponents; j++) {
                connection2DArray[i][j] = 0;
            }
        }

        for (Edge e : edges){
            Component componentA = e.getComponentA();
            Component componentB = e.getComponentB();
            if (connection2DArray[componentA.id % numOfComponents][componentB.id % numOfComponents] != 1){
                connection2DArray[componentA.id % numOfComponents][componentB.id % numOfComponents] = 1;
                connection2DArray[componentB.id % numOfComponents][componentA.id % numOfComponents] = 1;
            }
        }

        return connection2DArray;
    }
}
