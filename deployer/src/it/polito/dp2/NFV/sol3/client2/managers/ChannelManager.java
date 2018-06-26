package it.polito.dp2.NFV.sol3.client2.managers;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.sol3.client1.nfvdeployer.Localhost_NfvDeployerRest;
import it.polito.dp2.NFV.sol3.client1.readers.ConnectionPerformanceReaderImpl;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelType;

public class ChannelManager {
	public static ConnectionPerformanceReader retrieve(String host1, String host2) {
		// Retrieving the channel object from the NfvDeployer web service
		ChannelType retrievedChannel = Localhost_NfvDeployerRest
				.channels(Localhost_NfvDeployerRest.createClient(), Localhost_NfvDeployerRest.BASE_URI)
				.source_host_idDestination_host_id(host1, host2).getAsXml(ChannelType.class);
		
		return new ConnectionPerformanceReaderImpl(retrievedChannel);
	}
}
