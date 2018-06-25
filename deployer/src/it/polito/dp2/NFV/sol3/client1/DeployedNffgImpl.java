package it.polito.dp2.NFV.sol3.client1;

import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.client.ClientResponse;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.LinkAlreadyPresentException;
import it.polito.dp2.NFV.lab3.NoNodeException;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.client1.managers.CatalogManager;
import it.polito.dp2.NFV.sol3.client1.managers.HostManager;
import it.polito.dp2.NFV.sol3.client1.managers.NodeManager;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.HostType;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.HostsType;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.LinkType;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.Localhost_NfvDeployerRest;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.NffgType;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.NodeType;
import it.polito.dp2.NFV.sol3.client1.readers.HostReaderImpl;
import it.polito.dp2.NFV.sol3.client1.readers.LinkReaderImpl;
import it.polito.dp2.NFV.sol3.client1.readers.NffgReaderImpl;
import it.polito.dp2.NFV.sol3.client1.readers.NodeReaderImpl;

public class DeployedNffgImpl implements DeployedNffg {

	/**
	 * NF-FG data
	 */
	private NffgReader nffg;

	public DeployedNffgImpl(NffgType nffg) {
		// Retrieving NF-FG reader
		this.nffg = new NffgReaderImpl(nffg, retrieveHosts(), CatalogManager.retrieve());
	}

	@Override
	public NodeReader addNode(VNFTypeReader type, String hostName) throws AllocationException, ServiceException {
		// Node object to uploaded
		NodeType nodeToUpload = new NodeType();

		// Setting properties
		nodeToUpload.setFunctionalType(type.getFunctionalType().value().toString());
		nodeToUpload.setHost(hostName);

		// Uploading the node object
		Localhost_NfvDeployerRest.nffgs(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
				.nffg_idNodes(nffg.getName()).putXmlAs(nodeToUpload, ClientResponse.class);

		// Returning the corresponding reader
		return (new NodeReaderImpl(nodeToUpload, nffg, type, HostManager.retrieve(hostName)));
	}

	@Override
	public LinkReader addLink(NodeReader source, NodeReader dest, boolean overwrite)
			throws NoNodeException, LinkAlreadyPresentException, ServiceException {
		// Link object to be uploaded
		LinkType linkToUpload = new LinkType();

		// Setting properties
		linkToUpload.setSourceNode(source.getName());
		linkToUpload.setDestinationNode(dest.getName());

		// Uploading the link object
		Localhost_NfvDeployerRest.nffgs(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
				.nffg_idLinks(nffg.getName()).putXmlAs(linkToUpload, overwrite, ClientResponse.class);

		// Returning the corresponding reader
		return (new LinkReaderImpl(linkToUpload, NodeManager.retrieve(nffg, source.getName()),
				NodeManager.retrieve(nffg, dest.getName())));
	}

	@Override
	public NffgReader getReader() throws ServiceException {
		return nffg;
	}

	/**
	 * Retrieves all host readers from the NfvDeployer web service.
	 * 
	 * @return all deployed hosts readers.
	 */
	private Map<String, HostReader> retrieveHosts() {
		// Retrieving hosts
		HostsType retrievedHosts = Localhost_NfvDeployerRest
				.hosts(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
				.getAsXml(HostsType.class);

		// Building the result map
		final Map<String, HostReader> result = new HashMap<>();
		for (final HostType retrievedHost : retrievedHosts.getHost()) {
			result.put(retrievedHost.getId(), new HostReaderImpl(retrievedHost));
		}

		return result;
	}
}
