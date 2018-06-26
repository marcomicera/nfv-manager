package it.polito.dp2.NFV.sol3.client1.managers;

import javax.ws.rs.core.MediaType;

import it.polito.dp2.NFV.sol3.client1.NfvClientImpl;

public class LinkManager {
	/**
	 * Retrieves the next link ID that can be used in the specified NF-FG ID.
	 * 
	 * @param nffgId
	 *            the NF-FG ID of which the next usable link ID must be returned.
	 * @return the next usable link ID in the NF-FG.
	 */
	public static int nextId(String nffgId) {
		// return 1 + Integer.valueOf(Localhost_NfvDeployerRest
		// .nffgs(Localhost_NfvDeployerRest.createClient(),
		// Localhost_NfvDeployerRest.BASE_URI)
		// .nffg_idLinksHowmany(nffgId).getAsTextPlain(String.class));

		return 1 + Integer.valueOf(NfvClientImpl.getTarget().path("nffgs").path(nffgId).path("links").path("howmany")
				.request(MediaType.TEXT_PLAIN).get(String.class));
	}
}
