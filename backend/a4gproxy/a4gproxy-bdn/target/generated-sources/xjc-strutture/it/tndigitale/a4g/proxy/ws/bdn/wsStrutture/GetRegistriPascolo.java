//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:57 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.wsStrutture;

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
 *         &lt;element name="p_capo_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_azienda_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_allev_idfiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_spe_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_pascolo_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_ingresso_allev" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_ingresso_pas" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pCapoCodice",
    "pAziendaCodice",
    "pAllevIdfiscale",
    "pSpeCodice",
    "pPascoloCodice",
    "pDtIngressoAllev",
    "pDtIngressoPas"
})
@XmlRootElement(name = "getRegistriPascolo")
public class GetRegistriPascolo {

    @XmlElement(name = "p_capo_codice")
    protected String pCapoCodice;
    @XmlElement(name = "p_azienda_codice")
    protected String pAziendaCodice;
    @XmlElement(name = "p_allev_idfiscale")
    protected String pAllevIdfiscale;
    @XmlElement(name = "p_spe_codice")
    protected String pSpeCodice;
    @XmlElement(name = "p_pascolo_codice")
    protected String pPascoloCodice;
    @XmlElement(name = "p_dt_ingresso_allev")
    protected String pDtIngressoAllev;
    @XmlElement(name = "p_dt_ingresso_pas")
    protected String pDtIngressoPas;

    /**
     * Recupera il valore della proprietà pCapoCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCapoCodice() {
        return pCapoCodice;
    }

    /**
     * Imposta il valore della proprietà pCapoCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCapoCodice(String value) {
        this.pCapoCodice = value;
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
     * Recupera il valore della proprietà pPascoloCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPPascoloCodice() {
        return pPascoloCodice;
    }

    /**
     * Imposta il valore della proprietà pPascoloCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPPascoloCodice(String value) {
        this.pPascoloCodice = value;
    }

    /**
     * Recupera il valore della proprietà pDtIngressoAllev.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtIngressoAllev() {
        return pDtIngressoAllev;
    }

    /**
     * Imposta il valore della proprietà pDtIngressoAllev.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtIngressoAllev(String value) {
        this.pDtIngressoAllev = value;
    }

    /**
     * Recupera il valore della proprietà pDtIngressoPas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtIngressoPas() {
        return pDtIngressoPas;
    }

    /**
     * Imposta il valore della proprietà pDtIngressoPas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtIngressoPas(String value) {
        this.pDtIngressoPas = value;
    }

}
