//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:36:51 PM CEST 
//


package it.izs.bdr.webservices;

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
 *         &lt;element name="p_cod_capo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_data_dal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_data_al" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pCodCapo",
    "pDataDal",
    "pDataAl"
})
@XmlRootElement(name = "Get_Capi_Ovini_Allevamento_Periodo")
public class GetCapiOviniAllevamentoPeriodo {

    @XmlElement(name = "p_allev_id")
    protected String pAllevId;
    @XmlElement(name = "p_cod_capo")
    protected String pCodCapo;
    @XmlElement(name = "p_data_dal")
    protected String pDataDal;
    @XmlElement(name = "p_data_al")
    protected String pDataAl;

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

    /**
     * Recupera il valore della proprietà pDataDal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDataDal() {
        return pDataDal;
    }

    /**
     * Imposta il valore della proprietà pDataDal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDataDal(String value) {
        this.pDataDal = value;
    }

    /**
     * Recupera il valore della proprietà pDataAl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDataAl() {
        return pDataAl;
    }

    /**
     * Imposta il valore della proprietà pDataAl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDataAl(String value) {
        this.pDataAl = value;
    }

}
