//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:57 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.wsStrutture;

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
 *         &lt;element name="p_codice_azienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_data_inizio_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_data_fine_periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pCodiceAzienda",
    "pDataInizioPeriodo",
    "pDataFinePeriodo"
})
@XmlRootElement(name = "Elenco_RegistriPascolo_Aziendale_Cod_STR")
public class ElencoRegistriPascoloAziendaleCodSTR {

    @XmlElement(name = "p_codice_azienda")
    protected String pCodiceAzienda;
    @XmlElement(name = "p_data_inizio_periodo")
    protected String pDataInizioPeriodo;
    @XmlElement(name = "p_data_fine_periodo")
    protected String pDataFinePeriodo;

    /**
     * Recupera il valore della proprietà pCodiceAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCodiceAzienda() {
        return pCodiceAzienda;
    }

    /**
     * Imposta il valore della proprietà pCodiceAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCodiceAzienda(String value) {
        this.pCodiceAzienda = value;
    }

    /**
     * Recupera il valore della proprietà pDataInizioPeriodo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDataInizioPeriodo() {
        return pDataInizioPeriodo;
    }

    /**
     * Imposta il valore della proprietà pDataInizioPeriodo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDataInizioPeriodo(String value) {
        this.pDataInizioPeriodo = value;
    }

    /**
     * Recupera il valore della proprietà pDataFinePeriodo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDataFinePeriodo() {
        return pDataFinePeriodo;
    }

    /**
     * Imposta il valore della proprietà pDataFinePeriodo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDataFinePeriodo(String value) {
        this.pDataFinePeriodo = value;
    }

}
