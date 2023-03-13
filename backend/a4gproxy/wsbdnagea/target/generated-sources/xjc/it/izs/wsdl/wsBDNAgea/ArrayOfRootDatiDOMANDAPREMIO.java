//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.03.13 at 04:32:46 PM CET 
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
 * <p>Java class for ArrayOfRootDatiDOMANDA_PREMIO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
         * Gets the value of the pdomandabdnid property.
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
         * Sets the value of the pdomandabdnid property.
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
         * Gets the value of the pdomandaageaid property.
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
         * Sets the value of the pdomandaageaid property.
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
         * Gets the value of the pcuua property.
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
         * Sets the value of the pcuua property.
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
         * Gets the value of the pallevid property.
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
         * Sets the value of the pallevid property.
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
         * Gets the value of the punitatecnicaid property.
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
         * Sets the value of the punitatecnicaid property.
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
         * Gets the value of the pannodomanda property.
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
         * Sets the value of the pannodomanda property.
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
         * Gets the value of the ptipodocumento property.
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
         * Sets the value of the ptipodocumento property.
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
         * Gets the value of the pcodicedocumento property.
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
         * Sets the value of the pcodicedocumento property.
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
         * Gets the value of the pstatodomanda property.
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
         * Sets the value of the pstatodomanda property.
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
         * Gets the value of the pdatapresentazione property.
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
         * Sets the value of the pdatapresentazione property.
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
