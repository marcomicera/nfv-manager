package it.polito.dp2.NFV.sol3.service.database;

import java.util.List;
import java.util.Set;

import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.service.NfvValidationProvider;
import it.polito.dp2.NFV.sol3.service.gen.model.CatalogType;
import it.polito.dp2.NFV.sol3.service.gen.model.FunctionalTypeType;
import it.polito.dp2.NFV.sol3.service.gen.model.VNFType;

public class CatalogManager {
	/**
	 * Catalog data
	 */
	private static CatalogType catalog = new CatalogType();

	/**
	 * It retrieves the NFV's catalog information
	 * 
	 * @param monitor
	 *            NfvReader interface through which data must be retrieved.
	 */
	protected synchronized static CatalogType download(NfvReader monitor) {
		// Retrieving catalog information
		Set<VNFTypeReader> retrievedCatalog = monitor.getVNFCatalog();

		// The VNF list contained in the local catalog
		List<VNFType> vnfList = catalog.getVNF();

		// Adding retrieved VNFs in the local catalog
		for (VNFTypeReader vnf : retrievedCatalog) {
			// New temporary VNF object
			VNFType tempVnf = new VNFType();

			// Setting properties
			tempVnf.setId(vnf.getName());
			tempVnf.setFunctionalType(FunctionalTypeType.fromValue(vnf.getFunctionalType().value()));
			tempVnf.setRequiredMemory(vnf.getRequiredMemory());
			tempVnf.setRequiredStorage(vnf.getRequiredStorage());

			// Adding the temporary VNF object to the catalog's VFN list
			vnfList.add(tempVnf);
		}

		// Returning the downloaded catalog object
		return catalog;
	}

	/**
	 * It checks whether a catalog object is well-formed or not.
	 * 
	 * @param catalog
	 *            the catalog to be checked.
	 * @return true if well-formed, false otherwise.
	 */
	public static boolean isWellFormed(CatalogType catalog) {
		return (new NfvValidationProvider<CatalogType>()).isReadable(catalog.getClass(), null, null, null);
	}

	public static synchronized CatalogType getCatalog() {
		return catalog;
	}
	
}
