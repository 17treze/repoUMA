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
 * <p>Classe Java per ArrayOfRootDatiUNITA_TECNICHE_MUL complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiUNITA_TECNICHE_MUL"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UNITA_TECNICHE_MUL" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_UNITA_TECNICA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_FASCICOLO_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_PRODUZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AUTORIZZAZIONE_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOM_PROPRIETARIO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOM_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SOCCIDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_CALCOLO_CAPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiUNITA_TECNICHE_MUL", propOrder = {
    "unitatecnichemul"
})
public class ArrayOfRootDatiUNITATECNICHEMUL {

    @XmlElement(name = "UNITA_TECNICHE_MUL")
    protected List<ArrayOfRootDatiUNITATECNICHEMUL.UNITATECNICHEMUL> unitatecnichemul;

    /**
     * Gets the value of the unitatecnichemul property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unitatecnichemul property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUNITATECNICHEMUL().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiUNITATECNICHEMUL.UNITATECNICHEMUL }
     * 
     * 
     */
    public List<ArrayOfRootDatiUNITATECNICHEMUL.UNITATECNICHEMUL> getUNITATECNICHEMUL() {
        if (unitatecnichemul == null) {
            unitatecnichemul = new ArrayList<ArrayOfRootDatiUNITATECNICHEMUL.UNITATECNICHEMUL>();
        }
        return this.unitatecnichemul;
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
     *         &lt;element name="P_UNITA_TECNICA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_FASCICOLO_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_PRODUZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AUTORIZZAZIONE_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOM_PROPRIETARIO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOM_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SOCCIDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DATA_CALCOLO_CAPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "punitatecnicaid",
        "pfascicoloid",
        "allevid",
        "aziendacodice",
        "idfiscale",
        "specodice",
        "denominazione",
        "indirizzo",
        "localita",
        "cap",
        "comune",
        "tipoproduzione",
        "latitudine",
        "longitudine",
        "sezione",
        "subalterno",
        "dtinizioattivita",
        "dtfineattivita",
        "fogliocatastale",
        "particella",
        "autorizzazionelatte",
        "codfiscaleprop",
        "denomproprietario",
        "codfiscaledeten",
        "denomdetentore",
        "dtiniziodetentore",
        "dtfinedetentore",
        "soccida",
        "capitotali",
        "datacalcolocapi"
    })
    public static class UNITATECNICHEMUL {

        @XmlElement(name = "P_UNITA_TECNICA_ID", required = true)
        protected BigDecimal punitatecnicaid;
        @XmlElement(name = "P_FASCICOLO_ID")
        protected String pfascicoloid;
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
        @XmlElement(name = "LOCALITA")
        protected String localita;
        @XmlElement(name = "CAP")
        protected String cap;
        @XmlElement(name = "COMUNE")
        protected String comune;
        @XmlElement(name = "TIPO_PRODUZIONE")
        protected String tipoproduzione;
        @XmlElement(name = "LATITUDINE")
        protected BigDecimal latitudine;
        @XmlElement(name = "LONGITUDINE")
        protected BigDecimal longitudine;
        @XmlElement(name = "SEZIONE")
        protected String sezione;
        @XmlElement(name = "SUBALTERNO")
        protected String subalterno;
        @XmlElement(name = "DT_INIZIO_ATTIVITA")
        protected String dtinizioattivita;
        @XmlElement(name = "DT_FINE_ATTIVITA")
        protected String dtfineattivita;
        @XmlElement(name = "FOGLIO_CATASTALE")
        protected String fogliocatastale;
        @XmlElement(name = "PARTICELLA")
        protected String particella;
        @XmlElement(name = "AUTORIZZAZIONE_LATTE")
        protected String autorizzazionelatte;
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
        @XmlElement(name = "CAPI_TOTALI")
        protected BigDecimal capitotali;
        @XmlElement(name = "DATA_CALCOLO_CAPI")
        protected String datacalcolocapi;

        /**
         * Recupera il valore della proprietà punitatecnicaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPUNITATECNICAID() {
            return punitatecnicaid;
        }

        /**
         * Imposta il valore della proprietà punitatecnicaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPUNITATECNICAID(BigDecimal value) {
            this.punitatecnicaid = value;
        }

        /**
         * Recupera il valore della proprietà pfascicoloid.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPFASCICOLOID() {
            return pfascicoloid;
        }

        /**
         * Imposta il valore della proprietà pfascicoloid.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPFASCICOLOID(String value) {
            this.pfascicoloid = value;
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

    }

}
