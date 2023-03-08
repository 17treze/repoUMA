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
 * <p>Classe Java per ArrayOfRootDatiALLEVAMENTI complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiALLEVAMENTI"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ALLEVAMENTI" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COM_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_LIBRI_GENEALOGICI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AUTORIZZAZIONE_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_INIZIO_AUTORIZZAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_REVOCA_AUTORIZZAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_0_6_MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_6_24_MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_OLTRE_24_MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="VACCHE_DA_LATTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="BUFALINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="OVINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CAPRINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_ULTIMO_CENSIMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiALLEVAMENTI", propOrder = {
    "allevamenti"
})
public class ArrayOfRootDatiALLEVAMENTI {

    @XmlElement(name = "ALLEVAMENTI")
    protected List<ArrayOfRootDatiALLEVAMENTI.ALLEVAMENTI> allevamenti;

    /**
     * Gets the value of the allevamenti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the allevamenti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALLEVAMENTI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiALLEVAMENTI.ALLEVAMENTI }
     * 
     * 
     */
    public List<ArrayOfRootDatiALLEVAMENTI.ALLEVAMENTI> getALLEVAMENTI() {
        if (allevamenti == null) {
            allevamenti = new ArrayList<ArrayOfRootDatiALLEVAMENTI.ALLEVAMENTI>();
        }
        return this.allevamenti;
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
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COM_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_LIBRI_GENEALOGICI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AUTORIZZAZIONE_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_INIZIO_AUTORIZZAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DATA_REVOCA_AUTORIZZAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_0_6_MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_6_24_MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_OLTRE_24_MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="VACCHE_DA_LATTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="BUFALINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="OVINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CAPRINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DATA_ULTIMO_CENSIMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
        "allevid",
        "aziendacodice",
        "idfiscale",
        "specodice",
        "denominazione",
        "indirizzo",
        "cap",
        "localita",
        "comcodice",
        "flaglibrigenealogici",
        "latitudine",
        "longitudine",
        "sezione",
        "subalterno",
        "fogliocatastale",
        "particella",
        "autorizzazionelatte",
        "datainizioautorizzazione",
        "datarevocaautorizzazione",
        "capi06MESI",
        "capi624MESI",
        "capioltre24MESI",
        "vacchedalatte",
        "bufalini",
        "ovini",
        "caprini",
        "dataultimocensimento",
        "capitotali"
    })
    public static class ALLEVAMENTI {

        @XmlElement(name = "ALLEV_ID", required = true)
        protected BigDecimal allevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "ID_FISCALE")
        protected String idfiscale;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "DENOMINAZIONE")
        protected String denominazione;
        @XmlElement(name = "INDIRIZZO")
        protected String indirizzo;
        @XmlElement(name = "CAP")
        protected String cap;
        @XmlElement(name = "LOCALITA")
        protected String localita;
        @XmlElement(name = "COM_CODICE")
        protected String comcodice;
        @XmlElement(name = "FLAG_LIBRI_GENEALOGICI")
        protected String flaglibrigenealogici;
        @XmlElement(name = "LATITUDINE")
        protected BigDecimal latitudine;
        @XmlElement(name = "LONGITUDINE")
        protected BigDecimal longitudine;
        @XmlElement(name = "SEZIONE")
        protected String sezione;
        @XmlElement(name = "SUBALTERNO")
        protected String subalterno;
        @XmlElement(name = "FOGLIO_CATASTALE")
        protected String fogliocatastale;
        @XmlElement(name = "PARTICELLA")
        protected String particella;
        @XmlElement(name = "AUTORIZZAZIONE_LATTE")
        protected String autorizzazionelatte;
        @XmlElement(name = "DATA_INIZIO_AUTORIZZAZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datainizioautorizzazione;
        @XmlElement(name = "DATA_REVOCA_AUTORIZZAZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datarevocaautorizzazione;
        @XmlElement(name = "CAPI_0_6_MESI")
        protected BigDecimal capi06MESI;
        @XmlElement(name = "CAPI_6_24_MESI")
        protected BigDecimal capi624MESI;
        @XmlElement(name = "CAPI_OLTRE_24_MESI")
        protected BigDecimal capioltre24MESI;
        @XmlElement(name = "VACCHE_DA_LATTE")
        protected BigDecimal vacchedalatte;
        @XmlElement(name = "BUFALINI")
        protected BigDecimal bufalini;
        @XmlElement(name = "OVINI")
        protected BigDecimal ovini;
        @XmlElement(name = "CAPRINI")
        protected BigDecimal caprini;
        @XmlElement(name = "DATA_ULTIMO_CENSIMENTO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataultimocensimento;
        @XmlElement(name = "CAPI_TOTALI")
        protected BigDecimal capitotali;

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
         * Recupera il valore della proprietà denominazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDENOMINAZIONE() {
            return denominazione;
        }

        /**
         * Imposta il valore della proprietà denominazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDENOMINAZIONE(String value) {
            this.denominazione = value;
        }

        /**
         * Recupera il valore della proprietà indirizzo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getINDIRIZZO() {
            return indirizzo;
        }

        /**
         * Imposta il valore della proprietà indirizzo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setINDIRIZZO(String value) {
            this.indirizzo = value;
        }

        /**
         * Recupera il valore della proprietà cap.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAP() {
            return cap;
        }

        /**
         * Imposta il valore della proprietà cap.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAP(String value) {
            this.cap = value;
        }

        /**
         * Recupera il valore della proprietà localita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLOCALITA() {
            return localita;
        }

        /**
         * Imposta il valore della proprietà localita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLOCALITA(String value) {
            this.localita = value;
        }

        /**
         * Recupera il valore della proprietà comcodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCOMCODICE() {
            return comcodice;
        }

        /**
         * Imposta il valore della proprietà comcodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCOMCODICE(String value) {
            this.comcodice = value;
        }

        /**
         * Recupera il valore della proprietà flaglibrigenealogici.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGLIBRIGENEALOGICI() {
            return flaglibrigenealogici;
        }

        /**
         * Imposta il valore della proprietà flaglibrigenealogici.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGLIBRIGENEALOGICI(String value) {
            this.flaglibrigenealogici = value;
        }

        /**
         * Recupera il valore della proprietà latitudine.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getLATITUDINE() {
            return latitudine;
        }

        /**
         * Imposta il valore della proprietà latitudine.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setLATITUDINE(BigDecimal value) {
            this.latitudine = value;
        }

        /**
         * Recupera il valore della proprietà longitudine.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getLONGITUDINE() {
            return longitudine;
        }

        /**
         * Imposta il valore della proprietà longitudine.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setLONGITUDINE(BigDecimal value) {
            this.longitudine = value;
        }

        /**
         * Recupera il valore della proprietà sezione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSEZIONE() {
            return sezione;
        }

        /**
         * Imposta il valore della proprietà sezione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSEZIONE(String value) {
            this.sezione = value;
        }

        /**
         * Recupera il valore della proprietà subalterno.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSUBALTERNO() {
            return subalterno;
        }

        /**
         * Imposta il valore della proprietà subalterno.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSUBALTERNO(String value) {
            this.subalterno = value;
        }

        /**
         * Recupera il valore della proprietà fogliocatastale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFOGLIOCATASTALE() {
            return fogliocatastale;
        }

        /**
         * Imposta il valore della proprietà fogliocatastale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFOGLIOCATASTALE(String value) {
            this.fogliocatastale = value;
        }

        /**
         * Recupera il valore della proprietà particella.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPARTICELLA() {
            return particella;
        }

        /**
         * Imposta il valore della proprietà particella.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPARTICELLA(String value) {
            this.particella = value;
        }

        /**
         * Recupera il valore della proprietà autorizzazionelatte.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUTORIZZAZIONELATTE() {
            return autorizzazionelatte;
        }

        /**
         * Imposta il valore della proprietà autorizzazionelatte.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUTORIZZAZIONELATTE(String value) {
            this.autorizzazionelatte = value;
        }

        /**
         * Recupera il valore della proprietà datainizioautorizzazione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAINIZIOAUTORIZZAZIONE() {
            return datainizioautorizzazione;
        }

        /**
         * Imposta il valore della proprietà datainizioautorizzazione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAINIZIOAUTORIZZAZIONE(XMLGregorianCalendar value) {
            this.datainizioautorizzazione = value;
        }

        /**
         * Recupera il valore della proprietà datarevocaautorizzazione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAREVOCAAUTORIZZAZIONE() {
            return datarevocaautorizzazione;
        }

        /**
         * Imposta il valore della proprietà datarevocaautorizzazione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAREVOCAAUTORIZZAZIONE(XMLGregorianCalendar value) {
            this.datarevocaautorizzazione = value;
        }

        /**
         * Recupera il valore della proprietà capi06MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPI06MESI() {
            return capi06MESI;
        }

        /**
         * Imposta il valore della proprietà capi06MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPI06MESI(BigDecimal value) {
            this.capi06MESI = value;
        }

        /**
         * Recupera il valore della proprietà capi624MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPI624MESI() {
            return capi624MESI;
        }

        /**
         * Imposta il valore della proprietà capi624MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPI624MESI(BigDecimal value) {
            this.capi624MESI = value;
        }

        /**
         * Recupera il valore della proprietà capioltre24MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPIOLTRE24MESI() {
            return capioltre24MESI;
        }

        /**
         * Imposta il valore della proprietà capioltre24MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPIOLTRE24MESI(BigDecimal value) {
            this.capioltre24MESI = value;
        }

        /**
         * Recupera il valore della proprietà vacchedalatte.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVACCHEDALATTE() {
            return vacchedalatte;
        }

        /**
         * Imposta il valore della proprietà vacchedalatte.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVACCHEDALATTE(BigDecimal value) {
            this.vacchedalatte = value;
        }

        /**
         * Recupera il valore della proprietà bufalini.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBUFALINI() {
            return bufalini;
        }

        /**
         * Imposta il valore della proprietà bufalini.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBUFALINI(BigDecimal value) {
            this.bufalini = value;
        }

        /**
         * Recupera il valore della proprietà ovini.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOVINI() {
            return ovini;
        }

        /**
         * Imposta il valore della proprietà ovini.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOVINI(BigDecimal value) {
            this.ovini = value;
        }

        /**
         * Recupera il valore della proprietà caprini.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPRINI() {
            return caprini;
        }

        /**
         * Imposta il valore della proprietà caprini.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPRINI(BigDecimal value) {
            this.caprini = value;
        }

        /**
         * Recupera il valore della proprietà dataultimocensimento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAULTIMOCENSIMENTO() {
            return dataultimocensimento;
        }

        /**
         * Imposta il valore della proprietà dataultimocensimento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAULTIMOCENSIMENTO(XMLGregorianCalendar value) {
            this.dataultimocensimento = value;
        }

        /**
         * Recupera il valore della proprietà capitotali.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPITOTALI() {
            return capitotali;
        }

        /**
         * Imposta il valore della proprietà capitotali.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPITOTALI(BigDecimal value) {
            this.capitotali = value;
        }

    }

}
