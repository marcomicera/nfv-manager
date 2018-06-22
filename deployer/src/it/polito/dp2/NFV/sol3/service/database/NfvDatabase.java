package it.polito.dp2.NFV.sol3.service.database;

import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.lab1.NfvInfo;
import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;
import it.polito.dp2.NFV.sol3.service.MyHostPair;
import it.polito.dp2.NFV.sol3.service.gen.model.CatalogType;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelType;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelsType;
import it.polito.dp2.NFV.sol3.service.gen.model.HostType;
import it.polito.dp2.NFV.sol3.service.gen.model.HostsType;
import it.polito.dp2.NFV.sol3.service.gen.model.NffgType;

public class NfvDatabase {

	/**
	 * Boolean indicating whether the NFV database has been already downloaded or
	 * not.
	 */
	private static boolean databaseAlreadyLoaded = false;

	/**
	 * Monitor through which the NFV database is retrieved.
	 */
	private static NfvReader monitor;

	/**
	 * It initializes the NFV database, by first loading the Nffg0 and then
	 * downloading data about the IN and the catalog.
	 * 
	 * @throws FactoryConfigurationError
	 *             if there is some error while retrieving an implementation of the
	 *             NfvReader interface.
	 * @throws NfvReaderException
	 *             if an implementation of {@code NfvReader} cannot be created.
	 * @throws AllocationException
	 *             if some error occurred during the deployment on Neo4J.
	 */
	public synchronized static void init() throws NfvReaderException, FactoryConfigurationError, AllocationException {
		// If the database has not been downloaded yet
		if (!databaseAlreadyLoaded) {
			// TODO debugging
			System.out.println("Downloading NFV info...");

			// Using the NfvReader interface to retrieve data
			monitor = NfvReaderFactory.newInstance().newNfvReader();
			
			// TODO debugging
			System.out.println("Input data:");
			new NfvInfo(monitor).printAll();

			// Downloading catalog data
			CatalogManager.download(monitor);

			// Downloading hosts information
			Map<String, HostType> retrievedHosts = HostManager.download(monitor);

			// Deploying hosts
			try {
				HostManager.deployHosts(retrievedHosts);
			} catch (UnknownEntityException | MalformedException e) {
				throw new AllocationException("Could not allocate physical IN hosts.");
			}

			// Downloading connection performances information
			ChannelManager.download(monitor);

			// Downloading Nffg0
			NffgType retrievedNffg0 = NffgManager.download("Nffg0", monitor);

			// Deploying Nffg0
			try {
				NffgManager.deploy(retrievedNffg0);
			} catch (MalformedException | UnknownEntityException | DatatypeConfigurationException e) {
				throw new AllocationException("Could not allocate Nffg0");
			}

			// Setting the database download status flag
			databaseAlreadyLoaded = true;
		}
	}

	public static CatalogType getCatalog() {
		return CatalogManager.getCatalog();
	}

	public static HostsType getHosts() {
		HostsType hostsResult = new HostsType();
		hostsResult.getHost().addAll(HostManager.getHosts().values());

		return hostsResult;
	}

	public static HostType getHost(String id) {
		return HostManager.getHost(id);
	}

	public static ChannelsType getChannels() {
		ChannelsType channelsResult = new ChannelsType();
		channelsResult.getChannel().addAll(ChannelManager.getChannels().values());

		return channelsResult;
	}

	public static ChannelType getChannel(String host1, String host2) {
		return ChannelManager.getChannels().get(new MyHostPair(host1, host2));
	}
}
