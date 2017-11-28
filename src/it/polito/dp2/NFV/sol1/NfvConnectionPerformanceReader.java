package it.polito.dp2.NFV.sol1;

public class NfvConnectionPerformanceReader implements it.polito.dp2.NFV.ConnectionPerformanceReader {
	private float throughput;
	private int latency;
	
	NfvConnectionPerformanceReader(float throughput, int latency) {
		this.throughput = throughput;
		this.latency = latency;
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