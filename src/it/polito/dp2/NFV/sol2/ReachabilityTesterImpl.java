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
	private NfvReader nfvReader;
	
	private WebTarget target;
	
	/**
	 * Keeps track of already-loaded NF-FGs
	 */
	private Set<String> loadedNffgs;
	
	/**
	 * Keeps track of already-loaded nodes, with their 
	 * corresponding ID assigned by Neo4J.
	 */
	private Map<String, String> loadedNodes;
	
	/**
	 * Keeps track of already-loaded hosts
	 */
	private Map<String, String> loadedHosts;
	
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
		
		loadedNffgs = new HashSet<String>();
		
		loadedNodes = new HashMap<String, String>();
		loadedHosts = new HashMap<String, String>();
	}

	/**
	 * Retrieves the corresponding NF-FG reader object, performing all
	 * needed checks.
	 * @param nffgName					the NF-FG's name of which the
	 * 									reader object should be returned.
	 * @return							the NF-FG reader object.
	 * @throws UnknownNameException		if the name of the NF-FG is unknown or null.
	 */
	private NffgReader getNffg(String nffgName) throws UnknownNameException {
		// If the NF-FG name is null
		if(nffgName == null)
			throw new UnknownNameException("The specified NF-FG name is null.");
		
		// Retrieving the corresponding NF-FG
		NffgReader nffg = nfvReader.getNffg(nffgName);
		
		// If the NF-FG does not exist
		if(nffg == null)
			throw new UnknownNameException("The specified NF-FG is unknown.");
		
		return nffg;
	}
	
	/**
	 * Returns the base URI of the Neo4JSimpleXML web service.
	 * @return	base Neo4JSimpleXML's URI.
	 */
	private static URI getBaseURI() {
		String baseURI;
		try {
			baseURI = System.getProperty("it.polito.dp2.NFV.lab2.URL");
		} catch(SecurityException | NullPointerException e) {
			baseURI = "http://localhost:8080/Neo4JSimpleXML/webapi";
		}
		
		return UriBuilder.fromUri(baseURI + "/data/").build();
	}

	@Override
	public void loadGraph(String nffgName) 
			throws UnknownNameException, AlreadyLoadedException, ServiceException {
		// System.out.println("loadedNffgs: " + loadedNffgs);
		
		// Retrieving the NF-FG reader
		NffgReader nffg = getNffg(nffgName);
		
		// If the NF-FG has already been loaded
		if(loadedNffgs.contains(nffgName)) {
			// System.out.println(nffgName + " has already been loaded.");
			
			throw new AlreadyLoadedException(nffgName + " has already been loaded.");
		}
		
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
	 * Loads the specified host (if not already loaded) into the Neo4J 
	 * database through the Neo4JSimpleXML web service. 
	 * @param host							host to be loaded.
	 * @throws AlreadyLoadedException		if it already has been loaded.
	 * @throws ServiceException				if any other error occurs when 
	 * 										trying to upload the host.
	 */
	private void loadHost(HostReader host) throws AlreadyLoadedException, ServiceException {
		// Checking if the host has already been loaded
		if(loadedHosts.containsKey(host.getName())) {
			// System.out.println("Host " + host.getName() + "has already been uploaded.");
			
			throw new AlreadyLoadedException(
				"Host " + host.getName() + "has already been uploaded."
			);	
		}
		
		// If the host has no node allocated on, it won't be uploaded
		if(host.getNodes().size() == 0)
			return;
		
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
		// System.out.println("loadedHosts: " + loadedHosts);
		
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
				// System.out.println("Loading node " + node.getName());
				
				// Load the single node
				loadNode(node);
			} catch(AlreadyLoadedException e) {
				System.err.println(e.getMessage());
				
				// If the node has already been loaded, skip it
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
		// Checking if the node has already been loaded
		if(loadedNodes.containsKey(node.getName())) {
			// System.out.println("Node " + node.getName() + " has already been uploaded.");
			
			throw new AlreadyLoadedException(
				"Node " + node.getName() + " has already been uploaded."
			);
		}
		
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
		Node loadedNode;
		try {
			loadedNode = target	
	    		.path("node")
				.request()
				.post(Entity.entity(tempNode, MediaType.APPLICATION_XML), Node.class)
			;
		} catch(ProcessingException e) {
			throw new ServiceException("Could not load node " + node.getName());
		}
		
		// Storing the loaded node's Neo4j ID
		loadedNodes.put(node.getName(), loadedNode.getId());
		// System.out.println("loadedNodes: " + loadedNodes);
		
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
			try {
				// Load the corresponding host object to the Neo4J database
				loadHost(node.getHost());
			} catch(AlreadyLoadedException e) {
				/*
				 * If the host object has already been uploaded,
				 * nothing has to be done.
				 */
			}
			
			// New temporary relationship creation
			Relationship tempRelationship = new Relationship();
			String relationshipName = "AllocatedOn";
			
			// Setting the source node
			tempRelationship.setSrcNode(loadedNode.getId());
			
			/**
			 * Setting the destination node: this is the reason why
			 * all hosts must be loaded before nodes.
			 */
			tempRelationship.setDstNode(loadedHosts.get(node.getHost().getName()));
			
			/*System.out.println(
				"Relationship b/w " + node.getName() + 
				" (" + loadedNode.getId() + ") and " +
				node.getHost().getName() + " (" + 
				loadedHosts.get(node.getHost().getName()) + ")"
			);*/
			
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
		
		/* System.out.println(
			"Relationship b/w " + relationship.getSourceNode().getName() + 
			" (" + loadedNodes.get(relationship.getSourceNode().getName()) + ") and " +
			relationship.getDestinationNode().getName() + " (" + 
			loadedNodes.get(relationship.getDestinationNode().getName()) + ")"
		);*/
		
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

	@Override
	public Set<ExtendedNodeReader> getExtendedNodes(String nffgName)
			throws UnknownNameException, NoGraphException, ServiceException {
		// System.out.println("Requesting extended nodes for NF-FG " + nffgName);
		
		// Retrieving the NF-FG reader
		NffgReader nffg = getNffg(nffgName);
		
		// If the NF-FG exists but it has not been loaded
		if(!loadedNffgs.contains(nffg.getName())) {
			// System.out.println("The specified NF-FG has not been loaded.");
			
			throw new NoGraphException("The specified NF-FG has not been loaded.");
		}
		
		// System.out.println("The specified NF-FG exist: proceeding...");
		
		// This function's result
		Set<ExtendedNodeReader> result = new HashSet<ExtendedNodeReader>();
		
		// For all nodes belonging to the specified NF-FG
		for(NodeReader node: nffg.getNodes()) {
			NodeType nodeInfo = new NodeType();
			nodeInfo.setFunctionalType(node.getFuncType().getName());
			nodeInfo.setHost(node.getHost().getName());
			nodeInfo.setId(node.getName());
			
			result.add(
				new MyExtendedNodeReader(
					node, 
					nodeInfo, 
					loadedNodes.get(node.getName()),
					nfvReader,
					target
				)
			);
		}
		
		return result;
	}

	@Override
	public boolean isLoaded(String nffgName) throws UnknownNameException {
		// Checking the NF-FG name
		getNffg(nffgName);
		
		return loadedNffgs.contains(nffgName);
	}
}
