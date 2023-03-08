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
 * <p>Classe Java per ArrayOfRootDatiUBA_CENSIMENTO_OVINO_2012 complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiUBA_CENSIMENTO_OVINO_2012"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UBA_CENSIMENTO_OVINO_2012" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CENSIMENTO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="O_MASCHI_ADULTI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="O_MASCHI_ADULTI_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="O_FEMMINE_ADULTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="O_FEMMINE_ADULTE_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="O_MASCHI_RIMONTA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="O_MASCHI_RIMONTA_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="O_FEMMINE_RIMONTA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="O_FEMMINE_RIMONTA_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="O_CAPI_TOT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="O_AGNELLI_MAC_TOT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_MASCHI_ADULTI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_MASCHI_ADULTI_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_FEMMINE_ADULTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_FEMMINE_ADULTE_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_MASCHI_RIMONTA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_MASCHI_RIMONTA_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_FEMMINE_RIMONTA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_FEMMINE_RIMONTA_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_CAPI_TOT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="C_CAPRETTI_MAC_TOT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_CENSIMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_COM_AUTORITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_QUALIFICA_SCRAPIE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DESCR_QUALIFICA_SCRAPIE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_RILEVAZIONE_QUALIFICA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiUBA_CENSIMENTO_OVINO_2012", propOrder = {
    "ubacensimentoovino2012"
})
public class ArrayOfRootDatiUBACENSIMENTOOVINO2012 {

    @XmlElement(name = "UBA_CENSIMENTO_OVINO_2012")
    protected List<ArrayOfRootDatiUBACENSIMENTOOVINO2012 .UBACENSIMENTOOVINO2012> ubacensimentoovino2012;

    /**
     * Gets the value of the ubacensimentoovino2012 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ubacensimentoovino2012 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUBACENSIMENTOOVINO2012().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiUBACENSIMENTOOVINO2012 .UBACENSIMENTOOVINO2012 }
     * 
     * 
     */
    public List<ArrayOfRootDatiUBACENSIMENTOOVINO2012 .UBACENSIMENTOOVINO2012> getUBACENSIMENTOOVINO2012() {
        if (ubacensimentoovino2012 == null) {
            ubacensimentoovino2012 = new ArrayList<ArrayOfRootDatiUBACENSIMENTOOVINO2012 .UBACENSIMENTOOVINO2012>();
        }
        return this.ubacensimentoovino2012;
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
     *         &lt;element name="CENSIMENTO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DATA_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="O_MASCHI_ADULTI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="O_MASCHI_ADULTI_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="O_FEMMINE_ADULTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="O_FEMMINE_ADULTE_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="O_MASCHI_RIMONTA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="O_MASCHI_RIMONTA_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="O_FEMMINE_RIMONTA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="O_FEMMINE_RIMONTA_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="O_CAPI_TOT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="O_AGNELLI_MAC_TOT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_MASCHI_ADULTI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_MASCHI_ADULTI_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_FEMMINE_ADULTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_FEMMINE_ADULTE_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_MASCHI_RIMONTA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_MASCHI_RIMONTA_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_FEMMINE_RIMONTA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_FEMMINE_RIMONTA_LIB" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_CAPI_TOT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="C_CAPRETTI_MAC_TOT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DATA_CENSIMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_COM_AUTORITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_QUALIFICA_SCRAPIE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DESCR_QUALIFICA_SCRAPIE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_RILEVAZIONE_QUALIFICA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
        "censimentoid",
        "pallevid",
        "aziendacodice",
        "codfiscaleprop",
        "codfiscaledete",
        "speciecodice",
        "speciedescrizione",
        "datainizioperiodo",
        "datafineperiodo",
        "omaschiadulti",
        "omaschiadultilib",
        "ofemmineadulte",
        "ofemmineadultelib",
        "omaschirimonta",
        "omaschirimontalib",
        "ofemminerimonta",
        "ofemminerimontalib",
        "ocapitot",
        "oagnellimactot",
        "cmaschiadulti",
        "cmaschiadultilib",
        "cfemmineadulte",
        "cfemmineadultelib",
        "cmaschirimonta",
        "cmaschirimontalib",
        "cfemminerimonta",
        "cfemminerimontalib",
        "ccapitot",
        "ccaprettimactot",
        "datacensimento",
        "dtcomautorita",
        "codicequalificascrapie",
        "descrqualificascrapie",
        "datarilevazionequalifica"
    })
    public static class UBACENSIMENTOOVINO2012 {

        @XmlElement(name = "CENSIMENTO_ID", required = true)
        protected BigDecimal censimentoid;
        @XmlElement(name = "P_ALLEV_ID", required = true)
        protected BigDecimal pallevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "COD_FISCALE_PROP")
        protected String codfiscaleprop;
        @XmlElement(name = "COD_FISCALE_DETE")
        protected String codfiscaledete;
        @XmlElement(name = "SPECIE_CODICE")
        protected String speciecodice;
        @XmlElement(name = "SPECIE_DESCRIZIONE")
        protected String speciedescrizione;
        @XmlElement(name = "DATA_INIZIO_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datainizioperiodo;
        @XmlElement(name = "DATA_FINE_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datafineperiodo;
        @XmlElement(name = "O_MASCHI_ADULTI")
        protected BigDecimal omaschiadulti;
        @XmlElement(name = "O_MASCHI_ADULTI_LIB")
        protected BigDecimal omaschiadultilib;
        @XmlElement(name = "O_FEMMINE_ADULTE")
        protected BigDecimal ofemmineadulte;
        @XmlElement(name = "O_FEMMINE_ADULTE_LIB")
        protected BigDecimal ofemmineadultelib;
        @XmlElement(name = "O_MASCHI_RIMONTA")
        protected BigDecimal omaschirimonta;
        @XmlElement(name = "O_MASCHI_RIMONTA_LIB")
        protected BigDecimal omaschirimontalib;
        @XmlElement(name = "O_FEMMINE_RIMONTA")
        protected BigDecimal ofemminerimonta;
        @XmlElement(name = "O_FEMMINE_RIMONTA_LIB")
        protected BigDecimal ofemminerimontalib;
        @XmlElement(name = "O_CAPI_TOT")
        protected BigDecimal ocapitot;
        @XmlElement(name = "O_AGNELLI_MAC_TOT")
        protected BigDecimal oagnellimactot;
        @XmlElement(name = "C_MASCHI_ADULTI")
        protected BigDecimal cmaschiadulti;
        @XmlElement(name = "C_MASCHI_ADULTI_LIB")
        protected BigDecimal cmaschiadultilib;
        @XmlElement(name = "C_FEMMINE_ADULTE")
        protected BigDecimal cfemmineadulte;
        @XmlElement(name = "C_FEMMINE_ADULTE_LIB")
        protected BigDecimal cfemmineadultelib;
        @XmlElement(name = "C_MASCHI_RIMONTA")
        protected BigDecimal cmaschirimonta;
        @XmlElement(name = "C_MASCHI_RIMONTA_LIB")
        protected BigDecimal cmaschirimontalib;
        @XmlElement(name = "C_FEMMINE_RIMONTA")
        protected BigDecimal cfemminerimonta;
        @XmlElement(name = "C_FEMMINE_RIMONTA_LIB")
        protected BigDecimal cfemminerimontalib;
        @XmlElement(name = "C_CAPI_TOT")
        protected BigDecimal ccapitot;
        @XmlElement(name = "C_CAPRETTI_MAC_TOT")
        protected BigDecimal ccaprettimactot;
        @XmlElement(name = "DATA_CENSIMENTO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datacensimento;
        @XmlElement(name = "DT_COM_AUTORITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtcomautorita;
        @XmlElement(name = "CODICE_QUALIFICA_SCRAPIE")
        protected String codicequalificascrapie;
        @XmlElement(name = "DESCR_QUALIFICA_SCRAPIE")
        protected String descrqualificascrapie;
        @XmlElement(name = "DATA_RILEVAZIONE_QUALIFICA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datarilevazionequalifica;

        /**
         * Recupera il valore della proprietà censimentoid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCENSIMENTOID() {
            return censimentoid;
        }

        /**
         * Imposta il valore della proprietà censimentoid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCENSIMENTOID(BigDecimal value) {
            this.censimentoid = value;
        }

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
         * Recupera il valore della proprietà speciedescrizione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECIEDESCRIZIONE() {
            return speciedescrizione;
        }

        /**
         * Imposta il valore della proprietà speciedescrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECIEDESCRIZIONE(String value) {
            this.speciedescrizione = value;
        }

        /**
         * Recupera il valore della proprietà datainizioperiodo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAINIZIOPERIODO() {
            return datainizioperiodo;
        }

        /**
         * Imposta il valore della proprietà datainizioperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAINIZIOPERIODO(XMLGregorianCalendar value) {
            this.datainizioperiodo = value;
        }

        /**
         * Recupera il valore della proprietà datafineperiodo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAFINEPERIODO() {
            return datafineperiodo;
        }

        /**
         * Imposta il valore della proprietà datafineperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAFINEPERIODO(XMLGregorianCalendar value) {
            this.datafineperiodo = value;
        }

        /**
         * Recupera il valore della proprietà omaschiadulti.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOMASCHIADULTI() {
            return omaschiadulti;
        }

        /**
         * Imposta il valore della proprietà omaschiadulti.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOMASCHIADULTI(BigDecimal value) {
            this.omaschiadulti = value;
        }

        /**
         * Recupera il valore della proprietà omaschiadultilib.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOMASCHIADULTILIB() {
            return omaschiadultilib;
        }

        /**
         * Imposta il valore della proprietà omaschiadultilib.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOMASCHIADULTILIB(BigDecimal value) {
            this.omaschiadultilib = value;
        }

        /**
         * Recupera il valore della proprietà ofemmineadulte.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOFEMMINEADULTE() {
            return ofemmineadulte;
        }

        /**
         * Imposta il valore della proprietà ofemmineadulte.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOFEMMINEADULTE(BigDecimal value) {
            this.ofemmineadulte = value;
        }

        /**
         * Recupera il valore della proprietà ofemmineadultelib.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOFEMMINEADULTELIB() {
            return ofemmineadultelib;
        }

        /**
         * Imposta il valore della proprietà ofemmineadultelib.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOFEMMINEADULTELIB(BigDecimal value) {
            this.ofemmineadultelib = value;
        }

        /**
         * Recupera il valore della proprietà omaschirimonta.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOMASCHIRIMONTA() {
            return omaschirimonta;
        }

        /**
         * Imposta il valore della proprietà omaschirimonta.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOMASCHIRIMONTA(BigDecimal value) {
            this.omaschirimonta = value;
        }

        /**
         * Recupera il valore della proprietà omaschirimontalib.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOMASCHIRIMONTALIB() {
            return omaschirimontalib;
        }

        /**
         * Imposta il valore della proprietà omaschirimontalib.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOMASCHIRIMONTALIB(BigDecimal value) {
            this.omaschirimontalib = value;
        }

        /**
         * Recupera il valore della proprietà ofemminerimonta.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOFEMMINERIMONTA() {
            return ofemminerimonta;
        }

        /**
         * Imposta il valore della proprietà ofemminerimonta.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOFEMMINERIMONTA(BigDecimal value) {
            this.ofemminerimonta = value;
        }

        /**
         * Recupera il valore della proprietà ofemminerimontalib.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOFEMMINERIMONTALIB() {
            return ofemminerimontalib;
        }

        /**
         * Imposta il valore della proprietà ofemminerimontalib.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOFEMMINERIMONTALIB(BigDecimal value) {
            this.ofemminerimontalib = value;
        }

        /**
         * Recupera il valore della proprietà ocapitot.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOCAPITOT() {
            return ocapitot;
        }

        /**
         * Imposta il valore della proprietà ocapitot.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOCAPITOT(BigDecimal value) {
            this.ocapitot = value;
        }

        /**
         * Recupera il valore della proprietà oagnellimactot.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOAGNELLIMACTOT() {
            return oagnellimactot;
        }

        /**
         * Imposta il valore della proprietà oagnellimactot.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOAGNELLIMACTOT(BigDecimal value) {
            this.oagnellimactot = value;
        }

        /**
         * Recupera il valore della proprietà cmaschiadulti.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCMASCHIADULTI() {
            return cmaschiadulti;
        }

        /**
         * Imposta il valore della proprietà cmaschiadulti.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCMASCHIADULTI(BigDecimal value) {
            this.cmaschiadulti = value;
        }

        /**
         * Recupera il valore della proprietà cmaschiadultilib.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCMASCHIADULTILIB() {
            return cmaschiadultilib;
        }

        /**
         * Imposta il valore della proprietà cmaschiadultilib.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCMASCHIADULTILIB(BigDecimal value) {
            this.cmaschiadultilib = value;
        }

        /**
         * Recupera il valore della proprietà cfemmineadulte.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCFEMMINEADULTE() {
            return cfemmineadulte;
        }

        /**
         * Imposta il valore della proprietà cfemmineadulte.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCFEMMINEADULTE(BigDecimal value) {
            this.cfemmineadulte = value;
        }

        /**
         * Recupera il valore della proprietà cfemmineadultelib.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCFEMMINEADULTELIB() {
            return cfemmineadultelib;
        }

        /**
         * Imposta il valore della proprietà cfemmineadultelib.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCFEMMINEADULTELIB(BigDecimal value) {
            this.cfemmineadultelib = value;
        }

        /**
         * Recupera il valore della proprietà cmaschirimonta.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCMASCHIRIMONTA() {
            return cmaschirimonta;
        }

        /**
         * Imposta il valore della proprietà cmaschirimonta.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCMASCHIRIMONTA(BigDecimal value) {
            this.cmaschirimonta = value;
        }

        /**
         * Recupera il valore della proprietà cmaschirimontalib.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCMASCHIRIMONTALIB() {
            return cmaschirimontalib;
        }

        /**
         * Imposta il valore della proprietà cmaschirimontalib.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCMASCHIRIMONTALIB(BigDecimal value) {
            this.cmaschirimontalib = value;
        }

        /**
         * Recupera il valore della proprietà cfemminerimonta.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCFEMMINERIMONTA() {
            return cfemminerimonta;
        }

        /**
         * Imposta il valore della proprietà cfemminerimonta.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCFEMMINERIMONTA(BigDecimal value) {
            this.cfemminerimonta = value;
        }

        /**
         * Recupera il valore della proprietà cfemminerimontalib.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCFEMMINERIMONTALIB() {
            return cfemminerimontalib;
        }

        /**
         * Imposta il valore della proprietà cfemminerimontalib.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCFEMMINERIMONTALIB(BigDecimal value) {
            this.cfemminerimontalib = value;
        }

        /**
         * Recupera il valore della proprietà ccapitot.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCCAPITOT() {
            return ccapitot;
        }

        /**
         * Imposta il valore della proprietà ccapitot.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCCAPITOT(BigDecimal value) {
            this.ccapitot = value;
        }

        /**
         * Recupera il valore della proprietà ccaprettimactot.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCCAPRETTIMACTOT() {
            return ccaprettimactot;
        }

        /**
         * Imposta il valore della proprietà ccaprettimactot.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCCAPRETTIMACTOT(BigDecimal value) {
            this.ccaprettimactot = value;
        }

        /**
         * Recupera il valore della proprietà datacensimento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATACENSIMENTO() {
            return datacensimento;
        }

        /**
         * Imposta il valore della proprietà datacensimento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATACENSIMENTO(XMLGregorianCalendar value) {
            this.datacensimento = value;
        }

        /**
         * Recupera il valore della proprietà dtcomautorita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTCOMAUTORITA() {
            return dtcomautorita;
        }

        /**
         * Imposta il valore della proprietà dtcomautorita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTCOMAUTORITA(XMLGregorianCalendar value) {
            this.dtcomautorita = value;
        }

        /**
         * Recupera il valore della proprietà codicequalificascrapie.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEQUALIFICASCRAPIE() {
            return codicequalificascrapie;
        }

        /**
         * Imposta il valore della proprietà codicequalificascrapie.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEQUALIFICASCRAPIE(String value) {
            this.codicequalificascrapie = value;
        }

        /**
         * Recupera il valore della proprietà descrqualificascrapie.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDESCRQUALIFICASCRAPIE() {
            return descrqualificascrapie;
        }

        /**
         * Imposta il valore della proprietà descrqualificascrapie.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDESCRQUALIFICASCRAPIE(String value) {
            this.descrqualificascrapie = value;
        }

        /**
         * Recupera il valore della proprietà datarilevazionequalifica.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATARILEVAZIONEQUALIFICA() {
            return datarilevazionequalifica;
        }

        /**
         * Imposta il valore della proprietà datarilevazionequalifica.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATARILEVAZIONEQUALIFICA(XMLGregorianCalendar value) {
            this.datarilevazionequalifica = value;
        }

    }

}
