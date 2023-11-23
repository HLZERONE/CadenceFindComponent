package Cadence;

import java.util.*;

public class CadenceSolution {
	//TODO: Function that find the target start component in Big Graph, once found, call SychronousBFS
	
	/*
	INPUT: 
	c1 is in the big graph, and c2 is in the target graph
	c1 == c2, and c1 and c2 are the start point of both graph
	RETURN:
	AT THIS POINT: RETURN TRUE IF FOUND THE TARGET GRAPH, FALSE OTHERWISE
	TODO: Instead of return boolean, return a list of edge and component -> graph
	*/
	public boolean SychronousBFS(Component c1, Component c2){
		if(c1 == null || c2 == null || !c1.equals(c2)) return false;
		//C1 graph
		Set<Integer> visitedComponent_1 = new HashSet<>(); //store unique ID for components
        Set<Integer> visitedEdge_1 = new HashSet<>(); //store unique ID for edges
        Queue<Component> levels_1 = new LinkedList<>();
        visitedComponent_1.add(c1.id);
        levels_1.add(c1);
        
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
        		Component otherEndOne = findOtherSideComponent(componentOne, otherEndTwo, visitedEdge_1, visitedComponent_1);
        		if(otherEndOne == null) return false;
        		
        		//2. add to queue to traverse
        		levels_1.add(otherEndOne);
        		visitedComponent_2.add(otherEndTwo.id);
        		levels_2.add(otherEndTwo);
        	}
        }
        
        
        return levels_1.isEmpty() && levels_2.isEmpty();
	}
	
	
	//Find otherSide Component that not been visited before (FOR SychronousBFS()) and add that edge and component id to set
	private Component findOtherSideComponent(Component InputComponent, Component targetComponent, Set<Integer> visitedEdge, Set<Integer> visitedComponent) {
		for(Edge e: InputComponent.edges) {
			if(visitedEdge.contains(e.id)) continue;
			
			Component otherSide = e.getOtherComponent(InputComponent);
			if(otherSide.equals(targetComponent) && !visitedComponent.contains(otherSide.id)){
				visitedEdge.add(e.id);
				visitedComponent.add(otherSide.id);
				return otherSide;
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
