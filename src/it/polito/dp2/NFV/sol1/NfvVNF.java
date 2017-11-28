package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.FunctionalType;

public class NfvVNF extends NfvNamedEntity implements it.polito.dp2.NFV.VNFTypeReader {
	private String id;
	private FunctionalType functionalType;
    private int requiredMemory;
    private int requiredStorage;
	
    NfvVNF(String id, FunctionalType functionalType, int requiredMemory, int requiredStorage) {
		super(id);
		this.functionalType = functionalType;
		this.requiredMemory = requiredMemory;
		this.requiredStorage = requiredStorage;
	}
    
	@Override
	public String getName() {
		return id;
	}

	@Override
	public FunctionalType getFunctionalType() {
		return functionalType;
	}

	@Override
	public int getRequiredMemory() {
		return requiredMemory;
	}

	@Override
	public int getRequiredStorage() {
		return requiredStorage;
	}
}