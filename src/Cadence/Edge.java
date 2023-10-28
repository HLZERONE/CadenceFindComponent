package Cadence;

public class Edge{
	int id; //IMPORTANT: NEED TO BE UNIQUE!!
	int delay; // 10, 100, 1000
	Component A; //might be null
	Component B; //might be null
	
	public Edge(int id, Component A, Component B, int delay) {
		this.id = id;
		this.A = A;
		this.B = B;
		this.setDelay(delay);
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getDelay() {
		return this.delay;
	}
	
	public Component getComponentA() {
		return A;
	}
	
	public Component getComponentB() {
		return B;
	}
	
	public void setComponentA(Component A) {
		this.A = A;
	}
	
	public void setComponentB(Component B) {
		this.B = B;
	}
	
	public void setDelay(int delay) {
		try {
			if(delay != 10 && delay != 100 && delay != 1000) {
				throw new Exception("Delay number " + delay + 
						" is invalid. It has to be 10, 100, or 1000");
			}
			this.delay = delay;
		}catch(Exception e) {
			System.out.println(e);
		}
	}

	public boolean equals(Edge otherEdge) {
		//Check: delay == otherEdge.delay AND
		//	ComponentA == otherEdge.ComponentA && ComponentB == otherEdge.ComponentB
		//	OR Component == otherEdge.ComponentB && ComponentB == otherEdge.ComponentA
		return this.delay == otherEdge.getDelay() && 
				(A.equals(otherEdge.getComponentA()) && B.equals(otherEdge.getComponentB()) ||
				A.equals(otherEdge.getComponentB()) && B.equals(otherEdge.getComponentA()));
	}
	
}
