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
 * <p>Classe Java per ArrayOfRootDatiOVI_REGISTRI_PASCOLO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiOVI_REGISTRI_PASCOLO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OVI_REGISTRI_PASCOLO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="ASL_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CUAA_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COMUNE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ASL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INGRESSO_AL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_RIENTRO_DA_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ESTREMI_DOC_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_DOC_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_DESTINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ESTREMI_DOC_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_DOC_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CODICE_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_RAZZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiOVI_REGISTRI_PASCOLO", propOrder = {
    "oviregistripascolo"
})
public class ArrayOfRootDatiOVIREGISTRIPASCOLO {

    @XmlElement(name = "OVI_REGISTRI_PASCOLO")
    protected List<ArrayOfRootDatiOVIREGISTRIPASCOLO.OVIREGISTRIPASCOLO> oviregistripascolo;

    /**
     * Gets the value of the oviregistripascolo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the oviregistripascolo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOVIREGISTRIPASCOLO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiOVIREGISTRIPASCOLO.OVIREGISTRIPASCOLO }
     * 
     * 
     */
    public List<ArrayOfRootDatiOVIREGISTRIPASCOLO.OVIREGISTRIPASCOLO> getOVIREGISTRIPASCOLO() {
        if (oviregistripascolo == null) {
            oviregistripascolo = new ArrayList<ArrayOfRootDatiOVIREGISTRIPASCOLO.OVIREGISTRIPASCOLO>();
        }
        return this.oviregistripascolo;
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
     *         &lt;element name="ASL_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CUAA_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COMUNE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ASL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INGRESSO_AL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_RIENTRO_DA_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ESTREMI_DOC_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_DOC_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_DESTINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ESTREMI_DOC_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_DOC_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="CODICE_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_RAZZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "aslazienda",
        "codiceazienda",
        "allevidfiscale",
        "specodice",
        "cuaadetentore",
        "codicepascolo",
        "comunepascolo",
        "aslpascolo",
        "dtingressoalpascolo",
        "dtrientrodapascolo",
        "tipoprovenienza",
        "estremidocingresso",
        "dtdocingresso",
        "tipodestinazione",
        "estremidocuscita",
        "dtdocuscita",
        "capoid",
        "codicecapo",
        "codiceelettronico",
        "sesso",
        "codicerazza"
    })
    public static class OVIREGISTRIPASCOLO {

        @XmlElement(name = "ALLEV_ID", required = true)
        protected BigDecimal allevid;
        @XmlElement(name = "ASL_AZIENDA")
        protected String aslazienda;
        @XmlElement(name = "CODICE_AZIENDA")
        protected String codiceazienda;
        @XmlElement(name = "ALLEV_ID_FISCALE")
        protected String allevidfiscale;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "CUAA_DETENTORE")
        protected String cuaadetentore;
        @XmlElement(name = "CODICE_PASCOLO")
        protected String codicepascolo;
        @XmlElement(name = "COMUNE_PASCOLO")
        protected String comunepascolo;
        @XmlElement(name = "ASL_PASCOLO")
        protected String aslpascolo;
        @XmlElement(name = "DT_INGRESSO_AL_PASCOLO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtingressoalpascolo;
        @XmlElement(name = "DT_RIENTRO_DA_PASCOLO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtrientrodapascolo;
        @XmlElement(name = "TIPO_PROVENIENZA")
        protected String tipoprovenienza;
        @XmlElement(name = "ESTREMI_DOC_INGRESSO")
        protected String estremidocingresso;
        @XmlElement(name = "DT_DOC_INGRESSO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtdocingresso;
        @XmlElement(name = "TIPO_DESTINAZIONE")
        protected String tipodestinazione;
        @XmlElement(name = "ESTREMI_DOC_USCITA")
        protected String estremidocuscita;
        @XmlElement(name = "DT_DOC_USCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtdocuscita;
        @XmlElement(name = "CAPO_ID", required = true)
        protected BigDecimal capoid;
        @XmlElement(name = "CODICE_CAPO")
        protected String codicecapo;
        @XmlElement(name = "CODICE_ELETTRONICO")
        protected String codiceelettronico;
        @XmlElement(name = "SESSO")
        protected String sesso;
        @XmlElement(name = "CODICE_RAZZA")
        protected String codicerazza;

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
         * Recupera il valore della proprietà aslazienda.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASLAZIENDA() {
            return aslazienda;
        }

        /**
         * Imposta il valore della proprietà aslazienda.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASLAZIENDA(String value) {
            this.aslazienda = value;
        }

        /**
         * Recupera il valore della proprietà codiceazienda.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEAZIENDA() {
            return codiceazienda;
        }

        /**
         * Imposta il valore della proprietà codiceazienda.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEAZIENDA(String value) {
            this.codiceazienda = value;
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
         * Recupera il valore della proprietà cuaadetentore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCUAADETENTORE() {
            return cuaadetentore;
        }

        /**
         * Imposta il valore della proprietà cuaadetentore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCUAADETENTORE(String value) {
            this.cuaadetentore = value;
        }

        /**
         * Recupera il valore della proprietà codicepascolo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEPASCOLO() {
            return codicepascolo;
        }

        /**
         * Imposta il valore della proprietà codicepascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEPASCOLO(String value) {
            this.codicepascolo = value;
        }

        /**
         * Recupera il valore della proprietà comunepascolo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCOMUNEPASCOLO() {
            return comunepascolo;
        }

        /**
         * Imposta il valore della proprietà comunepascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCOMUNEPASCOLO(String value) {
            this.comunepascolo = value;
        }

        /**
         * Recupera il valore della proprietà aslpascolo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASLPASCOLO() {
            return aslpascolo;
        }

        /**
         * Imposta il valore della proprietà aslpascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASLPASCOLO(String value) {
            this.aslpascolo = value;
        }

        /**
         * Recupera il valore della proprietà dtingressoalpascolo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINGRESSOALPASCOLO() {
            return dtingressoalpascolo;
        }

        /**
         * Imposta il valore della proprietà dtingressoalpascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINGRESSOALPASCOLO(XMLGregorianCalendar value) {
            this.dtingressoalpascolo = value;
        }

        /**
         * Recupera il valore della proprietà dtrientrodapascolo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTRIENTRODAPASCOLO() {
            return dtrientrodapascolo;
        }

        /**
         * Imposta il valore della proprietà dtrientrodapascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTRIENTRODAPASCOLO(XMLGregorianCalendar value) {
            this.dtrientrodapascolo = value;
        }

        /**
         * Recupera il valore della proprietà tipoprovenienza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOPROVENIENZA() {
            return tipoprovenienza;
        }

        /**
         * Imposta il valore della proprietà tipoprovenienza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOPROVENIENZA(String value) {
            this.tipoprovenienza = value;
        }

        /**
         * Recupera il valore della proprietà estremidocingresso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getESTREMIDOCINGRESSO() {
            return estremidocingresso;
        }

        /**
         * Imposta il valore della proprietà estremidocingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setESTREMIDOCINGRESSO(String value) {
            this.estremidocingresso = value;
        }

        /**
         * Recupera il valore della proprietà dtdocingresso.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTDOCINGRESSO() {
            return dtdocingresso;
        }

        /**
         * Imposta il valore della proprietà dtdocingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTDOCINGRESSO(XMLGregorianCalendar value) {
            this.dtdocingresso = value;
        }

        /**
         * Recupera il valore della proprietà tipodestinazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPODESTINAZIONE() {
            return tipodestinazione;
        }

        /**
         * Imposta il valore della proprietà tipodestinazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPODESTINAZIONE(String value) {
            this.tipodestinazione = value;
        }

        /**
         * Recupera il valore della proprietà estremidocuscita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getESTREMIDOCUSCITA() {
            return estremidocuscita;
        }

        /**
         * Imposta il valore della proprietà estremidocuscita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setESTREMIDOCUSCITA(String value) {
            this.estremidocuscita = value;
        }

        /**
         * Recupera il valore della proprietà dtdocuscita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTDOCUSCITA() {
            return dtdocuscita;
        }

        /**
         * Imposta il valore della proprietà dtdocuscita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTDOCUSCITA(XMLGregorianCalendar value) {
            this.dtdocuscita = value;
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
         * Recupera il valore della proprietà codicecapo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICECAPO() {
            return codicecapo;
        }

        /**
         * Imposta il valore della proprietà codicecapo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICECAPO(String value) {
            this.codicecapo = value;
        }

        /**
         * Recupera il valore della proprietà codiceelettronico.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEELETTRONICO() {
            return codiceelettronico;
        }

        /**
         * Imposta il valore della proprietà codiceelettronico.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEELETTRONICO(String value) {
            this.codiceelettronico = value;
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
         * Recupera il valore della proprietà codicerazza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICERAZZA() {
            return codicerazza;
        }

        /**
         * Imposta il valore della proprietà codicerazza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICERAZZA(String value) {
            this.codicerazza = value;
        }

    }

}
