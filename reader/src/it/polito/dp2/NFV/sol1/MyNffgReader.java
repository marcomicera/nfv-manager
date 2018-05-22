package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.NffgType;

public class MyNffgReader extends MyNamedEntity implements it.polito.dp2.NFV.NffgReader {
	private NffgType info;
	private Map<String, NodeReader> nodes;
	
	public MyNffgReader(NffgType info) {
		super(info.getId() != null ? info.getId() : null);
		
		this.info = info;
		nodes = new HashMap<String, NodeReader>();
	}
	
	@Override
	public Calendar getDeployTime() {
		return info.getDeployTime().toGregorianCalendar();
	}

	@Override
	public NodeReader getNode(String name) {
		if(name == null || name.isEmpty() || nodes == null)
			return null;
		
		return nodes.get(name);
	}

	@Override
	public Set<NodeReader> getNodes() {
		if(nodes == null)
			return null;
		if(nodes.isEmpty())
			return new HashSet<NodeReader>();
		
		return new HashSet<NodeReader>(nodes.values());
	}
	
	public void setNodes(Map<String, NodeReader> nodes) {
		this.nodes = nodes;
	}
}