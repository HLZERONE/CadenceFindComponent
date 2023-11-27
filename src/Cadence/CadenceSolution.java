package Cadence;

import java.util.*;

public class CadenceSolution {
	//TODO: Function that find the target start component in Big Graph, once found, call SychronousBFS
	private List<Graph> findAllGraph(Graph searchGraph, Graph targetGraph){
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
	2.0: Instead of return boolean, return graph if exists, null if not found
	*/
	public Graph SychronousBFS(Component c1, Component c2){
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
        	
        	for(Edge e: componentTwo.edges) {
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
        		visitedComponent_1.add(otherEndTwo.id);
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
        Edge[] graphE = components.toArray(new Edge[edges.size()]);
        
        return new Graph(graphC, graphE);
	}
	
	
	//Find the not visited edge that connecting to this component and the not visited target component(FOR SychronousBFS())
	private Edge findSameEdge(Component InputComponent, Edge targetEdge, Set<Integer> visitedEdge, Set<Integer> visitedComponent) {
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

        //Map<Integer, Component> componentMap = graph.getComponentMap();
        //Map<Integer, Edge> edgeMap = graph.getEdgesMap();
        
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
    
    
    
}
