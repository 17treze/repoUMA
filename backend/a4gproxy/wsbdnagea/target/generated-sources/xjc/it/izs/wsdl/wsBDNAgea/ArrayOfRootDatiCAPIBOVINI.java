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
 * <p>Classe Java per ArrayOfRootDatiCAPI_BOVINI complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCAPI_BOVINI"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAPI_BOVINI" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="COD_MADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_PRECEDENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_COMPILAZIONE_CEDOLA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_STATO_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_REPERIMENTO_INFO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCAPI_BOVINI", propOrder = {
    "capibovini"
})
public class ArrayOfRootDatiCAPIBOVINI {

    @XmlElement(name = "CAPI_BOVINI")
    protected List<ArrayOfRootDatiCAPIBOVINI.CAPIBOVINI> capibovini;

    /**
     * Gets the value of the capibovini property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capibovini property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAPIBOVINI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCAPIBOVINI.CAPIBOVINI }
     * 
     * 
     */
    public List<ArrayOfRootDatiCAPIBOVINI.CAPIBOVINI> getCAPIBOVINI() {
        if (capibovini == null) {
            capibovini = new ArrayList<ArrayOfRootDatiCAPIBOVINI.CAPIBOVINI>();
        }
        return this.capibovini;
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
     *         &lt;element name="COD_PRECEDENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_COMPILAZIONE_CEDOLA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_STATO_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_REPERIMENTO_INFO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
        "codprecedente",
        "dtcompilazionecedola",
        "flagstatocapo",
        "tipoorigine",
        "razzacodice",
        "lgcodice",
        "dtiscrizione",
        "dtingresso",
        "dtreperimentoinfo"
    })
    public static class CAPIBOVINI {

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
        @XmlElement(name = "COD_PRECEDENTE")
        protected String codprecedente;
        @XmlElement(name = "DT_COMPILAZIONE_CEDOLA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtcompilazionecedola;
        @XmlElement(name = "FLAG_STATO_CAPO")
        protected String flagstatocapo;
        @XmlElement(name = "TIPO_ORIGINE")
        protected String tipoorigine;
        @XmlElement(name = "RAZZA_CODICE")
        protected String razzacodice;
        @XmlElement(name = "LG_CODICE")
        protected String lgcodice;
        @XmlElement(name = "DT_ISCRIZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtiscrizione;
        @XmlElement(name = "DT_INGRESSO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtingresso;
        @XmlElement(name = "DT_REPERIMENTO_INFO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtreperimentoinfo;

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
         * Recupera il valore della proprietà codprecedente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODPRECEDENTE() {
            return codprecedente;
        }

        /**
         * Imposta il valore della proprietà codprecedente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODPRECEDENTE(String value) {
            this.codprecedente = value;
        }

        /**
         * Recupera il valore della proprietà dtcompilazionecedola.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTCOMPILAZIONECEDOLA() {
            return dtcompilazionecedola;
        }

        /**
         * Imposta il valore della proprietà dtcompilazionecedola.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTCOMPILAZIONECEDOLA(XMLGregorianCalendar value) {
            this.dtcompilazionecedola = value;
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
         * Recupera il valore della proprietà dtreperimentoinfo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTREPERIMENTOINFO() {
            return dtreperimentoinfo;
        }

        /**
         * Imposta il valore della proprietà dtreperimentoinfo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTREPERIMENTOINFO(XMLGregorianCalendar value) {
            this.dtreperimentoinfo = value;
        }

    }

}
