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
 * <p>Classe Java per ArrayOfRootDatiUBA_CONSISTENZA_ALLEVAMENTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiUBA_CONSISTENZA_ALLEVAMENTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UBA_CONSISTENZA_ALLEVAMENTO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_0_6_MESI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_6_24_MESI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_OLTRE_24_MESI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_VITELLI_MACELLATI_0_8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_BOVINI_MACELLATI_OLTRE_8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiUBA_CONSISTENZA_ALLEVAMENTO", propOrder = {
    "ubaconsistenzaallevamento"
})
public class ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO {

    @XmlElement(name = "UBA_CONSISTENZA_ALLEVAMENTO")
    protected List<ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO.UBACONSISTENZAALLEVAMENTO> ubaconsistenzaallevamento;

    /**
     * Gets the value of the ubaconsistenzaallevamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ubaconsistenzaallevamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUBACONSISTENZAALLEVAMENTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO.UBACONSISTENZAALLEVAMENTO }
     * 
     * 
     */
    public List<ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO.UBACONSISTENZAALLEVAMENTO> getUBACONSISTENZAALLEVAMENTO() {
        if (ubaconsistenzaallevamento == null) {
            ubaconsistenzaallevamento = new ArrayList<ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO.UBACONSISTENZAALLEVAMENTO>();
        }
        return this.ubaconsistenzaallevamento;
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
     *         &lt;element name="CAPI_0_6_MESI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_6_24_MESI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_OLTRE_24_MESI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_VITELLI_MACELLATI_0_8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_BOVINI_MACELLATI_OLTRE_8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "capi06MESI",
        "capi624MESI",
        "capioltre24MESI",
        "numvitellimacellati08",
        "numbovinimacellatioltre8",
        "dtinizioperiodo",
        "dtfineperiodo"
    })
    public static class UBACONSISTENZAALLEVAMENTO {

        @XmlElement(name = "P_ALLEV_ID", required = true)
        protected BigDecimal pallevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "COD_FISCALE_PROP")
        protected String codfiscaleprop;
        @XmlElement(name = "COD_FISCALE_DETE")
        protected String codfiscaledete;
        @XmlElement(name = "CAPI_0_6_MESI")
        protected String capi06MESI;
        @XmlElement(name = "CAPI_6_24_MESI")
        protected String capi624MESI;
        @XmlElement(name = "CAPI_OLTRE_24_MESI")
        protected String capioltre24MESI;
        @XmlElement(name = "NUM_VITELLI_MACELLATI_0_8")
        protected String numvitellimacellati08;
        @XmlElement(name = "NUM_BOVINI_MACELLATI_OLTRE_8")
        protected String numbovinimacellatioltre8;
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
         * Recupera il valore della proprietà capi06MESI.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPI06MESI() {
            return capi06MESI;
        }

        /**
         * Imposta il valore della proprietà capi06MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPI06MESI(String value) {
            this.capi06MESI = value;
        }

        /**
         * Recupera il valore della proprietà capi624MESI.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPI624MESI() {
            return capi624MESI;
        }

        /**
         * Imposta il valore della proprietà capi624MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPI624MESI(String value) {
            this.capi624MESI = value;
        }

        /**
         * Recupera il valore della proprietà capioltre24MESI.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAPIOLTRE24MESI() {
            return capioltre24MESI;
        }

        /**
         * Imposta il valore della proprietà capioltre24MESI.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAPIOLTRE24MESI(String value) {
            this.capioltre24MESI = value;
        }

        /**
         * Recupera il valore della proprietà numvitellimacellati08.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMVITELLIMACELLATI08() {
            return numvitellimacellati08;
        }

        /**
         * Imposta il valore della proprietà numvitellimacellati08.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMVITELLIMACELLATI08(String value) {
            this.numvitellimacellati08 = value;
        }

        /**
         * Recupera il valore della proprietà numbovinimacellatioltre8.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMBOVINIMACELLATIOLTRE8() {
            return numbovinimacellatioltre8;
        }

        /**
         * Imposta il valore della proprietà numbovinimacellatioltre8.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMBOVINIMACELLATIOLTRE8(String value) {
            this.numbovinimacellatioltre8 = value;
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
