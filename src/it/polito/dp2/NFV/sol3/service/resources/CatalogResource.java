package it.polito.dp2.NFV.sol3.service.resources;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;

/**
 * Resource class hosted at the URI relative path "/catalog"
 */
@Path("/catalog")
@Api(value = "/catalog", description = "a collection of supported network function types")
public class CatalogResource {
	public String getCatalogsXML() {
		
	}
}
