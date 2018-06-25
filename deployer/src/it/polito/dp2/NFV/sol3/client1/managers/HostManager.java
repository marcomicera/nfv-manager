package it.polito.dp2.NFV.sol3.client1.managers;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.HostType;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.Localhost_NfvDeployerRest;
import it.polito.dp2.NFV.sol3.client1.readers.HostReaderImpl;

public class HostManager {
	/**
	 * Retrieves a single host reader object from the NfvDeployer web service.
	 * 
	 * @param hostId
	 *            the desired host's ID.
	 * @return the host reader object.
	 */
	public static HostReader retrieve(String hostId) {
		// Retrieving the host object
		HostType retrievedHost = Localhost_NfvDeployerRest
				.hosts(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI).host_id(hostId)
				.getAsXml(HostType.class);

		// Return the corresponding reader
		return (new HostReaderImpl(retrievedHost));
	}
}
