package it.polito.dp2.NFV.sol3.service.database;

import com.sun.jersey.api.client.ClientResponse;

import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;
import it.polito.dp2.NFV.sol3.service.neo4j.Labels;
import it.polito.dp2.NFV.sol3.service.neo4j.Localhost_Neo4JSimpleXMLWebapi;
import it.polito.dp2.NFV.sol3.service.neo4j.Node;
import it.polito.dp2.NFV.sol3.service.neo4j.Properties;
import it.polito.dp2.NFV.sol3.service.neo4j.Property;

public class NodeManager {
	/**
	 * It deploys a node into an NF-FG.
	 * @param nffgId	the NF-FG in which the node must be added.
	 * @param node		the node that must be deployed.
	 */
	public static void addNode(String nffgId, NodeType node) {
		// New node to be uploaded
		Node newNode = new Node();

		// Setting the new node's ID
		newNode.setId(node.getId());

		// New node's "name" property
		Property newNodeProperty = new Property();
		newNodeProperty.setName("name");
		newNodeProperty.setValue(node.getId());

		// New node's properties
		Properties newNodeProperties = new Properties();
		newNodeProperties.getProperty().add(newNodeProperty);

		// Setting the new node's properties
		newNode.setProperties(newNodeProperties);

		// Uploading the node
		ClientResponse nodeUploadResponse = Localhost_Neo4JSimpleXMLWebapi
				.data(Localhost_Neo4JSimpleXMLWebapi.createClient(), Localhost_Neo4JSimpleXMLWebapi.BASE_URI).node()
				.postXml(newNode, ClientResponse.class);

		// New node's "Node" label
		Labels newNodeLabels = new Labels();
		newNodeLabels.getLabel().add("Node");

		// Setting the new node's labels
		Localhost_Neo4JSimpleXMLWebapi
				.data(Localhost_Neo4JSimpleXMLWebapi.createClient(), Localhost_Neo4JSimpleXMLWebapi.BASE_URI)
				.nodeNodeidLabels(nodeUploadResponse.getEntity(Node.class).getId())
				.postXml(newNodeLabels, ClientResponse.class);
	}
}
