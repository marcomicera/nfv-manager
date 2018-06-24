package it.polito.dp2.NFV.sol3.service.database;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.LinkAlreadyPresentException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;
import it.polito.dp2.NFV.sol3.service.NfvValidationProvider;
import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;

public class LinkManager {
	/**
	 * Links data
	 */
	private static Map<String, LinkType> links = new ConcurrentHashMap<>();

	/**
	 * It deploys all links belonging to the specified NF-FG.
	 * 
	 * @param nffg
	 *            the NF-FG whose links must be deployed.
	 * @param overwrite
	 *            if the link is already present, it specifies whether the link
	 *            information should be overwritten or an error should be returned.
	 * @throws UnknownEntityException
	 *             if at least one link object (or its name) is null.
	 * @throws AllocationException
	 *             it some error occurred during allocation.
	 * @throws MalformedException
	 *             if at least one link object is malformed.
	 * @throws LinkAlreadyPresentException
	 *             if at least one of the links belonging to the NF-FG has already
	 *             been deployed (when {@code overwrite} is false ).
	 */
	protected static synchronized void deployLinks(NffgType nffg, boolean overwrite)
			throws UnknownEntityException, AllocationException, MalformedException, LinkAlreadyPresentException {
		// Argument checking
		if (nffg == null || nffg.getId() == null || "".compareTo(nffg.getId()) == 0)
			throw new UnknownEntityException("The NF-FG object is null.");

		// TODO debugging
		System.out.println("Deploying NF-FG " + nffg.getId() + " links...");

		// If the NF-FG does not exist yet
		if (!NffgManager.hasBeenDeployed(nffg))
			// The link cannot be deployed since it must belong to an NF-FG
			throw new AllocationException("Attempting to add links to a non-existing NF-FG.");

		// For each node belonging to the NF-FG
		for (NodeType node : nffg.getNodes().getNode()) {
			// TODO debugging
			System.out.println("<Node " + node.getId() + "> node.getLink(): " + node.getLink());
			System.out.println("<Node " + node.getId() + "> node.getLink().size(): " + node.getLink().size());

			// If the node has at least a link
			if (node.getLink() != null && node.getLink().size() > 0) {
				// TODO debugging
				System.out.println("Deploying links starting from " + node.getId() + "...");

				// For each link of the current node
				for (LinkType link : node.getLink())
					try {
						// TODO debugging
						System.out.println("Deploying link " + link.getId() + "...");

						deployLink(nffg.getId(), link, overwrite);
					} catch (UnknownEntityException | MalformedException | AllocationException
							| LinkAlreadyPresentException exception) {
						// TODO debugging
						System.out.println("Exception during the deployment of Link " + link.getId() + ": "
								+ exception.getMessage());

						// TODO Removing all previously-added links

						// TODO Remove the NF-FG from the nffgs local object
						// NffgManager.getNffgs().remove(nffg.getId());

						// Throw the exception to the caller
						throw exception;
					}
			} else {
				// TODO debugging
				System.out.println("Node " + node.getId() + " has no outgoing links...");
			}
		}
	}

	/**
	 * It deploys a link object on Neo4J.
	 * 
	 * @param link
	 *            the link to be deployed.
	 * @param nffgId
	 *            the NF-FG on which the link must be deployed on.
	 * @param overwrite
	 *            if the link is already present, it specifies whether the link
	 *            information should be overwritten or an error should be returned.
	 * @throws UnknownEntityException
	 *             if the link object (or its name) is null.
	 * @throws AllocationException
	 *             it some error occurred during allocation.
	 * @throws MalformedException
	 *             if the link object is malformed.
	 * @throws LinkAlreadyPresentException
	 *             if the link has already been deployed.
	 */
	public static void deployLink(String nffgId, LinkType link, boolean overwrite)
			throws UnknownEntityException, AllocationException, MalformedException, LinkAlreadyPresentException {
		// Argument checking
		if (link == null)
			throw new UnknownEntityException("The link object is null.");

		/**
		 * Name checking
		 */

		// Link name checking
		if (link.getId() == null || "".compareTo(link.getId()) == 0)
			throw new UnknownEntityException("The link has a null ID.");
		// Source node name checking
		else if (link.getSourceNode() == null || "".compareTo(link.getSourceNode()) == 0)
			throw new UnknownEntityException("Link " + link.getId() + " has a null source node ID.");
		// Destination node name checking
		else if (link.getDestinationNode() == null || "".compareTo(link.getDestinationNode()) == 0)
			throw new UnknownEntityException("Link " + link.getId() + " has a null destination node ID.");

		/**
		 * Non-existing object checks
		 */

		// If the NF-FG does not exist yet
		if (!NffgManager.hasBeenDeployed(nffgId))
			// The link cannot be deployed since it must belong to an NF-FG
			throw new AllocationException("Attempting to add link " + link.getId() + " to a non-existing NF-FG.");

		// If the source node does not exist yet
		if (!NodeManager.nodeHasBeenDeployed(nffgId, link.getSourceNode()))
			// The link cannot be deployed since it must start from a source node
			throw new AllocationException(
					"Attempting to add link " + link.getId() + " starting from a non-existing source node.");

		// If the destination node does not exist yet
		if (!NodeManager.nodeHasBeenDeployed(nffgId, link.getDestinationNode()))
			// The link cannot be deployed since it must end on a destination node
			throw new AllocationException(
					"Attempting to add link " + link.getId() + " ending on a non-existing destination node.");

		// If this link has already been deployed
		if (!overwrite && hasBeenDeployed(link))
			throw new LinkAlreadyPresentException("Link " + link.getId() + " has already been deployed.");

		// If this object is not well-formed
		if (!isWellFormed(link))
			throw new MalformedException("Link " + link.getId() + " object is malformed.");

		// TODO debugging
		System.out.println(
				"Deploying link from " + link.getSourceNode() + " towards " + link.getDestinationNode() + "...");

		// Retrieving this link's source and destination node IDs
		String sourceNodeId = NodeManager.getNeo4jId(nffgId, link.getSourceNode());
		String destinationNodeId = NodeManager.getNeo4jId(nffgId, link.getDestinationNode());

		// Deploying this link on Neo4J
		RelationshipManager.deployRelationship(sourceNodeId, destinationNodeId, "ForwardsTo");

		// Adding this new link to the links local map
		links.put(link.getId(), link);
	}

	/**
	 * It checks whether the specified link has alread been deployed or not.
	 * 
	 * @param link
	 *            the link to be checked.
	 * @return true if already deployed, false otherwise.
	 */
	public static boolean hasBeenDeployed(LinkType link) {
		return hasBeenDeployed(link.getId());
	}

	private synchronized static boolean hasBeenDeployed(String linkId) {
		return links.containsKey(linkId);
	}

	/**
	 * It checks whether the specified link object is well-formed or not.
	 * 
	 * @param link
	 *            the link to be checked.
	 * @return true if well-formed, false otherwise.
	 */
	public static boolean isWellFormed(LinkType link) {
		return (new NfvValidationProvider<LinkType>()).isReadable(link.getClass(), null, null, null);
	}

	public static synchronized Map<String, LinkType> getLinks() {
		return links;
	}
}
