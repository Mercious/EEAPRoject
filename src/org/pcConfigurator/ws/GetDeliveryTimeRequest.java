
package org.pcConfigurator.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="articleID" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "articleID"
})
@XmlRootElement(name = "getDeliveryTimeRequest")
public class GetDeliveryTimeRequest {

    protected long articleID;

    /**
     * Ruft den Wert der articleID-Eigenschaft ab.
     * 
     */
    public long getArticleID() {
        return articleID;
    }

    /**
     * Legt den Wert der articleID-Eigenschaft fest.
     * 
     */
    public void setArticleID(long value) {
        this.articleID = value;
    }

}
