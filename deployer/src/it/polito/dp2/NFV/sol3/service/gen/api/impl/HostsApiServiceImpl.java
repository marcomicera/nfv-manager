package it.polito.dp2.NFV.sol3.service.gen.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

import it.polito.dp2.NFV.sol3.service.NfvDeployerDatabase;
import it.polito.dp2.NFV.sol3.service.gen.api.ApiResponseMessage;
import it.polito.dp2.NFV.sol3.service.gen.api.HostsApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.NotFoundException;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;
import it.polito.dp2.NFV.sol3.service.gen.model.ObjectFactory;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class HostsApiServiceImpl extends HostsApiService {
	@Override
	public Response getHost(String hostId, SecurityContext securityContext) throws NotFoundException {
		// Retrieving the desired host object
		HostType retrievedHost = NfvDeployerDatabase.getHost(hostId);

		// If no host object has been found
		if (retrievedHost == null) {
			// Returning a NOT_FOUND response
			return Response.status(Status.NOT_FOUND)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "No host found with ID " + hostId))
					.build();
		}

		// Returning the corresponding host object
		return Response.ok().entity(
				// XmlRootObject wrapper
				new ObjectFactory().createHost(retrievedHost)).build();
	}

	@Override
	public Response getHosts(SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity(
				// XmlRootObject wrapper
				new ObjectFactory().createHosts(
						// Retrieving data from the NFV database
						NfvDeployerDatabase.getHosts()))
				.build();
	}
}
