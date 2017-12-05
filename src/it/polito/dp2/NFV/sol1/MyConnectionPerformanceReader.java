package it.polito.dp2.NFV.sol1;

public class MyConnectionPerformanceReader implements it.polito.dp2.NFV.ConnectionPerformanceReader {
	private int latency;
	private float throughput;
	
	public MyConnectionPerformanceReader(int latency, float throughput) {
		if(latency < 0 || throughput < 0) {
			System.err.println("Invalid connection parameters");
			System.exit(1);
		}
		
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