package it.polito.dp2.NFV.sol3.service.gen.api.factories;

import it.polito.dp2.NFV.sol3.service.gen.api.HostsApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.impl.HostsApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class HostsApiServiceFactory {

   private final static HostsApiService service = new HostsApiServiceImpl();

   public static HostsApiService getHostsApi()
   {
      return service;
   }
}
