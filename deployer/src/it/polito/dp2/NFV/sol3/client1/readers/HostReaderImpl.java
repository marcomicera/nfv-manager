package it.polito.dp2.NFV.sol3.client1.readers;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.HostType;

public class HostReaderImpl extends MyNamedEntity implements HostReader {
	private HostType info;
	private Set<NodeReader> nodes;

	public HostReaderImpl(HostType info) {
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
	 * 
	 * @param node
	 *            The new allocated node's reader
	 */
	public void addNode(NodeReader node) {
		nodes.add(node);
	}

}
