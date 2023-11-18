package Cadence;
import javax.swing.*;
import java.awt.*;

public class GraphVisualization extends JFrame {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int NODE_RADIUS = 25;

    private int[][] adjacencyMatrix;

    public GraphVisualization(int[][] adjacencyMatrix) {
        setTitle("Graph Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        this.adjacencyMatrix = adjacencyMatrix;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        int nodeCount = adjacencyMatrix.length;

        // Draw nodes
        g2d.setColor(Color.BLUE);
        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI * i / nodeCount;
            int x = (int) (WIDTH / 2 + (WIDTH / 3) * Math.cos(angle));
            int y = (int) (HEIGHT / 2 + (HEIGHT / 3) * Math.sin(angle));
            g2d.fillOval(x - NODE_RADIUS / 2, y - NODE_RADIUS / 2, NODE_RADIUS, NODE_RADIUS);
        }

        // Draw edges based on adjacency matrix
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
                }
            }
        }
    }

    public static void main(String[] args) {
        // Example adjacency matrix

        int[][] adjacencyMatrix = GenerateRandom.generateRandomGraph(3, 3).getConnection2DArray();

        SwingUtilities.invokeLater(() -> {
            GraphVisualization graph = new GraphVisualization(adjacencyMatrix);
            graph.setVisible(true);
        });
    }
}
