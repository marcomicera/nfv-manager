package it.polito.dp2.NFV.sol3.client1.managers;

import java.util.HashMap;
import java.util.Map;

import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.CatalogType;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.Localhost_NfvDeployerRest;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.VNFType;
import it.polito.dp2.NFV.sol3.client1.readers.VNFTypeReaderImpl;

public class CatalogManager {
	/**
	 * Retrieves the catalog from the NfvDeployer web service.
	 * 
	 * @return the deployed catalog.
	 */
	public static Map<String, VNFTypeReader> retrieve() {
		// Retrieving the catalog
		CatalogType retrievedCatalog = Localhost_NfvDeployerRest
				.catalog(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
				.getAsXml(CatalogType.class);

		// Building the result map
		final Map<String, VNFTypeReader> result = new HashMap<>();
		for (VNFType retrievedVnf : retrievedCatalog.getVNF()) {
			result.put(retrievedVnf.getId(), new VNFTypeReaderImpl(retrievedVnf));
		}

		return result;
	}
}
