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
 *         &lt;element name="p_dt_inizio_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_fine_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pDtInizioPeriodo",
    "pDtFinePeriodo"
})
@XmlRootElement(name = "Ovi_Notifica_Registrazione_Uscite", namespace = "http://bdr.izs.it/webservices")
public class OviNotificaRegistrazioneUscite {

    @XmlElement(name = "p_cuaa", namespace = "http://bdr.izs.it/webservices")
    protected String pCuaa;
    @XmlElement(name = "p_dt_inizio_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String pDtInizioPeriodo;
    @XmlElement(name = "p_dt_fine_periodo", namespace = "http://bdr.izs.it/webservices")
    protected String pDtFinePeriodo;

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
     * Recupera il valore della proprietà pDtInizioPeriodo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtInizioPeriodo() {
        return pDtInizioPeriodo;
    }

    /**
     * Imposta il valore della proprietà pDtInizioPeriodo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtInizioPeriodo(String value) {
        this.pDtInizioPeriodo = value;
    }

    /**
     * Recupera il valore della proprietà pDtFinePeriodo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtFinePeriodo() {
        return pDtFinePeriodo;
    }

    /**
     * Imposta il valore della proprietà pDtFinePeriodo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtFinePeriodo(String value) {
        this.pDtFinePeriodo = value;
    }

}
