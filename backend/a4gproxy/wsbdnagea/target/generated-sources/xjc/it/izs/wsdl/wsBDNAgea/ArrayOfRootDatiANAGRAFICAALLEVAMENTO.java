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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ArrayOfRootDatiANAGRAFICA_ALLEVAMENTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiANAGRAFICA_ALLEVAMENTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ANAGRAFICA_ALLEVAMENTO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_PRODUZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ORIENTAMENTO_PRODUTTIVO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AUTORIZZAZIONE_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOM_PROPRIETARIO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOM_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SOCCIDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_CALCOLO_CAPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_ALLEV_COD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_ALLEV_DESCR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiANAGRAFICA_ALLEVAMENTO", propOrder = {
    "anagraficaallevamento"
})
public class ArrayOfRootDatiANAGRAFICAALLEVAMENTO {

    @XmlElement(name = "ANAGRAFICA_ALLEVAMENTO")
    protected List<ArrayOfRootDatiANAGRAFICAALLEVAMENTO.ANAGRAFICAALLEVAMENTO> anagraficaallevamento;

    /**
     * Gets the value of the anagraficaallevamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the anagraficaallevamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getANAGRAFICAALLEVAMENTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiANAGRAFICAALLEVAMENTO.ANAGRAFICAALLEVAMENTO }
     * 
     * 
     */
    public List<ArrayOfRootDatiANAGRAFICAALLEVAMENTO.ANAGRAFICAALLEVAMENTO> getANAGRAFICAALLEVAMENTO() {
        if (anagraficaallevamento == null) {
            anagraficaallevamento = new ArrayList<ArrayOfRootDatiANAGRAFICAALLEVAMENTO.ANAGRAFICAALLEVAMENTO>();
        }
        return this.anagraficaallevamento;
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
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_PRODUZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ORIENTAMENTO_PRODUTTIVO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AUTORIZZAZIONE_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOM_PROPRIETARIO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOM_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SOCCIDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DATA_CALCOLO_CAPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_ALLEV_COD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_ALLEV_DESCR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "specodice",
        "denominazione",
        "indirizzo",
        "cap",
        "localita",
        "comune",
        "tipoproduzione",
        "orientamentoproduttivo",
        "autorizzazionelatte",
        "dtinizioattivita",
        "dtfineattivita",
        "codfiscaleprop",
        "denomproprietario",
        "codfiscaledeten",
        "denomdetentore",
        "dtiniziodetentore",
        "dtfinedetentore",
        "soccida",
        "latitudine",
        "longitudine",
        "fogliocatastale",
        "sezione",
        "particella",
        "subalterno",
        "capitotali",
        "datacalcolocapi",
        "tipoallevcod",
        "tipoallevdescr"
    })
    public static class ANAGRAFICAALLEVAMENTO {

        @XmlElement(name = "ALLEV_ID", required = true)
        protected BigDecimal allevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
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
        @XmlElement(name = "COMUNE")
        protected String comune;
        @XmlElement(name = "TIPO_PRODUZIONE")
        protected String tipoproduzione;
        @XmlElement(name = "ORIENTAMENTO_PRODUTTIVO")
        protected String orientamentoproduttivo;
        @XmlElement(name = "AUTORIZZAZIONE_LATTE")
        protected String autorizzazionelatte;
        @XmlElement(name = "DT_INIZIO_ATTIVITA")
        protected String dtinizioattivita;
        @XmlElement(name = "DT_FINE_ATTIVITA")
        protected String dtfineattivita;
        @XmlElement(name = "COD_FISCALE_PROP")
        protected String codfiscaleprop;
        @XmlElement(name = "DENOM_PROPRIETARIO")
        protected String denomproprietario;
        @XmlElement(name = "COD_FISCALE_DETEN")
        protected String codfiscaledeten;
        @XmlElement(name = "DENOM_DETENTORE")
        protected String denomdetentore;
        @XmlElement(name = "DT_INIZIO_DETENTORE")
        protected String dtiniziodetentore;
        @XmlElement(name = "DT_FINE_DETENTORE")
        protected String dtfinedetentore;
        @XmlElement(name = "SOCCIDA")
        protected String soccida;
        @XmlElement(name = "LATITUDINE")
        protected BigDecimal latitudine;
        @XmlElement(name = "LONGITUDINE")
        protected BigDecimal longitudine;
        @XmlElement(name = "FOGLIO_CATASTALE")
        protected String fogliocatastale;
        @XmlElement(name = "SEZIONE")
        protected String sezione;
        @XmlElement(name = "PARTICELLA")
        protected String particella;
        @XmlElement(name = "SUBALTERNO")
        protected String subalterno;
        @XmlElement(name = "CAPI_TOTALI")
        protected BigDecimal capitotali;
        @XmlElement(name = "DATA_CALCOLO_CAPI")
        protected String datacalcolocapi;
        @XmlElement(name = "TIPO_ALLEV_COD")
        protected String tipoallevcod;
        @XmlElement(name = "TIPO_ALLEV_DESCR")
        protected String tipoallevdescr;

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
         * Recupera il valore della proprietà comune.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCOMUNE() {
            return comune;
        }

        /**
         * Imposta il valore della proprietà comune.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCOMUNE(String value) {
            this.comune = value;
        }

        /**
         * Recupera il valore della proprietà tipoproduzione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOPRODUZIONE() {
            return tipoproduzione;
        }

        /**
         * Imposta il valore della proprietà tipoproduzione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOPRODUZIONE(String value) {
            this.tipoproduzione = value;
        }

        /**
         * Recupera il valore della proprietà orientamentoproduttivo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getORIENTAMENTOPRODUTTIVO() {
            return orientamentoproduttivo;
        }

        /**
         * Imposta il valore della proprietà orientamentoproduttivo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setORIENTAMENTOPRODUTTIVO(String value) {
            this.orientamentoproduttivo = value;
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
         * Recupera il valore della proprietà dtinizioattivita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDTINIZIOATTIVITA() {
            return dtinizioattivita;
        }

        /**
         * Imposta il valore della proprietà dtinizioattivita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTINIZIOATTIVITA(String value) {
            this.dtinizioattivita = value;
        }

        /**
         * Recupera il valore della proprietà dtfineattivita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDTFINEATTIVITA() {
            return dtfineattivita;
        }

        /**
         * Imposta il valore della proprietà dtfineattivita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTFINEATTIVITA(String value) {
            this.dtfineattivita = value;
        }

        /**
         * Recupera il valore della proprietà codfiscaleprop.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODFISCALEPROP() {
            return codfiscaleprop;
        }

        /**
         * Imposta il valore della proprietà codfiscaleprop.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODFISCALEPROP(String value) {
            this.codfiscaleprop = value;
        }

        /**
         * Recupera il valore della proprietà denomproprietario.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDENOMPROPRIETARIO() {
            return denomproprietario;
        }

        /**
         * Imposta il valore della proprietà denomproprietario.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDENOMPROPRIETARIO(String value) {
            this.denomproprietario = value;
        }

        /**
         * Recupera il valore della proprietà codfiscaledeten.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODFISCALEDETEN() {
            return codfiscaledeten;
        }

        /**
         * Imposta il valore della proprietà codfiscaledeten.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODFISCALEDETEN(String value) {
            this.codfiscaledeten = value;
        }

        /**
         * Recupera il valore della proprietà denomdetentore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDENOMDETENTORE() {
            return denomdetentore;
        }

        /**
         * Imposta il valore della proprietà denomdetentore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDENOMDETENTORE(String value) {
            this.denomdetentore = value;
        }

        /**
         * Recupera il valore della proprietà dtiniziodetentore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDTINIZIODETENTORE() {
            return dtiniziodetentore;
        }

        /**
         * Imposta il valore della proprietà dtiniziodetentore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTINIZIODETENTORE(String value) {
            this.dtiniziodetentore = value;
        }

        /**
         * Recupera il valore della proprietà dtfinedetentore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDTFINEDETENTORE() {
            return dtfinedetentore;
        }

        /**
         * Imposta il valore della proprietà dtfinedetentore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTFINEDETENTORE(String value) {
            this.dtfinedetentore = value;
        }

        /**
         * Recupera il valore della proprietà soccida.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSOCCIDA() {
            return soccida;
        }

        /**
         * Imposta il valore della proprietà soccida.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSOCCIDA(String value) {
            this.soccida = value;
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

        /**
         * Recupera il valore della proprietà datacalcolocapi.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDATACALCOLOCAPI() {
            return datacalcolocapi;
        }

        /**
         * Imposta il valore della proprietà datacalcolocapi.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDATACALCOLOCAPI(String value) {
            this.datacalcolocapi = value;
        }

        /**
         * Recupera il valore della proprietà tipoallevcod.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOALLEVCOD() {
            return tipoallevcod;
        }

        /**
         * Imposta il valore della proprietà tipoallevcod.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOALLEVCOD(String value) {
            this.tipoallevcod = value;
        }

        /**
         * Recupera il valore della proprietà tipoallevdescr.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOALLEVDESCR() {
            return tipoallevdescr;
        }

        /**
         * Imposta il valore della proprietà tipoallevdescr.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOALLEVDESCR(String value) {
            this.tipoallevdescr = value;
        }

    }

}
