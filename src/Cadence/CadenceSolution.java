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
	/* 
	INPUT: 
	c1 is in the big graph, and c2 is in the target graph
	c1 == c2, and c1 and c2 are the start point of both graph
	RETURN:
	1.0: AT THIS POINT: RETURN TRUE IF FOUND THE TARGET GRAPH, FALSE OTHERWISE
	2.0: Instead of return  boolean, return graph if exists, null if not found
	*/
	private static Graph SychronousBFS(Component c1, Component c2){
		if(c1 == null || c2 == null || !c1.equals(c2)) return null;
		
		//BUILD GRAPH MATERIAL
		List<Component> components = new ArrayList<>();
		List<Edge> edges = new ArrayList<>();
		
		//C1 graph
		Set<Integer> visitedComponent_1 = new HashSet<>(); //store unique ID for components
        Set<Integer> visitedEdge_1 = new HashSet<>(); //store unique ID for edges
        Queue<Component> levels_1 = new LinkedList<>();
        visitedComponent_1.add(c1.id);
        levels_1.add(c1);
        components.add(c1);
        
        //C2 graph
        Set<Integer> visitedComponent_2 = new HashSet<>(); //store unique ID for components
        Set<Integer> visitedEdge_2 = new HashSet<>(); //store unique ID for edges
        Queue<Component> levels_2 = new LinkedList<>();
        visitedComponent_2.add(c2.id);
        levels_2.add(c2);
        while(!levels_1.isEmpty() && !levels_2.isEmpty()) {
        	Component componentOne = levels_1.poll();
        	Component componentTwo = levels_2.poll();
        	//System.out.println("Component: " + componentTwo.id + " " + componentTwo.edges.size());
        	for(Edge e: componentTwo.edges) {
        		//System.out.println("Edge: " + e.id + " " + e.delay);
        		if(visitedEdge_2.contains(e.id)) continue; //edge has been visited before
        		visitedEdge_2.add(e.id);
        		
        		Component otherEndTwo = e.getOtherComponent(componentTwo);
        		if(visitedComponent_2.contains(otherEndTwo.id)) continue; //component has been visited before
        		
        		//not visited the component yet
        		
        		//1. check if componentOne contains the same otherEnd Component
        		Edge sameEdge = findSameEdge(componentOne, e, visitedEdge_1, visitedComponent_1);
        		if(sameEdge == null) return null;
        		Component otherEndOne = sameEdge.getOtherComponent(componentOne);
        		
        		//2. Add to build graph
        		components.add(otherEndOne);
        		edges.add(sameEdge);
        		
        		//3. add to queue to traverse
        		visitedComponent_1.add(otherEndOne.id);
        		visitedEdge_1.add(sameEdge.id);
        		levels_1.add(otherEndOne);
        		
        		visitedComponent_2.add(otherEndTwo.id);
        		levels_2.add(otherEndTwo);
        	}
        }
        
        if(!levels_1.isEmpty() || !levels_2.isEmpty()) {
        	return null;
        }
        
        Component[] graphC = components.toArray(new Component[components.size()]);
        Edge[] graphE = edges.toArray(new Edge[edges.size()]);
        
        //System.out.println("END");
        return new Graph(graphC, graphE);
	}
	
	//TODO: UPDATE TO DFS OR ADD MULTITHREAD
	private static void SychronousBFS2(Component c1, Component c2, 
			List<Graph> result, List<Component> components, List<Edge> edges, 
			Set<Integer> visitedComponent_1, Set<Integer> visitedEdge_1, Set<Integer> visitedComponent_2, Set<Integer> visitedEdge_2){
		if(c1 == null || c2 == null || !c1.equals(c2)) return;
		
		//C1 graph
        Queue<Component> levels_1 = new LinkedList<>();
        visitedComponent_1.add(c1.id);
        levels_1.add(c1);
        components.add(c1);
        
        //C2 graph
        Queue<Component> levels_2 = new LinkedList<>();
        visitedComponent_2.add(c2.id);
        levels_2.add(c2);
        while(!levels_1.isEmpty() && !levels_2.isEmpty()) {
        	Component componentOne = levels_1.poll();
        	Component componentTwo = levels_2.poll();
        	
        	for(Edge e: componentTwo.edges) {
        		if(visitedEdge_2.contains(e.id)) continue; //edge has been visited before
        		visitedEdge_2.add(e.id);
        		
        		Component otherEndTwo = e.getOtherComponent(componentTwo);
        		if(visitedComponent_2.contains(otherEndTwo.id)) continue; //component has been visited before
        		
        		//not visited the component yet
        		
        		//1. check if componentOne contains the same otherEnd Component
        		List<Edge> sameEdge = findSameEdge2(componentOne, e, visitedEdge_1, visitedComponent_1);
        		if(sameEdge.size() == 0) return;
        		else if(sameEdge.size() > 1) {
        			for(int i=1; i<sameEdge.size(); i++) {
        				Component otherEnd = sameEdge.get(i).getOtherComponent(componentOne);
        				components.add(otherEnd);
                		edges.add(sameEdge.get(i));
                		
                		visitedComponent_1.add(otherEnd.id);
                		visitedEdge_1.add(sameEdge.get(i).id);
                		visitedComponent_2.add(otherEndTwo.id);
 
        				SychronousBFS2(otherEnd, otherEndTwo, result, new ArrayList<>(components), new ArrayList<>(edges), 
        						new HashSet<>(visitedComponent_1), new HashSet<>(visitedEdge_1), new HashSet<>(visitedComponent_2), new HashSet<>(visitedEdge_2));
        			}
        		}
        		Component otherEndOne = sameEdge.get(0).getOtherComponent(componentOne);
        		
        		//2. Add to build graph
        		components.add(otherEndOne);
        		edges.add(sameEdge.get(0));
        		
        		//3. add to queue to traverse
        		visitedComponent_1.add(otherEndOne.id);
        		visitedEdge_1.add(sameEdge.get(0).id);
        		levels_1.add(otherEndOne);
        		
        		visitedComponent_2.add(otherEndTwo.id);
        		levels_2.add(otherEndTwo);
        	}
        }
        
        if(!levels_1.isEmpty() || !levels_2.isEmpty()) {
        	return;
        }
        
        Component[] graphC = components.toArray(new Component[components.size()]);
        Edge[] graphE = edges.toArray(new Edge[edges.size()]);
        result.add(new Graph(graphC, graphE));
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
    	Graph Asmall = GenerateRandom.generateRandomSubgraph(AGraph, 3, 2);
    	System.out.println(Asmall.edges.length);
   
    	List<Graph> matcher = findAllGraph(AGraph, Asmall);
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
