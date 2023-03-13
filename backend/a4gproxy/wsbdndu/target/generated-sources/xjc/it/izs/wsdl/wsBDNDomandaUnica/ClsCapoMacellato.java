//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.03.13 at 04:32:47 PM CET 
//


package it.izs.wsdl.wsBDNDomandaUnica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for clsCapoMacellato complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the aziendaLatitudine property.
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
     * Sets the value of the aziendaLatitudine property.
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
     * Gets the value of the aziendaLongitudine property.
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
     * Sets the value of the aziendaLongitudine property.
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
     * Gets the value of the dtIngresso property.
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
     * Sets the value of the dtIngresso property.
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
     * Gets the value of the dtMacellazione property.
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
     * Sets the value of the dtMacellazione property.
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
     * Gets the value of the codLibro property.
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
     * Sets the value of the codLibro property.
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
     * Gets the value of the descrLibro property.
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
     * Sets the value of the descrLibro property.
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
     * Gets the value of the dtComAutoritaIngresso property.
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
     * Sets the value of the dtComAutoritaIngresso property.
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
     * Gets the value of the dtInserimentoBdnIngresso property.
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
     * Sets the value of the dtInserimentoBdnIngresso property.
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
     * Gets the value of the cuaa property.
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
     * Sets the value of the cuaa property.
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
     * Gets the value of the dtComAutoritaUscita property.
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
     * Sets the value of the dtComAutoritaUscita property.
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
     * Gets the value of the dtUscita property.
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
     * Sets the value of the dtUscita property.
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
     * Gets the value of the dtInserimentoBdnUscita property.
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
     * Sets the value of the dtInserimentoBdnUscita property.
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
     * Gets the value of the capoId property.
     * 
     */
    public long getCapoId() {
        return capoId;
    }

    /**
     * Sets the value of the capoId property.
     * 
     */
    public void setCapoId(long value) {
        this.capoId = value;
    }

    /**
     * Gets the value of the allevId property.
     * 
     */
    public long getAllevId() {
        return allevId;
    }

    /**
     * Sets the value of the allevId property.
     * 
     */
    public void setAllevId(long value) {
        this.allevId = value;
    }

    /**
     * Gets the value of the codice property.
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
     * Sets the value of the codice property.
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
     * Gets the value of the sesso property.
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
     * Sets the value of the sesso property.
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
     * Gets the value of the dtNascita property.
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
     * Sets the value of the dtNascita property.
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
     * Gets the value of the razzaCodice property.
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
     * Sets the value of the razzaCodice property.
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
     * Gets the value of the dtInizioDetenzione property.
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
     * Sets the value of the dtInizioDetenzione property.
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
     * Gets the value of the dtFineDetenzione property.
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
     * Sets the value of the dtFineDetenzione property.
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
     * Gets the value of the aziendaCodice property.
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
     * Sets the value of the aziendaCodice property.
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
     * Gets the value of the flagDelegatoIngresso property.
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
     * Sets the value of the flagDelegatoIngresso property.
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
     * Gets the value of the ruoloUtenteIngresso property.
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
     * Sets the value of the ruoloUtenteIngresso property.
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
     * Gets the value of the flagDelegatoUscita property.
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
     * Sets the value of the flagDelegatoUscita property.
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
     * Gets the value of the ruoloUtenteUscita property.
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
     * Sets the value of the ruoloUtenteUscita property.
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
