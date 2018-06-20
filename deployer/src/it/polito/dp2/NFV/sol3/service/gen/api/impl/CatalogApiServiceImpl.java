package it.polito.dp2.NFV.sol3.service.gen.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.polito.dp2.NFV.sol3.service.database.NfvDatabase;
import it.polito.dp2.NFV.sol3.service.gen.api.CatalogApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.NotFoundException;
import it.polito.dp2.NFV.sol3.service.gen.model.ObjectFactory;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class CatalogApiServiceImpl extends CatalogApiService {
	@Override
	public Response getCatalog(SecurityContext securityContext) throws NotFoundException {
		return Response.ok().entity(
			// XmlRootObject wrapper
			new ObjectFactory().createCatalog(
				// Retrieving data from the NFV database
				NfvDatabase.getCatalog()
			)
		).build();
	}
}
