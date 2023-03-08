//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:04 PM CEST 
//


package it.izs.wsdl.wsBDNAgea;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà pAllevId.
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
     * Imposta il valore della proprietà pAllevId.
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
     * Recupera il valore della proprietà cuua.
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
     * Imposta il valore della proprietà cuua.
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
     * Recupera il valore della proprietà pCodRegione.
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
     * Imposta il valore della proprietà pCodRegione.
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
     * Recupera il valore della proprietà pCodMacello.
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
     * Imposta il valore della proprietà pCodMacello.
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
     * Recupera il valore della proprietà pIdFiscaleMac.
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
     * Imposta il valore della proprietà pIdFiscaleMac.
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
     * Recupera il valore della proprietà dtInizioPeriodo.
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
     * Imposta il valore della proprietà dtInizioPeriodo.
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
     * Recupera il valore della proprietà dtFinePeriodo.
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
     * Imposta il valore della proprietà dtFinePeriodo.
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
