# `nfv-manager`: NFV management using Neo4j
*NFV* stands for **Network Function Virtualization**, an architecture concept that decouples network functions (NAT, DNS, firewalling, etc.) from hardware appliances so they can run in software.

This software is composed of three parts:
1. [`reader`](reader) is a java JAXB application that serializes data about this *DP2-NFV* system into an XML file that is valid with respect to [an XSD schema](reader/xsd/nfvInfo.xsd).
2. [`loader`](loader) loads randomly-generated data to a Neo4J database and it must provide reachability information between two any given nodes.
3. [`deployer`](deployer) is a web service that simply deploys *NF-FG*s (Network Function-Forwarding Graphs) into the system. This directory also contains three client examples that make use of this web service.
