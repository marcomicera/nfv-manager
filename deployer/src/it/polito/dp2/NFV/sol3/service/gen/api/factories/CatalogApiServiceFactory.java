package it.polito.dp2.NFV.sol3.service.gen.api.factories;

import it.polito.dp2.NFV.sol3.service.gen.api.CatalogApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.impl.CatalogApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-13T16:17:44.690Z")
public class CatalogApiServiceFactory {

	private final static CatalogApiService service = new CatalogApiServiceImpl();

	public static CatalogApiService getCatalogApi() {
		return service;
	}
}
