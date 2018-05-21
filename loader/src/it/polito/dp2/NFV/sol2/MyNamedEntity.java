package it.polito.dp2.NFV.sol2;

public class MyNamedEntity implements it.polito.dp2.NFV.NamedEntityReader {
	private String name;

	MyNamedEntity(String name) {
		// Name validity check
		if(name == null || name.isEmpty()) {
			System.err.println("Invalid name");
			System.exit(1);
		}
		
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}