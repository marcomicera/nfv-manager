//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.24 at 02:54:25 PM CEST 
//


package it.polito.dp2.NFV.sol3.service.gen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReachableEntitiesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReachableEntitiesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="nodes" type="{}NodesType"/>
 *         &lt;element name="hosts" type="{}HostsType"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReachableEntitiesType", propOrder = {

})
public class ReachableEntitiesType {

    @XmlElement(required = true)
    protected NodesType nodes;
    @XmlElement(required = true)
    protected HostsType hosts;

    /**
     * Gets the value of the nodes property.
     * 
     * @return
     *     possible object is
     *     {@link NodesType }
     *     
     */
    public NodesType getNodes() {
        return nodes;
    }

    /**
     * Sets the value of the nodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link NodesType }
     *     
     */
    public void setNodes(NodesType value) {
        this.nodes = value;
    }

    /**
     * Gets the value of the hosts property.
     * 
     * @return
     *     possible object is
     *     {@link HostsType }
     *     
     */
    public HostsType getHosts() {
        return hosts;
    }

    /**
     * Sets the value of the hosts property.
     * 
     * @param value
     *     allowed object is
     *     {@link HostsType }
     *     
     */
    public void setHosts(HostsType value) {
        this.hosts = value;
    }

}