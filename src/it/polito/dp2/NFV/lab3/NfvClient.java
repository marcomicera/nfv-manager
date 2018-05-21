package it.polito.dp2.NFV.lab3;


/**
 * An interface for interacting with a client of a service
 * that provides the possibility to
 * - deploy NF-FGs
 * - add nodes to already deployed NF-FGs
 * - add links to already deployed NF-FGs
 */
public interface NfvClient {
		
	/**
	 * Deploys a new NF-FG with the features described by an NffgDescriptor and returns an object that can be used to interact with the deployed NF-FG.
	 * @param nffg	an object that describes the NF-FG to be deployed
	 * @throws AllocationException if the NF-FG could not be deployed because allocation was not possible.
	 * @throws ServiceException	if any other error occurred when trying to deploy the NF-FG.
	 * returns an object that implements the DeployedNffg interface and that allows interaction with the deployed NF-FG
	 */
	DeployedNffg deployNffg(NffgDescriptor nffg) throws AllocationException, ServiceException;
	
	/**
	 * Looks for an already deployed NF-FG by name and returns an object that can be used to interact with it
	 * @param	name the name of the NF-FG we are looking for
	 * @return	an object that implements the DeployedNffg interface and that allows interaction with the deployed NF-FG
	 * @throws UnknownEntityException	if the name passed as argument does not correspond to a deployed NF-FG
	 * @throws ServiceException if any other error occurred when trying to access the service
	 */
	DeployedNffg getDeployedNffg(String name) throws UnknownEntityException, ServiceException;

}
