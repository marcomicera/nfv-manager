package it.polito.dp2.NFV.sol3.client1.managers;

import java.util.Map;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.Localhost_NfvDeployerRest;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.NodeType;
import it.polito.dp2.NFV.sol3.client1.readers.NodeReaderImpl;

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
		NodeType retrievedNode = Localhost_NfvDeployerRest
				.nffgs(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
				.nffg_idNodesNode_id(nffg.getName(), nodeId).getAsXml(NodeType.class);

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
}
