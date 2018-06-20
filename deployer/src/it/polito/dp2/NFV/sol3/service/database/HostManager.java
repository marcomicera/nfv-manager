package it.polito.dp2.NFV.sol3.service.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;

public class HostManager {
	/**
	 * Hosts data
	 */
	protected static Map<String, HostType> hosts = new HashMap<>();
	
	/**
	 * It retrieves data about the physical hosts in the
	 * Infrastructure Network. 
	 * @param monitor	NfvReader interface through which data must be retrieved.
	 */
	protected static void downloadHosts(NfvReader monitor) {
		// Retrieving hosts information
		Set<HostReader> retrievedHosts = monitor.getHosts();
		
		// Adding retrieved hosts in the local hosts map
		for(HostReader host: retrievedHosts)
			hosts.put(host.getName(), getHost(host));
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
}
