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
 *         &lt;element name="p_cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_inizio_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_fine_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pCuaa",
    "pDtInizioPeriodo",
    "pDtFinePeriodo"
})
@XmlRootElement(name = "Notifica_Registrazione_Uscite", namespace = "http://bdr.izs.it/webservices")
public class NotificaRegistrazioneUscite {

    @XmlElement(name = "p_cuaa", namespace = "http://bdr.izs.it/webservices")
    protected String pCuaa;
    @XmlElement(name = "p_dt_inizio_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String pDtInizioPeriodo;
    @XmlElement(name = "p_dt_fine_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String pDtFinePeriodo;

    /**
     * Gets the value of the pCuaa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCuaa() {
        return pCuaa;
    }

    /**
     * Sets the value of the pCuaa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCuaa(String value) {
        this.pCuaa = value;
    }

    /**
     * Gets the value of the pDtInizioPeriodo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtInizioPeriodo() {
        return pDtInizioPeriodo;
    }

    /**
     * Sets the value of the pDtInizioPeriodo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtInizioPeriodo(String value) {
        this.pDtInizioPeriodo = value;
    }

    /**
     * Gets the value of the pDtFinePeriodo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtFinePeriodo() {
        return pDtFinePeriodo;
    }

    /**
     * Sets the value of the pDtFinePeriodo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtFinePeriodo(String value) {
        this.pDtFinePeriodo = value;
    }

}
