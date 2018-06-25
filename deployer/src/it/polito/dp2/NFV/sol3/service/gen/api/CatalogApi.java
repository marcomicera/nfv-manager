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
import it.polito.dp2.NFV.sol3.service.gen.api.factories.CatalogApiServiceFactory;
import it.polito.dp2.NFV.sol3.service.gen.model.CatalogType;
import it.polito.dp2.NFV.sol3.service.gen.model.VNFType;

@Path("/catalog")

@io.swagger.annotations.Api(description = "the catalog API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class CatalogApi extends RestApplication {
	private final CatalogApiService delegate = CatalogApiServiceFactory.getCatalogApi();

	public CatalogApi() throws ServiceException {
		super();
	}

	@GET
	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves the entire catalog of functional types", notes = "Retrieves a set containing all Virtual Network Function (VNF) type objects", response = CatalogType.class, tags = {
			"catalog", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = CatalogType.class) })
	public Response getCatalog(@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.getCatalog(securityContext);
	}

	@GET
	@Path("/howmany")
	@Produces({ "text/plain" })
	@io.swagger.annotations.ApiOperation(value = "retrieves the total number of VNF types in the DP2-NFV system", notes = "Retrieves the total number of Virtual Network Function types available in the DP2-NFV system", response = Integer.class, tags = {
			"catalog", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Integer.class) })
	public Response getHowManyVnf(@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.getHowManyVnf(securityContext);
	}

	@GET
	@Path("/{vnf_id}")
	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves a single VNF type object", notes = "Retrieves a single Virtual Network Function type having the ID specified in the resource path", response = VNFType.class, tags = {
			"catalog", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = VNFType.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "(VNF) Not found", response = Void.class) })
	public Response getVnf(@PathParam("vnf_id") String vnfId, @Context SecurityContext securityContext)
			throws NotFoundException {
		return delegate.getVnf(vnfId, securityContext);
	}
}
