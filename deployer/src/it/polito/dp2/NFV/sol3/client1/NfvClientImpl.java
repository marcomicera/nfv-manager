package it.polito.dp2.NFV.sol3.client1;

import java.util.List;

import com.sun.jersey.api.client.ClientResponse;

import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.LinkDescriptor;
import it.polito.dp2.NFV.lab3.NffgDescriptor;
import it.polito.dp2.NFV.lab3.NfvClient;
import it.polito.dp2.NFV.lab3.NodeDescriptor;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.LinkType;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.Localhost_NfvDeployerRest;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.NffgType;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.NodeType;

public class NfvClientImpl implements NfvClient {

	/**
	 * System property by which the NfvDeployer web service's URI should be
	 * retrieved.
	 */
	/*
	 * private static final String NFV_DEPLOYER_URI_PROPERTY =
	 * "it.polito.dp2.NFV.lab3.URL", NFV_DEPLOYER_PORT_PROPERTY =
	 * "it.polito.dp2.NFV.lab3.PORT";
	 */

	/**
	 * Default NfvDeployer web service's URI
	 */
	// private static final String NFV_DEPLOYER_DEFAULT_URI =
	// "http://localhost:8080/NfvDeployer/rest/";

	/**
	 * Base URI of the NfvDeployer web service.
	 */
	// private URI nfvDeployerBaseURI;

	@Override
	public DeployedNffg deployNffg(NffgDescriptor nffg) throws AllocationException, ServiceException {
		// NF-FG object to be uploaded to the NfvDeployer web service
		NffgType nffgToUpload = new NffgType();
		List<NodeType> nffgNodesToUpload = nffgToUpload.getNodes().getNode();

		// For each node to be deployed
		for (NodeDescriptor node : nffg.getNodes()) {
			/*
			 * Temporary node object to be added to the list of nodes of the NF-FG object to
			 * be uploaded
			 */
			NodeType tempNode = new NodeType();
			
			// Setting properties
			tempNode.setFunctionalType(node.getFuncType().getFunctionalType().value());
			tempNode.setHost(node.getHostName());
			
			// Temporary node link list
			List<LinkType> tempNodeLinkList = tempNode.getLink();
			
			// For each link of this node to be deployed
			for (LinkDescriptor nodeLink : node.getLinks()) {
				// Temporary link object to be added to the current node object
				LinkType tempNodeLink = new LinkType();
				
				// Setting properties
				//tempNodeLink.setSourceNode(nodeLink.getSourceNode());
			}
		}

		// Deploying the specified NF-FG
		Localhost_NfvDeployerRest.nffgs(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
				.putXmlAs(nffgToUpload, ClientResponse.class);

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeployedNffg getDeployedNffg(String name) throws UnknownEntityException, ServiceException {
		// Retrieving the NF-FG from the NfvDeployer web service
		NffgType retrievedNffg = Localhost_NfvDeployerRest
				.nffgs(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI).nffg_id(name)
				.getAsXml(NffgType.class);

		// Retrieving a DeployedNffg interface implementation
		DeployedNffgImpl result = new DeployedNffgImpl(retrievedNffg);

		return result;
	}

	/**
	 * Returns the base URI of the NfvDeployer web service.
	 * 
	 * @return base NfvDeployer's URI.
	 */
	/*
	 * private static URI getBaseURI() { String baseURI; String port;
	 * 
	 * try { baseURI = System.getProperty(NFV_DEPLOYER_URI_PROPERTY); } catch
	 * (SecurityException | NullPointerException e) { baseURI =
	 * NFV_DEPLOYER_DEFAULT_URI; }
	 * 
	 * // If there was no property containing the NfvDeployer URI if(baseURI ==
	 * null) { try { port = System.getProperty(NFV_DEPLOYER_PORT_PROPERTY); } catch
	 * (SecurityException | NullPointerException e) { port = "8080"; }
	 * 
	 * // If there was no property containing the NfvDeployer port if(port != null)
	 * { baseURI = NFV_DEPLOYER_DEFAULT_URI; } else { baseURI = "http://localhost:"
	 * + port + "/NfvDeployer/rest/"; } }
	 * 
	 * return UriBuilder.fromUri(baseURI).build(); }
	 */

}
