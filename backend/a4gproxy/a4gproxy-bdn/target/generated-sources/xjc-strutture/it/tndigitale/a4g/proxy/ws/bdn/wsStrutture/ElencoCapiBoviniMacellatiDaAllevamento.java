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
 *         &lt;element name="p_azienda_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_allev_id_fiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_specie_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_data_da" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_data_a" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pAziendaCodice",
    "pAllevIdFiscale",
    "pSpecieCodice",
    "pDataDa",
    "pDataA"
})
@XmlRootElement(name = "Elenco_Capi_Bovini_Macellati_Da_Allevamento")
public class ElencoCapiBoviniMacellatiDaAllevamento {

    @XmlElement(name = "p_azienda_codice")
    protected String pAziendaCodice;
    @XmlElement(name = "p_allev_id_fiscale")
    protected String pAllevIdFiscale;
    @XmlElement(name = "p_specie_codice")
    protected String pSpecieCodice;
    @XmlElement(name = "p_data_da")
    protected String pDataDa;
    @XmlElement(name = "p_data_a")
    protected String pDataA;

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
     * Recupera il valore della proprietà pAllevIdFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAllevIdFiscale() {
        return pAllevIdFiscale;
    }

    /**
     * Imposta il valore della proprietà pAllevIdFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAllevIdFiscale(String value) {
        this.pAllevIdFiscale = value;
    }

    /**
     * Recupera il valore della proprietà pSpecieCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPSpecieCodice() {
        return pSpecieCodice;
    }

    /**
     * Imposta il valore della proprietà pSpecieCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPSpecieCodice(String value) {
        this.pSpecieCodice = value;
    }

    /**
     * Recupera il valore della proprietà pDataDa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDataDa() {
        return pDataDa;
    }

    /**
     * Imposta il valore della proprietà pDataDa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDataDa(String value) {
        this.pDataDa = value;
    }

    /**
     * Recupera il valore della proprietà pDataA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDataA() {
        return pDataA;
    }

    /**
     * Imposta il valore della proprietà pDataA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDataA(String value) {
        this.pDataA = value;
    }

}
