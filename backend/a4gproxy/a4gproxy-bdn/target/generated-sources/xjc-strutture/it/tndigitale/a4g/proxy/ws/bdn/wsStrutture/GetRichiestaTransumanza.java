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
 *         &lt;element name="p_tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_asl_codice_provenienza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_asl_codice_destinazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_richiesta_da" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_dt_richiesta_a" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pTipo",
    "pAslCodiceProvenienza",
    "pAslCodiceDestinazione",
    "pDtRichiestaDa",
    "pDtRichiestaA"
})
@XmlRootElement(name = "getRichiestaTransumanza")
public class GetRichiestaTransumanza {

    @XmlElement(name = "p_tipo")
    protected String pTipo;
    @XmlElement(name = "p_asl_codice_provenienza")
    protected String pAslCodiceProvenienza;
    @XmlElement(name = "p_asl_codice_destinazione")
    protected String pAslCodiceDestinazione;
    @XmlElement(name = "p_dt_richiesta_da")
    protected String pDtRichiestaDa;
    @XmlElement(name = "p_dt_richiesta_a")
    protected String pDtRichiestaA;

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
     * Recupera il valore della proprietà pAslCodiceProvenienza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAslCodiceProvenienza() {
        return pAslCodiceProvenienza;
    }

    /**
     * Imposta il valore della proprietà pAslCodiceProvenienza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAslCodiceProvenienza(String value) {
        this.pAslCodiceProvenienza = value;
    }

    /**
     * Recupera il valore della proprietà pAslCodiceDestinazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAslCodiceDestinazione() {
        return pAslCodiceDestinazione;
    }

    /**
     * Imposta il valore della proprietà pAslCodiceDestinazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAslCodiceDestinazione(String value) {
        this.pAslCodiceDestinazione = value;
    }

    /**
     * Recupera il valore della proprietà pDtRichiestaDa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtRichiestaDa() {
        return pDtRichiestaDa;
    }

    /**
     * Imposta il valore della proprietà pDtRichiestaDa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtRichiestaDa(String value) {
        this.pDtRichiestaDa = value;
    }

    /**
     * Recupera il valore della proprietà pDtRichiestaA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDtRichiestaA() {
        return pDtRichiestaA;
    }

    /**
     * Imposta il valore della proprietà pDtRichiestaA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDtRichiestaA(String value) {
        this.pDtRichiestaA = value;
    }

}
