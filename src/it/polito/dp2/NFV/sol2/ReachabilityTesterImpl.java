package it.polito.dp2.NFV.sol2;


import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.lab2.AlreadyLoadedException;
import it.polito.dp2.NFV.lab2.ExtendedNodeReader;
import it.polito.dp2.NFV.lab2.NoGraphException;
import it.polito.dp2.NFV.lab2.ReachabilityTester;
import it.polito.dp2.NFV.lab2.ServiceException;
import it.polito.dp2.NFV.lab2.UnknownNameException;

public class ReachabilityTesterImpl implements ReachabilityTester {
	/**
	 * Class containing all informations about the DP2-NFV system 
	 */
	/*TODO has to be private*/public NfvReader nfvReader;
	
	private WebTarget target;
	
	/**
	 * Keeps track of already-loaded NF-FGs
	 */
	private static Set<String> loadedNffgs;
	
	/**
	 * Keeps track of already-loaded nodes, with their 
	 * corresponding ID assigned by Neo4J.
	 */
	private static Map<String, String> loadedNodes;
	
	/**
	 * Keeps track of already-loaded hosts
	 */
	private static Map<String, String> loadedHosts;
	
	/**
	 * Default constructor
	 */
	public ReachabilityTesterImpl() {
		try {
			nfvReader = NfvReaderFactory.newInstance().newNfvReader();
		} catch (NfvReaderException | FactoryConfigurationError e) {
			System.err.println("An implementation of NfvReader cannot be created.");
			e.printStackTrace();
			System.exit(1);
		}
		
		Client client = ClientBuilder.newClient();
		target = client.target(getBaseURI());
	}
	
	static {
		loadedNffgs = new HashSet<String>();
		
		loadedNodes = new HashMap<String, String>();
		loadedHosts = new HashMap<String, String>();
	}
	
	/*TODO debug*/public static void main(String[] args) throws Exception {
		System.setProperty("it.polito.dp2.NFV.NfvReaderFactory", "it.polito.dp2.NFV.Random.NfvReaderFactoryImpl");
		ReachabilityTesterImpl rt = new ReachabilityTesterImpl();
		
		// Reading the corresponding NF-FG
		String nffgName = "Nffg0";
		NffgReader nffg = rt.nfvReader.getNffg(nffgName);
		
		// Nffg infos
		System.out.print("Trying to load NF-FG " + nffgName + " with " + nffg.getNodes().size() + " nodes, with ");
		int linksNumber = 0;
		for(NodeReader node: nffg.getNodes())
			for(LinkReader link: node.getLinks())
				++linksNumber;
		System.out.print(linksNumber + " links.");
		System.out.println("Content: \n");
		for(NodeReader node: nffg.getNodes()) {
			System.out.println(node.getName());
			for(LinkReader link: node.getLinks())
				System.out.println(
					"Link from " + link.getSourceNode().getName() +
					" to " + link.getDestinationNode().getName()
				);
			System.out.println();
		}
		
		/*TODO debug*/System.out.println("*** Loading nodes... ***");
		rt.loadNodes(nffg.getNodes());
		
		/*TODO debug*/System.out.println("*** Loading relationships... ***");
		rt.loadRelationships(nffg.getNodes());
	}
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/Neo4JSimpleXML/webapi/data/").build();
	}
	
	@Override
	public void loadGraph(String nffgName) 
			throws UnknownNameException, AlreadyLoadedException, ServiceException {
		
		// If the NF-FG name is null
		if(nffgName == null) {
			/*TODO debug*/System.err.println("The specified NF-FG name is null.");
			throw new UnknownNameException("The specified NF-FG name is null.");
		}
		
		// If the NF-FG has been already loaded
		if(loadedNffgs.contains(nffgName)) {
			/*TODO debug*/System.err.println(nffgName + " has been already loaded.");
			throw new AlreadyLoadedException(nffgName + " has been already loaded.");
		}
		
		// Reading the corresponding NF-FG
		NffgReader nffg = nfvReader.getNffg(nffgName);
		
		// If the NF-FG does not exist
		if(nffg == null) {
			/*TODO debug*/System.err.println("The specified NF-FG is unknown.");
			throw new UnknownNameException("The specified NF-FG is unknown.");
		}
		
		// TODO debug
		System.out.print("Trying to load NF-FG " + nffgName + " with " + nffg.getNodes().size() + " nodes, with ");
		int linksNumber = 0;
		for(NodeReader node: nffg.getNodes())
			for(LinkReader link: node.getLinks())
				++linksNumber;
		System.out.print(linksNumber + " links.");
		
		// Loading all hosts
		loadHosts(nfvReader.getHosts());
		
		// Loading all nodes
		loadNodes(nffg.getNodes());
		
		// Loading all relationships
		loadRelationships(nffg.getNodes());
		
		/**
		 * Adding this NF-FG to the set of already-loaded NF-FGs if
		 * successfully loaded
		 */
		loadedNffgs.add(nffgName);
	}
	
	/**
	 * Loads all nodes belonging to the specified node list {@code nodes}
	 * through the Neo4JSimpleXML web service.
	 * @param nodes					the node list to be uploaded to the 
	 * 								web service.
	 * @throws ServiceException		if any other error occurs when trying 
	 * 								to upload a node.
	 */
	private void loadNodes(Set<NodeReader> nodes) throws ServiceException {
		// For each node belonging to the NF-FG nodes list
		for(NodeReader node: nodes)
			try {
				/*TODO debug*/System.out.println("Loading node " + node.getName());
				
				// Load the single node
				loadNode(node);
			} catch(AlreadyLoadedException e) {
				System.err.println(e.getMessage());
				
				// If the node has been already loaded, skip it
				continue;
			}
	}
	
	/**
	 * Loads the specified node (if not already loaded) into the Neo4J 
	 * database through the Neo4JSimpleXML web service. 
	 * @param node							node to be loaded.
	 * @throws AlreadyLoadedException		if it already has been loaded.
	 * @throws ServiceException				if any other error occurs when 
	 * 										trying to upload the node.
	 */
	private void loadNode(NodeReader node) throws AlreadyLoadedException, ServiceException {
		// Checking if the node has been already loaded
		if(loadedNodes.containsKey(node.getName()))
			throw new AlreadyLoadedException(
				"Node " + node.getName() + "has been already uploaded."
			);
		
		// New temporary node creation
		Host tempNode = new Host();
		
		// Temporary node's properties
		Properties properties = new Properties();
		List<Property> propertiesList = properties.getProperty();
		
		// "name" property
		Property nameProperty = new Property();
		nameProperty.setName("name");
		nameProperty.setValue(node.getName());
		propertiesList.add(nameProperty);		
		
		// Setting the node's properties
		tempNode.setProperties(properties);
		
		// Loading the temporary node
		Host loadedNode;
		try {
			loadedNode = target	
	    		.path("node")
				.request()
				.post(Entity.entity(tempNode, MediaType.APPLICATION_XML), Host.class)
			;
		} catch(ProcessingException e) {
			throw new ServiceException("Could not load node " + node.getName());
		}
		
		// Storing the loaded node's Neo4j ID
		loadedNodes.put(node.getName(), loadedNode.getId());
		/*TODO debug*/System.out.println("loadedNodes: " + loadedNodes);
		
		// Temporary node's labels
		Labels labels = new Labels();
		List<String> labelsList = labels.getLabel();
		
		// "Node" label
		labelsList.add("Node");
		
		// Loading the node's labels
		try {
			target
				.path("node")
				.path(loadedNode.getId())
				.path("labels")
				.request()
				.post(Entity.entity(labels, MediaType.APPLICATION_XML))
			;
		} catch(ProcessingException e) {
			throw new ServiceException("Could not load node " + node.getName() + "'s label");
		}
		
		// If the node has been allocated on a physical host
		if(node.getHost() != null) {
			// New temporary relationship creation
			Relationship tempRelationship = new Relationship();
			String relationshipName = "AllocatedOn";
			
			// Setting the source node
			tempRelationship.setSrcNode(loadedNode.getId());
			
			// Setting the destination node
			tempRelationship.setDstNode(loadedHosts.get(node.getHost().getName()));
			
			/*TODO debug*/System.out.println(
				"Relationship b/w " + node.getName() + 
				" (" + loadedNode.getId() + ") and " +
				node.getHost().getName() + " (" + 
				loadedHosts.get(node.getHost().getName()) + ")"
			);
			
			// Setting the relationship type
			tempRelationship.setType(relationshipName);
			
			// Loading the temporary relationship
			try {
				target	
		    		.path("node")
		    		.path(loadedNode.getId())
		    		.path("relationships")
					.request()
					.post(Entity.entity(tempRelationship, MediaType.APPLICATION_XML), Relationship.class)
				;
			} catch(ProcessingException e) {
				throw new ServiceException(
					"Could not load \"" + relationshipName + "\" relationship between node " + 
					node.getName() + " and host " + node.getHost().getName()
				);
			}
		}			
	}
	
	/**
	 * Loads all relationships belonging to the specified node list {@code nodes}
	 * through the Neo4JSimpleXML web service.
	 * @param nodes					nodes from which relationships have to
	 * 								 be uploaded.
	 * @throws ServiceException		if any other error occurs when trying 
	 * 								to upload a relationship.
	 */
	private void loadRelationships(Set<NodeReader> nodes) throws ServiceException {
		// For each node belonging to the NF-FG nodes list
		for(NodeReader node: nodes)
			// For each node's link
			for(LinkReader link: node.getLinks())
					loadRelationship(link);
	}
	
	/**
	 * Load the specified relationship (if not already loaded) into the Neo4J 
	 * database through the Neo4JSimpleXML web service. 
	 * @param relationship			relationship to be loaded.
	 * @throws ServiceException		if any other error occurs when trying 
	 * 								to upload the relationship.
	 */
	private void loadRelationship(LinkReader relationship) throws ServiceException {
		// New temporary relationship creation
		Relationship tempRelationship = new Relationship();
		String relationshipName = "ForwardsTo";
		
		// Setting the source node
		tempRelationship.setSrcNode(loadedNodes.get(relationship.getSourceNode().getName()));
		
		// Setting the destination node
		tempRelationship.setDstNode(loadedNodes.get(relationship.getDestinationNode().getName()));
		
		/*TODO debug*/System.out.println(
			"Relationship b/w " + relationship.getSourceNode().getName() + 
			" (" + loadedNodes.get(relationship.getSourceNode().getName()) + ") and " +
			relationship.getDestinationNode().getName() + " (" + 
			loadedNodes.get(relationship.getDestinationNode().getName()) + ")"
		);
		
		// Setting the relationship type
		tempRelationship.setType(relationshipName);
		
		// Loading the temporary relationship
		try {
			target	
	    		.path("node")
	    		.path(loadedNodes.get(relationship.getSourceNode().getName()))
	    		.path("relationships")
				.request()
				.post(Entity.entity(tempRelationship, MediaType.APPLICATION_XML), Relationship.class)
			;
		} catch(ProcessingException e) {
				throw new ServiceException(
					"Could not load \"" + relationshipName + "\" relationship between node " + 
					relationship.getSourceNode().getName() +
					" and node " + relationship.getDestinationNode().getName()
				);
			}
	}

	/**
	 * Loads all hosts belonging to the specified host list {@code hosts}
	 * through the Neo4JSimpleXML web service.
	 * @param hosts					the host list to be uploaded to the
	 * 								web service.
	 * @throws ServiceException		if any other error occurs when trying 
	 * 								to upload a host.
	 */
	private void loadHosts(Set<HostReader> hosts) throws ServiceException {
		// For each node belonging to the NF-FG nodes list
		for(HostReader host: hosts)
			try {
				/*TODO debug*/System.out.println("Loading host " + host.getName());
				
				// Load the single node
				loadHost(host);
			} catch(AlreadyLoadedException e) {
				System.err.println(e.getMessage());
				
				// If the host has been already loaded, skip it
				continue;
			}
	}

	/**
	 * Loads the specified host (if not already loaded) into the Neo4J 
	 * database through the Neo4JSimpleXML web service. 
	 * @param host							host to be loaded.
	 * @throws AlreadyLoadedException		if it already has been loaded.
	 * @throws ServiceException				if any other error occurs when 
	 * 										trying to upload the host.
	 */
	private void loadHost(HostReader host) throws AlreadyLoadedException, ServiceException {
		// Checking if the host has been already loaded
		if(loadedHosts.containsKey(host.getName()))
			throw new AlreadyLoadedException(
				"Host " + host.getName() + "has been already uploaded."
			);
		
		// New temporary host creation
		Host tempHost = new Host();
		
		// Temporary node's properties
		Properties properties = new Properties();
		List<Property> propertiesList = properties.getProperty();
		
		// "name" property
		Property nameProperty = new Property();
		nameProperty.setName("name");
		nameProperty.setValue(host.getName());
		propertiesList.add(nameProperty);		
		
		// Setting the host's properties
		tempHost.setProperties(properties);
		
		// Loading the temporary host
		Host loadedHost;
		try {
			loadedHost = target	
	    		.path("node")
				.request()
				.post(Entity.entity(tempHost, MediaType.APPLICATION_XML), Host.class)
			;
		} catch(ProcessingException e) {
			throw new ServiceException("Could not load host " + host.getName());
		}
		
		// Storing the loaded host's Neo4j ID
		loadedHosts.put(host.getName(), loadedHost.getId());
		/*TODO debug*/System.out.println("loadedHosts: " + loadedHosts);
		
		// Temporary host's labels
		Labels labels = new Labels();
		List<String> labelsList = labels.getLabel();
		
		// "Node" label
		labelsList.add("Host");
		
		// Loading the host's labels
		try {
			target
				.path("node")
				.path(loadedHost.getId())
				.path("labels")
				.request()
				.post(Entity.entity(labels, MediaType.APPLICATION_XML))
			;
		} catch(ProcessingException e) {
			throw new ServiceException("Could not load host " + host.getName() + "'s label");
		}
	}
	
	@Override
	public Set<ExtendedNodeReader> getExtendedNodes(String nffgName)
			throws UnknownNameException, NoGraphException, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLoaded(String nffgName) throws UnknownNameException {
		// TODO Auto-generated method stub
		return false;
	}

}
