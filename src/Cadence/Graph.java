package Cadence;

import java.awt.*;
import java.util.*;
public class Graph {
	Component[] components; //each component has Resource and Density attributes
	Edge[] edges; //each edge has delay, ComponentA, ComponentB attributes
	HashMap<Integer, Component> componentHashMap; //id: component
	HashMap<Integer, Edge> edgeHashMap; //id: edge
	
	public Graph(Component[] c, Edge[] e) {
		this.components = c;
		this.edges = e;
		//build component map
		this.componentHashMap = new HashMap<>();
		for(Component component: this.components){
			componentHashMap.put(component.id, component);
		}
		//build edge map
		this.edgeHashMap = new HashMap<>();
		for(Edge edge: edges){
			edgeHashMap.put(edge.id, edge);
		}
	}

	public int numComponents(){
		return this.components.length;
	}

	public int numEdges(){
		return this.edges.length;
	}
	
	//Output: whole Component list
	public Component[] getComponents() {
		return this.components;
	}

	//Output: component map
	public Map<Integer, Component> getComponentMap() {
		return this.componentHashMap;
	}
	
	//Output: specific component by id
	public Component getComponent(int id) {
		if(!this.componentHashMap.containsKey(id)) return null;
		return this.componentHashMap.get(id);
	}
	
	//Output: whole Edge list
	public Edge[] getEdges() {
		return this.edges;
	}

	//Output: Edge Map
	public Map<Integer, Edge> getEdgesMap() {
		return this.edgeHashMap;
	}
	
	//Output: specific edge by id
	public Edge getEdge(int id) {
		if(!this.componentHashMap.containsKey(id)) return null;
		return this.edgeHashMap.get(id);
	}

	//Output: boolean, check if graph contains edge with this id
	public boolean checkEdgeById(int id){
		return this.edgeHashMap.containsKey(id);
	}

	//Output: boolean, check if graph contains component with this id
	public boolean checkComponentById(int id){
		return this.componentHashMap.containsKey(id);
	}
	
	//check if there exists same edge in graph
	public boolean checkSameEdge(Edge checkEdge) {
		for(Edge e: edges) {
			if(e.equals(checkEdge)) {
				return true;
			}
		}
		return false;
	}
	
	//check if there exists same component in graph
	public boolean checkSameComponent(Component checkComponent) {
		for(Component c : components) {
			if(c.equals(checkComponent)) {
				return true;
			}
		}
		return false;
	}
	
	//return the same edge in graph if exists, or null otherwise
	public Edge getSameEdge(Edge checkEdge) {
		for(Edge e: edges) {
			if(e.equals(checkEdge)) {
				return e;
			}
		}
		return null;
	}
	
	//return the same component in graph if exists, or null otherwise
	public Component getSameComponent(Component checkComponent) {
		for(Component c : components) {
			if(c.equals(checkComponent)) {
				return c;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		
	}
}
