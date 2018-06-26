package it.polito.dp2.NFV.sol3.client1;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import it.polito.dp2.NFV.sol3.client1.managers.LinkManager;
import it.polito.dp2.NFV.sol3.client1.managers.NodeManager;
import it.polito.dp2.NFV.sol3.client1.readers.LinkReaderImpl;
import it.polito.dp2.NFV.sol3.client1.readers.NffgReaderImpl;
import it.polito.dp2.NFV.sol3.client1.readers.NodeReaderImpl;
import it.polito.dp2.NFV.sol3.service.gen.model.LinkType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;
import it.polito.dp2.NFV.sol3.service.gen.model.ObjectFactory;

public class DeployedNffgImpl implements DeployedNffg {

	/**
	 * NF-FG data
	 */
	private NffgReader nffg;
	private String nffgId;

	public DeployedNffgImpl(NffgType nffg) throws ServiceException {
		this.nffgId = nffg.getId();

		// TODO debugging
		System.out.println("Deployed Nffg " + nffgId);

		// Retrieving NF-FG reader
		this.nffg = new NffgReaderImpl(nffg, HostManager.retrieve(), CatalogManager.retrieve());
	}

	@Override
	public NodeReader addNode(VNFTypeReader type, String hostName) throws AllocationException, ServiceException {
		// Node object to uploaded
		NodeType nodeToUpload = new NodeType();

		// Setting properties
		nodeToUpload.setId(String.valueOf(NodeManager.nextId(nffgId)));
		nodeToUpload.setFunctionalType(type.getFunctionalType().name());
		nodeToUpload.setHost(hostName);

		// Uploading the node object
		// ClientResponse response = Localhost_NfvDeployerRest
		// .nffgs(Localhost_NfvDeployerRest.createClient(),
		// Localhost_NfvDeployerRest.BASE_URI)
		// .nffg_idNodes(nffgId).postXmlAs(new ObjectFactory().createNode(nodeToUpload),
		// ClientResponse.class);
		Response response = NfvClientImpl.getTarget().path("nffgs").path(nffgId).path("nodes")
				.request(MediaType.APPLICATION_XML)
				.post(Entity.entity(new ObjectFactory().createNode(nodeToUpload), MediaType.APPLICATION_XML));

		// Response analysis
		switch (response.getStatus()) {
		case 403:
			throw new AllocationException("Forbidden (allocation constraints not satisfied)");
		case 400:
			throw new ServiceException("The XML node object contained in the HTTP body was not valid.");
		case 404:
			throw new ServiceException("(NF-FG) Not found");
		}

		// Returning the corresponding reader
		return (new NodeReaderImpl(nodeToUpload, nffg, type, HostManager.retrieve(hostName)));
	}

	@Override
	public LinkReader addLink(NodeReader source, NodeReader dest, boolean overwrite)
			throws NoNodeException, LinkAlreadyPresentException, ServiceException {
		// Link object to be uploaded
		LinkType linkToUpload = new LinkType();

		// Setting properties
		linkToUpload.setId(String.valueOf(LinkManager.nextId(nffgId)));
		linkToUpload.setSourceNode(source.getName());
		linkToUpload.setDestinationNode(dest.getName());

		// Uploading the link object
//		ClientResponse response = Localhost_NfvDeployerRest
//				.nffgs(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
//				.nffg_idLinks(nffgId)
//				.postXmlAs(new ObjectFactory().createLink(linkToUpload), overwrite, ClientResponse.class);
		Response response = NfvClientImpl.getTarget().path("nffgs").path(nffgId).path("links").request(MediaType.APPLICATION_XML).post(Entity.entity(new ObjectFactory().createLink(linkToUpload), MediaType.APPLICATION_XML));

		// Response analysis
		switch (response.getStatus()) {
		case 400:
			throw new ServiceException("Bad request. The XML link object contained in the HTTP body was not valid.");
		case 403:
			throw new LinkAlreadyPresentException("Link was already present and overwrite was false.");
		case 404:
			throw new NoNodeException("No node was found.");
		}

		// Returning the corresponding reader
		return (new LinkReaderImpl(linkToUpload, NodeManager.retrieve(nffg, source.getName()),
				NodeManager.retrieve(nffg, dest.getName())));
	}

	@Override
	public NffgReader getReader() throws ServiceException {
		return nffg;
	}
}
