# `nfv-manager`: NFV management using Neo4j
*NFV* stands for **Network Function Virtualization**, an architecture concept that decouples network functions (NAT, DNS, firewalling, etc.) from hardware appliances so they can run in software.

This software is composed by three parts:
1. The `reader` is a java JAXB application that serializes data about this *DP2-NFV* system into an XML file that is valid with respect to the XSD schema developed in this same part;
2. The `loader` part must load randomly-generated data to a Neo4J database and it must be provide reachability information from node A to node B;
3. The `deployer` is a web services that simply deploys *NF-FG*s (Network Function-Forwarding Graphs) into the system. This directory also contains three client examples that make use of this web service.
