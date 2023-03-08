//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:56 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.dsequicapg;

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
 *         &lt;element name="CAPO"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPO_UNIRE_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="IDENT_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CAPO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PASSAPORTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_COMUNICAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_APPLICAZIONE_MARCHIO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="ST_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ST_ORIGINE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MANTELLO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_CONSUMO_UMANO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ID_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ID_FISCALE_DET" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_ISCRIZIONE_LIBRO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_UELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CERTIFICATO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_RIF_LOCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_MODELLO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_STATO_PASS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_ENTE_PASS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_RILASCIO_PASS" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="REGSTA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ME_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_VARIAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIVAL" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINVAL" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="REF_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_INSERIMENTO_BDN" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
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
    "capo"
})
@XmlRootElement(name = "dsEQUI_CAPI_G")
public class DsEQUICAPIG {

    @XmlElement(name = "CAPO")
    protected List<DsEQUICAPIG.CAPO> capo;

    /**
     * Gets the value of the capo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAPO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DsEQUICAPIG.CAPO }
     * 
     * 
     */
    public List<DsEQUICAPIG.CAPO> getCAPO() {
        if (capo == null) {
            capo = new ArrayList<DsEQUICAPIG.CAPO>();
        }
        return this.capo;
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
     *         &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPO_UNIRE_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="IDENT_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CAPO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PASSAPORTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_COMUNICAZIONE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_APPLICAZIONE_MARCHIO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="ST_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ST_ORIGINE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MANTELLO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_CONSUMO_UMANO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ID_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ID_FISCALE_DET" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_ISCRIZIONE_LIBRO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_UELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CERTIFICATO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_RIF_LOCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_MODELLO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_STATO_PASS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_ENTE_PASS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_RILASCIO_PASS" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="REGSTA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ME_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_VARIAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIVAL" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINVAL" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="REF_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DATA_INSERIMENTO_BDN" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
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
        "capoid",
        "allevid",
        "allevidfiscale",
        "aziendacodice",
        "speciecodice",
        "capounireid",
        "codelettronico",
        "identnome",
        "dtnascita",
        "sesso",
        "specapocodice",
        "passaporto",
        "tipoorigine",
        "dtcomunicazione",
        "dtapplicazionemarchio",
        "dtingresso",
        "stcodice",
        "storiginecodice",
        "razzacodice",
        "mantellocodice",
        "flagconsumoumano",
        "idfiscaleprop",
        "idfiscaledet",
        "lgcodice",
        "dtiscrizionelibro",
        "codiceueln",
        "numcertificato",
        "numriflocale",
        "dtmodello",
        "codicestatopass",
        "codiceentepass",
        "dtrilasciopass",
        "regstaid",
        "meid",
        "tipovariazione",
        "utenteid",
        "dtinival",
        "dtfinval",
        "refid",
        "datainserimentobdn"
    })
    public static class CAPO {

        @XmlElement(name = "CAPO_ID", required = true)
        protected BigDecimal capoid;
        @XmlElement(name = "ALLEV_ID", required = true)
        protected BigDecimal allevid;
        @XmlElement(name = "ALLEV_ID_FISCALE")
        protected String allevidfiscale;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "SPECIE_CODICE")
        protected String speciecodice;
        @XmlElement(name = "CAPO_UNIRE_ID")
        protected String capounireid;
        @XmlElement(name = "COD_ELETTRONICO")
        protected String codelettronico;
        @XmlElement(name = "IDENT_NOME")
        protected String identnome;
        @XmlElement(name = "DT_NASCITA")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtnascita;
        @XmlElement(name = "SESSO")
        protected String sesso;
        @XmlElement(name = "SPE_CAPO_CODICE")
        protected String specapocodice;
        @XmlElement(name = "PASSAPORTO")
        protected String passaporto;
        @XmlElement(name = "TIPO_ORIGINE")
        protected String tipoorigine;
        @XmlElement(name = "DT_COMUNICAZIONE")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtcomunicazione;
        @XmlElement(name = "DT_APPLICAZIONE_MARCHIO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtapplicazionemarchio;
        @XmlElement(name = "DT_INGRESSO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtingresso;
        @XmlElement(name = "ST_CODICE")
        protected String stcodice;
        @XmlElement(name = "ST_ORIGINE_CODICE")
        protected String storiginecodice;
        @XmlElement(name = "RAZZA_CODICE")
        protected String razzacodice;
        @XmlElement(name = "MANTELLO_CODICE")
        protected String mantellocodice;
        @XmlElement(name = "FLAG_CONSUMO_UMANO")
        protected String flagconsumoumano;
        @XmlElement(name = "ID_FISCALE_PROP")
        protected String idfiscaleprop;
        @XmlElement(name = "ID_FISCALE_DET")
        protected String idfiscaledet;
        @XmlElement(name = "LG_CODICE")
        protected String lgcodice;
        @XmlElement(name = "DT_ISCRIZIONE_LIBRO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtiscrizionelibro;
        @XmlElement(name = "CODICE_UELN")
        protected String codiceueln;
        @XmlElement(name = "NUM_CERTIFICATO")
        protected String numcertificato;
        @XmlElement(name = "NUM_RIF_LOCALE")
        protected String numriflocale;
        @XmlElement(name = "DT_MODELLO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtmodello;
        @XmlElement(name = "CODICE_STATO_PASS")
        protected String codicestatopass;
        @XmlElement(name = "CODICE_ENTE_PASS")
        protected String codiceentepass;
        @XmlElement(name = "DT_RILASCIO_PASS")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtrilasciopass;
        @XmlElement(name = "REGSTA_ID")
        protected BigDecimal regstaid;
        @XmlElement(name = "ME_ID")
        protected BigDecimal meid;
        @XmlElement(name = "TIPO_VARIAZIONE")
        protected String tipovariazione;
        @XmlElement(name = "UTENTE_ID")
        protected BigDecimal utenteid;
        @XmlElement(name = "DT_INIVAL")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtinival;
        @XmlElement(name = "DT_FINVAL")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtfinval;
        @XmlElement(name = "REF_ID")
        protected BigDecimal refid;
        @XmlElement(name = "DATA_INSERIMENTO_BDN")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar datainserimentobdn;

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
         * Recupera il valore della proprietà speciecodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECIECODICE() {
            return speciecodice;
        }

        /**
         * Imposta il valore della proprietà speciecodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECIECODICE(String value) {
            this.speciecodice = value;
        }

        /**
         * Recupera il valore della proprietà capounireid.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPOUNIREID() {
            return capounireid;
        }

        /**
         * Imposta il valore della proprietà capounireid.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPOUNIREID(String value) {
            this.capounireid = value;
        }

        /**
         * Recupera il valore della proprietà codelettronico.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODELETTRONICO() {
            return codelettronico;
        }

        /**
         * Imposta il valore della proprietà codelettronico.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODELETTRONICO(String value) {
            this.codelettronico = value;
        }

        /**
         * Recupera il valore della proprietà identnome.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIDENTNOME() {
            return identnome;
        }

        /**
         * Imposta il valore della proprietà identnome.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIDENTNOME(String value) {
            this.identnome = value;
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
         * Recupera il valore della proprietà specapocodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECAPOCODICE() {
            return specapocodice;
        }

        /**
         * Imposta il valore della proprietà specapocodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECAPOCODICE(String value) {
            this.specapocodice = value;
        }

        /**
         * Recupera il valore della proprietà passaporto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPASSAPORTO() {
            return passaporto;
        }

        /**
         * Imposta il valore della proprietà passaporto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPASSAPORTO(String value) {
            this.passaporto = value;
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
         * Recupera il valore della proprietà stcodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSTCODICE() {
            return stcodice;
        }

        /**
         * Imposta il valore della proprietà stcodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSTCODICE(String value) {
            this.stcodice = value;
        }

        /**
         * Recupera il valore della proprietà storiginecodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSTORIGINECODICE() {
            return storiginecodice;
        }

        /**
         * Imposta il valore della proprietà storiginecodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSTORIGINECODICE(String value) {
            this.storiginecodice = value;
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
         * Recupera il valore della proprietà mantellocodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMANTELLOCODICE() {
            return mantellocodice;
        }

        /**
         * Imposta il valore della proprietà mantellocodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMANTELLOCODICE(String value) {
            this.mantellocodice = value;
        }

        /**
         * Recupera il valore della proprietà flagconsumoumano.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGCONSUMOUMANO() {
            return flagconsumoumano;
        }

        /**
         * Imposta il valore della proprietà flagconsumoumano.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGCONSUMOUMANO(String value) {
            this.flagconsumoumano = value;
        }

        /**
         * Recupera il valore della proprietà idfiscaleprop.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIDFISCALEPROP() {
            return idfiscaleprop;
        }

        /**
         * Imposta il valore della proprietà idfiscaleprop.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIDFISCALEPROP(String value) {
            this.idfiscaleprop = value;
        }

        /**
         * Recupera il valore della proprietà idfiscaledet.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIDFISCALEDET() {
            return idfiscaledet;
        }

        /**
         * Imposta il valore della proprietà idfiscaledet.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIDFISCALEDET(String value) {
            this.idfiscaledet = value;
        }

        /**
         * Recupera il valore della proprietà lgcodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLGCODICE() {
            return lgcodice;
        }

        /**
         * Imposta il valore della proprietà lgcodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLGCODICE(String value) {
            this.lgcodice = value;
        }

        /**
         * Recupera il valore della proprietà dtiscrizionelibro.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTISCRIZIONELIBRO() {
            return dtiscrizionelibro;
        }

        /**
         * Imposta il valore della proprietà dtiscrizionelibro.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTISCRIZIONELIBRO(XMLGregorianCalendar value) {
            this.dtiscrizionelibro = value;
        }

        /**
         * Recupera il valore della proprietà codiceueln.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEUELN() {
            return codiceueln;
        }

        /**
         * Imposta il valore della proprietà codiceueln.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEUELN(String value) {
            this.codiceueln = value;
        }

        /**
         * Recupera il valore della proprietà numcertificato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMCERTIFICATO() {
            return numcertificato;
        }

        /**
         * Imposta il valore della proprietà numcertificato.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMCERTIFICATO(String value) {
            this.numcertificato = value;
        }

        /**
         * Recupera il valore della proprietà numriflocale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMRIFLOCALE() {
            return numriflocale;
        }

        /**
         * Imposta il valore della proprietà numriflocale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMRIFLOCALE(String value) {
            this.numriflocale = value;
        }

        /**
         * Recupera il valore della proprietà dtmodello.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTMODELLO() {
            return dtmodello;
        }

        /**
         * Imposta il valore della proprietà dtmodello.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTMODELLO(XMLGregorianCalendar value) {
            this.dtmodello = value;
        }

        /**
         * Recupera il valore della proprietà codicestatopass.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICESTATOPASS() {
            return codicestatopass;
        }

        /**
         * Imposta il valore della proprietà codicestatopass.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICESTATOPASS(String value) {
            this.codicestatopass = value;
        }

        /**
         * Recupera il valore della proprietà codiceentepass.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEENTEPASS() {
            return codiceentepass;
        }

        /**
         * Imposta il valore della proprietà codiceentepass.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEENTEPASS(String value) {
            this.codiceentepass = value;
        }

        /**
         * Recupera il valore della proprietà dtrilasciopass.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTRILASCIOPASS() {
            return dtrilasciopass;
        }

        /**
         * Imposta il valore della proprietà dtrilasciopass.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTRILASCIOPASS(XMLGregorianCalendar value) {
            this.dtrilasciopass = value;
        }

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
         * Recupera il valore della proprietà meid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEID() {
            return meid;
        }

        /**
         * Imposta il valore della proprietà meid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEID(BigDecimal value) {
            this.meid = value;
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
         * Recupera il valore della proprietà dtinival.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINIVAL() {
            return dtinival;
        }

        /**
         * Imposta il valore della proprietà dtinival.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINIVAL(XMLGregorianCalendar value) {
            this.dtinival = value;
        }

        /**
         * Recupera il valore della proprietà dtfinval.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTFINVAL() {
            return dtfinval;
        }

        /**
         * Imposta il valore della proprietà dtfinval.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTFINVAL(XMLGregorianCalendar value) {
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
         * Recupera il valore della proprietà datainserimentobdn.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAINSERIMENTOBDN() {
            return datainserimentobdn;
        }

        /**
         * Imposta il valore della proprietà datainserimentobdn.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAINSERIMENTOBDN(XMLGregorianCalendar value) {
            this.datainserimentobdn = value;
        }

    }

}
