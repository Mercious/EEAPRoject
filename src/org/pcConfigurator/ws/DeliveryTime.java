
package org.pcConfigurator.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r deliveryTime complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="deliveryTime">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deliveryTime" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deliveryTime", propOrder = {
    "deliveryTime"
})
public class DeliveryTime {

    protected int deliveryTime;

    /**
     * Ruft den Wert der deliveryTime-Eigenschaft ab.
     * 
     */
    public int getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * Legt den Wert der deliveryTime-Eigenschaft fest.
     * 
     */
    public void setDeliveryTime(int value) {
        this.deliveryTime = value;
    }

}
