package it.polito.dp2.NFV.sol3.client1.managers;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.client1.NfvClientImpl;
import it.polito.dp2.NFV.sol3.client1.readers.NodeReaderImpl;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;

public class NodeManager {

	/**
	 * Retrieves a single node reader object from the NfvDeployer web service.
	 * 
	 * @param nodeId
	 *            the desired node's ID.
	 * @param nffg
	 *            the NF-FG reader object to which the node belongs to.
	 * @return the node reader object.
	 */
	public static NodeReader retrieve(NffgReader nffg, String nodeId) {
		// Retrieving the node object
		// NodeType retrievedNode = Localhost_NfvDeployerRest
		// .nffgs(Localhost_NfvDeployerRest.createClient(),
		// Localhost_NfvDeployerRest.BASE_URI)
		// .nffg_idNodesNode_id(nffg.getName(), nodeId).getAsXml(NodeType.class);
		NodeType retrievedNode = NfvClientImpl.getTarget().path("nffgs").path(nffg.getName()).path("nodes").path(nodeId)
				.request(MediaType.APPLICATION_XML).get(NodeType.class);

		// Return the corresponding reader
		return (new NodeReaderImpl(retrievedNode, nffg, retrieveFunctionalType(retrievedNode.getFunctionalType()),
				HostManager.retrieve(retrievedNode.getHost())));
	}

	/**
	 * Returns a VNF functional type reader object.
	 * 
	 * @param type
	 *            the VNF functional type whose reader must be returned.
	 * @return the desired VNF functional type reader object.
	 */
	public static VNFTypeReader retrieveFunctionalType(String type) {
		// Retrieving the functional type reader
		// TODO This could be improved by adding the catalog/{vnf_type} resource
		Map<String, VNFTypeReader> retrievedCatalog = CatalogManager.retrieve();

		// Returning the desired functional type reader
		return retrievedCatalog.get(type);
	}

	/**
	 * Retrieves the next node ID that can be used in the specified NF-FG ID.
	 * 
	 * @param nffgId
	 *            the NF-FG ID of which the next usable node ID must be returned.
	 * @return the next usable node ID in the NF-FG.
	 */
	public static int nextId(String nffgId) {
//		return 1 + Integer.valueOf(Localhost_NfvDeployerRest
//				.nffgs(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
//				.nffg_idNodesHowmany(nffgId).getAsTextPlain(String.class));
		
		return 1 + Integer.valueOf(NfvClientImpl.getTarget().path("nffgs").path(nffgId).path("nodes").path("howmany").request(MediaType.TEXT_PLAIN).get(String.class));
	}
}
