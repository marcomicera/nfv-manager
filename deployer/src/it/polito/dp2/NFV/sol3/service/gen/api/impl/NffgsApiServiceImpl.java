package it.polito.dp2.NFV.sol3.service.gen.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import it.polito.dp2.NFV.sol3.service.NfvValidationProvider;
import it.polito.dp2.NFV.sol3.service.gen.api.ApiResponseMessage;
import it.polito.dp2.NFV.sol3.service.gen.api.NffgsApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.NotFoundException;
import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class NffgsApiServiceImpl extends NffgsApiService {
	@Override
	public Response addLink(String nffgId, Boolean overwrite, LinkType link, SecurityContext securityContext)
			throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response addNode(String nffgId, NodeType node, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response deleteLink(String nffgId, String linkId, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response deleteNode(String nffgId, String nodeId, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response deployNffg(NffgType nffg, SecurityContext securityContext) throws NotFoundException {
		// If the NF-FG object to be deployed is invalid
		if (!((new NfvValidationProvider<NffgType>()).isReadable(NffgType.class, null, null, null))) {
			// Replying with a BAD_REQUEST response
			return Response.status(Status.BAD_REQUEST)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "NF-FG object is invalid")).build();
		}

		// Deployment
		// ...

		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "Nffg added successfully")).build();
	}

	@Override
	public Response getExtendedNodes(String nffgId, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getNffg(String nffgId, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getNffgs(String since, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response getNodes(String nffgId, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response undeployNffg(String nffgId, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
