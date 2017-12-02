package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.NodeType;

public class MyNffgReader extends MyNamedEntity implements it.polito.dp2.NFV.NffgReader {
	private Calendar deployTime;
	private Map<String, NodeReader> nodes;
	
	public MyNffgReader(String name, Calendar deployTime, List<NodeType> nodes) {
		super(name);
		this.deployTime = deployTime;
		/*for(NodeType node: nodes)
			this.nodes.put(node.getId(), node);*/
	}

	@Override
	public Calendar getDeployTime() {
		return deployTime;
	}

	@Override
	public NodeReader getNode(String name) {
		if(name == null || nodes == null)
			return null;
		
		return nodes.get(name);
	}

	@Override
	public Set<NodeReader> getNodes() {
		return (Set<NodeReader>)nodes.values();
	}
	
}