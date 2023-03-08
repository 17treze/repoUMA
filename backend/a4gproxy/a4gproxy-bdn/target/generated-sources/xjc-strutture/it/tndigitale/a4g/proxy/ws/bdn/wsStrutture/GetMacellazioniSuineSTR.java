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
 *         &lt;element name="p_macello_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_regione_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_inizio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_fine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pMacelloCodice",
    "pRegioneCodice",
    "pDtInizio",
    "pDtFine"
})
@XmlRootElement(name = "getMacellazioniSuine_STR")
public class GetMacellazioniSuineSTR {

    @XmlElement(name = "p_macello_codice")
    protected String pMacelloCodice;
    @XmlElement(name = "p_regione_codice")
    protected String pRegioneCodice;
    @XmlElement(name = "p_dt_inizio")
    protected String pDtInizio;
    @XmlElement(name = "p_dt_fine")
    protected String pDtFine;

    /**
     * Recupera il valore della proprietà pMacelloCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPMacelloCodice() {
        return pMacelloCodice;
    }

    /**
     * Imposta il valore della proprietà pMacelloCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPMacelloCodice(String value) {
        this.pMacelloCodice = value;
    }

    /**
     * Recupera il valore della proprietà pRegioneCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRegioneCodice() {
        return pRegioneCodice;
    }

    /**
     * Imposta il valore della proprietà pRegioneCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRegioneCodice(String value) {
        this.pRegioneCodice = value;
    }

    /**
     * Recupera il valore della proprietà pDtInizio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtInizio() {
        return pDtInizio;
    }

    /**
     * Imposta il valore della proprietà pDtInizio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtInizio(String value) {
        this.pDtInizio = value;
    }

    /**
     * Recupera il valore della proprietà pDtFine.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtFine() {
        return pDtFine;
    }

    /**
     * Imposta il valore della proprietà pDtFine.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtFine(String value) {
        this.pDtFine = value;
    }

}
