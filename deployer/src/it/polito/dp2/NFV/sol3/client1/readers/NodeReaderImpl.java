package it.polito.dp2.NFV.sol3.client1.readers;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;

public class NodeReaderImpl extends MyNamedEntity implements NodeReader {
	private NodeType info;
	private NffgReader nffg;
	private VNFTypeReader functionalType;
	private HostReader host;
	private Set<LinkReader> links;

	public NodeReaderImpl(NodeType info, NffgReader nffg, VNFTypeReader functionalType, HostReader host) {
		super(info.getId());

		// Arguments checking
		if (nffg == null || functionalType == null /*|| host == null*/)
			throw new IllegalArgumentException("Invalid node parameters");

		this.info = info;
		this.nffg = nffg;
		this.functionalType = functionalType;
		this.host = host;
		links = new HashSet<LinkReader>();
	}

	@Override
	public VNFTypeReader getFuncType() {
		return functionalType;
	}

	@Override
	public HostReader getHost() {
		return host;
	}

	@Override
	public Set<LinkReader> getLinks() {
		if (links == null)
			return null;
		if (links.isEmpty())
			return new HashSet<LinkReader>();

		return links;
	}

	@Override
	public NffgReader getNffg() {
		return nffg;
	}

	public void setLinks() {
		for (LinkType link : info.getLink()) {
			NodeReader sourceNode = nffg.getNode(link.getSourceNode());
			NodeReader destinationNode = nffg.getNode(link.getDestinationNode());

			links.add(new LinkReaderImpl(link, sourceNode, destinationNode));
		}
	}
}
