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
 *         &lt;element name="p_specie_codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_latitudine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_longitudine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_raggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pSpecieCodice",
    "pLatitudine",
    "pLongitudine",
    "pRaggio"
})
@XmlRootElement(name = "FindAllevamentoNelRaggio_STR")
public class FindAllevamentoNelRaggioSTR {

    @XmlElement(name = "p_specie_codice")
    protected String pSpecieCodice;
    @XmlElement(name = "p_latitudine")
    protected String pLatitudine;
    @XmlElement(name = "p_longitudine")
    protected String pLongitudine;
    @XmlElement(name = "p_raggio")
    protected String pRaggio;

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
     * Recupera il valore della proprietà pLatitudine.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPLatitudine() {
        return pLatitudine;
    }

    /**
     * Imposta il valore della proprietà pLatitudine.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPLatitudine(String value) {
        this.pLatitudine = value;
    }

    /**
     * Recupera il valore della proprietà pLongitudine.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPLongitudine() {
        return pLongitudine;
    }

    /**
     * Imposta il valore della proprietà pLongitudine.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPLongitudine(String value) {
        this.pLongitudine = value;
    }

    /**
     * Recupera il valore della proprietà pRaggio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRaggio() {
        return pRaggio;
    }

    /**
     * Imposta il valore della proprietà pRaggio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRaggio(String value) {
        this.pRaggio = value;
    }

}
