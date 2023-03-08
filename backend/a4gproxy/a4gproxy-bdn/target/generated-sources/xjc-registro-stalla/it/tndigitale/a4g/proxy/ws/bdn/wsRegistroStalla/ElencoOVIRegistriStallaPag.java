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
 *         &lt;element name="p_allev_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_codice_capo_da" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_offset" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pAllevId",
    "pTipo",
    "pCodiceCapoDa",
    "pOffset"
})
@XmlRootElement(name = "Elenco_OVI_RegistriStalla_Pag")
public class ElencoOVIRegistriStallaPag {

    @XmlElement(name = "p_allev_id")
    protected String pAllevId;
    @XmlElement(name = "p_tipo")
    protected String pTipo;
    @XmlElement(name = "p_codice_capo_da")
    protected String pCodiceCapoDa;
    @XmlElement(name = "p_offset")
    protected String pOffset;

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
     * Recupera il valore della proprietà pTipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPTipo() {
        return pTipo;
    }

    /**
     * Imposta il valore della proprietà pTipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPTipo(String value) {
        this.pTipo = value;
    }

    /**
     * Recupera il valore della proprietà pCodiceCapoDa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCodiceCapoDa() {
        return pCodiceCapoDa;
    }

    /**
     * Imposta il valore della proprietà pCodiceCapoDa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodiceCapoDa(String value) {
        this.pCodiceCapoDa = value;
    }

    /**
     * Recupera il valore della proprietà pOffset.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOffset() {
        return pOffset;
    }

    /**
     * Imposta il valore della proprietà pOffset.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOffset(String value) {
        this.pOffset = value;
    }

}
