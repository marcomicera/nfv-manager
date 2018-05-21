package it.polito.dp2.NFV.lab3;

import java.util.HashSet;
import java.util.Set;

/**
 * A descriptor for an NF-FG
 *
 */
public class NffgDescriptor {
	public Set<NodeDescriptor> nodes;

	
	/**
	 * 
	 */
	public NffgDescriptor() {
		nodes = new HashSet<NodeDescriptor>();
	}


	/**
	 * @return the nodes of the NF-FG (live set)
	 */
	public Set<NodeDescriptor> getNodes() {
		return nodes;
	}


	/**
	 * @param nodes the nodes to set (live set)
	 */
	public void setNodes(Set<NodeDescriptor> nodes) {
		this.nodes = nodes;
	}
	
	
	
}
