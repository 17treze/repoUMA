//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:23 PM CEST 
//


package it.tndigitale.ws.bdn.wsinterrogazioni.wsaziendeqry;

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
 *         &lt;element name="p_dt_riferimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_tipologia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pDtRiferimento",
    "pTipologia"
})
@XmlRootElement(name = "FindAllevamento_DIFF")
public class FindAllevamentoDIFF {

    @XmlElement(name = "p_dt_riferimento")
    protected String pDtRiferimento;
    @XmlElement(name = "p_tipologia")
    protected String pTipologia;

    /**
     * Recupera il valore della proprietà pDtRiferimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtRiferimento() {
        return pDtRiferimento;
    }

    /**
     * Imposta il valore della proprietà pDtRiferimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtRiferimento(String value) {
        this.pDtRiferimento = value;
    }

    /**
     * Recupera il valore della proprietà pTipologia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPTipologia() {
        return pTipologia;
    }

    /**
     * Imposta il valore della proprietà pTipologia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPTipologia(String value) {
        this.pTipologia = value;
    }

}
