package it.polito.dp2.NFV.sol3.service.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.LinkAlreadyPresentException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;
import it.polito.dp2.NFV.sol3.service.NfvValidationProvider;
import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgsType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodesType;

public class NffgManager {
	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

	/**
	 * NF-FGs data
	 */
	private static Map<String, NffgType> nffgs = new ConcurrentHashMap<>();

	/**
	 * Ordered NF-FG IDs by their deployment time
	 */
	private static NavigableSet<NffgDeploymentInfo> orderedNffgIds = new ConcurrentSkipListSet<>(
			new Comparator<NffgDeploymentInfo>() {
				@Override
				public int compare(NffgDeploymentInfo o1, NffgDeploymentInfo o2) {
					return o1.deployTime.compare(o2.deployTime);
				}
			});

	/**
	 * Nested class used to store NF-FG IDs ordered by their deploy time.
	 */
	static class NffgDeploymentInfo {
		/**
		 * NF-FG deploy time
		 */
		private XMLGregorianCalendar deployTime;

		/**
		 * NF-FG ID
		 */
		private String nffgId;

		public NffgDeploymentInfo(XMLGregorianCalendar deployTime, String nffgId) {
			this.deployTime = deployTime;
			this.nffgId = nffgId;
		}

		@Override
		public String toString() {
			return "<" + nffgId + ", " + deployTime + ">";
		}
	}

	/**
	 * It downloads a specific NF-FG.
	 * 
	 * @param nffgName
	 *            the NF-FG name to be downloaded.
	 * @param monitor
	 *            NfvReader interface through which data must be retrieved.
	 * @return the downloaded NF-FG.
	 */
	protected synchronized static NffgType download(String nffgName, NfvReader monitor) {
		// Retrieving data about the Nffg
		NffgReader retrievedNffg = monitor.getNffg(nffgName);

		// Nffg JAXB annotated class
		NffgType newNffg = new NffgType();
		newNffg.setId(nffgName);

		// Setting the Nffg JAXB annotated class' deploy time
		GregorianCalendar gregorianDeployTime = new GregorianCalendar();
		gregorianDeployTime.setTime(retrievedNffg.getDeployTime().getTime());
		try {
			newNffg.setDeployTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianDeployTime));
		} catch (DatatypeConfigurationException e) {
			System.err.println("Could not convert deploy tiem in Gregorian format");
		}

		// Nffg's nodes JAXB annotated class
		NodesType newNffgNodes = new NodesType();

		// Nffg's node list
		List<NodeType> newNffgNodeList = newNffgNodes.getNode();
		for (NodeReader node : retrievedNffg.getNodes()) {
			// New temporary Nffg node object
			NodeType newNffgNode = new NodeType();

			// Setting the new temporary node object properties
			newNffgNode.setId(node.getName());
			newNffgNode.setHost(node.getHost().getName());
			newNffgNode.setFunctionalType(node.getFuncType().getFunctionalType().toString());

			// New temporary Nffg node links list
			List<LinkType> newNffgNodeLinks = newNffgNode.getLink();

			// For each node's link
			for (LinkReader link : node.getLinks()) {
				// New temporary
				LinkType newNffgNodeLink = new LinkType();

				// Setting the new temporary link object properties
				newNffgNodeLink.setId(link.getName());
				newNffgNodeLink.setSourceNode(link.getSourceNode().getName());
				newNffgNodeLink.setDestinationNode(link.getDestinationNode().getName());
				newNffgNodeLink.setMaximumLatency(link.getLatency());
				newNffgNodeLink.setMinimumThroughput(link.getThroughput());

				// Adding this link to the new temporary Nffg node object
				newNffgNodeLinks.add(newNffgNodeLink);
			}

			// Adding this temporary node to the Nffg's node list
			newNffgNodeList.add(newNffgNode);
		}

		// Setting the Nffg JAXB annotated class' nodes
		newNffg.setNodes(newNffgNodes);

		// Returning the downloaded NF-FG object
		return newNffg;
	}

	/**
	 * It deploys an NF-FG object into the Neo4J database (when a new NF-FG is
	 * deployed, its nodes are allocated onto the IN hosts and the new NF-FG is
	 * added to the list of NF-FGs; if it is not possible to allocate all the NF-FG
	 * nodes, deployment fails, and nothing is added). If deployment is successful,
	 * deploy time is set to the current time.
	 * 
	 * @param nffg
	 *            the NF-FG to be deployed.
	 * @throws UnknownEntityException
	 *             if the NF-FG object or name is null.
	 * @throws MalformedException
	 *             if the NF-FG object is not well formed.
	 * @throws AllocationException
	 *             if the NF-FG has already been deployed.
	 * @throws DatatypeConfigurationException
	 *             if it is not possible to get the current date in the desired
	 *             format.
	 */
	public synchronized static void deploy(NffgType nffg)
			throws MalformedException, UnknownEntityException, AllocationException, DatatypeConfigurationException {
		// Argument checking
		if (nffg == null || nffg.getId() == null)
			throw new UnknownEntityException("The NF-FG is null or it has a null ID.");

		// If this NF-FG has already been deployed
		if (hasBeenDeployed(nffg))
			throw new AllocationException("NF-FG " + nffg.getId() + " has already been allocated.");

		// If the NF-FG object is not well-formed
		if (!isWellFormed(nffg))
			throw new MalformedException("NF-FG " + nffg.getId() + " object is malformed.");

		// Checking whether all nodes can be allocated on their corresponding hosts
		if (!canAllocate(nffg))
			throw new AllocationException(
					"NF-FG " + nffg.getId() + " has some nodes that cannot be allocated on their corresponding hosts.");

		// DataTypeFactory used to get the current date
		DatatypeFactory dataTypeFactory = null;

		// Checking whether it is possible to retrieve the DataTypeFactory to convert
		// the current date
		dataTypeFactory = DatatypeFactory.newInstance();

		// TODO debugging
		System.out.println("Deploying " + nffg.getId() + "...");

		// Adding the deployed NF-FG to the nffgs local object
		nffgs.put(nffg.getId(), nffg);

		// Deploying NF-FG's nodes
		NodeManager.deployNodes(nffg);

		// Deploying NF-FG's links
		try {
			LinkManager.deployLinks(nffg, false);
		} catch (LinkAlreadyPresentException e) {
			throw new AllocationException("NF-FG " + nffg.getId() + " already had some links deployed.");
		}

		/*
		 * Setting the NF-FG deploy time
		 */
		
		// If the NF-FG already has a deploy time
		if(nffg.getDeployTime() != null) {
			// Set its deploy time
			nffgs.get(nffg.getId()).setDeployTime(nffg.getDeployTime());
			orderedNffgIds.add(new NffgDeploymentInfo(nffg.getDeployTime(), nffg.getId()));
		} else {
			// Set the deploytime equals to the current time
			XMLGregorianCalendar deployTime = dataTypeFactory.newXMLGregorianCalendar(new GregorianCalendar());
			nffgs.get(nffg.getId()).setDeployTime(deployTime);
			orderedNffgIds.add(new NffgDeploymentInfo(deployTime, nffg.getId()));
		}
	}

	/**
	 * Checking whether all nodes belonging to the specified NF-FG {@code nffg} can
	 * be allocated on their corresponding hosts.
	 * 
	 * @param nffg
	 *            NF-FG object to be checked.
	 * @return true if all nodes belonging to the NF-FG {@code nffg} can be
	 *         allocated on their corresponding hosts.
	 */
	private synchronized static boolean canAllocate(NffgType nffg) {
		// Assuming that the NF-FG can be allocated
		boolean canAllocate = true;

		/*
		 * Keeping the count of new nodes that will have to be allocated on each host.
		 * The key represents the host ID. The value represents the number of new nodes
		 * that should be allocated on it.
		 */
		Map<String, Integer> newNodesForEachHost = new HashMap<>();

		// Filling the hostId-newNodesNumber map
		for (NodeType node : nffg.getNodes().getNode()) {
			// If an entry on the hostId-newNodesNumber does not exist
			if (!newNodesForEachHost.containsKey(node.getHost()))
				// Create a new entry initialized to zero
				newNodesForEachHost.put(node.getHost(), 0);

			// If the node is allocated on a host
			if (node.getHost() != null && !node.getHost().isEmpty() && "".compareTo(node.getHost()) != 0)
				// Increment the number of nodes that should be allocated on it
				newNodesForEachHost.put(node.getHost(), newNodesForEachHost.get(node.getHost()) + 1);
		}

		// TODO debugging
		System.out.println(nffg.getId() + " hostId-newNodesNumber map:");
		for (Map.Entry<String, Integer> mapEntry : newNodesForEachHost.entrySet()) {
			System.out.print("<" + mapEntry.getKey() + ", " + mapEntry.getValue() + "> ");
		}

		// Performing the allocation check for each host
		for (Map.Entry<String, Integer> mapEntry : newNodesForEachHost.entrySet()) {
			// Actual allocation check
			canAllocate = HostManager.canAllocate(mapEntry.getKey(), mapEntry.getValue());

			// If this allocation test fails
			if (canAllocate == false)
				// It is useless to keep going with other
				break;
		}

		// Returning the allocation test outcome
		return canAllocate;
	}

	/**
	 * It checks whether the specified NF-FG has already been deployed or not.
	 * 
	 * @param nffg
	 *            the NF-FG to be checked.
	 * @return true if already deployed, false otherwise.
	 */
	public static boolean hasBeenDeployed(NffgType nffg) {
		return hasBeenDeployed(nffg.getId());
	}

	public synchronized static boolean hasBeenDeployed(String nffgId) {
		return nffgs.containsKey(nffgId);
	}

	/**
	 * It checks whether a NF-FG object is well-formed or not.
	 * 
	 * @param nffg
	 *            the NF-FG to be checked.
	 * @return true if well-formed, false otherwise.
	 */
	public static boolean isWellFormed(NffgType nffg) {
		return (new NfvValidationProvider<NffgType>()).isReadable(nffg.getClass(), null, null, null);
	}

	public static synchronized NffgType getNffg(String nffgId) {
		return nffgs.get(nffgId);
	}

	public static synchronized Map<String, NffgType> getNffgs() {
		return nffgs;
	}

	/**
	 * Returns a sub-set of NF-FG object deployed since {@code since}.
	 * 
	 * @param since
	 *            the time after which deployed NF-FGs must be returned.
	 * @return a sub-set of NF-FG object deployed after the specified time.
	 * @throws ParseException
	 *             if the date passed as argument does not respect the defined date
	 *             format.
	 */
	public static synchronized NffgsType getNffgs(String since) throws ParseException {
		// If the argument is null, all NF-FGs must be returned
		if (since == null) {
			// Converting the NF-FGs map into a list
			NffgsType result = new NffgsType();
			List<NffgType> resultList = result.getNffg();
			resultList.addAll(nffgs.values());

			// Return all NF-FGs
			return result;
		}

		// Converting the date to the desired format
		XMLGregorianCalendar gregorianCalendar = null;
		try {
			gregorianCalendar = convertDate(since);
		} catch (ParseException | DatatypeConfigurationException e) {
			throw new ParseException("Invalid date format. Expected: " + DATE_FORMAT,
					(e instanceof ParseException) ? ((ParseException) e).getErrorOffset() : null);
		}

		// NF-FG IDs deployed after the specified date
		NavigableSet<NffgDeploymentInfo> resultIds = orderedNffgIds
				.tailSet(new NffgDeploymentInfo(gregorianCalendar, null), true);

		// Building the result list
		NffgsType result = new NffgsType();
		List<NffgType> resultList = result.getNffg();
		for (NffgDeploymentInfo nffgDeploymentInfo : resultIds) {
			// Retrieving the corresponding NF-FG object
			NffgType nffg = getNffg(nffgDeploymentInfo.nffgId);

			// If the NF-FG object exists
			if (nffg != null)
				// Including it in the result map
				resultList.add(nffg);
		}

		// Returning the sub-set of NF-FGs deployed after the specified time
		return result;
	}

	/**
	 * Converts a String representing a date in a XMLGregorianCalendar object.
	 * 
	 * @param date
	 *            the date String to be converted.
	 * @return the XMLGregorianCalendar date object.
	 * @throws ParseException
	 *             if the date passed as argument does not respect the defined date
	 *             format.
	 * @throws DatatypeConfigurationException
	 *             if a data factory implementation is not available or cannot be
	 *             instantiated.
	 */
	private static XMLGregorianCalendar convertDate(String date) throws ParseException, DatatypeConfigurationException {
		Date parsedDate = new SimpleDateFormat(DATE_FORMAT).parse(date);
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(parsedDate);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	}
}
