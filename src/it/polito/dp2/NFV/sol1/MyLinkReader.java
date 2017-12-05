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
		
		if(	latency < 0 || 
			throughput < 0 ||
			sourceNode == null ||
			destinationNode == null
		) {
			System.err.println("Invalid link parameters");
			System.exit(1);
		}
		
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
}