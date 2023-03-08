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
 * <p>Classe Java per ArrayOfRootDatiCAPIMAC complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCAPIMAC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAPIMAC" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_PESO_CARCASSA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCAPIMAC", propOrder = {
    "capimac"
})
public class ArrayOfRootDatiCAPIMAC {

    @XmlElement(name = "CAPIMAC")
    protected List<ArrayOfRootDatiCAPIMAC.CAPIMAC> capimac;

    /**
     * Gets the value of the capimac property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capimac property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAPIMAC().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCAPIMAC.CAPIMAC }
     * 
     * 
     */
    public List<ArrayOfRootDatiCAPIMAC.CAPIMAC> getCAPIMAC() {
        if (capimac == null) {
            capimac = new ArrayList<ArrayOfRootDatiCAPIMAC.CAPIMAC>();
        }
        return this.capimac;
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
     *         &lt;element name="P_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_PESO_CARCASSA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="P_AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pcodice",
        "psesso",
        "ppesocarcassa",
        "pdtnascita",
        "pdtmacellazione",
        "pallevid",
        "paziendacodice",
        "pallevidfiscale",
        "pspecodice",
        "pdtuscita"
    })
    public static class CAPIMAC {

        @XmlElement(name = "P_CAPO_ID")
        protected BigDecimal pcapoid;
        @XmlElement(name = "P_CODICE")
        protected String pcodice;
        @XmlElement(name = "P_SESSO")
        protected String psesso;
        @XmlElement(name = "P_PESO_CARCASSA")
        protected BigDecimal ppesocarcassa;
        @XmlElement(name = "P_DT_NASCITA")
        protected String pdtnascita;
        @XmlElement(name = "P_DT_MACELLAZIONE")
        protected String pdtmacellazione;
        @XmlElement(name = "P_ALLEV_ID")
        protected BigDecimal pallevid;
        @XmlElement(name = "P_AZIENDA_CODICE")
        protected String paziendacodice;
        @XmlElement(name = "P_ALLEV_ID_FISCALE")
        protected String pallevidfiscale;
        @XmlElement(name = "P_SPE_CODICE")
        protected String pspecodice;
        @XmlElement(name = "P_DT_USCITA")
        protected String pdtuscita;

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
         * Recupera il valore della proprietà pcodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODICE() {
            return pcodice;
        }

        /**
         * Imposta il valore della proprietà pcodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODICE(String value) {
            this.pcodice = value;
        }

        /**
         * Recupera il valore della proprietà psesso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPSESSO() {
            return psesso;
        }

        /**
         * Imposta il valore della proprietà psesso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPSESSO(String value) {
            this.psesso = value;
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
         * Recupera il valore della proprietà pdtnascita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTNASCITA() {
            return pdtnascita;
        }

        /**
         * Imposta il valore della proprietà pdtnascita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTNASCITA(String value) {
            this.pdtnascita = value;
        }

        /**
         * Recupera il valore della proprietà pdtmacellazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTMACELLAZIONE() {
            return pdtmacellazione;
        }

        /**
         * Imposta il valore della proprietà pdtmacellazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTMACELLAZIONE(String value) {
            this.pdtmacellazione = value;
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
         * Recupera il valore della proprietà pallevidfiscale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPALLEVIDFISCALE() {
            return pallevidfiscale;
        }

        /**
         * Imposta il valore della proprietà pallevidfiscale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPALLEVIDFISCALE(String value) {
            this.pallevidfiscale = value;
        }

        /**
         * Recupera il valore della proprietà pspecodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPSPECODICE() {
            return pspecodice;
        }

        /**
         * Imposta il valore della proprietà pspecodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPSPECODICE(String value) {
            this.pspecodice = value;
        }

        /**
         * Recupera il valore della proprietà pdtuscita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTUSCITA() {
            return pdtuscita;
        }

        /**
         * Imposta il valore della proprietà pdtuscita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTUSCITA(String value) {
            this.pdtuscita = value;
        }

    }

}
