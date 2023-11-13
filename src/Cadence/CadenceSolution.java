package Cadence;

import java.util.*;

public class CadenceSolution {
    public boolean BFS(Graph graph){
        if(graph == null) return false;

        Set<Integer> visitedComponent = new HashSet<>(); //store unique ID for components
        Set<Integer> visitedEdge = new HashSet<>(); //store unique ID for edges

        Map<Integer, Component> componentMap = graph.getComponentMap();
        Map<Integer, Edge> edgeMap = graph.getEdgesMap();
        
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
