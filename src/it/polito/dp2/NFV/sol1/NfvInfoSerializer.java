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

import it.polito.dp2.NFV.*;
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
		
		try {
			// Creating the output file
			File file = new File(filename);
	        FileOutputStream fos = new FileOutputStream(file, false);
			
	        // Retrieving data
			NfvInfoSerializer data = new NfvInfoSerializer();
			
			/*	Creating a JAXBContext capable of handling classes generated into
            	the it.polito.dp2.NFV.sol1.jaxb package */
			JAXBContext jc = JAXBContext.newInstance("it.polito.dp2.NFV.sol1.jaxb");
			
			// Creating a Marshaler
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// Validation
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			try {
				Schema schema = sf.newSchema(new File("xsd/nfvInfo.xsd"));
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
                                    "]:" + ve.getMessage() + "\n\n"
                                );
                            }
                            return true;
                        }
                    }
				);
			} catch(SAXException se) {
                System.out.println("Unable to validate due to following error.");
                se.printStackTrace();
            }
	        
			// Creating the JAXBElement object
	        JAXBElement<NFVType> nfvElement = data.toJAXB();
			
        	// Marshaling to the specified file
			m.marshal(nfvElement, fos);
			fos.close();
		} catch(JAXBException je) {
			je.printStackTrace();
			System.exit(1);
		} catch (NfvReaderException e) {
			System.err.println("Could not instantiate data generator");
			e.printStackTrace();
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println("Invalid file name");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
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
}
