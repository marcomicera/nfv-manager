package it.polito.dp2.NFV.sol3.client2;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.client1.managers.CatalogManager;
import it.polito.dp2.NFV.sol3.client1.managers.HostManager;
import it.polito.dp2.NFV.sol3.client1.managers.NffgManager;
import it.polito.dp2.NFV.sol3.client2.managers.ChannelManager;

public class NfvReaderImpl implements NfvReader {
	
	/**
	 * System property by which the NfvDeployer web service's URI
	 * should be retrieved.
	 */
	// private static final String NFV_DEPLOYER_SYSTEM_PROPERTY = "it.polito.dp2.NFV.lab3.URL";
	
	/**
	 * Default NfvDeployer web service's URI
	 */
	// private static final String NFV_DEPLOYER_DEFAULT_URI = "http://localhost:8080/NfvDeployer/rest/";
	
	/**
	 * Base URI of the NfvDeployer web service. 
	 */
	// private URI nfvDeployerBaseURI;
	
	/**
	 * NfvReader's implementation's default constructor-
	 */
	public NfvReaderImpl() {
		// Retrieving the NfvDeployer web service's base URI
		// nfvDeployerBaseURI = getBaseURI();
		
		// Retrieving the NF-FG catalog
		
		// Retrieving the NF-FG hosts
	}

	@Override
	public ConnectionPerformanceReader getConnectionPerformance(HostReader host1, HostReader host2) {
		return ChannelManager.retrieve(host1.getName(), host2.getName());
	}

	@Override
	public HostReader getHost(String hostId) {
		return HostManager.retrieve(hostId);
	}

	@Override
	public Set<HostReader> getHosts() {
		try {
			return new HashSet<HostReader>(HostManager.retrieve().values());
		} catch (ServiceException e) {
			return null;
		}
	}

	@Override
	public NffgReader getNffg(String nffgId) {
		try {
			return NffgManager.retrieve(nffgId);
		} catch (ServiceException e) {
			return null;
		}
	}

	@Override
	public Set<NffgReader> getNffgs(Calendar since) {
		try {
			return NffgManager.retrieve(since);
		} catch (ServiceException e) {
			return null;
		}
	}

	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		return new HashSet<VNFTypeReader>(CatalogManager.retrieve().values());
	}

	/**
	 * Returns the base URI of the NfvDeployer web service.
	 * @return	base NfvDeployer's URI.
	 */
	/*private static URI getBaseURI() {
		String baseURI;
		try {
			baseURI = System.getProperty(NFV_DEPLOYER_SYSTEM_PROPERTY);
		} catch(SecurityException | NullPointerException e) {
			baseURI = NFV_DEPLOYER_DEFAULT_URI;
		}
		
		return UriBuilder.fromUri(baseURI + "/data/").build();
	}*/
}
