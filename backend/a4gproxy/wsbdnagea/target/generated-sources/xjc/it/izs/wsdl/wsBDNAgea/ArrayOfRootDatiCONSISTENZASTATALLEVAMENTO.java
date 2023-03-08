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
 * <p>Classe Java per ArrayOfRootDatiCONSISTENZA_STAT_ALLEVAMENTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCONSISTENZA_STAT_ALLEVAMENTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CONSISTENZA_STAT_ALLEVAMENTO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_DT_INIZIO_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_DT_FINE_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MEDIA_MASCHI_BOV_0_12MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="MEDIA_MASCHI_BUF_0_12MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="MEDIA_FEMMINE_BOV_0_12MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="MEDIA_FEMMINE_BUF_0_12MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="MEDIA_MASCHI_BOV_12_24MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="MEDIA_MASCHI_BUF_12_24MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="MEDIA_FEMMINE_BOV_12_24MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="MEDIA_FEMMINE_BUF_12_24MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="MEDIA_CAPI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="MAX_DATA_CALCOLO_CAPI" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="MIN_DATA_CALCOLO_CAPI" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_ELABORAZIONI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCONSISTENZA_STAT_ALLEVAMENTO", propOrder = {
    "consistenzastatallevamento"
})
public class ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO {

    @XmlElement(name = "CONSISTENZA_STAT_ALLEVAMENTO")
    protected List<ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO.CONSISTENZASTATALLEVAMENTO> consistenzastatallevamento;

    /**
     * Gets the value of the consistenzastatallevamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the consistenzastatallevamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCONSISTENZASTATALLEVAMENTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO.CONSISTENZASTATALLEVAMENTO }
     * 
     * 
     */
    public List<ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO.CONSISTENZASTATALLEVAMENTO> getCONSISTENZASTATALLEVAMENTO() {
        if (consistenzastatallevamento == null) {
            consistenzastatallevamento = new ArrayList<ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO.CONSISTENZASTATALLEVAMENTO>();
        }
        return this.consistenzastatallevamento;
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
     *         &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_DT_INIZIO_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_DT_FINE_ATTIVITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MEDIA_MASCHI_BOV_0_12MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="MEDIA_MASCHI_BUF_0_12MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="MEDIA_FEMMINE_BOV_0_12MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="MEDIA_FEMMINE_BUF_0_12MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="MEDIA_MASCHI_BOV_12_24MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="MEDIA_MASCHI_BUF_12_24MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="MEDIA_FEMMINE_BOV_12_24MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="MEDIA_FEMMINE_BUF_12_24MESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="MEDIA_CAPI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="MAX_DATA_CALCOLO_CAPI" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="MIN_DATA_CALCOLO_CAPI" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="NUM_ELABORAZIONI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
        "pallevid",
        "aziendacodice",
        "specodice",
        "allevdtinizioattivita",
        "allevdtfineattivita",
        "codfiscaleprop",
        "codfiscaledete",
        "mediamaschibov012MESI",
        "mediamaschibuf012MESI",
        "mediafemminebov012MESI",
        "mediafemminebuf012MESI",
        "mediamaschibov1224MESI",
        "mediamaschibuf1224MESI",
        "mediafemminebov1224MESI",
        "mediafemminebuf1224MESI",
        "mediacapitotali",
        "maxdatacalcolocapi",
        "mindatacalcolocapi",
        "numelaborazioni",
        "dtinizioperiodo",
        "dtfineperiodo"
    })
    public static class CONSISTENZASTATALLEVAMENTO {

        @XmlElement(name = "P_ALLEV_ID", required = true)
        protected BigDecimal pallevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "ALLEV_DT_INIZIO_ATTIVITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar allevdtinizioattivita;
        @XmlElement(name = "ALLEV_DT_FINE_ATTIVITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar allevdtfineattivita;
        @XmlElement(name = "COD_FISCALE_PROP")
        protected String codfiscaleprop;
        @XmlElement(name = "COD_FISCALE_DETE")
        protected String codfiscaledete;
        @XmlElement(name = "MEDIA_MASCHI_BOV_0_12MESI")
        protected BigDecimal mediamaschibov012MESI;
        @XmlElement(name = "MEDIA_MASCHI_BUF_0_12MESI")
        protected BigDecimal mediamaschibuf012MESI;
        @XmlElement(name = "MEDIA_FEMMINE_BOV_0_12MESI")
        protected BigDecimal mediafemminebov012MESI;
        @XmlElement(name = "MEDIA_FEMMINE_BUF_0_12MESI")
        protected BigDecimal mediafemminebuf012MESI;
        @XmlElement(name = "MEDIA_MASCHI_BOV_12_24MESI")
        protected BigDecimal mediamaschibov1224MESI;
        @XmlElement(name = "MEDIA_MASCHI_BUF_12_24MESI")
        protected BigDecimal mediamaschibuf1224MESI;
        @XmlElement(name = "MEDIA_FEMMINE_BOV_12_24MESI")
        protected BigDecimal mediafemminebov1224MESI;
        @XmlElement(name = "MEDIA_FEMMINE_BUF_12_24MESI")
        protected BigDecimal mediafemminebuf1224MESI;
        @XmlElement(name = "MEDIA_CAPI_TOTALI")
        protected BigDecimal mediacapitotali;
        @XmlElement(name = "MAX_DATA_CALCOLO_CAPI")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar maxdatacalcolocapi;
        @XmlElement(name = "MIN_DATA_CALCOLO_CAPI")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar mindatacalcolocapi;
        @XmlElement(name = "NUM_ELABORAZIONI")
        protected BigDecimal numelaborazioni;
        @XmlElement(name = "DT_INIZIO_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtinizioperiodo;
        @XmlElement(name = "DT_FINE_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtfineperiodo;

        /**
         * Recupera il valore della proprietà pallevid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPALLEVID() {
            return pallevid;
        }

        /**
         * Imposta il valore della proprietà pallevid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPALLEVID(BigDecimal value) {
            this.pallevid = value;
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
         * Recupera il valore della proprietà allevdtinizioattivita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getALLEVDTINIZIOATTIVITA() {
            return allevdtinizioattivita;
        }

        /**
         * Imposta il valore della proprietà allevdtinizioattivita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setALLEVDTINIZIOATTIVITA(XMLGregorianCalendar value) {
            this.allevdtinizioattivita = value;
        }

        /**
         * Recupera il valore della proprietà allevdtfineattivita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getALLEVDTFINEATTIVITA() {
            return allevdtfineattivita;
        }

        /**
         * Imposta il valore della proprietà allevdtfineattivita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setALLEVDTFINEATTIVITA(XMLGregorianCalendar value) {
            this.allevdtfineattivita = value;
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
         * Recupera il valore della proprietà codfiscaledete.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODFISCALEDETE() {
            return codfiscaledete;
        }

        /**
         * Imposta il valore della proprietà codfiscaledete.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODFISCALEDETE(String value) {
            this.codfiscaledete = value;
        }

        /**
         * Recupera il valore della proprietà mediamaschibov012MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEDIAMASCHIBOV012MESI() {
            return mediamaschibov012MESI;
        }

        /**
         * Imposta il valore della proprietà mediamaschibov012MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEDIAMASCHIBOV012MESI(BigDecimal value) {
            this.mediamaschibov012MESI = value;
        }

        /**
         * Recupera il valore della proprietà mediamaschibuf012MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEDIAMASCHIBUF012MESI() {
            return mediamaschibuf012MESI;
        }

        /**
         * Imposta il valore della proprietà mediamaschibuf012MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEDIAMASCHIBUF012MESI(BigDecimal value) {
            this.mediamaschibuf012MESI = value;
        }

        /**
         * Recupera il valore della proprietà mediafemminebov012MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEDIAFEMMINEBOV012MESI() {
            return mediafemminebov012MESI;
        }

        /**
         * Imposta il valore della proprietà mediafemminebov012MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEDIAFEMMINEBOV012MESI(BigDecimal value) {
            this.mediafemminebov012MESI = value;
        }

        /**
         * Recupera il valore della proprietà mediafemminebuf012MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEDIAFEMMINEBUF012MESI() {
            return mediafemminebuf012MESI;
        }

        /**
         * Imposta il valore della proprietà mediafemminebuf012MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEDIAFEMMINEBUF012MESI(BigDecimal value) {
            this.mediafemminebuf012MESI = value;
        }

        /**
         * Recupera il valore della proprietà mediamaschibov1224MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEDIAMASCHIBOV1224MESI() {
            return mediamaschibov1224MESI;
        }

        /**
         * Imposta il valore della proprietà mediamaschibov1224MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEDIAMASCHIBOV1224MESI(BigDecimal value) {
            this.mediamaschibov1224MESI = value;
        }

        /**
         * Recupera il valore della proprietà mediamaschibuf1224MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEDIAMASCHIBUF1224MESI() {
            return mediamaschibuf1224MESI;
        }

        /**
         * Imposta il valore della proprietà mediamaschibuf1224MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEDIAMASCHIBUF1224MESI(BigDecimal value) {
            this.mediamaschibuf1224MESI = value;
        }

        /**
         * Recupera il valore della proprietà mediafemminebov1224MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEDIAFEMMINEBOV1224MESI() {
            return mediafemminebov1224MESI;
        }

        /**
         * Imposta il valore della proprietà mediafemminebov1224MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEDIAFEMMINEBOV1224MESI(BigDecimal value) {
            this.mediafemminebov1224MESI = value;
        }

        /**
         * Recupera il valore della proprietà mediafemminebuf1224MESI.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEDIAFEMMINEBUF1224MESI() {
            return mediafemminebuf1224MESI;
        }

        /**
         * Imposta il valore della proprietà mediafemminebuf1224MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEDIAFEMMINEBUF1224MESI(BigDecimal value) {
            this.mediafemminebuf1224MESI = value;
        }

        /**
         * Recupera il valore della proprietà mediacapitotali.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMEDIACAPITOTALI() {
            return mediacapitotali;
        }

        /**
         * Imposta il valore della proprietà mediacapitotali.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMEDIACAPITOTALI(BigDecimal value) {
            this.mediacapitotali = value;
        }

        /**
         * Recupera il valore della proprietà maxdatacalcolocapi.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getMAXDATACALCOLOCAPI() {
            return maxdatacalcolocapi;
        }

        /**
         * Imposta il valore della proprietà maxdatacalcolocapi.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setMAXDATACALCOLOCAPI(XMLGregorianCalendar value) {
            this.maxdatacalcolocapi = value;
        }

        /**
         * Recupera il valore della proprietà mindatacalcolocapi.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getMINDATACALCOLOCAPI() {
            return mindatacalcolocapi;
        }

        /**
         * Imposta il valore della proprietà mindatacalcolocapi.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setMINDATACALCOLOCAPI(XMLGregorianCalendar value) {
            this.mindatacalcolocapi = value;
        }

        /**
         * Recupera il valore della proprietà numelaborazioni.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMELABORAZIONI() {
            return numelaborazioni;
        }

        /**
         * Imposta il valore della proprietà numelaborazioni.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMELABORAZIONI(BigDecimal value) {
            this.numelaborazioni = value;
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

    }

}
