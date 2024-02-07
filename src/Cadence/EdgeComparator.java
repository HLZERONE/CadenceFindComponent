package Cadence;

import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge>{

	@Override
	public int compare(Edge a, Edge b) {
		if(a.equals(b)) return 0;
		if(a.delay != b.delay) return a.delay - b.delay;
		
		int aDensity = a.getComponentA().density + a.getComponentB().density;
		int bDensity = b.getComponentA().density + b.getComponentB().density;
		if(aDensity != bDensity) return aDensity - bDensity;
		
		int aResource = a.getComponentA().resource + a.getComponentB().resource;
		int bResource = b.getComponentA().resource + b.getComponentB().resource;
		return aResource - bResource;
	}

}