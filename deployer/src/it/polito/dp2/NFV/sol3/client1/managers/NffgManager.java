package it.polito.dp2.NFV.sol3.client1.managers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.client1.NfvClientImpl;
import it.polito.dp2.NFV.sol3.client1.readers.NffgReaderImpl;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgsType;

public class NffgManager {
	public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

	/**
	 * Retrieves the next available NF-FG ID.
	 *
	 * @return the next usable NF-FG ID.
	 * @throws ServiceException 
	 */
	public static int nextId() throws ServiceException {
		Response response = NfvClientImpl.getTarget().path("nffgs").path("howmany").request(MediaType.TEXT_PLAIN)
				.get();

		if (response.getStatus() == 200) {
			int nextId = Integer.valueOf(response.readEntity(String.class));
			return 1 + nextId;
		} else
			throw new ServiceException();
	}

	public static Set<NffgReader> retrieve(Calendar since) throws ServiceException {
		// Formatting the date
		SimpleDateFormat formatter = new SimpleDateFormat(NffgManager.DATE_FORMAT);
		String formattedDate = since == null ? null : formatter.format(since);

		// NffgsType retrievedNffgs = Localhost_NfvDeployerRest
		// .nffgs(Localhost_NfvDeployerRest.createClient(),
		// Localhost_NfvDeployerRest.BASE_URI)
		// .getAsXml(formattedDate, NffgsType.class);
		NffgsType retrievedNffgs = NfvClientImpl.getTarget().path("nffgs").queryParam("since", formattedDate)
				.request(MediaType.APPLICATION_XML).get(NffgsType.class);

		List<NffgType> retrievedNffgsList = retrievedNffgs.getNffg();

		// Retrieving IN data
		Map<String, VNFTypeReader> catalog = CatalogManager.retrieve();
		Map<String, HostReader> hosts = HostManager.retrieve();

		// Building result set
		Set<NffgReader> result = new HashSet<>();
		for (NffgType retrievedNffg : retrievedNffgsList)
			result.add(new NffgReaderImpl(retrievedNffg, hosts, catalog));

		return result;
	}

	public static NffgReader retrieve(String nffgId) throws ServiceException {
		// NffgType retrievedNffg = Localhost_NfvDeployerRest
		// .nffgs(Localhost_NfvDeployerRest.createClient(),
		// Localhost_NfvDeployerRest.BASE_URI).nffg_id(nffgId)
		// .getAsXml(NffgType.class);

		NffgType retrievedNffg = NfvClientImpl.getTarget().path("nffgs").path(nffgId).request(MediaType.APPLICATION_XML)
				.get(NffgType.class);

		return new NffgReaderImpl(retrievedNffg, HostManager.retrieve(), CatalogManager.retrieve());
	}
}
