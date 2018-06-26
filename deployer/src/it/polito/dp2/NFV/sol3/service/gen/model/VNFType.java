//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.26 at 10:43:04 PM CEST 
//


package it.polito.dp2.NFV.sol3.service.gen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for VNFType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VNFType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="functionalType" use="required" type="{}functionalTypeType" />
 *       &lt;attribute name="requiredMemory" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="requiredStorage" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VNFType")
public class VNFType {

    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String id;
    @XmlAttribute(name = "functionalType", required = true)
    protected FunctionalTypeType functionalType;
    @XmlAttribute(name = "requiredMemory", required = true)
    protected int requiredMemory;
    @XmlAttribute(name = "requiredStorage", required = true)
    protected int requiredStorage;

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
     * Gets the value of the functionalType property.
     * 
     * @return
     *     possible object is
     *     {@link FunctionalTypeType }
     *     
     */
    public FunctionalTypeType getFunctionalType() {
        return functionalType;
    }

    /**
     * Sets the value of the functionalType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FunctionalTypeType }
     *     
     */
    public void setFunctionalType(FunctionalTypeType value) {
        this.functionalType = value;
    }

    /**
     * Gets the value of the requiredMemory property.
     * 
     */
    public int getRequiredMemory() {
        return requiredMemory;
    }

    /**
     * Sets the value of the requiredMemory property.
     * 
     */
    public void setRequiredMemory(int value) {
        this.requiredMemory = value;
    }

    /**
     * Gets the value of the requiredStorage property.
     * 
     */
    public int getRequiredStorage() {
        return requiredStorage;
    }

    /**
     * Sets the value of the requiredStorage property.
     * 
     */
    public void setRequiredStorage(int value) {
        this.requiredStorage = value;
    }

}
