
package org.pcConfigurator.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deliveryTime" type="{http://spring.io/guides/gs-producing-web-service}deliveryTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "deliveryTime"
})
@XmlRootElement(name = "getDeliveryTimeResponse")
public class GetDeliveryTimeResponse {

    @XmlElement(required = true)
    protected DeliveryTime deliveryTime;

    /**
     * Ruft den Wert der deliveryTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DeliveryTime }
     *     
     */
    public DeliveryTime getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * Legt den Wert der deliveryTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliveryTime }
     *     
     */
    public void setDeliveryTime(DeliveryTime value) {
        this.deliveryTime = value;
    }

}
