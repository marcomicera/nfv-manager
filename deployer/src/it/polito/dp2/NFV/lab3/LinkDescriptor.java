package it.polito.dp2.NFV.lab3;

import it.polito.dp2.NFV.LinkReader;

/**
 * A descriptor for a link in an NF-FG
 *
 */
public class LinkDescriptor {
	private NodeDescriptor sourceNode; //a Descriptor for the source node of the link
	private NodeDescriptor destinationNode; // a Descriptor for the destination node of the link
	private float throughput; // the required minimum throughput (expressed in Mbps) or 0 if it is not specified
	private int latency; // the required maximum latency (expressed in ms) or 0 if it is not specified
	
	public LinkDescriptor() {
	}

	/**
	 * @return the sourceNode (a Descriptor for the source node of the link)
	 */
	public NodeDescriptor getSourceNode() {
		return sourceNode;
	}

	/**
	 * @param sourceNode the sourceNode to set
	 */
	public void setSourceNode(NodeDescriptor sourceNode) {
		this.sourceNode = sourceNode;
	}

	/**
	 * @return the destinationNode (a Descriptor for the destination node of the link)
	 */
	public NodeDescriptor getDestinationNode() {
		return destinationNode;
	}
	
	/**
	 * @param destinationNode the destinationNode to set
	 */
	public void setDestinationNode(NodeDescriptor destinationNode) {
		this.destinationNode = destinationNode;
	}

	/**
	 * @return the throughput (the required minimum throughput (expressed in Mbps) or 0 if it is not specified)
	 */
	public float getThroughput() {
		return throughput;
	}
	
	/**
	 * @param throughput the throughput to set
	 */
	public void setThroughput(float throughput) {
		this.throughput = throughput;
	}

	/**
	 * @return the latency (the required maximum latency (expressed in ms) or 0 if it is not specified)
	 */
	public int getLatency() {
		return latency;
	}

	/**
	 * @param latency the latency to set
	 */
	public void setLatency(int latency) {
		this.latency = latency;
	}
	
}
