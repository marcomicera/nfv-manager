package it.polito.dp2.NFV.sol3.client2;

import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;

public class NfvReaderFactory extends it.polito.dp2.NFV.NfvReaderFactory {

	private static final NfvReader implementation = new NfvReaderImpl();
	
	@Override
	public NfvReader newNfvReader() throws NfvReaderException {
		return implementation;
	}
	
}
