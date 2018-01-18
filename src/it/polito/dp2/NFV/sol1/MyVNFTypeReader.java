package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.sol1.jaxb.VNFType;

public class MyVNFTypeReader extends MyNamedEntity implements it.polito.dp2.NFV.VNFTypeReader {
	private VNFType info;
	
    MyVNFTypeReader(VNFType info) {
		super(info.getId() != null ? info.getId() : null);
				
		this.info = info;
	}
    
	@Override
	public FunctionalType getFunctionalType() {
		return FunctionalType.fromValue(info.getFunctionalType().value());
	}

	@Override
	public int getRequiredMemory() {
		return info.getRequiredMemory();
	}

	@Override
	public int getRequiredStorage() {
		return info.getRequiredStorage();
	}
}