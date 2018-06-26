package it.polito.dp2.NFV.sol3.client1.managers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.client1.NfvClientImpl;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.Localhost_NfvDeployerRest;
import it.polito.dp2.NFV.sol3.client1.readers.HostReaderImpl;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;
import it.polito.dp2.NFV.sol3.service.gen.model.HostsType;

public class HostManager {
	/**
	 * Retrieves all host readers from the NfvDeployer web service.
	 * 
	 * @return all deployed hosts readers.
	 * @throws ServiceException 
	 */
	public static Map<String, HostReader> retrieve() throws ServiceException {
		// Retrieving hosts
		// HostsType retrievedHosts = Localhost_NfvDeployerRest
		// .hosts(Localhost_NfvDeployerRest.createClient(),
		// Localhost_NfvDeployerRest.BASE_URI)
		// .getAsXml(HostsType.class);
		HostsType retrievedHosts = null;
		Response response = NfvClientImpl.getTarget().path("hosts").request(MediaType.APPLICATION_XML).get();

		if (response.getStatus() == 200) {
			retrievedHosts = response.readEntity(HostsType.class);
		} else
			throw new ServiceException();

		// Building the result map
		final Map<String, HostReader> result = new HashMap<>();
		for (final HostType retrievedHost : retrievedHosts.getHost()) {
			result.put(retrievedHost.getId(), new HostReaderImpl(retrievedHost));
		}

		return result;
	}

	/**
	 * Retrieves a single host reader object from the NfvDeployer web service.
	 * 
	 * @param hostId
	 *            the desired host's ID.
	 * @return the host reader object.
	 */
	public static HostReader retrieve(String hostId) {
		// Retrieving the host object
		// HostType retrievedHost = Localhost_NfvDeployerRest
		// .hosts(Localhost_NfvDeployerRest.createClient(),
		// Localhost_NfvDeployerRest.BASE_URI).host_id(hostId)
		// .getAsXml(HostType.class);
		HostType retrievedHost = NfvClientImpl.getTarget().path("hosts").path(hostId).request(MediaType.APPLICATION_XML)
				.get(HostType.class);

		// Return the corresponding reader
		return (new HostReaderImpl(retrievedHost));
	}
}
