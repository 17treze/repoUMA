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
 * <p>Classe Java per ArrayOfRootDatiIRREGOLARITA complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiIRREGOLARITA"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IRREGOLARITA" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_IRREGOLARITA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_ANNO_DOMANDA" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_DT_CALCOLO_ESITO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_ESITO_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_SANZIONE" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
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
@XmlType(name = "ArrayOfRootDatiIRREGOLARITA", propOrder = {
    "irregolarita"
})
public class ArrayOfRootDatiIRREGOLARITA {

    @XmlElement(name = "IRREGOLARITA")
    protected List<ArrayOfRootDatiIRREGOLARITA.IRREGOLARITA> irregolarita;

    /**
     * Gets the value of the irregolarita property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the irregolarita property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIRREGOLARITA().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiIRREGOLARITA.IRREGOLARITA }
     * 
     * 
     */
    public List<ArrayOfRootDatiIRREGOLARITA.IRREGOLARITA> getIRREGOLARITA() {
        if (irregolarita == null) {
            irregolarita = new ArrayList<ArrayOfRootDatiIRREGOLARITA.IRREGOLARITA>();
        }
        return this.irregolarita;
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
     *         &lt;element name="P_IRREGOLARITA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_ANNO_DOMANDA" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_DT_CALCOLO_ESITO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_ESITO_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_SANZIONE" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
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
        "pirregolaritaid",
        "pcuua",
        "pannodomanda",
        "pdtcalcoloesito",
        "pesitocontrollo",
        "psanzione"
    })
    public static class IRREGOLARITA {

        @XmlElement(name = "P_IRREGOLARITA_ID", required = true)
        protected BigDecimal pirregolaritaid;
        @XmlElement(name = "P_CUUA")
        protected String pcuua;
        @XmlElement(name = "P_ANNO_DOMANDA", required = true)
        protected BigDecimal pannodomanda;
        @XmlElement(name = "P_DT_CALCOLO_ESITO")
        protected String pdtcalcoloesito;
        @XmlElement(name = "P_ESITO_CONTROLLO")
        protected String pesitocontrollo;
        @XmlElement(name = "P_SANZIONE", required = true)
        protected BigDecimal psanzione;

        /**
         * Recupera il valore della proprietà pirregolaritaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPIRREGOLARITAID() {
            return pirregolaritaid;
        }

        /**
         * Imposta il valore della proprietà pirregolaritaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPIRREGOLARITAID(BigDecimal value) {
            this.pirregolaritaid = value;
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
         * Recupera il valore della proprietà pdtcalcoloesito.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTCALCOLOESITO() {
            return pdtcalcoloesito;
        }

        /**
         * Imposta il valore della proprietà pdtcalcoloesito.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTCALCOLOESITO(String value) {
            this.pdtcalcoloesito = value;
        }

        /**
         * Recupera il valore della proprietà pesitocontrollo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPESITOCONTROLLO() {
            return pesitocontrollo;
        }

        /**
         * Imposta il valore della proprietà pesitocontrollo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPESITOCONTROLLO(String value) {
            this.pesitocontrollo = value;
        }

        /**
         * Recupera il valore della proprietà psanzione.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPSANZIONE() {
            return psanzione;
        }

        /**
         * Imposta il valore della proprietà psanzione.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPSANZIONE(BigDecimal value) {
            this.psanzione = value;
        }

    }

}
