//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:19 PM CEST 
//


package it.izs.wsdl.wsBDNDomandaUnica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per clsPremio_Validazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="clsPremio_Validazione"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Id_alle_BDN" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Anno_Campagna" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Codice_Intervento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsPremio_Validazione", propOrder = {
    "cuaa",
    "idAlleBDN",
    "annoCampagna",
    "codiceIntervento"
})
@XmlSeeAlso({
    ClsPremioValidazionePP.class
})
public class ClsPremioValidazione {

    @XmlElement(name = "CUAA")
    protected String cuaa;
    @XmlElement(name = "Id_alle_BDN")
    protected long idAlleBDN;
    @XmlElement(name = "Anno_Campagna")
    protected int annoCampagna;
    @XmlElement(name = "Codice_Intervento")
    protected String codiceIntervento;

    /**
     * Recupera il valore della proprietà cuaa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUAA() {
        return cuaa;
    }

    /**
     * Imposta il valore della proprietà cuaa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUAA(String value) {
        this.cuaa = value;
    }

    /**
     * Recupera il valore della proprietà idAlleBDN.
     * 
     */
    public long getIdAlleBDN() {
        return idAlleBDN;
    }

    /**
     * Imposta il valore della proprietà idAlleBDN.
     * 
     */
    public void setIdAlleBDN(long value) {
        this.idAlleBDN = value;
    }

    /**
     * Recupera il valore della proprietà annoCampagna.
     * 
     */
    public int getAnnoCampagna() {
        return annoCampagna;
    }

    /**
     * Imposta il valore della proprietà annoCampagna.
     * 
     */
    public void setAnnoCampagna(int value) {
        this.annoCampagna = value;
    }

    /**
     * Recupera il valore della proprietà codiceIntervento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceIntervento() {
        return codiceIntervento;
    }

    /**
     * Imposta il valore della proprietà codiceIntervento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceIntervento(String value) {
        this.codiceIntervento = value;
    }

}
