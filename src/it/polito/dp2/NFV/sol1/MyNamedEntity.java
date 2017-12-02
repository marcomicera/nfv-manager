package it.polito.dp2.NFV.sol1;

public class MyNamedEntity implements it.polito.dp2.NFV.NamedEntityReader {
	private String name;

	MyNamedEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}