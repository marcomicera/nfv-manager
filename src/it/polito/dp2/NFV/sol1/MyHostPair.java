package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.HostReader;

/**
 * Wrapper key-class used for mapping channels
 *
 */
public class MyHostPair {
	private final HostReader host1, host2;

	public MyHostPair(HostReader host1, HostReader host2) {
		this.host1 = host1;
		this.host2 = host2;
	}

	@Override
	public int hashCode() {
		/*TODO delete*/System.out.println("hashCode() function called on " + this + " (bucket is: " + (host1.getName().hashCode() + host2.getName().hashCode()) + ")");
		
		return (host1.getName().hashCode() + host2.getName().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof MyHostPair)) return false;
		
		MyHostPair otherPair = (MyHostPair)o;
		/*TODO delete*/System.out.println("equals() function called b/w " + otherPair + " and " + this);
		return (
			(	host1.getName().compareTo(otherPair.host1.getName()) == 0  
					&& 
				host2.getName().compareTo(otherPair.host2.getName()) == 0
			) || (
				host1.getName().compareTo(otherPair.host2.getName()) == 0
					&& 
				host2.getName().compareTo(otherPair.host1.getName()) == 0 
			)
		);
	}

	@Override
	public String toString() {
		return	"<" + 
				host1.getName() + ", " +
				host2.getName() + ">";
	}
}