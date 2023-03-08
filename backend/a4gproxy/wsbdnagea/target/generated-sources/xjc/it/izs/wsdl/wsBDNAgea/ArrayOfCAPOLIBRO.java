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
 * <p>Classe Java per ArrayOfCAPO_LIBRO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCAPO_LIBRO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAPO_LIBRO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_CAPO_LIBRO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_CODICE_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_ASSOCIAZIONE_RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_LIBRO_GENEALOGICO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfCAPO_LIBRO", propOrder = {
    "capolibro"
})
public class ArrayOfCAPOLIBRO {

    @XmlElement(name = "CAPO_LIBRO")
    protected List<ArrayOfCAPOLIBRO.CAPOLIBRO> capolibro;

    /**
     * Gets the value of the capolibro property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capolibro property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAPOLIBRO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfCAPOLIBRO.CAPOLIBRO }
     * 
     * 
     */
    public List<ArrayOfCAPOLIBRO.CAPOLIBRO> getCAPOLIBRO() {
        if (capolibro == null) {
            capolibro = new ArrayList<ArrayOfCAPOLIBRO.CAPOLIBRO>();
        }
        return this.capolibro;
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
     *         &lt;element name="P_CAPO_LIBRO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_CODICE_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_ASSOCIAZIONE_RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_LIBRO_GENEALOGICO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pcapolibroid",
        "pcodicecapo",
        "pdtiscrizione",
        "pdtrevoca",
        "passociazionerazzacodice",
        "plibrogenealogicocodice"
    })
    public static class CAPOLIBRO {

        @XmlElement(name = "P_CAPO_LIBRO_ID", required = true)
        protected BigDecimal pcapolibroid;
        @XmlElement(name = "P_CODICE_CAPO")
        protected String pcodicecapo;
        @XmlElement(name = "P_DT_ISCRIZIONE")
        protected String pdtiscrizione;
        @XmlElement(name = "P_DT_REVOCA")
        protected String pdtrevoca;
        @XmlElement(name = "P_ASSOCIAZIONE_RAZZA_CODICE")
        protected String passociazionerazzacodice;
        @XmlElement(name = "P_LIBRO_GENEALOGICO_CODICE")
        protected String plibrogenealogicocodice;

        /**
         * Recupera il valore della proprietà pcapolibroid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPCAPOLIBROID() {
            return pcapolibroid;
        }

        /**
         * Imposta il valore della proprietà pcapolibroid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPCAPOLIBROID(BigDecimal value) {
            this.pcapolibroid = value;
        }

        /**
         * Recupera il valore della proprietà pcodicecapo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODICECAPO() {
            return pcodicecapo;
        }

        /**
         * Imposta il valore della proprietà pcodicecapo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODICECAPO(String value) {
            this.pcodicecapo = value;
        }

        /**
         * Recupera il valore della proprietà pdtiscrizione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTISCRIZIONE() {
            return pdtiscrizione;
        }

        /**
         * Imposta il valore della proprietà pdtiscrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTISCRIZIONE(String value) {
            this.pdtiscrizione = value;
        }

        /**
         * Recupera il valore della proprietà pdtrevoca.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTREVOCA() {
            return pdtrevoca;
        }

        /**
         * Imposta il valore della proprietà pdtrevoca.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTREVOCA(String value) {
            this.pdtrevoca = value;
        }

        /**
         * Recupera il valore della proprietà passociazionerazzacodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPASSOCIAZIONERAZZACODICE() {
            return passociazionerazzacodice;
        }

        /**
         * Imposta il valore della proprietà passociazionerazzacodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPASSOCIAZIONERAZZACODICE(String value) {
            this.passociazionerazzacodice = value;
        }

        /**
         * Recupera il valore della proprietà plibrogenealogicocodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPLIBROGENEALOGICOCODICE() {
            return plibrogenealogicocodice;
        }

        /**
         * Imposta il valore della proprietà plibrogenealogicocodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPLIBROGENEALOGICOCODICE(String value) {
            this.plibrogenealogicocodice = value;
        }

    }

}
