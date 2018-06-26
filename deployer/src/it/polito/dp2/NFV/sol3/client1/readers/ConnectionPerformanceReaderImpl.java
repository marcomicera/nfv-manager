package it.polito.dp2.NFV.sol3.client1.readers;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.sol3.service.gen.model.ChannelType;

public class ConnectionPerformanceReaderImpl implements ConnectionPerformanceReader {
	private int latency;
	private float throughput;
	
	public ConnectionPerformanceReaderImpl(ChannelType channel) {
		if(channel.getAverageLatency() < 0 || channel.getAverageThroughput() < 0) {
			System.err.println("Invalid connection parameters");
			System.exit(1);
		}
		
		this.latency = channel.getAverageLatency();
		this.throughput = channel.getAverageThroughput();
	}

	@Override
	public int getLatency() {
		return latency;
	}

	@Override
	public float getThroughput() {
		return throughput;
	}
}