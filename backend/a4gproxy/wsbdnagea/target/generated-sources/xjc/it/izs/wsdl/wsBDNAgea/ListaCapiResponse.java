//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.03.13 at 04:32:46 PM CET 
//


package it.izs.wsdl.wsBDNAgea;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://bdr.izs.it/webservices/ResponseQuery.xsd}Lista_CapiResult" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "listaCapiResult"
})
@XmlRootElement(name = "Lista_CapiResponse", namespace = "http://bdr.izs.it/webservices")
public class ListaCapiResponse {

    @XmlElement(name = "Lista_CapiResult")
    protected ListaCapiResult listaCapiResult;

    /**
     * Gets the value of the listaCapiResult property.
     * 
     * @return
     *     possible object is
     *     {@link ListaCapiResult }
     *     
     */
    public ListaCapiResult getListaCapiResult() {
        return listaCapiResult;
    }

    /**
     * Sets the value of the listaCapiResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaCapiResult }
     *     
     */
    public void setListaCapiResult(ListaCapiResult value) {
        this.listaCapiResult = value;
    }

}
