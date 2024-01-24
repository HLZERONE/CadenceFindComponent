package Cadence;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphVisualization extends JFrame {

    private final int WIDTH = 800;
    private final int HEIGHT = 800;
    private final int NODE_RADIUS = 15;

    private int[][] adjacencyMatrix;
    private Graph graph;

    public GraphVisualization(int[][] adjacencyMatrix, Graph g) {
        setTitle("Graph Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        this.adjacencyMatrix = adjacencyMatrix;
        this.graph = g;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        int nodeCount = adjacencyMatrix.length;

        // Draw nodes
        g2d.setColor(Color.BLUE);

        double angle = 2 * Math.PI * 0 / nodeCount;

        int x = (int) (WIDTH / 2 + (WIDTH / 3) * Math.cos(angle));
        int y = (int) (HEIGHT / 2 + (HEIGHT / 3) * Math.sin(angle));
        /*
        g2d.fillOval(x - NODE_RADIUS / 2, y - NODE_RADIUS / 2, NODE_RADIUS, NODE_RADIUS);
         */
        int v = 0;
        for (int i = 0; i < nodeCount; i++) {
            angle = 2 * Math.PI * i / nodeCount;
            x = (int) (WIDTH / 2 + (WIDTH / 3) * Math.cos(angle));
            y = (int) (HEIGHT / 2 + (HEIGHT / 3) * Math.sin(angle));
            g2d.setColor(Color.BLUE);
            g2d.fillOval(x - NODE_RADIUS / 2, y - NODE_RADIUS / 2, NODE_RADIUS, NODE_RADIUS);
            String nodeText = "R: " + graph.components[v].getResource();
            nodeText = nodeText + " D: " + graph.components[v].getDensity();
            g2d.setColor(Color.BLACK);
            g2d.drawString(nodeText, x - NODE_RADIUS, y + NODE_RADIUS);
            v++;
        }

        // Draw edges based on adjacency matrix
        int e = 0;
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < nodeCount; i++) {
            for (int j = i; j < nodeCount; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    double angleI = 2 * Math.PI * i / nodeCount;
                    double angleJ = 2 * Math.PI * j / nodeCount;

                    int x1 = (int) (WIDTH / 2 + (WIDTH / 3) * Math.cos(angleI));
                    int y1 = (int) (HEIGHT / 2 + (HEIGHT / 3) * Math.sin(angleI));

                    int x2 = (int) (WIDTH / 2 + (WIDTH / 3) * Math.cos(angleJ));
                    int y2 = (int) (HEIGHT / 2 + (HEIGHT / 3) * Math.sin(angleJ));

                    g2d.drawLine(x1, y1, x2, y2);
                    String edgeText = Integer.toString(graph.edges[e].getDelay());
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(edgeText, (x1+x2)/2, (y1+y2)/2);
                    e++;
                }
            }
        }
    }


    public static void main(String[] args) {
        // Example adjacency matrix
        Graph graph1 = GenerateRandom.generateRandomGraph(10, 11);
        int[][] adjacencyMatrix = Get2Dconnection.getConnection(graph1);

        Graph AGraph = GenerateRandom.generateRandomGraph(6, 7);
        //build small graph
        Edge randomEdge = AGraph.getEdge(0).clone(); //select an edge to copy
        Edge[] smallEdges = new Edge[] {randomEdge};
        Component[] smallComponents = new Component[] {randomEdge.getComponentA(), randomEdge.getComponentB()};
        Graph smallGraph = new Graph(smallComponents, smallEdges);

        //find the graph
        List<Graph> matcher = CadenceSolution.findAllGraph(AGraph, smallGraph);

        int[][] adMatrix1 = Get2Dconnection.getConnection(AGraph);
        int[][] adMatrix2 = Get2Dconnection.getConnection(matcher.get(0));
        SwingUtilities.invokeLater(() -> {
            GraphVisualization graphVisualization = new GraphVisualization(adMatrix1, AGraph);
            GraphVisualization graphVisualization2 = new GraphVisualization(adMatrix2, matcher.get(0));
            graphVisualization.setVisible(true);
            graphVisualization2.setVisible(true);
            /*
            GraphVisualization graphVisualization = new GraphVisualization(adjacencyMatrix, graph1);
            graphVisualization.setVisible(true);
             */
        });
    }
}
