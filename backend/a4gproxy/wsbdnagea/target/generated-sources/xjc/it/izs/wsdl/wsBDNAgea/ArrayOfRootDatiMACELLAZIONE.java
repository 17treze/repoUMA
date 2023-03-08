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
 * <p>Classe Java per ArrayOfRootDatiMACELLAZIONE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
         * Recupera il valore della proprietà pcapoid.
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
         * Imposta il valore della proprietà pcapoid.
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
         * Recupera il valore della proprietà pmacelloid.
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
         * Imposta il valore della proprietà pmacelloid.
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
         * Recupera il valore della proprietà pcodregione.
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
         * Imposta il valore della proprietà pcodregione.
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
         * Recupera il valore della proprietà pcodmacello.
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
         * Imposta il valore della proprietà pcodmacello.
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
         * Recupera il valore della proprietà pcodfiscmacello.
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
         * Imposta il valore della proprietà pcodfiscmacello.
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
         * Recupera il valore della proprietà pdtmacellazione.
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
         * Imposta il valore della proprietà pdtmacellazione.
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
         * Recupera il valore della proprietà ppesocarcassa.
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
         * Imposta il valore della proprietà ppesocarcassa.
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
         * Recupera il valore della proprietà pnummacellazione.
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
         * Imposta il valore della proprietà pnummacellazione.
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
