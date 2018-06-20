package it.polito.dp2.NFV.sol3.service.database;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.sun.jersey.api.client.ClientResponse;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodesType;
import it.polito.dp2.NFV.sol3.service.neo4j.Labels;
import it.polito.dp2.NFV.sol3.service.neo4j.Localhost_Neo4JSimpleXMLWebapi;
import it.polito.dp2.NFV.sol3.service.neo4j.Node;
import it.polito.dp2.NFV.sol3.service.neo4j.Properties;
import it.polito.dp2.NFV.sol3.service.neo4j.Property;

public class NffgManager {
	/**
	 * NF-FGs data
	 */
	protected static Map<String, NffgType> nffgs = new HashMap<>();
	
	/**
	 * It downloads a specific NF-FG.
	 * @param nffgName	the NF-FG name to be downloaded.
	 * @param monitor	NfvReader interface through which data must be retrieved.
	 * @return			the downloaded NF-FG.
	 */
	protected static NffgType downloadNffg(String nffgName, NfvReader monitor) {
		// Retrieving data about the Nffg
		NffgReader retrievedNffg = monitor.getNffg(nffgName);

		// Nffg JAXB annotated class
		NffgType newNffg = new NffgType();
		newNffg.setId(nffgName);

		// Setting the Nffg JAXB annotated class' deploy time
		GregorianCalendar gregorianDeployTime = new GregorianCalendar();
		gregorianDeployTime.setTime(retrievedNffg.getDeployTime().getTime());
		try {
			newNffg.setDeployTime(
				DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianDeployTime)
			);
		} catch(DatatypeConfigurationException e) {
			System.err.println("Could not convert deploy tiem in Gregorian format");
		}
		
		// Nffg's nodes JAXB annotated class
		NodesType newNffgNodes = new NodesType();
		
		// Nffg's node list
		List<NodeType> newNffgNodeList = newNffgNodes.getNode();
		for(NodeReader node: retrievedNffg.getNodes()) {
			// New temporary Nffg node object
			NodeType newNffgNode = new NodeType();
			
			// Initializing the new temporary Nffg node object
			newNffgNode.setId(node.getName());
			newNffgNode.setHost(node.getHost().getName());
			newNffgNode.setFunctionalType(node.getFuncType().getFunctionalType().toString());
			
			// Adding this temporary node to the Nffg's node list
			newNffgNodeList.add(newNffgNode);
		}
		
		// Setting the Nffg JAXB annotated class' nodes
		newNffg.setNodes(newNffgNodes);
		
		// Returning the downloaded NF-FG
		return newNffg;
	}
	
	/**
	 * It deploys an NF-FG object into the Neo4J database.
	 * @param nffg
	 */
	public static void deployNffg(NffgType nffg) {
		// TODO Deploying an empty NF-FG
		
		// Deploying NF-FG's nodes
		for (NodeType node : nffg.getNodes().getNode()) {
			NodeManager.addNode(nffg.getId(), node);
		}
	}
}
