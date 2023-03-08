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
 *         &lt;element name="p_allev_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="storico" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="orderby_dtingresso" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
    "storico",
    "orderbyDtingresso"
})
@XmlRootElement(name = "getRegistriStalla_Ascii")
public class GetRegistriStallaAscii {

    @XmlElement(name = "p_allev_id")
    protected String pAllevId;
    protected boolean storico;
    @XmlElement(name = "orderby_dtingresso")
    protected boolean orderbyDtingresso;

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
     * Recupera il valore della proprietà storico.
     * 
     */
    public boolean isStorico() {
        return storico;
    }

    /**
     * Imposta il valore della proprietà storico.
     * 
     */
    public void setStorico(boolean value) {
        this.storico = value;
    }

    /**
     * Recupera il valore della proprietà orderbyDtingresso.
     * 
     */
    public boolean isOrderbyDtingresso() {
        return orderbyDtingresso;
    }

    /**
     * Imposta il valore della proprietà orderbyDtingresso.
     * 
     */
    public void setOrderbyDtingresso(boolean value) {
        this.orderbyDtingresso = value;
    }

}
