package it.polito.dp2.NFV.sol3.service.gen.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;
import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;
import java.util.List;
import javax.validation.constraints.*;
import io.swagger.annotations.*;


@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class ExtendedNodeType   {
  
  private String id = null;
  private String functionalType = null;
  private String host = null;
  private List<LinkType> links = new ArrayList<LinkType>();
  private List<HostType> reachableHosts = new ArrayList<HostType>();

  /**
   **/
  
  @ApiModelProperty(example = "WEBSERVERt4Nffg2", required = true, value = "")
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  
  @ApiModelProperty(example = "DPIa", required = true, value = "")
  @JsonProperty("functionalType")
  @NotNull
  public String getFunctionalType() {
    return functionalType;
  }
  public void setFunctionalType(String functionalType) {
    this.functionalType = functionalType;
  }

  /**
   **/
  
  @ApiModelProperty(example = "H7", value = "")
  @JsonProperty("host")
  public String getHost() {
    return host;
  }
  public void setHost(String host) {
    this.host = host;
  }

  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("links")
  @NotNull
  public List<LinkType> getLinks() {
    return links;
  }
  public void setLinks(List<LinkType> links) {
    this.links = links;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("reachableHosts")
  public List<HostType> getReachableHosts() {
    return reachableHosts;
  }
  public void setReachableHosts(List<HostType> reachableHosts) {
    this.reachableHosts = reachableHosts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtendedNodeType extendedNodeType = (ExtendedNodeType) o;
    return Objects.equals(id, extendedNodeType.id) &&
        Objects.equals(functionalType, extendedNodeType.functionalType) &&
        Objects.equals(host, extendedNodeType.host) &&
        Objects.equals(links, extendedNodeType.links) &&
        Objects.equals(reachableHosts, extendedNodeType.reachableHosts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, functionalType, host, links, reachableHosts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtendedNodeType {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    functionalType: ").append(toIndentedString(functionalType)).append("\n");
    sb.append("    host: ").append(toIndentedString(host)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("    reachableHosts: ").append(toIndentedString(reachableHosts)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

