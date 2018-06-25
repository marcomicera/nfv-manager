package it.polito.dp2.NFV.sol3.service.gen.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public abstract class ChannelsApiService {
      public abstract Response getChannel(String sourceHostId,String destinationHostId,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response getChannels(SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response getHowManyChannels(SecurityContext securityContext)
      throws NotFoundException;
}
