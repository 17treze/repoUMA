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
 *         &lt;element name="p_azienda_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_ordinamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pAziendaId",
    "pTipo",
    "pOrdinamento"
})
@XmlRootElement(name = "Elenco_RegistriPascolo_Aziendale_STR")
public class ElencoRegistriPascoloAziendaleSTR {

    @XmlElement(name = "p_azienda_id")
    protected String pAziendaId;
    @XmlElement(name = "p_tipo")
    protected String pTipo;
    @XmlElement(name = "p_ordinamento")
    protected String pOrdinamento;

    /**
     * Recupera il valore della proprietà pAziendaId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAziendaId() {
        return pAziendaId;
    }

    /**
     * Imposta il valore della proprietà pAziendaId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAziendaId(String value) {
        this.pAziendaId = value;
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
     * Recupera il valore della proprietà pOrdinamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOrdinamento() {
        return pOrdinamento;
    }

    /**
     * Imposta il valore della proprietà pOrdinamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOrdinamento(String value) {
        this.pOrdinamento = value;
    }

}
