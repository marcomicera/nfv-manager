package it.polito.dp2.NFV.sol3.client1.managers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.client1.NfvClientImpl;
import it.polito.dp2.NFV.sol3.client1.readers.VNFTypeReaderImpl;
import it.polito.dp2.NFV.sol3.service.gen.model.CatalogType;
import it.polito.dp2.NFV.sol3.service.gen.model.VNFType;

public class CatalogManager {
	/**
	 * Retrieves the catalog from the NfvDeployer web service.
	 * 
	 * @return the deployed catalog.
	 */
	public static Map<String, VNFTypeReader> retrieve() {
		// Retrieving the catalog
//		CatalogType retrievedCatalog = Localhost_NfvDeployerRest
//				.catalog(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
//				.getAsXml(CatalogType.class);
		CatalogType retrievedCatalog = NfvClientImpl.getTarget().path("catalog").request(MediaType.APPLICATION_XML).get(CatalogType.class);

		// Building the result map
		final Map<String, VNFTypeReader> result = new HashMap<>();
		for (VNFType retrievedVnf : retrievedCatalog.getVNF()) {
			result.put(retrievedVnf.getId(), new VNFTypeReaderImpl(retrievedVnf));
		}

		return result;
	}
}
