package it.polito.dp2.NFV.sol3.service.gen.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public abstract class HostsApiService {
	public abstract Response getHost(String hostId, SecurityContext securityContext) 
	throws NotFoundException;
	public abstract Response getHosts(SecurityContext securityContext) 
	throws NotFoundException;
	public abstract Response getHowManyHosts(SecurityContext securityContext) 
	throws NotFoundException;
}
