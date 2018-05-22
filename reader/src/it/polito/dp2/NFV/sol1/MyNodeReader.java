package it.polito.dp2.NFV.sol1;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.LinkType;
import it.polito.dp2.NFV.sol1.jaxb.NodeType;

public class MyNodeReader extends MyNamedEntity implements it.polito.dp2.NFV.NodeReader {
	private NodeType info;
	private NffgReader nffg;
	private VNFTypeReader functionalType;
	private HostReader host;
	private Set<LinkReader> links;
	
	public MyNodeReader(NodeType info, 
						NffgReader nffg,
						VNFTypeReader functionalType,
						HostReader host
	) {
		super(info.getId() != null ? info.getId() : null);
		
		if(nffg == null || functionalType == null || host == null) {
			System.err.println("Invalid node parameters");
			System.exit(1);
		}
		
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
		if(links == null)
			return null;
		if(links.isEmpty())
			return new HashSet<LinkReader>();
		
		return links;
	}

	@Override
	public NffgReader getNffg() {
		return nffg;
	}
	
	public void setLinks() {
		for(LinkType link: info.getLink()) {
			NodeReader sourceNode = nffg.getNode(link.getSourceNode());
			NodeReader destinationNode = nffg.getNode(link.getDestinationNode());
			
			links.add(
				new MyLinkReader(
					link,
					sourceNode,
					destinationNode
				)
			);
		}
	}
}