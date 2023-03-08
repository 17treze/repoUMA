//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:23 PM CEST 
//


package it.tndigitale.ws.bdn.wsinterrogazioni.wsaziendeqry;

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
 *         &lt;element name="p_codice_azienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_id_fiscale_allev" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_specie_allevata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_anno_controllo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pCodiceAzienda",
    "pIdFiscaleAllev",
    "pSpecieAllevata",
    "pAnnoControllo"
})
@XmlRootElement(name = "findEsitoSieroprevalenzaIBR")
public class FindEsitoSieroprevalenzaIBR {

    @XmlElement(name = "p_codice_azienda")
    protected String pCodiceAzienda;
    @XmlElement(name = "p_id_fiscale_allev")
    protected String pIdFiscaleAllev;
    @XmlElement(name = "p_specie_allevata")
    protected String pSpecieAllevata;
    @XmlElement(name = "p_anno_controllo")
    protected String pAnnoControllo;

    /**
     * Recupera il valore della proprietà pCodiceAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCodiceAzienda() {
        return pCodiceAzienda;
    }

    /**
     * Imposta il valore della proprietà pCodiceAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodiceAzienda(String value) {
        this.pCodiceAzienda = value;
    }

    /**
     * Recupera il valore della proprietà pIdFiscaleAllev.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPIdFiscaleAllev() {
        return pIdFiscaleAllev;
    }

    /**
     * Imposta il valore della proprietà pIdFiscaleAllev.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPIdFiscaleAllev(String value) {
        this.pIdFiscaleAllev = value;
    }

    /**
     * Recupera il valore della proprietà pSpecieAllevata.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPSpecieAllevata() {
        return pSpecieAllevata;
    }

    /**
     * Imposta il valore della proprietà pSpecieAllevata.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPSpecieAllevata(String value) {
        this.pSpecieAllevata = value;
    }

    /**
     * Recupera il valore della proprietà pAnnoControllo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAnnoControllo() {
        return pAnnoControllo;
    }

    /**
     * Imposta il valore della proprietà pAnnoControllo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAnnoControllo(String value) {
        this.pAnnoControllo = value;
    }

}
