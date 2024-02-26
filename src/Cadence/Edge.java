package Cadence;

/**
 * @brief Represents an edge in a graph connecting two components.
 * 
 *        This class models an edge in a graph connecting two components. Each
 *        edge has a unique id, delay, and references to two components (A and
 *        B).
 *        Delay values are limited to [10, 100, 1000].
 */
public class Edge {
	int id; // IMPORTANT: NEED TO BE UNIQUE!!
	int delay; // 10, 100, 1000
	Component A; // might be null
	Component B; // might be null

	/**
	 * Constructs an edge with the specified id, components, and delay.
	 * 
	 * @param id    Unique id of the edge.
	 * @param A     The first component connected by this edge.
	 * @param B     The second component connected by this edge.
	 * @param delay The delay value of the edge (10, 100, 1000).
	 */
	public Edge(int id, Component A, Component B, int delay) {
		this.id = id;
		this.A = A;
		this.B = B;
		this.setDelay(delay);
		A.addEdge(this);
		B.addEdge(this);
	}

	/**
	 * Get the unique id of the edge.
	 * 
	 * @return The unique id of the edge.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Get the delay value of the edge.
	 * 
	 * @return The delay value of the edge (10, 100, 1000).
	 */
	public int getDelay() {
		return this.delay;
	}

	/**
	 * Get the first component connected by this edge.
	 * 
	 * @return The first component connected by this edge.
	 */
	public Component getComponentA() {
		return A;
	}

	/**
	 * Get the second component connected by this edge.
	 * 
	 * @return The second component connected by this edge.
	 */
	public Component getComponentB() {
		return B;
	}

	// get the other one Component
	/**
	 * Get the other component connected to a given component by this edge.
	 * 
	 * @param c The component whose neighbor you want to find.
	 * @return The other component connected to the given component by this edge.
	 */
	public Component getOtherComponent(Component c) {
		if (c.id == A.id)
			return B;
		return A;
	}

	/**
	 * Set the first component connected by this edge.
	 * 
	 * @param A The new first component connected by this edge.
	 */
	public void setComponentA(Component A) {
		this.A = A;
	}

	/**
	 * Set the second component connected by this edge.
	 * 
	 * @param B The new second component connected by this edge.
	 */
	public void setComponentB(Component B) {
		this.B = B;
	}

	/**
	 * Set the delay value of the edge.
	 * 
	 * @param delay The new delay value of the edge (10, 100, 1000).
	 */
	public void setDelay(int delay) {
		try {
			if (delay != 10 && delay != 100 && delay != 1000) {
				throw new Exception("Delay number " + delay +
						" is invalid. It has to be 10, 100, or 1000");
			}
			this.delay = delay;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Check if two edges are equal based on their delay and connected components.
	 * 
	 * @param otherEdge The edge to compare to.
	 * @return True if the edges are equal, false otherwise.
	 */
	public boolean equals(Edge otherEdge) {
		// Check: delay == otherEdge.delay AND
		// ComponentA == otherEdge.ComponentA && ComponentB == otherEdge.ComponentB
		// OR Component == otherEdge.ComponentB && ComponentB == otherEdge.ComponentA
		return this.delay == otherEdge.getDelay() &&
				(A.equals(otherEdge.getComponentA()) && B.equals(otherEdge.getComponentB()) ||
						A.equals(otherEdge.getComponentB())&& B.equals(otherEdge.getComponentA()));
	}

	// clone the current edge
	/**
	 * Clones the current edge.
	 * 
	 * @return A new edge with the same id, connected components, and delay.
	 */
	public Edge clone() {
		return new Edge(this.id, this.A.clone(), this.B.clone(), this.delay);
	}
	
	public void print() {
		System.out.println("Edge ID: " + this.id + " Delay: " + this.delay);
		A.print();
		B.print();
	}

	public static void main(String[] args) {
		Component A = new Component(1, 2, 2);
		Component B = new Component(2, 2, 2);

		Edge e = new Edge(1, A, B, 10);
	}

}
