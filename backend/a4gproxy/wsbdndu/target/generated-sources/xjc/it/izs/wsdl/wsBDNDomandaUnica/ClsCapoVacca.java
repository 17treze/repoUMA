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
 * <p>Classe Java per clsCapoVacca complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="clsCapoVacca"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Capo_Id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Cod_Libro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Descr_libro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Dt_Nascita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Nascita_Vitello" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Razza_Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Codice_Vitello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Dt_Inizio_Detenzione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Fine_Detenzione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Azienda_Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Allev_Id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Azienda_Longitudine" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="Azienda_Latitudine" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="Flag_IBR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Flag_Rispetto_Prevalenza_IBR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Sesso_Vitello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Vacca_Dt_Ingresso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Vacca_Dt_Com_Autorita_Ingresso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Vacca_Dt_Inserimento_Bdn_Ingresso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Vitello_Dt_Appl_Marchio" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Vitello_Dt_Com_Autorita_Nascita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Vitello_Dt_Inserimento_Bdn_Nascita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Flag_Proroga_Marcatura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Vitello_Capo_Id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Vitello_Tipo_Origine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Vitello_Dt_Com_Autorita_Ingresso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Vitello_Dt_Inserimento_Bdn_Ingresso" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Flag_Delegato_Ingresso_Vacca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Ruolo_Utente_Ingresso_Vacca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Flag_Delegato_Nascita_Vitello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Ruolo_Utente_Nascita_Vitello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Flag_Delegato_Ingresso_Vitello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Ruolo_Utente_Ingresso_Vitello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsCapoVacca", propOrder = {
    "capoId",
    "codice",
    "codLibro",
    "descrLibro",
    "dtNascita",
    "dtNascitaVitello",
    "razzaCodice",
    "codiceVitello",
    "dtInizioDetenzione",
    "dtFineDetenzione",
    "aziendaCodice",
    "allevId",
    "aziendaLongitudine",
    "aziendaLatitudine",
    "flagIBR",
    "flagRispettoPrevalenzaIBR",
    "sessoVitello",
    "vaccaDtIngresso",
    "vaccaDtComAutoritaIngresso",
    "vaccaDtInserimentoBdnIngresso",
    "vitelloDtApplMarchio",
    "vitelloDtComAutoritaNascita",
    "vitelloDtInserimentoBdnNascita",
    "flagProrogaMarcatura",
    "cuaa",
    "sesso",
    "vitelloCapoId",
    "vitelloTipoOrigine",
    "vitelloDtComAutoritaIngresso",
    "vitelloDtInserimentoBdnIngresso",
    "flagDelegatoIngressoVacca",
    "ruoloUtenteIngressoVacca",
    "flagDelegatoNascitaVitello",
    "ruoloUtenteNascitaVitello",
    "flagDelegatoIngressoVitello",
    "ruoloUtenteIngressoVitello"
})
public class ClsCapoVacca {

    @XmlElement(name = "Capo_Id")
    protected long capoId;
    @XmlElement(name = "Codice")
    protected String codice;
    @XmlElement(name = "Cod_Libro")
    protected String codLibro;
    @XmlElement(name = "Descr_libro")
    protected String descrLibro;
    @XmlElement(name = "Dt_Nascita", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtNascita;
    @XmlElement(name = "Dt_Nascita_Vitello", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtNascitaVitello;
    @XmlElement(name = "Razza_Codice")
    protected String razzaCodice;
    @XmlElement(name = "Codice_Vitello")
    protected String codiceVitello;
    @XmlElement(name = "Dt_Inizio_Detenzione", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtInizioDetenzione;
    @XmlElement(name = "Dt_Fine_Detenzione", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtFineDetenzione;
    @XmlElement(name = "Azienda_Codice")
    protected String aziendaCodice;
    @XmlElement(name = "Allev_Id")
    protected long allevId;
    @XmlElement(name = "Azienda_Longitudine", required = true, type = Double.class, nillable = true)
    protected Double aziendaLongitudine;
    @XmlElement(name = "Azienda_Latitudine", required = true, type = Double.class, nillable = true)
    protected Double aziendaLatitudine;
    @XmlElement(name = "Flag_IBR")
    protected String flagIBR;
    @XmlElement(name = "Flag_Rispetto_Prevalenza_IBR")
    protected String flagRispettoPrevalenzaIBR;
    @XmlElement(name = "Sesso_Vitello")
    protected String sessoVitello;
    @XmlElement(name = "Vacca_Dt_Ingresso", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar vaccaDtIngresso;
    @XmlElement(name = "Vacca_Dt_Com_Autorita_Ingresso", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar vaccaDtComAutoritaIngresso;
    @XmlElement(name = "Vacca_Dt_Inserimento_Bdn_Ingresso", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar vaccaDtInserimentoBdnIngresso;
    @XmlElement(name = "Vitello_Dt_Appl_Marchio", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar vitelloDtApplMarchio;
    @XmlElement(name = "Vitello_Dt_Com_Autorita_Nascita", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar vitelloDtComAutoritaNascita;
    @XmlElement(name = "Vitello_Dt_Inserimento_Bdn_Nascita", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar vitelloDtInserimentoBdnNascita;
    @XmlElement(name = "Flag_Proroga_Marcatura")
    protected String flagProrogaMarcatura;
    @XmlElement(name = "Cuaa")
    protected String cuaa;
    @XmlElement(name = "Sesso")
    protected String sesso;
    @XmlElement(name = "Vitello_Capo_Id")
    protected long vitelloCapoId;
    @XmlElement(name = "Vitello_Tipo_Origine")
    protected String vitelloTipoOrigine;
    @XmlElement(name = "Vitello_Dt_Com_Autorita_Ingresso", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar vitelloDtComAutoritaIngresso;
    @XmlElement(name = "Vitello_Dt_Inserimento_Bdn_Ingresso", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar vitelloDtInserimentoBdnIngresso;
    @XmlElement(name = "Flag_Delegato_Ingresso_Vacca")
    protected String flagDelegatoIngressoVacca;
    @XmlElement(name = "Ruolo_Utente_Ingresso_Vacca")
    protected String ruoloUtenteIngressoVacca;
    @XmlElement(name = "Flag_Delegato_Nascita_Vitello")
    protected String flagDelegatoNascitaVitello;
    @XmlElement(name = "Ruolo_Utente_Nascita_Vitello")
    protected String ruoloUtenteNascitaVitello;
    @XmlElement(name = "Flag_Delegato_Ingresso_Vitello")
    protected String flagDelegatoIngressoVitello;
    @XmlElement(name = "Ruolo_Utente_Ingresso_Vitello")
    protected String ruoloUtenteIngressoVitello;

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
     * Recupera il valore della proprietà dtNascitaVitello.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtNascitaVitello() {
        return dtNascitaVitello;
    }

    /**
     * Imposta il valore della proprietà dtNascitaVitello.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtNascitaVitello(XMLGregorianCalendar value) {
        this.dtNascitaVitello = value;
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
     * Recupera il valore della proprietà codiceVitello.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceVitello() {
        return codiceVitello;
    }

    /**
     * Imposta il valore della proprietà codiceVitello.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceVitello(String value) {
        this.codiceVitello = value;
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
     * Recupera il valore della proprietà flagIBR.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagIBR() {
        return flagIBR;
    }

    /**
     * Imposta il valore della proprietà flagIBR.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagIBR(String value) {
        this.flagIBR = value;
    }

    /**
     * Recupera il valore della proprietà flagRispettoPrevalenzaIBR.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagRispettoPrevalenzaIBR() {
        return flagRispettoPrevalenzaIBR;
    }

    /**
     * Imposta il valore della proprietà flagRispettoPrevalenzaIBR.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagRispettoPrevalenzaIBR(String value) {
        this.flagRispettoPrevalenzaIBR = value;
    }

    /**
     * Recupera il valore della proprietà sessoVitello.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessoVitello() {
        return sessoVitello;
    }

    /**
     * Imposta il valore della proprietà sessoVitello.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessoVitello(String value) {
        this.sessoVitello = value;
    }

    /**
     * Recupera il valore della proprietà vaccaDtIngresso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVaccaDtIngresso() {
        return vaccaDtIngresso;
    }

    /**
     * Imposta il valore della proprietà vaccaDtIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVaccaDtIngresso(XMLGregorianCalendar value) {
        this.vaccaDtIngresso = value;
    }

    /**
     * Recupera il valore della proprietà vaccaDtComAutoritaIngresso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVaccaDtComAutoritaIngresso() {
        return vaccaDtComAutoritaIngresso;
    }

    /**
     * Imposta il valore della proprietà vaccaDtComAutoritaIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVaccaDtComAutoritaIngresso(XMLGregorianCalendar value) {
        this.vaccaDtComAutoritaIngresso = value;
    }

    /**
     * Recupera il valore della proprietà vaccaDtInserimentoBdnIngresso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVaccaDtInserimentoBdnIngresso() {
        return vaccaDtInserimentoBdnIngresso;
    }

    /**
     * Imposta il valore della proprietà vaccaDtInserimentoBdnIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVaccaDtInserimentoBdnIngresso(XMLGregorianCalendar value) {
        this.vaccaDtInserimentoBdnIngresso = value;
    }

    /**
     * Recupera il valore della proprietà vitelloDtApplMarchio.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVitelloDtApplMarchio() {
        return vitelloDtApplMarchio;
    }

    /**
     * Imposta il valore della proprietà vitelloDtApplMarchio.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVitelloDtApplMarchio(XMLGregorianCalendar value) {
        this.vitelloDtApplMarchio = value;
    }

    /**
     * Recupera il valore della proprietà vitelloDtComAutoritaNascita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVitelloDtComAutoritaNascita() {
        return vitelloDtComAutoritaNascita;
    }

    /**
     * Imposta il valore della proprietà vitelloDtComAutoritaNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVitelloDtComAutoritaNascita(XMLGregorianCalendar value) {
        this.vitelloDtComAutoritaNascita = value;
    }

    /**
     * Recupera il valore della proprietà vitelloDtInserimentoBdnNascita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVitelloDtInserimentoBdnNascita() {
        return vitelloDtInserimentoBdnNascita;
    }

    /**
     * Imposta il valore della proprietà vitelloDtInserimentoBdnNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVitelloDtInserimentoBdnNascita(XMLGregorianCalendar value) {
        this.vitelloDtInserimentoBdnNascita = value;
    }

    /**
     * Recupera il valore della proprietà flagProrogaMarcatura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagProrogaMarcatura() {
        return flagProrogaMarcatura;
    }

    /**
     * Imposta il valore della proprietà flagProrogaMarcatura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagProrogaMarcatura(String value) {
        this.flagProrogaMarcatura = value;
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
     * Recupera il valore della proprietà vitelloCapoId.
     * 
     */
    public long getVitelloCapoId() {
        return vitelloCapoId;
    }

    /**
     * Imposta il valore della proprietà vitelloCapoId.
     * 
     */
    public void setVitelloCapoId(long value) {
        this.vitelloCapoId = value;
    }

    /**
     * Recupera il valore della proprietà vitelloTipoOrigine.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVitelloTipoOrigine() {
        return vitelloTipoOrigine;
    }

    /**
     * Imposta il valore della proprietà vitelloTipoOrigine.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVitelloTipoOrigine(String value) {
        this.vitelloTipoOrigine = value;
    }

    /**
     * Recupera il valore della proprietà vitelloDtComAutoritaIngresso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVitelloDtComAutoritaIngresso() {
        return vitelloDtComAutoritaIngresso;
    }

    /**
     * Imposta il valore della proprietà vitelloDtComAutoritaIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVitelloDtComAutoritaIngresso(XMLGregorianCalendar value) {
        this.vitelloDtComAutoritaIngresso = value;
    }

    /**
     * Recupera il valore della proprietà vitelloDtInserimentoBdnIngresso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVitelloDtInserimentoBdnIngresso() {
        return vitelloDtInserimentoBdnIngresso;
    }

    /**
     * Imposta il valore della proprietà vitelloDtInserimentoBdnIngresso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVitelloDtInserimentoBdnIngresso(XMLGregorianCalendar value) {
        this.vitelloDtInserimentoBdnIngresso = value;
    }

    /**
     * Recupera il valore della proprietà flagDelegatoIngressoVacca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagDelegatoIngressoVacca() {
        return flagDelegatoIngressoVacca;
    }

    /**
     * Imposta il valore della proprietà flagDelegatoIngressoVacca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagDelegatoIngressoVacca(String value) {
        this.flagDelegatoIngressoVacca = value;
    }

    /**
     * Recupera il valore della proprietà ruoloUtenteIngressoVacca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuoloUtenteIngressoVacca() {
        return ruoloUtenteIngressoVacca;
    }

    /**
     * Imposta il valore della proprietà ruoloUtenteIngressoVacca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuoloUtenteIngressoVacca(String value) {
        this.ruoloUtenteIngressoVacca = value;
    }

    /**
     * Recupera il valore della proprietà flagDelegatoNascitaVitello.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagDelegatoNascitaVitello() {
        return flagDelegatoNascitaVitello;
    }

    /**
     * Imposta il valore della proprietà flagDelegatoNascitaVitello.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagDelegatoNascitaVitello(String value) {
        this.flagDelegatoNascitaVitello = value;
    }

    /**
     * Recupera il valore della proprietà ruoloUtenteNascitaVitello.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuoloUtenteNascitaVitello() {
        return ruoloUtenteNascitaVitello;
    }

    /**
     * Imposta il valore della proprietà ruoloUtenteNascitaVitello.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuoloUtenteNascitaVitello(String value) {
        this.ruoloUtenteNascitaVitello = value;
    }

    /**
     * Recupera il valore della proprietà flagDelegatoIngressoVitello.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagDelegatoIngressoVitello() {
        return flagDelegatoIngressoVitello;
    }

    /**
     * Imposta il valore della proprietà flagDelegatoIngressoVitello.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagDelegatoIngressoVitello(String value) {
        this.flagDelegatoIngressoVitello = value;
    }

    /**
     * Recupera il valore della proprietà ruoloUtenteIngressoVitello.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuoloUtenteIngressoVitello() {
        return ruoloUtenteIngressoVitello;
    }

    /**
     * Imposta il valore della proprietà ruoloUtenteIngressoVitello.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuoloUtenteIngressoVitello(String value) {
        this.ruoloUtenteIngressoVitello = value;
    }

}
