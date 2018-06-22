package it.polito.dp2.NFV.sol3.service.gen;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.service.database.NfvDatabase;

@ApplicationPath("/rest")
public class RestApplication extends Application {
	public RestApplication() throws ServiceException {
		try {
			// Initializing the NFV database
			NfvDatabase.init();
		} catch (NfvReaderException | FactoryConfigurationError | AllocationException exception) {
			System.err.println("Exception during initial NFV data retrieving: " + exception.getMessage());
			throw new ServiceException("Could not load initial NFV data.");
		}
	}
}