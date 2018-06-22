package it.polito.dp2.NFV.sol3.service.gen.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.xml.datatype.DatatypeConfigurationException;

import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.LinkAlreadyPresentException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;
import it.polito.dp2.NFV.sol3.service.database.LinkManager;
import it.polito.dp2.NFV.sol3.service.database.MalformedException;
import it.polito.dp2.NFV.sol3.service.database.NffgManager;
import it.polito.dp2.NFV.sol3.service.database.NodeManager;
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
		// Deploying the link
		try {
			LinkManager.deployLink(nffgId, link, overwrite);
		} catch (AllocationException exception) {
			// Replying with a NOT_FOUND response
			return Response.status(Status.NOT_FOUND)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, exception.getLocalizedMessage())).build();
		} catch (UnknownEntityException | MalformedException exception) {
			// Replying with a BAD_REQUEST response
			return Response.status(Status.BAD_REQUEST)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, exception.getLocalizedMessage())).build();
		} catch (LinkAlreadyPresentException exception) {
			// Replying with a FORBIDDEN response
			return Response.status(Status.FORBIDDEN)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, exception.getLocalizedMessage())).build();
		}

		// Returning a NO_CONTENT response
		return Response.status(Status.NO_CONTENT)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Link added successfully")).build();
	}

	@Override
	public Response addNode(String nffgId, NodeType node, SecurityContext securityContext) throws NotFoundException {
		// Deploying the node
		try {
			NodeManager.deployNode(nffgId, node);
		} catch (MalformedException | UnknownEntityException exception) {
			// Replying with a BAD_REQUEST response
			return Response.status(Status.BAD_REQUEST)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, exception.getLocalizedMessage())).build();
		} catch (AllocationException exception) {
			// Replying with a INTERNAL_SERVER_ERROR response
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, exception.getLocalizedMessage())).build();
		}

		// Returning a NO_CONTENT response
		return Response.status(Status.NO_CONTENT)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Node added successfully")).build();
	}

	@Override
	public Response deleteLink(String nffgId, String linkId, SecurityContext securityContext) throws NotFoundException {
		// Returning a NOT_IMPLEMENTED response
		return Response.status(Status.NOT_IMPLEMENTED)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Feature not implemented yet.")).build();
	}

	@Override
	public Response deleteNode(String nffgId, String nodeId, SecurityContext securityContext) throws NotFoundException {
		// Returning a NOT_IMPLEMENTED response
		return Response.status(Status.NOT_IMPLEMENTED)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Feature not implemented yet.")).build();
	}

	@Override
	public Response deployNffg(NffgType nffg, SecurityContext securityContext) throws NotFoundException {
		// Deploying the NF-FG object
		try {
			NffgManager.deploy(nffg);
		} catch (MalformedException | UnknownEntityException exception) {
			// Replying with a BAD_REQUEST response
			return Response.status(Status.BAD_REQUEST)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, exception.getLocalizedMessage())).build();
		} catch (AllocationException | DatatypeConfigurationException exception) {
			// Replying with a INTERNAL_SERVER_ERROR response
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new ApiResponseMessage(ApiResponseMessage.ERROR, exception.getLocalizedMessage())).build();
		}

		// Returning a NO_CONTENT response
		return Response.status(Status.NO_CONTENT)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Nffg added successfully")).build();
	}

	@Override
	public Response getExtendedNodes(String nffgId, SecurityContext securityContext) throws NotFoundException {
		// TODO IMPLEMENT!

		// Returning a NOT_IMPLEMENTED response
		return Response.status(Status.NOT_IMPLEMENTED)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Feature not implemented yet.")).build();
	}

	@Override
	public Response getNffg(String nffgId, SecurityContext securityContext) throws NotFoundException {
		// TODO IMPLEMENT!

		// Returning a NOT_IMPLEMENTED response
		return Response.status(Status.NOT_IMPLEMENTED)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Feature not implemented yet.")).build();
	}

	@Override
	public Response getNffgs(String since, SecurityContext securityContext) throws NotFoundException {
		// TODO since checks

		// TODO IMPLEMENT!

		// Returning a NOT_IMPLEMENTED response
		return Response.status(Status.NOT_IMPLEMENTED)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Feature not implemented yet.")).build();
	}

	@Override
	public Response getNodes(String nffgId, SecurityContext securityContext) throws NotFoundException {
		// TODO IMPLEMENT!

		// Returning a NOT_IMPLEMENTED response
		return Response.status(Status.NOT_IMPLEMENTED)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Feature not implemented yet.")).build();
	}

	@Override
	public Response undeployNffg(String nffgId, SecurityContext securityContext) throws NotFoundException {
		// Returning a NOT_IMPLEMENTED response
		return Response.status(Status.NOT_IMPLEMENTED)
				.entity(new ApiResponseMessage(ApiResponseMessage.OK, "Feature not implemented yet.")).build();
	}
}
