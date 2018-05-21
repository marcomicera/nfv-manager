package it.polito.dp2.NFV.lab3;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;

/**
 * A descriptor for a node in an NF-FG
 *
 */
public class NodeDescriptor {
	private VNFTypeReader funcType;
	private Set<LinkDescriptor> links;
	private String hostName;
	
	public NodeDescriptor() {
		links = new HashSet<LinkDescriptor>();
	}

	/**
	 * @return the functional type of the node
	 */
	public VNFTypeReader getFuncType() {
		return funcType;
	}
	
	/**
	 * @param funcType the functional type to set
	 */
	public void setFuncType(VNFTypeReader funcType) {
		this.funcType = funcType;
	}
	
	/**
	 * @return the links of the node (live set)
	 */
	public Set<LinkDescriptor> getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(Set<LinkDescriptor> links) {
		this.links = links;
	}

	/**
	 * @return the host name requested for allocation of this node
	 */
	public String getHostName() {
		return hostName;
	}
	
	/**
	 * @param hostName the host name to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
}
