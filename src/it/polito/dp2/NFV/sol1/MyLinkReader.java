package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.LinkType;

public class MyLinkReader extends MyNamedEntity implements it.polito.dp2.NFV.LinkReader {
	private LinkType info;
	private NodeReader sourceNode;
	private NodeReader destinationNode;
	
	public MyLinkReader(LinkType info, 
						NodeReader sourceNode, 
						NodeReader destinationNode
	) {
		super(info.getId() != null ? info.getId() : null);
		
		if(sourceNode == null || destinationNode == null) {
			System.err.println("Invalid link parameters");
			System.exit(1);
		}
		
		this.info = info;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
	}

	@Override
	public int getLatency() {
		return info.getMaximumLatency();
	}

	@Override
	public float getThroughput() {
		return info.getMinimumThroughput();
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