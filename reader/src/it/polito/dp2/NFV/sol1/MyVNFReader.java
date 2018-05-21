package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.sol1.jaxb.FunctionalTypeType;

public class MyVNFReader extends MyNamedEntity implements it.polito.dp2.NFV.VNFTypeReader {
	private FunctionalType functionalType;
    private int requiredMemory;
    private int requiredStorage;
	
    MyVNFReader(String id, 
    			FunctionalType functionalType, 
    			int requiredMemory, 
    			int requiredStorage) {
		super(id);
		
		if(	functionalType == null || 
			requiredMemory < 0 || 
			requiredStorage < 0
		) {
			System.err.println("Invalid VNF's parameters");
			System.exit(1);
		}
		
		this.functionalType = functionalType;
		this.requiredMemory = requiredMemory;
		this.requiredStorage = requiredStorage;
	}
    
    MyVNFReader(String id, 
				String functionalType,
				int requiredMemory, 
				int requiredStorage) {
		this(
			id,
			functionalType == null ? null : FunctionalType.fromValue(functionalType),
			requiredMemory,
			requiredStorage
		);
	}
    
    MyVNFReader(String id, 
    			FunctionalTypeType functionalType,
    			int requiredMemory, 
    			int requiredStorage) {
    	this(
			id,
			functionalType == null ? null : functionalType.value(),
			requiredMemory,
			requiredStorage
		);
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