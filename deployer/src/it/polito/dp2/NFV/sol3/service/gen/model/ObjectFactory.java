//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.19 at 03:53:59 PM CEST 
//


package it.polito.dp2.NFV.sol3.service.gen.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.polito.dp2.NFV.sol3.service.gen.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Node_QNAME = new QName("", "node");
    private final static QName _Nodes_QNAME = new QName("", "nodes");
    private final static QName _Channels_QNAME = new QName("", "channels");
    private final static QName _Nffgs_QNAME = new QName("", "nffgs");
    private final static QName _Catalog_QNAME = new QName("", "catalog");
    private final static QName _Hosts_QNAME = new QName("", "hosts");
    private final static QName _Host_QNAME = new QName("", "host");
    private final static QName _Channel_QNAME = new QName("", "channel");
    private final static QName _Link_QNAME = new QName("", "link");
    private final static QName _Nffg_QNAME = new QName("", "nffg");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.polito.dp2.NFV.sol3.service.gen.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NodeType }
     * 
     */
    public NodeType createNodeType() {
        return new NodeType();
    }

    /**
     * Create an instance of {@link NodesType }
     * 
     */
    public NodesType createNodesType() {
        return new NodesType();
    }

    /**
     * Create an instance of {@link ChannelsType }
     * 
     */
    public ChannelsType createChannelsType() {
        return new ChannelsType();
    }

    /**
     * Create an instance of {@link NffgsType }
     * 
     */
    public NffgsType createNffgsType() {
        return new NffgsType();
    }

    /**
     * Create an instance of {@link CatalogType }
     * 
     */
    public CatalogType createCatalogType() {
        return new CatalogType();
    }

    /**
     * Create an instance of {@link HostsType }
     * 
     */
    public HostsType createHostsType() {
        return new HostsType();
    }

    /**
     * Create an instance of {@link HostType }
     * 
     */
    public HostType createHostType() {
        return new HostType();
    }

    /**
     * Create an instance of {@link ChannelType }
     * 
     */
    public ChannelType createChannelType() {
        return new ChannelType();
    }

    /**
     * Create an instance of {@link LinkType }
     * 
     */
    public LinkType createLinkType() {
        return new LinkType();
    }

    /**
     * Create an instance of {@link NffgType }
     * 
     */
    public NffgType createNffgType() {
        return new NffgType();
    }

    /**
     * Create an instance of {@link VNFType }
     * 
     */
    public VNFType createVNFType() {
        return new VNFType();
    }

    /**
     * Create an instance of {@link NodeRefType }
     * 
     */
    public NodeRefType createNodeRefType() {
        return new NodeRefType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "node")
    public JAXBElement<NodeType> createNode(NodeType value) {
        return new JAXBElement<NodeType>(_Node_QNAME, NodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NodesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nodes")
    public JAXBElement<NodesType> createNodes(NodesType value) {
        return new JAXBElement<NodesType>(_Nodes_QNAME, NodesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChannelsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "channels")
    public JAXBElement<ChannelsType> createChannels(ChannelsType value) {
        return new JAXBElement<ChannelsType>(_Channels_QNAME, ChannelsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NffgsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nffgs")
    public JAXBElement<NffgsType> createNffgs(NffgsType value) {
        return new JAXBElement<NffgsType>(_Nffgs_QNAME, NffgsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CatalogType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "catalog")
    public JAXBElement<CatalogType> createCatalog(CatalogType value) {
        return new JAXBElement<CatalogType>(_Catalog_QNAME, CatalogType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HostsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "hosts")
    public JAXBElement<HostsType> createHosts(HostsType value) {
        return new JAXBElement<HostsType>(_Hosts_QNAME, HostsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HostType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "host")
    public JAXBElement<HostType> createHost(HostType value) {
        return new JAXBElement<HostType>(_Host_QNAME, HostType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChannelType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "channel")
    public JAXBElement<ChannelType> createChannel(ChannelType value) {
        return new JAXBElement<ChannelType>(_Channel_QNAME, ChannelType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "link")
    public JAXBElement<LinkType> createLink(LinkType value) {
        return new JAXBElement<LinkType>(_Link_QNAME, LinkType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NffgType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nffg")
    public JAXBElement<NffgType> createNffg(NffgType value) {
        return new JAXBElement<NffgType>(_Nffg_QNAME, NffgType.class, null, value);
    }

}
