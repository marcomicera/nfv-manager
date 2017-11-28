package it.polito.dp2.NFV.sol1;

public class NfvNamedEntity implements it.polito.dp2.NFV.NamedEntityReader {
	private String name;

	NfvNamedEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}