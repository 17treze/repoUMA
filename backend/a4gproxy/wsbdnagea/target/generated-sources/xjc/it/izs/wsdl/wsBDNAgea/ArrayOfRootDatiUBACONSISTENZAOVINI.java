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
 * <p>Classe Java per ArrayOfRootDatiUBA_CONSISTENZA_OVINI complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiUBA_CONSISTENZA_OVINI"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UBA_CONSISTENZA_OVINI" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="OVINI_MASCHI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="OVINI_FEMMINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPRINI_MASCHI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPRINI_FEMMINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="OVINI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPRINI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiUBA_CONSISTENZA_OVINI", propOrder = {
    "ubaconsistenzaovini"
})
public class ArrayOfRootDatiUBACONSISTENZAOVINI {

    @XmlElement(name = "UBA_CONSISTENZA_OVINI")
    protected List<ArrayOfRootDatiUBACONSISTENZAOVINI.UBACONSISTENZAOVINI> ubaconsistenzaovini;

    /**
     * Gets the value of the ubaconsistenzaovini property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ubaconsistenzaovini property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUBACONSISTENZAOVINI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiUBACONSISTENZAOVINI.UBACONSISTENZAOVINI }
     * 
     * 
     */
    public List<ArrayOfRootDatiUBACONSISTENZAOVINI.UBACONSISTENZAOVINI> getUBACONSISTENZAOVINI() {
        if (ubaconsistenzaovini == null) {
            ubaconsistenzaovini = new ArrayList<ArrayOfRootDatiUBACONSISTENZAOVINI.UBACONSISTENZAOVINI>();
        }
        return this.ubaconsistenzaovini;
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
     *         &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="OVINI_MASCHI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="OVINI_FEMMINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPRINI_MASCHI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPRINI_FEMMINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="OVINI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPRINI_TOTALI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "codfiscaleprop",
        "codfiscaledete",
        "specodice",
        "ovinimaschi",
        "ovinifemmine",
        "caprinimaschi",
        "caprinifemmine",
        "ovinitotali",
        "caprinitotali",
        "dtinizioperiodo",
        "dtfineperiodo"
    })
    public static class UBACONSISTENZAOVINI {

        @XmlElement(name = "P_ALLEV_ID", required = true)
        protected BigDecimal pallevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "COD_FISCALE_PROP")
        protected String codfiscaleprop;
        @XmlElement(name = "COD_FISCALE_DETE")
        protected String codfiscaledete;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "OVINI_MASCHI")
        protected String ovinimaschi;
        @XmlElement(name = "OVINI_FEMMINE")
        protected String ovinifemmine;
        @XmlElement(name = "CAPRINI_MASCHI")
        protected String caprinimaschi;
        @XmlElement(name = "CAPRINI_FEMMINE")
        protected String caprinifemmine;
        @XmlElement(name = "OVINI_TOTALI")
        protected String ovinitotali;
        @XmlElement(name = "CAPRINI_TOTALI")
        protected String caprinitotali;
        @XmlElement(name = "DT_INIZIO_PERIODO")
        protected String dtinizioperiodo;
        @XmlElement(name = "DT_FINE_PERIODO")
        protected String dtfineperiodo;

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
         * Recupera il valore della proprietà ovinimaschi.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOVINIMASCHI() {
            return ovinimaschi;
        }

        /**
         * Imposta il valore della proprietà ovinimaschi.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOVINIMASCHI(String value) {
            this.ovinimaschi = value;
        }

        /**
         * Recupera il valore della proprietà ovinifemmine.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOVINIFEMMINE() {
            return ovinifemmine;
        }

        /**
         * Imposta il valore della proprietà ovinifemmine.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOVINIFEMMINE(String value) {
            this.ovinifemmine = value;
        }

        /**
         * Recupera il valore della proprietà caprinimaschi.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPRINIMASCHI() {
            return caprinimaschi;
        }

        /**
         * Imposta il valore della proprietà caprinimaschi.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPRINIMASCHI(String value) {
            this.caprinimaschi = value;
        }

        /**
         * Recupera il valore della proprietà caprinifemmine.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPRINIFEMMINE() {
            return caprinifemmine;
        }

        /**
         * Imposta il valore della proprietà caprinifemmine.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPRINIFEMMINE(String value) {
            this.caprinifemmine = value;
        }

        /**
         * Recupera il valore della proprietà ovinitotali.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOVINITOTALI() {
            return ovinitotali;
        }

        /**
         * Imposta il valore della proprietà ovinitotali.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOVINITOTALI(String value) {
            this.ovinitotali = value;
        }

        /**
         * Recupera il valore della proprietà caprinitotali.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPRINITOTALI() {
            return caprinitotali;
        }

        /**
         * Imposta il valore della proprietà caprinitotali.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPRINITOTALI(String value) {
            this.caprinitotali = value;
        }

        /**
         * Recupera il valore della proprietà dtinizioperiodo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDTINIZIOPERIODO() {
            return dtinizioperiodo;
        }

        /**
         * Imposta il valore della proprietà dtinizioperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTINIZIOPERIODO(String value) {
            this.dtinizioperiodo = value;
        }

        /**
         * Recupera il valore della proprietà dtfineperiodo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDTFINEPERIODO() {
            return dtfineperiodo;
        }

        /**
         * Imposta il valore della proprietà dtfineperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDTFINEPERIODO(String value) {
            this.dtfineperiodo = value;
        }

    }

}
