package it.polito.dp2.NFV.sol3.service.gen.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import it.polito.dp2.NFV.sol3.service.NfvDeployerDatabase;
import it.polito.dp2.NFV.sol3.service.gen.api.ApiResponseMessage;
import it.polito.dp2.NFV.sol3.service.gen.api.ChannelsApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.NotFoundException;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelType;
import it.polito.dp2.NFV.sol3.service.gen.model.ObjectFactory;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class ChannelsApiServiceImpl extends ChannelsApiService {
	@Override
	public Response getChannel(String sourceHostId, String destinationHostId, SecurityContext securityContext)
			throws NotFoundException {
		// Retrieving the desired channel object
		ChannelType retrievedChannel = NfvDeployerDatabase.getChannel(sourceHostId, destinationHostId);

		// If no channel object has been found
		if (retrievedChannel == null) {
			// Returning a NOT_FOUND response
			return Response.status(Status.NOT_FOUND).entity(new ApiResponseMessage(ApiResponseMessage.ERROR,
					"No channel found between " + sourceHostId + " and " + destinationHostId)).build();
		}

		// Returning the corresponding host object
		return Response.ok().entity(
				// XmlRootObject wrapper
				new ObjectFactory().createChannel(retrievedChannel)).build();
	}

	@Override
	public Response getChannels(SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity(
				// XmlRootObject wrapper
				new ObjectFactory().createChannels(
						// Retrieving data from the NFV database
						NfvDeployerDatabase.getChannels()))
				.build();
	}
}
