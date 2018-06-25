package it.polito.dp2.NFV.sol3.client1.readers;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.VNFType;

public class VNFTypeReaderImpl extends MyNamedEntity implements VNFTypeReader {
	private VNFType info;
	
	public VNFTypeReaderImpl(VNFType info) {
		super(info.getId());
		
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
