package Cadence;
import javax.swing.*;
import java.awt.*;

class Coordinate {
    int x;
    int y;
    boolean interesting = false;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setInteresting(){
        this.interesting = true;
    }

    public void resetInteresting(){
        this.interesting = false;
    }
    public boolean contains(int x, int y){
        return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) <= Math.pow(5, 2);
    }
}



public class GraphVisualizationSmall extends JFrame {

    private final int WIDTH = 800;
    private final int HEIGHT = 800;

    private int[][] adjacencyMatrix;
    private Graph graph;
    private Graph subGraph;
    private Coordinate[] coordinates; //an array of coordinates, the indices correspond to the ID numbers


    public GraphVisualizationSmall(int[][] adjacencyMatrix, Graph g, Graph subG) {
        setTitle("Small Graph Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        this.adjacencyMatrix = adjacencyMatrix;
        this.graph = g;
        this.subGraph = subG;
        this.coordinates = new Coordinate[adjacencyMatrix.length];
    }

    private void labelInterestingCoordinates(){
        for(Component component : graph.getComponents()){
            for (Component subGraphComponent : subGraph.getComponents()){
                if (component.equals(subGraphComponent)){
                    coordinates[component.id].setInteresting();
                }
            }
        }
    }

    private void distributeCoordinateInACricle(){
        int middleX = WIDTH/2;
        int middleY = HEIGHT/2;
        for(int i = 0; i < coordinates.length; i++){
            double angle = 2 * Math.PI * i / coordinates.length;
            int x = (int) (WIDTH / 2 + (WIDTH / 3) * Math.cos(angle));
            int y = (int) (HEIGHT / 2 + (HEIGHT / 3) * Math.sin(angle));
            coordinates[i] = new Coordinate(x, y);
        }

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        distributeCoordinateInACricle();
        labelInterestingCoordinates();

        for(int i = 0; i < this.coordinates.length; i++){
            if (coordinates[i].interesting == true){
                g2d.setColor(Color.RED);
                int x = coordinates[i].x;
                int y = coordinates[i].y;
                g2d.fillOval(x-5, y-5, 15, 15);
                g2d.drawString("D: " + graph.getComponents()[i].getDensity() + " R: " + graph.getComponents()[i].getResource(), x+5, y);
            }
            else {
                g2d.setColor(Color.BLACK);
                g2d.drawOval(coordinates[i].x, coordinates[i].y, 10, 10);
            }
        }

        for(int i = 0; i < this.adjacencyMatrix.length; i++){
            for(int j = 0; j < this.adjacencyMatrix.length; j++){
                if(adjacencyMatrix[i][j] != 0){
                    int x1 = this.coordinates[i].x+5;
                    int y1 = this.coordinates[i].y+5;
                    int x2 = this.coordinates[j].x+5;
                    int y2 = this.coordinates[j].y+5;
                    g2d.drawLine(x1, y1, x2, y2);
                    g2d.drawString("" + adjacencyMatrix[i][j], (x1+x2)/2, (y1+y2)/2);
                }
            }
        }
    }


    public static void main(String[] args) {
        Component c0 = new Component(0, 1, 1);
        Component c1 = new Component(1, 2, 2);
        Component c2 = new Component(2, 3, 3);
        Component c3 = new Component(3, 4, 4);
        Edge e0 = new Edge(0, c0, c1, 10);
        Edge e1 = new Edge(1, c1, c2, 100);
        Edge e2 = new Edge(2, c1, c3, 1000);
        Component[] cList = new Component[4];
        Edge[] eList = new Edge[3];
        cList[0] = c0;
        cList[1] = c1;
        cList[2] = c2;
        cList[3] = c3;
        eList[0] = e0;
        eList[1] = e1;
        eList[2] = e2;
        Graph sysGraph = new Graph(cList, eList);

        Component sub_c0 = new Component(0, 1, 1);
        Component sub_c1 = new Component(1, 2, 2);
        Component sub_c2 = new Component(2, 3, 3);
        Edge sub_e0 = new Edge(0, sub_c0, sub_c1, 10);
        Edge sub_e1 = new Edge(1, sub_c1, sub_c2, 100);
        Component[] subCList = new Component[3];
        Edge[] subEList = new Edge[2];
        subCList[0] = sub_c0;
        subCList[1] = sub_c1;
        subCList[2] = sub_c2;
        subEList[0] = sub_e0;
        subEList[1] = sub_e1;
        Graph subGraph = new Graph(subCList, subEList);

        Graph newGraph = GenerateRandom.generateRandomGraph(10, 20);
        int[][] twoD = Get2Dconnection.getConnection(newGraph);
        Edge randomEdge = newGraph.getEdge(0).clone(); //select an edge to copy
        Edge[] smallEdges = new Edge[] {randomEdge};
        Component[] smallComponents = new Component[] {randomEdge.getComponentA(), randomEdge.getComponentB()};
        Graph smallGraph = new Graph(smallComponents, smallEdges);

        SwingUtilities.invokeLater(() -> new GraphVisualizationSmall(twoD, newGraph, smallGraph));
    }

}