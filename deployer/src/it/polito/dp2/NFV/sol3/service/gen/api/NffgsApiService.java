package it.polito.dp2.NFV.sol3.service.gen.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public abstract class NffgsApiService {
      public abstract Response addLink(String nffgId,Boolean overwrite,LinkType link,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response addNode(String nffgId,NodeType node,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response deleteLink(String nffgId,String linkId,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response deleteNode(String nffgId,String nodeId,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response deployNffg(NffgType nffg,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response getNffg(String nffgId,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response getNffgs(String since,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response getNode(String nffgId,String nodeId,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response getNodes(String nffgId,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response getReachableEntities(String nffgId,String nodeId,String relationshipTypes,String nodeLabel,SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response undeployNffg(String nffgId,SecurityContext securityContext)
      throws NotFoundException;
}
