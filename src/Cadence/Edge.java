package Cadence;

public class Edge implements Comparable<Edge>{
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

	@Override
	public int compareTo(Edge otherEdge) {
		if(this.delay != otherEdge.getDelay()) {
			return -1;
		}
		//Check: ComponentA == otherEdge.ComponentA && ComponentB == otherEdge.ComponentB
		//	OR Component == otherEdge.ComponentB && ComponentB == otherEdge.ComponentA
		if(this.A.compareTo(otherEdge.getComponentA()) + this.B.compareTo(otherEdge.getComponentB()) == 0 ||
				this.A.compareTo(otherEdge.getComponentB()) + this.B.compareTo(otherEdge.getComponentA()) == 0) {
			return 0;
		}
		return -1;
	}
}
