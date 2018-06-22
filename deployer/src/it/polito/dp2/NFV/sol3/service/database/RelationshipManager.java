package it.polito.dp2.NFV.sol3.service.database;

import com.sun.jersey.api.client.ClientResponse;

import it.polito.dp2.NFV.sol3.service.neo4j.Localhost_Neo4JSimpleXMLWebapi;
import it.polito.dp2.NFV.sol3.service.neo4j.Relationship;

public class RelationshipManager {
	/**
	 * Last relationship ID used in Neo4J.
	 */
	private static int lastRelationshipId = 0;

	/**
	 * It adds a relationship between a Neo4J source node and a Neo4J destination
	 * node.
	 * 
	 * @param sourceId
	 *            the Neo4J source node ID on which the relationship must start
	 *            from.
	 * @param destinationId
	 *            the Neo4J destination ID on which the relationship must end up to.
	 * @param type
	 *            the relationship type.
	 */
	public static void deployRelationship(String sourceId, String destinationId, String type) {
		// New relationship object to be deployed
		Relationship allocatedOnRelationship = new Relationship();
		allocatedOnRelationship.setSrcNode(sourceId);
		allocatedOnRelationship.setDstNode(destinationId);
		allocatedOnRelationship.setId(Integer.toString(++lastRelationshipId));
		allocatedOnRelationship.setType(type);

		// Deploying the relationship object
		Localhost_Neo4JSimpleXMLWebapi
				.data(Localhost_Neo4JSimpleXMLWebapi.createClient(), Localhost_Neo4JSimpleXMLWebapi.BASE_URI)
				.nodeNodeidRelationships(sourceId).postXml(allocatedOnRelationship, ClientResponse.class);
	}
}
