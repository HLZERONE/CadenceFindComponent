package Cadence;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GUI {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String INITIAL_FILE_PATH_TEXT = "no file selected";
    private static final int LOG_ROWS = 30;
    private static final int LOG_COLUMNS = 50;

    private JFrame frame;
    private JButton openButton, closeProgramButton, runButton;
    private static JTextField filePath;
    private JTextArea log;
    private File selectedFile;

    String selectedOption;

    private JPanel generateRandomGraphField;
    private JTextField sysComp, sysEdge, queryComp, queryEdge;

    int numSystemComponents = 10;
    int numSystemEdges = 20;
    int numQueryComponents = 4;
    int numQueryEdges = 4;

    private JCheckBox saveGraph;
    boolean saveGraphToFile = false;

    public GUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Cadence");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        initializeOpenFileSection();
        initializeLogSection();
        initializeGenerateGraphFields();
        initializeControlButtons();

        frame.setVisible(true);
    }

    private void initializeOpenFileSection() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] options = { "Use Input File", "Generate Random Graph" };
        JComboBox<String> optionDropdown = new JComboBox<>(options);
        optionDropdown.addActionListener(e -> handleOptionSelection(optionDropdown));
        panel.add(optionDropdown, BorderLayout.WEST);

        filePath = new JTextField(INITIAL_FILE_PATH_TEXT);
        panel.add(filePath, BorderLayout.CENTER);

        openButton = new JButton("Select File");
        openButton.addActionListener(e -> selectFile());
        panel.add(openButton, BorderLayout.EAST);

        frame.add(panel, BorderLayout.NORTH);
    }

    private void initializeLogSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        log = new JTextArea(LOG_ROWS, LOG_COLUMNS);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);

        JScrollPane logScrollPane = new JScrollPane(log);
        frame.add(logScrollPane, BorderLayout.WEST);
        frame.add(panel);

        log.append("Program initialized...\n");
    }

    private void initializeControlButtons() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        closeProgramButton = new JButton("Close Program");
        closeProgramButton.addActionListener(e -> System.exit(0));
        controlPanel.add(closeProgramButton);

        runButton = new JButton("Run");
        runButton.addActionListener(e -> executeGraphProcessing());
        controlPanel.add(runButton);

        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    private void handleOptionSelection(JComboBox<String> optionDropdown) {
        selectedOption = (String) optionDropdown.getSelectedItem();

        openButton.setEnabled("Use Input File".equals(selectedOption));
        generateRandomGraphField.setVisible("Generate Random Graph".equals(selectedOption));
        if ("Generate Random Graph".equals(selectedOption)) {
            filePath.setText("Random graph will be generated");
        } else {
            filePath.setText("no file selected");
        }
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

    private void initializeGenerateGraphFields() {
        generateRandomGraphField = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label0 = new JLabel("Graph Generation Parameters:");
        JLabel label1 = new JLabel("Number of system graph components");
        sysComp = new JTextField("10", 19);
        JLabel label2 = new JLabel("Number of system graph edges");
        sysEdge = new JTextField("20", 19);
        JLabel label3 = new JLabel("Number of query graph components");
        queryComp = new JTextField("4", 19);
        JLabel label4 = new JLabel("Number of query graph edges");
        queryEdge = new JTextField("4", 19);

        saveGraph = new JCheckBox("Save generated graph to file");

        generateRandomGraphField.add(label0);
        generateRandomGraphField.add(label1);
        generateRandomGraphField.add(sysComp);
        generateRandomGraphField.add(label2);
        generateRandomGraphField.add(sysEdge);
        generateRandomGraphField.add(label3);
        generateRandomGraphField.add(queryComp);
        generateRandomGraphField.add(label4);
        generateRandomGraphField.add(queryEdge);

        generateRandomGraphField.add(saveGraph);

        frame.add(generateRandomGraphField);
        generateRandomGraphField.setVisible(false);
    }

    private void executeGraphProcessing() {
        // This method should contain the logic to process the graph
        // For brevity, details are omitted here. Implement the graph processing logic.
        log.append("Processing graph...\n");
        // Example:
        // try {
        // processGraph();
        // } catch (Exception ex) {
        // ex.printStackTrace();
        // }

        try {
            Graph systemGraph = null;
            Graph queryGraph = null;
            if ("Use Input File".equals(selectedOption)) {
                systemGraph = GraphParserJSON.buildSystemGraph(selectedFile.getAbsolutePath());
                queryGraph = GraphParserJSON.buildQueryGraph(selectedFile.getAbsolutePath());
            } else if ("Generate Random Graph".equals(selectedOption)) {
                numSystemComponents = Integer.parseInt(sysComp.getText());
                numSystemEdges = Integer.parseInt(sysEdge.getText());
                numQueryComponents = Integer.parseInt(queryComp.getText());
                numQueryEdges = Integer.parseInt(queryEdge.getText());
                systemGraph = GenerateRandom.generateRandomGraph(numSystemComponents, numSystemEdges);
                queryGraph = GenerateRandom.generateRandomSubgraph(systemGraph, numQueryComponents, numQueryEdges);
                if (saveGraph.isSelected()) {
                    GraphParserJSON.combineGraphsToJsonFile(systemGraph, queryGraph, "output\\GeneratedGraph.json");
                }
            }

            CadenceSolution2 graphSolver = new CadenceSolution2();

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
            }

            // System graph
            GraphVisualizationBig graphVisualizationBig = new GraphVisualizationBig(systemGraph, queryGraph);
            graphVisualizationBig.setVisible(true);
            graphVisualizationBig.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // GraphVisualizationSmall graphVisualizationSmall = new
            // GraphVisualizationSmall(Get2Dconnection.getConnection(systemGraph),
            // systemGraph, queryGraph);
            // graphVisualizationSmall.setVisible(true);

            GraphVisualizationSmall graphVisualizationSmall = new GraphVisualizationSmall(
                    Get2Dconnection.getConnection(queryGraph),
                    queryGraph, queryGraph);
            graphVisualizationSmall.setVisible(true);
            graphVisualizationSmall.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
