//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:19 PM CEST 
//


package it.izs.wsdl.wsBDNDomandaUnica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per clsCapo complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="clsCapo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Capo_Id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Dt_Nascita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Razza_Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Dt_Inizio_Detenzione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Fine_Detenzione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Capo_Cod_Madre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Cod_Libro_Madre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Desc_Libro_Madre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Azienda_Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Allev_Id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Dt_Macellazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsCapo", propOrder = {
    "capoId",
    "codice",
    "sesso",
    "dtNascita",
    "razzaCodice",
    "dtInizioDetenzione",
    "dtFineDetenzione",
    "capoCodMadre",
    "codLibroMadre",
    "descLibroMadre",
    "aziendaCodice",
    "allevId",
    "dtMacellazione"
})
public class ClsCapo {

    @XmlElement(name = "Capo_Id")
    protected long capoId;
    @XmlElement(name = "Codice")
    protected String codice;
    @XmlElement(name = "Sesso")
    protected String sesso;
    @XmlElement(name = "Dt_Nascita", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtNascita;
    @XmlElement(name = "Razza_Codice")
    protected String razzaCodice;
    @XmlElement(name = "Dt_Inizio_Detenzione", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtInizioDetenzione;
    @XmlElement(name = "Dt_Fine_Detenzione", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtFineDetenzione;
    @XmlElement(name = "Capo_Cod_Madre")
    protected String capoCodMadre;
    @XmlElement(name = "Cod_Libro_Madre")
    protected String codLibroMadre;
    @XmlElement(name = "Desc_Libro_Madre")
    protected String descLibroMadre;
    @XmlElement(name = "Azienda_Codice")
    protected String aziendaCodice;
    @XmlElement(name = "Allev_Id")
    protected long allevId;
    @XmlElement(name = "Dt_Macellazione", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtMacellazione;

    /**
     * Recupera il valore della proprietà capoId.
     * 
     */
    public long getCapoId() {
        return capoId;
    }

    /**
     * Imposta il valore della proprietà capoId.
     * 
     */
    public void setCapoId(long value) {
        this.capoId = value;
    }

    /**
     * Recupera il valore della proprietà codice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodice() {
        return codice;
    }

    /**
     * Imposta il valore della proprietà codice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodice(String value) {
        this.codice = value;
    }

    /**
     * Recupera il valore della proprietà sesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Imposta il valore della proprietà sesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSesso(String value) {
        this.sesso = value;
    }

    /**
     * Recupera il valore della proprietà dtNascita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtNascita() {
        return dtNascita;
    }

    /**
     * Imposta il valore della proprietà dtNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtNascita(XMLGregorianCalendar value) {
        this.dtNascita = value;
    }

    /**
     * Recupera il valore della proprietà razzaCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazzaCodice() {
        return razzaCodice;
    }

    /**
     * Imposta il valore della proprietà razzaCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazzaCodice(String value) {
        this.razzaCodice = value;
    }

    /**
     * Recupera il valore della proprietà dtInizioDetenzione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtInizioDetenzione() {
        return dtInizioDetenzione;
    }

    /**
     * Imposta il valore della proprietà dtInizioDetenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtInizioDetenzione(XMLGregorianCalendar value) {
        this.dtInizioDetenzione = value;
    }

    /**
     * Recupera il valore della proprietà dtFineDetenzione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtFineDetenzione() {
        return dtFineDetenzione;
    }

    /**
     * Imposta il valore della proprietà dtFineDetenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtFineDetenzione(XMLGregorianCalendar value) {
        this.dtFineDetenzione = value;
    }

    /**
     * Recupera il valore della proprietà capoCodMadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapoCodMadre() {
        return capoCodMadre;
    }

    /**
     * Imposta il valore della proprietà capoCodMadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapoCodMadre(String value) {
        this.capoCodMadre = value;
    }

    /**
     * Recupera il valore della proprietà codLibroMadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodLibroMadre() {
        return codLibroMadre;
    }

    /**
     * Imposta il valore della proprietà codLibroMadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodLibroMadre(String value) {
        this.codLibroMadre = value;
    }

    /**
     * Recupera il valore della proprietà descLibroMadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescLibroMadre() {
        return descLibroMadre;
    }

    /**
     * Imposta il valore della proprietà descLibroMadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescLibroMadre(String value) {
        this.descLibroMadre = value;
    }

    /**
     * Recupera il valore della proprietà aziendaCodice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAziendaCodice() {
        return aziendaCodice;
    }

    /**
     * Imposta il valore della proprietà aziendaCodice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAziendaCodice(String value) {
        this.aziendaCodice = value;
    }

    /**
     * Recupera il valore della proprietà allevId.
     * 
     */
    public long getAllevId() {
        return allevId;
    }

    /**
     * Imposta il valore della proprietà allevId.
     * 
     */
    public void setAllevId(long value) {
        this.allevId = value;
    }

    /**
     * Recupera il valore della proprietà dtMacellazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtMacellazione() {
        return dtMacellazione;
    }

    /**
     * Imposta il valore della proprietà dtMacellazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtMacellazione(XMLGregorianCalendar value) {
        this.dtMacellazione = value;
    }

}
