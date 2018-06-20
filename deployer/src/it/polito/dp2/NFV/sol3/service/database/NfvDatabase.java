package it.polito.dp2.NFV.sol3.service.database;

import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.sol3.service.MyHostPair;
import it.polito.dp2.NFV.sol3.service.gen.model.CatalogType;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelType;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelsType;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;
import it.polito.dp2.NFV.sol3.service.gen.model.HostsType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;

public class NfvDatabase {
	
	/**
	 * Boolean indicating whether the NFV database has been
	 * already downloaded or not.
	 */
	private static boolean databaseAlreadyLoaded = false;
	
	/**
	 * Monitor through which the NFV database is retrieved.
	 */
	private static NfvReader monitor;
	
	/**
	 * It initializes the NFV database, by first loading the Nffg0
	 * and then downloading data about the IN and the catalog.
	 * @throws FactoryConfigurationError 	if there is some error while retrieving
	 * 										an implemenetation of the NfvReader interface. 
	 * @throws NfvReaderException 			if an implementation of 
	 * 										{@code NfvReader} cannot be created.
	 */
	public synchronized static void init() throws NfvReaderException, FactoryConfigurationError {
		// If the database has not been downloaded yet
		if(!databaseAlreadyLoaded) {
			// Using the NfvReader interface to retrieve data
			monitor = NfvReaderFactory.newInstance().newNfvReader();
			
			// Downloading Nffg0
			NffgType nffg0 = NffgManager.downloadNffg("Nffg0", monitor);
			
			// Deploying Nffg0
			NffgManager.deployNffg(nffg0);
			
			// Downloading the database
			downloadDatabase();

			// Setting the database download status flag
			databaseAlreadyLoaded = true;
		}
	}

	/**
	 * It retrieves the whole database content, storing it into this class' private
	 * fields.
	 */
	private static void downloadDatabase() {
		// Downloading catalog data
		CatalogManager.dowloadCatalog(monitor);
		
		// Downloading IN data
		downloadNetwork();
	}
	
	/**
	 * It retrieves the NFV's network information, consisting
	 * in physical hosts (INs) and physical channels (connections
	 * performance data)
	 */
	private static void downloadNetwork() {
		// TODO Retrieving hosts information
		HostManager.downloadHosts(monitor);
		
		// TODO Retrieving connection performances information
		ChannelManager.downloadChannels(monitor);
	}
 	
	public static CatalogType getCatalog() {
		return CatalogManager.catalog;
	}

	public static HostsType getHosts() {
		HostsType hostsResult = new HostsType();
		hostsResult.getHost().addAll(HostManager.hosts.values());
		
		return hostsResult;
	}
	
	public static HostType getHost(String id) {
		return HostManager.hosts.get(id);
	}
	
	public static ChannelsType getChannels() {
		ChannelsType channelsResult = new ChannelsType();
		channelsResult.getChannel().addAll(ChannelManager.channels.values());
		
		return channelsResult;
	}
	
	public static ChannelType getChannel(String host1, String host2) {
		return ChannelManager.channels.get(new MyHostPair(host1, host2));
	}
}
