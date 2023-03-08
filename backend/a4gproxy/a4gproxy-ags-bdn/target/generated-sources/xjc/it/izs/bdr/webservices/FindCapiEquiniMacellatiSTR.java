//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:36:51 PM CEST 
//


package it.izs.bdr.webservices;

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
 *         &lt;element name="p_codice_elettronico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_passaporto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_codice_ueln" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pCodiceElettronico",
    "pPassaporto",
    "pCodiceUeln"
})
@XmlRootElement(name = "find_Capi_Equini_Macellati_STR")
public class FindCapiEquiniMacellatiSTR {

    @XmlElement(name = "p_codice_elettronico")
    protected String pCodiceElettronico;
    @XmlElement(name = "p_passaporto")
    protected String pPassaporto;
    @XmlElement(name = "p_codice_ueln")
    protected String pCodiceUeln;

    /**
     * Recupera il valore della proprietà pCodiceElettronico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCodiceElettronico() {
        return pCodiceElettronico;
    }

    /**
     * Imposta il valore della proprietà pCodiceElettronico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodiceElettronico(String value) {
        this.pCodiceElettronico = value;
    }

    /**
     * Recupera il valore della proprietà pPassaporto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPPassaporto() {
        return pPassaporto;
    }

    /**
     * Imposta il valore della proprietà pPassaporto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPPassaporto(String value) {
        this.pPassaporto = value;
    }

    /**
     * Recupera il valore della proprietà pCodiceUeln.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCodiceUeln() {
        return pCodiceUeln;
    }

    /**
     * Imposta il valore della proprietà pCodiceUeln.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodiceUeln(String value) {
        this.pCodiceUeln = value;
    }

}
