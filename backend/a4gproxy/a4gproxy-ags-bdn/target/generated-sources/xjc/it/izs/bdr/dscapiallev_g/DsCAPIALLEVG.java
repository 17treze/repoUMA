//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:36:51 PM CEST 
//


package it.izs.bdr.dscapiallev_g;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="CAPI_BOVINI"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="REGSTA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="COD_MADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_PADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_PRECEDENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_APPLICAZIONE_MARCHIO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_LATTAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_LATTAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_COMPILAZIONE_CEDOLA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_STATO_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_INSEMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_PASSAPORTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_ERR_TEMPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RAZZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ANIMO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ST_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="LG_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE_ATTUALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE_ATTUALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_DENOMINAZIONE_ATTUALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_COMUNICAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="COD_STATO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_MADRE_GENETICA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "capibovini"
})
@XmlRootElement(name = "dsCAPIALLEV_G")
public class DsCAPIALLEVG {

    @XmlElement(name = "CAPI_BOVINI")
    protected List<DsCAPIALLEVG.CAPIBOVINI> capibovini;

    /**
     * Gets the value of the capibovini property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capibovini property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAPIBOVINI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DsCAPIALLEVG.CAPIBOVINI }
     * 
     * 
     */
    public List<DsCAPIALLEVG.CAPIBOVINI> getCAPIBOVINI() {
        if (capibovini == null) {
            capibovini = new ArrayList<DsCAPIALLEVG.CAPIBOVINI>();
        }
        return this.capibovini;
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
     *         &lt;element name="REGSTA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="COD_MADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_PADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_PRECEDENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_APPLICAZIONE_MARCHIO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_LATTAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_LATTAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_COMPILAZIONE_CEDOLA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_STATO_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_INSEMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_PASSAPORTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_ERR_TEMPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RAZZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ANIMO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ST_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="LG_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE_ATTUALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE_ATTUALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_DENOMINAZIONE_ATTUALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_COMUNICAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="COD_STATO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_MADRE_GENETICA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "regstaid",
        "capoid",
        "codice",
        "sesso",
        "dtnascita",
        "codmadre",
        "codpadre",
        "tag",
        "codprecedente",
        "dtapplicazionemarchio",
        "dtiniziolattazione",
        "dtfinelattazione",
        "dtcompilazionecedola",
        "flagstatocapo",
        "flaginseminazione",
        "flagpassaporto",
        "flagerrtempo",
        "tipoorigine",
        "razzaid",
        "razzacodice",
        "animoid",
        "allevid",
        "allevidfiscale",
        "allevidfiscaledeten",
        "stid",
        "lgid",
        "aziendaid",
        "aziendacodice",
        "specodice",
        "aziendacodiceattuale",
        "allevidfiscaleattuale",
        "allevdenominazioneattuale",
        "motivoingresso",
        "dtingresso",
        "dtuscita",
        "motivouscita",
        "dtcomunicazione",
        "codstatoorigine",
        "codmadregenetica"
    })
    public static class CAPIBOVINI {

        @XmlElement(name = "REGSTA_ID", required = true)
        protected BigDecimal regstaid;
        @XmlElement(name = "CAPO_ID", required = true)
        protected BigDecimal capoid;
        @XmlElement(name = "CODICE")
        protected String codice;
        @XmlElement(name = "SESSO")
        protected String sesso;
        @XmlElement(name = "DT_NASCITA")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtnascita;
        @XmlElement(name = "COD_MADRE")
        protected String codmadre;
        @XmlElement(name = "COD_PADRE")
        protected String codpadre;
        @XmlElement(name = "TAG")
        protected String tag;
        @XmlElement(name = "COD_PRECEDENTE")
        protected String codprecedente;
        @XmlElement(name = "DT_APPLICAZIONE_MARCHIO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtapplicazionemarchio;
        @XmlElement(name = "DT_INIZIO_LATTAZIONE")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtiniziolattazione;
        @XmlElement(name = "DT_FINE_LATTAZIONE")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtfinelattazione;
        @XmlElement(name = "DT_COMPILAZIONE_CEDOLA")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtcompilazionecedola;
        @XmlElement(name = "FLAG_STATO_CAPO")
        protected String flagstatocapo;
        @XmlElement(name = "FLAG_INSEMINAZIONE")
        protected String flaginseminazione;
        @XmlElement(name = "FLAG_PASSAPORTO")
        protected String flagpassaporto;
        @XmlElement(name = "FLAG_ERR_TEMPO")
        protected String flagerrtempo;
        @XmlElement(name = "TIPO_ORIGINE")
        protected String tipoorigine;
        @XmlElement(name = "RAZZA_ID")
        protected BigDecimal razzaid;
        @XmlElement(name = "RAZZA_CODICE")
        protected String razzacodice;
        @XmlElement(name = "ANIMO_ID")
        protected BigDecimal animoid;
        @XmlElement(name = "ALLEV_ID")
        protected BigDecimal allevid;
        @XmlElement(name = "ALLEV_ID_FISCALE")
        protected String allevidfiscale;
        @XmlElement(name = "ALLEV_ID_FISCALE_DETEN")
        protected String allevidfiscaledeten;
        @XmlElement(name = "ST_ID")
        protected BigDecimal stid;
        @XmlElement(name = "LG_ID")
        protected BigDecimal lgid;
        @XmlElement(name = "AZIENDA_ID")
        protected BigDecimal aziendaid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "AZIENDA_CODICE_ATTUALE")
        protected String aziendacodiceattuale;
        @XmlElement(name = "ALLEV_ID_FISCALE_ATTUALE")
        protected String allevidfiscaleattuale;
        @XmlElement(name = "ALLEV_DENOMINAZIONE_ATTUALE")
        protected String allevdenominazioneattuale;
        @XmlElement(name = "MOTIVO_INGRESSO")
        protected String motivoingresso;
        @XmlElement(name = "DT_INGRESSO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtingresso;
        @XmlElement(name = "DT_USCITA")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtuscita;
        @XmlElement(name = "MOTIVO_USCITA")
        protected String motivouscita;
        @XmlElement(name = "DT_COMUNICAZIONE")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtcomunicazione;
        @XmlElement(name = "COD_STATO_ORIGINE")
        protected String codstatoorigine;
        @XmlElement(name = "COD_MADRE_GENETICA")
        protected String codmadregenetica;

        /**
         * Recupera il valore della proprietà regstaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getREGSTAID() {
            return regstaid;
        }

        /**
         * Imposta il valore della proprietà regstaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setREGSTAID(BigDecimal value) {
            this.regstaid = value;
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
         * Recupera il valore della proprietà sesso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSESSO() {
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
        public void setSESSO(String value) {
            this.sesso = value;
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
         * Recupera il valore della proprietà codmadre.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODMADRE() {
            return codmadre;
        }

        /**
         * Imposta il valore della proprietà codmadre.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODMADRE(String value) {
            this.codmadre = value;
        }

        /**
         * Recupera il valore della proprietà codpadre.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODPADRE() {
            return codpadre;
        }

        /**
         * Imposta il valore della proprietà codpadre.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODPADRE(String value) {
            this.codpadre = value;
        }

        /**
         * Recupera il valore della proprietà tag.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTAG() {
            return tag;
        }

        /**
         * Imposta il valore della proprietà tag.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTAG(String value) {
            this.tag = value;
        }

        /**
         * Recupera il valore della proprietà codprecedente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODPRECEDENTE() {
            return codprecedente;
        }

        /**
         * Imposta il valore della proprietà codprecedente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODPRECEDENTE(String value) {
            this.codprecedente = value;
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
         * Recupera il valore della proprietà dtiniziolattazione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINIZIOLATTAZIONE() {
            return dtiniziolattazione;
        }

        /**
         * Imposta il valore della proprietà dtiniziolattazione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINIZIOLATTAZIONE(XMLGregorianCalendar value) {
            this.dtiniziolattazione = value;
        }

        /**
         * Recupera il valore della proprietà dtfinelattazione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTFINELATTAZIONE() {
            return dtfinelattazione;
        }

        /**
         * Imposta il valore della proprietà dtfinelattazione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTFINELATTAZIONE(XMLGregorianCalendar value) {
            this.dtfinelattazione = value;
        }

        /**
         * Recupera il valore della proprietà dtcompilazionecedola.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTCOMPILAZIONECEDOLA() {
            return dtcompilazionecedola;
        }

        /**
         * Imposta il valore della proprietà dtcompilazionecedola.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTCOMPILAZIONECEDOLA(XMLGregorianCalendar value) {
            this.dtcompilazionecedola = value;
        }

        /**
         * Recupera il valore della proprietà flagstatocapo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGSTATOCAPO() {
            return flagstatocapo;
        }

        /**
         * Imposta il valore della proprietà flagstatocapo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGSTATOCAPO(String value) {
            this.flagstatocapo = value;
        }

        /**
         * Recupera il valore della proprietà flaginseminazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGINSEMINAZIONE() {
            return flaginseminazione;
        }

        /**
         * Imposta il valore della proprietà flaginseminazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGINSEMINAZIONE(String value) {
            this.flaginseminazione = value;
        }

        /**
         * Recupera il valore della proprietà flagpassaporto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGPASSAPORTO() {
            return flagpassaporto;
        }

        /**
         * Imposta il valore della proprietà flagpassaporto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGPASSAPORTO(String value) {
            this.flagpassaporto = value;
        }

        /**
         * Recupera il valore della proprietà flagerrtempo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGERRTEMPO() {
            return flagerrtempo;
        }

        /**
         * Imposta il valore della proprietà flagerrtempo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGERRTEMPO(String value) {
            this.flagerrtempo = value;
        }

        /**
         * Recupera il valore della proprietà tipoorigine.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOORIGINE() {
            return tipoorigine;
        }

        /**
         * Imposta il valore della proprietà tipoorigine.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOORIGINE(String value) {
            this.tipoorigine = value;
        }

        /**
         * Recupera il valore della proprietà razzaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getRAZZAID() {
            return razzaid;
        }

        /**
         * Imposta il valore della proprietà razzaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setRAZZAID(BigDecimal value) {
            this.razzaid = value;
        }

        /**
         * Recupera il valore della proprietà razzacodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRAZZACODICE() {
            return razzacodice;
        }

        /**
         * Imposta il valore della proprietà razzacodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRAZZACODICE(String value) {
            this.razzacodice = value;
        }

        /**
         * Recupera il valore della proprietà animoid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getANIMOID() {
            return animoid;
        }

        /**
         * Imposta il valore della proprietà animoid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setANIMOID(BigDecimal value) {
            this.animoid = value;
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
         * Recupera il valore della proprietà allevidfiscale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getALLEVIDFISCALE() {
            return allevidfiscale;
        }

        /**
         * Imposta il valore della proprietà allevidfiscale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setALLEVIDFISCALE(String value) {
            this.allevidfiscale = value;
        }

        /**
         * Recupera il valore della proprietà allevidfiscaledeten.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getALLEVIDFISCALEDETEN() {
            return allevidfiscaledeten;
        }

        /**
         * Imposta il valore della proprietà allevidfiscaledeten.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setALLEVIDFISCALEDETEN(String value) {
            this.allevidfiscaledeten = value;
        }

        /**
         * Recupera il valore della proprietà stid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getSTID() {
            return stid;
        }

        /**
         * Imposta il valore della proprietà stid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setSTID(BigDecimal value) {
            this.stid = value;
        }

        /**
         * Recupera il valore della proprietà lgid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getLGID() {
            return lgid;
        }

        /**
         * Imposta il valore della proprietà lgid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setLGID(BigDecimal value) {
            this.lgid = value;
        }

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
         * Recupera il valore della proprietà aziendacodiceattuale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAZIENDACODICEATTUALE() {
            return aziendacodiceattuale;
        }

        /**
         * Imposta il valore della proprietà aziendacodiceattuale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAZIENDACODICEATTUALE(String value) {
            this.aziendacodiceattuale = value;
        }

        /**
         * Recupera il valore della proprietà allevidfiscaleattuale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getALLEVIDFISCALEATTUALE() {
            return allevidfiscaleattuale;
        }

        /**
         * Imposta il valore della proprietà allevidfiscaleattuale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setALLEVIDFISCALEATTUALE(String value) {
            this.allevidfiscaleattuale = value;
        }

        /**
         * Recupera il valore della proprietà allevdenominazioneattuale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getALLEVDENOMINAZIONEATTUALE() {
            return allevdenominazioneattuale;
        }

        /**
         * Imposta il valore della proprietà allevdenominazioneattuale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setALLEVDENOMINAZIONEATTUALE(String value) {
            this.allevdenominazioneattuale = value;
        }

        /**
         * Recupera il valore della proprietà motivoingresso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOTIVOINGRESSO() {
            return motivoingresso;
        }

        /**
         * Imposta il valore della proprietà motivoingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOTIVOINGRESSO(String value) {
            this.motivoingresso = value;
        }

        /**
         * Recupera il valore della proprietà dtingresso.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINGRESSO() {
            return dtingresso;
        }

        /**
         * Imposta il valore della proprietà dtingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINGRESSO(XMLGregorianCalendar value) {
            this.dtingresso = value;
        }

        /**
         * Recupera il valore della proprietà dtuscita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTUSCITA() {
            return dtuscita;
        }

        /**
         * Imposta il valore della proprietà dtuscita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTUSCITA(XMLGregorianCalendar value) {
            this.dtuscita = value;
        }

        /**
         * Recupera il valore della proprietà motivouscita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOTIVOUSCITA() {
            return motivouscita;
        }

        /**
         * Imposta il valore della proprietà motivouscita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOTIVOUSCITA(String value) {
            this.motivouscita = value;
        }

        /**
         * Recupera il valore della proprietà dtcomunicazione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTCOMUNICAZIONE() {
            return dtcomunicazione;
        }

        /**
         * Imposta il valore della proprietà dtcomunicazione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTCOMUNICAZIONE(XMLGregorianCalendar value) {
            this.dtcomunicazione = value;
        }

        /**
         * Recupera il valore della proprietà codstatoorigine.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODSTATOORIGINE() {
            return codstatoorigine;
        }

        /**
         * Imposta il valore della proprietà codstatoorigine.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODSTATOORIGINE(String value) {
            this.codstatoorigine = value;
        }

        /**
         * Recupera il valore della proprietà codmadregenetica.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODMADREGENETICA() {
            return codmadregenetica;
        }

        /**
         * Imposta il valore della proprietà codmadregenetica.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODMADREGENETICA(String value) {
            this.codmadregenetica = value;
        }

    }

}
