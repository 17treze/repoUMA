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
 * <p>Classe Java per clsCapoOvicaprino complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="clsCapoOvicaprino"&gt;
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
 *         &lt;element name="Azienda_Codice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Allev_Id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Genotipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Cons_Media" type="{http://www.w3.org/2001/XMLSchema}float"/&gt;
 *         &lt;element name="Dt_Macellazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Com_Macellazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Registr_Macellazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Ultimo_Prel_Profilassi" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Com_Nascita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Inserimento_Bdn_Nascita" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Dt_Appl_Marchio" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Flag_Delegato_Nascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Ruolo_Utente_Nascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsCapoOvicaprino", propOrder = {
    "capoId",
    "codice",
    "sesso",
    "dtNascita",
    "razzaCodice",
    "dtInizioDetenzione",
    "dtFineDetenzione",
    "aziendaCodice",
    "allevId",
    "genotipo",
    "consMedia",
    "dtMacellazione",
    "dtComMacellazione",
    "dtRegistrMacellazione",
    "dtUltimoPrelProfilassi",
    "dtComNascita",
    "dtInserimentoBdnNascita",
    "dtApplMarchio",
    "cuaa",
    "flagDelegatoNascita",
    "ruoloUtenteNascita"
})
public class ClsCapoOvicaprino {

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
    @XmlElement(name = "Genotipo")
    protected String genotipo;
    @XmlElement(name = "Cons_Media")
    protected float consMedia;
    @XmlElement(name = "Dt_Macellazione", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtMacellazione;
    @XmlElement(name = "Dt_Com_Macellazione", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtComMacellazione;
    @XmlElement(name = "Dt_Registr_Macellazione", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtRegistrMacellazione;
    @XmlElement(name = "Dt_Ultimo_Prel_Profilassi", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtUltimoPrelProfilassi;
    @XmlElement(name = "Dt_Com_Nascita", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtComNascita;
    @XmlElement(name = "Dt_Inserimento_Bdn_Nascita", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtInserimentoBdnNascita;
    @XmlElement(name = "Dt_Appl_Marchio", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtApplMarchio;
    @XmlElement(name = "Cuaa")
    protected String cuaa;
    @XmlElement(name = "Flag_Delegato_Nascita")
    protected String flagDelegatoNascita;
    @XmlElement(name = "Ruolo_Utente_Nascita")
    protected String ruoloUtenteNascita;

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
     * Recupera il valore della proprietà genotipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenotipo() {
        return genotipo;
    }

    /**
     * Imposta il valore della proprietà genotipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenotipo(String value) {
        this.genotipo = value;
    }

    /**
     * Recupera il valore della proprietà consMedia.
     * 
     */
    public float getConsMedia() {
        return consMedia;
    }

    /**
     * Imposta il valore della proprietà consMedia.
     * 
     */
    public void setConsMedia(float value) {
        this.consMedia = value;
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
     * Recupera il valore della proprietà dtComMacellazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtComMacellazione() {
        return dtComMacellazione;
    }

    /**
     * Imposta il valore della proprietà dtComMacellazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtComMacellazione(XMLGregorianCalendar value) {
        this.dtComMacellazione = value;
    }

    /**
     * Recupera il valore della proprietà dtRegistrMacellazione.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtRegistrMacellazione() {
        return dtRegistrMacellazione;
    }

    /**
     * Imposta il valore della proprietà dtRegistrMacellazione.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtRegistrMacellazione(XMLGregorianCalendar value) {
        this.dtRegistrMacellazione = value;
    }

    /**
     * Recupera il valore della proprietà dtUltimoPrelProfilassi.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtUltimoPrelProfilassi() {
        return dtUltimoPrelProfilassi;
    }

    /**
     * Imposta il valore della proprietà dtUltimoPrelProfilassi.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtUltimoPrelProfilassi(XMLGregorianCalendar value) {
        this.dtUltimoPrelProfilassi = value;
    }

    /**
     * Recupera il valore della proprietà dtComNascita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtComNascita() {
        return dtComNascita;
    }

    /**
     * Imposta il valore della proprietà dtComNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtComNascita(XMLGregorianCalendar value) {
        this.dtComNascita = value;
    }

    /**
     * Recupera il valore della proprietà dtInserimentoBdnNascita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtInserimentoBdnNascita() {
        return dtInserimentoBdnNascita;
    }

    /**
     * Imposta il valore della proprietà dtInserimentoBdnNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtInserimentoBdnNascita(XMLGregorianCalendar value) {
        this.dtInserimentoBdnNascita = value;
    }

    /**
     * Recupera il valore della proprietà dtApplMarchio.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtApplMarchio() {
        return dtApplMarchio;
    }

    /**
     * Imposta il valore della proprietà dtApplMarchio.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtApplMarchio(XMLGregorianCalendar value) {
        this.dtApplMarchio = value;
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
     * Recupera il valore della proprietà flagDelegatoNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagDelegatoNascita() {
        return flagDelegatoNascita;
    }

    /**
     * Imposta il valore della proprietà flagDelegatoNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagDelegatoNascita(String value) {
        this.flagDelegatoNascita = value;
    }

    /**
     * Recupera il valore della proprietà ruoloUtenteNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuoloUtenteNascita() {
        return ruoloUtenteNascita;
    }

    /**
     * Imposta il valore della proprietà ruoloUtenteNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuoloUtenteNascita(String value) {
        this.ruoloUtenteNascita = value;
    }

}
