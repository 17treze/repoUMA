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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per clsPremio_Validazione_PP complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="clsPremio_Validazione_PP"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://bdr.izs.it/webservices}clsPremio_Validazione"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CUAA2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsPremio_Validazione_PP", propOrder = {
    "cuaa2"
})
public class ClsPremioValidazionePP
    extends ClsPremioValidazione
{

    @XmlElement(name = "CUAA2")
    protected String cuaa2;

    /**
     * Recupera il valore della proprietà cuaa2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUAA2() {
        return cuaa2;
    }

    /**
     * Imposta il valore della proprietà cuaa2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUAA2(String value) {
        this.cuaa2 = value;
    }

}
