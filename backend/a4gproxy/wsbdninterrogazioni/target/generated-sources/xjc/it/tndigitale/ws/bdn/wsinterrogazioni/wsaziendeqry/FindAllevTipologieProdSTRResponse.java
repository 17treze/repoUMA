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
 *         &lt;element name="FindAllev_Tipologie_Prod_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "findAllevTipologieProdSTRResult"
})
@XmlRootElement(name = "FindAllev_Tipologie_Prod_STRResponse")
public class FindAllevTipologieProdSTRResponse {

    @XmlElement(name = "FindAllev_Tipologie_Prod_STRResult")
    protected String findAllevTipologieProdSTRResult;

    /**
     * Recupera il valore della proprietà findAllevTipologieProdSTRResult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFindAllevTipologieProdSTRResult() {
        return findAllevTipologieProdSTRResult;
    }

    /**
     * Imposta il valore della proprietà findAllevTipologieProdSTRResult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFindAllevTipologieProdSTRResult(String value) {
        this.findAllevTipologieProdSTRResult = value;
    }

}
