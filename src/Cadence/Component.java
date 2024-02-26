package Cadence;

import java.util.*;

/**
 * @brief Represents a component in a graph.
 * 
 *        This class models a component in a graph. Each component has a unique
 *        id, resource, and density.
 *        Resource range: [1 - 8]
 *        Density range: [1 - 16]
 */
public class Component {
	int id; // IMPORTANT: NEED TO BE UNIQUE!!
	int resource; // 1 <= R <= 8
	int density; // 1 <= D <= 16

	// list of edges that connect to this component
	List<Edge> edges;

	/**
	 * Constructs a component with the specified id, resource, and density. Creates
	 * new ArrayList to store edges.
	 * 
	 * @param id       Unique id of the component.
	 * @param resource Resource value of the component [1 - 8].
	 * @param density  Density value of the component [1 - 16].
	 */
	public Component(int id, int resource, int density) {
		this.id = id;
		this.edges = new ArrayList<>();
		this.setResource(resource);
		this.setDensity(density);
	}

	/**
	 * Gets the resource value of the component.
	 * 
	 * @return The resource value of the component.
	 */
	public int getResource() {
		return this.resource;
	}

	/**
	 * Gets the density value of the component.
	 * 
	 * @return The density value of the component.
	 */
	public int getDensity() {
		return this.density;
	}

	/**
	 * Sets the density value of the component.
	 * 
	 * @param density The density value of the component.
	 */
	public void setDensity(int density) {
		try {
			if (density < 1 || density > 16) {
				throw new Exception("Density number " + density + " is invalid");
			}
			this.density = density;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Sets the resource value of the component.
	 * 
	 * @param resource The resource value of the component.
	 */
	public void setResource(int resource) {
		try {
			if (resource < 1 || resource > 8) {
				throw new Exception("Resource number " + resource + " is invalid");
			}
			this.resource = resource;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Check if two components are equal based on their resource and density values.
	 * 
	 * @param otherComponent The other component to compare with.
	 * @return True if the two components are equal, false otherwise.
	 */
	public boolean equals(Component otherComponent) {
		return this.density == otherComponent.getDensity() && this.resource == otherComponent.getResource();
	}

	/**
	 * Add an edge that connects to this component.
	 * 
	 * (Note: auto adds when building an edge using this component.)
	 * 
	 * @param checkEdge The edge to add.
	 */
	public void addEdge(Edge e) {
		this.edges.add(e);
	}

	/**
	 * Clones the current component.
	 * 
	 * @return A new component with the same id, resource, and density values.
	 */
	public Component clone() {
		return new Component(this.id, this.resource, this.density);
	}
	
	public void print() {
		System.out.println("Component ID: " + this.id + " Density: " + this.density + 
				" Resource: " + this.resource + " Edge Num: " + this.edges.size());
	}

	public static void main(String[] args) {
		Component A = new Component(1, 2, 2);
		Component B = new Component(2, 2, 2);
		Set<Component> testSet = new HashSet<>();
		testSet.add(A);
		System.out.println(testSet.contains(A));
	}
}
