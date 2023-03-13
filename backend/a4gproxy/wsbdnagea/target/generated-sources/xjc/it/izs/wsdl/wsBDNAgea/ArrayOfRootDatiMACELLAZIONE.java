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
 * <p>Java class for ArrayOfRootDatiMACELLAZIONE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiMACELLAZIONE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MACELLAZIONE" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_MACELLO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_FISC_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="P_PESO_CARCASSA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_NUM_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiMACELLAZIONE", propOrder = {
    "macellazione"
})
public class ArrayOfRootDatiMACELLAZIONE {

    @XmlElement(name = "MACELLAZIONE")
    protected List<ArrayOfRootDatiMACELLAZIONE.MACELLAZIONE> macellazione;

    /**
     * Gets the value of the macellazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the macellazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMACELLAZIONE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiMACELLAZIONE.MACELLAZIONE }
     * 
     * 
     */
    public List<ArrayOfRootDatiMACELLAZIONE.MACELLAZIONE> getMACELLAZIONE() {
        if (macellazione == null) {
            macellazione = new ArrayList<ArrayOfRootDatiMACELLAZIONE.MACELLAZIONE>();
        }
        return this.macellazione;
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
     *         &lt;element name="P_CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="P_MACELLO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_FISC_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="P_PESO_CARCASSA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="P_NUM_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pcapoid",
        "pmacelloid",
        "pcodregione",
        "pcodmacello",
        "pcodfiscmacello",
        "pdtmacellazione",
        "ppesocarcassa",
        "pnummacellazione"
    })
    public static class MACELLAZIONE {

        @XmlElement(name = "P_CAPO_ID")
        protected BigDecimal pcapoid;
        @XmlElement(name = "P_MACELLO_ID")
        protected BigDecimal pmacelloid;
        @XmlElement(name = "P_COD_REGIONE")
        protected String pcodregione;
        @XmlElement(name = "P_COD_MACELLO")
        protected String pcodmacello;
        @XmlElement(name = "P_COD_FISC_MACELLO")
        protected String pcodfiscmacello;
        @XmlElement(name = "P_DT_MACELLAZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdtmacellazione;
        @XmlElement(name = "P_PESO_CARCASSA")
        protected BigDecimal ppesocarcassa;
        @XmlElement(name = "P_NUM_MACELLAZIONE")
        protected String pnummacellazione;

        /**
         * Gets the value of the pcapoid property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPCAPOID() {
            return pcapoid;
        }

        /**
         * Sets the value of the pcapoid property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPCAPOID(BigDecimal value) {
            this.pcapoid = value;
        }

        /**
         * Gets the value of the pmacelloid property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPMACELLOID() {
            return pmacelloid;
        }

        /**
         * Sets the value of the pmacelloid property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPMACELLOID(BigDecimal value) {
            this.pmacelloid = value;
        }

        /**
         * Gets the value of the pcodregione property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODREGIONE() {
            return pcodregione;
        }

        /**
         * Sets the value of the pcodregione property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODREGIONE(String value) {
            this.pcodregione = value;
        }

        /**
         * Gets the value of the pcodmacello property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODMACELLO() {
            return pcodmacello;
        }

        /**
         * Sets the value of the pcodmacello property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODMACELLO(String value) {
            this.pcodmacello = value;
        }

        /**
         * Gets the value of the pcodfiscmacello property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODFISCMACELLO() {
            return pcodfiscmacello;
        }

        /**
         * Sets the value of the pcodfiscmacello property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODFISCMACELLO(String value) {
            this.pcodfiscmacello = value;
        }

        /**
         * Gets the value of the pdtmacellazione property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDTMACELLAZIONE() {
            return pdtmacellazione;
        }

        /**
         * Sets the value of the pdtmacellazione property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDTMACELLAZIONE(XMLGregorianCalendar value) {
            this.pdtmacellazione = value;
        }

        /**
         * Gets the value of the ppesocarcassa property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPPESOCARCASSA() {
            return ppesocarcassa;
        }

        /**
         * Sets the value of the ppesocarcassa property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPPESOCARCASSA(BigDecimal value) {
            this.ppesocarcassa = value;
        }

        /**
         * Gets the value of the pnummacellazione property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPNUMMACELLAZIONE() {
            return pnummacellazione;
        }

        /**
         * Sets the value of the pnummacellazione property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPNUMMACELLAZIONE(String value) {
            this.pnummacellazione = value;
        }

    }

}
