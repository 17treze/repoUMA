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
 *         &lt;element name="get_Elenco_Capi_Premio2Result" type="{http://bdr.izs.it/webservices}clsPremio_ValidazioneResponse" minOccurs="0"/&gt;
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
    "getElencoCapiPremio2Result"
})
@XmlRootElement(name = "get_Elenco_Capi_Premio2Response")
public class GetElencoCapiPremio2Response {

    @XmlElement(name = "get_Elenco_Capi_Premio2Result")
    protected ClsPremioValidazioneResponse getElencoCapiPremio2Result;

    /**
     * Recupera il valore della proprietà getElencoCapiPremio2Result.
     * 
     * @return
     *     possible object is
     *     {@link ClsPremioValidazioneResponse }
     *     
     */
    public ClsPremioValidazioneResponse getGetElencoCapiPremio2Result() {
        return getElencoCapiPremio2Result;
    }

    /**
     * Imposta il valore della proprietà getElencoCapiPremio2Result.
     * 
     * @param value
     *     allowed object is
     *     {@link ClsPremioValidazioneResponse }
     *     
     */
    public void setGetElencoCapiPremio2Result(ClsPremioValidazioneResponse value) {
        this.getElencoCapiPremio2Result = value;
    }

}
