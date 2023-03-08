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
 * <p>Classe Java per ArrayOfRootDatiOVI_USCITE_PASCOLO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiOVI_USCITE_PASCOLO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OVI_USCITE_PASCOLO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CUAA_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ASL_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PAS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COMUNE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ASL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_USCITA_DA_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_OVINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DT_COM_AUTORITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="ESTREMI_MOD_ACCOMPAGNAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_MODELLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiOVI_USCITE_PASCOLO", propOrder = {
    "oviuscitepascolo"
})
public class ArrayOfRootDatiOVIUSCITEPASCOLO {

    @XmlElement(name = "OVI_USCITE_PASCOLO")
    protected List<ArrayOfRootDatiOVIUSCITEPASCOLO.OVIUSCITEPASCOLO> oviuscitepascolo;

    /**
     * Gets the value of the oviuscitepascolo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the oviuscitepascolo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOVIUSCITEPASCOLO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiOVIUSCITEPASCOLO.OVIUSCITEPASCOLO }
     * 
     * 
     */
    public List<ArrayOfRootDatiOVIUSCITEPASCOLO.OVIUSCITEPASCOLO> getOVIUSCITEPASCOLO() {
        if (oviuscitepascolo == null) {
            oviuscitepascolo = new ArrayList<ArrayOfRootDatiOVIUSCITEPASCOLO.OVIUSCITEPASCOLO>();
        }
        return this.oviuscitepascolo;
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
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CUAA_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ASL_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PAS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COMUNE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ASL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_USCITA_DA_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="NUM_OVINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DT_COM_AUTORITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="ESTREMI_MOD_ACCOMPAGNAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_MODELLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
        "allevid",
        "aziendacodice",
        "allevidfiscale",
        "specodice",
        "cuaadetentore",
        "aslazienda",
        "pasid",
        "codicepascolo",
        "comunepascolo",
        "aslpascolo",
        "dtuscitadapascolo",
        "numovini",
        "dtcomautorita",
        "estremimodaccompagnamento",
        "dtmodello"
    })
    public static class OVIUSCITEPASCOLO {

        @XmlElement(name = "ALLEV_ID", required = true)
        protected BigDecimal allevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "ALLEV_ID_FISCALE")
        protected String allevidfiscale;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "CUAA_DETENTORE")
        protected String cuaadetentore;
        @XmlElement(name = "ASL_AZIENDA")
        protected String aslazienda;
        @XmlElement(name = "PAS_ID")
        protected BigDecimal pasid;
        @XmlElement(name = "CODICE_PASCOLO")
        protected String codicepascolo;
        @XmlElement(name = "COMUNE_PASCOLO")
        protected String comunepascolo;
        @XmlElement(name = "ASL_PASCOLO")
        protected String aslpascolo;
        @XmlElement(name = "DT_USCITA_DA_PASCOLO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtuscitadapascolo;
        @XmlElement(name = "NUM_OVINI")
        protected BigDecimal numovini;
        @XmlElement(name = "DT_COM_AUTORITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtcomautorita;
        @XmlElement(name = "ESTREMI_MOD_ACCOMPAGNAMENTO")
        protected String estremimodaccompagnamento;
        @XmlElement(name = "DT_MODELLO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtmodello;

        /**
         * Recupera il valore della proprietà allevid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getALLEVID() {
            return allevid;
        }

        /**
         * Imposta il valore della proprietà allevid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setALLEVID(BigDecimal value) {
            this.allevid = value;
        }

        /**
         * Recupera il valore della proprietà aziendacodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAZIENDACODICE() {
            return aziendacodice;
        }

        /**
         * Imposta il valore della proprietà aziendacodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAZIENDACODICE(String value) {
            this.aziendacodice = value;
        }

        /**
         * Recupera il valore della proprietà allevidfiscale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getALLEVIDFISCALE() {
            return allevidfiscale;
        }

        /**
         * Imposta il valore della proprietà allevidfiscale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setALLEVIDFISCALE(String value) {
            this.allevidfiscale = value;
        }

        /**
         * Recupera il valore della proprietà specodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECODICE() {
            return specodice;
        }

        /**
         * Imposta il valore della proprietà specodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECODICE(String value) {
            this.specodice = value;
        }

        /**
         * Recupera il valore della proprietà cuaadetentore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCUAADETENTORE() {
            return cuaadetentore;
        }

        /**
         * Imposta il valore della proprietà cuaadetentore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCUAADETENTORE(String value) {
            this.cuaadetentore = value;
        }

        /**
         * Recupera il valore della proprietà aslazienda.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASLAZIENDA() {
            return aslazienda;
        }

        /**
         * Imposta il valore della proprietà aslazienda.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASLAZIENDA(String value) {
            this.aslazienda = value;
        }

        /**
         * Recupera il valore della proprietà pasid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPASID() {
            return pasid;
        }

        /**
         * Imposta il valore della proprietà pasid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPASID(BigDecimal value) {
            this.pasid = value;
        }

        /**
         * Recupera il valore della proprietà codicepascolo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEPASCOLO() {
            return codicepascolo;
        }

        /**
         * Imposta il valore della proprietà codicepascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEPASCOLO(String value) {
            this.codicepascolo = value;
        }

        /**
         * Recupera il valore della proprietà comunepascolo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCOMUNEPASCOLO() {
            return comunepascolo;
        }

        /**
         * Imposta il valore della proprietà comunepascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCOMUNEPASCOLO(String value) {
            this.comunepascolo = value;
        }

        /**
         * Recupera il valore della proprietà aslpascolo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASLPASCOLO() {
            return aslpascolo;
        }

        /**
         * Imposta il valore della proprietà aslpascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASLPASCOLO(String value) {
            this.aslpascolo = value;
        }

        /**
         * Recupera il valore della proprietà dtuscitadapascolo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTUSCITADAPASCOLO() {
            return dtuscitadapascolo;
        }

        /**
         * Imposta il valore della proprietà dtuscitadapascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTUSCITADAPASCOLO(XMLGregorianCalendar value) {
            this.dtuscitadapascolo = value;
        }

        /**
         * Recupera il valore della proprietà numovini.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMOVINI() {
            return numovini;
        }

        /**
         * Imposta il valore della proprietà numovini.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMOVINI(BigDecimal value) {
            this.numovini = value;
        }

        /**
         * Recupera il valore della proprietà dtcomautorita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTCOMAUTORITA() {
            return dtcomautorita;
        }

        /**
         * Imposta il valore della proprietà dtcomautorita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTCOMAUTORITA(XMLGregorianCalendar value) {
            this.dtcomautorita = value;
        }

        /**
         * Recupera il valore della proprietà estremimodaccompagnamento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getESTREMIMODACCOMPAGNAMENTO() {
            return estremimodaccompagnamento;
        }

        /**
         * Imposta il valore della proprietà estremimodaccompagnamento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setESTREMIMODACCOMPAGNAMENTO(String value) {
            this.estremimodaccompagnamento = value;
        }

        /**
         * Recupera il valore della proprietà dtmodello.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTMODELLO() {
            return dtmodello;
        }

        /**
         * Imposta il valore della proprietà dtmodello.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTMODELLO(XMLGregorianCalendar value) {
            this.dtmodello = value;
        }

    }

}
