package it.polito.dp2.NFV.lab3;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SeedSetter {
	
	private Client c;
	private WebTarget target;    

    public SeedSetter() {
	        // create Client and WebTarget
	        c = ClientBuilder.newClient();
	        String url = System.getProperty("it.polito.dp2.NFV.lab3.Neo4JSimpleXMLURL");
	        target = c.target(url);
	}
    
    public void setSeed(long seed) {
        Response response;
        response = target.path("seed")
					.request()
					.put(Entity.entity(seed, MediaType.TEXT_PLAIN));
        if (response.getStatus() !=204) {
        	System.out.println("Operation Failed (status code:"+response.getStatus()+")");
        	System.exit(1);
        }
    }
	        
    public static void main(String[] args) {
		// read seed property
		Long seedObj = Long.getLong("it.polito.dp2.NFV.Random.seed");
		if (seedObj == null)
			seedObj = new Long(12345); // default value
		// and set seed
		SeedSetter s = new SeedSetter();
		s.setSeed(seedObj);
    }
		  
}
