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
 *         &lt;element name="getRegistriUsciteOvini_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "getRegistriUsciteOviniSTRResult"
})
@XmlRootElement(name = "getRegistriUsciteOvini_STRResponse")
public class GetRegistriUsciteOviniSTRResponse {

    @XmlElement(name = "getRegistriUsciteOvini_STRResult")
    protected String getRegistriUsciteOviniSTRResult;

    /**
     * Recupera il valore della proprietà getRegistriUsciteOviniSTRResult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetRegistriUsciteOviniSTRResult() {
        return getRegistriUsciteOviniSTRResult;
    }

    /**
     * Imposta il valore della proprietà getRegistriUsciteOviniSTRResult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetRegistriUsciteOviniSTRResult(String value) {
        this.getRegistriUsciteOviniSTRResult = value;
    }

}
