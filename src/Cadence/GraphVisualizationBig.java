package Cadence;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/*
contain all the coordinates in an array
each coordinate correspond to a node ID in the system graph
if the node itself matches any node in the subgraph, mark it as "interesting"
at the "interesting" nodes' coordinates, draw out circles (and their connections?)
 */

class DistributeCoordinatesUniform {
    public static Coordinate[] distributeCoordinatesUniform(int maxX, int maxY, int NUM_POINTS) {
        Coordinate[] coordinates = new Coordinate[NUM_POINTS];
        int numRows = (int) Math.ceil(Math.sqrt(NUM_POINTS));
        int numCols = (int) Math.ceil((double) NUM_POINTS / numRows);
        int spacingX = maxX / (numCols + 1);
        int spacingY = maxY / (numRows + 1);

        int index = 0;
        for (int row = 0; row < numRows && index < NUM_POINTS; row++) {
            for (int col = 0; col < numCols && index < NUM_POINTS; col++) {
                int x = (col + 1) * spacingX;
                int y = (row + 1) * spacingY + 20;
                coordinates[index++] = new Coordinate(x, y);
            }
        }
        return coordinates;
    }
}

public class GraphVisualizationBig extends JFrame implements MouseListener {

    private int HEIGHT = 800;
    private int WIDTH = 1500;
    private Graph systemGraph;
    private Graph subGraph;
    private int[][] adjacencyMatrix;

    private Coordinate[] coordinates;
    public GraphVisualizationBig(Graph systemG, Graph subG) {
        super("Interactive GUI");
        //setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(this);
        setVisible(true);
        systemGraph = systemG;
        subGraph = subG;
        coordinates = DistributeCoordinatesUniform.distributeCoordinatesUniform(WIDTH, HEIGHT, systemG.numComponents());
        adjacencyMatrix = Get2Dconnection.getConnection(systemGraph);
    }

    private void labelInterestingCoordinates(){
        for(Component component : systemGraph.getComponents()){
            for (Component subGraphComponent : subGraph.getComponents()){
                if (component.equals(subGraphComponent)){
                    coordinates[component.id].setInteresting();
                }
            }
        }
    }


    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        labelInterestingCoordinates();
        for(int i = 0; i < this.coordinates.length; i++){
            int x = coordinates[i].x;
            int y = coordinates[i].y;
            if (coordinates[i].interesting == true){
                g2d.setColor(Color.RED);
                g2d.fillOval(x-1, y-1, 5, 5);
                g2d.drawString("" + systemGraph.getComponents()[i].getDensity() + "," + systemGraph.getComponents()[i].getResource(), x-2, y);
                for(int j = 0; j < adjacencyMatrix[i].length; j++){
                    if (adjacencyMatrix[i][j] != 0){
                        int x2 = coordinates[j].x;
                        int y2 = coordinates[j].y;
                        g2d.setColor(Color.BLACK);
                        g2d.drawLine(x, y, x2, y2);
                        g2d.drawString("" + adjacencyMatrix[i][j], (x+x2)/2, (y+y2)/2);
                    }
                }
            }
            else {
                g2d.setColor(Color.BLACK);
                g2d.drawOval(x, y, 5, 5);
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for(int i = 0; i < coordinates.length; i++){
            if (coordinates[i].contains(x, y)){
                String connectedComponentsMessage = "";
                for (int j = 0; j < adjacencyMatrix[i].length; j++){
                    if (adjacencyMatrix[i][j] != 0){
                        connectedComponentsMessage = connectedComponentsMessage + j + "\n";
                    }
                }
                Component c = systemGraph.getComponent(i);
                JOptionPane.showMessageDialog(this, "component ID: " + i +
                        " D: " + c.getDensity() + " R: " + c.getResource() +
                        "\n" + "adjacent Components: \n" + connectedComponentsMessage, "Clicked Component Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

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

        Graph newGraph = GenerateRandom.generateRandomGraph(1000, 1001);
        Edge randomEdge = newGraph.getEdge(0).clone(); //select an edge to copy
        Edge[] smallEdges = new Edge[] {randomEdge};
        Component[] smallComponents = new Component[] {randomEdge.getComponentA(), randomEdge.getComponentB()};
        Graph smallGraph = new Graph(smallComponents, smallEdges);

        SwingUtilities.invokeLater(() -> new GraphVisualizationBig(newGraph, smallGraph));
    }
}

