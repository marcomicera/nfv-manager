//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.25 at 12:09:25 AM CET 
//


package it.polito.dp2.NFV.sol3.service.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for functionalTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="functionalTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="CACHE"/>
 *     &lt;enumeration value="DPI"/>
 *     &lt;enumeration value="FW"/>
 *     &lt;enumeration value="MAIL_CLIENT"/>
 *     &lt;enumeration value="MAIL_SERVER"/>
 *     &lt;enumeration value="NAT"/>
 *     &lt;enumeration value="SPAM"/>
 *     &lt;enumeration value="VPN"/>
 *     &lt;enumeration value="WEB_CLIENT"/>
 *     &lt;enumeration value="WEB_SERVER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "functionalTypeType")
@XmlEnum
public enum FunctionalTypeType {

    CACHE,
    DPI,
    FW,
    MAIL_CLIENT,
    MAIL_SERVER,
    NAT,
    SPAM,
    VPN,
    WEB_CLIENT,
    WEB_SERVER;

    public String value() {
        return name();
    }

    public static FunctionalTypeType fromValue(String v) {
        return valueOf(v);
    }

}