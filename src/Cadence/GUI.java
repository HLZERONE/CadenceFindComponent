package Cadence;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI {

        private final int WIDTH = 600;
        private final int HEIGHT = 900;

        private JFrame frame;
        JButton openButton, closeProgramButton, runButton;

        static JTextField filePath;

        JTextArea log;

        JPanel panel;

        File selectedFile;

        private void openFile() {
                panel = new JPanel();
                filePath = new JTextField("no file selected", 30);
                panel.add(filePath);
                openButton = new JButton("Select File...");
                openButton.addActionListener(e -> selectFile());
                panel.add(openButton);
                frame.add(panel);
        }

        private void selectFile() {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile();
                        filePath.setText(selectedFile.getAbsolutePath());
                        log.append("Selected file: " + selectedFile.getAbsolutePath() + "\n");
                }
        }

        private void log() {
                log = new JTextArea(40, 50);
                log.setMargin(new Insets(5, 5, 5, 5));
                log.setEditable(false);
                JScrollPane logScrollPane = new JScrollPane(log);
                frame.add(logScrollPane, BorderLayout.CENTER);
        }

        private void run() {
                frame = new JFrame("Cadence");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new FlowLayout());

                openFile();
                log();

                closeProgramButton = new JButton("Close Program");
                closeProgramButton.addActionListener(e -> System.exit(0));
                frame.add(closeProgramButton);
                runButton = new JButton("Run");
                runButton.addActionListener(e -> {
                        try {
                                Graph systemGraph = GraphParserJSON.buildSystemGraph(selectedFile.getAbsolutePath());
                                Graph queryGraph = GraphParserJSON.buildQueryGraph(selectedFile.getAbsolutePath());

                                CadenceSolution2 graphSolver = new CadenceSolution2();

                                /*
                                 * for (Graph graph : graphs) {
                                 * Graph.printGraph(graph);
                                 * System.out.println("----");
                                 * }
                                 */

                                // System.out.println("Big Graph: ");
                                // Graph.printGraph(systemGraph);
                                // System.out.println("Target Graph: ");
                                // Graph.printGraph(queryGraph);

                                log.append("Big Graph: \n");
                                for (Edge systemEdges : systemGraph.edges) {
                                        log.append("Edge ID: " + systemEdges.id + " Delay: " + systemEdges.delay
                                                        + "\n");
                                        log.append("Component ID: " + systemEdges.A.id + " Density: "
                                                        + systemEdges.A.density +
                                                        " Resource: " + systemEdges.A.resource + " Edge Num: "
                                                        + systemEdges.A.edges.size() + "\n");
                                        log.append("Component ID: " + systemEdges.B.id + " Density: "
                                                        + systemEdges.B.density +
                                                        " Resource: " + systemEdges.B.resource + " Edge Num: "
                                                        + systemEdges.B.edges.size() + "\n");
                                }

                                log.append("Target Graph: \n");
                                for (Edge queryEdges : queryGraph.edges) {
                                        log.append("Edge ID: " + queryEdges.id + " Delay: " + queryEdges.delay + "\n");
                                        log.append("Component ID: " + queryEdges.A.id + " Density: "
                                                        + queryEdges.A.density +
                                                        " Resource: " + queryEdges.A.resource + " Edge Num: "
                                                        + queryEdges.A.edges.size() + "\n");
                                        log.append("Component ID: " + queryEdges.B.id + " Density: "
                                                        + queryEdges.B.density +
                                                        " Resource: " + queryEdges.B.resource + " Edge Num: "
                                                        + queryEdges.B.edges.size() + "\n");
                                }

                                List<Graph> matcher = graphSolver.findAllGraph(systemGraph, queryGraph);
                                // System.out.println("Total match graph number: " + matcher.size());
                                // for (Graph g : matcher) {
                                //         System.out.println("Component Number: " + g.components.length);
                                //         System.out.println("Edge Number: " + g.edges.length);
                                //         System.out.println("Match: " + queryGraph.equals(g));
                                //         Graph.printGraph(g);
                                //         System.out.println("----");
                                //         // Graph.printGraph(graphs.get(1));
                                // }

                                log.append("Total match graph number: " + matcher.size() + "\n");
                                for (Graph g : matcher) {
                                        log.append("Component Number: " + g.components.length + "\n");
                                        log.append("Edge Number: " + g.edges.length + "\n");
                                        log.append("Match: " + queryGraph.equals(g) + "\n");
                                        for (Edge edge : g.edges) {
                                                log.append("Edge ID: " + edge.id + " Delay: " + edge.delay + "\n");
                                                log.append("Component ID: " + edge.A.id + " Density: "
                                                                + edge.A.density +
                                                                " Resource: " + edge.A.resource + " Edge Num: "
                                                                + edge.A.edges.size() + "\n");
                                                log.append("Component ID: " + edge.B.id + " Density: "
                                                                + edge.B.density +
                                                                " Resource: " + edge.B.resource + " Edge Num: "
                                                                + edge.B.edges.size() + "\n");
                                        }
                                        log.append("----\n");
                                        // Graph.printGraph(graphs.get(1));
                                }

                                // System graph
                                // GraphVisualization graphVisualization = new
                                // GraphVisualization(Get2Dconnection.getConnection(systemGraph), systemGraph);
                                // graphVisualization.setVisible(true);

                                // GraphVisualization graphVisualization2 = new
                                // GraphVisualization(Get2Dconnection.getConnection(matcher.get(0)),
                                // matcher.get(0));
                                // graphVisualization2.setVisible(true);

                                GraphVisualizationSmall graphVisualizationSmall = new
                                GraphVisualizationSmall(Get2Dconnection.getConnection(systemGraph),
                                systemGraph, queryGraph);
                                graphVisualizationSmall.setVisible(true);
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                });
                frame.add(runButton);

                frame.setSize(WIDTH, HEIGHT);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

        }

        public GUI() {
                run();
        }

        public static void main(String[] args) {
                javax.swing.SwingUtilities.invokeLater(GUI::new);
        }
}
