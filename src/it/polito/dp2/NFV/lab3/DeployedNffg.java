/**
 * 
 */
package it.polito.dp2.NFV.lab3;

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;

/**
 * An interface for interacting with an already deployed NF-FG
 *
 */
public interface DeployedNffg {
	/**
	 * Adds a new node to this already deployed NF-FG. The node is added without any link.
	 * A node name is chosen by the implementation
	 * @param type	the type of VNF the node to be added has to implement
	 * @param hostName	the name of the host requested for allocation or null if no specific host is requested
	 * @throws AllocationException if the node could not be added because allocation was not possible.
	 * @throws ServiceException	if any other error occurred when trying to deploy the NF-FG.
	 * returns	an interface for reading information about the added node.
	 */
	NodeReader addNode(VNFTypeReader type, String hostName) throws AllocationException, ServiceException;

	/**
	 * Adds a new link to this already deployed NF-FG without specification of throughput and latency requirements.
	 * A link name is chosen by the implementation
	 * @param source	the source node of the link
	 * @param dest	the destination node of the link
	 * @param overwrite	true if the link information has to be overwritten if the link was already present
	 * @throws NoNodeException if any of the nodes passed as arguments does not exist in the deployed NF-FG.
	 * @throws LinkAlreadyPresentException	if a link connecting the specified nodes was already present and overwrite is false.
	 * @throws ServiceException	if any other error occurred when trying to add the link.
	 * returns	an interface for reading information about the added link.
	 */
	LinkReader addLink(NodeReader source, NodeReader dest, boolean overwrite) throws NoNodeException, LinkAlreadyPresentException, ServiceException;

	/**
	 * Provides an NffgReader to read information about the deployed NF-FG.
	 * The reader must be up to date with the modifications made by the other methods of this interface
	 * @return an object that provides information about the deployed NF-FG
	 * @throws ServiceException if any error occurs when trying to create the reader
	 */
	NffgReader getReader() throws ServiceException;

}
