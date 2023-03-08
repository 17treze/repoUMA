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
 *         &lt;element name="p_cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="data_inizio_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="data_fine_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pCuaa",
    "dataInizioPeriodo",
    "dataFinePeriodo"
})
@XmlRootElement(name = "Scarico_Allevamenti_Consistenza", namespace = "http://bdr.izs.it/webservices")
public class ScaricoAllevamentiConsistenza {

    @XmlElement(name = "p_cuaa", namespace = "http://bdr.izs.it/webservices")
    protected String pCuaa;
    @XmlElement(name = "data_inizio_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String dataInizioPeriodo;
    @XmlElement(name = "data_fine_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String dataFinePeriodo;

    /**
     * Recupera il valore della proprietà pCuaa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCuaa() {
        return pCuaa;
    }

    /**
     * Imposta il valore della proprietà pCuaa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCuaa(String value) {
        this.pCuaa = value;
    }

    /**
     * Recupera il valore della proprietà dataInizioPeriodo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataInizioPeriodo() {
        return dataInizioPeriodo;
    }

    /**
     * Imposta il valore della proprietà dataInizioPeriodo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataInizioPeriodo(String value) {
        this.dataInizioPeriodo = value;
    }

    /**
     * Recupera il valore della proprietà dataFinePeriodo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataFinePeriodo() {
        return dataFinePeriodo;
    }

    /**
     * Imposta il valore della proprietà dataFinePeriodo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataFinePeriodo(String value) {
        this.dataFinePeriodo = value;
    }

}
