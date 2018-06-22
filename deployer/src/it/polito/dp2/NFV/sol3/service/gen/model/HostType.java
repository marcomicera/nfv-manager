//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.22 at 06:23:41 PM CEST 
//


package it.polito.dp2.NFV.sol3.service.gen.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for HostType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HostType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="node" type="{}NodeRefType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="availableMemory" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="availableStorage" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="maxVNFs" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HostType", propOrder = {
    "node"
})
public class HostType {

    protected List<NodeRefType> node;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String id;
    @XmlAttribute(name = "availableMemory")
    protected Integer availableMemory;
    @XmlAttribute(name = "availableStorage")
    protected Integer availableStorage;
    @XmlAttribute(name = "maxVNFs", required = true)
    protected int maxVNFs;

    /**
     * Gets the value of the node property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the node property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NodeRefType }
     * 
     * 
     */
    public List<NodeRefType> getNode() {
        if (node == null) {
            node = new ArrayList<NodeRefType>();
        }
        return this.node;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the availableMemory property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getAvailableMemory() {
        if (availableMemory == null) {
            return  0;
        } else {
            return availableMemory;
        }
    }

    /**
     * Sets the value of the availableMemory property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAvailableMemory(Integer value) {
        this.availableMemory = value;
    }

    /**
     * Gets the value of the availableStorage property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getAvailableStorage() {
        if (availableStorage == null) {
            return  0;
        } else {
            return availableStorage;
        }
    }

    /**
     * Sets the value of the availableStorage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAvailableStorage(Integer value) {
        this.availableStorage = value;
    }

    /**
     * Gets the value of the maxVNFs property.
     * 
     */
    public int getMaxVNFs() {
        return maxVNFs;
    }

    /**
     * Sets the value of the maxVNFs property.
     * 
     */
    public void setMaxVNFs(int value) {
        this.maxVNFs = value;
    }

}
