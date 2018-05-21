package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.NfvReaderException;

public class NfvReaderFactory extends it.polito.dp2.NFV.NfvReaderFactory {
	@Override
	public NfvReaderImpl newNfvReader() throws NfvReaderException {
		return new NfvReaderImpl();
	}
}