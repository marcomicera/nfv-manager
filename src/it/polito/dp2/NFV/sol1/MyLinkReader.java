package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.NodeReader;

public class MyLinkReader extends MyNamedEntity implements it.polito.dp2.NFV.LinkReader {
	private int latency;
	private float throughput;
	private NodeReader	sourceNode,
						destinationNode;
	
	public MyLinkReader(String id, 
						int latency, 
						float throughput,
						NodeReader sourceNode,
						NodeReader destinationNode) {
		super(id);
		this.latency = latency;
		this.throughput = throughput;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
	}

	@Override
	public int getLatency() {
		return latency;
	}

	@Override
	public float getThroughput() {
		return throughput;
	}
	
	@Override
	public NodeReader getSourceNode() {
		return sourceNode;
	}
	
	@Override
	public NodeReader getDestinationNode() {
		return destinationNode;
	}
	
//	public void setSourceNode(NodeReader sourceNode) {
//		this.sourceNode = sourceNode;
//	}
//	
//	public void setDestinationNode(NodeReader destinationNode) {
//		this.destinationNode = destinationNode;
//	}
}