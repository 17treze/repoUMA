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
 * <p>Classe Java per clsCapoMacellato complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="clsCapoMacellato"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Azienda_Latitudine" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="Azienda_Longitudine" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="Dt_Ingresso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Macellazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Cod_Libro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Descr_Libro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Dt_Com_Autorita_Ingresso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Inserimento_Bdn_Ingresso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Dt_Com_Autorita_Uscita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Uscita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Inserimento_Bdn_Uscita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Capo_Id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Allev_Id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Dt_Nascita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Razza_Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Dt_Inizio_Detenzione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Fine_Detenzione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Azienda_Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Flag_Delegato_Ingresso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Ruolo_Utente_Ingresso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Flag_Delegato_Uscita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Ruolo_Utente_Uscita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsCapoMacellato", propOrder = {
    "aziendaLatitudine",
    "aziendaLongitudine",
    "dtIngresso",
    "dtMacellazione",
    "codLibro",
    "descrLibro",
    "dtComAutoritaIngresso",
    "dtInserimentoBdnIngresso",
    "cuaa",
    "dtComAutoritaUscita",
    "dtUscita",
    "dtInserimentoBdnUscita",
    "capoId",
    "allevId",
    "codice",
    "sesso",
    "dtNascita",
    "razzaCodice",
    "dtInizioDetenzione",
    "dtFineDetenzione",
    "aziendaCodice",
    "flagDelegatoIngresso",
    "ruoloUtenteIngresso",
    "flagDelegatoUscita",
    "ruoloUtenteUscita"
})
public class ClsCapoMacellato {

    @XmlElement(name = "Azienda_Latitudine", required = true, type = Double.class, nillable = true)
    protected Double aziendaLatitudine;
    @XmlElement(name = "Azienda_Longitudine", required = true, type = Double.class, nillable = true)
    protected Double aziendaLongitudine;
    @XmlElement(name = "Dt_Ingresso", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtIngresso;
    @XmlElement(name = "Dt_Macellazione", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtMacellazione;
    @XmlElement(name = "Cod_Libro")
    protected String codLibro;
    @XmlElement(name = "Descr_Libro")
    protected String descrLibro;
    @XmlElement(name = "Dt_Com_Autorita_Ingresso", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtComAutoritaIngresso;
    @XmlElement(name = "Dt_Inserimento_Bdn_Ingresso", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtInserimentoBdnIngresso;
    @XmlElement(name = "Cuaa")
    protected String cuaa;
    @XmlElement(name = "Dt_Com_Autorita_Uscita", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtComAutoritaUscita;
    @XmlElement(name = "Dt_Uscita", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtUscita;
    @XmlElement(name = "Dt_Inserimento_Bdn_Uscita", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtInserimentoBdnUscita;
    @XmlElement(name = "Capo_Id")
    protected long capoId;
    @XmlElement(name = "Allev_Id")
    protected long allevId;
    @XmlElement(name = "Codice")
    protected String codice;
    @XmlElement(name = "Sesso")
    protected String sesso;
    @XmlElement(name = "Dt_Nascita", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtNascita;
    @XmlElement(name = "Razza_Codice")
    protected String razzaCodice;
    @XmlElement(name = "Dt_Inizio_Detenzione", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtInizioDetenzione;
    @XmlElement(name = "Dt_Fine_Detenzione", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtFineDetenzione;
    @XmlElement(name = "Azienda_Codice")
    protected String aziendaCodice;
    @XmlElement(name = "Flag_Delegato_Ingresso")
    protected String flagDelegatoIngresso;
    @XmlElement(name = "Ruolo_Utente_Ingresso")
    protected String ruoloUtenteIngresso;
    @XmlElement(name = "Flag_Delegato_Uscita")
    protected String flagDelegatoUscita;
    @XmlElement(name = "Ruolo_Utente_Uscita")
    protected String ruoloUtenteUscita;

    /**
     * Recupera il valore della proprietà aziendaLatitudine.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAziendaLatitudine() {
        return aziendaLatitudine;
    }

    /**
     * Imposta il valore della proprietà aziendaLatitudine.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAziendaLatitudine(Double value) {
        this.aziendaLatitudine = value;
    }

    /**
     * Recupera il valore della proprietà aziendaLongitudine.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAziendaLongitudine() {
        return aziendaLongitudine;
    }

    /**
     * Imposta il valore della proprietà aziendaLongitudine.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAziendaLongitudine(Double value) {
        this.aziendaLongitudine = value;
    }

    /**
     * Recupera il valore della proprietà dtIngresso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtIngresso() {
        return dtIngresso;
    }

    /**
     * Imposta il valore della proprietà dtIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtIngresso(XMLGregorianCalendar value) {
        this.dtIngresso = value;
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

    /**
     * Recupera il valore della proprietà codLibro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodLibro() {
        return codLibro;
    }

    /**
     * Imposta il valore della proprietà codLibro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodLibro(String value) {
        this.codLibro = value;
    }

    /**
     * Recupera il valore della proprietà descrLibro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrLibro() {
        return descrLibro;
    }

    /**
     * Imposta il valore della proprietà descrLibro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrLibro(String value) {
        this.descrLibro = value;
    }

    /**
     * Recupera il valore della proprietà dtComAutoritaIngresso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtComAutoritaIngresso() {
        return dtComAutoritaIngresso;
    }

    /**
     * Imposta il valore della proprietà dtComAutoritaIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtComAutoritaIngresso(XMLGregorianCalendar value) {
        this.dtComAutoritaIngresso = value;
    }

    /**
     * Recupera il valore della proprietà dtInserimentoBdnIngresso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtInserimentoBdnIngresso() {
        return dtInserimentoBdnIngresso;
    }

    /**
     * Imposta il valore della proprietà dtInserimentoBdnIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtInserimentoBdnIngresso(XMLGregorianCalendar value) {
        this.dtInserimentoBdnIngresso = value;
    }

    /**
     * Recupera il valore della proprietà cuaa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuaa() {
        return cuaa;
    }

    /**
     * Imposta il valore della proprietà cuaa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuaa(String value) {
        this.cuaa = value;
    }

    /**
     * Recupera il valore della proprietà dtComAutoritaUscita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtComAutoritaUscita() {
        return dtComAutoritaUscita;
    }

    /**
     * Imposta il valore della proprietà dtComAutoritaUscita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtComAutoritaUscita(XMLGregorianCalendar value) {
        this.dtComAutoritaUscita = value;
    }

    /**
     * Recupera il valore della proprietà dtUscita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtUscita() {
        return dtUscita;
    }

    /**
     * Imposta il valore della proprietà dtUscita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtUscita(XMLGregorianCalendar value) {
        this.dtUscita = value;
    }

    /**
     * Recupera il valore della proprietà dtInserimentoBdnUscita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtInserimentoBdnUscita() {
        return dtInserimentoBdnUscita;
    }

    /**
     * Imposta il valore della proprietà dtInserimentoBdnUscita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtInserimentoBdnUscita(XMLGregorianCalendar value) {
        this.dtInserimentoBdnUscita = value;
    }

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
     * Recupera il valore della proprietà flagDelegatoIngresso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagDelegatoIngresso() {
        return flagDelegatoIngresso;
    }

    /**
     * Imposta il valore della proprietà flagDelegatoIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagDelegatoIngresso(String value) {
        this.flagDelegatoIngresso = value;
    }

    /**
     * Recupera il valore della proprietà ruoloUtenteIngresso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuoloUtenteIngresso() {
        return ruoloUtenteIngresso;
    }

    /**
     * Imposta il valore della proprietà ruoloUtenteIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuoloUtenteIngresso(String value) {
        this.ruoloUtenteIngresso = value;
    }

    /**
     * Recupera il valore della proprietà flagDelegatoUscita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagDelegatoUscita() {
        return flagDelegatoUscita;
    }

    /**
     * Imposta il valore della proprietà flagDelegatoUscita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagDelegatoUscita(String value) {
        this.flagDelegatoUscita = value;
    }

    /**
     * Recupera il valore della proprietà ruoloUtenteUscita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuoloUtenteUscita() {
        return ruoloUtenteUscita;
    }

    /**
     * Imposta il valore della proprietà ruoloUtenteUscita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuoloUtenteUscita(String value) {
        this.ruoloUtenteUscita = value;
    }

}
