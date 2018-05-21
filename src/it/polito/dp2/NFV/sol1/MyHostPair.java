package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.HostReader;

/**
 * Wrapper key-class used for mapping channels
 */
public class MyHostPair {
	private final HostReader host1, host2;

	public MyHostPair(HostReader host1, HostReader host2) {
		if(host1 == null || host2 == null) {
			System.err.println("At least one host is null");
			System.exit(1);
		}
		
		this.host1 = host1;
		this.host2 = host2;
	}

	/**
	 * Hashing function used by the java.util.Map's get() function
	 */
	@Override
	public int hashCode() {
		return (host1.getName() + host2.getName()).hashCode();
	}

	/**
	 * Equality check function used by the java.util.Map's get() function
	 */
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof MyHostPair)) return false;
		
		MyHostPair otherPair = (MyHostPair)o;
		
		return	host1.getName().compareTo(otherPair.host1.getName()) == 0  
					&& 
				host2.getName().compareTo(otherPair.host2.getName()) == 0;
	}

	/**
	 * Pretty-printing
	 */
	@Override
	public String toString() {
		return	"<" + host1.getName() + ", " + host2.getName() + ">";
	}
}