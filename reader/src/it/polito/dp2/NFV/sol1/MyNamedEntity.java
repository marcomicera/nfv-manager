package it.polito.dp2.NFV.sol1;

public class MyNamedEntity implements it.polito.dp2.NFV.NamedEntityReader {
	private String name;

	MyNamedEntity(String name) {
		if(name == null || name.isEmpty()) {
			System.err.println("Invalid name");
			System.exit(1);
		}
		
		this.name = name;
	}

	public String getName() {
		return name;
	}
}