package it.polito.dp2.NFV.sol2;

import java.net.URI;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.lab2.AlreadyLoadedException;
import it.polito.dp2.NFV.lab2.ExtendedNodeReader;
import it.polito.dp2.NFV.lab2.NoGraphException;
import it.polito.dp2.NFV.lab2.ReachabilityTester;
import it.polito.dp2.NFV.lab2.ServiceException;
import it.polito.dp2.NFV.lab2.UnknownNameException;

public class ReachabilityTesterImpl implements ReachabilityTester {
	/*TODO testing*/
	public static void main(String[] args) {
		// Building the JAX-RS client 
		Client client = ClientBuilder.newClient();
		
		// Web target
		WebTarget target = client.target(getBaseURI());
		
		String test = target.path("currentNodes")
							.request()
							.accept(MediaType.TEXT_PLAIN_TYPE)
							.get(String.class);
		
		System.out.println(test);
	}

	@Override
	public void loadGraph(String nffgName) 
			throws UnknownNameException, AlreadyLoadedException, ServiceException {
		// TODO Auto-generated method stub
		return;
	}
	
	private static URI getBaseURI() {
	    return UriBuilder.fromUri("http://localhost:8080/Neo4JSimpleXML/webapi/data/").build();
	}

	@Override
	public Set<ExtendedNodeReader> getExtendedNodes(String nffgName)
			throws UnknownNameException, NoGraphException, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLoaded(String nffgName) throws UnknownNameException {
		// TODO Auto-generated method stub
		return false;
	}

}
