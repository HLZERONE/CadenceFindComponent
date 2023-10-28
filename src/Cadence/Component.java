package Cadence;

public class Component implements Comparable<Component>{
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
	
	@Override
	public int compareTo(Component otherComponent) {
		if(this.density != otherComponent.getDensity()) {
			return -1;
		}
		if(this.resource == otherComponent.getResource()) {
			return -1;
		}
		return 0;
	}
	
	
}
