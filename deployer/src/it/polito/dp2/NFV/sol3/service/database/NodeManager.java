package it.polito.dp2.NFV.sol3.service.database;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.jersey.api.client.ClientResponse;

import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;
import it.polito.dp2.NFV.sol3.service.NfvValidationProvider;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeRefType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodesType;
import it.polito.dp2.NFV.sol3.service.neo4j.Labels;
import it.polito.dp2.NFV.sol3.service.neo4j.Localhost_Neo4JSimpleXMLWebapi;
import it.polito.dp2.NFV.sol3.service.neo4j.Node;
import it.polito.dp2.NFV.sol3.service.neo4j.Properties;
import it.polito.dp2.NFV.sol3.service.neo4j.Property;

public class NodeManager {
	/**
	 * Nodes data
	 */
	private static Map<String, NodeType> nodes = new ConcurrentHashMap<>();

	/**
	 * Entity-Neo4J ID mapping
	 */
	private static Map<String, String> neo4jIds = new ConcurrentHashMap<>();

	/**
	 * It deploys all nodes belonging to the specified NF-FG.
	 * 
	 * @param nffg
	 *            the NF-FG whose nodes must be deployed.
	 * @throws UnknownEntityException
	 *             if at least one node object (or its name) is null.
	 * @throws AllocationException
	 *             it some error occurred during allocation.
	 * @throws MalformedException
	 *             if at least one node object is malformed.
	 */
	protected synchronized static void deployNodes(NffgType nffg)
			throws MalformedException, UnknownEntityException, AllocationException {
		// For each node belonging to the NF-FG
		for (NodeType node : nffg.getNodes().getNode())
			try {
				// TODO debugging
				System.out.println("Deploying node " + node.getId() + "...");

				NodeManager.deployNode(nffg.getId(), node);
			} catch (UnknownEntityException | MalformedException | AllocationException exception) {
				// TODO debugging
				System.out.println(
						"Exception during the deployment of Node " + node.getId() + ": " + exception.getMessage());

				// TODO Removing all previously-added nodes

				// TODO Remove the NF-FG from the nffgs local object
				// NffgManager.getNffgs().remove(nffg.getId());

				// Throw the exception to the caller
				throw exception;
			}
	}

	/**
	 * It deploys a node into an NF-FG. (when a node is added, it is added without
	 * links and it is also allocated on a host; if allocation is not possible, the
	 * addition of the node fails and nothing is added)
	 * 
	 * @param nffgId
	 *            the NF-FG in which the node must be added.
	 * @param node
	 *            the node that must be deployed.
	 * @throws UnknownEntityException
	 *             if the node object (or its name) is null.
	 * @throws AllocationException
	 *             it some error occurred during allocation.
	 * @throws MalformedException
	 *             if the node object is malformed.
	 */
	public static void deployNode(String nffgId, NodeType node)
			throws UnknownEntityException, AllocationException, MalformedException {
		// Using the generic node-deployment function
		deploy(node, false, nffgId);
	}

	/**
	 * It deploys an object on Neo4J, whether it is a node or an host.
	 * 
	 * @param object
	 *            the object to be deployed.
	 * @param isHost
	 *            true if {@code object} represents a node, false if it represents a
	 *            host
	 * @param nffgId
	 *            the NF-FG on which the node must be deployed on. In case
	 *            {@code object} represents a host, this argument makes no sense and
	 *            it can be set to null.
	 * @throws UnknownEntityException
	 *             if the {@code object} object (or its name) is null.
	 * @throws AllocationException
	 *             it some error occurred during allocation.
	 * @throws MalformedException
	 *             if the {@code object} object is malformed.
	 */
	protected synchronized static void deploy(Object object, boolean isHost, String nffgId)
			throws UnknownEntityException, AllocationException, MalformedException {
		// Argument checking
		if (object == null)
			throw new UnknownEntityException("The " + (isHost ? "host" : "node") + " object is null.");

		// Argument class check
		if (!(object instanceof NodeType) && !(object instanceof HostType))
			// Could not deploy a Neo4J node which is not a Node or a Host
			throw new IllegalArgumentException("The object has not a valid type");

		// Argument casts
		NodeType node = null;
		HostType host = null;

		// Casting
		if (object instanceof NodeType && !isHost) {
			// TODO debugging
			System.out.println("The object is a Node");

			node = (NodeType) object;
		} else if (object instanceof HostType && isHost) {
			// TODO debugging
			System.out.println("The object is a Node");

			host = (HostType) object;
		} else
			// Wrong argument combination
			throw new IllegalArgumentException("Wrong argument combination:\n\t"
					+ "• When object is an instance of NodeType, isHost must be false\n\t"
					+ "• When object is an instance of HostType, isHost must be true");

		// Name checking
		if (!isHost && (node.getId() == null || "".compareTo(node.getId()) == 0))
			throw new UnknownEntityException("The node has a null ID.");
		else if (isHost && (host.getId() == null || "".compareTo(host.getId()) == 0))
			throw new UnknownEntityException("The host has a null ID.");

		// Node-specific checks
		if (!isHost) {
			// If the NF-FG does not exist yet
			if (!NffgManager.hasBeenDeployed(nffgId))
				// The node cannot be deployed since it must belong to an NF-FG
				throw new AllocationException("Attempting to add node " + node.getId() + " to a non-existing NF-FG.");

			// If the host in which the node is allocated does not exist yet
			if (!hostHasBeenDeployed(node.getHost()))
				// The node cannot be deployed since it must be allocated on a host
				throw new AllocationException(
						"Attempting to allocate node " + node.getId() + " to a non-existing host.");
		}

		// If this object has already been deployed
		if (hasBeenDeployed(isHost ? host : node))
			throw new AllocationException(
					(isHost ? ("Host " + host.getId()) : ("Node " + node.getId())) + " has already been deployed.");

		// If this object is not well-formed
		if (!isWellFormed(isHost ? host : node))
			throw new MalformedException(
					(isHost ? ("Host " + host.getId()) : ("Node " + node.getId())) + " object is malformed.");

		// New object to be uploaded
		Node newObject = new Node();

		// Setting the new object's ID
		newObject.setId(isHost ? host.getId() : node.getId());

		// New node's "name" property
		Property newObjectProperty = new Property();
		newObjectProperty.setName("name");
		newObjectProperty.setValue(isHost ? host.getId() : node.getId());

		// New node's properties
		Properties newObjectProperties = new Properties();
		newObjectProperties.getProperty().add(newObjectProperty);

		// Setting the new object properties
		newObject.setProperties(newObjectProperties);

		// TODO debugging
		System.out.println("Uploading " + (isHost ? "host " : "node "));

		// Uploading the node
		ClientResponse nodeUploadResponse = Localhost_Neo4JSimpleXMLWebapi
				.data(Localhost_Neo4JSimpleXMLWebapi.createClient(), Localhost_Neo4JSimpleXMLWebapi.BASE_URI).node()
				.postXml(newObject, ClientResponse.class);

		// Retrieving the new node Neo4J ID
		String neo4jObjectId = nodeUploadResponse.getEntity(Node.class).getId();

		// New node's "Node" label
		Labels newObjectLabels = new Labels();
		newObjectLabels.getLabel().add(isHost ? "Host" : "Node");

		// Setting the new node's labels
		Localhost_Neo4JSimpleXMLWebapi
				.data(Localhost_Neo4JSimpleXMLWebapi.createClient(), Localhost_Neo4JSimpleXMLWebapi.BASE_URI)
				.nodeNodeidLabels(neo4jObjectId).postXml(newObjectLabels, ClientResponse.class);

		// Allocating the node on its IN host
		if (!isHost) {
			// TODO debugging
			System.out.println("Allocating node " + node.getId() + " on host " + node.getHost() + "...");

			// Retrieving the Neo4j ID of the host on which the node should be allocated on
			String neo4jAllocatedHostId = HostManager.getNeo4jIds().get(node.getHost());

			// Allocating the node on its IN host on Neo4J
			RelationshipManager.deployRelationship(neo4jObjectId, neo4jAllocatedHostId, "AllocatedOn");

			// Allocating the node on its IN host in the local hosts object
			NodeRefType nodeReference = new NodeRefType();
			nodeReference.setId(node.getId());
			HostManager.getHost(node.getHost()).getNode().add(nodeReference);
		}

		// Adding this new object to the corresponding local map
		if (isHost) {
			HostManager.getHostsMap().put(host.getId(), host);
			HostManager.getNeo4jIds().put(host.getId(), neo4jObjectId);
		} else {
			nodes.put(node.getId(), node);
			neo4jIds.put(node.getId(), neo4jObjectId);
		}
	}

	/**
	 * It checks whether the specified node or host ({@code object}) has already
	 * been deployed or not.
	 * 
	 * @param object
	 *            the node/host to be checked.
	 * @return true if already deployed, false otherwise.
	 */
	public static boolean hasBeenDeployed(Object object) {
		if (object instanceof NodeType)
			return nodeHasBeenDeployed(((NodeType) object).getId());
		else if (object instanceof HostType)
			return hostHasBeenDeployed(((HostType) object).getId());
		else
			throw new IllegalArgumentException("The argument's typr must be either a NodeType or HostType.");
	}

	protected synchronized static boolean nodeHasBeenDeployed(String nodeId) {
		return nodes.containsKey(nodeId);
	}

	private synchronized static boolean hostHasBeenDeployed(String hostId) {
		return HostManager.getHostsMap().containsKey(hostId);
	}

	/**
	 * It checks whether the specified node or host ({@code object}) is well-formed
	 * or not.
	 * 
	 * @param object
	 *            the object to be checked.
	 * @return true if well-formed, false otherwise.
	 */
	public static boolean isWellFormed(Object object) {
		if (object instanceof NodeType)
			return nodeIsWellFormed((NodeType) object);
		else if (object instanceof HostType)
			return hostIsWellFormed((HostType) object);
		else
			throw new IllegalArgumentException("The argument's typr must be either a NodeType or HostType.");
	}

	private static boolean nodeIsWellFormed(NodeType node) {
		return (new NfvValidationProvider<NodeType>()).isReadable(node.getClass(), null, null, null);
	}

	public static boolean hostIsWellFormed(HostType host) {
		return (new NfvValidationProvider<HostType>()).isReadable(host.getClass(), null, null, null);
	}

	public static synchronized NodesType getNodes() {
		// Building the result object
		NodesType result = new NodesType();
		result.getNode().addAll(nodes.values());

		return result;
	}

	public static synchronized Map<String, String> getNeo4jIds() {
		return neo4jIds;
	}
}
