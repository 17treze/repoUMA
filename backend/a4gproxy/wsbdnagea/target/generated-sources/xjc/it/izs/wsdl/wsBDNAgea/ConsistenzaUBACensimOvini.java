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
 *         &lt;element name="data_inizio_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="data_fine_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_tipo_responsabilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "dataInizioPeriodo",
    "dataFinePeriodo",
    "pTipoResponsabilita"
})
@XmlRootElement(name = "Consistenza_UBA_Censim_Ovini", namespace = "http://bdr.izs.it/webservices")
public class ConsistenzaUBACensimOvini {

    @XmlElement(name = "p_cuaa", namespace = "http://bdr.izs.it/webservices")
    protected String pCuaa;
    @XmlElement(name = "data_inizio_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String dataInizioPeriodo;
    @XmlElement(name = "data_fine_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String dataFinePeriodo;
    @XmlElement(name = "p_tipo_responsabilita", namespace = "http://bdr.izs.it/webservices")
    protected String pTipoResponsabilita;

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
     * Gets the value of the dataInizioPeriodo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataInizioPeriodo() {
        return dataInizioPeriodo;
    }

    /**
     * Sets the value of the dataInizioPeriodo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataInizioPeriodo(String value) {
        this.dataInizioPeriodo = value;
    }

    /**
     * Gets the value of the dataFinePeriodo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataFinePeriodo() {
        return dataFinePeriodo;
    }

    /**
     * Sets the value of the dataFinePeriodo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataFinePeriodo(String value) {
        this.dataFinePeriodo = value;
    }

    /**
     * Gets the value of the pTipoResponsabilita property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPTipoResponsabilita() {
        return pTipoResponsabilita;
    }

    /**
     * Sets the value of the pTipoResponsabilita property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPTipoResponsabilita(String value) {
        this.pTipoResponsabilita = value;
    }

}
