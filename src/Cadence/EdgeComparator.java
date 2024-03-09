package Cadence;

import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge>{

	@Override
	public int compare(Edge a, Edge b) {
		if(a.equals(b)) return 0;
		//comparing delay
		if(a.delay != b.delay) return a.delay - b.delay;
		
		//comparing component density
		int AsmallDensity, AbigDensity;
		if(a.getComponentA().density < a.getComponentB().density) {
			AsmallDensity = a.getComponentA().density;
			AbigDensity = a.getComponentB().density;
		}else {
			AsmallDensity = a.getComponentB().density;
			AbigDensity = a.getComponentA().density;
		}
		
		int BsmallDensity, BbigDensity;
		if(b.getComponentA().density < b.getComponentB().density) {
			BsmallDensity = b.getComponentA().density;
			BbigDensity = b.getComponentB().density;
		}else {
			BsmallDensity = b.getComponentB().density;
			BbigDensity = b.getComponentA().density;
		}
		
		if(AsmallDensity != BsmallDensity) return AsmallDensity - BsmallDensity;
		if(AbigDensity != BbigDensity) return AbigDensity - BbigDensity;
		
		//comparing component resource
		int AsmallResource, AbigResource;
		if(a.getComponentA().resource < a.getComponentB().resource) {
			AsmallResource = a.getComponentA().resource;
			AbigResource = a.getComponentB().resource;
		}else {
			AsmallResource = a.getComponentB().resource;
			AbigResource = a.getComponentA().resource;
		}
		
		int BsmallResource, BbigResource;
		if(b.getComponentA().resource < b.getComponentB().resource) {
			BsmallResource = b.getComponentA().resource;
			BbigResource = b.getComponentB().resource;
		}else {
			BsmallResource = b.getComponentB().resource;
			BbigResource = b.getComponentA().resource;
		}
		
		if(AsmallResource != BsmallResource) return AsmallResource - BsmallResource;
		return AbigResource - BbigResource;
	}

}