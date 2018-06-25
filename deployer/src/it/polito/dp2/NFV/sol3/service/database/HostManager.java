package it.polito.dp2.NFV.sol3.service.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;
import it.polito.dp2.NFV.sol3.service.gen.model.HostsType;
import it.polito.dp2.NFV.sol3.service.neo4j.Localhost_Neo4JSimpleXMLWebapi;
import it.polito.dp2.NFV.sol3.service.neo4j.Relationships.Relationship;

public class HostManager {
	/**
	 * Hosts data
	 */
	private static Map<String, HostType> hosts = new ConcurrentHashMap<>();

	/**
	 * Entity-Neo4J ID mapping
	 */
	private static Map<String, String> neo4jIds = new ConcurrentHashMap<>();

	/**
	 * It retrieves data about the physical hosts in the Infrastructure Network.
	 * 
	 * @param monitor
	 *            NfvReader interface through which data must be retrieved.
	 * @return all the retrieved hosts.
	 */
	protected synchronized static Map<String, HostType> download(NfvReader monitor) {
		Map<String, HostType> result = new HashMap<>();

		// Retrieving hosts information
		Set<HostReader> retrievedHosts = monitor.getHosts();

		// Adding retrieved hosts in the local hosts map
		for (HostReader host : retrievedHosts)
			result.put(host.getName(), getHost(host));

		// Return downloaded hosts
		return result;
	}

	/**
	 * It converts a JAXB annotated host class (HostType) from a HostReader
	 * interface.
	 * 
	 * @param host
	 *            HostReader interface to be converted.
	 * @return a JAXB annotated host class.
	 */
	private static HostType getHost(HostReader host) {
		// New temporary host object
		HostType tempHost = new HostType();

		// Setting properties
		tempHost.setId(host.getName());
		tempHost.setMaxVNFs(host.getMaxVNFs());
		tempHost.setAvailableMemory(host.getAvailableMemory());
		tempHost.setAvailableStorage(host.getAvailableStorage());

		// Returning the converted host object
		return tempHost;
	}

	/**
	 * It deploys all downloaded host objects into Neo4J.
	 * 
	 * @param hosts
	 *            hosts to be deployed.
	 * @throws MalformedException
	 *             if a host object is malformed.
	 * @throws AllocationException
	 *             it some error occurred during the allocation of a host object.
	 * @throws UnknownEntityException
	 *             if a host object (or its name) is null.
	 */
	public synchronized static void deployHosts(Map<String, HostType> hosts)
			throws UnknownEntityException, AllocationException, MalformedException {
		// For each downloaded host
		for (HostType host : hosts.values())
			deployHost(host);
	}

	/**
	 * It deploys a host into an NF-FG.
	 * 
	 * @param host
	 *            the host that must be deployed.
	 * @throws UnknownEntityException
	 *             if the host object (or its name) is null.
	 * @throws AllocationException
	 *             it some error occurred during allocation.
	 * @throws MalformedException
	 *             if the host object is malformed.
	 */
	public synchronized static void deployHost(HostType host)
			throws UnknownEntityException, AllocationException, MalformedException {
		// Using the generic node-deployment function
		NodeManager.deploy(host, true, null);
	}

	/**
	 * It returns true if a new node can be allocated on the specified host
	 * {@code hostId} object.
	 * 
	 * @param hostId
	 *            the host to be checked.
	 * @param newNodesNumber
	 *            the number of new nodes that should be allocated on the specified
	 *            host {@code hostId} object.
	 * @return true if another node can be allocated on the host, false otherwise.
	 */
	protected static synchronized boolean canAllocate(String hostId, int newNodesNumber) {
		// Retrieving the corresponding host
		HostType host = hosts.get(hostId);

		// If the host does not exist
		if (host == null)
			throw new IllegalArgumentException("Testing node allocation on a non-existing host " + hostId + ".");

		// Retrieving the host VNF capability
		int hostCapability = host.getMaxVNFs();

		// Retrieving all inbound relationships on the host from Neo4J
		List<Relationship> inboundRelationships = Localhost_Neo4JSimpleXMLWebapi
				.data(Localhost_Neo4JSimpleXMLWebapi.createClient(), Localhost_Neo4JSimpleXMLWebapi.BASE_URI)
				.nodeNodeidRelationshipsIn(neo4jIds.get(hostId)).getAsRelationships().getRelationship();

		// Counting allocated nodes on the host
		int allocatedNodes = 0;
		for (Relationship relationship : inboundRelationships)
			if ("AllocatedOn".compareTo(relationship.getType()) == 0)
				++allocatedNodes;

		if (allocatedNodes + newNodesNumber > hostCapability)
			return false;
		else
			return true;
	}

	public static synchronized HostType getHost(String hostId) {
		return hosts.get(hostId);
	}

	public static synchronized HostsType getHosts() {
		// Building the result object
		HostsType result = new HostsType();
		result.getHost().addAll(hosts.values());

		return result;
	}
	
	protected static synchronized Map<String, HostType> getHostsMap() {
		return hosts;
	}

	public static synchronized Map<String, String> getNeo4jIds() {
		return neo4jIds;
	}
	
	public static synchronized int howMany() {
		return hosts.size();
	}
}
