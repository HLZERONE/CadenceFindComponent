package Cadence;

import java.util.Comparator;

public class ComponentComparator implements Comparator<Component>{

	@Override
	public int compare(Component a, Component b) {
		if(a.equals(b)) return 0;
		if(a.density != b.density) return a.density - b.density;
		return a.resource - b.resource;
	}

}
