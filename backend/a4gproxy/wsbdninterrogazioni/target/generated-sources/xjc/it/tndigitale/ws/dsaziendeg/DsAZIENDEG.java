//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:23 PM CEST 
//


package it.tndigitale.ws.dsaziendeg;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import it.tndigitale.a4g.proxy.LocalDateTimeXmlAdapter;
import it.tndigitale.a4g.proxy.LocalDateXmlAdapter;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element name="AZIENDE"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="HOLDER_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="HOLDER_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NORD_GB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="EST_GB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="FUSO_GB" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="X_UTM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="Y_UTM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="FUSO_UTM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="QUAL_SANITARIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_APERTURA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_CHIUSURA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DISTRETTO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DISTRETTO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ASL_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ASL_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="COM_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_VARIAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_ESTRAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="REF_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NOTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "aziende"
})
@XmlRootElement(name = "dsAZIENDE_G")
public class DsAZIENDEG {

    @XmlElement(name = "AZIENDE")
    protected List<DsAZIENDEG.AZIENDE> aziende;

    /**
     * Gets the value of the aziende property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aziende property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAZIENDE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DsAZIENDEG.AZIENDE }
     * 
     * 
     */
    public List<DsAZIENDEG.AZIENDE> getAZIENDE() {
        if (aziende == null) {
            aziende = new ArrayList<DsAZIENDEG.AZIENDE>();
        }
        return this.aziende;
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
     *         &lt;element name="AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="HOLDER_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="HOLDER_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NORD_GB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="EST_GB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="FUSO_GB" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="X_UTM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="Y_UTM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="FUSO_UTM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="QUAL_SANITARIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_APERTURA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_CHIUSURA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DISTRETTO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DISTRETTO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ASL_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ASL_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="COM_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_VARIAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_ESTRAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="REF_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NOTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "aziendaid",
        "codice",
        "longitudine",
        "latitudine",
        "holderid",
        "holderidfiscale",
        "nordgb",
        "estgb",
        "fusogb",
        "xutm",
        "yutm",
        "fusoutm",
        "cap",
        "localita",
        "qualsanitaria",
        "indirizzo",
        "dtapertura",
        "dtchiusura",
        "distrettoid",
        "distrettocodice",
        "aslid",
        "aslcodice",
        "comid",
        "comcodice",
        "tipovariazione",
        "dataestrazione",
        "dtinival",
        "dtfinval",
        "refid",
        "note",
        "utenteid",
        "fogliocatastale",
        "particella",
        "sezione",
        "subalterno"
    })
    public static class AZIENDE {

        @XmlElement(name = "AZIENDA_ID", required = true)
        protected BigDecimal aziendaid;
        @XmlElement(name = "CODICE")
        protected String codice;
        @XmlElement(name = "LONGITUDINE")
        protected BigDecimal longitudine;
        @XmlElement(name = "LATITUDINE")
        protected BigDecimal latitudine;
        @XmlElement(name = "HOLDER_ID")
        protected BigDecimal holderid;
        @XmlElement(name = "HOLDER_ID_FISCALE")
        protected String holderidfiscale;
        @XmlElement(name = "NORD_GB")
        protected BigDecimal nordgb;
        @XmlElement(name = "EST_GB")
        protected BigDecimal estgb;
        @XmlElement(name = "FUSO_GB")
        protected String fusogb;
        @XmlElement(name = "X_UTM")
        protected BigDecimal xutm;
        @XmlElement(name = "Y_UTM")
        protected BigDecimal yutm;
        @XmlElement(name = "FUSO_UTM")
        protected String fusoutm;
        @XmlElement(name = "CAP")
        protected String cap;
        @XmlElement(name = "LOCALITA")
        protected String localita;
        @XmlElement(name = "QUAL_SANITARIA")
        protected String qualsanitaria;
        @XmlElement(name = "INDIRIZZO")
        protected String indirizzo;
        @XmlElement(name = "DT_APERTURA", type = String.class)
        @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
        @XmlSchemaType(name = "date")
        protected LocalDate dtapertura;
        @XmlElement(name = "DT_CHIUSURA", type = String.class)
        @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
        @XmlSchemaType(name = "date")
        protected LocalDate dtchiusura;
        @XmlElement(name = "DISTRETTO_ID")
        protected BigDecimal distrettoid;
        @XmlElement(name = "DISTRETTO_CODICE")
        protected String distrettocodice;
        @XmlElement(name = "ASL_ID")
        protected BigDecimal aslid;
        @XmlElement(name = "ASL_CODICE")
        protected String aslcodice;
        @XmlElement(name = "COM_ID")
        protected BigDecimal comid;
        @XmlElement(name = "COM_CODICE")
        protected String comcodice;
        @XmlElement(name = "TIPO_VARIAZIONE")
        protected String tipovariazione;
        @XmlElement(name = "DATA_ESTRAZIONE", type = String.class)
        @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
        @XmlSchemaType(name = "date")
        protected LocalDate dataestrazione;
        @XmlElement(name = "DT_INIVAL", type = String.class)
        @XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
        @XmlSchemaType(name = "dateTime")
        protected LocalDateTime dtinival;
        @XmlElement(name = "DT_FINVAL", type = String.class)
        @XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
        @XmlSchemaType(name = "dateTime")
        protected LocalDateTime dtfinval;
        @XmlElement(name = "REF_ID")
        protected BigDecimal refid;
        @XmlElement(name = "NOTE")
        protected String note;
        @XmlElement(name = "UTENTE_ID")
        protected BigDecimal utenteid;
        @XmlElement(name = "FOGLIO_CATASTALE")
        protected String fogliocatastale;
        @XmlElement(name = "PARTICELLA")
        protected String particella;
        @XmlElement(name = "SEZIONE")
        protected String sezione;
        @XmlElement(name = "SUBALTERNO")
        protected String subalterno;

        /**
         * Recupera il valore della proprietà aziendaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getAZIENDAID() {
            return aziendaid;
        }

        /**
         * Imposta il valore della proprietà aziendaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setAZIENDAID(BigDecimal value) {
            this.aziendaid = value;
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
         * Recupera il valore della proprietà holderid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getHOLDERID() {
            return holderid;
        }

        /**
         * Imposta il valore della proprietà holderid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setHOLDERID(BigDecimal value) {
            this.holderid = value;
        }

        /**
         * Recupera il valore della proprietà holderidfiscale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHOLDERIDFISCALE() {
            return holderidfiscale;
        }

        /**
         * Imposta il valore della proprietà holderidfiscale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHOLDERIDFISCALE(String value) {
            this.holderidfiscale = value;
        }

        /**
         * Recupera il valore della proprietà nordgb.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNORDGB() {
            return nordgb;
        }

        /**
         * Imposta il valore della proprietà nordgb.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNORDGB(BigDecimal value) {
            this.nordgb = value;
        }

        /**
         * Recupera il valore della proprietà estgb.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getESTGB() {
            return estgb;
        }

        /**
         * Imposta il valore della proprietà estgb.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setESTGB(BigDecimal value) {
            this.estgb = value;
        }

        /**
         * Recupera il valore della proprietà fusogb.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFUSOGB() {
            return fusogb;
        }

        /**
         * Imposta il valore della proprietà fusogb.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFUSOGB(String value) {
            this.fusogb = value;
        }

        /**
         * Recupera il valore della proprietà xutm.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getXUTM() {
            return xutm;
        }

        /**
         * Imposta il valore della proprietà xutm.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setXUTM(BigDecimal value) {
            this.xutm = value;
        }

        /**
         * Recupera il valore della proprietà yutm.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getYUTM() {
            return yutm;
        }

        /**
         * Imposta il valore della proprietà yutm.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setYUTM(BigDecimal value) {
            this.yutm = value;
        }

        /**
         * Recupera il valore della proprietà fusoutm.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFUSOUTM() {
            return fusoutm;
        }

        /**
         * Imposta il valore della proprietà fusoutm.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFUSOUTM(String value) {
            this.fusoutm = value;
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
         * Recupera il valore della proprietà qualsanitaria.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQUALSANITARIA() {
            return qualsanitaria;
        }

        /**
         * Imposta il valore della proprietà qualsanitaria.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQUALSANITARIA(String value) {
            this.qualsanitaria = value;
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
         * Recupera il valore della proprietà dtapertura.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public LocalDate getDTAPERTURA() {
            return dtapertura;
        }

        /**
         * Imposta il valore della proprietà dtapertura.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTAPERTURA(LocalDate value) {
            this.dtapertura = value;
        }

        /**
         * Recupera il valore della proprietà dtchiusura.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public LocalDate getDTCHIUSURA() {
            return dtchiusura;
        }

        /**
         * Imposta il valore della proprietà dtchiusura.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTCHIUSURA(LocalDate value) {
            this.dtchiusura = value;
        }

        /**
         * Recupera il valore della proprietà distrettoid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getDISTRETTOID() {
            return distrettoid;
        }

        /**
         * Imposta il valore della proprietà distrettoid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setDISTRETTOID(BigDecimal value) {
            this.distrettoid = value;
        }

        /**
         * Recupera il valore della proprietà distrettocodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDISTRETTOCODICE() {
            return distrettocodice;
        }

        /**
         * Imposta il valore della proprietà distrettocodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDISTRETTOCODICE(String value) {
            this.distrettocodice = value;
        }

        /**
         * Recupera il valore della proprietà aslid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getASLID() {
            return aslid;
        }

        /**
         * Imposta il valore della proprietà aslid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setASLID(BigDecimal value) {
            this.aslid = value;
        }

        /**
         * Recupera il valore della proprietà aslcodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASLCODICE() {
            return aslcodice;
        }

        /**
         * Imposta il valore della proprietà aslcodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASLCODICE(String value) {
            this.aslcodice = value;
        }

        /**
         * Recupera il valore della proprietà comid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCOMID() {
            return comid;
        }

        /**
         * Imposta il valore della proprietà comid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCOMID(BigDecimal value) {
            this.comid = value;
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
         * Recupera il valore della proprietà tipovariazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOVARIAZIONE() {
            return tipovariazione;
        }

        /**
         * Imposta il valore della proprietà tipovariazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOVARIAZIONE(String value) {
            this.tipovariazione = value;
        }

        /**
         * Recupera il valore della proprietà dataestrazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public LocalDate getDATAESTRAZIONE() {
            return dataestrazione;
        }

        /**
         * Imposta il valore della proprietà dataestrazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDATAESTRAZIONE(LocalDate value) {
            this.dataestrazione = value;
        }

        /**
         * Recupera il valore della proprietà dtinival.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public LocalDateTime getDTINIVAL() {
            return dtinival;
        }

        /**
         * Imposta il valore della proprietà dtinival.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTINIVAL(LocalDateTime value) {
            this.dtinival = value;
        }

        /**
         * Recupera il valore della proprietà dtfinval.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public LocalDateTime getDTFINVAL() {
            return dtfinval;
        }

        /**
         * Imposta il valore della proprietà dtfinval.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTFINVAL(LocalDateTime value) {
            this.dtfinval = value;
        }

        /**
         * Recupera il valore della proprietà refid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getREFID() {
            return refid;
        }

        /**
         * Imposta il valore della proprietà refid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setREFID(BigDecimal value) {
            this.refid = value;
        }

        /**
         * Recupera il valore della proprietà note.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNOTE() {
            return note;
        }

        /**
         * Imposta il valore della proprietà note.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNOTE(String value) {
            this.note = value;
        }

        /**
         * Recupera il valore della proprietà utenteid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getUTENTEID() {
            return utenteid;
        }

        /**
         * Imposta il valore della proprietà utenteid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setUTENTEID(BigDecimal value) {
            this.utenteid = value;
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

    }

}
