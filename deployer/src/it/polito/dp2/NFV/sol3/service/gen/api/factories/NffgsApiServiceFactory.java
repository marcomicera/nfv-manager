package it.polito.dp2.NFV.sol3.service.gen.api.factories;

import it.polito.dp2.NFV.sol3.service.gen.api.NffgsApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.impl.NffgsApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class NffgsApiServiceFactory {

   private final static NffgsApiService service = new NffgsApiServiceImpl();

   public static NffgsApiService getNffgsApi()
   {
      return service;
   }
}
