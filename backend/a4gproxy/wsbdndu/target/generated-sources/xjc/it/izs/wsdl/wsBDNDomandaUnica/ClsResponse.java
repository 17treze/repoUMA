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
 * <p>Classe Java per clsResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="clsResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ErrCod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ErrDescr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsResponse", propOrder = {
    "errCod",
    "errDescr"
})
@XmlSeeAlso({
    ClsPremioValidazioneResponse.class
})
public abstract class ClsResponse {

    @XmlElement(name = "ErrCod")
    protected String errCod;
    @XmlElement(name = "ErrDescr")
    protected String errDescr;

    /**
     * Recupera il valore della proprietà errCod.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrCod() {
        return errCod;
    }

    /**
     * Imposta il valore della proprietà errCod.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrCod(String value) {
        this.errCod = value;
    }

    /**
     * Recupera il valore della proprietà errDescr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrDescr() {
        return errDescr;
    }

    /**
     * Imposta il valore della proprietà errDescr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrDescr(String value) {
        this.errDescr = value;
    }

}
