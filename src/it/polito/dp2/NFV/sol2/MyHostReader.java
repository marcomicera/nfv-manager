package it.polito.dp2.NFV.sol2;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.NodeReader;

public class MyHostReader extends MyNamedEntity implements it.polito.dp2.NFV.HostReader {
	private HostType info;
	private Set<NodeReader> nodes;

	public MyHostReader(HostType info) {
		super(info.getId() != null ? info.getId() : null);
		
		this.info = info;
		nodes = new HashSet<NodeReader>();
	}

	@Override
	public int getAvailableMemory() {
		return info.getAvailableMemory();
	}

	@Override
	public int getAvailableStorage() {
		return info.getAvailableStorage();
	}

	@Override
	public int getMaxVNFs() {
		return info.getMaxVNFs();
	}

	@Override
	public Set<NodeReader> getNodes() {
		return nodes;
	}
	
	/**
	 * Adds a allocated node on this host
	 * @param node		The new allocated node's reader
	 */
	public void addNode(NodeReader node) {
		nodes.add(node);
	}
}