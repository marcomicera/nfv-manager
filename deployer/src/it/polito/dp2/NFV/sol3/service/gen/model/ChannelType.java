//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.19 at 03:17:28 PM CEST 
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
 * <p>Java class for ChannelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ChannelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="host1" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="host2" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="averageThroughput" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="averageLatency" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChannelType")
public class ChannelType {

    @XmlAttribute(name = "host1", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String host1;
    @XmlAttribute(name = "host2", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String host2;
    @XmlAttribute(name = "averageThroughput")
    protected Float averageThroughput;
    @XmlAttribute(name = "averageLatency")
    protected Integer averageLatency;

    /**
     * Gets the value of the host1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost1() {
        return host1;
    }

    /**
     * Sets the value of the host1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost1(String value) {
        this.host1 = value;
    }

    /**
     * Gets the value of the host2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost2() {
        return host2;
    }

    /**
     * Sets the value of the host2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost2(String value) {
        this.host2 = value;
    }

    /**
     * Gets the value of the averageThroughput property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getAverageThroughput() {
        return averageThroughput;
    }

    /**
     * Sets the value of the averageThroughput property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setAverageThroughput(Float value) {
        this.averageThroughput = value;
    }

    /**
     * Gets the value of the averageLatency property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAverageLatency() {
        return averageLatency;
    }

    /**
     * Sets the value of the averageLatency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAverageLatency(Integer value) {
        this.averageLatency = value;
    }

}
