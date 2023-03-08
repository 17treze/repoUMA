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
 *         &lt;element name="p_dt_inizio_controllo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_fine_controllo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_ultima_estrazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pDtInizioControllo",
    "pDtFineControllo",
    "pDtUltimaEstrazione"
})
@XmlRootElement(name = "Variazioni_Controlli", namespace = "http://bdr.izs.it/webservices")
public class VariazioniControlli {

    @XmlElement(name = "p_dt_inizio_controllo", namespace = "http://bdr.izs.it/webservices")
    protected String pDtInizioControllo;
    @XmlElement(name = "p_dt_fine_controllo", namespace = "http://bdr.izs.it/webservices")
    protected String pDtFineControllo;
    @XmlElement(name = "p_dt_ultima_estrazione", namespace = "http://bdr.izs.it/webservices")
    protected String pDtUltimaEstrazione;

    /**
     * Recupera il valore della proprietà pDtInizioControllo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtInizioControllo() {
        return pDtInizioControllo;
    }

    /**
     * Imposta il valore della proprietà pDtInizioControllo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtInizioControllo(String value) {
        this.pDtInizioControllo = value;
    }

    /**
     * Recupera il valore della proprietà pDtFineControllo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtFineControllo() {
        return pDtFineControllo;
    }

    /**
     * Imposta il valore della proprietà pDtFineControllo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtFineControllo(String value) {
        this.pDtFineControllo = value;
    }

    /**
     * Recupera il valore della proprietà pDtUltimaEstrazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtUltimaEstrazione() {
        return pDtUltimaEstrazione;
    }

    /**
     * Imposta il valore della proprietà pDtUltimaEstrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtUltimaEstrazione(String value) {
        this.pDtUltimaEstrazione = value;
    }

}
