package it.polito.dp2.NFV.sol3.service.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.sol3.service.MyHostPair;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelType;

public class ChannelManager {
	/**
	 * Channel data
	 */
	protected static Map<MyHostPair, ChannelType> channels = new HashMap<>();
	
	/**
	 * It retrieves data about the physicals channels in the
	 * Infrastructure Network.
	 * @param monitor	NfvReader interface through which data must be retrieved.
	 */
	protected static void downloadChannels(NfvReader monitor) {
		// Retrieving hosts information
		Set<HostReader> retrievedHosts = monitor.getHosts();
		
		// For each host pair
		 for(HostReader host1: retrievedHosts) {
			 for(HostReader host2: retrievedHosts) {
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
				  * Adding the temporary channel object to the
				  * local channels map
				  */
				 channels.put(
					 new MyHostPair(host1, host2),
					 tempChannel
				 );
			 }
		 }
	}
}
