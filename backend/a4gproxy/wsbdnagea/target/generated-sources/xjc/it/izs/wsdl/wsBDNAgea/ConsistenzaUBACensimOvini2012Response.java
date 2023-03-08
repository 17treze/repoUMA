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
 *         &lt;element ref="{http://bdr.izs.it/webservices/ResponseQuery.xsd}Consistenza_UBA_Censim_Ovini_2012Result" minOccurs="0"/&gt;
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
    "consistenzaUBACensimOvini2012Result"
})
@XmlRootElement(name = "Consistenza_UBA_Censim_Ovini_2012Response", namespace = "http://bdr.izs.it/webservices")
public class ConsistenzaUBACensimOvini2012Response {

    @XmlElement(name = "Consistenza_UBA_Censim_Ovini_2012Result")
    protected ConsistenzaUBACensimOvini2012Result consistenzaUBACensimOvini2012Result;

    /**
     * Recupera il valore della proprietà consistenzaUBACensimOvini2012Result.
     * 
     * @return
     *     possible object is
     *     {@link ConsistenzaUBACensimOvini2012Result }
     *     
     */
    public ConsistenzaUBACensimOvini2012Result getConsistenzaUBACensimOvini2012Result() {
        return consistenzaUBACensimOvini2012Result;
    }

    /**
     * Imposta il valore della proprietà consistenzaUBACensimOvini2012Result.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsistenzaUBACensimOvini2012Result }
     *     
     */
    public void setConsistenzaUBACensimOvini2012Result(ConsistenzaUBACensimOvini2012Result value) {
        this.consistenzaUBACensimOvini2012Result = value;
    }

}
