package it.polito.dp2.NFV.lab3.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.dp2.NFV.*;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.LinkAlreadyPresentException;
import it.polito.dp2.NFV.lab3.LinkDescriptor;
import it.polito.dp2.NFV.lab3.NffgDescriptor;
import it.polito.dp2.NFV.lab3.NfvClient;
import it.polito.dp2.NFV.lab3.NfvClientException;
import it.polito.dp2.NFV.lab3.NfvClientFactory;
import it.polito.dp2.NFV.lab3.NodeDescriptor;
import it.polito.dp2.NFV.lab3.ServiceException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class NFVTests {

	private static NfvReader referenceNfvReader;	// reference data generator
	private static NfvReader initialTestNfvReader;			// NfvReader under test
	// private static NfvClient testNfvClient;			// NfvClient under test
	private static long testcase;
	private static URL serviceUrl; 
	private static int nffgNumber = 0;
	private static NffgReader referenceNffgReader=null;
	private static VNFTypeReader referenceVNFTypeReader=null;
	private static HostReader referenceHostReader=null;
	private static NodeReader referenceNodeReader=null;
	private static LinkReader referenceLinkReader=null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Create reference data generator
		System.setProperty("it.polito.dp2.NFV.NfvReaderFactory", "it.polito.dp2.NFV.Random.NfvReaderFactoryImpl");
		referenceNfvReader = NfvReaderFactory.newInstance().newNfvReader();
		nffgNumber = referenceNfvReader.getNffgs(null).size();
		
        // Create initial implementation under test
        System.setProperty("it.polito.dp2.NFV.NfvReaderFactory", "it.polito.dp2.NFV.sol3.client2.NfvReaderFactory");
        initialTestNfvReader = NfvReaderFactory.newInstance().newNfvReader();

		// Check the data set is adequate for the test
		boolean found=false;
		if(nffgNumber>0){	// we need at least one NF-FG
			referenceNffgReader = referenceNfvReader.getNffg("Nffg0");
			Set<NodeReader> ns = referenceNffgReader.getNodes();
			if (ns.size()>1) {	// we need at least two nodes
				TreeSet<NodeReader> nrts = new TreeSet<NodeReader>(new NamedEntityReaderComparator());
				nrts.addAll(ns);
				for (NodeReader node:nrts) {
					if (node.getLinks().size()>0) { // we need at least one link
						referenceNodeReader = node;
						TreeSet<LinkReader> lrts = new TreeSet<LinkReader>(new NamedEntityReaderComparator());
						lrts.addAll(node.getLinks());
						referenceLinkReader = lrts.iterator().next();
						break;
					}
				}
				TreeSet<VNFTypeReader> rvts = new TreeSet<VNFTypeReader>(new NamedEntityReaderComparator());
				rvts.addAll(referenceNfvReader.getVNFCatalog());
				for (VNFTypeReader vt:rvts) {
					if ((referenceHostReader = lookForHost(vt))!=null)
						referenceVNFTypeReader = vt;
					break;
				}				
			}
			if (referenceNffgReader!=null && referenceNodeReader!=null && referenceLinkReader!=null && referenceVNFTypeReader!=null)
				found = true;
		}
		assertEquals("Tests cannot run. Please choose another seed.",found,true);
		
		// read testcase property
		Long testcaseObj = Long.getLong("it.polito.dp2.NFV.Random.testcase");
		if (testcaseObj == null)
			testcase = 0;
		else
			testcase = testcaseObj.longValue();

	}

	/**
	 * Looks for an host on which a node with the given VNF type can be allocated
	 * @param vt the VNF type
	 * @return a HostReader for the found host or null if no host has been found
	 */
	private static HostReader lookForHost(VNFTypeReader vt) {
		TreeSet<HostReader> hts = new TreeSet<HostReader>(new NamedEntityReaderComparator());
		hts.addAll(referenceNfvReader.getHosts());
		for (HostReader h:hts) {
			// compute free memory and free storage
			int freeMemory = h.getAvailableMemory();
			int freeStorage = h.getAvailableStorage();
			for (NodeReader n:h.getNodes()) {
				VNFTypeReader type = n.getFuncType();
				freeMemory-=type.getRequiredMemory();
				freeStorage-=type.getRequiredStorage();
			}
			
			if (	h.getMaxVNFs()-h.getNodes().size()>0 &&
					freeMemory >= vt.getRequiredMemory() &&
					freeStorage >= vt.getRequiredStorage()
			)
				return h;
		}
		return null;
	}

	@Before
	public void setUp() throws Exception {
        assertNotNull("Could not run tests: the implementation under test generated a null NfvReader", initialTestNfvReader);
	}
	
	/**
	 * Starts the comparison of two sets of elements that extend NamedEntiryReader.
	 * This method already makes some comparisons that are independent of the type
	 * (e.g. the sizes of the sets must match). Then the method arranges the set
	 * elements into ordered sets (TreeSet) and returns a pair of iterators that
	 * can be used later on for one-to-one matching of the set elements
	 * @param rs	the first set to be compared
	 * @param ts	the second set to be compared
	 * @param type	a string that specified the type of data in the set (will appear in test messages)
	 * @return		a list made of two iterators to be used for one-to-one comparisons of the set elements
	 */
	public <T extends NamedEntityReader> List<Iterator<T>> startComparison(Set<T> rs, Set<T> ts, String type) {
		// if one of the two sets is null while the other isn't null, the test fails
		if ((rs == null) && (ts != null) || (rs != null) && (ts == null)) {
		    fail("returned set of "+type+" was null when it should be non-null or vice versa");
		    return null;
		}

		// if both sets are null, there are no data to compare, and the test passes
		if ((rs == null) && (ts == null)) {
		    assertTrue("there are no "+type+"!", true);
		    return null;
		}
		
		// check that the number of elements matches
		assertEquals("wrong Number of "+type, rs.size(), ts.size());
		
		// create TreeSets of elements, using the comparator for sorting, one for reference and one for implementation under test 
		TreeSet<T> rts = new TreeSet<T>(new NamedEntityReaderComparator());
		TreeSet<T> tts = new TreeSet<T>(new NamedEntityReaderComparator());
   
		rts.addAll(rs);
		tts.addAll(ts);
		
		// get iterators and store them in a list
		List<Iterator<T>> list = new ArrayList<Iterator<T>>();
		list.add(rts.iterator());
		list.add(tts.iterator());
		
		// return the list
		return list;

	}

	// method for comparing two non-null strings    
	private void compareString(String rs, String ts, String meaning) {
		assertNotNull("NULL "+meaning, ts);
		assertEquals("Wrong "+meaning, rs, ts);		
	}
	
	// method for comparing two float with precision of 0.1
	public void compareFloat(float rc, float tc, String meaning) {
		double precision=0.1;
		
		// Compute the condition to be checked
		boolean condition = (tc>(rc-precision)) && (tc<(rc+precision));
		
		assertTrue("wrong "+meaning, condition);
	}

	// creates an instance of the NfvClient under test
	private NfvClient createTestNfvClient() throws NfvClientException, FactoryConfigurationError {
		// Create client under test
		NfvClient testNfvClient = NfvClientFactory.newInstance().newNfvClient();
		assertNotNull("The implementation under test generated a null NfvClient", testNfvClient);
		return testNfvClient;
	}
	
	// creates an instance of the NfvReader under test
	private NfvReader createTestNfvReader() throws NfvReaderException, FactoryConfigurationError {
		// Create client under test
		System.setProperty("it.polito.dp2.NFV.NfvReaderFactory", "it.polito.dp2.NFV.sol3.client2.NfvReaderFactory");
		NfvReader testNfvReader = NfvReaderFactory.newInstance().newNfvReader();
		assertNotNull("The implementation under test generated a null NfvReader", testNfvReader);
		return testNfvReader;
	}

    @Test
    public final void testGetHosts() {
		// call getHosts on the two implementations
		Set<HostReader> rss = referenceNfvReader.getHosts();
		Set<HostReader> tss = initialTestNfvReader.getHosts();
		
		// compare the returned sets
		List<Iterator<HostReader>> list = startComparison(rss, tss, "Hosts");
		if (list!=null) {
			Iterator<HostReader> ri = list.get(0);
			Iterator<HostReader> ti = list.get(1);
			
			while (ri.hasNext() && ti.hasNext()) {
				compareHostReader(ri.next(),ti.next());
			}
		}
    }
    
    // private method for comparing two HostReader objects
	private void compareHostReader(HostReader rsr, HostReader tsr) {
		// check the HostReaders are not null
		assertNotNull("internal tester error: null server reader", rsr);
        assertNotNull("unexpected null server reader", tsr);
        
        System.out.println("Comparing server "+rsr.getName());

        // check the HostReaders return the same data 
        compareString(rsr.getName(), tsr.getName(), "server name");       
		assertEquals("wrong available memory in server", rsr.getAvailableMemory(), tsr.getAvailableMemory());
		assertEquals("wrong available storage in server", rsr.getAvailableStorage(), tsr.getAvailableStorage());
		assertEquals("wrong max vnfs in server", rsr.getMaxVNFs(), tsr.getMaxVNFs());
	}

    @Test
    public final void testGetPerformance() {
    	// get reference list of servers
		Set<HostReader> rss = referenceNfvReader.getHosts();
    	// for each pair of HostReader
    	for (HostReader server1:rss)
    		for(HostReader server2:rss) {
    			// call getConnectionPerformance on the two implementations
    			ConnectionPerformanceReader rpr = referenceNfvReader.getConnectionPerformance(server1, server2);
    			ConnectionPerformanceReader tpr = initialTestNfvReader.getConnectionPerformance(server1, server2);
    			compareConnectionPerformanceReader(rpr, tpr);
    		}
    }

	private void compareConnectionPerformanceReader(ConnectionPerformanceReader rpr, ConnectionPerformanceReader tpr) {
		// check the ConnectionPerformanceReaders are not null
		//assertNotNull("internal tester error: null ConnectionPerformanceReader", rpr);
        //assertNotNull("a null ConnectionPerformanceReader has been found", tpr);
        
        // check the ConnectionPerformanceReaders return the same data
		if(rpr!=null) {
			assertNotNull("a null ConnectionPerformanceReader has been found", tpr);
			compareFloat(rpr.getThroughput(), tpr.getThroughput(),"throughput");
			assertEquals("wrong latency", rpr.getLatency(), tpr.getLatency());
		}
	}

	@Test
	public final void testDeployNffg() throws Exception {
		System.out.println("DEBUG: starting testDeployNffg");
		if (nffgNumber>1) {
			// 1. make descriptor for Nffg1
			NffgDescriptor descr = new NffgDescriptor();
			NffgReader nffg = referenceNfvReader.getNffg("Nffg1");
			// 2. populate the set of node descriptors and add created descriptors to a map with name as index
			Set<NodeDescriptor> dnodes = descr.getNodes();
			HashMap<String,NodeDescriptor> nodeMap = new HashMap<String,NodeDescriptor>();
			for (NodeReader node:nffg.getNodes()) {
				// create descriptor for node and initialize it (except links)
				NodeDescriptor nd = new NodeDescriptor();		
				nd.setFuncType(node.getFuncType());
				HostReader hr = node.getHost();
				if (hr!=null)
					nd.setHostName(hr.getName());
				// add descriptor to set and to map
				dnodes.add(nd);
				nodeMap.put(node.getName(),nd);
			}
			// 3. now create link descriptors for each node descriptor
			for (NodeReader node:nffg.getNodes()) {
				NodeDescriptor nd = nodeMap.get(node.getName());
				for (LinkReader lr:node.getLinks()) {
					NodeDescriptor dest = nodeMap.get(lr.getDestinationNode().getName());
					LinkDescriptor ld = new LinkDescriptor();
					ld.setSourceNode(nd);
					ld.setDestinationNode(dest);
					ld.setThroughput(lr.getThroughput());
					ld.setLatency(lr.getLatency());
					nd.getLinks().add(ld);
				}
			}
			// 4 now we can try to deploy Nffg1
			// create client under test
			NfvClient testNfvClient = createTestNfvClient();
			// deploy
			testNfvClient.deployNffg(descr);
			// Now there should be two Nffgs
			compareNffgNumber(2);

		} else
			System.out.println("Deploy Nffg test skipped in this case");
	}
	
	private void compareNffgNumber(int expected) throws NfvReaderException, FactoryConfigurationError {
		// create new NfvReader
		NfvReader testNfvReader = createTestNfvReader();
		Set<NffgReader> set = testNfvReader.getNffgs(null);
		assertNotNull("getNffgs returned null set", set);
		assertEquals("Wrong number of Nffgs",expected,set.size());
	}

	@Test
	public final void testAddNodeAndLink() throws Exception {
		System.out.println("DEBUG: starting testAddNodeAndLink");
		// 1. Add a new node to Nffg0 and check the number of nodes has been increased by 1
		// create client under test
		NfvClient testNfvClient = createTestNfvClient();
		// get deployed Nffg "Nffg0"
		DeployedNffg nffg = testNfvClient.getDeployedNffg("Nffg0");
		// add node to deployed Nffg with reference VNFTypeReader and reference host name
		NodeReader newNode = nffg.addNode(referenceVNFTypeReader,referenceHostReader.getName());
		assertNotNull("Null NodeReader returned by addNode",newNode);
		String newNodeName = newNode.getName();
		assertNotNull("Null node name returned by addNode",newNodeName);
		
		// Now there should be one more node
		int expectedNodeNumber = referenceNffgReader.getNodes().size() +1;
		compareNodeNumber(expectedNodeNumber, referenceNffgReader.getName());
		compareNodeNumber(expectedNodeNumber, nffg);
		
		// 2. Add a new link to Nffg0 and check the link can be read back
		LinkReader newLink = nffg.addLink(newNode, referenceNodeReader, false);
		compareLink(newLink,"Nffg0",newNodeName);

	}
	
	private void compareLink(LinkReader newLink, String nffgName, String newNodeName) throws NfvReaderException, FactoryConfigurationError {
		assertNotNull("Null LinkReader passed to LinkReader", newLink);
		assertNotNull("Null source node in LinkReader",newLink.getSourceNode());
		assertNotNull("Null destination node in LinkReader",newLink.getDestinationNode());

		// create new NfvReader under test
		NfvReader testNfvReader = createTestNfvReader();
		
		// read links from this reader
		NffgReader nffgReader = testNfvReader.getNffg(nffgName);
		assertNotNull("Null NffgReader returned by the getter",nffgReader);
		NodeReader nodeReader = nffgReader.getNode(newNodeName);
		assertNotNull("Null NodeReader returned by getNode",nodeReader);
		Set<LinkReader> slr = nodeReader.getLinks();
		assertNotNull("Null set of LinkReader returned by getLinks",slr);
		// look for link with the same name as newLink
		boolean found = false;
		for (LinkReader lr:slr) {
			String name = lr.getName();
			
			assertNotNull("Null name returned by getName",name);
	
			if (name.equals(newLink.getName())) {
				found=true;
				// Link found: check its attributes
				// First extract attributes
				NodeReader source = lr.getSourceNode();
				assertNotNull("Null source node in LinkReader",source);
				
				NodeReader dest = lr.getDestinationNode();
				assertNotNull("Null destinatyion node in LinkReader",dest);
				
				float t = lr.getThroughput();
				int l = lr.getLatency();
				
				// Then compare source and destination names
				compareString(source.getName(), newLink.getSourceNode().getName(), "source node name");
				compareString(dest.getName(), newLink.getDestinationNode().getName(), "destination node name");
				// Then compare throughput and latency
				compareFloat(t, newLink.getThroughput(), "throughput");
				assertEquals("Wrong latency", l, newLink.getLatency());
			}
		}
		if (!found)
			fail("Link with the expected name not found");
		
	}

	/**
	 * Compares the expected number of nodes with the one that is obtained by reading data
	 * by means of a new  NfvReader
	 * @param expected the expected number of nodes
	 * @param nffgName	the name of the NF-FG for which the number of nodes has to be compared
	 * @throws NfvReaderException	if an NfvReader cannot be obtained
	 * @throws FactoryConfigurationError	if a factory configuration error occurs when trying to obtain the reader
	 */
	private void compareNodeNumber(int expected, String nffgName) throws NfvReaderException, FactoryConfigurationError {
		// create new NfvReader
		NfvReader testNfvReader = createTestNfvReader();
		
		// read nodes and check their number is as expected
		NffgReader nffgReader = testNfvReader.getNffg(nffgName);
		compareNodeNumber(expected, nffgReader);
	}

	/**
	 * Compares the expected number of nodes with the one that can be obtained by a given NffgReader
	 * @param expected the expected number of nodes
	 * @param nffgReader	the NffgReader to be used for the comparison
	 */
	private void compareNodeNumber(int expected, NffgReader nffgReader) {
		Set<NodeReader> tns = nffgReader.getNodes();
		assertNotNull("Null node set",tns);
		assertEquals("Wrong number of nodes", expected ,tns.size());
	}

	/**
	 * Compares the expected number of nodes with the one that can be obtained by a given DeployedNffg
	 * @param expected the expected number of nodes
	 * @param nffg the DeployedNffg to be used for the comparison
	 */
	private void compareNodeNumber(int expected, DeployedNffg nffg) throws ServiceException {
		compareNodeNumber(expected,nffg.getReader());
	}

	@Test
	public final void testOverwriteAlreadyExistingLink() throws Exception {
		System.out.println("DEBUG: starting testAddLink");
		// Load the reference link to the reference node of Nffg0 with overwrite=true
		// and check no exception is thrown
		
		// create client under test
		NfvClient testNfvClient = createTestNfvClient();
		// get deployed Nffg "Nffg0"
		DeployedNffg nffg = testNfvClient.getDeployedNffg("Nffg0");
		// add reference link to deployed Nffg 
		nffg.addLink(referenceLinkReader.getSourceNode(), referenceLinkReader.getDestinationNode(), true);		
	}
	
	@Test (expected=LinkAlreadyPresentException.class)
	public final void testAddAlreadyExistingLink() throws Exception {
		System.out.println("DEBUG: starting testAddLink");
		// Load the reference link to the reference node of Nffg0 with overwrite=false
		// and check the right exception is thrown
		
		// create client under test
		NfvClient testNfvClient = createTestNfvClient();
		// get deployed Nffg "Nffg0"
		DeployedNffg nffg = testNfvClient.getDeployedNffg("Nffg0");
		// add reference link to deployed Nffg 
		nffg.addLink(referenceLinkReader.getSourceNode(), referenceLinkReader.getDestinationNode(), false);			
	}

}

class NamedEntityReaderComparator implements Comparator<NamedEntityReader> {
    public int compare(NamedEntityReader f0, NamedEntityReader f1) {
    	return f0.getName().compareTo(f1.getName());
    }
}
