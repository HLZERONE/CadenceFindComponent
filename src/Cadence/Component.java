package Cadence;

public class Component{
	int id; //IMPORTANT: NEED TO BE UNIQUE!!
	int resource; //1 <= R <= 8
	int density; //1 <= D <= 16
	
	public Component(int id, int resource, int density) {
		this.id = id;
		this.setResource(resource);
		this.setDensity(density);
	}
	
	//getter
	public int getResource() {
		return this.resource;
	}
	
	public int getDensity() {
		return this.density;
	}
	
	//setter
	public void setDensity(int density) {
		try {
			if(density < 1 || density > 16) {
				throw new Exception("Density number " + density + " is invalid");
			}
			this.density = density;
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void setResource(int resource) {
		try {
			if(resource < 1 || resource > 8) {
				throw new Exception("Resource number " + resource + " is invalid");
			}
			this.resource = resource;
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	public boolean equals(Component otherComponent) {
		return this.density == otherComponent.getDensity() && this.resource == otherComponent.getResource();
	}
	
	public static void main(String[] args) {
		Component A = new Component(1, 2, 2);
		Component B = new Component(2, 2, 2);
		if(A.equals(B)) {
			System.out.println("the same");
		}
	}
}
