package Cadence;

public class Graph {
	Component[] components; //each component has Resource and Density attributes
	Edge[] edges; //each edge has delay, ComponentA, ComponentB attributes
	
	public Graph(Component[] c, Edge[] e) {
		this.components = c;
		this.edges = e;
	}
	
	//Output: whole Component list
	public Component[] getComponents() {
		return this.components;
	}
	
	//Output: specific component by index
	public Component getComponent(int index) {
		return this.components[index];
	}
	
	//Output: whole Edge list
	public Edge[] getEdges() {
		return this.edges;
	}
	
	//Output: specific edge by index
	public Edge getEdge(int index) {
		return this.edges[index];
	}
	
	public static void main(String[] args) {
		
	}
}
