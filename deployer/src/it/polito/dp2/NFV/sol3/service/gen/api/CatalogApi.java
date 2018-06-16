package it.polito.dp2.NFV.sol3.service.gen.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.polito.dp2.NFV.sol3.service.gen.api.factories.CatalogApiServiceFactory;
import it.polito.dp2.NFV.sol3.service.gen.model.CatalogType;

@Path("/catalog")

@io.swagger.annotations.Api(description = "the catalog API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-13T16:17:44.690Z")
public class CatalogApi {
	private final CatalogApiService delegate = CatalogApiServiceFactory.getCatalogApi();

	@GET
	@Produces({ "application/xml" })
	@io.swagger.annotations.ApiOperation(value = "retrieves the entire catalog of functional types", notes = "Retrieves a set containing all Virtual Network Function (VNF) type objects", response = CatalogType.class, tags = {
			"catalog", })
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = CatalogType.class) })
	public Response getCatalog(@Context SecurityContext securityContext) throws NotFoundException {
		return delegate.getCatalog(securityContext);
	}
}
