package it.polito.dp2.NFV.sol3.service.gen.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.polito.dp2.NFV.sol3.service.NfvDeployerDatabase;
import it.polito.dp2.NFV.sol3.service.gen.api.ApiResponseMessage;
import it.polito.dp2.NFV.sol3.service.gen.api.ChannelsApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.NotFoundException;
import it.polito.dp2.NFV.sol3.service.gen.model.ObjectFactory;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class ChannelsApiServiceImpl extends ChannelsApiService {
	@Override
	public Response getChannel(String sourceHostId, String destinationHostId, SecurityContext securityContext)
			throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getChannels(SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity(
			// XmlRootObject wrapper
			new ObjectFactory().createChannels(
				// Retrieving data from the NFV database
				NfvDeployerDatabase.getChannels()
			)
		).build();
	}
}
