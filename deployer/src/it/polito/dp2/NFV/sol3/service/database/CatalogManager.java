package it.polito.dp2.NFV.sol3.service.database;

import java.util.List;
import java.util.Set;

import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.service.gen.model.CatalogType;
import it.polito.dp2.NFV.sol3.service.gen.model.FunctionalTypeType;
import it.polito.dp2.NFV.sol3.service.gen.model.VNFType;

public class CatalogManager {
	/**
	 * Catalog data
	 */
	protected static CatalogType catalog = new CatalogType();
	
	/**
	 * It retrieves the NFV's catalog information
	 * @param monitor	NfvReader interface through which data must be retrieved.
	 */
	protected static void dowloadCatalog(NfvReader monitor) {
		// Retrieving catalog information
		Set<VNFTypeReader> retrievedCatalog = monitor.getVNFCatalog();
		
		// The VNF list contained in the local catalog
		List<VNFType> vnfList = catalog.getVNF();
		
		// Adding retrieved VNFs in the local catalog
		for(VNFTypeReader vnf: retrievedCatalog) {
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
	}
}
