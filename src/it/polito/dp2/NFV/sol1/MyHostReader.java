package it.polito.dp2.NFV.sol1;

import java.util.Set;

import it.polito.dp2.NFV.NodeReader;

public class MyHostReader extends MyNamedEntity implements it.polito.dp2.NFV.HostReader {
	private int availableMemory;
	private int availableStorage;
	private int maxVNFs;
	private Set<NodeReader> nodes;

	public MyHostReader(String id, 
						int availableMemory, 
						int availableStorage, 
						int maxVNFs,
						Set<NodeReader> nodes) {
		super(id);
		this.availableMemory = availableMemory;
		this.availableStorage = availableStorage;
		this.maxVNFs = maxVNFs;
		this.nodes = nodes;
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
		return nodes;
	}
	
}