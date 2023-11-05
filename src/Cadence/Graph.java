package Cadence;

import java.awt.*;

public class Graph {
	Component[] components; //each component has Resource and Density attributes
	Edge[] edges; //each edge has delay, ComponentA, ComponentB attributes
	
	public Graph(Component[] c, Edge[] e) {
		this.components = c;
		this.edges = e;
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
	/*
	public static HashMap<Component, Integer> sameComponentCount(Component[] components){
		for(Component c: components){

		}
	}
	*/
	
	public static void main(String[] args) {
		
	}
}
