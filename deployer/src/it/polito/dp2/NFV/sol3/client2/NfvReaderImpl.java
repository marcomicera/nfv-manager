package it.polito.dp2.NFV.sol3.client2;

import java.net.URI;
import java.util.Calendar;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.VNFTypeReader;

public class NfvReaderImpl implements NfvReader {
	
	/**
	 * System property by which the NfvDeployer web service's URI
	 * should be retrieved.
	 */
	private static final String NFV_DEPLOYER_SYSTEM_PROPERTY = "it.polito.dp2.NFV.lab3.URL";
	
	/**
	 * Default NfvDeployer web service's URI
	 */
	private static final String NFV_DEPLOYER_DEFAULT_URI = "http://localhost:8080/NfvDeployer/rest/";
	
	/**
	 * Base URI of the NfvDeployer web service. 
	 */
	private URI nfvDeployerBaseURI;

	/**
	 * NfvReader's implementation's default constructor-
	 */
	public NfvReaderImpl() {
		// Retrieving the NfvDeployer web service's base URI
		nfvDeployerBaseURI = getBaseURI();	
	}

	@Override
	public ConnectionPerformanceReader getConnectionPerformance(HostReader arg0, HostReader arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HostReader getHost(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HostReader> getHosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NffgReader getNffg(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<NffgReader> getNffgs(Calendar arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the base URI of the NfvDeployer web service.
	 * @return	base NfvDeployer's URI.
	 */
	private static URI getBaseURI() {
		String baseURI;
		try {
			baseURI = System.getProperty(NFV_DEPLOYER_SYSTEM_PROPERTY);
		} catch(SecurityException | NullPointerException e) {
			baseURI = NFV_DEPLOYER_DEFAULT_URI;
		}
		
		return UriBuilder.fromUri(baseURI + "/data/").build();
	}
}
