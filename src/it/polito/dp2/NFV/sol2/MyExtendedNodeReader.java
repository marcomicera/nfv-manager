package it.polito.dp2.NFV.sol2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.lab2.ExtendedNodeReader;
import it.polito.dp2.NFV.lab2.NoGraphException;
import it.polito.dp2.NFV.lab2.ServiceException;

public class MyExtendedNodeReader extends MyNodeReader implements ExtendedNodeReader {
	private Set<HostReader> reachableHosts;
	private Nodes.Node neo4jNode;
	private NfvReader nfvReader;
	private WebTarget target;
	
	public MyExtendedNodeReader(NodeReader node, 
								NodeType info,
								String neo4jId,
								NfvReader nfvReader,
								WebTarget target) {
		super(
			info, 
			node.getNffg(),
			node.getFuncType(),
			node.getHost()
		);
		
		setLinks();
		
		neo4jNode = new Nodes.Node();
		neo4jNode.setId(neo4jId);
		
		this.nfvReader = nfvReader;
		this.target = target;
		
		reachableHosts = new HashSet<HostReader>();	
	}
	
	@Override
	public Set<HostReader> getReachableHosts() throws NoGraphException, ServiceException {
		// TODO debug
		System.out.println("Retrieving reachable hosts from node " + info.id + " (Neo4j ID: " + neo4jNode.id + ")");
		
		setReachableHosts();
		return reachableHosts;
	}
	
	private void setReachableHosts() throws ServiceException {
		Set<String> visitedNodeIds = new HashSet<String>();
		visit(neo4jNode, visitedNodeIds, reachableHosts);
	}
	
	// recursively visits reachable nodes and collects reachable hosts
	private void visit(Nodes.Node node, Set<String> visitedNodeIds, Set<HostReader> visitedNodeReachableHosts)
			throws ServiceException {
		// TODO debug
		System.out.println("Visiting node with ID " + node.id);
		
		// If this node has not been visited yet
		if(!visitedNodeIds.contains(node.id)) {
			// TODO debug
			System.out.println("This node " + node.id + " has not been visited before");
			// Add the current node to the set of visited ones
			visitedNodeIds.add(node.id);
			
			// Retrieving the physical host on which this node is allocated on
			List<Nodes.Node> neo4jHostResponse = getReachableEntities(
				node.id,
				"AllocatedOn",
				"Host"
			);
			
			// TODO debug
			System.out.println("Node " + node.id + " hosts: " + neo4jHostResponse);
			
			/*
			 * Adding the physical host to the physical set of reachable
			 * hosts if not null 
			 */
			if(neo4jHostResponse.size() >= 1) {
				// Retrieving the actual host Neo4j object
				Nodes.Node neo4jHost = neo4jHostResponse.get(0);
				
				// TODO debug
				System.out.println("Node " + node.id + " is allocated on host " + neo4jHost.id);
				
				// Retrieving the host name
				String hostName = null;
				for(Property property: neo4jHost.getProperties().getProperty())
					if(property.getName().compareTo("name") == 0) {
						hostName = property.getValue();
						break;
					}
				
				// TODO debug
				System.out.println("Node " + node.id + " is allocated on a host named " + hostName);
				
				// Adding the host reader to the set of reachable hosts
				if(hostName == null)
					throw new ServiceException(
						"Host having ID " + neo4jHost.id +
						" has no \"name\" property."
					);
				else {
					visitedNodeReachableHosts.add(nfvReader.getHost(hostName));
					
					// TODO debug
					System.out.println("Node " + node.id + " reachableHosts: " + reachableHosts);
				}
			}
			
			List<Nodes.Node> reachableNodes = getReachableEntities(
				node.id,
				"ForwardsTo",
				"Node"
			);
			
			// Visiting neighbors nodes
			for(Nodes.Node neighbor: reachableNodes) {
				if(neighbor.id != node.id)
					visit(neighbor, visitedNodeIds, visitedNodeReachableHosts);
			}
		} else {
			// TODO debug
			System.out.println("This node " + node.id + " has already been visited");
		}
	}

	private List<Nodes.Node> getReachableEntities(	String entityId, 
													String relationshipType, 
													String entityType) 
													throws ServiceException {
		// Loading reachable nodes
		Nodes reachableNodes = null;
		try {
			reachableNodes = target	
				.path("node")
				.path(entityId)
				.path("reachableNodes")
				.queryParam("relationshipTypes", relationshipType)
				.queryParam("nodeLabel", entityType)
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get(Nodes.class)
			;
		} catch(ProcessingException e) {
			throw new ServiceException(
				"Could not get reachable " + entityType + "s from " + entityId
			);
		}
		
		return reachableNodes == null ? new ArrayList<Nodes.Node>() : reachableNodes.getNode();
	}
	
	/*private Node getEntityInfo(String entityId) throws ServiceException {
		Node node = null;
		
		try {
			node = target	
				.path("node")
				.path(entityId)
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get(Node.class)
			;
		} catch(ProcessingException e) {
			throw new ServiceException(
				"Could not get " + entityId + "'s informations"
			);
		}
		
		return node;
	}*/
}
