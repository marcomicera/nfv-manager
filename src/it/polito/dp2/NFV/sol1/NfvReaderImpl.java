package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
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
	private Set<VNFTypeReader> catalog;
	private Set<HostReader> hosts;
	
	public NfvReaderImpl() throws NfvReaderException {
		inputFile = System.getProperty(NfvConfig.inputFileProperty);
		/**/System.out.println("************************************************* Input file is: " + inputFile);
		if(inputFile != null) {
			readFile();
			readCatalog();
			readHosts();
		}
		else
			throw new NfvReaderException("Could not find input file");
	}
	
	@SuppressWarnings("unchecked") // TODO can this suppressed warning be avoided?
	private void readFile() {
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
			Object unmarshalledInput = u.unmarshal(fis);
			if(unmarshalledInput instanceof JAXBElement<?>)
				nfvInfo = ((JAXBElement<NFVType>)unmarshalledInput).getValue();
			else
				throw new ClassCastException();
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
			catalog.add(
				new MyVNFReader(
					vnf.getId(),
					vnf.getFunctionalType(),
					vnf.getRequiredMemory(),
					vnf.getRequiredStorage()
				)
			); 
	}

	private void readHosts() {
		for(HostType host: nfvInfo.getNetwork().getHosts().getHost())
			hosts.add(
				new MyHostReader(
					host.getId(),
					host.getAvailableMemory(),
					host.getAvailableStorage(),
					host.getMaxVNFs(),
					readNodes(host)
				)
			);
	}

	/**
	 * Reads NF-FG nodes allocated on this host
	 * @param host the host of which nodes have to be read 
	 * @return set of nodes allocated to the host
	 */
	private Set<NodeReader> readNodes(HostType host) {
		Set<NodeReader> nodes = null;
		
		for(NodeRefType nodeRef: host.getNode()) 
			for(NffgType nffg: nfvInfo.getNffgs().getNffg()) 
				for(NodeType node: nffg.getNodes().getNode()) 
					if(nodeRef.getId().compareTo(node.getId()) == 0) {
						// Searching functional type
						/*VNFType ft = null;
						for(VNFType vnf: nfvInfo.getCatalog().getVNF())
							if(vnf.getFunctionalType() == node.getFunctionalType())
								ft = vnf;
						if(ft != null)
							MyVNFReader 
						
						nodes.add(
							new MyNodeReader(
								node.getId(),
								ft,
								node.getHost(),
								node.getLink(),
								new MyNffgReader(
									nffg.getId(),
									nffg.getDeployTime().toGregorianCalendar(),
									nffg.getNodes().getNode()
								)
							)
						);
						
						continue;*/
					}
		
		return nodes;
	}
	
	@Override
	public Set<NffgReader> getNffgs(Calendar since) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NffgReader getNffg(String var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HostReader> getHosts() {
		return hosts;
	}

	@Override
	public HostReader getHost(String var1) {
		// TODO Auto-generated method stub
		// TODO transform hosts from Set<> to Map<> to use the get() function
		return null;
	}

	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		return catalog;
	}

	@Override
	public ConnectionPerformanceReader getConnectionPerformance(HostReader host1, HostReader host2) {
		// TODO Auto-generated method stub
		return null;
	}
	
}