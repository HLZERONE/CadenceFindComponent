package Cadence;

/**
 * @brief Provides a utility for generating a 2D connection matrix based on a
 *        graph.
 *
 *        This class allows you to generate a 2D connection matrix from a given
 *        graph. The connection matrix represents the connectivity between
 *        components in the graph.
 */
public class Get2Dconnection {

    /**
     * @brief Generates a 2D connection matrix based on the provided graph.
     *
     *        This method calculates a 2D connection matrix based on the components
     *        and edges in the input graph. The matrix indicates whether there is a
     *        connection between components using binary values (0 for no
     *        connection, 1 for a connection).
     * @param graph The graph for which the connection matrix is generated.
     * @return A 2D integer array representing the connection matrix.
     */
    public static int[][] getConnection(Graph graph) {
        Component[] components = graph.getComponents();
        int numOfComponents = components.length;
        Edge[] edges = graph.getEdges();

        // Initialize the 2D connection matrix with all zeros
        int[][] connection2DArray = new int[numOfComponents][numOfComponents];
        for (int i = 0; i < numOfComponents; i++) {
            for (int j = 0; j < numOfComponents; j++) {
                connection2DArray[i][j] = 0;
            }
        }

        // Populate the connection matrix based on edge information
        for (Edge e : edges) {
            Component componentA = e.getComponentA();
            Component componentB = e.getComponentB();

            // Set the connection between components to 1 (indicating a connection)
            if (connection2DArray[componentA.id % numOfComponents][componentB.id % numOfComponents] == 0) {
                connection2DArray[componentA.id % numOfComponents][componentB.id % numOfComponents] = e.getDelay();
                connection2DArray[componentB.id % numOfComponents][componentA.id % numOfComponents] = e.getDelay();
            }
        }

        return connection2DArray;
    }
}
