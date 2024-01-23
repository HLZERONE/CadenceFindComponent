package Cadence;

import java.util.*;
import Cadence.GenerateRandom;
import Cadence.GraphVisualization;

public class CadenceSolution {
	//TODO: Function that find the target start component in Big Graph, once found, call SychronousBFS
	public static List<Graph> findAllGraph(Graph searchGraph, Graph targetGraph){
		//edge cases
		if(searchGraph == null || targetGraph == null || targetGraph.components.length == 0) return new ArrayList<>();
		
		List<Graph> allTarget = new ArrayList<>();
		Component beginComponent = targetGraph.components[0];
		
		for(Component c: searchGraph.components) {
			if(c.equals(beginComponent)) {
				Graph loopGraph = SychronousBFS(c, beginComponent);
				if(loopGraph != null) {//found
					allTarget.add(loopGraph);
				}
			}
		}
		
		return allTarget;
	}
	
	//TODO: UPDATE TO DFS OR ADD MULTITHREAD
	private static void SychronousBFS2(Component c1, Component c2, 
                                       List<Graph> result, List<Component> components, List<Edge> edges, 
                                       Set<Integer> visitedComponent_1, Set<Integer> visitedEdge_1, 
                                       Set<Integer> visitedComponent_2, Set<Integer> visitedEdge_2) {
        // Queue to store the state of each possible path
        Queue<State> queue = new LinkedList<>();
        // Initial state
        State initialState = new State(c1, c2, new ArrayList<>(components), new ArrayList<>(edges), 
                                       new HashSet<>(visitedComponent_1), new HashSet<>(visitedEdge_1), 
                                       new HashSet<>(visitedComponent_2), new HashSet<>(visitedEdge_2));
        queue.add(initialState);

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            // Current state details
            Component currentComponent_1 = currentState.component1;
            Component currentComponent_2 = currentState.component2;
            List<Component> currentPathComponents = currentState.components;
            List<Edge> currentPathEdges = currentState.edges;
            Set<Integer> currentVisitedComponent_1 = currentState.visitedComponent_1;
            Set<Integer> currentVisitedEdge_1 = currentState.visitedEdge_1;
            Set<Integer> currentVisitedComponent_2 = currentState.visitedComponent_2;
            Set<Integer> currentVisitedEdge_2 = currentState.visitedEdge_2;

            // Exploring edges of the current component
            for (Edge e : currentComponent_2.edges) {
                if (currentVisitedEdge_2.contains(e.id)) continue;
                currentVisitedEdge_2.add(e.id);

                Component otherEndTwo = e.getOtherComponent(currentComponent_2);
                if (currentVisitedComponent_2.contains(otherEndTwo.id)) continue;

                // Finding matching edges
                List<Edge> matchingEdges = findSameEdge2(currentComponent_1, e, currentVisitedEdge_1, currentVisitedComponent_1);
                for (Edge matchingEdge : matchingEdges) {
                    Component otherEndOne = matchingEdge.getOtherComponent(currentComponent_1);
                    
                    // Creating new state for each matching edge and adding to the queue
                    State newState = new State(otherEndOne, otherEndTwo, 
                                               new ArrayList<>(currentPathComponents), 
                                               new ArrayList<>(currentPathEdges), 
                                               new HashSet<>(currentVisitedComponent_1), 
                                               new HashSet<>(currentVisitedEdge_1), 
                                               new HashSet<>(currentVisitedComponent_2), 
                                               new HashSet<>(currentVisitedEdge_2));
                    queue.add(newState);
                }
            }
        }

        // Adding result if the exploration is complete
        if (!components.isEmpty()) {
            Component[] graphC = components.toArray(new Component[components.size()]);
            Edge[] graphE = edges.toArray(new Edge[edges.size()]);
            result.add(new Graph(graphC, graphE));
        }
    }

    // Helper class to represent the current state of exploration
    static class State {
        Component component1;
        Component component2;
        List<Component> components;
        List<Edge> edges;
        Set<Integer> visitedComponent_1;
        Set<Integer> visitedEdge_1;
        Set<Integer> visitedComponent_2;
        Set<Integer> visitedEdge_2;

        // Constructor
        public State(Component comp1, Component comp2, List<Component> comps, List<Edge> eds, 
                     Set<Integer> vComp1, Set<Integer> vEdge1, Set<Integer> vComp2, Set<Integer> vEdge2) {
            this.component1 = comp1;
            this.component2 = comp2;
            this.components = comps;
            this.edges = eds;
            this.visitedComponent_1 = vComp1;
            this.visitedEdge_1 = vEdge1;
            this.visitedComponent_2 = vComp2;
            this.visitedEdge_2 = vEdge2;
        }
    }
	
	//Find the not visited edge that connecting to this component and the not visited target component(FOR SychronousBFS())
	private static List<Edge> findSameEdge2(Component InputComponent, Edge targetEdge, Set<Integer> visitedEdge, Set<Integer> visitedComponent) {
		List<Edge> sameEdge = new ArrayList<>();
		for(Edge e: InputComponent.edges) {
			//not visited, same edge, with not visited other side component
			if(!visitedEdge.contains(e.id) && e.equals(targetEdge) && !visitedComponent.contains(e.getOtherComponent(InputComponent).id)){
				sameEdge.add(e);
			}
		}
		return sameEdge;
	}
	
	//Find the not visited edge that connecting to this component and the not visited target component(FOR SychronousBFS())
	private static Edge findSameEdge(Component InputComponent, Edge targetEdge, Set<Integer> visitedEdge, Set<Integer> visitedComponent) {
		for(Edge e: InputComponent.edges) {
			//not visited, same edge, with not visited other side component
			if(!visitedEdge.contains(e.id) && e.equals(targetEdge) && !visitedComponent.contains(e.getOtherComponent(InputComponent).id)){
				return e;
			}
		}
		return null;
	}
	
    public boolean BFS(Graph graph){
        if(graph == null) return false;

        Set<Integer> visitedComponent = new HashSet<>(); //store unique ID for components
        Set<Integer> visitedEdge = new HashSet<>(); //store unique ID for edges
        
        //the start point
        Component targetComponent = graph.components[0]; 
        visitedComponent.add(targetComponent.id);
        
        //BFS
        Queue<Component> levels = new LinkedList<>();
        levels.add(targetComponent);
        while(!levels.isEmpty()) {
        	Component c = levels.poll();
        	for(Edge e: c.edges) {
        		if(visitedEdge.contains(e.id)) continue; //edge has been visited before
        		
        		visitedEdge.add(e.id);
        		Component otherEnd = e.getOtherComponent(c);
        		if(visitedComponent.contains(otherEnd.id)) continue; //component has been visited before
        		
        		//not visited the component yet, add to queue to traverse
        		visitedComponent.add(otherEnd.id);
        		levels.add(otherEnd);
        	}
        }
        
        return true;
    }
    
    public static void main(String[] args) {
    	Graph AGraph = GenerateRandom.generateRandomGraph(400, 399);
    	Graph Asmall = GenerateRandom.generateRandomSubgraph(AGraph, 20, 19);
    	System.out.println(Asmall.edges.length);
   
    	List<Graph> matcher = findAllGraph(AGraph, Asmall);
    	System.out.println(matcher.size());
    	for(Graph g: matcher) {
    		System.out.println(g.edges.length);
    		Graph.printGraph(g);
    		System.out.println("----");
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
