package Cadence;

public class Edge {
	int delay; // 10, 100, 1000
	Component A; //might be null
	Component B; //might be null
	
	public Edge(Component A, Component B, int delay) {
		this.A = A;
		this.B = B;
		this.setDelay(delay);
	}
	
	public int getDelay() {
		return this.delay;
	}
	
	public Component getAComponent() {
		return A;
	}
	
	public Component getBComponent() {
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
}
