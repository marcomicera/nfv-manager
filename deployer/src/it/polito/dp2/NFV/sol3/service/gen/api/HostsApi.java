package it.polito.dp2.NFV.sol3.service.gen.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.polito.dp2.NFV.sol3.service.gen.api.factories.HostsApiServiceFactory;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;
import it.polito.dp2.NFV.sol3.service.gen.model.HostsType;

@Path("/hosts")

@io.swagger.annotations.Api(description = "the hosts API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-13T16:17:44.690Z")
public class HostsApi {
	private final HostsApiService delegate = HostsApiServiceFactory.getHostsApi();

	@GET
	@Path("/{host_id}")
	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves a single host object", notes = "Retrieves a single host object having the ID specified in the resource path", response = HostType.class, tags = {
			"hosts", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = HostType.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(Host) Not found", response = Void.class) })
	public Response getHost(@PathParam("host_id") String hostId, @Context SecurityContext securityContext)
			throws NotFoundException {
		return delegate.getHost(hostId, securityContext);
	}

	@GET
	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves all physical hosts in the DP2-NFV system", notes = "Retrieves a set containing all physical IN host objects in the DP2-NFV system", response = HostsType.class, tags = {
			"hosts", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = HostsType.class) })
	public Response getHosts(@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.getHosts(securityContext);
	}
}
