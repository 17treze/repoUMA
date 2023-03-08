//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:54 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.wsRegistroStalla;

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
 *         &lt;element name="p_capo_codice_madre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_inizio_lattazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pCapoCodiceMadre",
    "pDtInizioLattazione"
})
@XmlRootElement(name = "getCapoLattazione")
public class GetCapoLattazione {

    @XmlElement(name = "p_capo_codice_madre")
    protected String pCapoCodiceMadre;
    @XmlElement(name = "p_dt_inizio_lattazione")
    protected String pDtInizioLattazione;

    /**
     * Recupera il valore della proprietà pCapoCodiceMadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCapoCodiceMadre() {
        return pCapoCodiceMadre;
    }

    /**
     * Imposta il valore della proprietà pCapoCodiceMadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCapoCodiceMadre(String value) {
        this.pCapoCodiceMadre = value;
    }

    /**
     * Recupera il valore della proprietà pDtInizioLattazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtInizioLattazione() {
        return pDtInizioLattazione;
    }

    /**
     * Imposta il valore della proprietà pDtInizioLattazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtInizioLattazione(String value) {
        this.pDtInizioLattazione = value;
    }

}
