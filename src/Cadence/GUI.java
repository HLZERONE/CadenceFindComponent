package Cadence;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class GUI {

        private final int WIDTH = 600;
        private final int HEIGHT = 300;

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
                log = new JTextArea(10, 50);
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

                                System.out.println("Big Graph: ");
                                Graph.printGraph(systemGraph);
                                System.out.println("Target Graph: ");
                                Graph.printGraph(queryGraph);

                                List<Graph> matcher = graphSolver.findAllGraph(systemGraph, queryGraph);
                                System.out.println("Total match graph number: " + matcher.size());
                                for (Graph g : matcher) {
                                        System.out.println("Component Number: " + g.components.length);
                                        System.out.println("Edge Number: " + g.edges.length);
                                        System.out.println("Match: " + queryGraph.equals(g));
                                        Graph.printGraph(g);
                                        System.out.println("----");
                                        // Graph.printGraph(graphs.get(1));
                                }

                                // System graph
                                GraphVisualization graphVisualization = new GraphVisualization(Get2Dconnection.getConnection(systemGraph), systemGraph); 
                                graphVisualization.setVisible(true);

                                GraphVisualization graphVisualization2 = new GraphVisualization(Get2Dconnection.getConnection(matcher.get(0)), matcher.get(0));
                                graphVisualization2.setVisible(true);
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
