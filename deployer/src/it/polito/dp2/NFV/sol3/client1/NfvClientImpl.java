package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.ClientResponse;

import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.LinkDescriptor;
import it.polito.dp2.NFV.lab3.NffgDescriptor;
import it.polito.dp2.NFV.lab3.NfvClient;
import it.polito.dp2.NFV.lab3.NodeDescriptor;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;
import it.polito.dp2.NFV.sol3.client1.managers.NffgManager;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.Localhost_NfvDeployerRest;
import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodesType;
import it.polito.dp2.NFV.sol3.service.gen.model.ObjectFactory;

public class NfvClientImpl implements NfvClient {

	/**
	 * System property by which the NfvDeployer web service's URI should be
	 * retrieved.
	 */
	private static final String NFV_DEPLOYER_URI_PROPERTY = "it.polito.dp2.NFV.lab3.URL",
			NFV_DEPLOYER_PORT_PROPERTY = "it.polito.dp2.NFV.lab3.PORT";

	/**
	 * Default NfvDeployer web service's URI
	 */
	private static final String NFV_DEPLOYER_DEFAULT_URI = "http://localhost:8080/NfvDeployer/rest/";

	/**
	 * Base URI of the NfvDeployer web service.
	 */
	// private URI nfvDeployerBaseURI;

	private static WebTarget target;

	static {
		target = ClientBuilder.newClient().target(getBaseURI());
	}

	@Override
	public DeployedNffg deployNffg(NffgDescriptor nffg) throws AllocationException, ServiceException {
		// NF-FG object to be uploaded to the NfvDeployer web service
		NffgType nffgToUpload = new NffgType();

		// Nodes to be uploaded within the NF-FG object
		NodesType nffgNodesToUpload = new NodesType();
		List<NodeType> nffgNodesListToUpload = nffgNodesToUpload.getNode();

		// Local nodes map
		Map<NodeDescriptor, NodeType> nodesMap = new HashMap<>();

		// Next NF-FG ID that can be used
		int nextNffgId = NffgManager.nextId();

		// TODO debugging
		System.out.println("Deploying Nffg" + nextNffgId + "...");

		// Next node ID that can be used in this NF-FG
		int nextNodeId = 0;

		// Setting properties
		nffgToUpload.setId("Nffg" + nextNffgId);
		nffgToUpload.setDeployTime(null);

		// For each node to be deployed
		for (NodeDescriptor node : nffg.getNodes()) {
			/*
			 * Temporary node object to be added to the list of nodes of the NF-FG object to
			 * be uploaded
			 */
			NodeType tempNode = new NodeType();

			// TODO debugging
			System.out.println("Setting the following node ID: " + node.getFuncType().getName() + (nextNodeId + 1)
					+ "Nffg" + nextNffgId);

			// Setting properties
			tempNode.setId(node.getFuncType().getName() + (nextNodeId++) + "Nffg" + nextNffgId);
			tempNode.setFunctionalType(node.getFuncType().getName());
			tempNode.setHost(node.getHostName());

			// Adding temporary node to the NF-FG object to be uploaded
			nffgNodesListToUpload.add(tempNode);

			// Adding temporary node to the local nodes map
			nodesMap.put(node, tempNode);
		}

		// Deploying nodes links
		for (Entry<NodeDescriptor, NodeType> nodesMapEntry : nodesMap.entrySet()) {
			// Node object to uploaded
			NodeType nodeToUpload = nodesMapEntry.getValue();

			// Node descriptor containing data inserted by the client
			NodeDescriptor node = nodesMapEntry.getKey();

			// Link list of the node object to be uploaded
			List<LinkType> nodeLinksToUpload = nodeToUpload.getLink();

			// Next link ID that can be used in this NF-FG
			int nextLinkId = 0;

			for (LinkDescriptor link : node.getLinks()) {
				// Temporary link object to be uploaded
				LinkType tempLink = new LinkType();

				// Setting properties
				tempLink.setId("Link" + (nextLinkId++));
				tempLink.setSourceNode(nodesMap.get(link.getSourceNode()).getId());
				tempLink.setDestinationNode(nodesMap.get(link.getDestinationNode()).getId());
				tempLink.setMaximumLatency(link.getLatency());
				tempLink.setMinimumThroughput(link.getThroughput());

				/*
				 * Adding the temporary link object to the link list of the node object to be
				 * uploaded
				 */
				nodeLinksToUpload.add(tempLink);
			}
		}

		// Setting nodes to the NF-FG object to upload
		nffgToUpload.setNodes(nffgNodesToUpload);

		// Deploying the specified NF-FG
		// Localhost_NfvDeployerRest.nffgs(Localhost_NfvDeployerRest.createClient(),
		// Localhost_NfvDeployerRest.BASE_URI)
		// .postXmlAs(new ObjectFactory().createNffg(nffgToUpload),
		// ClientResponse.class);
		Response response = target.path("nffgs").request(MediaType.APPLICATION_XML)
				.post(Entity.entity(new ObjectFactory().createNffg(nffgToUpload), MediaType.APPLICATION_XML));

		// TODO debugging
		System.out.println("NF-FG deployment request finished. Response code: " + response.getStatus() + "\nAllow: " + response.getAllowedMethods());
		
		// Response analysis
		switch (response.getStatus()) {
		case 400:
			throw new ServiceException("Bad request. The XML NF-FG object contained in the HTTP body was not valid.");
		case 500:
			throw new AllocationException("Internal server error: it was not possible to create the specified NF-FG.");
		}

		// Returning an NF-FG interface implementation
		return new DeployedNffgImpl(nffgToUpload);
	}

	@Override
	public DeployedNffg getDeployedNffg(String name) throws UnknownEntityException, ServiceException {
		// Retrieving the NF-FG from the NfvDeployer web service
		// NffgType retrievedNffg = Localhost_NfvDeployerRest
		// .nffgs(Localhost_NfvDeployerRest.createClient(),
		// Localhost_NfvDeployerRest.BASE_URI).nffg_id(name)
		// .getAsXml(NffgType.class);
		NffgType retrievedNffg = target.path("nffgs").path(name).request(MediaType.APPLICATION_XML).get(NffgType.class);

		// Retrieving a DeployedNffg interface implementation
		DeployedNffgImpl result = new DeployedNffgImpl(retrievedNffg);

		return result;
	}

	/**
	 * Returns the base URI of the NfvDeployer web service.
	 * 
	 * @return base NfvDeployer's URI.
	 */
	private static URI getBaseURI() {
		String baseURI;
		String port;

		try {
			baseURI = System.getProperty(NFV_DEPLOYER_URI_PROPERTY);
		} catch (SecurityException | NullPointerException e) {
			baseURI = NFV_DEPLOYER_DEFAULT_URI;
		}

		// If there was no property containing the NfvDeployer URI
		if (baseURI == null) {
			try {
				port = System.getProperty(NFV_DEPLOYER_PORT_PROPERTY);
			} catch (SecurityException | NullPointerException e) {
				port = "8080";
			}

			// If there was no property containing the NfvDeployer port
			if (port != null) {
				baseURI = NFV_DEPLOYER_DEFAULT_URI;
			} else {
				baseURI = "http://localhost:" + port + "/NfvDeployer/rest/";
			}
		}
		
		// TODO debugging
		System.out.println("base URI: " + baseURI);

		return UriBuilder.fromUri(baseURI).build();
	}

	public static synchronized WebTarget getTarget() {
		return target;
	}
}
