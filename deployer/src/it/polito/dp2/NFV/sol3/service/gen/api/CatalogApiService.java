package it.polito.dp2.NFV.sol3.service.gen.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-13T16:17:44.690Z")
public abstract class CatalogApiService {
	public abstract Response getCatalog(SecurityContext securityContext) throws NotFoundException;
}
