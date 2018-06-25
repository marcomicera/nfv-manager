package it.polito.dp2.NFV.sol3.client1.readers;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.NffgType;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.NodeType;

public class NffgReaderImpl extends MyNamedEntity implements NffgReader {
	private NffgType info;
	private Map<String, NodeReader> nodes;

	public NffgReaderImpl(NffgType nffg, Map<String, HostReader> hosts, Map<String, VNFTypeReader> catalog) {
		super(nffg.getId());

		info = nffg;

		readNodes(hosts, catalog);
	}

	@Override
	public Calendar getDeployTime() {
		return info.getDeployTime().toGregorianCalendar();
	}

	@Override
	public NodeReader getNode(String name) {
		// Argument checking
		if (name == null || name.isEmpty() || "".compareTo(name) == 0)
			// Invalid name
			throw new IllegalArgumentException("Invalid node name.");

		// If no nodes are deployed in this NF-FG
		if (nodes == null || nodes.isEmpty())
			return null;

		// Returning the right node object
		return nodes.get(name);
	}

	@Override
	public Set<NodeReader> getNodes() {
		// Argument checking
		if (nodes == null)
			return null;

		// In no nodes are deployed in this Nf-FG
		if (nodes.isEmpty())
			// Return an empty set
			return new HashSet<NodeReader>();

		// Returning all node objects in a set
		return new HashSet<NodeReader>(nodes.values());
	}

	private void readNodes(Map<String, HostReader> hosts, Map<String, VNFTypeReader> catalog) {
		for (NodeType node : info.getNodes().getNode()) {
			NodeReader tempNodeReader = new NodeReaderImpl(node, this, catalog.get(node.getFunctionalType()),
					hosts.get(node.getHost()));

			nodes.put(node.getId(), tempNodeReader);

			if (node.getHost() != null) {
				HostReaderImpl host = (HostReaderImpl) hosts.get(node.getHost());
				host.addNode(tempNodeReader);
			}
		}
	}
}
