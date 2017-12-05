package it.polito.dp2.NFV.sol1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;

public class MyHostReader extends MyNamedEntity implements it.polito.dp2.NFV.HostReader {
	/**
	 * Capability parameters
	 */
	private int availableMemory;
	private int availableStorage;
	private int maxVNFs;
	
	/**
	 * Allocated nodes on the host
	 */
	private Map<String, NodeReader> nodes;

	public MyHostReader(String id, 
						int availableMemory, 
						int availableStorage, 
						int maxVNFs) {
		super(id);
		this.availableMemory = availableMemory;
		this.availableStorage = availableStorage;
		this.maxVNFs = maxVNFs;
		nodes = new HashMap<String, NodeReader>();
	}

	@Override
	public int getAvailableMemory() {
		return availableMemory;
	}

	@Override
	public int getAvailableStorage() {
		return availableStorage;
	}

	@Override
	public int getMaxVNFs() {
		return maxVNFs;
	}

	@Override
	public Set<NodeReader> getNodes() {
		return new HashSet<NodeReader>(nodes.values());
	}
	
	/**
	 * Adds a allocated node on this host
	 * @param nodeName	The new allocated node's ID
	 * @param node		The new allocated node's reader
	 */
	public void addNode(String nodeName, NodeReader node) { 
		nodes.put(nodeName, node);
	}
}