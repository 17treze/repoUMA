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
 * <p>Classe Java per ArrayOfRootDatiCONSISTENZA_ALLEVAMENTO_DETT complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCONSISTENZA_ALLEVAMENTO_DETT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CONSISTENZA_ALLEVAMENTO_DETT" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_ALLEV_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_ALLEV_DESCR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ORIENTAMENTO_DESCR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_M_0_6_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_M_0_6_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_M_0_6_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_M_6_24_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_M_6_24_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_M_6_24_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_M_OLTRE_24_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_M_OLTRE_24_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_M_OLTRE_24_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_F_0_6_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_F_0_6_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_F_0_6_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_F_6_24_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_F_6_24_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_F_6_24_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_F_OLTRE_24_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_F_OLTRE_24_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_F_OLTRE_24_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CONSISTENZA_MEDIA_TOTALE_M" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CONSISTENZA_MEDIA_TOTALE_F" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCONSISTENZA_ALLEVAMENTO_DETT", propOrder = {
    "consistenzaallevamentodett"
})
public class ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT {

    @XmlElement(name = "CONSISTENZA_ALLEVAMENTO_DETT")
    protected List<ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT.CONSISTENZAALLEVAMENTODETT> consistenzaallevamentodett;

    /**
     * Gets the value of the consistenzaallevamentodett property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the consistenzaallevamentodett property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCONSISTENZAALLEVAMENTODETT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT.CONSISTENZAALLEVAMENTODETT }
     * 
     * 
     */
    public List<ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT.CONSISTENZAALLEVAMENTODETT> getCONSISTENZAALLEVAMENTODETT() {
        if (consistenzaallevamentodett == null) {
            consistenzaallevamentodett = new ArrayList<ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT.CONSISTENZAALLEVAMENTODETT>();
        }
        return this.consistenzaallevamentodett;
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
     *         &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_ALLEV_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_ALLEV_DESCR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ORIENTAMENTO_DESCR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_M_0_6_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_M_0_6_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_M_0_6_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_M_6_24_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_M_6_24_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_M_6_24_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_M_OLTRE_24_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_M_OLTRE_24_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_M_OLTRE_24_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_F_0_6_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_F_0_6_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_F_0_6_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_F_6_24_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_F_6_24_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_F_6_24_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_F_OLTRE_24_MESI_CARNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_F_OLTRE_24_MESI_LATTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_F_OLTRE_24_MESI_MISTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CONSISTENZA_MEDIA_TOTALE_M" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CONSISTENZA_MEDIA_TOTALE_F" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "codfiscaleprop",
        "codfiscaledete",
        "dtinizioperiodo",
        "dtfineperiodo",
        "tipoallevcodice",
        "tipoallevdescr",
        "orientamentodescr",
        "capim06MESICARNE",
        "capim06MESILATTE",
        "capim06MESIMISTO",
        "capim624MESICARNE",
        "capim624MESILATTE",
        "capim624MESIMISTO",
        "capimoltre24MESICARNE",
        "capimoltre24MESILATTE",
        "capimoltre24MESIMISTO",
        "capif06MESICARNE",
        "capif06MESILATTE",
        "capif06MESIMISTO",
        "capif624MESICARNE",
        "capif624MESILATTE",
        "capif624MESIMISTO",
        "capifoltre24MESICARNE",
        "capifoltre24MESILATTE",
        "capifoltre24MESIMISTO",
        "consistenzamediatotalem",
        "consistenzamediatotalef"
    })
    public static class CONSISTENZAALLEVAMENTODETT {

        @XmlElement(name = "P_ALLEV_ID", required = true)
        protected BigDecimal pallevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "COD_FISCALE_PROP")
        protected String codfiscaleprop;
        @XmlElement(name = "COD_FISCALE_DETE")
        protected String codfiscaledete;
        @XmlElement(name = "DT_INIZIO_PERIODO")
        protected String dtinizioperiodo;
        @XmlElement(name = "DT_FINE_PERIODO")
        protected String dtfineperiodo;
        @XmlElement(name = "TIPO_ALLEV_CODICE")
        protected String tipoallevcodice;
        @XmlElement(name = "TIPO_ALLEV_DESCR")
        protected String tipoallevdescr;
        @XmlElement(name = "ORIENTAMENTO_DESCR")
        protected String orientamentodescr;
        @XmlElement(name = "CAPI_M_0_6_MESI_CARNE")
        protected String capim06MESICARNE;
        @XmlElement(name = "CAPI_M_0_6_MESI_LATTE")
        protected String capim06MESILATTE;
        @XmlElement(name = "CAPI_M_0_6_MESI_MISTO")
        protected String capim06MESIMISTO;
        @XmlElement(name = "CAPI_M_6_24_MESI_CARNE")
        protected String capim624MESICARNE;
        @XmlElement(name = "CAPI_M_6_24_MESI_LATTE")
        protected String capim624MESILATTE;
        @XmlElement(name = "CAPI_M_6_24_MESI_MISTO")
        protected String capim624MESIMISTO;
        @XmlElement(name = "CAPI_M_OLTRE_24_MESI_CARNE")
        protected String capimoltre24MESICARNE;
        @XmlElement(name = "CAPI_M_OLTRE_24_MESI_LATTE")
        protected String capimoltre24MESILATTE;
        @XmlElement(name = "CAPI_M_OLTRE_24_MESI_MISTO")
        protected String capimoltre24MESIMISTO;
        @XmlElement(name = "CAPI_F_0_6_MESI_CARNE")
        protected String capif06MESICARNE;
        @XmlElement(name = "CAPI_F_0_6_MESI_LATTE")
        protected String capif06MESILATTE;
        @XmlElement(name = "CAPI_F_0_6_MESI_MISTO")
        protected String capif06MESIMISTO;
        @XmlElement(name = "CAPI_F_6_24_MESI_CARNE")
        protected String capif624MESICARNE;
        @XmlElement(name = "CAPI_F_6_24_MESI_LATTE")
        protected String capif624MESILATTE;
        @XmlElement(name = "CAPI_F_6_24_MESI_MISTO")
        protected String capif624MESIMISTO;
        @XmlElement(name = "CAPI_F_OLTRE_24_MESI_CARNE")
        protected String capifoltre24MESICARNE;
        @XmlElement(name = "CAPI_F_OLTRE_24_MESI_LATTE")
        protected String capifoltre24MESILATTE;
        @XmlElement(name = "CAPI_F_OLTRE_24_MESI_MISTO")
        protected String capifoltre24MESIMISTO;
        @XmlElement(name = "CONSISTENZA_MEDIA_TOTALE_M")
        protected String consistenzamediatotalem;
        @XmlElement(name = "CONSISTENZA_MEDIA_TOTALE_F")
        protected String consistenzamediatotalef;

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

        /**
         * Recupera il valore della proprietà tipoallevcodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOALLEVCODICE() {
            return tipoallevcodice;
        }

        /**
         * Imposta il valore della proprietà tipoallevcodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOALLEVCODICE(String value) {
            this.tipoallevcodice = value;
        }

        /**
         * Recupera il valore della proprietà tipoallevdescr.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOALLEVDESCR() {
            return tipoallevdescr;
        }

        /**
         * Imposta il valore della proprietà tipoallevdescr.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOALLEVDESCR(String value) {
            this.tipoallevdescr = value;
        }

        /**
         * Recupera il valore della proprietà orientamentodescr.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getORIENTAMENTODESCR() {
            return orientamentodescr;
        }

        /**
         * Imposta il valore della proprietà orientamentodescr.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setORIENTAMENTODESCR(String value) {
            this.orientamentodescr = value;
        }

        /**
         * Recupera il valore della proprietà capim06MESICARNE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIM06MESICARNE() {
            return capim06MESICARNE;
        }

        /**
         * Imposta il valore della proprietà capim06MESICARNE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIM06MESICARNE(String value) {
            this.capim06MESICARNE = value;
        }

        /**
         * Recupera il valore della proprietà capim06MESILATTE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIM06MESILATTE() {
            return capim06MESILATTE;
        }

        /**
         * Imposta il valore della proprietà capim06MESILATTE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIM06MESILATTE(String value) {
            this.capim06MESILATTE = value;
        }

        /**
         * Recupera il valore della proprietà capim06MESIMISTO.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIM06MESIMISTO() {
            return capim06MESIMISTO;
        }

        /**
         * Imposta il valore della proprietà capim06MESIMISTO.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIM06MESIMISTO(String value) {
            this.capim06MESIMISTO = value;
        }

        /**
         * Recupera il valore della proprietà capim624MESICARNE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIM624MESICARNE() {
            return capim624MESICARNE;
        }

        /**
         * Imposta il valore della proprietà capim624MESICARNE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIM624MESICARNE(String value) {
            this.capim624MESICARNE = value;
        }

        /**
         * Recupera il valore della proprietà capim624MESILATTE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIM624MESILATTE() {
            return capim624MESILATTE;
        }

        /**
         * Imposta il valore della proprietà capim624MESILATTE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIM624MESILATTE(String value) {
            this.capim624MESILATTE = value;
        }

        /**
         * Recupera il valore della proprietà capim624MESIMISTO.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIM624MESIMISTO() {
            return capim624MESIMISTO;
        }

        /**
         * Imposta il valore della proprietà capim624MESIMISTO.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIM624MESIMISTO(String value) {
            this.capim624MESIMISTO = value;
        }

        /**
         * Recupera il valore della proprietà capimoltre24MESICARNE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIMOLTRE24MESICARNE() {
            return capimoltre24MESICARNE;
        }

        /**
         * Imposta il valore della proprietà capimoltre24MESICARNE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIMOLTRE24MESICARNE(String value) {
            this.capimoltre24MESICARNE = value;
        }

        /**
         * Recupera il valore della proprietà capimoltre24MESILATTE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIMOLTRE24MESILATTE() {
            return capimoltre24MESILATTE;
        }

        /**
         * Imposta il valore della proprietà capimoltre24MESILATTE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIMOLTRE24MESILATTE(String value) {
            this.capimoltre24MESILATTE = value;
        }

        /**
         * Recupera il valore della proprietà capimoltre24MESIMISTO.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIMOLTRE24MESIMISTO() {
            return capimoltre24MESIMISTO;
        }

        /**
         * Imposta il valore della proprietà capimoltre24MESIMISTO.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIMOLTRE24MESIMISTO(String value) {
            this.capimoltre24MESIMISTO = value;
        }

        /**
         * Recupera il valore della proprietà capif06MESICARNE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIF06MESICARNE() {
            return capif06MESICARNE;
        }

        /**
         * Imposta il valore della proprietà capif06MESICARNE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIF06MESICARNE(String value) {
            this.capif06MESICARNE = value;
        }

        /**
         * Recupera il valore della proprietà capif06MESILATTE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIF06MESILATTE() {
            return capif06MESILATTE;
        }

        /**
         * Imposta il valore della proprietà capif06MESILATTE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIF06MESILATTE(String value) {
            this.capif06MESILATTE = value;
        }

        /**
         * Recupera il valore della proprietà capif06MESIMISTO.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIF06MESIMISTO() {
            return capif06MESIMISTO;
        }

        /**
         * Imposta il valore della proprietà capif06MESIMISTO.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIF06MESIMISTO(String value) {
            this.capif06MESIMISTO = value;
        }

        /**
         * Recupera il valore della proprietà capif624MESICARNE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIF624MESICARNE() {
            return capif624MESICARNE;
        }

        /**
         * Imposta il valore della proprietà capif624MESICARNE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIF624MESICARNE(String value) {
            this.capif624MESICARNE = value;
        }

        /**
         * Recupera il valore della proprietà capif624MESILATTE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIF624MESILATTE() {
            return capif624MESILATTE;
        }

        /**
         * Imposta il valore della proprietà capif624MESILATTE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIF624MESILATTE(String value) {
            this.capif624MESILATTE = value;
        }

        /**
         * Recupera il valore della proprietà capif624MESIMISTO.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIF624MESIMISTO() {
            return capif624MESIMISTO;
        }

        /**
         * Imposta il valore della proprietà capif624MESIMISTO.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIF624MESIMISTO(String value) {
            this.capif624MESIMISTO = value;
        }

        /**
         * Recupera il valore della proprietà capifoltre24MESICARNE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIFOLTRE24MESICARNE() {
            return capifoltre24MESICARNE;
        }

        /**
         * Imposta il valore della proprietà capifoltre24MESICARNE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIFOLTRE24MESICARNE(String value) {
            this.capifoltre24MESICARNE = value;
        }

        /**
         * Recupera il valore della proprietà capifoltre24MESILATTE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIFOLTRE24MESILATTE() {
            return capifoltre24MESILATTE;
        }

        /**
         * Imposta il valore della proprietà capifoltre24MESILATTE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIFOLTRE24MESILATTE(String value) {
            this.capifoltre24MESILATTE = value;
        }

        /**
         * Recupera il valore della proprietà capifoltre24MESIMISTO.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIFOLTRE24MESIMISTO() {
            return capifoltre24MESIMISTO;
        }

        /**
         * Imposta il valore della proprietà capifoltre24MESIMISTO.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIFOLTRE24MESIMISTO(String value) {
            this.capifoltre24MESIMISTO = value;
        }

        /**
         * Recupera il valore della proprietà consistenzamediatotalem.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCONSISTENZAMEDIATOTALEM() {
            return consistenzamediatotalem;
        }

        /**
         * Imposta il valore della proprietà consistenzamediatotalem.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCONSISTENZAMEDIATOTALEM(String value) {
            this.consistenzamediatotalem = value;
        }

        /**
         * Recupera il valore della proprietà consistenzamediatotalef.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCONSISTENZAMEDIATOTALEF() {
            return consistenzamediatotalef;
        }

        /**
         * Imposta il valore della proprietà consistenzamediatotalef.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCONSISTENZAMEDIATOTALEF(String value) {
            this.consistenzamediatotalef = value;
        }

    }

}
