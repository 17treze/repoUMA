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
 *         &lt;element name="p_allev_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_cod_regione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_cod_macello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_id_fiscale_mac" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dt_inizio_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dt_fine_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pAllevId",
    "cuua",
    "pCodRegione",
    "pCodMacello",
    "pIdFiscaleMac",
    "dtInizioPeriodo",
    "dtFinePeriodo"
})
@XmlRootElement(name = "Lista_Capi_Macellati_2", namespace = "http://bdr.izs.it/webservices")
public class ListaCapiMacellati2 {

    @XmlElement(name = "p_allev_id", namespace = "http://bdr.izs.it/webservices")
    protected String pAllevId;
    @XmlElement(name = "CUUA", namespace = "http://bdr.izs.it/webservices")
    protected String cuua;
    @XmlElement(name = "p_cod_regione", namespace = "http://bdr.izs.it/webservices")
    protected String pCodRegione;
    @XmlElement(name = "p_cod_macello", namespace = "http://bdr.izs.it/webservices")
    protected String pCodMacello;
    @XmlElement(name = "p_id_fiscale_mac", namespace = "http://bdr.izs.it/webservices")
    protected String pIdFiscaleMac;
    @XmlElement(name = "dt_inizio_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String dtInizioPeriodo;
    @XmlElement(name = "dt_fine_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String dtFinePeriodo;

    /**
     * Gets the value of the pAllevId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAllevId() {
        return pAllevId;
    }

    /**
     * Sets the value of the pAllevId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAllevId(String value) {
        this.pAllevId = value;
    }

    /**
     * Gets the value of the cuua property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUUA() {
        return cuua;
    }

    /**
     * Sets the value of the cuua property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUUA(String value) {
        this.cuua = value;
    }

    /**
     * Gets the value of the pCodRegione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCodRegione() {
        return pCodRegione;
    }

    /**
     * Sets the value of the pCodRegione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodRegione(String value) {
        this.pCodRegione = value;
    }

    /**
     * Gets the value of the pCodMacello property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCodMacello() {
        return pCodMacello;
    }

    /**
     * Sets the value of the pCodMacello property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodMacello(String value) {
        this.pCodMacello = value;
    }

    /**
     * Gets the value of the pIdFiscaleMac property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPIdFiscaleMac() {
        return pIdFiscaleMac;
    }

    /**
     * Sets the value of the pIdFiscaleMac property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPIdFiscaleMac(String value) {
        this.pIdFiscaleMac = value;
    }

    /**
     * Gets the value of the dtInizioPeriodo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDtInizioPeriodo() {
        return dtInizioPeriodo;
    }

    /**
     * Sets the value of the dtInizioPeriodo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDtInizioPeriodo(String value) {
        this.dtInizioPeriodo = value;
    }

    /**
     * Gets the value of the dtFinePeriodo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDtFinePeriodo() {
        return dtFinePeriodo;
    }

    /**
     * Sets the value of the dtFinePeriodo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDtFinePeriodo(String value) {
        this.dtFinePeriodo = value;
    }

}
