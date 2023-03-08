//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:56 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.wsAnagraficaCapo;

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
 *         &lt;element name="p_storico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_cod_capo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pStorico",
    "pCodCapo"
})
@XmlRootElement(name = "getCapiOviniAllevamento_STR")
public class GetCapiOviniAllevamentoSTR {

    @XmlElement(name = "p_allev_id")
    protected String pAllevId;
    @XmlElement(name = "p_storico")
    protected String pStorico;
    @XmlElement(name = "p_cod_capo")
    protected String pCodCapo;

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
     * Recupera il valore della proprietà pStorico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPStorico() {
        return pStorico;
    }

    /**
     * Imposta il valore della proprietà pStorico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPStorico(String value) {
        this.pStorico = value;
    }

    /**
     * Recupera il valore della proprietà pCodCapo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCodCapo() {
        return pCodCapo;
    }

    /**
     * Imposta il valore della proprietà pCodCapo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodCapo(String value) {
        this.pCodCapo = value;
    }

}
