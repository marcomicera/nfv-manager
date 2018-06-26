package it.polito.dp2.NFV.sol3.client1.readers;

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;

public class LinkReaderImpl extends MyNamedEntity implements LinkReader {
	private LinkType info;
	private NodeReader sourceNode;
	private NodeReader destinationNode;

	public LinkReaderImpl(LinkType info, NodeReader sourceNode, NodeReader destinationNode) {
		super(info.getId());

		if (sourceNode == null || destinationNode == null)
			throw new IllegalArgumentException("Invalid link parameters");

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
