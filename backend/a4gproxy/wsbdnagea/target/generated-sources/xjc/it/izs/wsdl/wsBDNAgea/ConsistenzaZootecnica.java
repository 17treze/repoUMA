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
 *         &lt;element name="CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_allev_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_data_richiesta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "cuua",
    "pAllevId",
    "pDataRichiesta"
})
@XmlRootElement(name = "Consistenza_Zootecnica", namespace = "http://bdr.izs.it/webservices")
public class ConsistenzaZootecnica {

    @XmlElement(name = "CUUA", namespace = "http://bdr.izs.it/webservices")
    protected String cuua;
    @XmlElement(name = "p_allev_id", namespace = "http://bdr.izs.it/webservices")
    protected String pAllevId;
    @XmlElement(name = "p_data_richiesta", namespace = "http://bdr.izs.it/webservices")
    protected String pDataRichiesta;

    /**
     * Recupera il valore della proprietà cuua.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUUA() {
        return cuua;
    }

    /**
     * Imposta il valore della proprietà cuua.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUUA(String value) {
        this.cuua = value;
    }

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
     * Recupera il valore della proprietà pDataRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDataRichiesta() {
        return pDataRichiesta;
    }

    /**
     * Imposta il valore della proprietà pDataRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDataRichiesta(String value) {
        this.pDataRichiesta = value;
    }

}
