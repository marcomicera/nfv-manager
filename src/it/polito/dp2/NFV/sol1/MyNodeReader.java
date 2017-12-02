package it.polito.dp2.NFV.sol1;

import java.util.Set;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.VNFTypeReader;

public class MyNodeReader extends MyNamedEntity implements it.polito.dp2.NFV.NodeReader {
	private VNFTypeReader functionalType;
	private HostReader host;
	private Set<LinkReader> links;
	private NffgReader nffg;
	
	public MyNodeReader(String id,
						FunctionalType functionalType, 
						HostReader host, 
						Set<LinkReader> links,
						NffgReader nffg) {
		super(id);
		//this.functionalType = new VNFTypeReader();
		this.host = host;
		this.links = links;
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
		return links;
	}

	@Override
	public NffgReader getNffg() {
		return nffg;
	}
	
}