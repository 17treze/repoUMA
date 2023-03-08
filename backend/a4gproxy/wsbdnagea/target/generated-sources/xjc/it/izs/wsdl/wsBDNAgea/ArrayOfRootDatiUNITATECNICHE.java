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
 * <p>Classe Java per ArrayOfRootDatiUNITA_TECNICHE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiUNITA_TECNICHE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UNITA_TECNICHE" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_UNITA_TECNICA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_FASCICOLO_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_TIPO_PRODUZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_REPERIMENTO_INFO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DATA_REVOCA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiUNITA_TECNICHE", propOrder = {
    "unitatecniche"
})
public class ArrayOfRootDatiUNITATECNICHE {

    @XmlElement(name = "UNITA_TECNICHE")
    protected List<ArrayOfRootDatiUNITATECNICHE.UNITATECNICHE> unitatecniche;

    /**
     * Gets the value of the unitatecniche property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unitatecniche property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUNITATECNICHE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiUNITATECNICHE.UNITATECNICHE }
     * 
     * 
     */
    public List<ArrayOfRootDatiUNITATECNICHE.UNITATECNICHE> getUNITATECNICHE() {
        if (unitatecniche == null) {
            unitatecniche = new ArrayList<ArrayOfRootDatiUNITATECNICHE.UNITATECNICHE>();
        }
        return this.unitatecniche;
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
     *         &lt;element name="P_UNITA_TECNICA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_FASCICOLO_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_TIPO_PRODUZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_REPERIMENTO_INFO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DATA_REVOCA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "punitatecnicaid",
        "pfascicoloid",
        "pallevid",
        "ptipoproduzione",
        "paziendacodice",
        "pdtreperimentoinfo",
        "pdatarevoca"
    })
    public static class UNITATECNICHE {

        @XmlElement(name = "P_UNITA_TECNICA_ID", required = true)
        protected BigDecimal punitatecnicaid;
        @XmlElement(name = "P_FASCICOLO_ID")
        protected String pfascicoloid;
        @XmlElement(name = "P_ALLEV_ID", required = true)
        protected BigDecimal pallevid;
        @XmlElement(name = "P_TIPO_PRODUZIONE")
        protected String ptipoproduzione;
        @XmlElement(name = "P_AZIENDA_CODICE")
        protected String paziendacodice;
        @XmlElement(name = "P_DT_REPERIMENTO_INFO")
        protected String pdtreperimentoinfo;
        @XmlElement(name = "P_DATA_REVOCA")
        protected String pdatarevoca;

        /**
         * Recupera il valore della proprietà punitatecnicaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPUNITATECNICAID() {
            return punitatecnicaid;
        }

        /**
         * Imposta il valore della proprietà punitatecnicaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPUNITATECNICAID(BigDecimal value) {
            this.punitatecnicaid = value;
        }

        /**
         * Recupera il valore della proprietà pfascicoloid.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPFASCICOLOID() {
            return pfascicoloid;
        }

        /**
         * Imposta il valore della proprietà pfascicoloid.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPFASCICOLOID(String value) {
            this.pfascicoloid = value;
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
         * Recupera il valore della proprietà ptipoproduzione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPTIPOPRODUZIONE() {
            return ptipoproduzione;
        }

        /**
         * Imposta il valore della proprietà ptipoproduzione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPTIPOPRODUZIONE(String value) {
            this.ptipoproduzione = value;
        }

        /**
         * Recupera il valore della proprietà paziendacodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPAZIENDACODICE() {
            return paziendacodice;
        }

        /**
         * Imposta il valore della proprietà paziendacodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPAZIENDACODICE(String value) {
            this.paziendacodice = value;
        }

        /**
         * Recupera il valore della proprietà pdtreperimentoinfo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTREPERIMENTOINFO() {
            return pdtreperimentoinfo;
        }

        /**
         * Imposta il valore della proprietà pdtreperimentoinfo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTREPERIMENTOINFO(String value) {
            this.pdtreperimentoinfo = value;
        }

        /**
         * Recupera il valore della proprietà pdatarevoca.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDATAREVOCA() {
            return pdatarevoca;
        }

        /**
         * Imposta il valore della proprietà pdatarevoca.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDATAREVOCA(String value) {
            this.pdatarevoca = value;
        }

    }

}
