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
 *         &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "cuaa"
})
@XmlRootElement(name = "Download_Allevamenti", namespace = "http://bdr.izs.it/webservices")
public class DownloadAllevamenti {

    @XmlElement(name = "CUAA", namespace = "http://bdr.izs.it/webservices")
    protected String cuaa;

    /**
     * Recupera il valore della proprietà cuaa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUAA() {
        return cuaa;
    }

    /**
     * Imposta il valore della proprietà cuaa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUAA(String value) {
        this.cuaa = value;
    }

}
