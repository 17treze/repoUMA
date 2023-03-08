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
 * <p>Classe Java per ArrayOfRootDatiFASCICOLO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
         * Recupera il valore della proprietà pfascicolobdnid.
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
         * Imposta il valore della proprietà pfascicolobdnid.
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
         * Recupera il valore della proprietà pfascicoloageaid.
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
         * Imposta il valore della proprietà pfascicoloageaid.
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
         * Recupera il valore della proprietà pcuaa.
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
         * Imposta il valore della proprietà pcuaa.
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
         * Recupera il valore della proprietà pdtfascicolo.
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
         * Imposta il valore della proprietà pdtfascicolo.
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
