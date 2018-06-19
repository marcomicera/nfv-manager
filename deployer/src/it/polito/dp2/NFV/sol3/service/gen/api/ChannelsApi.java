package it.polito.dp2.NFV.sol3.service.gen.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.service.gen.RestApplication;
import it.polito.dp2.NFV.sol3.service.gen.api.factories.ChannelsApiServiceFactory;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelType;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelsType;

@Path("/channels")

@io.swagger.annotations.Api(description = "the channels API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class ChannelsApi extends RestApplication {
	private final ChannelsApiService delegate = ChannelsApiServiceFactory.getChannelsApi();

	public ChannelsApi() throws ServiceException {
		super();
	}

	@GET
	@Path("/{source_host_id}/{destination_host_id}")
	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves a channel between two specified hosts", notes = "Retrieves a single physical channel given two physical hosts specified in the resource path", response = ChannelType.class, tags = {
			"channels", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = ChannelType.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(Channel) Not found", response = Void.class) })
	public Response getChannel(@PathParam("source_host_id") String sourceHostId,
			@PathParam("destination_host_id") String destinationHostId, @Context SecurityContext securityContext)
			throws NotFoundException {
		return delegate.getChannel(sourceHostId, destinationHostId, securityContext);
	}

	@GET
	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrievs all physical channels", notes = "Retrieves a set containing all physical channels connecting physical IN hosts", response = ChannelsType.class, tags = {
			"channels", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = ChannelsType.class) })
	public Response getChannels(@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.getChannels(securityContext);
	}
}
