package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
	 * Default constructor
	 * @throws NfvReaderException
	 */
	public NfvInfoSerializer() throws NfvReaderException {
		this(NfvReaderFactory.newInstance().newNfvReader());
	}
	
	/**
	 * Creates a NfvInfoSerializer with a given monitor
	 * @param monitor
	 */
	public NfvInfoSerializer(NfvReader monitor) {
		if(monitor == null) {
			System.err.println("Invalid monitor");
			System.exit(1);
		}
		
		converter = new NfvJAXBConverter(monitor);
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
		
		// Retrieving data and serializing
		JAXBElement<NFVType> nfvElement = null;
		try {
			NfvInfoSerializer serializer = new NfvInfoSerializer();
	        nfvElement = serializer.toJAXB(); // Conversion
	        serializer.marshal(nfvElement, fos); // Marshalling
		} catch (NfvReaderException e) {
			System.err.println("Could not instantiate data generator");
			e.printStackTrace();
			System.exit(1);
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
	
	/**
	 * Marshals object obj to the OutputStream os using a schema
	 * @param obj object to be marshalled
	 * @param os OutputStream used during marshalling
	 */
	private void marshal(Object obj, OutputStream os) {
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
			// JAXBContext capable of handling JAXB-compatible generated classes 
			JAXBContext jc = JAXBContext.newInstance(NfvConfig.jaxbClassesPackage);
			
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
			m.marshal(obj, os == null ? System.out : os);
			if(os != null)
				os.close();
		} catch (JAXBException e1) {
			System.err.println("A JAXB exception occurred");
			e1.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("An I/O exception occurred");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
