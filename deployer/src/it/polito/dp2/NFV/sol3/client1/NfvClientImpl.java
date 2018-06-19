package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.NffgDescriptor;
import it.polito.dp2.NFV.lab3.NfvClient;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;

public class NfvClientImpl implements NfvClient {
	
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
	 * NfvClient's implementation's default constructor.
	 */
	public NfvClientImpl() {
		// Retrieving the NfvDeployer web service's base URI
		nfvDeployerBaseURI = getBaseURI();	
	}

	@Override
	public DeployedNffg deployNffg(NffgDescriptor nffg) throws AllocationException, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeployedNffg getDeployedNffg(String name) throws UnknownEntityException, ServiceException {
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
