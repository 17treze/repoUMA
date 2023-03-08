//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:04 PM CEST 
//


package it.izs.wsdl.wsBDNAgea;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per ArrayOfRootDatiSUI_TEMPI_NOTIFICA_NASCITE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiSUI_TEMPI_NOTIFICA_NASCITE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SUI_TEMPI_NOTIFICA_NASCITE" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_APPLICAZIONE_MARCHIO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_COM_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_REGISTRAZ_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="GG_RITARDO_APPL_MARCHIO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="GG_RITARDO_REGISTRAZ_NASCITA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_DELEGATO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RUOLO_UTENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRootDatiSUI_TEMPI_NOTIFICA_NASCITE", propOrder = {
    "suitempinotificanascite"
})
public class ArrayOfRootDatiSUITEMPINOTIFICANASCITE {

    @XmlElement(name = "SUI_TEMPI_NOTIFICA_NASCITE")
    protected List<ArrayOfRootDatiSUITEMPINOTIFICANASCITE.SUITEMPINOTIFICANASCITE> suitempinotificanascite;

    /**
     * Gets the value of the suitempinotificanascite property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the suitempinotificanascite property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSUITEMPINOTIFICANASCITE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiSUITEMPINOTIFICANASCITE.SUITEMPINOTIFICANASCITE }
     * 
     * 
     */
    public List<ArrayOfRootDatiSUITEMPINOTIFICANASCITE.SUITEMPINOTIFICANASCITE> getSUITEMPINOTIFICANASCITE() {
        if (suitempinotificanascite == null) {
            suitempinotificanascite = new ArrayList<ArrayOfRootDatiSUITEMPINOTIFICANASCITE.SUITEMPINOTIFICANASCITE>();
        }
        return this.suitempinotificanascite;
    }


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
     *         &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_APPLICAZIONE_MARCHIO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_COM_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_REGISTRAZ_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="GG_RITARDO_APPL_MARCHIO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="GG_RITARDO_REGISTRAZ_NASCITA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_DELEGATO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RUOLO_UTENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "cuaa",
        "dtinizioresponsabilita",
        "dtfineresponsabilita",
        "dtinizioperiodo",
        "dtfineperiodo",
        "aziendacodice",
        "idfiscale",
        "specodice",
        "allevid",
        "capoid",
        "codice",
        "dtnascita",
        "dtapplicazionemarchio",
        "dtcomnascita",
        "dtregistraznascita",
        "ggritardoapplmarchio",
        "ggritardoregistraznascita",
        "flagdelegato",
        "ruoloutente"
    })
    public static class SUITEMPINOTIFICANASCITE {

        @XmlElement(name = "CUAA")
        protected String cuaa;
        @XmlElement(name = "DT_INIZIO_RESPONSABILITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtinizioresponsabilita;
        @XmlElement(name = "DT_FINE_RESPONSABILITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtfineresponsabilita;
        @XmlElement(name = "DT_INIZIO_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtinizioperiodo;
        @XmlElement(name = "DT_FINE_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtfineperiodo;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "ID_FISCALE")
        protected String idfiscale;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "ALLEV_ID")
        protected BigDecimal allevid;
        @XmlElement(name = "CAPO_ID")
        protected BigDecimal capoid;
        @XmlElement(name = "CODICE")
        protected String codice;
        @XmlElement(name = "DT_NASCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtnascita;
        @XmlElement(name = "DT_APPLICAZIONE_MARCHIO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtapplicazionemarchio;
        @XmlElement(name = "DT_COM_NASCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtcomnascita;
        @XmlElement(name = "DT_REGISTRAZ_NASCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtregistraznascita;
        @XmlElement(name = "GG_RITARDO_APPL_MARCHIO")
        protected BigDecimal ggritardoapplmarchio;
        @XmlElement(name = "GG_RITARDO_REGISTRAZ_NASCITA")
        protected BigDecimal ggritardoregistraznascita;
        @XmlElement(name = "FLAG_DELEGATO")
        protected String flagdelegato;
        @XmlElement(name = "RUOLO_UTENTE")
        protected String ruoloutente;

        /**
         * Recupera il valore della proprietà cuaa.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCUAA() {
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
        public void setCUAA(String value) {
            this.cuaa = value;
        }

        /**
         * Recupera il valore della proprietà dtinizioresponsabilita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINIZIORESPONSABILITA() {
            return dtinizioresponsabilita;
        }

        /**
         * Imposta il valore della proprietà dtinizioresponsabilita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINIZIORESPONSABILITA(XMLGregorianCalendar value) {
            this.dtinizioresponsabilita = value;
        }

        /**
         * Recupera il valore della proprietà dtfineresponsabilita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTFINERESPONSABILITA() {
            return dtfineresponsabilita;
        }

        /**
         * Imposta il valore della proprietà dtfineresponsabilita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTFINERESPONSABILITA(XMLGregorianCalendar value) {
            this.dtfineresponsabilita = value;
        }

        /**
         * Recupera il valore della proprietà dtinizioperiodo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINIZIOPERIODO() {
            return dtinizioperiodo;
        }

        /**
         * Imposta il valore della proprietà dtinizioperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINIZIOPERIODO(XMLGregorianCalendar value) {
            this.dtinizioperiodo = value;
        }

        /**
         * Recupera il valore della proprietà dtfineperiodo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTFINEPERIODO() {
            return dtfineperiodo;
        }

        /**
         * Imposta il valore della proprietà dtfineperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTFINEPERIODO(XMLGregorianCalendar value) {
            this.dtfineperiodo = value;
        }

        /**
         * Recupera il valore della proprietà aziendacodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAZIENDACODICE() {
            return aziendacodice;
        }

        /**
         * Imposta il valore della proprietà aziendacodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAZIENDACODICE(String value) {
            this.aziendacodice = value;
        }

        /**
         * Recupera il valore della proprietà idfiscale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIDFISCALE() {
            return idfiscale;
        }

        /**
         * Imposta il valore della proprietà idfiscale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIDFISCALE(String value) {
            this.idfiscale = value;
        }

        /**
         * Recupera il valore della proprietà specodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECODICE() {
            return specodice;
        }

        /**
         * Imposta il valore della proprietà specodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECODICE(String value) {
            this.specodice = value;
        }

        /**
         * Recupera il valore della proprietà allevid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getALLEVID() {
            return allevid;
        }

        /**
         * Imposta il valore della proprietà allevid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setALLEVID(BigDecimal value) {
            this.allevid = value;
        }

        /**
         * Recupera il valore della proprietà capoid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPOID() {
            return capoid;
        }

        /**
         * Imposta il valore della proprietà capoid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPOID(BigDecimal value) {
            this.capoid = value;
        }

        /**
         * Recupera il valore della proprietà codice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICE() {
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
        public void setCODICE(String value) {
            this.codice = value;
        }

        /**
         * Recupera il valore della proprietà dtnascita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTNASCITA() {
            return dtnascita;
        }

        /**
         * Imposta il valore della proprietà dtnascita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTNASCITA(XMLGregorianCalendar value) {
            this.dtnascita = value;
        }

        /**
         * Recupera il valore della proprietà dtapplicazionemarchio.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTAPPLICAZIONEMARCHIO() {
            return dtapplicazionemarchio;
        }

        /**
         * Imposta il valore della proprietà dtapplicazionemarchio.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTAPPLICAZIONEMARCHIO(XMLGregorianCalendar value) {
            this.dtapplicazionemarchio = value;
        }

        /**
         * Recupera il valore della proprietà dtcomnascita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTCOMNASCITA() {
            return dtcomnascita;
        }

        /**
         * Imposta il valore della proprietà dtcomnascita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTCOMNASCITA(XMLGregorianCalendar value) {
            this.dtcomnascita = value;
        }

        /**
         * Recupera il valore della proprietà dtregistraznascita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTREGISTRAZNASCITA() {
            return dtregistraznascita;
        }

        /**
         * Imposta il valore della proprietà dtregistraznascita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTREGISTRAZNASCITA(XMLGregorianCalendar value) {
            this.dtregistraznascita = value;
        }

        /**
         * Recupera il valore della proprietà ggritardoapplmarchio.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getGGRITARDOAPPLMARCHIO() {
            return ggritardoapplmarchio;
        }

        /**
         * Imposta il valore della proprietà ggritardoapplmarchio.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setGGRITARDOAPPLMARCHIO(BigDecimal value) {
            this.ggritardoapplmarchio = value;
        }

        /**
         * Recupera il valore della proprietà ggritardoregistraznascita.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getGGRITARDOREGISTRAZNASCITA() {
            return ggritardoregistraznascita;
        }

        /**
         * Imposta il valore della proprietà ggritardoregistraznascita.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setGGRITARDOREGISTRAZNASCITA(BigDecimal value) {
            this.ggritardoregistraznascita = value;
        }

        /**
         * Recupera il valore della proprietà flagdelegato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGDELEGATO() {
            return flagdelegato;
        }

        /**
         * Imposta il valore della proprietà flagdelegato.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGDELEGATO(String value) {
            this.flagdelegato = value;
        }

        /**
         * Recupera il valore della proprietà ruoloutente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRUOLOUTENTE() {
            return ruoloutente;
        }

        /**
         * Imposta il valore della proprietà ruoloutente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRUOLOUTENTE(String value) {
            this.ruoloutente = value;
        }

    }

}
