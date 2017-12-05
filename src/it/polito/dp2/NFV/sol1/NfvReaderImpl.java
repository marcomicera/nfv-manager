package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import it.polito.dp2.NFV.*;
import it.polito.dp2.NFV.sol1.jaxb.*;

class NfvReaderImpl implements it.polito.dp2.NFV.NfvReader {
	private String inputFile;
	private NFVType nfvInfo;
	private Map<String, VNFTypeReader> catalog;
	private Map<String, HostReader> hosts;
	private Map<MyHostPair, ConnectionPerformanceReader> channels;
	private Map<String, NffgReader> nffgs;
	
	public NfvReaderImpl() throws NfvReaderException {
		inputFile = System.getProperty(NfvConfig.inputFileProperty);
		if(inputFile != null) {
			readFile();
			
			catalog = new HashMap<String, VNFTypeReader>();
			readCatalog();
			
			hosts = new HashMap<String, HostReader>();
			readHosts();
			
			nffgs = new HashMap<String, NffgReader>();
			readNffgs();
			
			channels = new HashMap<MyHostPair, ConnectionPerformanceReader>();
			readChannels();
		}
		else
			throw new NfvReaderException("Could not find input file");
	}

	private void readFile() throws NfvReaderException {
		// Reading the input file
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(inputFile));
		} catch (FileNotFoundException e) {
			System.err.println("Could not find input file");
			e.printStackTrace();
			System.exit(1);
		}
		
		// Validation
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			schema = sf.newSchema(new File(NfvConfig.schemaFile));
		} catch (SAXException e) {
			System.err.println("Could not read the schema file");
			e.printStackTrace();
		}
		
		try {
			/*	Creating a JAXBContext capable of handling classes generated into
	    	the it.polito.dp2.NFV.sol1.jaxb package */
			JAXBContext jc = JAXBContext.newInstance(NfvConfig.jaxbClassesPackage);
			
			// Creating an Unmarshaller
			Unmarshaller u = jc.createUnmarshaller();
			u.setSchema(schema);
			
			// Unmarshalling input file
			Object root = u.unmarshal(fis);
			if(!(root instanceof JAXBElement<?>))
				throw new NfvReaderException("/Unexpected root element");
			Object rootObject = ((JAXBElement<?>)root).getValue();
			if(rootObject == null)
				throw new NfvReaderException("No root element found");
			if(!(rootObject instanceof NFVType))
				throw new NfvReaderException("/Unexpected root element");
			nfvInfo = (NFVType)rootObject;
		} catch (JAXBException e) {
			System.err.println("JAXB exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (ClassCastException cce) {
        	System.err.println("Unexpected root element found");
        	cce.printStackTrace();
        	System.exit(1);
        }
	}
	
	private void readCatalog() {
		for(VNFType vnf: nfvInfo.getCatalog().getVNF())
			try {
				catalog.put(
					vnf.getId(), 
					new MyVNFReader(
						vnf.getId(),
						vnf.getFunctionalType(),
						vnf.getRequiredMemory(),
						vnf.getRequiredStorage()
					)
				);
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
	}

	private void readNffgs() {
		for(NffgType nffg: nfvInfo.getNffgs().getNffg()) {
			MyNffgReader tempNffgReader = new MyNffgReader(
				nffg.getId(),
				nffg.getDeployTime()
			);
			
			Map<String, NodeReader> tempNodeReaders = readNodes(nffg, tempNffgReader); 
			
			tempNffgReader.setNodes(tempNodeReaders);
			
			if(tempNodeReaders != null) {
				Iterator<NodeReader> it = tempNodeReaders.values().iterator();
				
				while(it.hasNext()) {
					MyNodeReader tempNodeReader = (MyNodeReader)it.next();
					tempNodeReader.setLinks(
						readLinks(
							readNodeInfo(tempNodeReader),
							tempNffgReader
						)
					);
				}
			}
			
			nffgs.put(
				nffg.getId(),
				tempNffgReader
			);
		}
	}
	
	private Map<String, NodeReader> readNodes(NffgType nffg, MyNffgReader nffgReader) {
		Map<String, NodeReader> nodes = new HashMap<String, NodeReader>();
		for(NodeType node: nffg.getNodes().getNode()) {
			MyNodeReader tempNodeReader = new MyNodeReader(
				node.getId(),
				catalog.get(node.getFunctionalType()),
				hosts.get(node.getHost()),
				nffgReader
			);
			
			nodes.put(
				node.getId(), 
				tempNodeReader
			);
			
			if(node.getHost() != null) {
				MyHostReader host = (MyHostReader)hosts.get(node.getHost());
				host.addNode(node.getId(), tempNodeReader);
			}
		}
		
		return nodes;
	}
	
	private Map<String, LinkReader> readLinks(NodeType node, MyNffgReader nffgReader) {
		Map<String, LinkReader> links = new HashMap<String, LinkReader>();
		
		for(LinkType link: node.getLink()) {
			NodeReader sourceNode = nffgReader.getNode(link.getSourceNode());
			NodeReader destinationNode = nffgReader.getNode(link.getDestinationNode());
			
			links.put(
				link.getId(),
				new MyLinkReader(
					link.getId(),
					link.getMaximumLatency() == null ? 0 : link.getMaximumLatency(),
					link.getMinimumThroughput() == null ? 0 : link.getMinimumThroughput(),
					sourceNode,
					destinationNode
				)
			);
		}
		
		return links;
	}
	
	private void readHosts() {
		for(HostType host: nfvInfo.getNetwork().getHosts().getHost())
			hosts.put(
				host.getId(),
				new MyHostReader(
					host.getId(),
					host.getAvailableMemory(),
					host.getAvailableStorage(),
					host.getMaxVNFs()
				)
			);
	}

	private NodeType readNodeInfo(String nodeRef) {
		for(NffgType nffg: nfvInfo.getNffgs().getNffg()) 
			for(NodeType node: nffg.getNodes().getNode()) 
				if(nodeRef.compareTo(node.getId()) == 0)
					return node;
		
		return null;
	}
	
	private NodeType readNodeInfo(NodeReader node) {
		return readNodeInfo(node.getName());
	}
	
	private void readChannels() {
		for(ChannelType channel: nfvInfo.getNetwork().getChannels().getChannel()) {
			/*TODO delete*/System.out.println(
				"Inserting channel b/w: " + channel.getHost1() + " and " + channel.getHost2()
			);
			MyHostPair tempHostPair = new MyHostPair(
				hosts.get(channel.getHost1()),
				hosts.get(channel.getHost2())
			);
			
			channels.put(
				tempHostPair,
				new MyConnectionPerformanceReader(
					//tempHostPair,
					channel.getAverageLatency(),
					channel.getAverageThroughput()
				)
			);
		}
	}
	
	@Override
	public Set<NffgReader> getNffgs(Calendar since) {
		if(nffgs == null) {
			return new HashSet<NffgReader>();
		} else if(since == null) {
			return new HashSet<NffgReader>(nffgs.values());
		} else {
			HashSet<NffgReader> result = new HashSet<NffgReader>();
			Iterator<NffgReader> it = nffgs.values().iterator();

			while (it.hasNext()) {
				NffgReader nffg = (NffgReader)it.next();
				if(nffg.getDeployTime() != null && nffg.getDeployTime().after(since))
					result.add(nffg);
			}

			return result;
		}
	}

	@Override
	public NffgReader getNffg(String name) {
		return nffgs.get(name);
	}

	@Override
	public Set<HostReader> getHosts() {
		return new HashSet<HostReader>(hosts.values());
	}

	@Override
	public HostReader getHost(String name) {
		return hosts.get(name);
	}

	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		return new HashSet<VNFTypeReader>(catalog.values());
	}

	@Override
	public ConnectionPerformanceReader getConnectionPerformance(HostReader host1, 
																HostReader host2) {
		
		if(host1 == null || host2 == null || channels == null || channels.isEmpty())
			return null;
		
		ConnectionPerformanceReader cpr = channels.get(new MyHostPair(host1, host2));
		
		return new MyConnectionPerformanceReader(
			cpr.getLatency(),
			cpr.getThroughput()
		);
	}
	
}