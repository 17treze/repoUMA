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
 * <p>Classe Java per ArrayOfALLEVAMENTO_LIBRO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfALLEVAMENTO_LIBRO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ALLEVAMENTO_LIBRO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_ALLEV_LIBRO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_ID_FISCALE_ALLEV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_ASSOCIAZIONE_RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_CODICE_LGU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfALLEVAMENTO_LIBRO", propOrder = {
    "allevamentolibro"
})
public class ArrayOfALLEVAMENTOLIBRO {

    @XmlElement(name = "ALLEVAMENTO_LIBRO")
    protected List<ArrayOfALLEVAMENTOLIBRO.ALLEVAMENTOLIBRO> allevamentolibro;

    /**
     * Gets the value of the allevamentolibro property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the allevamentolibro property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALLEVAMENTOLIBRO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfALLEVAMENTOLIBRO.ALLEVAMENTOLIBRO }
     * 
     * 
     */
    public List<ArrayOfALLEVAMENTOLIBRO.ALLEVAMENTOLIBRO> getALLEVAMENTOLIBRO() {
        if (allevamentolibro == null) {
            allevamentolibro = new ArrayList<ArrayOfALLEVAMENTOLIBRO.ALLEVAMENTOLIBRO>();
        }
        return this.allevamentolibro;
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
     *         &lt;element name="P_ALLEV_LIBRO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_ID_FISCALE_ALLEV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_ASSOCIAZIONE_RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_CODICE_LGU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pallevlibroid",
        "pidfiscaleallev",
        "paziendacodice",
        "pspeciecodice",
        "pdtiscrizione",
        "pdtrevoca",
        "passociazionerazzacodice",
        "pcodicelgu"
    })
    public static class ALLEVAMENTOLIBRO {

        @XmlElement(name = "P_ALLEV_LIBRO_ID", required = true)
        protected BigDecimal pallevlibroid;
        @XmlElement(name = "P_ID_FISCALE_ALLEV")
        protected String pidfiscaleallev;
        @XmlElement(name = "P_AZIENDA_CODICE")
        protected String paziendacodice;
        @XmlElement(name = "P_SPECIE_CODICE")
        protected String pspeciecodice;
        @XmlElement(name = "P_DT_ISCRIZIONE")
        protected String pdtiscrizione;
        @XmlElement(name = "P_DT_REVOCA")
        protected String pdtrevoca;
        @XmlElement(name = "P_ASSOCIAZIONE_RAZZA_CODICE")
        protected String passociazionerazzacodice;
        @XmlElement(name = "P_CODICE_LGU")
        protected String pcodicelgu;

        /**
         * Recupera il valore della proprietà pallevlibroid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPALLEVLIBROID() {
            return pallevlibroid;
        }

        /**
         * Imposta il valore della proprietà pallevlibroid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPALLEVLIBROID(BigDecimal value) {
            this.pallevlibroid = value;
        }

        /**
         * Recupera il valore della proprietà pidfiscaleallev.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPIDFISCALEALLEV() {
            return pidfiscaleallev;
        }

        /**
         * Imposta il valore della proprietà pidfiscaleallev.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPIDFISCALEALLEV(String value) {
            this.pidfiscaleallev = value;
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
         * Recupera il valore della proprietà pspeciecodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPSPECIECODICE() {
            return pspeciecodice;
        }

        /**
         * Imposta il valore della proprietà pspeciecodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPSPECIECODICE(String value) {
            this.pspeciecodice = value;
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
         * Recupera il valore della proprietà pcodicelgu.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODICELGU() {
            return pcodicelgu;
        }

        /**
         * Imposta il valore della proprietà pcodicelgu.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODICELGU(String value) {
            this.pcodicelgu = value;
        }

    }

}
