package it.polito.dp2.NFV.sol3.service.gen.api.factories;

import it.polito.dp2.NFV.sol3.service.gen.api.ChannelsApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.impl.ChannelsApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class ChannelsApiServiceFactory {

   private final static ChannelsApiService service = new ChannelsApiServiceImpl();

   public static ChannelsApiService getChannelsApi()
   {
      return service;
   }
}
