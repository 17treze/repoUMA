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
 * <p>Classe Java per ArrayOfRootDatiCAMPIONE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCAMPIONE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAMPIONE" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_CAMPIONE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_DOMANDA_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_TIPO_DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DOMANDA_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_DT_INS_CAMPIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCAMPIONE", propOrder = {
    "campione"
})
public class ArrayOfRootDatiCAMPIONE {

    @XmlElement(name = "CAMPIONE")
    protected List<ArrayOfRootDatiCAMPIONE.CAMPIONE> campione;

    /**
     * Gets the value of the campione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the campione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAMPIONE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCAMPIONE.CAMPIONE }
     * 
     * 
     */
    public List<ArrayOfRootDatiCAMPIONE.CAMPIONE> getCAMPIONE() {
        if (campione == null) {
            campione = new ArrayList<ArrayOfRootDatiCAMPIONE.CAMPIONE>();
        }
        return this.campione;
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
     *         &lt;element name="P_CAMPIONE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_DOMANDA_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_TIPO_DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DOMANDA_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_DT_INS_CAMPIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pcampioneid",
        "pdomandaageaid",
        "ptipodocumento",
        "pdomandabdnid",
        "pdtinscampione"
    })
    public static class CAMPIONE {

        @XmlElement(name = "P_CAMPIONE_ID", required = true)
        protected BigDecimal pcampioneid;
        @XmlElement(name = "P_DOMANDA_AGEA_ID")
        protected String pdomandaageaid;
        @XmlElement(name = "P_TIPO_DOCUMENTO")
        protected String ptipodocumento;
        @XmlElement(name = "P_DOMANDA_BDN_ID", required = true)
        protected BigDecimal pdomandabdnid;
        @XmlElement(name = "P_DT_INS_CAMPIONE")
        protected String pdtinscampione;

        /**
         * Recupera il valore della proprietà pcampioneid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPCAMPIONEID() {
            return pcampioneid;
        }

        /**
         * Imposta il valore della proprietà pcampioneid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPCAMPIONEID(BigDecimal value) {
            this.pcampioneid = value;
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
         * Recupera il valore della proprietà pdtinscampione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTINSCAMPIONE() {
            return pdtinscampione;
        }

        /**
         * Imposta il valore della proprietà pdtinscampione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTINSCAMPIONE(String value) {
            this.pdtinscampione = value;
        }

    }

}
