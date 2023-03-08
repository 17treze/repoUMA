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
 * <p>Classe Java per ArrayOfRootDatiDOMANDA_PREMIO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiDOMANDA_PREMIO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DOMANDA_PREMIO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_DOMANDA_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_DOMANDA_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_UNITA_TECNICA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_ANNO_DOMANDA" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_TIPO_DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_CODICE_DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_STATO_DOMANDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DATA_PRESENTAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
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
@XmlType(name = "ArrayOfRootDatiDOMANDA_PREMIO", propOrder = {
    "domandapremio"
})
public class ArrayOfRootDatiDOMANDAPREMIO {

    @XmlElement(name = "DOMANDA_PREMIO")
    protected List<ArrayOfRootDatiDOMANDAPREMIO.DOMANDAPREMIO> domandapremio;

    /**
     * Gets the value of the domandapremio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domandapremio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDOMANDAPREMIO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiDOMANDAPREMIO.DOMANDAPREMIO }
     * 
     * 
     */
    public List<ArrayOfRootDatiDOMANDAPREMIO.DOMANDAPREMIO> getDOMANDAPREMIO() {
        if (domandapremio == null) {
            domandapremio = new ArrayList<ArrayOfRootDatiDOMANDAPREMIO.DOMANDAPREMIO>();
        }
        return this.domandapremio;
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
     *         &lt;element name="P_DOMANDA_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_DOMANDA_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_UNITA_TECNICA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_ANNO_DOMANDA" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_TIPO_DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_CODICE_DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_STATO_DOMANDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DATA_PRESENTAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
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
        "pdomandabdnid",
        "pdomandaageaid",
        "pcuua",
        "pallevid",
        "punitatecnicaid",
        "pannodomanda",
        "ptipodocumento",
        "pcodicedocumento",
        "pstatodomanda",
        "pdatapresentazione"
    })
    public static class DOMANDAPREMIO {

        @XmlElement(name = "P_DOMANDA_BDN_ID", required = true)
        protected BigDecimal pdomandabdnid;
        @XmlElement(name = "P_DOMANDA_AGEA_ID")
        protected String pdomandaageaid;
        @XmlElement(name = "P_CUUA")
        protected String pcuua;
        @XmlElement(name = "P_ALLEV_ID", required = true)
        protected BigDecimal pallevid;
        @XmlElement(name = "P_UNITA_TECNICA_ID", required = true)
        protected BigDecimal punitatecnicaid;
        @XmlElement(name = "P_ANNO_DOMANDA", required = true)
        protected BigDecimal pannodomanda;
        @XmlElement(name = "P_TIPO_DOCUMENTO")
        protected String ptipodocumento;
        @XmlElement(name = "P_CODICE_DOCUMENTO")
        protected String pcodicedocumento;
        @XmlElement(name = "P_STATO_DOMANDA")
        protected String pstatodomanda;
        @XmlElement(name = "P_DATA_PRESENTAZIONE", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdatapresentazione;

        /**
         * Recupera il valore della proprietà pdomandabdnid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPDOMANDABDNID() {
            return pdomandabdnid;
        }

        /**
         * Imposta il valore della proprietà pdomandabdnid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPDOMANDABDNID(BigDecimal value) {
            this.pdomandabdnid = value;
        }

        /**
         * Recupera il valore della proprietà pdomandaageaid.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDOMANDAAGEAID() {
            return pdomandaageaid;
        }

        /**
         * Imposta il valore della proprietà pdomandaageaid.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDOMANDAAGEAID(String value) {
            this.pdomandaageaid = value;
        }

        /**
         * Recupera il valore della proprietà pcuua.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCUUA() {
            return pcuua;
        }

        /**
         * Imposta il valore della proprietà pcuua.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCUUA(String value) {
            this.pcuua = value;
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
         * Recupera il valore della proprietà pannodomanda.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPANNODOMANDA() {
            return pannodomanda;
        }

        /**
         * Imposta il valore della proprietà pannodomanda.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPANNODOMANDA(BigDecimal value) {
            this.pannodomanda = value;
        }

        /**
         * Recupera il valore della proprietà ptipodocumento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPTIPODOCUMENTO() {
            return ptipodocumento;
        }

        /**
         * Imposta il valore della proprietà ptipodocumento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPTIPODOCUMENTO(String value) {
            this.ptipodocumento = value;
        }

        /**
         * Recupera il valore della proprietà pcodicedocumento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODICEDOCUMENTO() {
            return pcodicedocumento;
        }

        /**
         * Imposta il valore della proprietà pcodicedocumento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODICEDOCUMENTO(String value) {
            this.pcodicedocumento = value;
        }

        /**
         * Recupera il valore della proprietà pstatodomanda.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPSTATODOMANDA() {
            return pstatodomanda;
        }

        /**
         * Imposta il valore della proprietà pstatodomanda.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPSTATODOMANDA(String value) {
            this.pstatodomanda = value;
        }

        /**
         * Recupera il valore della proprietà pdatapresentazione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDATAPRESENTAZIONE() {
            return pdatapresentazione;
        }

        /**
         * Imposta il valore della proprietà pdatapresentazione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDATAPRESENTAZIONE(XMLGregorianCalendar value) {
            this.pdatapresentazione = value;
        }

    }

}
