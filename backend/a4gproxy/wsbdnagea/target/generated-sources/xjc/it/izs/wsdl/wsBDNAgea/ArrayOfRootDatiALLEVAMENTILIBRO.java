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
 * <p>Classe Java per ArrayOfRootDatiALLEVAMENTI_LIBRO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiALLEVAMENTI_LIBRO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ALLEVAMENTI_LIBRO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ALLEV_LIBRO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="ASSOCIAZIONE_RAZZA_COD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ASSOCIAZIONE_RAZZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_LGU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ID_LGU" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DENOMINAZIONE_LGU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_VARIAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="REF_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NOTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiALLEVAMENTI_LIBRO", propOrder = {
    "allevamentilibro"
})
public class ArrayOfRootDatiALLEVAMENTILIBRO {

    @XmlElement(name = "ALLEVAMENTI_LIBRO")
    protected List<ArrayOfRootDatiALLEVAMENTILIBRO.ALLEVAMENTILIBRO> allevamentilibro;

    /**
     * Gets the value of the allevamentilibro property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the allevamentilibro property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALLEVAMENTILIBRO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiALLEVAMENTILIBRO.ALLEVAMENTILIBRO }
     * 
     * 
     */
    public List<ArrayOfRootDatiALLEVAMENTILIBRO.ALLEVAMENTILIBRO> getALLEVAMENTILIBRO() {
        if (allevamentilibro == null) {
            allevamentilibro = new ArrayList<ArrayOfRootDatiALLEVAMENTILIBRO.ALLEVAMENTILIBRO>();
        }
        return this.allevamentilibro;
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
     *         &lt;element name="ALLEV_LIBRO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="ASSOCIAZIONE_RAZZA_COD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ASSOCIAZIONE_RAZZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_LGU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ID_LGU" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DENOMINAZIONE_LGU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_VARIAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="REF_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NOTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
        "allevlibroid",
        "aziendaid",
        "aziendacodice",
        "speid",
        "specodice",
        "allevidfiscale",
        "allevid",
        "dtiscrizione",
        "dtrevoca",
        "associazionerazzacod",
        "associazionerazzaid",
        "codicelgu",
        "idlgu",
        "denominazionelgu",
        "tipovariazione",
        "dtinival",
        "dtfinval",
        "refid",
        "note",
        "utenteid"
    })
    public static class ALLEVAMENTILIBRO {

        @XmlElement(name = "ALLEV_LIBRO_ID", required = true)
        protected BigDecimal allevlibroid;
        @XmlElement(name = "AZIENDA_ID")
        protected BigDecimal aziendaid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "SPE_ID")
        protected BigDecimal speid;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "ALLEV_ID_FISCALE")
        protected String allevidfiscale;
        @XmlElement(name = "ALLEV_ID")
        protected BigDecimal allevid;
        @XmlElement(name = "DT_ISCRIZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtiscrizione;
        @XmlElement(name = "DT_REVOCA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtrevoca;
        @XmlElement(name = "ASSOCIAZIONE_RAZZA_COD")
        protected String associazionerazzacod;
        @XmlElement(name = "ASSOCIAZIONE_RAZZA_ID")
        protected BigDecimal associazionerazzaid;
        @XmlElement(name = "CODICE_LGU")
        protected String codicelgu;
        @XmlElement(name = "ID_LGU")
        protected BigDecimal idlgu;
        @XmlElement(name = "DENOMINAZIONE_LGU")
        protected String denominazionelgu;
        @XmlElement(name = "TIPO_VARIAZIONE")
        protected String tipovariazione;
        @XmlElement(name = "DT_INIVAL")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtinival;
        @XmlElement(name = "DT_FINVAL")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtfinval;
        @XmlElement(name = "REF_ID")
        protected BigDecimal refid;
        @XmlElement(name = "NOTE")
        protected String note;
        @XmlElement(name = "UTENTE_ID")
        protected BigDecimal utenteid;

        /**
         * Recupera il valore della proprietà allevlibroid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getALLEVLIBROID() {
            return allevlibroid;
        }

        /**
         * Imposta il valore della proprietà allevlibroid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setALLEVLIBROID(BigDecimal value) {
            this.allevlibroid = value;
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
         * Recupera il valore della proprietà speid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getSPEID() {
            return speid;
        }

        /**
         * Imposta il valore della proprietà speid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setSPEID(BigDecimal value) {
            this.speid = value;
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
         * Recupera il valore della proprietà dtiscrizione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTISCRIZIONE() {
            return dtiscrizione;
        }

        /**
         * Imposta il valore della proprietà dtiscrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTISCRIZIONE(XMLGregorianCalendar value) {
            this.dtiscrizione = value;
        }

        /**
         * Recupera il valore della proprietà dtrevoca.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTREVOCA() {
            return dtrevoca;
        }

        /**
         * Imposta il valore della proprietà dtrevoca.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTREVOCA(XMLGregorianCalendar value) {
            this.dtrevoca = value;
        }

        /**
         * Recupera il valore della proprietà associazionerazzacod.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASSOCIAZIONERAZZACOD() {
            return associazionerazzacod;
        }

        /**
         * Imposta il valore della proprietà associazionerazzacod.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASSOCIAZIONERAZZACOD(String value) {
            this.associazionerazzacod = value;
        }

        /**
         * Recupera il valore della proprietà associazionerazzaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getASSOCIAZIONERAZZAID() {
            return associazionerazzaid;
        }

        /**
         * Imposta il valore della proprietà associazionerazzaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setASSOCIAZIONERAZZAID(BigDecimal value) {
            this.associazionerazzaid = value;
        }

        /**
         * Recupera il valore della proprietà codicelgu.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICELGU() {
            return codicelgu;
        }

        /**
         * Imposta il valore della proprietà codicelgu.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICELGU(String value) {
            this.codicelgu = value;
        }

        /**
         * Recupera il valore della proprietà idlgu.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getIDLGU() {
            return idlgu;
        }

        /**
         * Imposta il valore della proprietà idlgu.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setIDLGU(BigDecimal value) {
            this.idlgu = value;
        }

        /**
         * Recupera il valore della proprietà denominazionelgu.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDENOMINAZIONELGU() {
            return denominazionelgu;
        }

        /**
         * Imposta il valore della proprietà denominazionelgu.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDENOMINAZIONELGU(String value) {
            this.denominazionelgu = value;
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

    }

}
