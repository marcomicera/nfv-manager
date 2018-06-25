package it.polito.dp2.NFV.sol3.service.database;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.sol3.service.MyHostPair;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelType;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelsType;

public class ChannelManager {
	/**
	 * Channels data
	 */
	private static Map<MyHostPair, ChannelType> channels = new ConcurrentHashMap<>();

	/**
	 * It retrieves data about the physicals channels in the Infrastructure Network.
	 * 
	 * @param monitor
	 *            NfvReader interface through which data must be retrieved.
	 */
	protected synchronized static void download(NfvReader monitor) {
		// Retrieving hosts information
		Set<HostReader> retrievedHosts = monitor.getHosts();

		// For each host pair
		for (HostReader host1 : retrievedHosts) {
			for (HostReader host2 : retrievedHosts) {
				// Retrieving the connection performances
				ConnectionPerformanceReader retrievedConnection = monitor.getConnectionPerformance(host1, host2);

				// New temporary channel object
				ChannelType tempChannel = new ChannelType();

				// Setting properties
				tempChannel.setHost1(host1.getName());
				tempChannel.setHost2(host2.getName());
				tempChannel.setAverageLatency(retrievedConnection.getLatency());
				tempChannel.setAverageThroughput(retrievedConnection.getThroughput());

				/*
				 * Adding the temporary channel object to the local channels map
				 */
				channels.put(new MyHostPair(host1, host2), tempChannel);
			}
		}
	}

	public static synchronized ChannelsType getChannels() {
		// Builnding the result object 
		ChannelsType result = new ChannelsType();
		result.getChannel().addAll(channels.values());

		return result;
	}

	/**
	 * Returns the channel between the two specified host.
	 * 
	 * @param host1
	 *            the source host from which the channel starts.
	 * @param host2
	 *            the destination host on which the channel ends.
	 * @return the desired channel object.
	 * 
	 */
	public static ChannelType getChannel(String host1, String host2) {
		return channels.get(new MyHostPair(host1, host2));
	}
	
	public static synchronized int howMany() {
		return channels.size();
	}
}
