//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:54 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.wsRegistroStalla;

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
 *         &lt;element name="p_mac_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_reg_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_azienda_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_allev_idfiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_spe_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_paese_provenienza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_macellazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pMacCodice",
    "pRegCodice",
    "pAziendaCodice",
    "pAllevIdfiscale",
    "pSpeCodice",
    "pPaeseProvenienza",
    "pDtMacellazione"
})
@XmlRootElement(name = "FindMacellazioniSuiniPerEsameTrichinoscopico_STR")
public class FindMacellazioniSuiniPerEsameTrichinoscopicoSTR {

    @XmlElement(name = "p_mac_codice")
    protected String pMacCodice;
    @XmlElement(name = "p_reg_codice")
    protected String pRegCodice;
    @XmlElement(name = "p_azienda_codice")
    protected String pAziendaCodice;
    @XmlElement(name = "p_allev_idfiscale")
    protected String pAllevIdfiscale;
    @XmlElement(name = "p_spe_codice")
    protected String pSpeCodice;
    @XmlElement(name = "p_paese_provenienza")
    protected String pPaeseProvenienza;
    @XmlElement(name = "p_dt_macellazione")
    protected String pDtMacellazione;

    /**
     * Recupera il valore della proprietà pMacCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPMacCodice() {
        return pMacCodice;
    }

    /**
     * Imposta il valore della proprietà pMacCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPMacCodice(String value) {
        this.pMacCodice = value;
    }

    /**
     * Recupera il valore della proprietà pRegCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRegCodice() {
        return pRegCodice;
    }

    /**
     * Imposta il valore della proprietà pRegCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRegCodice(String value) {
        this.pRegCodice = value;
    }

    /**
     * Recupera il valore della proprietà pAziendaCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAziendaCodice() {
        return pAziendaCodice;
    }

    /**
     * Imposta il valore della proprietà pAziendaCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAziendaCodice(String value) {
        this.pAziendaCodice = value;
    }

    /**
     * Recupera il valore della proprietà pAllevIdfiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAllevIdfiscale() {
        return pAllevIdfiscale;
    }

    /**
     * Imposta il valore della proprietà pAllevIdfiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAllevIdfiscale(String value) {
        this.pAllevIdfiscale = value;
    }

    /**
     * Recupera il valore della proprietà pSpeCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPSpeCodice() {
        return pSpeCodice;
    }

    /**
     * Imposta il valore della proprietà pSpeCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPSpeCodice(String value) {
        this.pSpeCodice = value;
    }

    /**
     * Recupera il valore della proprietà pPaeseProvenienza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPPaeseProvenienza() {
        return pPaeseProvenienza;
    }

    /**
     * Imposta il valore della proprietà pPaeseProvenienza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPPaeseProvenienza(String value) {
        this.pPaeseProvenienza = value;
    }

    /**
     * Recupera il valore della proprietà pDtMacellazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtMacellazione() {
        return pDtMacellazione;
    }

    /**
     * Imposta il valore della proprietà pDtMacellazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtMacellazione(String value) {
        this.pDtMacellazione = value;
    }

}
