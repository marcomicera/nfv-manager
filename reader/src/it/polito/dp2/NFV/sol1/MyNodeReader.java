package it.polito.dp2.NFV.sol1;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.VNFTypeReader;

public class MyNodeReader extends MyNamedEntity implements it.polito.dp2.NFV.NodeReader {
	private VNFTypeReader functionalType;
	private HostReader host;
	private Map<String, LinkReader> links;
	private NffgReader nffg;
	
	public MyNodeReader(String id,
						VNFTypeReader functionalType, 
						HostReader host,
						NffgReader nffg) {
		super(id);
		
		if(functionalType == null || host == null || nffg == null) {
			System.err.println("Invalid node's parameters");
			System.exit(1);
		}
		
		this.functionalType = functionalType;
		this.host = host;
		this.nffg = nffg;
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
		
		return new HashSet<LinkReader>(links.values());
	}

	@Override
	public NffgReader getNffg() {
		return nffg;
	}
	
	public void setLinks(Map<String, LinkReader> links) {
		this.links = links;
	}
}