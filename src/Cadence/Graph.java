package Cadence;

import java.awt.*;
import java.util.*;

/**
 * @brief Represents a graph with components and edges.
 * 
 *        This class models a graph structure consisting of components (nodes)
 *        and edges (connections between nodes).
 *        Each component has associated resource and density attributes. Each
 *        edge has a delay and connects two components.
 *        Component attributes:
 *        Resource range: [1 - 8]
 * 	  Density range: [1 - 16]
 *        Edge attributes:
 * 	  Delay attribute range: [10, 100, 1000]
 */
public class Graph {
	/**
	 * Array of components in the graph.
	 * Each component has Resource and Density attributes.
	 */
	final Component[] components;

	/**
	 * Array of edges in the graph.
	 * Each edge has Delay, ComponentA, ComponentB attributes.
	 */
	final Edge[] edges;

	/**
	 * Map of components in the graph.
	 * Key: component id
	 * Value: component
	 */
	final HashMap<Integer, Component> componentHashMap;

	/**
	 * Map of edges in the graph.
	 * Key: edge id
	 * Value: edge
	 */
	final HashMap<Integer, Edge> edgeHashMap;

	/**
	 * Constructs a graph with the specified components and edges.
	 * 
	 * @param c The components in the graph.
	 * @param e The edges in the graph.
	 */
	
	/**
	 * The components and edges array has been sorted or not
	 * Be used at equal() function
	 */
	boolean beenSort = false;
	
	public Graph(Component[] c, Edge[] e) {
		this.components = c;
		this.edges = e;
		// build component map
		this.componentHashMap = new HashMap<>();
		for (Component component : this.components) {
			componentHashMap.put(component.id, component);
		}
		// build edge map
		this.edgeHashMap = new HashMap<>();
		for (Edge edge : edges) {
			edgeHashMap.put(edge.id, edge);
		}
	}

	/**
	 * Gets the number of components in the graph.
	 * 
	 * @return The number of components in the graph.
	 */
	public int numComponents() {
		return this.components.length;
	}

	/**
	 * Gets the number of edges in the graph.
	 * 
	 * @return The number of edges in the graph.
	 */
	public int numEdges() {
		return this.edges.length;
	}


	/**
	 * Returns the array of all components in the graph.
	 * 
	 * @return An array of component objects.
	 */

	public Component[] getComponents() {
		return this.components;
	}

	// Output: component map
	/**
	 * Returns the map of all components in the graph.
	 * 
	 * @return A map of component objects indexed by ID.
	 */
	public Map<Integer, Component> getComponentMap() {
		return this.componentHashMap;
	}

	/**
	 * Returns the component with the specified ID.
	 * 
	 * @param id The ID of the component to return.
	 * @return The component with the specified ID, or null if there is no such component.
	 */
	public Component getComponent(int id) {
		if (!this.componentHashMap.containsKey(id))
			return null;
		return this.componentHashMap.get(id);
	}

	/**
	 * Returns the array of all edges in the graph.
	 * 
	 * @return An array of edge objects.
	 */
	public Edge[] getEdges() {
		return this.edges;
	}

	/**
	 * Returns the map of all edges in the graph.
	 * 
	 * @return A map of edge objects indexed by ID.
	 */
	public Map<Integer, Edge> getEdgesMap() {
		return this.edgeHashMap;
	}

	/**
	 * Returns the edge with the specified ID.
	 * 
	 * @param id The ID of the edge to return.
	 * @return The edge with the specified ID, or null if there is no such edge.
	 */
	public Edge getEdge(int id) {
		if (!this.componentHashMap.containsKey(id))
			return null;
		return this.edgeHashMap.get(id);
	}

	/**
	 * Checks if the graph contains an edge with the specified ID.
	 * 
	 * @param id The ID of the edge to check.
	 * @return True if the graph contains an edge with the specified ID, false otherwise.
	 */
	public boolean checkEdgeById(int id) {
		return this.edgeHashMap.containsKey(id);
	}

	/**
	 * Checks if the graph contains a component with the specified ID.
	 * @param id The ID of the component to check.
	 * @return True if the graph contains a component with the specified ID, false otherwise.
	 */
	public boolean checkComponentById(int id) {
		return this.componentHashMap.containsKey(id);
	}

	/**
	 * Checks if the graph contains an edge with the same ID as the specified edge.
	 * @param checkEdge The edge to check.
	 * @return True if the graph contains an edge with the same ID as the specified edge, false otherwise.
	 */
	public boolean checkSameEdge(Edge checkEdge) {
		for (Edge e : edges) {
			if (e.equals(checkEdge)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the graph contains a component with the same ID as the specified component.
	 * @param checkComponent The component to check.
	 * @return True if the graph contains a component with the same ID as the specified component, false otherwise.
	 */
	public boolean checkSameComponent(Component checkComponent) {
		for (Component c : components) {
			if (c.equals(checkComponent)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the edge with the same ID as the specified edge.
	 * @param checkEdge The edge to check.
	 * @return The edge with the same ID as the specified edge, or null if there is no such edge.
	 */
	public Edge getSameEdge(Edge checkEdge) {
		for (Edge e : edges) {
			if (e.equals(checkEdge)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Returns the component with the same ID as the specified component.
	 * @param checkComponent The component to check.
	 * @return The component with the same ID as the specified component, or null if there is no such component.
	 */
	public Component getSameComponent(Component checkComponent) {
		for (Component c : components) {
			if (c.equals(checkComponent)) {
				return c;
			}
		}
		return null;
	}
	
	public void sort() {
		Arrays.sort(components, new ComponentComparator());
		Arrays.sort(edges, new EdgeComparator());
		beenSort = true;
	}
	
	public boolean equals(Graph otherG) {
		if(this.components.length != otherG.components.length || this.edges.length != otherG.edges.length) {
			System.out.println("Unequal Length:");
			System.out.println("Component Length: " + this.components.length + " " + otherG.components.length);
			System.out.println("Edge Length: " + this.edges.length + " " + otherG.edges.length);
			
			return false;
		}
		
		//Check if sort yet
		if(!beenSort) {
			sort();
		}
		if(!otherG.beenSort) {
			otherG.sort();
		}
		
		//Check if Components equal
		for(int i=0; i<components.length; i++) {
			if(!components[i].equals(otherG.components[i])) {
				System.out.println("The not equal Component: ");
				components[i].print();
				otherG.components[i].print();
				return false;
			}
		}
		
		//Check if Edges equal
		for(int i=0; i<edges.length; i++) {
			if(!edges[i].equals(otherG.edges[i])) {
				System.out.println("The not equal Edge: ");
				edges[i].print();
				otherG.edges[i].print();
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Prints the graph to the console.
	 * @param g The graph to print.
	 */
	public static void printGraph(Graph g) {
//		for(Component c: g.components) {
//			c.print();
//		}
		for (Edge e : g.edges) {
			e.print();
		}
	}
	
	
	
	public static void main(String[] args) {

	}
}
