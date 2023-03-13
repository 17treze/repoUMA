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
 * <p>Java class for ArrayOfRootDatiFASCICOLO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiFASCICOLO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FASCICOLO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_FASCICOLO_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_FASCICOLO_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_FASCICOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
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
@XmlType(name = "ArrayOfRootDatiFASCICOLO", propOrder = {
    "fascicolo"
})
public class ArrayOfRootDatiFASCICOLO {

    @XmlElement(name = "FASCICOLO")
    protected List<ArrayOfRootDatiFASCICOLO.FASCICOLO> fascicolo;

    /**
     * Gets the value of the fascicolo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fascicolo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFASCICOLO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiFASCICOLO.FASCICOLO }
     * 
     * 
     */
    public List<ArrayOfRootDatiFASCICOLO.FASCICOLO> getFASCICOLO() {
        if (fascicolo == null) {
            fascicolo = new ArrayList<ArrayOfRootDatiFASCICOLO.FASCICOLO>();
        }
        return this.fascicolo;
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
     *         &lt;element name="P_FASCICOLO_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_FASCICOLO_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_FASCICOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
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
        "pfascicolobdnid",
        "pfascicoloageaid",
        "pcuaa",
        "pdtfascicolo"
    })
    public static class FASCICOLO {

        @XmlElement(name = "P_FASCICOLO_BDN_ID", required = true)
        protected BigDecimal pfascicolobdnid;
        @XmlElement(name = "P_FASCICOLO_AGEA_ID")
        protected String pfascicoloageaid;
        @XmlElement(name = "P_CUAA")
        protected String pcuaa;
        @XmlElement(name = "P_DT_FASCICOLO", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdtfascicolo;

        /**
         * Gets the value of the pfascicolobdnid property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPFASCICOLOBDNID() {
            return pfascicolobdnid;
        }

        /**
         * Sets the value of the pfascicolobdnid property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPFASCICOLOBDNID(BigDecimal value) {
            this.pfascicolobdnid = value;
        }

        /**
         * Gets the value of the pfascicoloageaid property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPFASCICOLOAGEAID() {
            return pfascicoloageaid;
        }

        /**
         * Sets the value of the pfascicoloageaid property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPFASCICOLOAGEAID(String value) {
            this.pfascicoloageaid = value;
        }

        /**
         * Gets the value of the pcuaa property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCUAA() {
            return pcuaa;
        }

        /**
         * Sets the value of the pcuaa property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCUAA(String value) {
            this.pcuaa = value;
        }

        /**
         * Gets the value of the pdtfascicolo property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDTFASCICOLO() {
            return pdtfascicolo;
        }

        /**
         * Sets the value of the pdtfascicolo property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDTFASCICOLO(XMLGregorianCalendar value) {
            this.pdtfascicolo = value;
        }

    }

}
