/**
 * 
 */
package it.polito.dp2.NFV.lab3;

import it.polito.dp2.NFV.FactoryConfigurationError;

/**
 * Defines a factory API that enables applications to obtain one or more objects
 * implementing the {@link NfvClient} interface.
 *
 */
public abstract class NfvClientFactory {

	private static final String propertyName = "it.polito.dp2.NFV.NfvClientFactory";
	
	protected NfvClientFactory() {}
	
	/**
	 * Obtain a new instance of a <tt>NFfvClientFactory</tt>.
	 * 
	 * <p>
	 * This static method creates a new factory instance. This method uses the
	 * <tt>it.polito.dp2.NFV.NfvClientFactory</tt> system property to
	 * determine the NfvClientFactory implementation class to load.
	 * </p>
	 * <p>
	 * Once an application has obtained a reference to a
	 * <tt>NfvClientFactory</tt> it can use the factory to obtain a new
	 * {@link NfvClient} instance.
	 * </p>
	 * 
	 * @return a new instance of a <tt>NfvClientFactory</tt>.
	 * 
	 * @throws FactoryConfigurationError if the implementation is not available 
	 * or cannot be instantiated.
	 */
	public static NfvClientFactory newInstance() throws FactoryConfigurationError {
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		if(loader == null) {
			loader = NfvClientFactory.class.getClassLoader();
		}
		
		String className = System.getProperty(propertyName);
		if (className == null) {
			throw new FactoryConfigurationError("cannot create a new instance of a NfvClientFactory"
												+ "since the system property '" + propertyName + "'"
												+ "is not defined");
		}
		
		try {
			Class<?> c = (loader != null) ? loader.loadClass(className) : Class.forName(className);
			return (NfvClientFactory) c.newInstance();
		} catch (Exception e) {
			throw new FactoryConfigurationError(e, "error instantiatig class '" + className + "'.");
		}
	}
	
	
	/**
	 * Creates a new instance of a {@link NfvClient} implementation.
	 * 
	 * @return a new instance of a {@link NfvClient} implementation.
	 * @throws {@link NfvClientException} if an implementation of {@link NfvClient} cannot be created.
	 */
	public abstract NfvClient newNfvClient() throws NfvClientException;
}