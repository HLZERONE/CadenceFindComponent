package Cadence;

import java.util.*;
import Cadence.GenerateRandom;
import Cadence.GraphVisualization;

public class CadenceSolution2 {
	
	List<Graph> allTarget = new ArrayList<>();
	
	//TODO: Function that find the target start component in Big Graph, once found, call SychronousBFS
	public List<Graph> findAllGraph(Graph searchGraph, Graph targetGraph){
		//edge cases
		if(searchGraph == null || targetGraph == null || targetGraph.components.length == 0) return new ArrayList<>();
		
		
		this.allTarget = new ArrayList<>();
		Component beginComponent = targetGraph.components[0];
		
		for(Component c: searchGraph.components) {
			if(c.equals(beginComponent)) {
				SychronousBFS(c, beginComponent, new State());
			}
		}
		
		return allTarget;
	}
	
	private void SychronousBFS(Component c1, Component c2, State s){
		if(c1 == null || c2 == null || !c1.equals(c2)) return;
		
		//C1 graph
        s.addComponentToGraph(c1);
        
        //C2 graph
        s.visitedComponent_2.add(c2.id);
        
        Queue<Component[]> levels = new LinkedList<>();
        levels.add(new Component[] {c1, c2});
        
        while(!levels.isEmpty()) {
        	Component[] nextC = levels.poll();
        	Component componentOne = nextC[0];
        	Component componentTwo = nextC[1];
        	
        	for(Edge e: componentTwo.edges) {
        		if(s.visitedEdge_2.contains(e.id)) continue; //edge has been visited before
        		s.visitedEdge_2.add(e.id);
        		
        		Component otherEndTwo = e.getOtherComponent(componentTwo);
        		if(s.visitedComponent_2.contains(otherEndTwo.id)) continue; //component has been visited before
        		
        		//not visited the component yet
        		
        		//1. check if componentOne contains the same otherEnd Component
        		List<Edge> sameEdge = findSameEdge(componentOne, e, s.visitedEdge_1, s.visitedComponent_1);
        		if(sameEdge.size() == 0) return;
        		
        		else if(sameEdge.size() > 1) {
        			for(int i=1; i<sameEdge.size(); i++) {
        				Component otherEnd = sameEdge.get(i).getOtherComponent(componentOne);
        				State newS = s.copyState();
        				newS.addEdgeToGraph(sameEdge.get(i));
        				
        				SychronousBFS(otherEnd, otherEndTwo, newS);
        			}
        		}
        		
        		Component otherEndOne = sameEdge.get(0).getOtherComponent(componentOne);
        		
        		//2. Add to build graph
        		s.addComponentToGraph(otherEndOne);
        		s.addEdgeToGraph(sameEdge.get(0));
        		
        		//3. add to queue to traverse
        		s.visitedComponent_2.add(otherEndTwo.id);
        		levels.add(new Component[] {otherEndOne, otherEndTwo});
        	}
        }
        
        this.allTarget.add(s.toGraph());
	}
	

    // Helper class to represent the current state of exploration
    class State {
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
        
        //Make a copy of current State
        public State copyState() {
        	return new State(new ArrayList<>(components), new ArrayList<>(edges), 
					new HashSet<>(visitedComponent_1), new HashSet<>(visitedEdge_1), 
					new HashSet<>(visitedComponent_2), new HashSet<>(visitedEdge_2));
        }
        
        //Add component/edge to the new build graph (the target graph in big graph)
        public void addComponentToGraph(Component c) {
        	components.add(c);
        	visitedComponent_1.add(c.id);
        }
        public void addEdgeToGraph(Edge e) {
        	edges.add(e);
        	visitedEdge_1.add(e.id);
        }
        
        public Graph toGraph() {
        	return new Graph(components.toArray(new Component[components.size()]), edges.toArray(new Edge[edges.size()]));
        }
    }
	
	//Find the not visited edge that connecting to this component and the not visited target component(FOR SychronousBFS())
	private List<Edge> findSameEdge(Component InputComponent, Edge targetEdge, Set<Integer> visitedEdge, Set<Integer> visitedComponent) {
		List<Edge> sameEdge = new ArrayList<>();
		for(Edge e: InputComponent.edges) {
			//not visited, same edge, with not visited other side component
			if(!visitedEdge.contains(e.id) && e.equals(targetEdge) && !visitedComponent.contains(e.getOtherComponent(InputComponent).id)){
				sameEdge.add(e);
			}
		}
		return sameEdge;
	}
	
	

    
    public static void main(String[] args) {
    	CadenceSolution2 graphSolver = new CadenceSolution2();
    	Graph AGraph = GenerateRandom.generateRandomGraph(400, 399);
    	Graph Asmall = GenerateRandom.generateRandomSubgraph(AGraph, 3, 2);
    	System.out.println(Asmall.edges.length);
    	
    	List<Graph> matcher = graphSolver.findAllGraph(AGraph, Asmall);
    	System.out.println(matcher.size());
    	for(Graph g: matcher) {
    		System.out.println(g.edges.length);
    		Graph.printGraph(g);
    		
    		System.out.println("----");
    		Graph.printGraph(Asmall);
    	}
    	/*
    	//build small graph
    	Edge randomEdge = AGraph.getEdge(0).clone(); //select an edge to copy
    	Edge[] smallEdges = new Edge[] {randomEdge};
    	Component[] smallComponents = new Component[] {randomEdge.getComponentA(), randomEdge.getComponentB()};
    	Graph smallGraph = new Graph(smallComponents, smallEdges);
    	
    	//find the graph
    	List<Graph> matcher = findAllGraph(AGraph, smallGraph);
    	for(Graph g: matcher) {
    		Graph.printGraph(g);
    		System.out.println("----");
    	}
    	Graph.printGraph(smallGraph);
    	System.out.println("----");
    	
    	//try not match graph
    	Graph BGraph = GenerateRandom.generateRandomGraph(3, 2);
    	List<Graph> ABmatcher = findAllGraph(AGraph, BGraph);
    	System.out.println(ABmatcher.size()); //should be zero
    	Graph.printGraph(AGraph);
    	System.out.println("----");
    	Graph.printGraph(BGraph);
    	*/
    }
    
    
}
