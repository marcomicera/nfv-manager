package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.sol1.jaxb.NFVType;


public class NfvInfoSerializer {
	private NfvJAXBConverter converter;

	/**
	 * Creates a NfvInfoSerializer with a given monitor
	 * @param monitor
	 */
	public NfvInfoSerializer(NfvReader monitor) {
		converter = new NfvJAXBConverter(monitor);
	}

	/**
	 * Default constructor
	 * @throws NfvReaderException
	 */
	public NfvInfoSerializer() throws NfvReaderException {
		this(NfvReaderFactory.newInstance().newNfvReader());
	}
	
	/**
	 * Main application serializing data about the DP2-NFV system 
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Usage: java NfvInfoSerializer filename");
			System.exit(1);
		}
		
		String filename = args[0];
		
		// Creating the output file
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filename), false);
		} catch(FileNotFoundException e) {
			System.err.println("Could not create output file");
			e.printStackTrace();
			System.exit(1);
		}
		
		// Retrieving data
		JAXBElement<NFVType> nfvElement = null;
		try {
			NfvInfoSerializer data = new NfvInfoSerializer();
	        nfvElement = data.toJAXB(); // Conversion
		} catch (NfvReaderException e) {
			System.err.println("Could not instantiate data generator");
			e.printStackTrace();
			System.exit(1);
		}
		
		// Validation
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			schema = sf.newSchema(new File("xsd/nfvInfo.xsd"));
		} catch (SAXException e) {
            System.err.println("Could not read the schema file");
            e.printStackTrace();            
        }
		
		try {
			/*	Creating a JAXBContext capable of handling classes generated into
	    	the it.polito.dp2.NFV.sol1.jaxb package */
			JAXBContext jc = JAXBContext.newInstance("it.polito.dp2.NFV.sol1.jaxb");
			
			// Creating a Marshaller
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			m.setSchema(schema);
			m.setEventHandler(
				new ValidationEventHandler() {
	                // allows unmarshalling to continue even if there are errors
					@Override
	                public boolean handleEvent(ValidationEvent ve) {
	                    // ignore warnings
	                    if (ve.getSeverity() != ValidationEvent.WARNING) {
	                        ValidationEventLocator vel = ve.getLocator();
	                        System.out.println("Line:Col[" + vel.getLineNumber() +
	                            ":" + vel.getColumnNumber() +
	                            "]:" + ve.getMessage()
	                        );
	                    }
	                    return true;
	                }
	            }
			);
			
			// Marshalling to the specified file
			m.marshal(nfvElement, /*fos*/ System.out);
			fos.close();
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			System.err.println("An I/O occurred");
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts the NFV object obtainable by NfvReader
	 * into a JAXB equivalent class
	 * @return the NFV JAXB-compatible object
	 */
	private JAXBElement<NFVType> toJAXB() {
		return converter.getNfvInfo();
	}
}
