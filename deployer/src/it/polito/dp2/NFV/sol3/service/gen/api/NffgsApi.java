package it.polito.dp2.NFV.sol3.service.gen.api;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.swagger.annotations.ApiParam;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.service.gen.RestApplication;
import it.polito.dp2.NFV.sol3.service.gen.api.factories.NffgsApiServiceFactory;
import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgsType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodesType;
import it.polito.dp2.NFV.sol3.service.gen.model.ReachableEntitiesType;

@Path("/nffgs")

@io.swagger.annotations.Api(description = "the nffgs API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class NffgsApi extends RestApplication {
	private final NffgsApiService delegate = NffgsApiServiceFactory.getNffgsApi();

	public NffgsApi() throws ServiceException {
		super();
	}

	@POST
	@Path("/{nffg_id}/links")
	@Consumes({ "text/boolean", "application/xml" })

	@io.swagger.annotations.ApiOperation(value = "adds a single link to the NF-FG", notes = "Adds a single link in an already deployed NF-FG (the client must be able to specify what should happen if the link is already present, i.e. whether the link information should be overwritten or an error should be returned).", response = Void.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 204, message = "Link added successfully", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. The XML link object contained in the HTTP body was not valid.", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden (when overwrite = false, hence the link has not been overwritten)", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG) Not found", response = Void.class) })
	public Response addLink(@PathParam("nffg_id") String nffgId, @NotNull @QueryParam("overwrite") Boolean overwrite,
			@ApiParam(value = "Link to be inserted", required = true) LinkType link,
			@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.addLink(nffgId, overwrite, link, securityContext);
	}

	@POST
	@Path("/{nffg_id}/nodes")
	@Consumes({ "application/xml" })

	@io.swagger.annotations.ApiOperation(value = "adds a single node to the NF-FG", notes = "Adds single node in an already deployed NF-FG (it is added without links and it is also allocated on a host; if allocation is not possible, the addition of the node fails and nothing is added).", response = Void.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 204, message = "Node added successfully", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. The XML node object contained in the HTTP body was not valid.", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden (allocation constraints not satisfied)", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG) Not found", response = Void.class) })
	public Response addNode(@PathParam("nffg_id") String nffgId,
			@ApiParam(value = "Node to be inserted", required = true) NodeType node,
			@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.addNode(nffgId, node, securityContext);
	}

	@DELETE
	@Path("/{nffg_id}/links/{link_id}")

	@io.swagger.annotations.ApiOperation(value = "removes a single link", notes = "Removes a single link in an already deployed NF-FG", response = Void.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK (link deleted)", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG or link) Not found", response = Void.class) })
	public Response deleteLink(@PathParam("nffg_id") String nffgId, @PathParam("link_id") String linkId,
			@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.deleteLink(nffgId, linkId, securityContext);
	}

	@DELETE
	@Path("/{nffg_id}/nodes/{node_id}")

	@io.swagger.annotations.ApiOperation(value = "removes a single node", notes = "Removes a single node in an already deployed NF-FG (removal must be possible only if the node has no incoming links and no outgoing links; removing the node also removes its allocation).", response = Void.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK (node deleted)", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden (node is connected in some way)", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG or node) Not found", response = Void.class) })
	public Response deleteNode(@PathParam("nffg_id") String nffgId, @PathParam("node_id") String nodeId,
			@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.deleteNode(nffgId, nodeId, securityContext);
	}

	@POST

	@Consumes({ "application/xml" })

	@io.swagger.annotations.ApiOperation(value = "deploys a NF-FG", notes = "Deploys a new NF-FG into the system (when a new NF-FG is deployed, its nodes are allocated onto the IN hosts; if it is not possible to allocate all the NF-FG nodes, deployment fails, and nothing is added). If deployment is successful, deploy time is set to the current time.", response = Void.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 204, message = "No content (nffg added successfully)", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. The XML NF-FG object contained in the HTTP body was not valid.", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error: it was not possible to create the specified NF-FG.", response = Void.class) })
	public Response deployNffg(@ApiParam(value = "NF-FG to be inserted", required = true) NffgType nffg,
			@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.deployNffg(nffg, securityContext);
	}

	@GET
	@Path("/{nffg_id}/links/howmany")

	@Produces({ "text/plain" })
	@io.swagger.annotations.ApiOperation(value = "retrieves the total number of links in the specified NF-FG", notes = "Retrieves the total number of link objects deployed in this NF-FG in the DP2-NFV system", response = Integer.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Integer.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG) Not found", response = Void.class) })
	public Response getHowManyLinks(@PathParam("nffg_id") String nffgId, @Context SecurityContext securityContext)
			throws NotFoundException {
		return delegate.getHowManyLinks(nffgId, securityContext);
	}

	@GET
	@Path("/howmany")

	@Produces({ "text/plain" })
	@io.swagger.annotations.ApiOperation(value = "retrieves all total number of NF-FGs", notes = "Retrieves the total number of all NF-FG objects in the DP2-NFV system", response = Integer.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Integer.class) })
	public Response getHowManyNffgs(@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.getHowManyNffgs(securityContext);
	}

	@GET
	@Path("/{nffg_id}/nodes/howmany")

	@Produces({ "text/plain" })
	@io.swagger.annotations.ApiOperation(value = "retrieves the total number of nodes in the specified NF-FG", notes = "Retrieves the total number of nodes objects deployed in this NF-FG in the DP2-NFV system", response = Integer.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Integer.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG) Not found", response = Void.class) })
	public Response getHowManyNodes(@PathParam("nffg_id") String nffgId, @Context SecurityContext securityContext)
			throws NotFoundException {
		return delegate.getHowManyNodes(nffgId, securityContext);
	}

	@GET
	@Path("/{nffg_id}")

	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves a single NF-FG object", notes = "Retrieves all info about the NF-FG specified in the resource path", response = NffgType.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = NffgType.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG) Not found", response = Void.class) })
	public Response getNffg(@PathParam("nffg_id") String nffgId, @Context SecurityContext securityContext)
			throws NotFoundException {
		return delegate.getNffg(nffgId, securityContext);
	}

	@GET

	@Consumes({ "text/plain" })
	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves all NF-FGs", notes = "Retrieves all NF-FGs requested by end-users", response = NffgsType.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = NffgsType.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Invalid date format.", response = Void.class) })
	public Response getNffgs(@QueryParam("since") String since, @Context SecurityContext securityContext)
			throws NotFoundException {
		return delegate.getNffgs(since, securityContext);
	}

	@GET
	@Path("/{nffg_id}/nodes/{node_id}")

	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves a single node object", notes = "Retrieves a single node object having specified the NF-FG ID in which the node belongs to and its ID in the resource path", response = NodeType.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = NodeType.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG or node) Not found", response = Void.class) })
	public Response getNode(@PathParam("nffg_id") String nffgId, @PathParam("node_id") String nodeId,
			@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.getNode(nffgId, nodeId, securityContext);
	}

	@GET
	@Path("/{nffg_id}/nodes")

	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves all nodes from an NF-FG", notes = "Retrieves all node objects from an existing NF-FG", response = NodesType.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = NodesType.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG) Not found", response = Void.class) })
	public Response getNodes(@PathParam("nffg_id") String nffgId, @Context SecurityContext securityContext)
			throws NotFoundException {
		return delegate.getNodes(nffgId, securityContext);
	}

	@GET
	@Path("/{nffg_id}/nodes/{node_id}/reachableEntities")

	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "get reachable node/hosts from this ndoe", notes = "Get all nodes and hosts (with a given label) reachable from this node via one or more given relationships", response = ReachableEntitiesType.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = ReachableEntitiesType.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Invalid parameters.", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG or node) Not found", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error", response = Void.class) })
	public Response getReachableEntities(@PathParam("nffg_id") String nffgId, @PathParam("node_id") String nodeId,
			@QueryParam("relationshipTypes") String relationshipTypes, @QueryParam("nodeLabel") String nodeLabel,
			@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.getReachableEntities(nffgId, nodeId, relationshipTypes, nodeLabel, securityContext);
	}

	@DELETE
	@Path("/{nffg_id}")

	@io.swagger.annotations.ApiOperation(value = "undeploys a NF-FG", notes = "Undeploy an NF-FG (when an NF-FG is undeployed, its allocation relationships are removed).", response = Void.class, tags = {
			"nffgs", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK (NF-FG deleted)", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(NF-FG) Not found", response = Void.class) })
	public Response undeployNffg(@PathParam("nffg_id") String nffgId, @Context SecurityContext securityContext)
			throws NotFoundException {
		return delegate.undeployNffg(nffgId, securityContext);
	}
}
