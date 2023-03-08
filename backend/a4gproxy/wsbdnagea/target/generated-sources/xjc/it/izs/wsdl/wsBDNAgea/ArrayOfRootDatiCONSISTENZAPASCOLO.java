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
 * <p>Classe Java per ArrayOfRootDatiCONSISTENZA_PASCOLO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCONSISTENZA_PASCOLO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CONSISTENZA_PASCOLO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="PAS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="COD_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SIGLA_PROVINCIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ISTAT_COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_6_24_MESI_50GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_OLTRE_24_MESI_50GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_6_24_MESI_90GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_OLTRE_24_MESI_90GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_6_24_MESI_150GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_OLTRE_24_MESI_150GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCONSISTENZA_PASCOLO", propOrder = {
    "consistenzapascolo"
})
public class ArrayOfRootDatiCONSISTENZAPASCOLO {

    @XmlElement(name = "CONSISTENZA_PASCOLO")
    protected List<ArrayOfRootDatiCONSISTENZAPASCOLO.CONSISTENZAPASCOLO> consistenzapascolo;

    /**
     * Gets the value of the consistenzapascolo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the consistenzapascolo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCONSISTENZAPASCOLO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCONSISTENZAPASCOLO.CONSISTENZAPASCOLO }
     * 
     * 
     */
    public List<ArrayOfRootDatiCONSISTENZAPASCOLO.CONSISTENZAPASCOLO> getCONSISTENZAPASCOLO() {
        if (consistenzapascolo == null) {
            consistenzapascolo = new ArrayList<ArrayOfRootDatiCONSISTENZAPASCOLO.CONSISTENZAPASCOLO>();
        }
        return this.consistenzapascolo;
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
     *         &lt;element name="PAS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="COD_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SIGLA_PROVINCIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ISTAT_COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_DETEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_6_24_MESI_50GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_OLTRE_24_MESI_50GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_6_24_MESI_90GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_OLTRE_24_MESI_90GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_6_24_MESI_150GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_OLTRE_24_MESI_150GG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pasid",
        "codpascolo",
        "siglaprovincia",
        "istatcomune",
        "localita",
        "codfiscaledeten",
        "aziendacodice",
        "capi624MESI50GG",
        "capioltre24MESI50GG",
        "capi624MESI90GG",
        "capioltre24MESI90GG",
        "capi624MESI150GG",
        "capioltre24MESI150GG"
    })
    public static class CONSISTENZAPASCOLO {

        @XmlElement(name = "PAS_ID", required = true)
        protected BigDecimal pasid;
        @XmlElement(name = "COD_PASCOLO")
        protected String codpascolo;
        @XmlElement(name = "SIGLA_PROVINCIA")
        protected String siglaprovincia;
        @XmlElement(name = "ISTAT_COMUNE")
        protected String istatcomune;
        @XmlElement(name = "LOCALITA")
        protected String localita;
        @XmlElement(name = "COD_FISCALE_DETEN")
        protected String codfiscaledeten;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "CAPI_6_24_MESI_50GG")
        protected String capi624MESI50GG;
        @XmlElement(name = "CAPI_OLTRE_24_MESI_50GG")
        protected String capioltre24MESI50GG;
        @XmlElement(name = "CAPI_6_24_MESI_90GG")
        protected String capi624MESI90GG;
        @XmlElement(name = "CAPI_OLTRE_24_MESI_90GG")
        protected String capioltre24MESI90GG;
        @XmlElement(name = "CAPI_6_24_MESI_150GG")
        protected String capi624MESI150GG;
        @XmlElement(name = "CAPI_OLTRE_24_MESI_150GG")
        protected String capioltre24MESI150GG;

        /**
         * Recupera il valore della proprietà pasid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPASID() {
            return pasid;
        }

        /**
         * Imposta il valore della proprietà pasid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPASID(BigDecimal value) {
            this.pasid = value;
        }

        /**
         * Recupera il valore della proprietà codpascolo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODPASCOLO() {
            return codpascolo;
        }

        /**
         * Imposta il valore della proprietà codpascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODPASCOLO(String value) {
            this.codpascolo = value;
        }

        /**
         * Recupera il valore della proprietà siglaprovincia.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSIGLAPROVINCIA() {
            return siglaprovincia;
        }

        /**
         * Imposta il valore della proprietà siglaprovincia.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSIGLAPROVINCIA(String value) {
            this.siglaprovincia = value;
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
         * Recupera il valore della proprietà capi624MESI50GG.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPI624MESI50GG() {
            return capi624MESI50GG;
        }

        /**
         * Imposta il valore della proprietà capi624MESI50GG.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPI624MESI50GG(String value) {
            this.capi624MESI50GG = value;
        }

        /**
         * Recupera il valore della proprietà capioltre24MESI50GG.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIOLTRE24MESI50GG() {
            return capioltre24MESI50GG;
        }

        /**
         * Imposta il valore della proprietà capioltre24MESI50GG.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIOLTRE24MESI50GG(String value) {
            this.capioltre24MESI50GG = value;
        }

        /**
         * Recupera il valore della proprietà capi624MESI90GG.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPI624MESI90GG() {
            return capi624MESI90GG;
        }

        /**
         * Imposta il valore della proprietà capi624MESI90GG.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPI624MESI90GG(String value) {
            this.capi624MESI90GG = value;
        }

        /**
         * Recupera il valore della proprietà capioltre24MESI90GG.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIOLTRE24MESI90GG() {
            return capioltre24MESI90GG;
        }

        /**
         * Imposta il valore della proprietà capioltre24MESI90GG.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIOLTRE24MESI90GG(String value) {
            this.capioltre24MESI90GG = value;
        }

        /**
         * Recupera il valore della proprietà capi624MESI150GG.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPI624MESI150GG() {
            return capi624MESI150GG;
        }

        /**
         * Imposta il valore della proprietà capi624MESI150GG.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPI624MESI150GG(String value) {
            this.capi624MESI150GG = value;
        }

        /**
         * Recupera il valore della proprietà capioltre24MESI150GG.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIOLTRE24MESI150GG() {
            return capioltre24MESI150GG;
        }

        /**
         * Imposta il valore della proprietà capioltre24MESI150GG.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIOLTRE24MESI150GG(String value) {
            this.capioltre24MESI150GG = value;
        }

    }

}
