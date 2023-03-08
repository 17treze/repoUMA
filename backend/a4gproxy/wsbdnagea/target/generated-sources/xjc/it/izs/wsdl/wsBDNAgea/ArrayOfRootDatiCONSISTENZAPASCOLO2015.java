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
 * <p>Classe Java per ArrayOfRootDatiCONSISTENZA_PASCOLO2015 complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCONSISTENZA_PASCOLO2015"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CONSISTENZA_PASCOLO2015" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="GRSPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FASCIA_ETA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="TOT_GG_PASCOLAMENTO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="PASCOLO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SIGLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ISTAT_COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCONSISTENZA_PASCOLO2015", propOrder = {
    "consistenzapascolo2015"
})
public class ArrayOfRootDatiCONSISTENZAPASCOLO2015 {

    @XmlElement(name = "CONSISTENZA_PASCOLO2015")
    protected List<ArrayOfRootDatiCONSISTENZAPASCOLO2015 .CONSISTENZAPASCOLO2015> consistenzapascolo2015;

    /**
     * Gets the value of the consistenzapascolo2015 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the consistenzapascolo2015 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCONSISTENZAPASCOLO2015().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCONSISTENZAPASCOLO2015 .CONSISTENZAPASCOLO2015 }
     * 
     * 
     */
    public List<ArrayOfRootDatiCONSISTENZAPASCOLO2015 .CONSISTENZAPASCOLO2015> getCONSISTENZAPASCOLO2015() {
        if (consistenzapascolo2015 == null) {
            consistenzapascolo2015 = new ArrayList<ArrayOfRootDatiCONSISTENZAPASCOLO2015 .CONSISTENZAPASCOLO2015>();
        }
        return this.consistenzapascolo2015;
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
     *         &lt;element name="GRSPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FASCIA_ETA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="TOT_GG_PASCOLAMENTO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="PASCOLO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SIGLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ISTAT_COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "grspecodice",
        "specodice",
        "spedescrizione",
        "fasciaeta",
        "numcapi",
        "totggpascolamento",
        "pascolocodice",
        "cuaa",
        "sigla",
        "istatcomune",
        "localita",
        "latitudine",
        "longitudine",
        "fogliocatastale",
        "particella",
        "sezione",
        "subalterno",
        "codfiscaledeten"
    })
    public static class CONSISTENZAPASCOLO2015 {

        @XmlElement(name = "GRSPE_CODICE")
        protected String grspecodice;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "SPE_DESCRIZIONE")
        protected String spedescrizione;
        @XmlElement(name = "FASCIA_ETA")
        protected String fasciaeta;
        @XmlElement(name = "NUM_CAPI")
        protected BigDecimal numcapi;
        @XmlElement(name = "TOT_GG_PASCOLAMENTO")
        protected BigDecimal totggpascolamento;
        @XmlElement(name = "PASCOLO_CODICE")
        protected String pascolocodice;
        @XmlElement(name = "CUAA")
        protected String cuaa;
        @XmlElement(name = "SIGLA")
        protected String sigla;
        @XmlElement(name = "ISTAT_COMUNE")
        protected String istatcomune;
        @XmlElement(name = "LOCALITA")
        protected String localita;
        @XmlElement(name = "LATITUDINE")
        protected BigDecimal latitudine;
        @XmlElement(name = "LONGITUDINE")
        protected BigDecimal longitudine;
        @XmlElement(name = "FOGLIO_CATASTALE")
        protected String fogliocatastale;
        @XmlElement(name = "PARTICELLA")
        protected String particella;
        @XmlElement(name = "SEZIONE")
        protected String sezione;
        @XmlElement(name = "SUBALTERNO")
        protected String subalterno;
        @XmlElement(name = "COD_FISCALE_DETEN")
        protected String codfiscaledeten;

        /**
         * Recupera il valore della proprietà grspecodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGRSPECODICE() {
            return grspecodice;
        }

        /**
         * Imposta il valore della proprietà grspecodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGRSPECODICE(String value) {
            this.grspecodice = value;
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
         * Recupera il valore della proprietà spedescrizione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPEDESCRIZIONE() {
            return spedescrizione;
        }

        /**
         * Imposta il valore della proprietà spedescrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPEDESCRIZIONE(String value) {
            this.spedescrizione = value;
        }

        /**
         * Recupera il valore della proprietà fasciaeta.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFASCIAETA() {
            return fasciaeta;
        }

        /**
         * Imposta il valore della proprietà fasciaeta.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFASCIAETA(String value) {
            this.fasciaeta = value;
        }

        /**
         * Recupera il valore della proprietà numcapi.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPI() {
            return numcapi;
        }

        /**
         * Imposta il valore della proprietà numcapi.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPI(BigDecimal value) {
            this.numcapi = value;
        }

        /**
         * Recupera il valore della proprietà totggpascolamento.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTOTGGPASCOLAMENTO() {
            return totggpascolamento;
        }

        /**
         * Imposta il valore della proprietà totggpascolamento.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTOTGGPASCOLAMENTO(BigDecimal value) {
            this.totggpascolamento = value;
        }

        /**
         * Recupera il valore della proprietà pascolocodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPASCOLOCODICE() {
            return pascolocodice;
        }

        /**
         * Imposta il valore della proprietà pascolocodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPASCOLOCODICE(String value) {
            this.pascolocodice = value;
        }

        /**
         * Recupera il valore della proprietà cuaa.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCUAA() {
            return cuaa;
        }

        /**
         * Imposta il valore della proprietà cuaa.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCUAA(String value) {
            this.cuaa = value;
        }

        /**
         * Recupera il valore della proprietà sigla.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSIGLA() {
            return sigla;
        }

        /**
         * Imposta il valore della proprietà sigla.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSIGLA(String value) {
            this.sigla = value;
        }

        /**
         * Recupera il valore della proprietà istatcomune.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getISTATCOMUNE() {
            return istatcomune;
        }

        /**
         * Imposta il valore della proprietà istatcomune.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setISTATCOMUNE(String value) {
            this.istatcomune = value;
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

    }

}
