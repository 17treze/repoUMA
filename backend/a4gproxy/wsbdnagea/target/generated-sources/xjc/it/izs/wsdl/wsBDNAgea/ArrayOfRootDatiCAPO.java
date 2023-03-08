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
 * <p>Classe Java per ArrayOfRootDatiCAPO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCAPO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAPO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CAPO_LIBRO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LG_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_VARIAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="REF_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NOTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_INSERIMENTO_BDN" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCAPO", propOrder = {
    "capo"
})
public class ArrayOfRootDatiCAPO {

    @XmlElement(name = "CAPO")
    protected List<ArrayOfRootDatiCAPO.CAPO> capo;

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
     * {@link ArrayOfRootDatiCAPO.CAPO }
     * 
     * 
     */
    public List<ArrayOfRootDatiCAPO.CAPO> getCAPO() {
        if (capo == null) {
            capo = new ArrayList<ArrayOfRootDatiCAPO.CAPO>();
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
     *         &lt;element name="CAPO_LIBRO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LG_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINVAL" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_VARIAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="REF_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NOTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DATA_INSERIMENTO_BDN" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
        "capolibroid",
        "capoid",
        "codicecapo",
        "lgid",
        "lgcodice",
        "dtiscrizione",
        "dtrevoca",
        "dtinival",
        "dtfinval",
        "tipovariazione",
        "refid",
        "note",
        "utenteid",
        "datainserimentobdn"
    })
    public static class CAPO {

        @XmlElement(name = "CAPO_LIBRO_ID", required = true)
        protected BigDecimal capolibroid;
        @XmlElement(name = "CAPO_ID")
        protected BigDecimal capoid;
        @XmlElement(name = "CODICE_CAPO")
        protected String codicecapo;
        @XmlElement(name = "LG_ID")
        protected BigDecimal lgid;
        @XmlElement(name = "LG_CODICE")
        protected String lgcodice;
        @XmlElement(name = "DT_ISCRIZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtiscrizione;
        @XmlElement(name = "DT_REVOCA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtrevoca;
        @XmlElement(name = "DT_INIVAL")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtinival;
        @XmlElement(name = "DT_FINVAL")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtfinval;
        @XmlElement(name = "TIPO_VARIAZIONE")
        protected String tipovariazione;
        @XmlElement(name = "REF_ID")
        protected BigDecimal refid;
        @XmlElement(name = "NOTE")
        protected String note;
        @XmlElement(name = "UTENTE_ID")
        protected BigDecimal utenteid;
        @XmlElement(name = "DATA_INSERIMENTO_BDN")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datainserimentobdn;

        /**
         * Recupera il valore della proprietà capolibroid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPOLIBROID() {
            return capolibroid;
        }

        /**
         * Imposta il valore della proprietà capolibroid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPOLIBROID(BigDecimal value) {
            this.capolibroid = value;
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
