//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:04 PM CEST 
//


package it.izs.wsdl.wsBDNAgea;

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
 *         &lt;element ref="{http://bdr.izs.it/webservices/ResponseQuery.xsd}Ovi_Notifica_Registrazione_UsciteResult" minOccurs="0"/&gt;
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
    "oviNotificaRegistrazioneUsciteResult"
})
@XmlRootElement(name = "Ovi_Notifica_Registrazione_UsciteResponse", namespace = "http://bdr.izs.it/webservices")
public class OviNotificaRegistrazioneUsciteResponse {

    @XmlElement(name = "Ovi_Notifica_Registrazione_UsciteResult")
    protected OviNotificaRegistrazioneUsciteResult oviNotificaRegistrazioneUsciteResult;

    /**
     * Recupera il valore della proprietà oviNotificaRegistrazioneUsciteResult.
     * 
     * @return
     *     possible object is
     *     {@link OviNotificaRegistrazioneUsciteResult }
     *     
     */
    public OviNotificaRegistrazioneUsciteResult getOviNotificaRegistrazioneUsciteResult() {
        return oviNotificaRegistrazioneUsciteResult;
    }

    /**
     * Imposta il valore della proprietà oviNotificaRegistrazioneUsciteResult.
     * 
     * @param value
     *     allowed object is
     *     {@link OviNotificaRegistrazioneUsciteResult }
     *     
     */
    public void setOviNotificaRegistrazioneUsciteResult(OviNotificaRegistrazioneUsciteResult value) {
        this.oviNotificaRegistrazioneUsciteResult = value;
    }

}
