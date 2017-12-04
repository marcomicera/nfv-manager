package it.polito.dp2.NFV.sol1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;

public class MyHostReader extends MyNamedEntity implements it.polito.dp2.NFV.HostReader {
	private int availableMemory;
	private int availableStorage;
	private int maxVNFs;
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
		//return (Set<NodeReader>)nodes.values();
		return new HashSet<NodeReader>(nodes.values());
	}
	
	public void addNode(String nodeName, NodeReader node) {
		nodes.put(nodeName, node);
	}
}