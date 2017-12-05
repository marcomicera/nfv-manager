package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFV.NodeReader;

public class MyNffgReader extends MyNamedEntity implements it.polito.dp2.NFV.NffgReader {
	private Calendar deployTime;
	private Map<String, NodeReader> nodes;
	
	public MyNffgReader(String name, Calendar deployTime) {
		super(name);
		
		if(deployTime == null) {
			System.err.println("Invalid NFFG's deploy time");
			System.exit(1);
		}
		
		this.deployTime = deployTime;
	}
	
	public MyNffgReader(String name, XMLGregorianCalendar deployTime) {
		this(
			name, 
			deployTime == null ? null : deployTime.toGregorianCalendar()
		);
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
		if(nodes == null)
			return null;
		
		return new HashSet<NodeReader>(nodes.values());
	}
	
	public void setNodes(Map<String, NodeReader> nodes) {
		if(nodes != null) this.nodes = nodes;
	}
}