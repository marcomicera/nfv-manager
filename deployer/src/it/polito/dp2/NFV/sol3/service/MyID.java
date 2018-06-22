package it.polito.dp2.NFV.sol3.service;

/**
 * Wrapper key-class used for storing Neo4J entities with their double ID
 */
public class MyID {
	/**
	 * Entity ID
	 */
	private String entityId;
	
	/**
	 * Entity ID assigned by Neo4J
	 */
	private String neo4jId;

	public MyID(String entityId) {
		if (entityId == null || entityId.isEmpty() || "".compareTo(entityId) == 0) {
			System.err.println("The ID is null");
			System.exit(1);
		}

		this.entityId = entityId;
		this.neo4jId = null;
	}

	public MyID(String entityId, String neo4jId) {
		if (entityId == null || neo4jId == null || entityId.isEmpty() || neo4jId.isEmpty()
				|| "".compareTo(entityId) == 0 || "".compareTo(neo4jId) == 0) {
			System.err.println("At least one ID is null");
			System.exit(1);
		}

		this.entityId = entityId;
		this.neo4jId = neo4jId;
	}

	/**
	 * Hashing function used by the java.util.Map's get() function
	 */
	@Override
	public int hashCode() {
		return entityId.hashCode();
	}

	/**
	 * Equality check function used by the java.util.Map's get() function
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MyID))
			return false;

		MyID otherPair = (MyID) o;

		return entityId.compareTo(otherPair.entityId) == 0;
	}

	/**
	 * Pretty-printing
	 */
	@Override
	public String toString() {
		return "<" + entityId + ", " + neo4jId + ">";
	}

	public String getEntityId() {
		return entityId;
	}

	public String getNeo4jId() {
		return neo4jId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setNeo4jId(String neo4jId) {
		this.neo4jId = neo4jId;
	}
}