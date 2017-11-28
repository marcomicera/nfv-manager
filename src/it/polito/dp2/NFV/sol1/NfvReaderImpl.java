package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.NFVType;
import it.polito.dp2.NFV.sol1.jaxb.NffgsType;

class NfvReaderImpl implements it.polito.dp2.NFV.NfvReader {
	private String inputFile;
	private NFVType nfvInfo;
	private Set<NfvVNF> catalog;
	
	
	public NfvReaderImpl() throws NfvReaderException {
		inputFile = System.getProperty(NfvConfig.inputFileProperty);
		/**/System.out.println("************************************************* Input file is: " + inputFile);
		if(inputFile != null)
			this.readFile();
		else
			throw new NfvReaderException("Could not find input file");
	}
	
	/*public static void main(String[] args) {
		NfvReaderImpl r;
		try {
			r = new NfvReaderImpl();
			System.out.print(r.nfvInfo);
		} catch (NfvReaderException e) {
			System.err.print(e.getMessage());
			e.printStackTrace();
		}  
		
	}*/
	
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
				throw new JAXBException("could not unmarshal input file");
		} catch (JAXBException e) {
			System.err.println("JAXB exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (ClassCastException cce) {
        	System.err.println("Unexpected root element found");
        	cce.printStackTrace();
        	System.exit(1);
        }
		
		catalog = readCatalog();
	}
	
	private Set<NfvVNF> readCatalog() {
		
		
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HostReader getHost(String var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionPerformanceReader getConnectionPerformance(HostReader host1, HostReader host2) {
		// TODO Auto-generated method stub
		return null;
	}
	
}