package Cadence;

import java.util.*;
import Cadence.GenerateRandom;
import Cadence.GraphVisualization;

public class CadenceSolution2 {

	List<Graph> allTarget = new ArrayList<>();
	Set<Component> startComponent = new HashSet<>();
	Component beginComponent;

	double lastRunTime = 0;

	public List<Graph> findAllGraph(Graph searchGraph, Graph targetGraph) {
		long startTime = System.nanoTime();

		// edge cases
		if (searchGraph == null || targetGraph == null || targetGraph.components.length == 0)
			return new ArrayList<>();

		this.allTarget = new ArrayList<>();
		this.startComponent = new HashSet<>();
		this.beginComponent = targetGraph.components[0];

		for (Component c : searchGraph.components) {
			if (c.equals(beginComponent) && !startComponent.contains(c)) {
				SychronousBFS(c, beginComponent, new State());
			}
		}

		long endTime = System.nanoTime();

		this.lastRunTime = (double) (endTime - startTime) / 1000000.0;

		return allTarget;
	}

	// return the runtime in nanoseconds of the last call of findAllGraph function
	public double getLastRunTime() {
		return lastRunTime;
	}

	private void SychronousBFS(Component c1, Component c2, State s) {
		if (c1 == null || c2 == null || !c1.equals(c2))
			return;

		// C1 graph
		s.addComponentToGraph(c1);

		// C2 graph
		s.visitedComponent_2.add(c2.id);

		Queue<Component[]> levels = new LinkedList<>();
		levels.add(new Component[] { c1, c2 });

		while (!levels.isEmpty()) {
			Component[] nextC = levels.poll();
			Component componentOne = nextC[0];
			Component componentTwo = nextC[1];
			if (componentOne.equals(beginComponent))
				startComponent.add(componentOne);

			for (Edge e : componentTwo.edges) {
				if (s.visitedEdge_2.contains(e.id))
					continue; // edge has been visited before
				s.visitedEdge_2.add(e.id);

				Component otherEndTwo = e.getOtherComponent(componentTwo);
				if (s.visitedComponent_2.contains(otherEndTwo.id)) {// component has been visited
											// before, but edge not visited

					Edge sameE = findSameEdgeWithVisitedComponent(componentOne, e, s.visitedEdge_1);
					if (sameE == null)
						return;
					s.addEdgeToGraph(sameE);

					continue;
				}

				// not visited the component yet

				// 1. check if componentOne contains the same otherEnd Component
				List<Edge> sameEdge = findSameEdge(componentOne, e, s.visitedEdge_1,
						s.visitedComponent_1);
				if (sameEdge.size() == 0)
					return;

				else if (sameEdge.size() > 1) {
					for (int i = 1; i < sameEdge.size(); i++) {
						Component otherEnd = sameEdge.get(i).getOtherComponent(componentOne);
						State newS = s.copyState();
						newS.addEdgeToGraph(sameEdge.get(i));
						SychronousBFS(otherEnd, otherEndTwo, newS);
					}
				}

				Component otherEndOne = sameEdge.get(0).getOtherComponent(componentOne);

				// 2. Add to build graph
				s.addComponentToGraph(otherEndOne);
				s.addEdgeToGraph(sameEdge.get(0));

				// 3. add to queue to traverse
				s.visitedComponent_2.add(otherEndTwo.id);
				levels.add(new Component[] { otherEndOne, otherEndTwo });
			}
		}

		this.allTarget.add(s.toGraph());
	}

	// Helper class to represent the current state of exploration
	private class State {
		List<Component> components;
		List<Edge> edges;
		Set<Integer> visitedComponent_1;
		Set<Integer> visitedEdge_1;
		Set<Integer> visitedComponent_2;
		Set<Integer> visitedEdge_2;

		// Constructor
		public State() {
			this.components = new ArrayList<>();
			this.edges = new ArrayList<>();
			this.visitedComponent_1 = new HashSet<>();
			this.visitedEdge_1 = new HashSet<>();
			this.visitedComponent_2 = new HashSet<>();
			this.visitedEdge_2 = new HashSet<>();
		}

		public State(List<Component> comps, List<Edge> eds,
				Set<Integer> vComp1, Set<Integer> vEdge1, Set<Integer> vComp2, Set<Integer> vEdge2) {
			this.components = comps;
			this.edges = eds;
			this.visitedComponent_1 = vComp1;
			this.visitedEdge_1 = vEdge1;
			this.visitedComponent_2 = vComp2;
			this.visitedEdge_2 = vEdge2;
		}

		// Make a copy of current State
		public State copyState() {
			return new State(new ArrayList<>(components), new ArrayList<>(edges),
					new HashSet<>(visitedComponent_1), new HashSet<>(visitedEdge_1),
					new HashSet<>(visitedComponent_2), new HashSet<>(visitedEdge_2));
		}

		// Add component/edge to the new build graph (the target graph in big graph)
		public void addComponentToGraph(Component c) {
			components.add(c);
			visitedComponent_1.add(c.id);
		}

		public void addEdgeToGraph(Edge e) {
			edges.add(e);
			visitedEdge_1.add(e.id);
		}

		public Graph toGraph() {
			return new Graph(components.toArray(new Component[components.size()]),
					edges.toArray(new Edge[edges.size()]));
		}
	}

	// Find the not visited edge that connecting to this component and the not
	// visited target component(FOR SychronousBFS())
	private List<Edge> findSameEdge(Component InputComponent, Edge targetEdge, Set<Integer> visitedEdge,
			Set<Integer> visitedComponent) {
		List<Edge> sameEdge = new ArrayList<>();
		for (Edge e : InputComponent.edges) {
			// not visited, same edge, with not visited other side component
			if (!visitedEdge.contains(e.id) && e.equals(targetEdge)
					&& !visitedComponent.contains(e.getOtherComponent(InputComponent).id)) {
				sameEdge.add(e);
			}
		}
		return sameEdge;
	}

	// Assumption: only one Edge connect between two component
	private Edge findSameEdgeWithVisitedComponent(Component InputComponent, Edge targetEdge,
			Set<Integer> visitedEdge) {
		for (Edge e : InputComponent.edges) {
			// not visited, same edge, with not visited other side component
			if (!visitedEdge.contains(e.id) && e.equals(targetEdge)) {
				return e;
			}
		}
		return null;
	}

	public static void multiTest(int loop, int bigGComponentNum, int bigGEdgeNum, int targetGComponentNum,
			int targetGEdgeNum) {
		CadenceSolution2 graphSolver = new CadenceSolution2();
		double totalRuntime = 0;
		int errorCount = 0;
    	
    	OuterLoop:
		for (int i = 0; i < loop; i++) {
			Graph AGraph = GenerateRandom.generateRandomGraph(bigGComponentNum, bigGEdgeNum);
			Graph Asmall = GenerateRandom.generateRandomSubgraph(AGraph, targetGComponentNum,
					targetGEdgeNum);

			List<Graph> matcher = graphSolver.findAllGraph(AGraph, Asmall);
			totalRuntime += graphSolver.getLastRunTime();
			for (Graph g : matcher) {
				if (!Asmall.equals(g)) {
					errorCount++;
        			/*
					System.out.println("TARGET GRAPH: ");
					Graph.printGraph(Asmall);
					System.out.println("INCORRECT GRAPH: ");
					Graph.printGraph(g);
             		break OuterLoop;
             		*/
				}
			}
		}
		System.out.println("AVG RUNTIME(MS): " + totalRuntime / loop);
		System.out.println("Error Rate(%): " + (double) errorCount / loop * 100);
	}

	public static void main(String[] args) {
    	//int loop, int bigGComponentNum, int bigGEdgeNum, int targetGComponentNum, int targetGEdgeNum
		 CadenceSolution2.multiTest(1000, 1000, 999, 100, 99);
//		 CadenceSolution2 graphSolver = new CadenceSolution2();
//		 Graph AGraph = GenerateRandom.generateRandomGraph(1000, 999);
//		 Graph Asmall = GenerateRandom.generateRandomSubgraph(AGraph, 100, 99);
//		 
//		 List<Graph> matcher = graphSolver.findAllGraph(AGraph, Asmall);
//		 //Graph.printGraph(Asmall);
//		 System.out.println("Number of Match: " + matcher.size());
//		 System.out.println("Runtime in ms: " + graphSolver.getLastRunTime());
//		 for(Graph g: matcher) {
//		 System.out.println("isMatch: " + Asmall.equals(g));
//		 //Graph.printGraph(g);
//		 System.out.println("----");
//}

		// //build small graph
		// System.out.println("Custom Graph");
		// Edge randomEdge = AGraph.getEdge(0).clone(); //select an edge to copy
		// Edge[] smallEdges = new Edge[] {randomEdge};
		// Component[] smallComponents = new Component[] {randomEdge.getComponentA(),
		// randomEdge.getComponentB()};
		// Graph smallGraph = new Graph(smallComponents, smallEdges);
		//
		// Graph.printGraph(smallGraph);
		// System.out.println("Number of Match: " + matcher.size());
		// System.out.println("Runtime in nanoseconds: " +
		// graphSolver.getLastRunTime());
		// //find the graph
		// matcher = graphSolver.findAllGraph(AGraph, smallGraph);
		// for(Graph g: matcher) {
		// System.out.println("isMatch: " + Asmall.equals(g));
		// Graph.printGraph(g);
		// System.out.println("----");
		// }

		// try not match graph
		// System.out.println("Unmatch Graph");
		// Graph BGraph = GenerateRandom.generateRandomGraph(10, 9);
		// List<Graph> ABmatcher = graphSolver.findAllGraph(AGraph, BGraph);
		// System.out.println("isMatch: "+ ABmatcher.size()); //should be zero
		// System.out.println("Runtime in nanoseconds: " +
		// graphSolver.getLastRunTime());
		// System.out.println("----");
		//
	}

}
