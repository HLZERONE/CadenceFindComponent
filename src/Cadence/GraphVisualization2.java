package Cadence;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GraphVisualization extends JFrame {

    private final int WIDTH = 800;
    private final int HEIGHT = 800;
    private final int NODE_RADIUS = 15;

    private int[][] adjacencyMatrix;
    private Graph graph;
    private Graph smallGraph; // smallGraph 的引用

    public GraphVisualization(int[][] adjacencyMatrix, Graph g, Graph smallGraph) {
        setTitle("Graph Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        this.adjacencyMatrix = adjacencyMatrix;
        this.graph = g;
        this.smallGraph = smallGraph;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        int nodeCount = adjacencyMatrix.length;

        // 获取拥有最多边的节点索引
        int centerNodeIndex = findIndexOfNodeWithMostEdges();

        // 画所有节点
        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI * i / (nodeCount - 1); // 调整圆形布局
            int x, y;
            if (i == centerNodeIndex) {
                // 将拥有最多边的节点放在中心
                x = WIDTH / 2;
                y = HEIGHT / 2;
            } else {
                // 其他节点围绕中心节点排列
                x = (int) (WIDTH / 2 + (WIDTH / 3) * Math.cos(angle));
                y = (int) (HEIGHT / 2 + (HEIGHT / 3) * Math.sin(angle));
            }
            
            boolean isInSmallGraph = isNodeInSmallGraph(graph.components[i].id);
            g2d.setColor(isInSmallGraph ? Color.RED : Color.BLACK);
            g2d.fillOval(x - NODE_RADIUS / 2, y - NODE_RADIUS / 2, NODE_RADIUS, NODE_RADIUS);

            if (isInSmallGraph || i == centerNodeIndex) {
                String nodeText = "R: " + graph.components[i].getResource() + " D: " + graph.components[i].getDensity();
                g2d.drawString(nodeText, x - NODE_RADIUS, y + NODE_RADIUS);
            }
        }

        // 画边
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    drawEdge(g2d, i, j, isEdgeInSmallGraph(i, j));
                }
            }
        }
    }

    private void drawEdge(Graphics2D g2d, int nodeIndex1, int nodeIndex2, boolean isInSmallGraph) {
        double angleI = 2 * Math.PI * nodeIndex1 / nodeCount;
        double angleJ = 2 * Math.PI * nodeIndex2 / nodeCount;

        int x1 = (int) (WIDTH / 2 + (WIDTH / 3) * Math.cos(angleI));
        int y1 = (int) (HEIGHT / 2 + (HEIGHT / 3) * Math.sin(angleI));

        int x2 = (int) (WIDTH / 2 + (WIDTH / 3) * Math.cos(angleJ));
        int y2 = (int) (HEIGHT / 2 + (HEIGHT / 3) * Math.sin(angleJ));

        g2d.setColor(isInSmallGraph ? Color.RED : Color.BLACK);
        g2d.drawLine(x1, y1, x2, y2);
        if (isInSmallGraph) {
            Edge edge = findEdge(nodeIndex1, nodeIndex2);
            if (edge != null) {
                String edgeText = "Delay: " + edge.getDelay();
                g2d.drawString(edgeText, (x1 + x2) / 2, (y1 + y2) / 2);
            }
        }
    }

    private boolean isNodeInSmallGraph(int nodeId) {
        return Arrays.stream(smallGraph.components).anyMatch(c -> c.id == nodeId);
    }

    private boolean isEdgeInSmallGraph(int nodeIndex1, int nodeIndex2) {
        Component c1 = graph.components[nodeIndex1];
        Component c2 = graph.components[nodeIndex2];
        return Arrays.stream(smallGraph.edges).anyMatch(e -> 
            (e.A.id == c1.id && e.B.id == c2.id) || (e.A.id == c2.id && e.B.id == c1.id)
        );
    }

    private Edge findEdge(int nodeIndex1, int nodeIndex2) {
        Component c1 = graph.components[nodeIndex1];
        Component c2 = graph.components[nodeIndex2];
        for (Edge e : graph.edges) {
            if ((e.A.equals(c1) && e.B.equals(c2)) || (e.A.equals(c2) && e.B.equals(c1))) {
                return e;
            }
        }
        return null;
    }
	
	private int findIndexOfNodeWithMostEdges() {
        int maxEdgeCount = 0;
        int nodeIndexWithMostEdges = -1;
        for (int i = 0; i < graph.components.length; i++) {
            int edgeCount = graph.components[i].edges.size();
            if (edgeCount > maxEdgeCount) {
                maxEdgeCount = edgeCount;
                nodeIndexWithMostEdges = i;
            }
        }
        return nodeIndexWithMostEdges;
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
