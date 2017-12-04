package it.polito.dp2.NFV.sol1;

public class MyConnectionPerformanceReader implements it.polito.dp2.NFV.ConnectionPerformanceReader {
	private int latency;
	private float throughput;
	
	public MyConnectionPerformanceReader(int latency, float throughput) {
		this.latency = latency;
		this.throughput = throughput;
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