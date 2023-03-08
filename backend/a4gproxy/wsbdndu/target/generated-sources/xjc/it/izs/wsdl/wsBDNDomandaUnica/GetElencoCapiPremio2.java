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
 *         &lt;element name="p_premio" type="{http://bdr.izs.it/webservices}clsPremio_Validazione_PP" minOccurs="0"/&gt;
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
    "pPremio"
})
@XmlRootElement(name = "get_Elenco_Capi_Premio2")
public class GetElencoCapiPremio2 {

    @XmlElement(name = "p_premio")
    protected ClsPremioValidazionePP pPremio;

    /**
     * Recupera il valore della proprietà pPremio.
     * 
     * @return
     *     possible object is
     *     {@link ClsPremioValidazionePP }
     *     
     */
    public ClsPremioValidazionePP getPPremio() {
        return pPremio;
    }

    /**
     * Imposta il valore della proprietà pPremio.
     * 
     * @param value
     *     allowed object is
     *     {@link ClsPremioValidazionePP }
     *     
     */
    public void setPPremio(ClsPremioValidazionePP value) {
        this.pPremio = value;
    }

}
