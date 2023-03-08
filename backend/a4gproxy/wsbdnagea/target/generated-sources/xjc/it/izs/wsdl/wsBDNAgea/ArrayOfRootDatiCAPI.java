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
 * <p>Classe Java per ArrayOfRootDatiCAPI complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCAPI"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAPI" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="COD_MADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_PADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_STATO_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_INSEMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RAZZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ST_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ST_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCAPI", propOrder = {
    "capi"
})
public class ArrayOfRootDatiCAPI {

    @XmlElement(name = "CAPI")
    protected List<ArrayOfRootDatiCAPI.CAPI> capi;

    /**
     * Gets the value of the capi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAPI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCAPI.CAPI }
     * 
     * 
     */
    public List<ArrayOfRootDatiCAPI.CAPI> getCAPI() {
        if (capi == null) {
            capi = new ArrayList<ArrayOfRootDatiCAPI.CAPI>();
        }
        return this.capi;
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
     *         &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="COD_MADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_PADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_STATO_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_INSEMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RAZZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ST_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ST_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
        "codice",
        "sesso",
        "dtnascita",
        "codmadre",
        "codpadre",
        "flagstatocapo",
        "flaginseminazione",
        "tipoorigine",
        "razzaid",
        "razzacodice",
        "stid",
        "stcodice",
        "speid",
        "specodice",
        "dtiscrizione"
    })
    public static class CAPI {

        @XmlElement(name = "CAPO_ID", required = true)
        protected BigDecimal capoid;
        @XmlElement(name = "CODICE")
        protected String codice;
        @XmlElement(name = "SESSO")
        protected String sesso;
        @XmlElement(name = "DT_NASCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtnascita;
        @XmlElement(name = "COD_MADRE")
        protected String codmadre;
        @XmlElement(name = "COD_PADRE")
        protected String codpadre;
        @XmlElement(name = "FLAG_STATO_CAPO")
        protected String flagstatocapo;
        @XmlElement(name = "FLAG_INSEMINAZIONE")
        protected String flaginseminazione;
        @XmlElement(name = "TIPO_ORIGINE")
        protected String tipoorigine;
        @XmlElement(name = "RAZZA_ID")
        protected BigDecimal razzaid;
        @XmlElement(name = "RAZZA_CODICE")
        protected String razzacodice;
        @XmlElement(name = "ST_ID")
        protected BigDecimal stid;
        @XmlElement(name = "ST_CODICE")
        protected String stcodice;
        @XmlElement(name = "SPE_ID")
        protected BigDecimal speid;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "DT_ISCRIZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtiscrizione;

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

    }

}
