package Cadence;

public class TestGraph {
        public static void main(String[] args) {
                try {
                        Graph mainGraph = GraphBuilder.buildGraphFromFile("mainGraph.txt");
                        Graph subGraph = GraphBuilder.buildGraphFromFile("subGraph.txt");
                }
                catch(Exception e) {
                        System.out.println(e);
                }

                
        }
}
