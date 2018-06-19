package it.polito.dp2.NFV.sol3.service;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.service.gen.api.NffgsApiService;
import it.polito.dp2.NFV.sol3.service.gen.api.NotFoundException;
import it.polito.dp2.NFV.sol3.service.gen.api.factories.NffgsApiServiceFactory;
import it.polito.dp2.NFV.sol3.service.gen.model.CatalogType;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelType;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelsType;
import it.polito.dp2.NFV.sol3.service.gen.model.FunctionalTypeType;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;
import it.polito.dp2.NFV.sol3.service.gen.model.HostsType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodeType;
import it.polito.dp2.NFV.sol3.service.gen.model.NodesType;
import it.polito.dp2.NFV.sol3.service.gen.model.VNFType;

public class NfvDeployerDatabase {
	
	/**
	 * Boolean indicating whether the NFV database has been
	 * already downloaded or not.
	 */
	private static boolean databaseAlreadyLoaded = false;
	
	/**
	 * Monitor through which the NFV database is retrieved.
	 */
	private static NfvReader monitor;

	/**
	 * NFV data
	 */
	private static CatalogType catalog = new CatalogType();
	private static HostsType hosts = new HostsType();
	private static ChannelsType channels = new ChannelsType();

	/**
	 * It initializes the NFV database, by first loading the Nffg0
	 * and then downloading data about the IN and the catalog.
	 * @throws FactoryConfigurationError 	if there is some error while retrieving
	 * 										an implemenetation of the NfvReader interface. 
	 * @throws NfvReaderException 			if an implementation of 
	 * 										{@code NfvReader} cannot be created.
	 */
	public synchronized static void init() throws NfvReaderException, FactoryConfigurationError {
		// If the database has not been downloaded yet
		if(!databaseAlreadyLoaded) {
			// Using the NfvReader interface to retrieve data
			monitor = NfvReaderFactory.newInstance().newNfvReader();
			
			// Deploying Nffg0
			deployNffg0();
			
			// Downloading the database
			downloadDatabase();
			
			// Setting the database download status flag
			databaseAlreadyLoaded = true;
		}
	}
	
	/**
	 * It retrieves the whole database content, 
	 * storing it into this class' private fields.
	 */
	private static void downloadDatabase() {
		// Downloading catalog and IN data
		dowloadCatalog();
		downloadNetwork();
	}
	
	/**
	 * It deploys the Nffg0 (which is always among the ones returned by NfvReader)
	 */
	private static void deployNffg0() {
		// Retrieving data about Nffg0
		NffgReader retrievedNffg0 = monitor.getNffg("Nffg0");
		
		// Nffg0 JAXB annotated class to be deployed
		NffgType newNffg0 = new NffgType();
		newNffg0.setId("Nffg0");
		
		// Setting the Nffg0 JAXB annotated class' deploy time
		GregorianCalendar gregorianDeployTime = new GregorianCalendar();
		gregorianDeployTime.setTime(retrievedNffg0.getDeployTime().getTime());
		try {
			newNffg0.setDeployTime(
				DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianDeployTime)
			);
		} catch(DatatypeConfigurationException e) {
			System.err.println("Could not convert deploy tiem in Gregorian format");
		}
		
		// Nffg0's nodes JAXB annotated class to be deployed
		NodesType newNffg0Nodes = new NodesType();
		
		// Nffg0's node list
		List<NodeType> newNffg0NodeList = newNffg0Nodes.getNode();
		for(NodeReader node: retrievedNffg0.getNodes()) {
			// New temporary Nffg0 node object
			NodeType newNffg0Node = new NodeType();
			
			// Initializing the new temporary Nffg0 node object
			newNffg0Node.setId(node.getName());
			newNffg0Node.setHost(node.getHost().getName());
			newNffg0Node.setFunctionalType(node.getFuncType().getFunctionalType().toString());
			
			// Adding this temporary node to the Nffg0's node list
			newNffg0NodeList.add(newNffg0Node);
		}
		
		// Setting the Nffg0 JAXB annotated class' nodes
		newNffg0.setNodes(newNffg0Nodes);
		
		// Deploying data about Nffg0
		NffgsApiService nffgsDeployer = NffgsApiServiceFactory.getNffgsApi();
		try {
			nffgsDeployer.deployNffg(newNffg0, null);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * It retrieves the NFV's catalog information 
	 */
	private static void dowloadCatalog() {
		// Retrieving catalog information
		Set<VNFTypeReader> retrievedCatalog = monitor.getVNFCatalog();
		
		// The VNF list contained in the local catalog
		List<VNFType> vnfList = catalog.getVNF();
		
		// Adding retrieved VNFs in the local catalog
		for(VNFTypeReader vnf: retrievedCatalog) {
			// New temporary VNF object
			VNFType tempVnf = new VNFType();
			
			// Setting properties
			tempVnf.setId(vnf.getName());
			tempVnf.setFunctionalType(FunctionalTypeType.fromValue(vnf.getFunctionalType().value()));
			tempVnf.setRequiredMemory(vnf.getRequiredMemory());
			tempVnf.setRequiredStorage(vnf.getRequiredStorage());
			
			// Adding the temporary VNF object to the catalog's VFN list
			vnfList.add(tempVnf);
		}
	}
	
	/**
	 * It retrieves the NFV's network information, consisting
	 * in physical hosts (INs) and physical channels (connections
	 * performance data)
	 */
	private static void downloadNetwork() {
		// TODO Retrieving hosts information
		downloadHosts();
		
		// TODO Retrieving connection performances information
		downloadChannels();
	}
 	
	/**
	 * It retrieves data about the physical hosts in the
	 * Infrastructure Network. 
	 */
	private static void downloadHosts() {
		// Retrieving hosts information
		Set<HostReader> retrievedHosts = monitor.getHosts();
		
		// The host list contained in the local hosts object
		List<HostType> hostList = hosts.getHost();
		
		// Adding retrieved hosts in the local hosts object
		for(HostReader host: retrievedHosts) {
			// Adding the temporary host object to the local hosts object
			hostList.add(getHost(host));
		}
	}
	
	/**
	 * It retrieves data about the physicals channels in the
	 * Infrastructure Network.
	 */
	private static void downloadChannels() {
		// Retrieving hosts information
		Set<HostReader> retrievedHosts = monitor.getHosts();
		
		// The channel list contained in the local channels object 
		List<ChannelType> channelList = channels.getChannel();
		
		// For each host pair
		 for(HostReader host1: retrievedHosts) {
			 for(HostReader host2: retrievedHosts) {
				 // Retrieving the connection performances
				 ConnectionPerformanceReader retrievedConnection = monitor.getConnectionPerformance(host1, host2);
				 
				 // New temporary channel object
				 ChannelType tempChannel = new ChannelType();
				 
				 // Setting properties
				 tempChannel.setHost1(host1.getName());
				 tempChannel.setHost2(host2.getName());
				 tempChannel.setAverageLatency(retrievedConnection.getLatency());
				 tempChannel.setAverageThroughput(retrievedConnection.getThroughput());
				 
				 /*
				  * Adding the temporary channel object to the
				  * local channels object
				  */
				 channelList.add(tempChannel);
			 }
		 }
	}
	
	/**
	 * It converts a JAXB annotated host class (HostType)
	 * from a HostReader interface. 
	 * @param host	HostReader interface to be converted.
	 * @return		a JAXB annotated host class.
	 */
	private static HostType getHost(HostReader host) {
		// New temporary host object
		HostType tempHost = new HostType();
		
		// Setting properties
		tempHost.setId(host.getName());
		tempHost.setMaxVNFs(host.getMaxVNFs());
		tempHost.setAvailableMemory(host.getAvailableMemory());
		tempHost.setAvailableStorage(host.getAvailableStorage());
		
		// Returning the converted host object
		return tempHost;
	}

	public static CatalogType getCatalog() {
		return catalog;
	}

	public static HostsType getHosts() {
		return hosts;
	}
	
	public static ChannelsType getChannels() {
		return channels;
	}
}
