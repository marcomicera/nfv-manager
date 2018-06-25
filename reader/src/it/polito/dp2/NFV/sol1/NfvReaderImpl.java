package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
	
	/**
	 * Default constructor creating a NFV reader
	 * @throws NfvReaderException if the input file name is invalid
	 */
	public NfvReaderImpl() throws NfvReaderException {
		inputFile = System.getProperty(NfvConfig.inputFileProperty);
		if(inputFile != null || inputFile.isEmpty()) {
			// readFile();
			
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

	private void readCatalog() {
		for(VNFType vnf: nfvInfo.getCatalog().getVNF())
			try {
				catalog.put(
					vnf.getId(), 
					new MyVNFTypeReader(vnf)
				);
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
	}

	private void readNffgs() {
		for(NffgType nffg: nfvInfo.getNffgs().getNffg()) {
			MyNffgReader tempNffgReader = new MyNffgReader(nffg);
			
			Map<String, NodeReader> tempNodeReaders = readNodes(nffg, tempNffgReader); 
			
			tempNffgReader.setNodes(tempNodeReaders);
			
			if(tempNodeReaders != null) {
				Iterator<NodeReader> it = tempNodeReaders.values().iterator();
				
				while(it.hasNext()) {
					MyNodeReader tempNodeReader = (MyNodeReader)it.next();
					tempNodeReader.setLinks();
				}
			}
			
			nffgs.put(
				nffg.getId(),
				tempNffgReader
			);
		}
	}
	
	public Map<String, NodeReader> readNodes(NffgType nffg, MyNffgReader nffgReader) {
		Map<String, NodeReader> nodes = new HashMap<String, NodeReader>();
		for(NodeType node: nffg.getNodes().getNode()) {
			MyNodeReader tempNodeReader = new MyNodeReader(
				node,
				nffgReader,
				catalog.get(node.getFunctionalType()),
				hosts.get(node.getHost())
			);
			
			nodes.put(
				node.getId(), 
				tempNodeReader
			);
			
			if(node.getHost() != null) {
				MyHostReader host = (MyHostReader)hosts.get(node.getHost());
				host.addNode(tempNodeReader);
			}
		}
		
		return nodes;
	}
	
	private void readHosts() {
		for(HostType host: nfvInfo.getNetwork().getHosts().getHost())
			hosts.put(
				host.getId(),
				new MyHostReader(host)
			);
	}
	
	private void readChannels() {
		for(ChannelType channel: nfvInfo.getNetwork().getChannels().getChannel()) {
			MyHostPair tempHostPair = new MyHostPair(
				hosts.get(channel.getHost1()),
				hosts.get(channel.getHost2())
			);
			
			channels.put(
				tempHostPair,
				new MyConnectionPerformanceReader(
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